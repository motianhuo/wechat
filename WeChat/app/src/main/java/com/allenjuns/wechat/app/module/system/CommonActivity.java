package com.allenjuns.wechat.app.module.system;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.common.Constants;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.widget.EmptyLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-08
 */
public class CommonActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.layout)
    View layout;
    private EmptyLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_common);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
        emptyLayout = new EmptyLayout(this, layout);
        emptyLayout.showEmpty();
    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
        String titile = getIntent().getStringExtra(Constants.TITLE);
        txt_title.setText(titile);
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
}
