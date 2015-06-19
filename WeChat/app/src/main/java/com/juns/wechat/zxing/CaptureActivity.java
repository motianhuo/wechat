package com.juns.wechat.zxing;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.SplashActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.activity.FriendMsgActivity;
import com.juns.wechat.view.activity.SetMoneyActivity;
import com.juns.wechat.zxing.camera.CameraManager;
import com.juns.wechat.zxing.decoding.CaptureActivityHandler;
import com.juns.wechat.zxing.decoding.InactivityTimer;
import com.juns.wechat.zxing.view.ViewfinderView;

/**
 * @ClassName: CaptureActivity
 * @Description: 二维码扫描
 * @author juns
 * @date 2013-8-14
 */
public class CaptureActivity extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private TextView mTitle;
	private ImageView mGoHome;
	private boolean isNoCute = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_code_scan);
		CameraManager.init(getApplication());
		initControl();
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getBoolean("isNoCute")) {
			isNoCute = true;
		} else {
			isNoCute = false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isNoCute) {
				Utils.finish(CaptureActivity.this);
			} else {
				Utils.start_Activity(CaptureActivity.this, SplashActivity.class);
				Utils.finish(CaptureActivity.this);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initControl() {
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mTitle = (TextView) findViewById(R.id.common_title_msg);
		mTitle.setText("扫一扫");
		mGoHome = (ImageView) findViewById(R.id.img_back);
		mGoHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.finish(CaptureActivity.this);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	public void onDestroy() {
		if (inactivityTimer != null)
			inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	/**
	 * 扫描正确后的震动声音,如果感觉apk大了,可以删除
	 */
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	public void handleDecode(com.google.zxing.Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();

		final String resultString = result.getText();
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("result", resultString);
		resultIntent.putExtras(bundle);
		this.setResult(RESULT_OK, resultIntent);

		if (!isNoCute) {
			if (TextUtils.isEmpty(resultString)) {
				Utils.showLongToast(CaptureActivity.this, "二维码信息错误！");
				return;
			} else {
				if (resultString.startsWith("JUNS_WeChat@User")) {
					String[] name = resultString.split(":");
					Log.e("", "扫描到的好友为：" + name[1]);
					Utils.start_Activity(CaptureActivity.this,
							FriendMsgActivity.class, new BasicNameValuePair(
									Constants.User_ID, name[1]),
							new BasicNameValuePair(Constants.NAME, name[1]));
				} else if (resultString.startsWith("JUNS_WeChat@getMoney")) {
					String[] msg = resultString.split(":");
					String[] money_msg = msg[1].split(",");
					Log.e("", "扫描到的好友ID为：" + money_msg[1]);
					Utils.start_Activity(
							CaptureActivity.this,
							SetMoneyActivity.class,
							new BasicNameValuePair(Constants.User_ID,
									money_msg[0]),
							new BasicNameValuePair(Constants.NAME, money_msg[1]));
				} else if (resultString.startsWith("http://")
						|| resultString.startsWith("https://")) {
					Uri uri = Uri.parse(resultString);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} else {
					Utils.showLongToast(this, "扫描结果为：" + result);
				}
			}
		}
		finish();
	}
}
