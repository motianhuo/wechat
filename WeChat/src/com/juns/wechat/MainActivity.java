package com.juns.wechat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.http.message.BasicNameValuePair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;
import com.juns.wechat.bean.InviteMessage;
import com.juns.wechat.bean.InviteMessage.InviteMesageStatus;
import com.juns.wechat.bean.User;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.dialog.WarnTipDialog;
import com.juns.wechat.dialog.TitleMenu.ActionItem;
import com.juns.wechat.dialog.TitleMenu.TitlePopup;
import com.juns.wechat.dialog.TitleMenu.TitlePopup.OnItemOnClickListener;
import com.juns.wechat.view.UpdateService;
import com.juns.wechat.view.activity.AddGroupChatActivity;
import com.juns.wechat.view.activity.GetMoneyActivity;
import com.juns.wechat.view.activity.PublicActivity;
import com.juns.wechat.view.fragment.Fragment_Dicover;
import com.juns.wechat.view.fragment.Fragment_Friends;
import com.juns.wechat.view.fragment.Fragment_Msg;
import com.juns.wechat.view.fragment.Fragment_Profile;
import com.juns.wechat.zxing.CaptureActivity;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private TextView txt_title;
	private ImageView img_right;
	private WarnTipDialog Tipdialog;
	private NewMessageBroadcastReceiver msgReceiver;
	protected static final String TAG = "MainActivity";
	private TitlePopup titlePopup;
	private TextView unreaMsgdLabel;// 未读消息textview
	private TextView unreadAddressLable;// 未读通讯录textview
	private TextView unreadFindLable;// 发现
	private Fragment[] fragments;
	public Fragment_Msg homefragment;
	private Fragment_Friends contactlistfragment;
	private Fragment_Dicover findfragment;
	private Fragment_Profile profilefragment;
	private ImageView[] imagebuttons;
	private TextView[] textviews;
	private String connectMsg = "";;
	private int index;
	private int currentTabIndex;// 当前fragment的index

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		App.getInstance2().addActivity(this);
		findViewById();
		initViews();
		initTabView();
		// initVersion();
		setOnListener();
		initPopWindow();
		initReceiver();
	}

	private void initTabView() {
		unreaMsgdLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		unreadFindLable = (TextView) findViewById(R.id.unread_find_number);
		homefragment = new Fragment_Msg();
		contactlistfragment = new Fragment_Friends();
		findfragment = new Fragment_Dicover();
		profilefragment = new Fragment_Profile();
		fragments = new Fragment[] { homefragment, contactlistfragment,
				findfragment, profilefragment };
		imagebuttons = new ImageView[4];
		imagebuttons[0] = (ImageView) findViewById(R.id.ib_weixin);
		imagebuttons[1] = (ImageView) findViewById(R.id.ib_contact_list);
		imagebuttons[2] = (ImageView) findViewById(R.id.ib_find);
		imagebuttons[3] = (ImageView) findViewById(R.id.ib_profile);

		imagebuttons[0].setSelected(true);
		textviews = new TextView[4];
		textviews[0] = (TextView) findViewById(R.id.tv_weixin);
		textviews[1] = (TextView) findViewById(R.id.tv_contact_list);
		textviews[2] = (TextView) findViewById(R.id.tv_find);
		textviews[3] = (TextView) findViewById(R.id.tv_profile);
		textviews[0].setTextColor(0xFF45C01A);
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, homefragment)
				.add(R.id.fragment_container, contactlistfragment)
				.add(R.id.fragment_container, profilefragment)
				.add(R.id.fragment_container, findfragment)
				.hide(contactlistfragment).hide(profilefragment)
				.hide(findfragment).show(homefragment).commit();
		updateUnreadLabel();
	}

	public void onTabClicked(View view) {
		img_right.setVisibility(View.GONE);
		switch (view.getId()) {
		case R.id.re_weixin:
			img_right.setVisibility(View.VISIBLE);
			index = 0;
			if (homefragment != null) {
				homefragment.refresh();
			}
			txt_title.setText(R.string.app_name);
			img_right.setImageResource(R.drawable.icon_add);
			break;
		case R.id.re_contact_list:
			index = 1;
			txt_title.setText(R.string.contacts);
			img_right.setVisibility(View.VISIBLE);
			img_right.setImageResource(R.drawable.icon_titleaddfriend);
			break;
		case R.id.re_find:
			index = 2;
			txt_title.setText(R.string.discover);
			break;
		case R.id.re_profile:
			index = 3;
			txt_title.setText(R.string.me);
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		imagebuttons[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		imagebuttons[index].setSelected(true);
		textviews[currentTabIndex].setTextColor(0xFF999999);
		textviews[index].setTextColor(0xFF45C01A);
		currentTabIndex = index;
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, R.string.menu_groupchat,
				R.drawable.icon_menu_group));
		titlePopup.addAction(new ActionItem(this, R.string.menu_addfriend,
				R.drawable.icon_menu_addfriend));
		titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode,
				R.drawable.icon_menu_sao));
		titlePopup.addAction(new ActionItem(this, R.string.menu_money,
				R.drawable.abv));
	}

	private OnItemOnClickListener onitemClick = new OnItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			// mLoadingDialog.show();
			switch (position) {
			case 0:// 发起群聊
				Utils.start_Activity(MainActivity.this,
						AddGroupChatActivity.class);
				break;
			case 1:// 添加朋友
				Utils.start_Activity(MainActivity.this, PublicActivity.class,
						new BasicNameValuePair(Constants.NAME, "添加朋友"));
				break;
			case 2:// 扫一扫
				Utils.start_Activity(MainActivity.this, CaptureActivity.class);
				break;
			case 3:// 收钱
				Utils.start_Activity(MainActivity.this, GetMoneyActivity.class);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewById() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		img_right = (ImageView) findViewById(R.id.img_right);
	}

	private void initViews() {
		// 设置消息页面为初始页面
		img_right.setVisibility(View.VISIBLE);
		img_right.setImageResource(R.drawable.icon_add);
	}

	private void setOnListener() {
		img_right.setOnClickListener(this);

	}

	private int keyBackClickCount = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1:
				EMChatManager.getInstance().logout();// 退出环信聊天
				App.getInstance2().exit();
				finish();
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				break;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_right:
			if (index == 0) {
				titlePopup.show(findViewById(R.id.layout_bar));
			} else {
				Utils.start_Activity(MainActivity.this, PublicActivity.class,
						new BasicNameValuePair(Constants.NAME, "添加朋友"));
			}
			break;
		default:
			break;
		}
	}

	private void initVersion() {
		// TODO 检查版本更新
		String versionInfo = Utils.getValue(this, Constants.VersionInfo);
		if (!TextUtils.isEmpty(versionInfo)) {
			Tipdialog = new WarnTipDialog(this,
					"发现新版本：  1、更新啊阿三达到阿德阿   2、斯顿阿斯顿撒旦？");
			Tipdialog.setBtnOkLinstener(onclick);
			Tipdialog.show();
		}
	}

	private DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Utils.showLongToast(MainActivity.this, "正在下载...");// TODO
			Tipdialog.dismiss();
		}
	};

	private void initReceiver() {
		Intent intent = new Intent(this, UpdateService.class);
		startService(intent);
		registerReceiver(new MyBroadcastReceiver(), new IntentFilter(
				"com.juns.wechat.Brodcast"));
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// 注册一个透传消息的BroadcastReceiver
		IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getCmdMessageBroadcastAction());
		cmdMessageIntentFilter.setPriority(3);
		registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);
		// setContactListener监听联系人的变化等
		// EMContactManager.getInstance().setContactListener(
		// new MyContactListener());
		// 注册一个监听连接状态的listener
		// EMChatManager.getInstance().addConnectionListener(
		// new MyConnectionListener());
		// // 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(
				new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}

	// 自己联系人 群组数据返回监听
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// Bundle bundle = intent.getExtras();
			homefragment.refresh();
			contactlistfragment.refresh();
		}
	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					connectMsg = getString(R.string.app_name);
					txt_title.setText(connectMsg);
					homefragment.errorItem.setVisibility(View.GONE);
				}
			});
		}

		@Override
		public void onDisconnected(final int error) {
			connectMsg = "微信(未连接)";
			txt_title.setText(connectMsg);
			final String st1 = getResources().getString(
					R.string.Less_than_chat_server_connection);
			final String st2 = getResources().getString(
					R.string.the_current_network);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						// showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						// showConflictDialog();
					} else {
						homefragment.errorItem.setVisibility(View.VISIBLE);
						if (NetUtils.hasNetwork(MainActivity.this))
							homefragment.errorText.setText(st1);
						else
							homefragment.errorText.setText(st2);
					}
				}

			});
		}
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

			String from = intent.getStringExtra("from");
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			if (ChatActivity.activityInstance != null) {
				if (message.getChatType() == ChatType.GroupChat) {
					if (message.getTo().equals(
							ChatActivity.activityInstance.getToChatUsername()))
						return;
				} else {
					if (from.equals(ChatActivity.activityInstance
							.getToChatUsername()))
						return;
				}
			}

			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();
			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			if (currentTabIndex == 0) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (homefragment != null) {
					homefragment.refresh();
				}
			}
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");

			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);

				if (msg != null) {

					if (ChatActivity.activityInstance != null) {
						if (msg.getChatType() == ChatType.Chat) {
							if (from.equals(ChatActivity.activityInstance
									.getToChatUsername()))
								return;
						}
					}

					msg.isAcked = true;
				}
			}
		}
	};

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {

			// 被邀请
			String st3 = getResources().getString(
					R.string.Invite_you_to_join_a_group_chat);
			User user = GloableParams.Users.get(inviter);
			if (user != null) {
				EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
				msg.setChatType(ChatType.GroupChat);
				msg.setFrom(inviter);
				msg.setTo(groupId);
				msg.setMsgId(UUID.randomUUID().toString());
				msg.addBody(new TextMessageBody(user.getUserName() + st3));
				msg.setAttribute("useravatar", user.getHeadUrl());
				msg.setAttribute("usernick", user.getUserName());
				// 保存邀请消息
				EMChatManager.getInstance().saveMessage(msg);
				// 提醒新消息
				EMNotifier.getInstance(getApplicationContext())
						.notifyOnNewMsg();
			}
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 0)
						homefragment.refresh();
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
						if (currentTabIndex == 0)
							homefragment.refresh();
					} catch (Exception e) {
						EMLog.e(TAG, "refresh exception " + e.getMessage());
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散 提示用户群被解散, 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					if (currentTabIndex == 0)
						homefragment.refresh();
				}
			});
		}

		@Override
		public void onApplicationReceived(String groupId, String groupName,
				String applyer, String reason) {
			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			// 提示有新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName,
				String accepter) {
			String st4 = getResources().getString(
					R.string.Agreed_to_your_group_chat_application);
			// 加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + st4));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 0)
						homefragment.refresh();
					// if (CommonUtils.getTopActivity(MainActivity.this).equals(
					// GroupsActivity.class.getName())) {
					// GroupsActivity.instance.onResume();
					// }
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName,
				String decliner, String reason) {
			// 加群申请被拒绝，demo未实现
		}

	};

	/**
	 * 透传消息BroadcastReceiver
	 */
	private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			EMLog.d(TAG, "收到透传消息");
			// 获取cmd message对象
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = intent.getParcelableExtra("message");
			// 获取消息body
			CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
			String action = cmdMsgBody.action;// 获取自定义action

			// 获取扩展属性 此处省略
			// message.getStringAttribute("");
			EMLog.d(TAG,
					String.format("透传消息：action:%s,message:%s", action,
							message.toString()));
			String st9 = getResources().getString(
					R.string.receive_the_passthrough);
			Toast.makeText(MainActivity.this, st9 + action, Toast.LENGTH_SHORT)
					.show();
		}
	};

	/**
	 * 获取未读消息数
	 */
	public void updateUnreadLabel() {
		int count = 0;
		count = EMChatManager.getInstance().getUnreadMsgsCount();
		if (count > 0) {
			unreaMsgdLabel.setText(String.valueOf(count));
			unreaMsgdLabel.setVisibility(View.VISIBLE);
		} else {
			unreaMsgdLabel.setVisibility(View.INVISIBLE);
		}
	}

}