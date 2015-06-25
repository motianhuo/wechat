package com.juns.wechat.view;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.bean.GroupInfo;
import com.juns.wechat.bean.User;
import com.juns.wechat.common.Utils;
import com.juns.wechat.net.BaseJsonRes;
import com.juns.wechat.net.NetClient;

public class UpdateService extends Service {
	protected NetClient netClient;
	protected FinalDb db;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		netClient = new NetClient(this);
		db = FinalDb.create(this, Constants.DB_NAME, false);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int RunCount = Utils.getIntValue(this, "RUN_COUNT");
		if (RunCount % 10 == 0) {
			initUserList();
			initGroupList();

			String str_contact = Utils.getValue(this, Constants.ContactMsg);
			PackageManager pm = getPackageManager();
			boolean permission = (PackageManager.PERMISSION_GRANTED == pm
					.checkPermission("android.permission.READ_CONTACTS",
							"com.juns.wechat"));
			if (TextUtils.isEmpty(str_contact) && permission) {
				str_contact = getContact();
				Utils.putValue(this, Constants.ContactMsg, str_contact);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 获取群组列表
	private void initGroupList() {
		GloableParams.ListGroupInfos = db.findAll(GroupInfo.class);
		netClient.post(Constants.getGroupListURL, null, new BaseJsonRes() {

			@Override
			public void onMySuccess(String data) {
				GloableParams.ListGroupInfos = JSON.parseArray(data,
						GroupInfo.class);
				for (GroupInfo group : GloableParams.ListGroupInfos) {
					if (db.findById(group.getId(), GroupInfo.class) != null)
						db.deleteById(GroupInfo.class, group.getId());
					db.save(group);
					GloableParams.GroupInfos.put(group.getGroup_id(), group);
				}
				sendBrodcast("GroupList");
			}

			@Override
			public void onMyFailure() {
				// initGroupList();
			}
		});
	}

	// 获取好友列表和订阅号
	private void initUserList() {
		GloableParams.UserInfos = db.findAll(User.class);

		netClient.post(Constants.getUserInfoURL, null, new BaseJsonRes() {

			@Override
			public void onMySuccess(String data) {
				List<User> new_users = JSON.parseArray(data, User.class);
				for (User user : new_users) {
					if (user.getUserName() == null) {
						user.setUserName("WX" + user.getTelephone());
						new_users.remove(user);
						new_users.add(user);
					}
					if (db.findById(user.getId(), User.class) != null)
						db.deleteById(User.class, user.getId());
					db.save(user);
					GloableParams.Users.put(user.getTelephone(), user);
				}
				sendBrodcast("UserList");
			}

			@Override
			public void onMyFailure() {
				// initUserList();
			}
		});
	}

	public String getContact() {
		// 获得所有的联系人
		String strTelphones = "";
		String strNames = "";
		Cursor cur = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		// 循环遍历
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = cur.getString(displayNameColumn);
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do { // 遍历所有的电话号码
							String phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							if (phoneNumber.startsWith("+186")) {
								phoneNumber = phoneNumber.substring(4);
							}
							if (Utils.isMobileNO(phoneNumber)) {
								strTelphones = strTelphones + "'" + phoneNumber
										+ "',";
								strNames = strNames + "',";
							}
						} while (phones.moveToNext());
					}
				}
			} while (cur.moveToNext());
		}
		if (strTelphones.length() > 0 && strNames.length() > 0) {
			strTelphones = strTelphones.substring(0, strTelphones.length() - 1);
			strNames = strNames.substring(0, strNames.length() - 1);
		}
		return strTelphones;
	}

	private void sendBrodcast(String Action) {
		Intent intent = new Intent();
		intent.setAction("com.juns.wechat.Brodcast");
		intent.putExtra("Action", Action);
		sendBroadcast(intent);
	}
}
