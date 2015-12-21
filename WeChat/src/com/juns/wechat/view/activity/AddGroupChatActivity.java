package com.juns.wechat.view.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DensityUtil;
import com.juns.health.net.loopj.android.http.RequestParams;
import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.bean.GroupInfo;
import com.juns.wechat.bean.User;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.chat.utils.Constant;
import com.juns.wechat.common.PingYinUtil;
import com.juns.wechat.common.PinyinComparator;
import com.juns.wechat.common.Utils;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.net.BaseJsonRes;
import com.juns.wechat.view.BaseActivity;
import com.juns.wechat.widght.SideBar;

public class AddGroupChatActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private ImageView iv_search, img_back;
	private TextView tv_header, txt_title, txt_right;;
	private ListView listView;
	private EditText et_search;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private ContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<String> exitingMembers = new ArrayList<String>();
	private List<User> alluserList;// 好友列表
	// 可滑动的显示选中用户的View
	private LinearLayout menuLinerLayout;

	// 选中用户总数,右上角显示
	int total = 0;
	private String userId = null;
	private String groupId = null;
	private String groupname;
	// 添加的列表
	private List<String> addList = new ArrayList<String>();
	private String hxid;
	private EMGroup group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_chatroom);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWindowManager.removeView(mDialogText);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("发起群聊");
		txt_right = (TextView) this.findViewById(R.id.txt_right);
		txt_right.setText("确定");
		txt_right.setTextColor(0xFF45C01A);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		menuLinerLayout = (LinearLayout) this
				.findViewById(R.id.linearLayoutMenu);
		et_search = (EditText) this.findViewById(R.id.et_search);
		listView = (ListView) findViewById(R.id.list);
		iv_search = (ImageView) this.findViewById(R.id.iv_search);
		mDialogText = (TextView) LayoutInflater.from(this).inflate(
				R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		indexBar = (SideBar) findViewById(R.id.sideBar);
		indexBar.setListView(listView);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View headerView = layoutInflater.inflate(R.layout.item_chatroom_header,
				null);
		tv_header = (TextView) headerView.findViewById(R.id.tv_header);
		listView.addHeaderView(headerView);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void initView() {
		hxid = Utils.getValue(AddGroupChatActivity.this, Constants.User_ID);
		groupId = getIntent().getStringExtra(Constants.GROUP_ID);
		userId = getIntent().getStringExtra(Constants.User_ID);
		if (groupId != null) {
			isCreatingNewGroup = false;
			group = EMGroupManager.getInstance().getGroup(groupId);
			if (group != null) {
				exitingMembers = group.getMembers();
				groupname = group.getGroupName();
			}
		} else if (userId != null) {
			isCreatingNewGroup = true;
			exitingMembers.add(userId);
			total = 1;
			addList.add(userId);
		} else {
			isCreatingNewGroup = true;
		}
	}

	@Override
	protected void initData() {
		// 获取好友列表
		alluserList = new ArrayList<User>();
		for (User user : GloableParams.UserInfos) {
			if (!user.getUserName().equals(Constant.NEW_FRIENDS_USERNAME)
					& !user.getUserName().equals(Constant.GROUP_USERNAME))
				alluserList.add(user);
		}
		contactAdapter = new ContactAdapter(AddGroupChatActivity.this,
				alluserList);
		listView.setAdapter(contactAdapter);
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
		tv_header.setOnClickListener(this);
		txt_right.setOnClickListener(this);
		et_search.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					String str_s = et_search.getText().toString().trim();
					List<User> users_temp = new ArrayList<User>();
					for (User user : alluserList) {
						String usernick = user.getUserName();
						if (usernick.contains(str_s)) {
							users_temp.add(user);
						}
						contactAdapter = new ContactAdapter(
								AddGroupChatActivity.this, users_temp);
						listView.setAdapter(contactAdapter);
					}
				} else {
					contactAdapter = new ContactAdapter(
							AddGroupChatActivity.this, alluserList);
					listView.setAdapter(contactAdapter);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(AddGroupChatActivity.this);
			break;
		case R.id.tv_header:
			// TODO 打开群列表
			break;
		case R.id.txt_right:// 确定按钮
			save();
			break;
		default:
			break;
		}
	}

	/**
	 * 确认选择的members
	 * 
	 * @param v
	 */
	public void save() {
		if (addList.size() == 0) {
			Utils.showLongToast(AddGroupChatActivity.this, "请选择用户");
			return;
		}
		// 如果只有一个用户说明只是单聊,并且不是从群组加人
		if (addList.size() == 1 && isCreatingNewGroup) {
			String userId = addList.get(0);
			User user = GloableParams.Users.get(userId);
			Intent intent = new Intent(AddGroupChatActivity.this,
					ChatActivity.class);
			intent.putExtra(Constants.NAME, user.getUserName());
			intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
			intent.putExtra(Constants.User_ID, user.getTelephone());
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		} else {
			if (isCreatingNewGroup) {
				getLoadingDialog("正在创建群聊...").show();
			} else {
				getLoadingDialog("正在加人...").show();
			}
			creatNewGroup(addList);// 创建群组
		}
	}

	class ContactAdapter extends BaseAdapter implements SectionIndexer {
		private Context mContext;
		private boolean[] isCheckedArray;
		private Bitmap[] bitmaps;
		private List<User> list = new ArrayList<User>();

		@SuppressWarnings("unchecked")
		public ContactAdapter(Context mContext, List<User> users) {
			this.mContext = mContext;
			this.list = users;
			bitmaps = new Bitmap[list.size()];
			isCheckedArray = new boolean[list.size()];
			// 排序(实现了中英文混排)
			Collections.sort(list, new PinyinComparator());
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final User user = list.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.contact_item, null);
			}
			ImageView ivAvatar = ViewHolder.get(convertView,
					R.id.contactitem_avatar_iv);
			TextView tvCatalog = ViewHolder.get(convertView,
					R.id.contactitem_catalog);
			TextView tvNick = ViewHolder
					.get(convertView, R.id.contactitem_nick);
			final CheckBox checkBox = ViewHolder
					.get(convertView, R.id.checkbox);
			checkBox.setVisibility(View.VISIBLE);
			String catalog = "";
			if (TextUtils.isEmpty(user.getUserName()))
				catalog = "#";
			else
				catalog = PingYinUtil.converterToFirstSpell(user.getUserName())
						.substring(0, 1);
			if (position == 0) {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(catalog);
			} else {
				String lastCatalog = "";
				User Nextuser = list.get(position - 1);
				if (TextUtils.isEmpty(Nextuser.getUserName()))
					lastCatalog = "#";
				else
					lastCatalog = PingYinUtil.converterToFirstSpell(
							Nextuser.getUserName()).substring(0, 1);
				if (catalog.equals(lastCatalog)) {
					tvCatalog.setVisibility(View.GONE);
				} else {
					tvCatalog.setVisibility(View.VISIBLE);
					tvCatalog.setText(catalog);
				}
			}
			ivAvatar.setImageResource(R.drawable.head);
			tvNick.setText(user.getUserName());
			if (exitingMembers != null
					&& exitingMembers.contains(user.getTelephone())) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}
			if (addList != null && addList.contains(user.getTelephone())) {
				checkBox.setChecked(true);
				isCheckedArray[position] = true;
			}else {
				checkBox.setChecked(false);
			}
			if (checkBox != null) {
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 群组中原来的成员一直设为选中状态
						if (exitingMembers.contains(user.getTelephone())) {
							isChecked = true;
							checkBox.setChecked(true);
						}else {
							checkBox.setChecked(false);
						}
						isCheckedArray[position] = isChecked;
						// 如果是单选模式
						if (isSignleChecked && isChecked) {
							for (int i = 0; i < isCheckedArray.length; i++) {
								if (i != position) {
									isCheckedArray[i] = false;
								}
							}
							contactAdapter.notifyDataSetChanged();
						}

						if (isChecked) {
							// 选中用户显示在滑动栏显示
							showCheckImage(null, list.get(position));
						} else {
							// 用户显示在滑动栏删除
							deleteImage(list.get(position));
						}
					}
				});
				// 群组中原来的成员一直设为选中状态
				if (exitingMembers.contains(user.getTelephone())) {
					checkBox.setChecked(true);
					isCheckedArray[position] = true;
				} else {
					checkBox.setChecked(isCheckedArray[position]);
				}

			}
			return convertView;
		}

		@Override
		public int getPositionForSection(int section) {
			for (int i = 0; i < list.size(); i++) {
				User user = list.get(i);
				String catalog = "";
				if (TextUtils.isEmpty(user.getUserName()))
					catalog = "#";
				else
					catalog = PingYinUtil.converterToFirstSpell(
							user.getUserName()).substring(0, 1);
				char firstChar = catalog.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			return null;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
		checkBox.toggle();
	}

	// 即时显示被选中用户的头像和昵称。
	private void showCheckImage(Bitmap bitmap, User glufineid) {
		if (exitingMembers.contains(glufineid.getUserName()) && groupId != null) {
			return;
		}
		if (addList.contains(glufineid.getTelephone())) {
			return;
		}
		total++;

		final ImageView imageView = new ImageView(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				DensityUtil.dip2px(this, 40), DensityUtil.dip2px(this, 40));
		lp.setMargins(0, 0, DensityUtil.dip2px(this, 5), 0);
		imageView.setLayoutParams(lp);

		// 设置id，方便后面删除
		imageView.setTag(glufineid);
		if (bitmap == null) {
			imageView.setImageResource(R.drawable.head);
		} else {
			imageView.setImageBitmap(bitmap);
		}

		menuLinerLayout.addView(imageView);
		txt_right.setText("确定(" + total + ")");
		if (total > 0) {
			if (iv_search.getVisibility() == View.VISIBLE) {
				iv_search.setVisibility(View.GONE);
			}
		}
		addList.add(glufineid.getTelephone());
	}

	private void deleteImage(User glufineid) {
		View view = (View) menuLinerLayout.findViewWithTag(glufineid);

		menuLinerLayout.removeView(view);
		total--;
		txt_right.setText("确定(" + total + ")");
		addList.remove(glufineid.getTelephone());
		if (total < 1) {
			if (iv_search.getVisibility() == View.GONE) {
				iv_search.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 创建新群组
	 * 
	 * @param newmembers
	 */
	String groupName = "";
	String manber = "";

	private void creatNewGroup(final List<String> members) {
		// TODO 请求服务器创建群组，服务端实现接口
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 调用sdk创建群组方法
				try {
					final String[] strmembers = new String[members.size()];
					for (int i = 0; i < members.size(); i++) {
						User user = GloableParams.Users.get(members.get(i));
						if (user != null) {
							if (i < 3) {
								if (i == 0)
									groupName = user.getUserName();
								else
									groupName += "、" + user.getUserName();
							} else if (i == 4) {
								groupName += "...";
							}
							strmembers[i] = user.getTelephone();
							if (i == 0) {
								manber = user.getTelephone();
							} else {
								manber += "、" + user.getTelephone();
							}
						}
					}
					final EMGroup group = EMGroupManager.getInstance()
							.createPublicGroup(groupName, "", strmembers, true);
					runOnUiThread(new Runnable() {
						public void run() {
							if (group != null) {
								// TODO 保存本地数据库
								GloableParams.GroupInfos = new HashMap<String, GroupInfo>();
								GroupInfo info = new GroupInfo();
								info.setGroup_id(group.getGroupId());
								info.setGroup_name(groupName);
								info.setMembers(manber);
								String owner_id = Utils.getValue(
										AddGroupChatActivity.this,
										Constants.User_ID);
								info.setOwner_id(owner_id);
								GloableParams.GroupInfos.put(
										group.getGroupId(), info);
								FinalDb db = FinalDb.create(
										AddGroupChatActivity.this,
										Constants.DB_NAME, false);
								db.save(info);
								addServieGroup(group.getGroupId(), groupName,
										manber, owner_id);// 保存服务器

							}
						}

					});
				} catch (final EaseMobException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Utils.showLongToast(AddGroupChatActivity.this,
									"创建失败");
							getLoadingDialog("正在创建群聊...").dismiss();
						}
					});
				}

			}
		}).start();
	}

	private void addServieGroup(final String groupId, final String groupName,
			String manber, String owner_id) {
		RequestParams params = new RequestParams();
		params.put("group_id", groupId);
		params.put("group_name", groupName);
		params.put("owner_id", owner_id);
		params.put("members", manber);
		params.put("description", "");
		params.put("image_path", "");
		netClient.post(Constants.newGroupURL, params, new BaseJsonRes() {

			@Override
			public void onMySuccess(String data) {
				Intent intent = new Intent(AddGroupChatActivity.this,
						ChatActivity.class);
				intent.putExtra(Constants.NAME, groupName);
				intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_GROUP);
				intent.putExtra(Constants.GROUP_ID, groupId);
				startActivity(intent);
				getLoadingDialog("正在创建群聊...").dismiss();
				finish();
			}

			@Override
			public void onMyFailure() {

			}
		});
	}
}
