package com.allenjuns.wechat.app.module.dicover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.utils.PhoneUtils;
import com.allenjuns.wechat.widget.RevealBackgroundView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :发布朋友圈
 * Author : AllenJuns
 * Date   : 2016-3-08
 */
public class PublishActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.txt_right)
    TextView txt_right;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.layout_title)
    View layout_title;
    @Bind(R.id.scrollView)
    View scrollView;
    @Bind(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_publish);
        setupRevealBackground(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
        txt_title.setText(getString(R.string.discover_txt_pengyouquan));
        txt_right.setText("发送");
        txt_right.setTextColor(0xFF45C01A);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            vRevealBackground.setVisibility(View.GONE);
            vRevealBackground.setToFinishedFrame();
            int actionbarSize = PhoneUtils.dpToPx(56);
            layout_title.setTranslationY(-actionbarSize);
            layout_title.animate()
                    .translationY(0)
                    .setDuration(300).setStartDelay(200).start();
            scrollView.setAlpha(0f);
            scrollView.animate()
                    .alpha(1f)
                    .setDuration(300).setStartDelay(300).start();
        } else {
        }
    }

    //返回按钮点击事件
    @OnClick(R.id.img_back)
    public void close() {
        MFGT.finish(this);
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_DRAWING_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return false;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
        }
    }

    public static void startPublisActivityFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, PublishActivity.class);
        intent.putExtra(ARG_DRAWING_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }
}
