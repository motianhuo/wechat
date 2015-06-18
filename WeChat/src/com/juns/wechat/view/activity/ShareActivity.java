package com.juns.wechat.view.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//朋友圈分享页面
public class ShareActivity extends BaseActivity implements OnClickListener {
	private TextView txt_title, txt_right;
	private ImageView img_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_share);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("朋友圈分享");
		txt_right = (TextView) this.findViewById(R.id.txt_right);
		txt_right.setText("发送");
		txt_right.setTextColor(0xFF45C01A);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// TODO
			Utils.finish(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent);
			} else if (type.startsWith("image/")) {
				handleSendImage(intent);
			}
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			if (type.startsWith("image/")) {
				handleSendMultipleImages(intent);
			}
		} else {
			// Handle other intents, such as being started from the home screen
		}
	}

	void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		String sharedTitle = intent.getStringExtra(Intent.EXTRA_TITLE);
		if (sharedText != null) {
			// Update UI to reflect text being shared
			Utils.showLongToast(context, sharedText);
		}
	}

	void handleSendImage(Intent intent) {
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
			Utils.showLongToast(context, imageUri.toString());
		}
	}

	void handleSendMultipleImages(Intent intent) {
		ArrayList<Uri> imageUris = intent
				.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		if (imageUris != null) {
			// Update UI to reflect multiple images being shared
		}
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
		txt_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(ShareActivity.this);
			break;
		case R.id.txt_right:
			break;
		default:
			break;
		}
	}

}
