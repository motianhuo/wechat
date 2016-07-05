package com.allenjuns.wechat.app.module.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.utils.QRCodeUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :我的二维码
 * Author : AllenJuns
 * Date   : 2016-3-09
 */
public class MyCodeActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.img_code)
    ImageView img_code;
    @Bind(R.id.img_head)
    ImageView img_head;
    @Bind(R.id.img_sex)
    ImageView img_sex;
    @Bind(R.id.txt_name)
    TextView txt_name;
    @Bind(R.id.txt_id)
    TextView txt_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mycode);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        img_back.setVisibility(View.VISIBLE);
        txt_title.setText(getString(R.string.profile_txt_mycode));
    }

    @Override
    protected void initData() {
        Bitmap bitmap = QRCodeUtils.createQRCodeWithLogo("AllenJuns", 500,
                BitmapFactory.decodeResource(getResources(), R.drawable.head));
        img_code.setImageBitmap(bitmap);
    }

    @Override
    protected void setListener() {

    }

    //返回按钮点击事件
    @OnClick(R.id.img_back)
    public void close() {
        MFGT.finish(this);
    }
}
