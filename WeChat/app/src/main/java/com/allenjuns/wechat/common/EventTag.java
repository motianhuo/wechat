package com.allenjuns.wechat.common;

public class EventTag {
    public static final int CHAT_NOTIFY_MSG = 0x1001;// 消息拉取刷新

    public static final int ACCOUNT_LOGIN = 0x2001;// 登录成功
    public static final int ACCOUNT_LOGOUT = 0x2002;// 注销成功
    public static final int ACCOUNT_RELOGOUT = 0x2003;// 需要重新登录
    public static final int ACCOUNT_UPDATE_INFO = 0x2004;// 用户信息修改
    public static final int ACCOUNT_FRIEND_UPDATE = 0x2005;// 好友列表更新


}
