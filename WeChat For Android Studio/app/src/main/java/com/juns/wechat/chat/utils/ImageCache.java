/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.juns.wechat.chat.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageCache {
	
	private ImageCache() {
		// use 1/8 of available heap size
		cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8)) {
              @Override
              protected int sizeOf(String key, Bitmap value) {
                  return value.getRowBytes() * value.getHeight();
              }
          };
	}

	private static ImageCache imageCache = null;

	public static synchronized ImageCache getInstance() {
		if (imageCache == null) {
			imageCache = new ImageCache();
		}
		return imageCache;

	}
	private LruCache<String, Bitmap> cache = null;
	
	/**
	 * put bitmap to image cache
	 * @param key
	 * @param value
	 * @return  the puts bitmap
	 */
	public Bitmap put(String key, Bitmap value){
		return cache.put(key, value);
	}
	
	/**
	 * return the bitmap
	 * @param key
	 * @return
	 */
	public Bitmap get(String key){
		return cache.get(key);
	}
}
