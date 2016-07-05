package com.allenjuns.wechat.app.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 *
 */
public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootLayoutId = getRootLayoutId();
        View rootView = inflater.inflate(rootLayoutId, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        initData();
        setListener();
        return rootView;
    }

    public abstract int getRootLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();

}
