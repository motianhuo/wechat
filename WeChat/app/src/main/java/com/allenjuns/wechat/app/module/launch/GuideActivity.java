package com.allenjuns.wechat.app.module.launch;

import android.os.Bundle;

import com.allenjuns.wechat.common.EventTag;
import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.app.module.main.MainActivity;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.event.EventListener;
import com.allenjuns.wechat.event.EventManager;
import com.allenjuns.wechat.utils.CommonUtils;

import butterknife.OnClick;

/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-08
 */
public class GuideActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guide);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        EventManager.ins().registListener(EventTag.ACCOUNT_LOGIN, listener);
    }

    //返回按钮点击事件
    @OnClick(R.id.img_login)
    public void img_login_OnClick() {
        MFGT.gotoLogin(this);
    }

    //返回按钮点击事件
    @OnClick(R.id.img_register)
    public void img_register_OnClick() {
        MFGT.gotoRegister(this);
    }

    EventListener listener = new EventListener() {
        @Override
        public void handleMessage(int what, int arg1, int arg2, Object dataobj) {
            switch (what) {
                case EventTag.ACCOUNT_LOGIN:
                    finish();
                    break;
            }
        }
    };

}
