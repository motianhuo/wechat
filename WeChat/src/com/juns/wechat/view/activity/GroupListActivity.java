package com.juns.wechat.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.adpter.MyGroupAdpter;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//群聊列表
public class GroupListActivity extends BaseActivity implements OnClickListener {
	private TextView txt_title;
	private ImageView img_back, img_right;
	private ListView mlistview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_listview);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("群聊");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		img_right = (ImageView) findViewById(R.id.img_right);
		img_right.setImageResource(R.drawable.icon_add);
		img_right.setVisibility(View.VISIBLE);
		mlistview = (ListView) findViewById(R.id.listview);
		View layout_head = getLayoutInflater().inflate(
				R.layout.layout_head_search, null);
		mlistview.addHeaderView(layout_head);
	}

	@Override
	protected void initView() {
		new Thread(new Runnable() {

			@Override
			public void run() {
					
					if (GloableParams.ListGroupInfos != null && GloableParams.ListGroupInfos.size() > 0) {
						mlistview.setAdapter(new MyGroupAdpter(
								GroupListActivity.this, GloableParams.ListGroupInfos));
					} else {
						TextView txt_nodata = (TextView) findViewById(R.id.txt_nochat);
						txt_nodata.setText("暂时没有群聊");
						txt_nodata.setVisibility(View.VISIBLE);
					}
			}
		}).start();

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
		img_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(GroupListActivity.this);
			break;
		case R.id.img_right:
			Utils.start_Activity(GroupListActivity.this,
					AddGroupChatActivity.class);
			break;
		default:
			break;
		}
	}

}
