package com.juns.wechat.chat.utils;

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

import com.easemob.chat.EMVideoCallHelper;
import com.juns.wechat.App;

public class CameraHelper implements PreviewCallback {
	private static final String TAG = "CameraHelper";

	static final int mwidth = 640;
	static final int mheight = 480;

	private Camera mCamera;
	private int camera_count;

	private Parameters mParameters;

	private byte[] yuv_frame;

	private byte[] yuv_Rotate90;

	private byte[] yuv_Rotate90lr;

	private SurfaceHolder localSurfaceHolder;

	private boolean start_flag;

	private EMVideoCallHelper callHelper;

	public CameraHelper(EMVideoCallHelper callHelper,
			SurfaceHolder localSurfaceHolder) {
		this.callHelper = callHelper;
		this.localSurfaceHolder = localSurfaceHolder;
	}

	/**
	 * 开启相机拍摄
	 */
	public void startCapture() {
		if (mCamera == null) {
			// mCamera = Camera.open();
			camera_count = Camera.getNumberOfCameras();
			Log.e(TAG, "camera count:" + camera_count);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				for (int i = 0; i < camera_count; i++) {
					CameraInfo info = new CameraInfo();
					Camera.getCameraInfo(i, info);
					// find front camera
					if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
						Log.e(TAG, "to open front camera");
						mCamera = Camera.open(i);
					}
				}
			}
			if (mCamera == null) {
				Log.e(TAG, "AAAAA OPEN camera");
				mCamera = Camera.open();
			}

		}

		try {
			mCamera.stopPreview();
			mParameters = mCamera.getParameters();
			if (isScreenOriatationPortrait()) {
				mCamera.setDisplayOrientation(90);
			}
			mParameters.setPreviewSize(mwidth, mheight);
			mParameters.setPreviewFrameRate(25);
			mCamera.setParameters(mParameters);
			int mformat = mParameters.getPreviewFormat();
			int bitsperpixel = ImageFormat.getBitsPerPixel(mformat);
			Log.e(TAG, "pzy bitsperpixel: " + bitsperpixel);
			yuv_frame = new byte[mwidth * mheight * bitsperpixel / 8];
			yuv_Rotate90 = new byte[mwidth * mheight * bitsperpixel / 8];
			yuv_Rotate90lr = new byte[mwidth * mheight * bitsperpixel / 8];
			mCamera.addCallbackBuffer(yuv_frame);
			// mCamera.setPreviewDisplay(holder);
			mCamera.setPreviewDisplay(localSurfaceHolder);
			mCamera.setPreviewCallbackWithBuffer(this);

			EMVideoCallHelper.getInstance().setResolution(mwidth, mheight);

			mCamera.startPreview();
			Log.d(TAG, "camera start preview");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if (start_flag == true) {
			// 根据屏幕方向写入及传输数据
			if (isScreenOriatationPortrait()) {
				YUV420spRotate90(yuv_Rotate90, yuv_frame, mwidth, mheight);
				YUV42left2right(yuv_Rotate90lr, yuv_Rotate90, mheight, mwidth);
				callHelper.processPreviewData(mheight, mwidth, yuv_Rotate90lr);
			} else {
				YUV42left2right(yuv_Rotate90, yuv_frame, mwidth, mheight);
				callHelper.processPreviewData(mheight, mwidth, yuv_Rotate90);
			}
		}
		camera.addCallbackBuffer(yuv_frame);
	}

	/**
	 * 停止拍摄
	 */
	public void stopCapture() {
		start_flag = false;
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * 获取是否已开启视频数据传输
	 * 
	 * @return
	 */
	public boolean isStarted() {
		return start_flag;
	}

	/**
	 * 设置是否传输视频数据
	 * 
	 * @param start
	 */
	public void setStartFlag(boolean start) {
		this.start_flag = start;
	}

	void YUV420spRotate90(byte[] dst, byte[] src, int srcWidth, int srcHeight) {
		int nWidth = 0, nHeight = 0;
		int wh = 0;
		int uvHeight = 0;
		if (srcWidth != nWidth || srcHeight != nHeight) {
			nWidth = srcWidth;
			nHeight = srcHeight;
			wh = srcWidth * srcHeight;
			uvHeight = srcHeight >> 1;// uvHeight = height / 2
		}

		int k = 0;
		for (int i = 0; i < srcWidth; i++) {
			int nPos = srcWidth - 1;
			for (int j = 0; j < srcHeight; j++) {
				dst[k] = src[nPos - i];
				k++;
				nPos += srcWidth;
			}
		}

		for (int i = 0; i < srcWidth; i += 2) {
			int nPos = wh + srcWidth - 1;
			for (int j = 0; j < uvHeight; j++) {
				dst[k] = src[nPos - i - 1];
				dst[k + 1] = src[nPos - i];
				k += 2;
				nPos += srcWidth;
			}
		}
		return;
	}

	void YUV42left2right(byte[] dst, byte[] src, int srcWidth, int srcHeight) {
		// int nWidth = 0, nHeight = 0;
		int wh = 0;
		int uvHeight = 0;
		// if(srcWidth != nWidth || srcHeight != nHeight)
		{
			// nWidth = srcWidth;
			// nHeight = srcHeight;
			wh = srcWidth * srcHeight;
			uvHeight = srcHeight >> 1;// uvHeight = height / 2
		}

		// 转换Y
		int k = 0;
		int nPos = 0;
		for (int i = 0; i < srcHeight; i++) {
			nPos += srcWidth;
			for (int j = 0; j < srcWidth; j++) {
				dst[k] = src[nPos - j - 1];
				k++;
			}

		}
		nPos = wh + srcWidth - 1;
		for (int i = 0; i < uvHeight; i++) {
			for (int j = 0; j < srcWidth; j += 2) {
				dst[k] = src[nPos - j - 1];
				dst[k + 1] = src[nPos - j];
				k += 2;

			}
			nPos += srcWidth;
		}
		return;
	}

	boolean isScreenOriatationPortrait() {
		return App.getInstance().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}
}
