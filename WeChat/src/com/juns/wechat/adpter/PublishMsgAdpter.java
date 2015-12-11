package com.juns.wechat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.common.ViewHolder;

public class PublishMsgAdpter extends BaseAdapter {
	protected Context context;
	LayoutInflater mInflater;

	public PublishMsgAdpter(Context ctx) {
		context = ctx;
	}

	@Override
	public int getCount() {
		return 5;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_item_msg, parent, false);
		}
		ImageView img_avar = ViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
		TextView txt_del = ViewHolder.get(convertView, R.id.txt_del);
		TextView txt_content = ViewHolder.get(convertView, R.id.txt_content);
		TextView txt_time = ViewHolder.get(convertView, R.id.txt_time);
		TextView unreadLabel = ViewHolder.get(convertView,
				R.id.unread_msg_number);
		img_avar.setImageResource(R.drawable.icon_public);
		txt_name.setText("魔方陪你玩");
		txt_content.setText("最强音浪对抗《全民枪战2.0》上首款主题套装");
		txt_time.setText("昨天");
		if (position == 0) {
			unreadLabel.setText("3");
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.GONE);
		}
		return convertView;
	}
}
