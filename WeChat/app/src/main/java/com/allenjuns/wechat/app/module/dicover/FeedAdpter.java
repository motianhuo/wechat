package com.allenjuns.wechat.app.module.dicover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.allenjuns.wechat.R;


public class FeedAdpter extends BaseAdapter {
	protected Context context;
	LayoutInflater mInflater;

	public FeedAdpter(Context ctx) {
		context = ctx;
	}

	@Override
	public int getCount() {
		return 15;
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
					R.layout.layout_item_feed, parent, false);
		}

		return convertView;
	}
}
