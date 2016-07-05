package com.allenjuns.wechat.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;

public class EmptyLayout {
    public static final int STATE_LOADING = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_CONTENT = 4;

    private Context mContext;
    private FrameLayout mBackgroundView;
    private ViewGroup mLoadingView;
    private ViewGroup mEmptyView;
    private ViewGroup mErrorView;
    private View mContentView;
    private ImageView iv_empty, iv_error;
    private TextView tv_empty, tv_error;
    private Button btn_empty, btn_error;
    private LayoutInflater mInflater;
    private boolean mViewsAdded = false;
    private View.OnClickListener mEmptyButtonClickListener;
    private View.OnClickListener mErrorButtonClickListener;

    private int state = STATE_LOADING;
    private int bg_color = 0;
    private int emptyDrawable = 0;
    private int errorDrawable = 0;
    private float lineSpacingExtra = 0;
    private String emptyStr;
    private String errorStr;
    private String emptyButtonTitle;
    private String errorButtonTitle;
    private boolean isShowEmptyButton = false;
    private boolean isShowErrorButton = false;
    private int marginTop = 0;

    public EmptyLayout(Context context, View contentView) {
        this.mContext = context;
        this.mContentView = contentView;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initDefaultValues();
    }

    public void setBackgroundColor(int color) {
        this.bg_color = color;
    }

    public void setEmptyText(String emptyStr) {
        this.emptyStr = emptyStr;
    }

    public void setErrorText(String errorStr) {
        this.errorStr = errorStr;
    }

    public void setEmptyDrawable(int drawable) {
        this.emptyDrawable = drawable;
    }

    public void setErrorDrawable(int drawable) {
        this.errorDrawable = drawable;
    }

    public void setLineSpacingExtra(float lineSpace) {
        this.lineSpacingExtra = lineSpace;
    }

    public void setEmptyButtonText(String emptyButtonTitle) {
        this.emptyButtonTitle = emptyButtonTitle;
    }

    public String getEmptyButtonText() {
        return emptyButtonTitle;
    }

    public void setErrorButtonText(String errorButtonTitle) {
        this.errorButtonTitle = errorButtonTitle;
    }

    public String getErrorButtonText() {
        return errorButtonTitle;
    }

    public void setEmptyButtonShow(boolean isShowEmptyButton) {
        this.isShowEmptyButton = isShowEmptyButton;
    }

    public void setErrorButtonShow(boolean isShowErrorButton) {
        this.isShowErrorButton = isShowErrorButton;
    }

    public void setEmptyButtonClickListener(View.OnClickListener emptyButtonClickListener) {
        this.mEmptyButtonClickListener = emptyButtonClickListener;
        if (btn_empty != null) {
            btn_empty.setOnClickListener(emptyButtonClickListener);
        }
    }

    public void setErrorButtonClickListener(View.OnClickListener errorButtonClickListener) {
        this.mErrorButtonClickListener = errorButtonClickListener;
        if (btn_error != null) {
            btn_error.setOnClickListener(errorButtonClickListener);
        }
    }

    public void showLoading() {
        this.state = STATE_LOADING;
        changeState();
    }

    public void showEmpty() {
        this.state = STATE_EMPTY;
        changeState();
    }

    public void showError() {
        this.state = STATE_ERROR;
        changeState();
    }

    public void showContent() {
        this.state = STATE_CONTENT;
        changeState();
    }

    public void showEmptyOrError(int errcode) {
        if (errcode == -1) {
            showError();
        } else {
            showEmpty();
        }
    }

    public void setMargin(int top) {
        this.marginTop = top;
    }

    /**
     * 初始化基本�??
     */
    private void initDefaultValues() {
        bg_color = Color.TRANSPARENT;
        emptyDrawable = R.drawable.server_error;
        errorDrawable = R.drawable.server_error;
        emptyStr = mContext.getString((R.string.def_empty_data_text));
        errorStr = mContext.getString((R.string.def_net_error_text));
        emptyButtonTitle = mContext.getString((R.string.def_empty_button_retry));
        errorButtonTitle = mContext.getString((R.string.def_empty_button_retry));
        lineSpacingExtra = 6;
        isShowEmptyButton = false;
        isShowErrorButton = true;
    }

