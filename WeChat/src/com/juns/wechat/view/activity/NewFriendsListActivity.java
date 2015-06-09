package com.juns.wechat.view.activity;

import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.adpter.NewFriendsAdapter;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//新朋友
public class NewFriendsListActivity extends BaseActivity implements
		OnClickListener {
	private TextView txt_title, txt_right;
	private ImageView img_back;
	private ListView mlistview;
	private View layout_head;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_listview);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("新的朋友");
		txt_right = (TextView) findViewById(R.id.txt_right);
		txt_right.setText("添加朋友");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		mlistview = (ListView) findViewById(R.id.listview);
		layout_head = getLayoutInflater().inflate(
				R.layout.layout_head_newfriend, null);
		mlistview.addHeaderView(layout_head);
		mlistview.setAdapter(new NewFriendsAdapter(this));
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
		txt_right.setOnClickListener(this);
		layout_head.findViewById(R.id.txt_search).setOnClickListener(this);
		layout_head.findViewById(R.id.txt_tel).setOnClickListener(this);
		layout_head.findViewById(R.id.txt_qq).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(NewFriendsListActivity.this);
			break;
		case R.id.txt_right:
			Utils.start_Activity(this, PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, "添加朋友"));
			break;
		case R.id.txt_search:
			Utils.start_Activity(this, PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, "搜索"));
			break;
		case R.id.txt_tel:
			Utils.start_Activity(this, AddFromContactActivity.class);
			break;
		case R.id.txt_qq:
			Utils.start_Activity(this, PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, "添加QQ好友"));
			break;
		default:
			break;
		}
	}
}
