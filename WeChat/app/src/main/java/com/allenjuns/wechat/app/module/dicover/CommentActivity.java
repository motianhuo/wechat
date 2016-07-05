package com.allenjuns.wechat.app.module.dicover;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.common.CommentAnimUtils;
import com.allenjuns.wechat.common.MFGT;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :评论
 * Author : AllenJuns
 * Date   : 2016-3-08
 */
public class CommentActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.first_view)
    View firstView;
    @Bind(R.id.second_view)
    View secondView;
    private int fHeight = 0, sHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MFGT.finishFormBottom(this);
    }
    @Override
    protected void findView() {
    }

    @Override
    protected void initView() {
        txt_title.setText(getString(R.string.discover_txt_pengyouquan));
        img_back.setVisibility(View.VISIBLE);
        fHeight = firstView.getHeight();
        sHeight = secondView.getHeight();
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
        MFGT.finishFormBottom(this);
    }

    @OnClick(R.id.btn_anim3_show)
    public void Open() {
        CommentAnimUtils.initShowAnim(firstView, secondView, fHeight, sHeight);
    }
    @OnClick(R.id.btn_anim3_hidden)
    public void Hidden() {
        CommentAnimUtils.initHiddenAnim(firstView, secondView, fHeight, sHeight);
    }


}
