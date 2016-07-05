package com.allenjuns.wechat.app.module.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.app.module.user.NearByActivity;
import com.allenjuns.wechat.chatuidemo.db.InviteMessgeDao;
import com.allenjuns.wechat.chatuidemo.ui.ContactListFragment;
import com.allenjuns.wechat.chatuidemo.ui.ConversationListFragment;
import com.allenjuns.wechat.common.EventTag;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.dialog.TitleMenu.ActionItem;
import com.allenjuns.wechat.dialog.TitleMenu.TitlePopup;
import com.allenjuns.wechat.event.EventListener;
import com.allenjuns.wechat.event.EventManager;
import com.allenjuns.wechat.utils.CommonUtils;
import com.allenjuns.wechat.widget.DMTabHost;
import com.allenjuns.wechat.widget.MFViewPager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

import static butterknife.ButterKnife.bind;

public class MainActivity extends BaseActivity implements DMTabHost.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_right)
    ImageView img_right;
    @Bind(R.id.tab_host)
    DMTabHost mHost;
    @Bind(R.id.viewpager)
    MFViewPager viewpager;
    private MainTabAdpter adapter;
    private int keyBackClickCount = 0;
    private int PageIndex = 0;
    private TitlePopup titlePopup;
    private ConversationListFragment conversationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind(this);
        initMainTab();
        initPopWindow();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (keyBackClickCount++) {
                case 0:
                    CommonUtils.showLongToast(getString(R.string.key_back_msg));
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 3000);
                    break;
                case 1:
                    MFGT.finishFormBottom(this);
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initPopWindow() {
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(onitemClick);
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(this, R.string.menu_groupchat,
                R.drawable.icon_menu_group));
        titlePopup.addAction(new ActionItem(this, R.string.menu_addfriend,
                R.drawable.icon_menu_addfriend));
        titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode,
                R.drawable.icon_menu_sao));
        titlePopup.addAction(new ActionItem(this, R.string.menu_money,
                R.drawable.icon_menu_money));
    }

    private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

        @Override
        public void onItemClick(ActionItem item, int position) {
            switch (position) {
                case 0:// 发起群聊
                    MFGT.gotoCommon(MainActivity.this, getString(R.string.menu_groupchat));
                    break;
                case 1:// 添加朋友
//                    MFGT.gotoCommon(MainActivity.this, getString(R.string.menu_addfriend));
                    MFGT.startActivity(MainActivity.this, NearByActivity.class);
                    //TODO
                    break;
                case 2:// 扫一扫
                    //TODO
                    MFGT.gotoZXCode(MainActivity.this);
                    break;
                case 3:// 收钱
                    MFGT.gotoWalletActivity(MainActivity.this);
                    //TODO
                    break;
                default:
                    break;
            }
        }
    };

    private void initMainTab() {
        conversationListFragment = new ConversationListFragment();
        mHost.setChecked(0);
        adapter = new MainTabAdpter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        adapter.clear();
//        viewpager.setScrollble(false);
        viewpager.setOffscreenPageLimit(4);
        adapter.addFragment(new ConversationListFragment(), getString(R.string.app_name));
        adapter.addFragment(new ContactListFragment(), getString(R.string.contacts));
        adapter.addFragment(new Fragment_Dicover(), getString(R.string.discover));
        adapter.addFragment(new Fragment_Profile(), getString(R.string.me));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void findView() {
        getSwipeBackLayout().setEnableGesture(false);
    }

    @Override
    protected void initView() {
        img_right.setVisibility(View.VISIBLE);
        img_right.setImageResource(R.drawable.icon_add);
        mHost.setOnCheckedChangeListener(this);
        mHost.setChecked(0);
        viewpager.setOnPageChangeListener(this);
        mHost.setHasNew(2, true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        EventManager.ins().registListener(EventTag.ACCOUNT_RELOGOUT, listener);
        EventManager.ins().registListener(EventTag.CHAT_NOTIFY_MSG, listener);
    }

    @Override
    public void onCheckedChange(int checkedPosition, boolean byUser) {
        setTabMsg(checkedPosition);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mHost.setChecked(position);
        setTabMsg(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //购物点击事件
    @OnClick(R.id.img_right)
    public void img_right_Click() {
        if (PageIndex == 0) {
            titlePopup.show(findViewById(R.id.layout_title));
        } else {
            //TODO
            MFGT.gotoCommon(MainActivity.this, getString(R.string.menu_addfriend));
        }
    }

    private void setTabMsg(int checkedPosition) {
        PageIndex = checkedPosition;
        viewpager.setCurrentItem(checkedPosition);
        switch (checkedPosition) {
            case 0:
                img_right.setVisibility(View.VISIBLE);
                img_right.setImageResource(R.drawable.icon_add);
                txt_title.setText(getString(R.string.app_name));
                break;
            case 1:
                img_right.setVisibility(View.VISIBLE);
                img_right.setImageResource(R.drawable.icon_titleaddfriend);
                txt_title.setText(getString(R.string.contacts));
                break;
            case 2:
                img_right.setVisibility(View.GONE);
                txt_title.setText(getString(R.string.discover));
                break;
            case 3:
                img_right.setVisibility(View.GONE);
                txt_title.setText(getString(R.string.me));
                break;
        }
    }


    EventListener listener = new EventListener() {
        @Override
        public void handleMessage(int what, int arg1, int arg2, Object dataobj) {
            switch (what) {
                case EventTag.CHAT_NOTIFY_MSG:
//                    if (PageIndex == 0) {
//                        // 当前页面如果为聊天历史页面，刷新此页面
//                        if (conversationListFragment != null) {
//                            conversationListFragment.refresh();
//                        }
//                    }
                    mHost.setUnreadCount(0, getUnreadMsgCountTotal());
                    break;
                case EventTag.ACCOUNT_FRIEND_UPDATE:
                    mHost.setUnreadCount(1, getUnreadAddressCountTotal());
                    break;
                case EventTag.ACCOUNT_RELOGOUT:
                    CommonUtils.showLongToast((String) dataobj);
                    MFGT.gotoGuide(MainActivity. this);
                    break;
            }
        }
    };

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(this);
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }
}
