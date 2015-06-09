package com.juns.wechat.chat.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ExpandGridView extends GridView {

	public ExpandGridView(Context context) {
		super(context);
	}

	public ExpandGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
