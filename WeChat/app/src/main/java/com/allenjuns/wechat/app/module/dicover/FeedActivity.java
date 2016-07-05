package com.allenjuns.wechat.app.module.dicover;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.widget.RefreshLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :朋友圈
 * Author : AllenJuns
 * Date   : 2016-3-11
 */
public class FeedActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.btn_add)
    ImageButton btn_add;
    @Bind(R.id.swipe_container)
    RefreshLayout mRefreshLayout;
    @Bind(R.id.lv_listview)
    ListView mlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feed);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
        txt_title.setText(getString(R.string.discover_txt_pengyouquan));
        mRefreshLayout.setChildView(this, mlistview);
        View layout_head = getLayoutInflater().inflate(R.layout.layout_album_header,
                null);
        mlistview.addHeaderView(layout_head);
        mlistview.setAdapter(new FeedAdpter(this));
        mlistview.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    public void onRefresh() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MFGT.gotoCommentActivity(this);
    }

    //返回按钮点击事件
    @OnClick(R.id.img_back)
    public void close() {
        MFGT.finish(this);
    }
    @OnClick(R.id.txt_title)
    public void goTop() {
        mlistview.setSelection(0);
    }
    //  发布添加点击事件
    @OnClick(R.id.btn_add)
    public void btn_add_OnClick() {
        int[] startingLocation = new int[2];
        btn_add.getLocationOnScreen(startingLocation);
        startingLocation[0] += btn_add.getWidth() / 2;
        PublishActivity.startPublisActivityFromLocation(startingLocation, this);
        overridePendingTransition(0, 0);
    }

}
