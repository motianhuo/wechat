package com.juns.wechat.view.activity;

import java.lang.reflect.InvocationTargetException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//浏览器
public class WebViewActivity extends BaseActivity {
	private ImageView img_back;
	private TextView txt_title;
	private WebView mWebView;
	private ProgressBar progressbar;
	private String strurl = "";
	private MyTimer mTimer;
	private int progress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_web);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			mWebView.getClass().getMethod("onResume")
					.invoke(mWebView, (Object[]) null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			mWebView.getClass().getMethod("onPause")
					.invoke(mWebView, (Object[]) null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		mWebView = (WebView) findViewById(R.id.mwebview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initView() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getString(Constants.URL) != null) {
			strurl = "";
			strurl = bundle.getString(Constants.URL);
		}
		if (bundle != null && bundle.getString(Constants.Title) != null) {
			txt_title.setText(bundle.getString(Constants.Title));
		}
		if (!TextUtils.isEmpty(strurl)) {
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.setWebViewClient(new WeiboWebViewClient());
			mWebView.setWebChromeClient(new WebChromeClient());
			mWebView.loadUrl(strurl);
		}
	}

	private class WeiboWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if (mTimer == null) {
				mTimer = new MyTimer(15000, 50);
			}
			mTimer.start();
			progressbar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mTimer.cancel();
			progress = 0;
			progressbar.setProgress(100);
			progressbar.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initData() {

	}

	/* 定义一个倒计时的内部类 */
	private class MyTimer extends CountDownTimer {
		public MyTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			progress = 100;
			progressbar.setVisibility(View.GONE);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if (progress == 100) {
				progressbar.setVisibility(View.GONE);
			} else {
				progressbar.setProgress(progress++);
			}
		}
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.finish(WebViewActivity.this);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack())
				mWebView.goBack();
			else
				Utils.finish(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
