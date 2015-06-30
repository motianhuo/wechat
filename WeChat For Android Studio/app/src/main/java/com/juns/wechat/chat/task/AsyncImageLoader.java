package com.juns.wechat.chat.task;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.provider.MediaStore.Video.Thumbnails;

public class AsyncImageLoader {

	private Object lock = new Object();
	private boolean mAllowLoad = true;
	private boolean firstLoad = true;
	private int mStartLoadLimit = 0;
	private int mStopLoadLimit = 0;
	final Handler handler = new Handler();

	public interface OnImageLoadListener {
		public void onImageLoad(Integer t, Bitmap bitmap);

		public void onError(Integer t);

	}

	public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
		if (startLoadLimit > stopLoadLimit) {
			return;
		}
		mStartLoadLimit = startLoadLimit;
		mStartLoadLimit = stopLoadLimit;

	}

	public void restore() {
		mAllowLoad = true;
		firstLoad = true;
	}

	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	public void unlock() {
		mAllowLoad = true;
		synchronized (lock) {
			lock.notifyAll();

		}

	}

	public void loadImage(Integer t, String imageUrl,
			OnImageLoadListener listener) {
		final OnImageLoadListener mListener = listener;
		final String mImageUrl = imageUrl;
		final Integer mt = t;
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!mAllowLoad) {
					synchronized (lock) {

						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}

					
				}
				
				if (mAllowLoad && firstLoad) {
					loadImage(mImageUrl, mt, mListener);
				}
				if (mAllowLoad && mt <= mStopLoadLimit
						&& mt >= mStartLoadLimit) {
					loadImage(mImageUrl, mt, mListener);
				}
				
				
			}
		}).start();
	}

	private void loadImage(final String mImageUrl, final Integer mt,
			final OnImageLoadListener mListener) {
		if (imageCache.containsKey(mImageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(mImageUrl);
			final Bitmap b = softReference.get();
			if (b != null) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (mAllowLoad) {
							mListener.onImageLoad(mt, b);
						}

					}
				});
				return;
			}
		}
		try {
			final Bitmap b = loadImageFromFilePath(mImageUrl);
			if (b != null) {
				imageCache.put(mImageUrl, new SoftReference<Bitmap>(b));
			}
			handler.post(new Runnable() {

				@Override
				public void run() {
					if (mAllowLoad) {
						mListener.onImageLoad(mt, b);
					}
				}
			});

		} catch (Exception e) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					mListener.onError(mt);
				}
			});
			e.printStackTrace();
		}

	}

	private HashMap<String, SoftReference<Bitmap>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public static Bitmap loadImageFromFilePath(String filePath) {
		return ThumbnailUtils.createVideoThumbnail(filePath,
				Thumbnails.MICRO_KIND);
	}

}
