package com.allenjuns.wechat.app.module.system;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.common.MFGT;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :设置
 * Author : AllenJuns
 * Date   : 2016-3-09
 */
public class SettingActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
        txt_title.setText(getString(R.string.profile_txt_setting));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }
    //返回按钮点击事件
    @OnClick(R.id.img_back)
    public void close() {
        MFGT.finish(this);
    }
    //返回按钮点击事件
    @OnClick(R.id.btnexit)
    public void btnexit() {
         MFGT.gotoGuide(this);
    }


    @OnClick(R.id.txt_about)
    public void txt_about_OnClick() {
        MFGT.gotoWebView(this, getString(R.string.setting_txt_about), "https://github.com/motianhuo");
    }
}
