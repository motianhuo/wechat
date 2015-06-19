package com.juns.wechat.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.easemob.chat.EMChatManager;
import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.bean.User;

public class UserUtils {
	/**
	 * 获取用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static User getUserModel(Context context) {
		User user = null;
		String jsondata = Utils.getValue(context, Constants.UserInfo);
		Log.e("", jsondata);
		if (!TextUtils.isEmpty(jsondata))
			user = JSON.parseObject(jsondata, User.class);
		return user;
	}

	/**
	 * 获取用户ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserID(Context context) {
		User user = getUserModel(context);
		if (user != null)
			return user.getTelephone();
		else
			return "";
	}

	/**
	 * 获取用户名字
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		User user = getUserModel(context);
		if (user != null)
			return user.getUserName();
		else
			return "";
	}

	/**
	 * 获取用户
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserPwd(Context context) {
		User user = getUserModel(context);
		if (user != null)
			return user.getPassword();
		else
			return "";
	}

	public static void getLogout(Context context) {
		EMChatManager.getInstance().logout();// 退出环信聊天
		Utils.RemoveValue(context, Constants.LoginState);
		Utils.RemoveValue(context, Constants.UserInfo);
		App.getInstance2().exit();
	}

}
