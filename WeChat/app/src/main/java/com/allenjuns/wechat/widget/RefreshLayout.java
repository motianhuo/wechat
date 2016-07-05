package com.allenjuns.wechat.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.TextView;

import com.allenjuns.wechat.R;


public class RefreshLayout extends SwipeRefreshLayout {
    private final int mTouchSlop;
    private ListView mListView;
    private OnLoadListener mOnLoadListener;

    private float firstTouchY;
    private float lastTouchY;

    private boolean isLoading = false;
    private    View footerLayout;
    private TextView textMore;
    private ProgressBarCircular progressBar;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    // set the child view of RefreshLayout,ListView
    public void setChildView(Activity context, ListView mListView) {
        this.mListView = mListView;
          footerLayout = context.getLayoutInflater().inflate(R.layout.listview_footer, null);
        textMore = (TextView) footerLayout.findViewById(R.id.text_more);
        progressBar = (ProgressBarCircular) footerLayout.findViewById(R.id.load_progress_bar);
        textMore.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mListView.addFooterView(footerLayout);
        textMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                firstTouchY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                lastTouchY = event.getRawY();
                if (canLoadMore()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private boolean canLoadMore() {
        return isBottom() && !isLoading && isPullingUp();
    }

    private boolean isBottom() {
        if (mListView.getCount() > 0) {
            if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1
                    && mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPullingUp() {
        return (firstTouchY - lastTouchY) >= mTouchSlop;
    }

    private void loadData() {
        if (mOnLoadListener != null) {
            footerLayout.setVisibility(View.VISIBLE);
            setLoading(true);
        }
    }

    public void setLoading(boolean loading) {
        if (mListView == null)
            return;
        isLoading = loading;
        if (loading) {
            if (isRefreshing()) {
                setRefreshing(false);
            }
            mListView.setSelection(mListView.getAdapter().getCount() - 1);
            textMore.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            mOnLoadListener.onLoad();
        } else {
            firstTouchY = 0;
            lastTouchY = 0;
        }
    }

    public void loadMoreFinish(boolean finish) {
        if (finish) {
            textMore.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            textMore.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    public interface OnLoadListener {
        public void onLoad();
    }
}
