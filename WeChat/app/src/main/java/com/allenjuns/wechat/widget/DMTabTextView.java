package com.allenjuns.wechat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allenjuns.wechat.R;


public class DMTabTextView extends RelativeLayout {
    private TextView tab_button;
    private View view_line;
    private boolean isChecked = false;

    public DMTabTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs);
    }

    public DMTabTextView(Context context) {
        super(context);
    }

    public DMTabTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.main_tab_text_item, this);
        tab_button = (TextView) contentView.findViewById(R.id.tv_tab_item_text);
        view_line = contentView.findViewById(R.id.view_line);
        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.tab_button);
        String text = a.getString(R.styleable.tab_button_tabtext);
        a.recycle();
        tab_button.setText(text);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected)
            view_line.setVisibility(VISIBLE);
        else
            view_line.setVisibility(INVISIBLE);
    }
}