    private void initView() {
        if (mLoadingView == null) {
            mLoadingView = (ViewGroup) mInflater.inflate(R.layout.def_loading_layout, null);
        }

        if (mEmptyView == null) {
            mEmptyView = (ViewGroup) mInflater.inflate(R.layout.def_empty_layout, null);
            iv_empty = (ImageView) mEmptyView.findViewById(R.id.iv_data_null);
            tv_empty = (TextView) mEmptyView.findViewById(R.id.tv_null_desc);
            btn_empty = (Button) mEmptyView.findViewById(R.id.btn_null);

            if (mEmptyButtonClickListener != null) {
                btn_empty.setOnClickListener(mEmptyButtonClickListener);
            }
        }

        if (mErrorView == null) {
            mErrorView = (ViewGroup) mInflater.inflate(R.layout.def_error_layout, null);
            iv_error = (ImageView) mErrorView.findViewById(R.id.iv_error);
            tv_error = (TextView) mErrorView.findViewById(R.id.tv_error_desc);
            btn_error = (Button) mErrorView.findViewById(R.id.btn_error);

            if (mErrorButtonClickListener != null) {
                btn_error.setOnClickListener(mErrorButtonClickListener);
            }
        }

        iv_empty.setImageResource(emptyDrawable);
        tv_empty.setText(emptyStr);
        tv_empty.setLineSpacing(lineSpacingExtra, 1f);
        btn_empty.setText(emptyButtonTitle);

        iv_error.setImageResource(errorDrawable);
        tv_error.setText(errorStr);
        btn_error.setText(errorButtonTitle);

        if (isShowEmptyButton) {
            btn_empty.setVisibility(View.VISIBLE);
        } else {
            btn_empty.setVisibility(View.GONE);
        }

        if (isShowErrorButton) {
            btn_error.setVisibility(View.VISIBLE);
        } else {
            btn_error.setVisibility(View.GONE);
        }

        if (!mViewsAdded) {
            mBackgroundView = new FrameLayout(mContext);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.topMargin = marginTop;
            lp.gravity = Gravity.CENTER;
            mBackgroundView.setBackgroundColor(bg_color);
            mBackgroundView.setLayoutParams(lp);

            if (mLoadingView != null) {
                mBackgroundView.addView(mLoadingView);
            }

            if (mEmptyView != null) {
                mBackgroundView.addView(mEmptyView);
            }

            if (mErrorView != null) {
                mBackgroundView.addView(mErrorView);
            }

            mViewsAdded = true;

            if (mContentView != null) {
                ((ViewGroup) mContentView.getParent()).addView(mBackgroundView);
            }

        }

    }

    private void changeState() {
        initView();

        if (mContentView == null)
            return;

        switch (state) {
            case STATE_LOADING:
                if (mBackgroundView != null) {
                    mBackgroundView.setVisibility(View.VISIBLE);
                }

                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.VISIBLE);
                }
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.GONE);
                }

                if (mErrorView != null) {
                    mErrorView.setVisibility(View.GONE);
                }
                mContentView.setVisibility(View.GONE);

                break;
            case STATE_EMPTY:
                if (mBackgroundView != null) {
                    mBackgroundView.setVisibility(View.VISIBLE);
                }

                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                }
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.VISIBLE);
                }

                if (mErrorView != null) {
                    mErrorView.setVisibility(View.GONE);
                }
                mContentView.setVisibility(View.GONE);

                break;
            case STATE_ERROR:
                if (mBackgroundView != null) {
                    mBackgroundView.setVisibility(View.VISIBLE);
                }

                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                }
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.GONE);
                }

                if (mErrorView != null) {
                    mErrorView.setVisibility(View.VISIBLE);
                }
                mContentView.setVisibility(View.GONE);

                break;
            case STATE_CONTENT:
                if (mBackgroundView != null) {
                    mBackgroundView.setVisibility(View.GONE);
                }

                mContentView.setVisibility(View.VISIBLE);
                mContentView.setAlpha(0f);
                mContentView.animate().alpha(1f).setDuration(800).start();
                break;

            default:
                break;
        }
    }
}
