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
package com.juns.wechat.chat.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.easemob.chat.EMMessage;
import com.easemob.util.EMLog;

public class DownloadImageTask extends AsyncTask<EMMessage, Integer, Bitmap>{
	private DownloadFileCallback callback;
	Bitmap bitmap = null;
	public boolean downloadThumbnail = false;
	EMMessage message;
	private String remoteDir;

	public DownloadImageTask(String remoteDir, DownloadFileCallback callback){
		this.callback = callback;
		this.remoteDir = remoteDir;
	}
	
	@Override
	protected Bitmap doInBackground(EMMessage... params) {
	    /*
	    try {
	        message = params[1];//视频的图片path信息的message
        } catch (Exception e) {
            message = params[0];
        }
	  
	    
		String remoteFilePath = message.getFilePath().substring(message.getFilePath().lastIndexOf("/")+1);
		if(remoteDir != null){
			remoteFilePath = remoteDir + remoteFilePath;
		}
		final String localFilePath;
		if (downloadThumbnail) {
		    localFilePath = getThumbnailImagePath(message.getFilePath());
		    SMTLog.d("###", "localFilePath: "+localFilePath);
		} else {
		    localFilePath = message.getFilePath();
		}
//		final String remoteFilePath = message.getFilePath();
//		final String localFilePath = User.getImagePath() + "/"+ message.getImageName();
		SMTLog.d("###", "download picture from remote "+ remoteFilePath + " to local:" + localFilePath);
		final HttpFileManager httpFileMgr = new HttpFileManager(EaseMobUserConfig.getInstance().applicationContext,
		        EaseMobChatConfig.getInstance().EASEMOB_STORAGE_URL);
		CloudOperationCallback callback = new CloudOperationCallback() {
			public void onSuccess() {
				SMTLog.d("###", "offline file saved to "+ localFilePath);
				// after download to phone, we will delete the
				// file on server
				try {
					//httpFileMgr.deleteFileInBackground(remoteFilePath, null, null);
				    bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(localFilePath)));
					//bitmap = Bitmap.createScaledBitmap(bm, 120, 120, true);
					//bitmap = Bitmap.createBitmap(bm);
					//bm.recycle();
					//bm = null;
					
				} catch (Exception e) {
					e.printStackTrace();
					bitmap = null;
				}
			}

			public void onError(String msg) {
				SMTLog.e("###","offline file transfer error:" + msg);
				File file = new File(localFilePath);
				if(file.exists())
					file.delete();
			}

			public void onProgress(int progress) {
				onProgressUpdate(progress);
			}
		};
		if (downloadThumbnail) {
		    httpFileMgr.downloadThumbnailFile(remoteFilePath, localFilePath, EaseMobUserConfig.getInstance().APPKEY, null, callback);
		} else {
		    httpFileMgr.downloadFile(remoteFilePath, localFilePath, EaseMobUserConfig.getInstance().APPKEY, null, callback);
		}
		return bitmap;
		*/
	    //todo: need to implement
	    return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		callback.afterDownload(result);
	}
	
	@Override
	protected void onPreExecute() {
		callback.beforeDownload();
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		callback.downloadProgress(values[0]);
	}
	
	
	
	public interface DownloadFileCallback{
		void beforeDownload();
		void downloadProgress(int progress);
		void afterDownload(Bitmap bitmap);
	}
	
	
	public static String getThumbnailImagePath(String imagePath) {
        String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
        path += "th" + imagePath.substring(imagePath.lastIndexOf("/")+1, imagePath.length());
        EMLog.d("msg", "original image path:" + imagePath);
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }
}
