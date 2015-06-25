package com.juns.wechat.adpter;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.bean.GroupInfo;
import com.juns.wechat.bean.PublicMsgInfo;
import com.juns.wechat.bean.User;
import com.juns.wechat.chat.utils.Constant;
import com.juns.wechat.chat.utils.SmileUtils;
import com.juns.wechat.common.UserUtils;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.dialog.WarnTipDialog;
import com.juns.wechat.net.NetClient;
import com.juns.wechat.widght.swipe.SwipeLayout;

public class NewMsgAdpter extends BaseAdapter {
	protected Context context;
	private List<EMConversation> conversationList;
	private WarnTipDialog Tipdialog;
	private int deleteID;
	private String ChatID;
	private NetClient netClient;
	private String userid;
	private Hashtable<String, String> ChatRecord = new Hashtable<String, String>();
	public PublicMsgInfo PublicMsg = null;

	public NewMsgAdpter(Context ctx, List<EMConversation> objects) {
		context = ctx;
		conversationList = objects;
		netClient = new NetClient(ctx);
		userid = UserUtils.getUserID(context);
	}

	public void setPublicMsg(PublicMsgInfo Msg) {
		PublicMsg = Msg;
	}

	public PublicMsgInfo getPublicMsg() {
		return PublicMsg;
	}

	public Hashtable<String, String> getChatRecord() {
		return ChatRecord;
	}

	@Override
	public int getCount() {
		return conversationList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_item_msg, parent, false);
		}
		ImageView img_avar = ViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
		TextView txt_state = ViewHolder.get(convertView, R.id.txt_state);
		TextView txt_del = ViewHolder.get(convertView, R.id.txt_del);
		TextView txt_content = ViewHolder.get(convertView, R.id.txt_content);
		TextView txt_time = ViewHolder.get(convertView, R.id.txt_time);
		TextView unreadLabel = ViewHolder.get(convertView,
				R.id.unread_msg_number);
		SwipeLayout swipe = ViewHolder.get(convertView, R.id.swipe);
		if (PublicMsg != null && position == 0) {
			txt_name.setText("订阅号");
			img_avar.setImageResource(R.drawable.icon_public);
			txt_time.setText(PublicMsg.getTime());
			txt_content.setText(PublicMsg.getContent());
			unreadLabel.setText("3");
			unreadLabel.setVisibility(View.VISIBLE);
			swipe.setSwipeEnabled(false);
		} else {
			swipe.setSwipeEnabled(true);
			// 获取与此用户/群组的会话
			final EMConversation conversation = conversationList.get(position);
			// 获取用户username或者群组groupid
			ChatID = conversation.getUserName();
			txt_del.setTag(ChatID);
			if (conversation.isGroup()) {
				img_avar.setImageResource(R.drawable.defult_group);
				GroupInfo info = GloableParams.GroupInfos.get(ChatID);
				if (info != null) {
					txt_name.setText(info.getGroup_name());
				} else {
					// initGroupInfo(img_avar, txt_name);// 获取群组信息
				}
			} else {
				User user = GloableParams.Users.get(ChatID);
				if (user != null) {
					txt_name.setText(user.getUserName());
				} else {
					txt_name.setText("好友");
					UserUtils.initUserInfo(context, ChatID, img_avar, txt_name);// 获取用户信息
				}
			}
			if (conversation.getUnreadMsgCount() > 0) {
				// 显示与此用户的消息未读数
				unreadLabel.setText(String.valueOf(conversation
						.getUnreadMsgCount()));
				unreadLabel.setVisibility(View.VISIBLE);
			} else {
				unreadLabel.setVisibility(View.INVISIBLE);
			}
			if (conversation.getMsgCount() != 0) {
				// 把最后一条消息的内容作为item的message内容
				EMMessage lastMessage = conversation.getLastMessage();
				txt_content.setText(
						SmileUtils.getSmiledText(context,
								getMessageDigest(lastMessage, context)),
						BufferType.SPANNABLE);
				txt_time.setText(DateUtils.getTimestampString(new Date(
						lastMessage.getMsgTime())));
				if (lastMessage.status == EMMessage.Status.SUCCESS) {
					txt_state.setText("送达");
					// txt_state.setBackgroundResource(R.drawable.btn_bg_orgen);
				} else if (lastMessage.status == EMMessage.Status.FAIL) {
					txt_state.setText("失败");
					// txt_state.setBackgroundResource(R.drawable.btn_bg_red);
				} else if (lastMessage.direct == EMMessage.Direct.RECEIVE) {
					txt_state.setText("已读");
					txt_state.setBackgroundResource(R.drawable.btn_bg_blue);
				}
			}

			txt_del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteID = position;
					Tipdialog = new WarnTipDialog((Activity) context,
							"您确定要删除该聊天吗？");
					Tipdialog.setBtnOkLinstener(onclick);
					Tipdialog.show();
				}
			});
		}
		return convertView;
	}

	private DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			EMConversation conversation = conversationList.get(deleteID);
			EMChatManager.getInstance().deleteConversation(
					conversation.getUserName());
			// Utils.showLongToast((Activity) context, "删除成功");
			conversationList.remove(deleteID);
			notifyDataSetChanged();
			Tipdialog.dismiss();
		}
	};

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				digest = getStrng(context, R.string.location_recv);
				String name = message.getFrom();
				if (GloableParams.UserInfos != null) {
					User user = GloableParams.Users.get(message.getFrom());
					if (user != null && null != user.getUserName())
						name = user.getUserName();
				}
				digest = String.format(digest, name);
				return digest;
			} else {
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture)
					+ imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice_msg);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			if (!message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call)
						+ txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			System.err.println("error, unknow type");
			return "";
		}
		return digest;
	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}
}
