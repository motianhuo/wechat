/************************************************************
 *  * EaseMob CONFIDENTIAL 
 * __________________ 
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved. 
 *  
 * NOTICE: All information contained herein is, and remains 
 * the property of EaseMob Technologies.
 * Dissemination of this information or reproduction of this material 
 * is strictly forbidden unless prior written permission is obtained
 * from EaseMob Technologies.
 */
package com.allenjuns.wechat.chatuidemo.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.allenjuns.wechat.R;
import com.allenjuns.wechat.chatuidemo.video.util.Utils;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RecorderVideoActivity extends BaseActivity implements
		OnClickListener, SurfaceHolder.Callback, OnErrorListener,
		OnInfoListener {
	private static final String TAG = "RecorderVideoActivity";
	private final static String CLASS_LABEL = "RecordActivity";
	private PowerManager.WakeLock mWakeLock;
	private ImageView btnStart;
	private ImageView btnStop;
	private MediaRecorder mediaRecorder;
	private VideoView mVideoView;// to display video
	String localPath = "";// path to save recorded video
	private Camera mCamera;
	private int previewWidth = 480;
	private int previewHeight = 480;
	private Chronometer chronometer;
	private int frontCamera = 0; // 0 is back camera，1 is front camera
	private Button btn_switch;
	Parameters cameraParameters = null;
	private SurfaceHolder mSurfaceHolder;
	int defaultVideoFrameRate = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// no title
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// full screen
		// translucency mode，used in surface view
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.em_recorder_activity);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
				CLASS_LABEL);
		mWakeLock.acquire();
		initViews();
	}

	private void initViews() {
		btn_switch = (Button) findViewById(R.id.switch_btn);
		btn_switch.setOnClickListener(this);
		btn_switch.setVisibility(View.VISIBLE);
		mVideoView = (VideoView) findViewById(R.id.mVideoView);
		btnStart = (ImageView) findViewById(R.id.recorder_start);
		btnStop = (ImageView) findViewById(R.id.recorder_stop);
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		mSurfaceHolder = mVideoView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
	}

	public void back(View view) {
		releaseRecorder();
		releaseCamera();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mWakeLock == null) {
			// keep screen on
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
					CLASS_LABEL);
			mWakeLock.acquire();
		}
	}

	@SuppressLint("NewApi")
	private boolean initCamera() {
		try {
			if (frontCamera == 0) {
				mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
			} else {
				mCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
			}
			Parameters camParams = mCamera.getParameters();
			mCamera.lock();
			mSurfaceHolder = mVideoView.getHolder();
			mSurfaceHolder.addCallback(this);
			mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			mCamera.setDisplayOrientation(90);

		} catch (RuntimeException ex) {
			EMLog.e("video", "init Camera fail " + ex.getMessage());
			return false;
		}
		return true;
	}

	private void handleSurfaceChanged() {
		if (mCamera == null) {
			finish();
			return;
		}
		boolean hasSupportRate = false;
		List<Integer> supportedPreviewFrameRates = mCamera.getParameters()
				.getSupportedPreviewFrameRates();
		if (supportedPreviewFrameRates != null
				&& supportedPreviewFrameRates.size() > 0) {
			Collections.sort(supportedPreviewFrameRates);
			for (int i = 0; i < supportedPreviewFrameRates.size(); i++) {
				int supportRate = supportedPreviewFrameRates.get(i);

				if (supportRate == 15) {
					hasSupportRate = true;
				}

			}
			if (hasSupportRate) {
				defaultVideoFrameRate = 15;
			} else {
				defaultVideoFrameRate = supportedPreviewFrameRates.get(0);
			}

		}

		// get all resolutions which camera provide
		List<Size> resolutionList = Utils.getResolutionList(mCamera);
		if (resolutionList != null && resolutionList.size() > 0) {
			Collections.sort(resolutionList, new Utils.ResolutionComparator());
			Size previewSize = null;
			boolean hasSize = false;

			// use 60*480 if camera support
			for (int i = 0; i < resolutionList.size(); i++) {
				Size size = resolutionList.get(i);
				if (size != null && size.width == 640 && size.height == 480) {
					previewSize = size;
					previewWidth = previewSize.width;
					previewHeight = previewSize.height;
					hasSize = true;
					break;
				}
			}
			// use medium resolution if camera don't support the above resolution
			if (!hasSize) {
				int mediumResolution = resolutionList.size() / 2;
				if (mediumResolution >= resolutionList.size())
					mediumResolution = resolutionList.size() - 1;
				previewSize = resolutionList.get(mediumResolution);
				previewWidth = previewSize.width;
				previewHeight = previewSize.height;

			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.switch_btn:
			switchCamera();
			break;
		case R.id.recorder_start:
			// start recording
		    if(!startRecording())
		        return;
			Toast.makeText(this, R.string.The_video_to_start, Toast.LENGTH_SHORT).show();
			btn_switch.setVisibility(View.INVISIBLE);
			btnStart.setVisibility(View.INVISIBLE);
			btnStart.setEnabled(false);
			btnStop.setVisibility(View.VISIBLE);
			chronometer.setBase(SystemClock.elapsedRealtime());
			chronometer.start();
			break;
		case R.id.recorder_stop:
		    btnStop.setEnabled(false);
			stopRecording();
			btn_switch.setVisibility(View.VISIBLE);
			chronometer.stop();
			btnStart.setVisibility(View.VISIBLE);
			btnStop.setVisibility(View.INVISIBLE);
			new AlertDialog.Builder(this)
					.setMessage(R.string.Whether_to_send)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									sendVideo(null);

								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								    if(localPath != null){
								        File file = new File(localPath);
								        if(file.exists())
								            file.delete();
								    }
								    finish();
									
								}
							}).setCancelable(false).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mSurfaceHolder = holder;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera == null){
			if(!initCamera()){
			    showFailDialog();
			    return;
			}
			
		}
		try {
			mCamera.setPreviewDisplay(mSurfaceHolder);
			mCamera.startPreview();
			handleSurfaceChanged();
		} catch (Exception e1) {
			EMLog.e("video", "start preview fail " + e1.getMessage());
			showFailDialog();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		EMLog.v("video", "surfaceDestroyed");  
	}

	public boolean startRecording(){
		if (mediaRecorder == null){
			if(!initRecorder())
			    return false;
		}
		mediaRecorder.setOnInfoListener(this);
		mediaRecorder.setOnErrorListener(this);
		mediaRecorder.start();
		return true;
	}

	@SuppressLint("NewApi")
	private boolean initRecorder(){
	    if(!EaseCommonUtils.isSdcardExist()){
	        showNoSDCardDialog();
	        return false;
	    }
	    
		if (mCamera == null) {
			if(!initCamera()){
			    showFailDialog();
			    return false;
			}
		}
		mVideoView.setVisibility(View.VISIBLE);
		mCamera.stopPreview();
		mediaRecorder = new MediaRecorder();
		mCamera.unlock();
		mediaRecorder.setCamera(mCamera);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		if (frontCamera == 1) {
			mediaRecorder.setOrientationHint(270);
		} else {
			mediaRecorder.setOrientationHint(90);
		}

		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		// set resolution, should be set after the format and encoder was set
		mediaRecorder.setVideoSize(previewWidth, previewHeight);
		mediaRecorder.setVideoEncodingBitRate(384 * 1024);
		// set frame rate, should be set after the format and encoder was set
		if (defaultVideoFrameRate != -1) {
			mediaRecorder.setVideoFrameRate(defaultVideoFrameRate);
		}
		// set the path for video file
		localPath = PathUtil.getInstance().getVideoPath() + "/"
				+ System.currentTimeMillis() + ".mp4";
		mediaRecorder.setOutputFile(localPath);
		mediaRecorder.setMaxDuration(30000);
		mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
		try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
		return true;

	}

	public void stopRecording() {
		if (mediaRecorder != null) {
			mediaRecorder.setOnErrorListener(null);
			mediaRecorder.setOnInfoListener(null);
			try {
				mediaRecorder.stop();
			} catch (IllegalStateException e) {
				EMLog.e("video", "stopRecording error:" + e.getMessage());
			}
		}
		releaseRecorder();

		if (mCamera != null) {
			mCamera.stopPreview();
			releaseCamera();
		}
	}

	private void releaseRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	protected void releaseCamera() {
		try {
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
		} catch (Exception e) {
		}
	}

	@SuppressLint("NewApi")
	public void switchCamera() {

		if (mCamera == null) {
			return;
		}
		if (Camera.getNumberOfCameras() >= 2) {
			btn_switch.setEnabled(false);
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}

			switch (frontCamera) {
			case 0:
				mCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
				frontCamera = 1;
				break;
			case 1:
				mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
				frontCamera = 0;
				break;
			}
			try {
				mCamera.lock();
				mCamera.setDisplayOrientation(90);
				mCamera.setPreviewDisplay(mVideoView.getHolder());
				mCamera.startPreview();
			} catch (IOException e) {
				mCamera.release();
				mCamera = null;
			}
			btn_switch.setEnabled(true);

		}

	}

	MediaScannerConnection msc = null;
	ProgressDialog progressDialog = null;

	public void sendVideo(View view) {
		if (TextUtils.isEmpty(localPath)) {
			EMLog.e("Recorder", "recorder fail please try again!");
			return;
		}
		if(msc == null)
    		msc = new MediaScannerConnection(this,
    				new MediaScannerConnectionClient() {
    
    					@Override
    					public void onScanCompleted(String path, Uri uri) {
    						EMLog.d(TAG, "scanner completed");
    						msc.disconnect();
    						progressDialog.dismiss();
    						setResult(RESULT_OK, getIntent().putExtra("uri", uri));
    						finish();
    					}
    
    					@Override
    					public void onMediaScannerConnected() {
    						msc.scanFile(localPath, "video/*");
    					}
    				});
		
		
		if(progressDialog == null){
		    progressDialog = new ProgressDialog(this);
		    progressDialog.setMessage("processing...");
		    progressDialog.setCancelable(false);
		}
		progressDialog.show();
		msc.connect();

	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		EMLog.v("video", "onInfo");
		if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
			EMLog.v("video", "max duration reached");
			stopRecording();
			btn_switch.setVisibility(View.VISIBLE);
			chronometer.stop();
			btnStart.setVisibility(View.VISIBLE);
			btnStop.setVisibility(View.INVISIBLE);
			chronometer.stop();
			if (localPath == null) {
				return;
			}
			String st3 = getResources().getString(R.string.Whether_to_send);
			new AlertDialog.Builder(this)
					.setMessage(st3)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									sendVideo(null);

								}
							}).setNegativeButton(R.string.cancel, null)
					.setCancelable(false).show();
		}

	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		EMLog.e("video", "recording onError:");
		stopRecording();
		Toast.makeText(this,
				"Recording error has occurred. Stopping the recording",
				Toast.LENGTH_SHORT).show();

	}

	public void saveBitmapFile(Bitmap bitmap) {
		File file = new File(Environment.getExternalStorageDirectory(), "a.jpg");
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseCamera();

		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}

	}

	@Override
	public void onBackPressed() {
		back(null);
	}

	private void showFailDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.prompt)
				.setMessage(R.string.Open_the_equipment_failure)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();

							}
						}).setCancelable(false).show();

	}
	
	private void showNoSDCardDialog() {
	    new AlertDialog.Builder(this)
        .setTitle(R.string.prompt)
        .setMessage("No sd card!")
        .setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        finish();

                    }
                }).setCancelable(false).show();
	}

}
