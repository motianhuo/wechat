package com.allenjuns.wechat.app.module.main;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseFragment;
import com.allenjuns.wechat.app.module.user.NearByActivity;
import com.allenjuns.wechat.common.Constants;
import com.allenjuns.wechat.common.MFGT;

import butterknife.OnClick;

/**
 * Description :发现
 * Author : AllenJuns
 * Date   : 2016-3-07
 */
public class Fragment_Dicover extends BaseFragment {

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_dicover;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    //朋友圈点击事件
    @OnClick(R.id.txt_pengyouquan)
    public void txt_pengyouquan_Click() {
        MFGT.gotoFeedActivity(getActivity());
    }

    //扫一扫点击事件
    @OnClick(R.id.txt_saoyisao)
    public void txt_saoyisao_Click() {
        MFGT.gotoZXCode(getActivity());
    }

    //摇一摇点击事件
    @OnClick(R.id.txt_yaoyiyao)
    public void txt_yaoyiyao_Click() {
        MFGT.gotoCommon(getActivity(), getString(R.string.discover_txt_yaoyiyao));
    }

    //附近的人点击事件
    @OnClick(R.id.txt_nearby)
    public void txt_nearby_Click() {
        MFGT.startActivity(getActivity(), NearByActivity.class);
    }

    //漂流瓶点击事件
    @OnClick(R.id.txt_piaoliuping)
    public void txt_piaoliuping_Click() {
        MFGT.gotoCommon(getActivity(), getString(R.string.discover_txt_piaoliuping));
    }

    //购物点击事件
    @OnClick(R.id.txt_shop)
    public void txt_shop_Click() {
        MFGT.gotoWebView(getActivity(), getString(R.string.discover_txt_shop), Constants.SHOP_URL);
    }

    //游戏点击事件
    @OnClick(R.id.txt_game)
    public void txt_game_Click() {
        MFGT.gotoWebView(getActivity(), getString(R.string.discover_txt_game), Constants.GAME_URL);
    }


}
