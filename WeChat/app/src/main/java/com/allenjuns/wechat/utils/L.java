package com.allenjuns.wechat.utils;

import android.util.Log;

import com.allenjuns.wechat.config.AppConfig;


/**
 * LOG
 */
public class L {
	/**
	 * if it is essential to print bug message. I can initial this value in the
	 * launch activity
	 */
	public static boolean isDebug = !AppConfig.isPublish;
	// A good convention is to declare a TAG constant in your class:
	private static final String TAG = AppConfig.TAG;

	public static void i(String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
		}
	}

	public static void d(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (isDebug) {
			Log.e(TAG, msg);
		}
	}

	public static void v(String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
		}
	}

	// subsequent calls to the log methods.
	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}
}
