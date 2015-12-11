package com.juns.wechat.view.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.adpter.NewMsgAdpter;
import com.juns.wechat.bean.GroupInfo;
import com.juns.wechat.bean.PublicMsgInfo;
import com.juns.wechat.bean.User;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.common.NetUtil;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.activity.PublishMsgListActivity;

//消息
public class Fragment_Msg extends Fragment implements OnClickListener,
		OnItemClickListener {
	private Activity ctx;
	private View layout, layout_head;;
	public RelativeLayout errorItem;
	public TextView errorText;
	private ListView lvContact;
	private NewMsgAdpter adpter;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();
	private MainActivity parentActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			parentActivity = (MainActivity) getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_msg,
					null);
			lvContact = (ListView) layout.findViewById(R.id.listview);
			errorItem = (RelativeLayout) layout
					.findViewById(R.id.rl_error_item);
			errorText = (TextView) errorItem
					.findViewById(R.id.tv_connect_errormsg);
			setOnListener();
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	@Override
	public void onResume() {
		super.onResume();
		conversationList.clear();
		initViews();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		initViews();
	}

	private void initViews() {
		conversationList.addAll(loadConversationsWithRecentChat());
		if (conversationList != null && conversationList.size() > 0) {
			layout.findViewById(R.id.txt_nochat).setVisibility(View.GONE);
			adpter = new NewMsgAdpter(getActivity(), conversationList);
			// TODO 加载订阅号信息 ，增加一个Item
			// if (GloableParams.isHasPulicMsg) {
			EMConversation nee = new EMConversation("100000");
			conversationList.add(0, nee);
			String time = Utils.getValue(getActivity(), "Time");
			String content = Utils.getValue(getActivity(), "Content");
			time = "下午 02:45";
			content = "[腾讯娱乐]《炉石传说》荣列中国区App Store年度精选";
			PublicMsgInfo msgInfo = new PublicMsgInfo();
			msgInfo.setContent(content);
			msgInfo.setMsg_ID("12");
			msgInfo.setTime(time);
			adpter.setPublicMsg(msgInfo);
			// }
			lvContact.setAdapter(adpter);
		} else {
			layout.findViewById(R.id.txt_nochat).setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		List<EMConversation> list = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				list.add(conversation);
		}
		// 排序
		sortConversationByLastChatTime(list);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1,
					final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	private void setOnListener() {
		lvContact.setOnItemClickListener(this);
		errorItem.setOnClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (adpter.PublicMsg != null && position == 0) {
			// 打开订阅号列表页面
			Utils.start_Activity(getActivity(), PublishMsgListActivity.class);
		} else {
			((MainActivity) getActivity()).updateUnreadLabel();
			EMConversation conversation = conversationList.get(position);
			Intent intent = new Intent(getActivity(), ChatActivity.class);
			Hashtable<String, String> ChatRecord = adpter.getChatRecord();
			if (ChatRecord != null) {
				if (conversation.isGroup()) {
					GroupInfo info = GloableParams.GroupInfos.get(conversation
							.getUserName());
					if (info != null) {
						intent.putExtra(Constants.TYPE,
								ChatActivity.CHATTYPE_GROUP);
						intent.putExtra(Constants.GROUP_ID, info.getGroup_id());
						intent.putExtra(Constants.NAME, info.getGroup_name());// 设置标题
						getActivity().startActivity(intent);
					} else {
						intent.putExtra(Constants.TYPE,
								ChatActivity.CHATTYPE_GROUP);
						intent.putExtra(Constants.GROUP_ID, info.getGroup_id());
						intent.putExtra(Constants.NAME, R.string.group_chats);// 设置标题
						getActivity().startActivity(intent);
					}
				} else {
					User user = GloableParams.Users.get(conversation
							.getUserName());
					if (user != null) {
						intent.putExtra(Constants.NAME, user.getUserName());// 设置昵称
						intent.putExtra(Constants.TYPE,
								ChatActivity.CHATTYPE_SINGLE);
						intent.putExtra(Constants.User_ID,
								conversation.getUserName());
						getActivity().startActivity(intent);
					} else {
						intent.putExtra(Constants.NAME, "好友");
						intent.putExtra(Constants.TYPE,
								ChatActivity.CHATTYPE_SINGLE);
						intent.putExtra(Constants.User_ID,
								conversation.getUserName());
						getActivity().startActivity(intent);
					}
				}
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_error_item:
			NetUtil.openSetNetWork(getActivity());
			break;
		default:
			break;
		}
	}

}
