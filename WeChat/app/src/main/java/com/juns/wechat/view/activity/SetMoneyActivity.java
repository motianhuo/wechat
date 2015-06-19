package com.juns.wechat.view.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.bean.User;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//付钱
public class SetMoneyActivity extends BaseActivity implements OnClickListener {
	private TextView txt_title, tv_msg, tv_money;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_setmoney);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void initControl() {
		findViewById(R.id.img_back).setVisibility(View.VISIBLE);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("微信支付");
		tv_msg = (TextView) findViewById(R.id.tv_msg);
		tv_money = (TextView) findViewById(R.id.tv_money);

	}

	@Override
	protected void initView() {
		String UserId = getIntent().getStringExtra(Constants.User_ID);
		String money = getIntent().getStringExtra(Constants.NAME);
		User user = GloableParams.Users.get(UserId);
		String strname = "您正在向 " + user.getUserName() + "<br/>微信号：" + UserId
				+ " 付钱";
		tv_msg.setText(Html.fromHtml(strname));
		String strmoney = "￥<font color='#ff11aca6'>" + money
				+ "<small>元</small></font> ";
		tv_money.setText(Html.fromHtml(strmoney));
	}

	@Override
	protected void setListener() {
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.btn_pay).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(SetMoneyActivity.this);
			break;
		case R.id.btn_pay:
			Utils.showLongToast(this, "付款成功！");
			Utils.finish(this);
			break;
		default:
			break;
		}
	}

}
