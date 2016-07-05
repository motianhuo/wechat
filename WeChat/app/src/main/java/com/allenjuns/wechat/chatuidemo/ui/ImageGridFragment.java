package com.allenjuns.wechat.chatuidemo.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allenjuns.wechat.BuildConfig;
import com.allenjuns.wechat.R;
import com.allenjuns.wechat.chatuidemo.domain.VideoEntity;
import com.allenjuns.wechat.chatuidemo.video.util.ImageCache;
import com.allenjuns.wechat.chatuidemo.video.util.ImageResizer;
import com.allenjuns.wechat.chatuidemo.video.util.Utils;
import com.allenjuns.wechat.chatuidemo.widget.RecyclingImageView;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.TextFormater;

import java.util.ArrayList;
import java.util.List;

public class ImageGridFragment extends Fragment implements OnItemClickListener {

	private static final String TAG = "ImageGridFragment";
	private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageAdapter mAdapter;
	private ImageResizer mImageResizer;
	List<VideoEntity> mList;

	/**
	 * Empty constructor as per the Fragment documentation
	 */
	public ImageGridFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageThumbSize = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_spacing);
		mList=new ArrayList<VideoEntity>();
		getVideoFile();
		mAdapter = new ImageAdapter(getActivity());
		
		ImageCache.ImageCacheParams cacheParams=new ImageCache.ImageCacheParams();

		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory

		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		mImageResizer = new ImageResizer(getActivity(), mImageThumbSize);
		mImageResizer.setLoadingImage(R.drawable.em_empty_photo);
		mImageResizer.addImageCache(getActivity().getSupportFragmentManager(),
				cacheParams);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.em_image_grid_fragment,
				container, false);
		final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView,
					int scrollState) {
				// Pause fetcher to ensure smoother scrolling when flinging
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					// Before Honeycomb pause image loading on scroll to help
					// with performance
					if (!Utils.hasHoneycomb()) {
						mImageResizer.setPauseWork(true);
					}
				} else {
					mImageResizer.setPauseWork(false);
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		// This listener is used to get the final width of the GridView and then
		// calculate the
		// number of columns and the width of each column. The width of each
		// column is variable
		// as the GridView has stretchMode=columnWidth. The column width is used
		// to set the height
		// of each view so we get nice square thumbnails.
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@TargetApi(VERSION_CODES.JELLY_BEAN)
					@Override
					public void onGlobalLayout() {
						final int numColumns = (int) Math.floor(mGridView
								.getWidth()
								/ (mImageThumbSize + mImageThumbSpacing));
						if (numColumns > 0) {
							final int columnWidth = (mGridView.getWidth() / numColumns)
									- mImageThumbSpacing;
							mAdapter.setItemHeight(columnWidth);
							if (BuildConfig.DEBUG) {
								Log.d(TAG,
										"onCreateView - numColumns set to "
												+ numColumns);
							}
							if (Utils.hasJellyBean()) {
								mGridView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							} else {
								mGridView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							}
						}
					}
				});
		return v;

	}

	@Override
	public void onResume() {
		super.onResume();
		mImageResizer.setExitTasksEarly(false);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageResizer.closeCache();
		mImageResizer.clearCache();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
		
		mImageResizer.setPauseWork(true);
		
		if(position==0)
		{
			
			Intent intent=new Intent();
			intent.setClass(getActivity(), RecorderVideoActivity.class);
			startActivityForResult(intent, 100);
		}else{
			VideoEntity vEntty=mList.get(position-1);
			// limit the size to 10M
			if (vEntty.size > 1024 * 1024 * 10) {
				String st = getResources().getString(R.string.temporary_does_not);
				Toast.makeText(getActivity(), st, Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent=getActivity().getIntent().putExtra("path", vEntty.filePath).putExtra("dur", vEntty.duration);
			getActivity().setResult(Activity.RESULT_OK, intent);
			getActivity().finish();
		}
	}

	private class ImageAdapter extends BaseAdapter {

		private final Context mContext;
		private int mItemHeight = 0;
		private RelativeLayout.LayoutParams mImageViewLayoutParams;

		public ImageAdapter(Context context) {
			super();
			mContext = context;
			mImageViewLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}

		@Override
		public int getCount() {
			return mList.size()+1;
		}

		@Override
		public Object getItem(int position) {
			return (position==0)?null:mList.get(position-1);
		}

		@Override
		public long getItemId(int position) {
			return position ;
		}
 
 
		@Override
		public View getView(int position, View convertView, ViewGroup container) {
			 ViewHolder holder=null;
			 if(convertView==null)
			 {
				 holder=new ViewHolder();
				 convertView=LayoutInflater.from(mContext).inflate(R.layout.em_choose_griditem, container,false);
				 holder.imageView=(RecyclingImageView) convertView.findViewById(R.id.imageView);
				 holder.icon=(ImageView) convertView.findViewById(R.id.video_icon);
				 holder.tvDur=(TextView)convertView.findViewById(R.id.chatting_length_iv);
				 holder.tvSize=(TextView)convertView.findViewById(R.id.chatting_size_iv);
				 holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				 holder.imageView.setLayoutParams(mImageViewLayoutParams);
				 convertView.setTag(holder);
			 }else{
				 holder=(ViewHolder) convertView.getTag();
			 }
			 
			// Check the height matches our calculated column width
			if (holder.imageView.getLayoutParams().height != mItemHeight) {
				holder.imageView.setLayoutParams(mImageViewLayoutParams);
			}

			// Finally load the image asynchronously into the ImageView, this
			// also takes care of
			// setting a placeholder image while the background thread runs
			String st1 = getResources().getString(R.string.Video_footage);
			if(position==0)
			{
				holder.icon.setVisibility(View.GONE);
				holder.tvDur.setVisibility(View.GONE);
				holder.tvSize.setText(st1);
				holder.imageView.setImageResource(R.drawable.em_actionbar_camera_icon);
			}else{
				holder.icon.setVisibility(View.VISIBLE);
				VideoEntity entty=mList.get(position-1);
				holder.tvDur.setVisibility(View.VISIBLE);
				
				holder.tvDur.setText(DateUtils.toTime(entty.duration));
				holder.tvSize.setText(TextFormater.getDataSize(entty.size));
				holder.imageView.setImageResource(R.drawable.em_empty_photo);
				mImageResizer.loadImage(entty.filePath, holder.imageView);
			}
			return convertView;
			// END_INCLUDE(load_gridview_item)
		}

		/**
		 * Sets the item height. Useful for when we know the column width so the
		 * height can be set to match.
		 * 
		 * @param height
		 */
		public void setItemHeight(int height) {
			if (height == mItemHeight) {
				return;
			}
			mItemHeight = height;
			mImageViewLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, mItemHeight);
			mImageResizer.setImageSize(height);
			notifyDataSetChanged();
		}

		class ViewHolder{
			
			RecyclingImageView imageView;
			ImageView icon;
			TextView tvDur;
			TextView tvSize;		
		}	 
	}

	private void getVideoFile()
	{
		ContentResolver mContentResolver=getActivity().getContentResolver();
		Cursor cursor=mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,MediaStore.Video.DEFAULT_SORT_ORDER);
		
		if (cursor != null && cursor.moveToFirst()) {
			do {

				// ID:MediaStore.Audio.Media._ID
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media._ID));

				// title：MediaStore.Audio.Media.TITLE
				String title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
				// path：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

				// duration：MediaStore.Audio.Media.DURATION
				int duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

				// 大小：MediaStore.Audio.Media.SIZE
				int size = (int) cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

				VideoEntity entty = new VideoEntity();
				entty.ID = id;
				entty.title = title;
				entty.filePath = url;
				entty.duration = duration;
				entty.size = size;
				mList.add(entty);
			} while (cursor.moveToNext());

		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK)
		{
			if(requestCode==100)
			{
				Uri uri=data.getParcelableExtra("uri");
				String[] projects = new String[] { MediaStore.Video.Media.DATA,
						MediaStore.Video.Media.DURATION };
				Cursor cursor = getActivity().getContentResolver().query(
						uri, projects, null,
						null, null);
				int duration=0;
				String filePath=null;
				
				if (cursor.moveToFirst()) {
					// path：MediaStore.Audio.Media.DATA
					filePath = cursor.getString(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
					// duration：MediaStore.Audio.Media.DURATION
					duration = cursor
							.getInt(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
					EMLog.d(TAG, "duration:"+duration);
				}
				if(cursor!=null)
                {
                	cursor.close();
                	cursor=null;
                }
				 
				getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent().putExtra("path", filePath).putExtra("dur", duration));
				getActivity().finish();
				
			}
		}	
	}
}
