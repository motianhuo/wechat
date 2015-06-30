package com.juns.wechat.view.activity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.bean.User;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

public class MyCodeActivity extends BaseActivity implements OnClickListener {

	private TextView txt_title, txt_right, tvname, tv_accout;
	private ImageView img_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mycode);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(MyCodeActivity.this);
			break;

		default:
			break;
		}
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("我的二维码");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		tvname = (TextView) findViewById(R.id.tvname);
		tv_accout = (TextView) findViewById(R.id.tvmsg);
	}

	@Override
	protected void initView() {
		img_back.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		String id = Utils.getValue(this, Constants.User_ID);
		tv_accout.setText("微信号：" + id);
		User user = GloableParams.Users.get(id);
		if (user != null && !TextUtils.isEmpty(user.getUserName()))
			tvname.setText(user.getUserName());
		Bitmap qrcode = generateQRCode("JUNS_WeChat@User:" + id);
		ImageView imageView = (ImageView) findViewById(R.id.img_code);
		imageView.setImageBitmap(qrcode);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
		int w = matrix.getWidth();
		int h = matrix.getHeight();
		int[] rawData = new int[w * h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int color = Color.WHITE;
				if (matrix.get(i, j)) {
					color = Color.BLACK;
				}
				rawData[i + (j * w)] = color;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.RGB_565);
		bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
		return bitmap;
	}

	private Bitmap generateQRCode(String content) {
		try {
			QRCodeWriter writer = new QRCodeWriter();
			// MultiFormatWriter writer = new MultiFormatWriter();
			BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE,
					500, 500);
			return bitMatrix2Bitmap(matrix);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
