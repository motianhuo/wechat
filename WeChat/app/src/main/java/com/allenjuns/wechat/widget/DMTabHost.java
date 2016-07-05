package com.allenjuns.wechat.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class DMTabHost extends LinearLayout implements OnClickListener {
    public DMTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof DMTabButton || view instanceof DMTabTextView) {
                view.setOnClickListener(this);
            }
        }
    }

    public void setChecked(int position) {
        setChecked(position, false);
    }

    private void setChecked(int position, boolean byUser) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof DMTabButton || view instanceof DMTabTextView) {
                view.setSelected(position == i);
                if (position == i && mListener != null) {
                    mListener.onCheckedChange(position, byUser);
                }
            }
        }
    }

    /**
     * 设置是否有更新
     *
     * @param position
     * @param hasNew
     */
    public void setHasNew(int position, boolean hasNew) {
        DMTabButton button = (DMTabButton) getChildAt(position);
        if (button != null) {
            button.setHasNew(hasNew);
        }
    }

    /**
     * 设置未读数
     *
     * @param position
     * @param count
     */
    public void setUnreadCount(int position, int count) {
        DMTabButton button = (DMTabButton) getChildAt(position);
        if (button != null) {
            button.setUnreadCount(count);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (v == view) {
                setChecked(i, true);
                break;
            }
        }
    }

    private OnCheckedChangeListener mListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(int checkedPosition, boolean byUser);
    }
}
