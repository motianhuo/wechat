package com.allenjuns.wechat.app.module.launch;

import android.content.Intent;
import android.os.Bundle;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.chatuidemo.ChatHelper;
import com.allenjuns.wechat.common.MFGT;
import com.easemob.redpacketsdk.RPCallback;
import com.easemob.redpacketsdk.RedPacket;
import com.hyphenate.chat.EMClient;

/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-08
 */
public class SplashActivtiy extends BaseActivity {
    private static final int sleepTime = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splach);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {

    }
    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            public void run() {
                if (ChatHelper.getInstance().isLoggedIn()) {
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    RedPacket.getInstance().initRPToken(ChatHelper.getInstance().getCurrentUsernName(), ChatHelper.getInstance().getCurrentUsernName(),
                            EMClient.getInstance().getChatConfig().getAccessToken(), new RPCallback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError(String s, String s1) {
                        }
                    });
                    long costTime = System.currentTimeMillis() - start;
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    MFGT.gotoMainActivity(SplashActivtiy.this);
                    finish();
                }else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplashActivtiy.this, GuideActivity.class));
                    finish();
                }
            }
        }).start();

    }
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }
}
