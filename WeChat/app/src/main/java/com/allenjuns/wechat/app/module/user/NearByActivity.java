package com.allenjuns.wechat.app.module.user;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.app.module.user.adpter.NearByAdpter;
import com.allenjuns.wechat.bean.UserInfo;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.widget.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :
 * Author : AllenJuns
 * Date   : 2016-3-15
 */
public class NearByActivity extends BaseActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener{
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.swipe_view)
    SwipeFlingAdapterView swipeView;
    String [] headerIcons = {
            "http://img0.imgtn.bdimg.com/it/u=3959292123,1789415419&fm=21&gp=0.jpg",
            "http://tupian.enterdesk.com/uploadfile/2014/1126/20141126102508269.jpg",
            "http://d.3987.com/sif_150301/desk_004.jpg",
            "http://img1.imgtn.bdimg.com/it/u=358325221,1288320732&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3382799710,1639843170&fm=21&gp=0.jpg",
            "http://image.tianjimedia.com/uploadImages/2014/127/37/3ZOEO4C10NZG.jpg",
            "http://image93.360doc.com/DownloadImg/2016/01/1713/64616030_8.jpg",
            "http://img2.imgtn.bdimg.com/it/u=414263374,1235451346&fm=21&gp=0.jpg",
            "http://pic29.nipic.com/20130501/12558275_114724775130_2.jpg",
            "http://photo.l99.com/bigger/20/1415193157174_j2fa5b.jpg"};
    String [] names = {"张三","李四","王五","小明","小红","小花"};

    Random ran = new Random();
    private int cardWidth;
    private int cardHeight;
    private NearByAdpter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_nearby);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
        txt_title.setText(getString(R.string.discover_txt_nearby));
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));
    }

    @Override
    protected void initData() {
        loadData();
    }
    private void loadData() {
        ArrayList<UserInfo> list = new ArrayList<>(10);
        UserInfo talent;
        for (int i = 0; i < 10; i++) {
            talent = new UserInfo();
            talent.setUser_avar(headerIcons[i]);
            talent.setNickname( names[ran.nextInt(names.length - 1)]);
            list.add(talent);
        }
        adapter = new NearByAdpter(this);
        adapter.addAll(list);
        swipeView.setAdapter(adapter);
    }
    @Override
    protected void setListener() {
        swipeView.setFlingListener(this);
        swipeView.setOnItemClickListener(this);
    }
    //返回按钮点击事件
    @OnClick(R.id.img_back)
    public void close() {
        MFGT.finish(this);
    }

    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {

    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {
//        CommonUtils.showLongToast("swipe left card");
    }

    @Override
    public void onRightCardExit(Object dataObject) {
//        CommonUtils.showLongToast("swipe right card");
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == 3) {
            loadData();
        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {
    }

}
