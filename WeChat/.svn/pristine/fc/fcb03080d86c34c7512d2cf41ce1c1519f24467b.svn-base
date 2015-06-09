package com.juns.wechat.view.activity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//收钱
public class GetMoneyActivity extends BaseActivity implements OnClickListener {

	private TextView txt_title, txt_right;
	private ImageView img_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_getmoney);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(GetMoneyActivity.this);
			break;

		default:
			break;
		}
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		// String strname =
		// "收钱<font color='#ff11aca6'><small>(微信支付)</small></font> ";
		// txt_title.setText(Html.fromHtml(strname));
		txt_title.setText("微信支付");
		txt_right = (TextView) findViewById(R.id.txt_right);
		txt_right.setText("设置金额");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initView() {
		img_back.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		String id = Utils.getValue(this, Constants.User_ID);
		Bitmap qrcode = generateQRCode("JUNS_WeChat@getMoney:" + id + ",188");
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
