package com.allenjuns.wechat.app.module.money;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.widget.ScrollGridView;
import com.easemob.redpacketui.utils.RedPacketUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :钱包
 * Author : AllenJuns
 * Date   : 2016-3-08
 */
public class WalletActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.gridView)
    ScrollGridView gridView;
    private final static String[] names = new String[]{"刷卡", "转账", "手机充值",
            "理财通", "滴滴出行", "生活缴费", "电影票", "美丽说", "京东精选", "信用卡还款", "微信红包",
            "火车票机票", "吃喝玩乐", "腾讯公益", "AA收款"};
    private final static int[] images = new int[]{R.drawable.w1,
            R.drawable.w2, R.drawable.w3, R.drawable.w4, R.drawable.w5,
            R.drawable.w6, R.drawable.w7, R.drawable.w8, R.drawable.w9,
            R.drawable.w10, R.drawable.w11, R.drawable.w12, R.drawable.w13,
            R.drawable.w14, R.drawable.w15};

        private Myadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
        txt_title.setText(getString(R.string.profile_txt_money));
        gridView.setFocusable(false);
    }

    @Override
    protected void initData() {
        adapter = new Myadapter(WalletActivity.this, images, names);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
    }

    //返回按钮点击事件
    @OnClick(R.id.img_back)
    public void close() {
        MFGT.finish(this);
    }
    //返回按钮点击事件
    @OnClick(R.id.layout_money)
    public void layout_money() {
        RedPacketUtil.startChangeActivity(this);
    }

    class Myadapter extends BaseAdapter {

        private LayoutInflater inflater;
        private int[] imageDatas;
        private String[] nameDatas;

        public Myadapter(Context context, int[] imageDatas, String[] nameDatas) {
            inflater = LayoutInflater.from(context);

            this.imageDatas = imageDatas;
            this.nameDatas = nameDatas;
        }

        @Override
        public int getCount() {
            return imageDatas.length;
        }

        @Override
        public Object getItem(int position) {
            return imageDatas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.layout_item_wallet, null, false);
            ImageView iv_image = (ImageView) convertView
                    .findViewById(R.id.iv_image);
            TextView tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            String name = nameDatas[position];
            int imageRes = imageDatas[position];
            iv_image.setImageResource(imageRes);
            tv_name.setText(name);
            return convertView;
        }
    }

}
