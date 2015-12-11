package com.juns.wechat.view.activity;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.adpter.PublishMsgAdpter;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//订阅号信息列表
public class PublishMsgListActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private TextView txt_title;
	private ImageView img_back;
	private ListView mlistview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_listview);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("订阅号");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		mlistview = (ListView) findViewById(R.id.listview);
		mlistview.setAdapter(new PublishMsgAdpter(this));
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		// TODO 根据时间排序加载 订阅号信息列表
	}

	@Override
	protected void setListener() {
		mlistview.setOnItemClickListener(this);
		img_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(PublishMsgListActivity.this);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Utils.start_Activity(this, PublishMsgDetailActivity.class,
				new BasicNameValuePair(Constants.NAME, "魔方陪你玩"));
	}

}
