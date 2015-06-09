package com.juns.wechat.widght.FlippingImageView;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class RotateAnimation extends Animation {

	private Camera mCamera;
	private float mCenterX;
	private float mCenterY;
	private Mode mMode;

	public RotateAnimation(float centerX, float centerY, Mode mode) {
		mCenterX = centerX;
		mCenterY = centerY;
		mMode = mode;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float deg = 0.0F + 360.0F * interpolatedTime;
		Matrix matrix = t.getMatrix();
		mCamera.save();
		if (mMode == Mode.X)
			mCamera.rotateX(deg);
		if (mMode == Mode.Y)
			mCamera.rotateY(deg);
		if (mMode == Mode.Z)
			mCamera.rotateZ(deg);

		mCamera.getMatrix(matrix);
		mCamera.restore();
		matrix.preTranslate(-mCenterX, -mCenterY);
		matrix.postTranslate(mCenterX, mCenterY);

	}

	public enum Mode {
		X, Y, Z;
	}
}
