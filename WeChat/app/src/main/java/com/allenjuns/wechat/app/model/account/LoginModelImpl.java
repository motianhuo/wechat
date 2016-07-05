package com.allenjuns.wechat.app.model.account;

import com.allenjuns.wechat.chatuidemo.ChatHelper;
import com.allenjuns.wechat.event.EventManager;
import com.easemob.redpacketsdk.RPCallback;
import com.easemob.redpacketsdk.RedPacket;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.allenjuns.wechat.common.EventTag;

/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-04
 */
public class LoginModelImpl {
    public void login(final String name, final String pwd, final LoginCallback callback) {
        EMClient.getInstance().login(name, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                ChatHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                RedPacket.getInstance().initRPToken(name, pwd,
                        EMClient.getInstance().getChatConfig().getAccessToken(),
                        new RPCallback() {
                            @Override
                            public void onSuccess() {
                                ChatHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                                ChatHelper.getInstance().asyncFetchContactsFromServer(null);
                                EventManager.ins().sendEvent(EventTag.ACCOUNT_LOGIN,0,0,0);
                            }

                            @Override
                            public void onError(String s, String s1) {
                            }
                        });
                callback.onSuccess("");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                callback.onFailure(" Login Error!"+message);
            }
        });

    }
}
