//package com.allenjuns.wechat.app.module.main;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.PixelFormat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.allenjuns.wechat.R;
//import com.allenjuns.wechat.app.base.BaseFragment;
//import com.allenjuns.wechat.app.module.main.adpter.ContactAdapter;
//import com.allenjuns.wechat.chatuidemo.ui.ChatActivity;
//import com.allenjuns.wechat.common.MFGT;
//import com.allenjuns.wechat.widget.EmptyLayout;
//import com.allenjuns.wechat.widget.SideBar;
//import com.hyphenate.easeui.EaseConstant;
//
//import butterknife.Bind;
//
///**
// * Description :通讯录
// * Author : AllenJuns
// * Date   : 2016-3-07
// */
//public class Fragment_Friends extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener {
//    @Bind(R.id.lv_listview)
//    ListView lvContact;
//    @Bind(R.id.sideBar)
//    SideBar indexBar;
//    private EmptyLayout emptyLayout;
//    private TextView mDialogText;
//    private WindowManager mWindowManager;
//    private Activity ctx;
//    private View layout_head,layout_addfriend,layout_group,layout_public;
//
//    @Override
//    public int getRootLayoutId() {
//        return R.layout.fragment_friends;
//    }
//
//    @Override
//    protected void initView() {
//        ctx = this.getActivity();
//        emptyLayout = new EmptyLayout(getContext(), lvContact);
//        mWindowManager = (WindowManager) ctx
//                .getSystemService(Context.WINDOW_SERVICE);
//        indexBar.setListView(lvContact);
//        mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(
//                R.layout.view_list_position, null);
//        mDialogText.setVisibility(View.INVISIBLE);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_APPLICATION,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        mWindowManager.addView(mDialogText, lp);
//        indexBar.setTextView(mDialogText);
//        layout_head = ctx.getLayoutInflater().inflate(
//                R.layout.layout_head_friend, null);
//        lvContact.addHeaderView(layout_head);
//        layout_addfriend=layout_head.findViewById(R.id.layout_addfriend);
//        layout_group=layout_head.findViewById(R.id.layout_group);
//        layout_public  =layout_head.findViewById(R.id.layout_public);
//    }
//
//    @Override
//    protected void initData() {
//        lvContact.setAdapter(new ContactAdapter(getActivity()));
//    }
//
//    @Override
//    protected void setListener() {
//        layout_addfriend.setOnClickListener(this);
//        layout_group.setOnClickListener(this);
//        layout_public.setOnClickListener(this);
//        lvContact.setOnItemClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.layout_addfriend:
//                MFGT.gotoCommon(getActivity(), getString(R.string.recommended_friends));
//                break;
//            case R.id.layout_group:
//                MFGT.gotoCommon(getActivity(), getString(R.string.group_chats));
//                break;
//            case R.id.layout_public:
//                MFGT.gotoCommon(getActivity(), getString(R.string.official_accounts));
//            break;
//
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////        MFGT.startActivity(getActivity(), FriendInfoActivity.class);
//        startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, "17020000000"));
//    }
//}
