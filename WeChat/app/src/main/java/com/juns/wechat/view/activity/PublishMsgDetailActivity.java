package com.juns.wechat.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.adpter.PublishMsgDetailAdpter;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//订阅号信息详情
public class PublishMsgDetailActivity extends BaseActivity implements
		OnClickListener {
	private TextView txt_title;
	private ImageView img_talk;
	private ListView mlistview;
	private View layout_head;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_listview);
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
		img_talk = (ImageView) findViewById(R.id.img_right);
		img_talk.setVisibility(View.VISIBLE);
		img_talk.setImageResource(R.drawable.icon_chat_user);
		mlistview = (ListView) findViewById(R.id.listview);
		mlistview.setAdapter(new PublishMsgDetailAdpter(this));
		mlistview.setSelection(2);
	}

	@Override
	protected void initView() {
		String title = getIntent().getStringExtra(Constants.NAME);
		txt_title.setText(title);
	}

	@Override
	protected void setListener() {
		findViewById(R.id.img_back).setOnClickListener(this);
		img_talk.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(PublishMsgDetailActivity.this);
			break;
		case R.id.img_right:
			break;
		default:
			break;
		}
	}

}
