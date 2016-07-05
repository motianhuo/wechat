package com.allenjuns.wechat.app.module.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.app.module.dicover.FeedAdpter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :聊天界面
 * Author : AllenJuns
 * Date   : 2016-3-09
 */
public class ChatActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.lv_listview)
    ListView mlistview;
    @Bind(R.id.ll_face_container)
    View ll_face_container;
    @Bind(R.id.ll_btn_container)
    View ll_btn_container;
    @Bind(R.id.ll_vole)
    View ll_vole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
//        txt_title.setText(getString(R.string.profile_txt_setting));
        FeedAdpter adpter=   new FeedAdpter(this);
//        mlistview.setAdapter(adpter);
//        mlistview.setSelection(adpter.getCount());
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
//        MFGT.finish(this);
        ll_vole.setVisibility(View.GONE);
        ll_face_container.setVisibility(View.GONE);
        ll_btn_container.setVisibility(View.GONE);
    }
    @OnClick(R.id.btn_vol)
    public void btn_vol() {
        ll_vole.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.btn_smail)
    public void btn_smail() {
        ll_face_container.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.btn_tools)
    public void btn_tools() {
        ll_btn_container.setVisibility(View.VISIBLE);
    }

}
