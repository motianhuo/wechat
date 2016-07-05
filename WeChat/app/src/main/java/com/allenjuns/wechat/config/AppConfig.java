package com.allenjuns.wechat.config;

import com.allenjuns.wechat.AppContext;
import com.allenjuns.wechat.chatuidemo.ChatHelper;
import com.easemob.redpacketsdk.RedPacket;

public class AppConfig {
	public static boolean isPublish = false;// ?�
	public static final String TAG = "JUNS";
	private static AppConfig self = null;
	public static boolean mIsInit = false;

	public static synchronized AppConfig ins() {
		if (self == null) {
			self = new AppConfig();
		}
		return self;
	}

	public void init() {
		synchronized (this) {
			if (!mIsInit) {
				// TODO
				ChatHelper.getInstance().init(AppContext.getInstance());
				RedPacket.getInstance().initContext(AppContext.getInstance());
			}
			mIsInit = true;
		}
	}

}
