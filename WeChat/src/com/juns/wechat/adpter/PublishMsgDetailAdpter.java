package com.juns.wechat.adpter;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.view.activity.WebViewActivity;

//订阅号信息详情页面
public class PublishMsgDetailAdpter extends BaseAdapter {
	protected Context context;

	public PublishMsgDetailAdpter(Context ctx) {
		context = ctx;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			if (position == 0) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_item_publishmsgdetail, parent, false);
				convertView.setOnClickListener(onclicklister);
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_item_publishmsgdetail2, parent, false);
				View layout_msg1 = ViewHolder
						.get(convertView, R.id.layout_msg1);
				View layout_msg2 = ViewHolder
						.get(convertView, R.id.layout_msg2);
				View layout_msg3 = ViewHolder
						.get(convertView, R.id.layout_msg3);
				layout_msg1.setOnClickListener(onclicklister);
				layout_msg2.setOnClickListener(onclicklister);
				layout_msg3.setOnClickListener(onclicklister);
			}
		}

		return convertView;
	}

	private OnClickListener onclicklister = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Utils.start_Activity(
					(Activity) context,
					WebViewActivity.class,
					new BasicNameValuePair(Constants.Title, "魔方陪你玩"),
					new BasicNameValuePair(
							Constants.URL,
							"http://mp.weixin.qq.com/s?__biz=MzA4NzA3NzAzNg==&mid=401118458&idx=1&sn=f7023910ab455d316121bbd32b80cb74&scene=0#wechat_redirect"));
		}
	};
}
