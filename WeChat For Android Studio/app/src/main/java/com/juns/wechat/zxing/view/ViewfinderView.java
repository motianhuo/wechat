package com.juns.wechat.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.juns.wechat.R;
import com.juns.wechat.zxing.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final long ANIMATION_DELAY = 100L;
	private static final int OPAQUE = 0xFF;

	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {
			int linewidht = 10;
			paint.setColor(frameColor);

			canvas.drawRect(15 + frame.left, 15 + frame.top,
					15 + (linewidht + frame.left), 15 + (50 + frame.top), paint);
			canvas.drawRect(15 + frame.left, 15 + frame.top,
					15 + (50 + frame.left), 15 + (linewidht + frame.top), paint);
			canvas.drawRect(-15 + ((0 - linewidht) + frame.right),
					15 + frame.top, -15 + (1 + frame.right),
					15 + (50 + frame.top), paint);
			canvas.drawRect(-15 + (-50 + frame.right), 15 + frame.top, -15
					+ frame.right, 15 + (linewidht + frame.top), paint);
			canvas.drawRect(15 + frame.left, -15 + (-49 + frame.bottom),
					15 + (linewidht + frame.left), -15 + (1 + frame.bottom),
					paint);
			canvas.drawRect(15 + frame.left, -15
					+ ((0 - linewidht) + frame.bottom), 15 + (50 + frame.left),
					-15 + (1 + frame.bottom), paint);
			canvas.drawRect(-15 + ((0 - linewidht) + frame.right), -15
					+ (-49 + frame.bottom), -15 + (1 + frame.right), -15
					+ (1 + frame.bottom), paint);
			canvas.drawRect(-15 + (-50 + frame.right), -15
					+ ((0 - linewidht) + frame.bottom), -15 + frame.right, -15
					+ (linewidht - (linewidht - 1) + frame.bottom), paint);

			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			// juns 2015年5月13日16:36:29 去掉中间+
			int vmiddle = frame.height() / 2 + frame.top;
			int hmiddle = frame.width() / 2 + frame.left;
			canvas.drawRect(frame.left + 2, vmiddle - 1, frame.right - 1,
					vmiddle + 2, paint);

			canvas.drawRect(hmiddle - 1, frame.top + 2, hmiddle + 2,
					frame.bottom - 1, paint);
			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}

			// Request another update at the animation interval, but only
			// repaint the laser line,
			// not the entire viewfinder mask.
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
