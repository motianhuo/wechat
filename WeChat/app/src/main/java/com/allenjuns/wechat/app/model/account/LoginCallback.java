package com.allenjuns.wechat.app.model.account;

/**
 * Description : 登录接口回调
 * Author : AllenJuns
 * Date   : 2016-3-04
 */
public interface LoginCallback {
    void onSuccess(String data);

    void onFailure(String error);
}
