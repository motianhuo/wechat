package com.juns.wechat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.health.net.loopj.android.http.RequestParams;
import com.juns.wechat.Constants;
import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.net.BaseJsonRes;
import com.juns.wechat.view.BaseActivity;

//注册后填写用户信息
public class EditUserNameActivity extends BaseActivity implements
		OnClickListener {
	private TextView txt_title;
	private ImageView img_back;
	private EditText edit_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_edit_userinfo);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("欢迎");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.GONE);
		edit_name = (EditText) findViewById(R.id.edit_name);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
		findViewById(R.id.btn_start).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(EditUserNameActivity.this);
			break;
		case R.id.btn_start:
			setUserMsg();
			break;
		default:
			break;
		}
	}

	private void setUserMsg() {
		String telphone = Utils.getValue(EditUserNameActivity.this,
				Constants.NAME);
		String name = edit_name.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Utils.showLongToast(EditUserNameActivity.this, "请填写您的昵称！ ");
			return;
		}
		RequestParams params = new RequestParams();
		params.put("username", name);
		params.put("telphone", telphone);
		getLoadingDialog("正在加载...  ").show();
		netClient.post(Constants.UpdateInfoURL, params, new BaseJsonRes() {

			@Override
			public void onMySuccess(String data) {
				Utils.putValue(EditUserNameActivity.this, Constants.UserInfo,
						data);
				getLoadingDialog("正在加载").dismiss();
				Intent intent = new Intent(EditUserNameActivity.this,
						MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				finish();
			}

			@Override
			public void onMyFailure() {
				getLoadingDialog("正在登录").dismiss();
			}
		});
	}

}
