package com.juns.wechat.view.activity;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//设置
public class SettingActivity extends BaseActivity implements OnClickListener {

	private TextView txt_title, txt_tip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		findViewById();
		initViews();
		setOnListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewById() {
		findViewById(R.id.img_back).setVisibility(View.VISIBLE);
		findViewById(R.id.txt_right).setVisibility(View.GONE);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("设置");
	}

	private void initViews() {
	}

	private void setOnListener() {
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.btnexit).setOnClickListener(this);
		findViewById(R.id.txt_msgtip).setOnClickListener(this);
		findViewById(R.id.txt_usersafe).setOnClickListener(this);
		findViewById(R.id.txt_yinsi).setOnClickListener(this);
		findViewById(R.id.txt_about).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(SettingActivity.this);
			break;
		case R.id.txt_about:
			Utils.start_Activity(SettingActivity.this, WebViewActivity.class,
					new BasicNameValuePair(Constants.Title, "关于微信"),
					new BasicNameValuePair(Constants.URL,
							"https://github.com/motianhuo/wechat"));
			break;

		case R.id.btnexit:
			EMChatManager.getInstance().logout();// 退出环信聊天
			Utils.RemoveValue(context, Constants.LoginState);
			Utils.RemoveValue(context, Constants.UserInfo);
			Utils.RemoveValue(context, Constants.NAME);
			Utils.RemoveValue(context, Constants.PWD);
			App.getInstance2().exit();
			Utils.start_Activity(this, LoginActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void initControl() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

}
