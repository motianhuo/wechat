package com.allenjuns.wechat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ScrollGridView extends GridView
{

	public ScrollGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}

}
