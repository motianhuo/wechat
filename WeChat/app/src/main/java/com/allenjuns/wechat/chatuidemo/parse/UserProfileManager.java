package com.allenjuns.wechat.chatuidemo.parse;

import android.content.Context;

import com.allenjuns.wechat.chatuidemo.ChatHelper.DataSyncListener;
import com.allenjuns.wechat.chatuidemo.utils.PreferenceManager;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import java.util.ArrayList;
import java.util.List;

public class UserProfileManager {

    /**
     * application context
     */
    protected Context appContext = null;

    /**
     * init flag: test if the sdk has been inited before, we don't need to init
     * again
     */
    private boolean sdkInited = false;

    /**
     * HuanXin sync contact nick and avatar listener
     */
    private List<DataSyncListener> syncContactInfosListeners;

    private boolean isSyncingContactInfosWithServer = false;

    private EaseUser currentUser;

    public UserProfileManager() {
    }

    public synchronized boolean init(Context context) {
        if (sdkInited) {
            return true;
        }
        //TODO 服务器加载数据
//		ParseManager.getInstance().onInit(context);
        syncContactInfosListeners = new ArrayList<DataSyncListener>();
        sdkInited = true;
        return true;
    }

    public void addSyncContactInfoListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactInfosListeners.contains(listener)) {
            syncContactInfosListeners.add(listener);
        }
    }

    public void removeSyncContactInfoListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncContactInfosListeners.contains(listener)) {
            syncContactInfosListeners.remove(listener);
        }
    }

    public void asyncFetchContactInfosFromServer(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
        if (isSyncingContactInfosWithServer) {
            return;
        }
        isSyncingContactInfosWithServer = true;
        //TODO 服务器加载数据
        List<EaseUser> mList = new ArrayList<EaseUser>();
        for (String name : usernames) {
            EaseUser user = new EaseUser(name);
            user.setAvatar("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2554876210,496793817&fm=58");
            user.setNick("张小龙");
            EaseCommonUtils.setUserInitialLetter(user);
            mList.add(user);
        }
        callback.onSuccess(mList);
    }

    public void notifyContactInfosSyncListener(boolean success) {
        for (DataSyncListener listener : syncContactInfosListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingContactInfoWithServer() {
        return isSyncingContactInfosWithServer;
    }

    public synchronized void reset() {
        isSyncingContactInfosWithServer = false;
        currentUser = null;
        PreferenceManager.getInstance().removeCurrentUserInfo();
    }

    public synchronized EaseUser getCurrentUserInfo() {
        if (currentUser == null) {
            String username = EMClient.getInstance().getCurrentUser();
            currentUser = new EaseUser(username);
            String nick = getCurrentUserNick();
            currentUser.setNick((nick != null) ? nick : username);
            currentUser.setAvatar(getCurrentUserAvatar());
        }
        return currentUser;
    }

    public boolean updateCurrentUserNickName(final String nickname) {
        //TODO juns 修改昵称
        boolean isSuccess = true;
//        if (isSuccess) {
//            setCurrentUserNick(nickname);
//        }
        return isSuccess;
    }

    public String uploadUserAvatar(byte[] data) {
        //TODO juns 上传头像
        String avatarUrl = "http://upload-images.jianshu.io/upload_images/225849-6ddf0c36b2c5e252.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
//        if (avatarUrl != null) {
//            setCurrentUserAvatar(avatarUrl);
//        }
        return avatarUrl;
    }

    public void asyncGetCurrentUserInfo() {
        //TODO 服务器加载登录用户数据
        String avatarUrl = "http://upload-images.jianshu.io/upload_images/1836709-862f0d3e5e7786a8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
        String nickname = "AllenJuns";
        setCurrentUserNick(nickname);
        setCurrentUserAvatar(avatarUrl);
    }

    public void asyncGetUserInfo(final String username, final EMValueCallBack<EaseUser> callback) {
        //TODO 服务器加载数据
    }

    private void setCurrentUserNick(String nickname) {
        getCurrentUserInfo().setNick(nickname);
        PreferenceManager.getInstance().setCurrentUserNick(nickname);
    }

    private void setCurrentUserAvatar(String avatar) {
        getCurrentUserInfo().setAvatar(avatar);
        PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
    }

    private String getCurrentUserNick() {
        return PreferenceManager.getInstance().getCurrentUserNick();
    }

    private String getCurrentUserAvatar() {
        return PreferenceManager.getInstance().getCurrentUserAvatar();
    }

}
