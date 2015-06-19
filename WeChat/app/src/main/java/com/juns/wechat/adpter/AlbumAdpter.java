package com.juns.wechat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.juns.wechat.R;

public class AlbumAdpter extends BaseAdapter {
	protected Context context;
	LayoutInflater mInflater;

	public AlbumAdpter(Context ctx) {
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
					R.layout.layout_item_album, parent, false);
		}

		return convertView;
	}
}
