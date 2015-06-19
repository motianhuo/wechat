package com.juns.wechat.net;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.juns.health.net.loopj.android.http.AsyncHttpClient;
import com.juns.health.net.loopj.android.http.JsonHttpResponseHandler;
import com.juns.health.net.loopj.android.http.RequestParams;
import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.common.NetUtil;
import com.juns.wechat.common.Utils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class NetClient {

	private static Context context;
	// http 请求
	private AsyncHttpClient client;
	// 超时时间
	private int TIMEOUT = 20000;

	// 图片加载类
	private static ImageLoader mImageLoader;
	private static DisplayImageOptions binner_options;
	private static DisplayImageOptions icon_options;
	private static DisplayImageOptions user_icon_options;
	private static DisplayImageOptions girl_options;
	// Imageload缓存目录
	private static File cacheDir = new File(App.getHJYCacheDir() + "/Imageload");

	public NetClient(Context context) {
		NetClient.context = context;
		client = new AsyncHttpClient();
		client.setTimeout(TIMEOUT);
	}

	static {
		ActivityManager am = (ActivityManager) App.getInstance()
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memClass / 4; // 硬引用缓存容量，为系统可用内存的1/4

		binner_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.large_logo_default_4) // resource
				// or
				.showImageForEmptyUri(R.drawable.large_logo_default_4) // resource
				.showImageOnFail(R.drawable.large_logo_default_4) // resource or
				.cacheInMemory(false) // default
				.cacheOnDisc(true) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new SimpleBitmapDisplayer()) // default
				.build();
		icon_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.head) // resource
														// or
				.showImageForEmptyUri(R.drawable.head) // resource
				.showImageOnFail(R.drawable.head) // resource or
				.cacheInMemory(true) // default
				.cacheOnDisc(true) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new RoundedBitmapDisplayer(50)) // default
				.build();
		user_icon_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.head) // resource
				.showImageForEmptyUri(R.drawable.head) // resource
				.showImageOnFail(R.drawable.head) // resource
				.cacheInMemory(true) // default
				.cacheOnDisc(true) // default
				.imageScaleType(ImageScaleType.NONE) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new SimpleBitmapDisplayer()) // default
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				App.getInstance()).threadPoolSize(10)
				.threadPriority(Thread.NORM_PRIORITY + 1)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(cacheSize))
				.memoryCacheSize(cacheSize)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheSize(30 * 1024 * 1024).discCacheFileCount(500)
				.writeDebugLogs().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(config);
	}

	/**
	 * 根据url获取小图片 并自动设置到imageview中 获取的图片保存到内存(切勿加载大图)
	 * 
	 * @param img
	 * @param url
	 */
	public static void getIconBitmap(ImageView view, String url) {
		mImageLoader.displayImage(url, view, icon_options);
	}

	/**
	 * 根据url获取大图片 并自动设置到imageview中 获取的图片不保存到内存
	 * 
	 * @param img
	 * @param url
	 */
	public static void getBinnerBitmap(ImageView imageView, String url) {
		mImageLoader.displayImage(url, imageView, binner_options);
	}

	public static void getGirlBitmap(ImageView imageView, String url) {
		mImageLoader.displayImage(url, imageView, girl_options);
	}

	public static void getBinnerBitmap(String url, ImageLoadingListener listener) {
		mImageLoader.loadImage(url, listener);
	}

	/**
	 * 根据url获取大图片 并自动设置到imageview中 获取的图片不保存到内存
	 * 
	 * @param img
	 * @param url
	 */
	public static void getHalfHeightBitmap(ImageView imageView, String uri,
			ImageLoadingListener lister) {
		mImageLoader.displayImage(uri, imageView, binner_options, lister);
	}

	/**
	 * get方式请求调用方法 返回格式均为json对象 返回为json
	 * 
	 * @param url
	 *            请求URL
	 * @param params
	 *            请求参数 可以为空
	 * @param res
	 *            必须实现此类 处理成功失败等 回调
	 */
	public void get(String url, RequestParams params,
			final JsonHttpResponseHandler res) {
		if (!NetUtil.checkNetWork(context)) {
			Utils.showLongToast(context, Constants.NET_ERROR);
			return;
		}
		try {
			if (params != null)
				// 带请求参数 获取json对象
				client.get(url, params, res);
			else
				// 不请求参数 获取json对象
				client.get(url, res);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}

	/**
	 * json post方式请求调用方法 返回为json
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数 可以为空
	 * @param res
	 *            必须实现此类 处理成功失败等 回调
	 */
	public void post(String url, RequestParams params,
			final JsonHttpResponseHandler res) {
		System.out.println("请求URL：" + url);
		if (!NetUtil.checkNetWork(context)) {
			Utils.showLongToast(context, Constants.NET_ERROR);
			return;
		}
		try {
			if (params != null) {
				client.post(url, params, res);
			} else {
				client.post(url, res);
			}
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
}
