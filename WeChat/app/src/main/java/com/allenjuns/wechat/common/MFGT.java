package com.allenjuns.wechat.common;

import android.app.Activity;
import android.content.Intent;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.module.account.LoginActivity;
import com.allenjuns.wechat.app.module.account.RegiseterActivity;
import com.allenjuns.wechat.app.module.dicover.CommentActivity;
import com.allenjuns.wechat.app.module.dicover.FeedActivity;
import com.allenjuns.wechat.app.module.launch.GuideActivity;
import com.allenjuns.wechat.app.module.main.MainActivity;
import com.allenjuns.wechat.app.module.money.WalletActivity;
import com.allenjuns.wechat.app.module.system.CommonActivity;
import com.allenjuns.wechat.app.module.system.SettingActivity;
import com.allenjuns.wechat.app.module.system.WebViewActivity;
import com.allenjuns.wechat.app.module.user.FriendInfoActivity;
import com.allenjuns.wechat.app.module.user.MyCodeActivity;
import com.allenjuns.wechat.chatuidemo.ChatHelper;
import com.allenjuns.wechat.chatuidemo.ui.ChatActivity;
import com.allenjuns.wechat.zxing.CaptureActivity;
import com.hyphenate.EMCallBack;

/**
 * Description :页面跳转
 * Author : AllenJuns
 * Date   : 2016-3-03
 */
public class MFGT {
    /**
     * 右侧动画关闭 Activity
     *
     * @param activity
     */
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

    /**
     * 底部动画关闭 Activity
     *
     * @param activity
     */
    public static void finishFormBottom(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_bottom_in,
                R.anim.push_bottom_out);
    }

    /**
     * 跳转登陆界面
     */
    public static void gotoMainActivity(Activity context) {
        startActivity(context, MainActivity.class);
    }

    /**
     * 跳转登陆界面
     */
    public static void gotoLogin(Activity context) {
        startActivity(context, LoginActivity.class);
    }

    /**
     * 跳转注册界面
     */
    public static void gotoRegister(Activity context) {
        startActivity(context, RegiseterActivity.class);
    }

    /**
     * 跳转Webview界面
     */
    public static void gotoWebView(Activity context, String title, String url) {
        Intent intent = new Intent();
        intent.setClass(context, WebViewActivity.class);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    /**
     * 跳转朋友圈界面
     */
    public static void gotoFeedActivity(Activity context) {
        startActivity(context, FeedActivity.class);
    }

    /**
     * 跳转二维码扫描界面
     */
    public static void gotoZXCode(Activity context) {
        startActivity(context, CaptureActivity.class);
    }

    /**
     * 跳转我的二维码界面
     */
    public static void gotoUserCode(Activity context) {
        startActivity(context, MyCodeActivity.class);
    }

    /**
     * 跳转好友资料界面
     */
    public static void gotoFriendsMsgActivity(Activity context,String username) {
        Intent intent = new Intent();
        intent.setClass(context, FriendInfoActivity.class);
        intent.putExtra("username", username);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    /**
     * 跳转钱包界面
     */
    public static void gotoWalletActivity(Activity context) {
        startActivity(context, WalletActivity.class);
    }

    /**
     * 跳转设置界面
     */
    public static void gotoSettingActivity(Activity context) {
        startActivity(context, SettingActivity.class);
    }

    /**
     * 跳转聊天界面
     */
    public static void gotoChatActivity(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, ChatActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    /**
     * 跳转评论界面
     */
    public static void gotoCommentActivity(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, CommentActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_up_in,
                R.anim.push_up_out);
    }

    public static void gotoCommon(Activity context, String title) {
        Intent intent = new Intent();
        intent.setClass(context, CommonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(Constants.TITLE, title);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    /**
     * 重启应用
     *
     * @param context
     */
    public static void gotoGuide(final Activity context) {
        // 退出环信聊天
        ChatHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        ExitAppUtils.getInstance().exit();
                        Intent it = new Intent(context, GuideActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
            }
        });
    }
}
