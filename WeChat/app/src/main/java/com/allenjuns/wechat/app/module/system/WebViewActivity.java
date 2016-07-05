package com.allenjuns.wechat.app.module.system;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.app.base.BaseActivity;
import com.allenjuns.wechat.common.Constants;
import com.allenjuns.wechat.common.MFGT;
import com.allenjuns.wechat.widget.EmptyLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description :内部浏览器
 * Author : AllenJuns
 * Date   : 2016-3-08
 */
public class WebViewActivity extends BaseActivity {
    @Bind(R.id.txt_title)
    TextView txt_title;
    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.mwebview)
    WebView mWebView;
    private String strUrl = "";
    private EmptyLayout emptyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
        img_back.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(Constants.URL) != null) {
            strUrl = bundle.getString(Constants.URL);
        }
        if (bundle != null && bundle.getString(Constants.TITLE) != null) {
            txt_title.setText(bundle.getString(Constants.TITLE));
        }
        emptyLayout = new EmptyLayout(this, mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WeiboWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(strUrl);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        emptyLayout.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl(strUrl);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                MFGT.finish(this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //返回按钮点击事件
    @OnClick(R.id.img_back)
    public void close() {
        MFGT.finish(this);
    }

    private class WeiboWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            emptyLayout.showLoading();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            emptyLayout.showContent();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            emptyLayout.showError();
        }
    }
}
