package com.allenjuns.wechat.net;//package com.juns.wxshop.net;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.aishidi.logistics.App;
//import com.aishidi.logistics.Constants;
//import com.aishidi.logistics.common.Utils;
//import com.juns.health.net.loopj.android.http.JsonHttpResponseHandler;
//
//public abstract class BaseJsonRes extends JsonHttpResponseHandler {
//
//	@Override
//	public void onSuccess(JSONObject response) {
//		try {
//			String status = response.getString(Constants.Status);
//			System.out.println("status:" + response);
//			if (status == null) {
//				Utils.showLongToast(App.getInstance(), Constants.NET_ERROR);
//			} else if (status.equals("200")) {
//				String str_data = response.getString(Constants.Value);
//				onMySuccess(str_data);
//			} else {
//				String str = response.getString(Constants.Desc);
//				Utils.showLongToast(App.getInstance(), str);
//				onMyFailure(status);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//			onMyFailure("201");
//		}
//	}
//
//	@Override
//	public void onFailure(Throwable e, JSONArray errorResponse) {
//		super.onFailure(e, errorResponse);
//		onMyFailure("201");
//	}
//
//	public abstract void onMySuccess(String data);
//
//	public abstract void onMyFailure(String status);
//}
