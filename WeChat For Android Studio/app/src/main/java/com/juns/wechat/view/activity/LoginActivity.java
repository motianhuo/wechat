package com.juns.wechat.view.activity;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.juns.health.net.loopj.android.http.RequestParams;
import com.juns.wechat.Constants;
import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.common.DES;
import com.juns.wechat.common.Utils;
import com.juns.wechat.net.BaseJsonRes;
import com.juns.wechat.view.BaseActivity;

//登陆
public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView txt_title;
	private ImageView img_back;
	private Button btn_login, btn_register;
	private EditText et_usertel, et_password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("登陆");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_qtlogin);
		et_usertel = (EditText) findViewById(R.id.et_usertel);
		et_password = (EditText) findViewById(R.id.et_password);
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
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		findViewById(R.id.tv_wenti).setOnClickListener(this);
		et_usertel.addTextChangedListener(new TextChange());
		et_password.addTextChangedListener(new TextChange());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(LoginActivity.this);
			break;
		case R.id.tv_wenti:
			Utils.start_Activity(LoginActivity.this, WebViewActivity.class,
					new BasicNameValuePair(Constants.Title, "帮助"),
					new BasicNameValuePair(Constants.URL,
							"http://weixin.qq.com/"));
			break;
		case R.id.btn_qtlogin:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;
		case R.id.btn_login:
			getLogin();
			break;
		default:
			break;
		}
	}

	private void getLogin() {
		String userName = et_usertel.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		getLoadingDialog("正在登录...").show();
		getLogin(userName, password);
	}

	private void getLogin(final String userName, final String password) {
		if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
			RequestParams params = new RequestParams();
			params.put("username", userName);
			params.put("password", DES.md5Pwd(password));
			getLoadingDialog("正在登录...  ").show();
			netClient.post(Constants.Login_URL, params, new BaseJsonRes() {

				@Override
				public void onMySuccess(String data) {
					Utils.putValue(LoginActivity.this, Constants.UserInfo, data);
					Utils.putBooleanValue(LoginActivity.this,
							Constants.LoginState, true);
					Utils.putValue(LoginActivity.this, Constants.NAME, userName);
					Utils.putValue(LoginActivity.this, Constants.PWD,
							DES.md5Pwd(password));
					getChatserive(userName, DES.md5Pwd(password));
				}

				@Override
				public void onMyFailure() {
					getLoadingDialog("正在登录").dismiss();
				}
			});
		} else {
			Utils.showLongToast(LoginActivity.this, "请填写账号或密码！");
		}
	}

	private void getChatserive(final String userName, final String password) {
		EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							public void run() {
								Utils.putBooleanValue(LoginActivity.this,
										Constants.LoginState, true);
								Utils.putValue(LoginActivity.this,
										Constants.User_ID, userName);
								Utils.putValue(LoginActivity.this,
										Constants.PWD, password);
								Log.d("main", "登陆聊天服务器成功！");
								// 加载群组和会话
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance()
										.loadAllConversations();
								getLoadingDialog("正在登录...").dismiss();
								Intent intent = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivity(intent);
								overridePendingTransition(R.anim.push_up_in,
										R.anim.push_up_out);
								finish();
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "登陆聊天服务器失败！");
						runOnUiThread(new Runnable() {
							public void run() {
								getLoadingDialog("正在登录...").dismiss();
								Utils.showLongToast(LoginActivity.this, "登陆失败！");
							}
						});
					}
				});
	}

	// EditText监听器
	class TextChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {
			boolean Sign2 = et_usertel.getText().length() > 0;
			boolean Sign3 = et_password.getText().length() > 4;
			if (Sign2 & Sign3) {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_bg_green));
				btn_login.setEnabled(true);
			} else {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_enable_green));
				btn_login.setTextColor(0xFFD0EFC6);
				btn_login.setEnabled(false);
			}
		}
	}

}
