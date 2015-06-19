package com.juns.wechat.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.NetUtils;
import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.bean.GroupInfo;
import com.juns.wechat.bean.User;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.chat.widght.ExpandGridView;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//群设置
public class GroupSettingActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_back;
	private TextView tv_groupname;
	private TextView txt_title;// 标题，成员总数
	int m_total = 0;// 成员总数
	private ExpandGridView gridview;// 成员列表
	// 修改群名称、置顶、、、、
	private RelativeLayout re_change_groupname;
	private RelativeLayout rl_switch_chattotop;
	private RelativeLayout rl_switch_block_groupmsg;
	private RelativeLayout re_clear;

	// 状态变化
	private CheckBox check_top, check_closetip;
	// 删除并退出

	private Button exitBtn;
	private String hxid;
	private String group_name;// 群名称
	boolean is_admin = false;// 是否是管理员
	List<User> members = new ArrayList<User>();
	String longClickUsername = null;

	private String groupId;
	private EMGroup group;
	private GridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_groupsetting);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("聊天信息");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		tv_groupname = (TextView) findViewById(R.id.txt_groupname);
		gridview = (ExpandGridView) findViewById(R.id.gridview);

		re_change_groupname = (RelativeLayout) findViewById(R.id.re_change_groupname);
		rl_switch_chattotop = (RelativeLayout) findViewById(R.id.rl_switch_chattotop);
		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
		re_clear = (RelativeLayout) findViewById(R.id.re_clear);

		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
	}

	@Override
	protected void initView() {
		hxid = Utils.getValue(GroupSettingActivity.this, Constants.User_ID);
	}

	@Override
	protected void initData() {
		// 获取传过来的groupid
		groupId = getIntent().getStringExtra(Constants.GROUP_ID);
		// 获取本地该群数据
		group = EMGroupManager.getInstance().getGroup(groupId);
		if (group == null) {
			try {
				// 去网络中查找该群
				group = EMGroupManager.getInstance()
						.getGroupFromServer(groupId);
				if (group == null) {
					Toast.makeText(GroupSettingActivity.this, "该群已经被解散...",
							Toast.LENGTH_SHORT).show();
					setResult(100);
					finish();
					return;
				}
			} catch (EaseMobException e) {
				e.printStackTrace();
				return;
			}
		}

		// 获取封装的群名（里面封装了显示的群名和群组成员的信息）
		String group_name = group.getGroupName();
		// 获取群成员信息
		tv_groupname.setText(group_name);

		GroupInfo groupinfo = GloableParams.GroupInfos.get(groupId);
		if (groupinfo != null) {
			String[] manbers = groupinfo.getMembers().split("、");
			if (manbers != null && manbers.length > 0) {
				m_total = manbers.length;
				txt_title.setText("聊天信息(" + String.valueOf(m_total) + ")");
				// 解析群组成员信息
				for (int i = 0; i < m_total; i++) {
					User user = GloableParams.Users.get(manbers[i]);
					if (user == null) {
						user = new User();
						user.setTelephone(manbers[i]);
					}
					members.add(user);
				}
				// 显示群组成员头像和昵称
				showMembers(members);
				// 判断是否是群主，是群主有删成员的权限，并显示减号按钮
				if (null != groupinfo.getOwner_id() && null != hxid
						&& hxid.equals(groupinfo.getOwner_id())) {
					is_admin = true;
				}
			}
		}
	}

	// 显示群成员头像昵称的gridview
	private void showMembers(List<User> members) {
		adapter = new GridAdapter(this, members);
		gridview.setAdapter(adapter);

		// 设置OnTouchListener,为了让群主方便地推出删除模》
		gridview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (adapter.isInDeleteMode) {
						adapter.isInDeleteMode = false;
						adapter.notifyDataSetChanged();
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	@Override
	protected void setListener() {
		re_change_groupname.setOnClickListener(this);
		rl_switch_chattotop.setOnClickListener(this);
		rl_switch_block_groupmsg.setOnClickListener(this);
		re_clear.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		img_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(GroupSettingActivity.this);
			break;
		case R.id.btn_exit_grp:
			// deleteMembersFromGroup(hxid);
			break;
		default:
			break;
		}
	}

	// 群组成员gridadapter
	private class GridAdapter extends BaseAdapter {

		public boolean isInDeleteMode;
		private List<User> objects;
		Context context;

		public GridAdapter(Context context, List<User> objects) {

			this.objects = objects;
			this.context = context;
			isInDeleteMode = false;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_chatsetting_gridview, null);
			}
			ImageView iv_avatar = (ImageView) convertView
					.findViewById(R.id.iv_avatar);
			TextView tv_username = (TextView) convertView
					.findViewById(R.id.tv_username);
			ImageView badge_delete = (ImageView) convertView
					.findViewById(R.id.badge_delete);

			// 最后一个item，减人按钮
			if (position == getCount() - 1 && is_admin) {
				tv_username.setText("");
				badge_delete.setVisibility(View.GONE);
				iv_avatar.setImageResource(R.drawable.icon_btn_deleteperson);

				if (isInDeleteMode) {
					// 正处于删除模式下，隐藏删除按钮
					convertView.setVisibility(View.GONE);
				} else {

					convertView.setVisibility(View.VISIBLE);
				}

				iv_avatar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						isInDeleteMode = true;
						notifyDataSetChanged();
					}
				});

			} else if ((is_admin && position == getCount() - 2)
					|| (!is_admin && position == getCount() - 1)) { // 添加群组成员按钮
				tv_username.setText("");
				badge_delete.setVisibility(View.GONE);
				iv_avatar.setImageResource(R.drawable.jy_drltsz_btn_addperson);
				// 正处于删除模式下,隐藏添加按钮
				if (isInDeleteMode) {
					convertView.setVisibility(View.GONE);
				} else {
					convertView.setVisibility(View.VISIBLE);
				}
				iv_avatar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 进入选人页面
						startActivity((new Intent(GroupSettingActivity.this,
								AddGroupChatActivity.class).putExtra(
								Constants.GROUP_ID, groupId)));
					}
				});
			}

			else { // 普通item，显示群组成员
				final User user = GloableParams.UserInfos.get(position);
				String usernick = user.getUserName();
				final String userhid = user.getTelephone();
				final String useravatar = user.getHeadUrl();
				tv_username.setText(usernick);
				iv_avatar.setImageResource(R.drawable.head);// TODO 网络加载头像
				iv_avatar.setTag(useravatar);
				if (isInDeleteMode) {
					// 如果是删除模式下，显示减人图标
					convertView.findViewById(R.id.badge_delete).setVisibility(
							View.VISIBLE);
				} else {
					convertView.findViewById(R.id.badge_delete).setVisibility(
							View.INVISIBLE);
				}
				iv_avatar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isInDeleteMode) {
							// 如果是删除自己，return
							if (EMChatManager.getInstance().getCurrentUser()
									.equals(userhid)) {
								Utils.showLongToast(GroupSettingActivity.this,
										"不能删除自己");
								return;
							}
							if (!NetUtils.hasNetwork(getApplicationContext())) {
								Toast.makeText(
										getApplicationContext(),
										getString(R.string.network_unavailable),
										Toast.LENGTH_SHORT).show();
								return;
							}
							// deleteMembersFromGroup(userhid);//TODO
						} else {
							// 正常情况下点击user，可以进入用户详情或者聊天页面等等
							Intent intent = new Intent(
									GroupSettingActivity.this,
									ChatActivity.class);
							intent.putExtra(Constants.NAME, user.getUserName());
							intent.putExtra(Constants.TYPE,
									ChatActivity.CHATTYPE_SINGLE);
							intent.putExtra(Constants.User_ID,
									user.getTelephone());
							startActivity(intent);
							overridePendingTransition(R.anim.push_left_in,
									R.anim.push_left_out);
						}
					}
				});
			}
			return convertView;
		}

		@Override
		public int getCount() {
			if (is_admin) {
				return objects.size() + 2;
			} else {

				return objects.size() + 1;
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return objects.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	}
}
