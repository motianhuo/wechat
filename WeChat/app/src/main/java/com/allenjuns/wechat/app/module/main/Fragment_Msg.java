package com.allenjuns.wechat.app.module.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseFragment;
import com.allenjuns.wechat.app.module.main.adpter.NewMsgAdpter;
import com.allenjuns.wechat.chatuidemo.ui.ChatActivity;
import com.allenjuns.wechat.widget.EmptyLayout;
import com.allenjuns.wechat.widget.RefreshLayout;
import com.hyphenate.easeui.EaseConstant;

import butterknife.Bind;

/**
 * Description :消息
 * Author : AllenJuns
 * Date   : 2016-3-07
 */
public class Fragment_Msg extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    @Bind(R.id.lv_listview)
    ListView mListView;
    @Bind(R.id.swipe_container)
    RefreshLayout mRefreshLayout;
    private EmptyLayout emptyLayout;
    private NewMsgAdpter mAdapter;
    private Activity ctx;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void initView() {
        ctx = this.getActivity();
        emptyLayout = new EmptyLayout(ctx, mRefreshLayout);
        emptyLayout.showLoading();
        mRefreshLayout.setChildView(ctx, mListView);
    }

    @Override
    protected void initData() {
        mAdapter = new NewMsgAdpter(ctx);
        mListView.setAdapter(mAdapter);
        emptyLayout.showContent();
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onRefresh() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        MFGT.gotoChatActivity(getActivity());
        startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, "17020000000"));
    }


}
