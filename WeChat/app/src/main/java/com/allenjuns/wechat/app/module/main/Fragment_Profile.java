package com.allenjuns.wechat.app.module.main;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseFragment;
import com.allenjuns.wechat.common.MFGT;

import butterknife.OnClick;

/**
 * Description :个人中心
 * Author : AllenJuns
 * Date   : 2016-3-07
 */
public class Fragment_Profile extends BaseFragment {

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_profile;
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

    //相册按钮点击事件
    @OnClick(R.id.txt_album)
    public void txt_album_Click() {
        MFGT.gotoCommon(getActivity(), getString(R.string.profile_txt_album));
    }

    //收藏按钮点击事件
    @OnClick(R.id.txt_collect)
    public void txt_collect_Click() {
        MFGT.gotoCommon(getActivity(), getString(R.string.profile_txt_collect));
    }

    //钱包按钮点击事件
    @OnClick(R.id.txt_money)
    public void txt_money_Click() {
        MFGT.gotoWalletActivity(getActivity() );
    }

    //卡包按钮点击事件
    @OnClick(R.id.txt_card)
    public void txt_card_Click() {
        MFGT.gotoCommon(getActivity(), getString(R.string.profile_txt_card));
    }

    //表情按钮点击事件
    @OnClick(R.id.txt_smail)
    public void txt_smail_Click() {
        MFGT.gotoCommon(getActivity(), getString(R.string.profile_txt_smail));
    }

    //设置按钮点击事件
    @OnClick(R.id.txt_setting)
    public void setting_Click() {
        MFGT.gotoSettingActivity(getActivity());
    }

    //个人信息点击事件
    @OnClick(R.id.view_user)
    public void profile_Click() {
        MFGT.gotoUserCode(getActivity());
    }
}
