package com.allenjuns.wechat.app.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.chatuidemo.ui.ChatActivity;
import com.allenjuns.wechat.common.MFGT;
import com.hyphenate.easeui.EaseConstant;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-09
 */
public class FriendInfoActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_friendmsg);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("username") != null) {
            username = bundle.getString("username");
        }
    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
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
    @OnClick(R.id.btn_sendmsg)
    public void btn_sendmsg() {
        startActivity(new Intent(this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, username));
         overridePendingTransition(R.anim.push_left_in,
                 R.anim.push_left_out);
    }
}
