package net.cc.qbaseframework.corenet.okhttp;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import okhttp3.Response;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SyncHttpRequest
 * @Description 同步请求
 * @author chen chao
 * @date 2016-4-26
 */

public class SyncHttpRequest extends HttpRequest {

	public static final String TAG = SyncHttpRequest.class.getSimpleName();

	public static Object request(String requestType, String url, Map<String, String> headMap, Object requestParamObj) {
		Object response = null;
		if (GET_METHOD.equals(requestType)) {
			response = get(url, headMap, requestParamObj);
		} else if (POST_METHOD.equals(requestType)) {
			response = post(url, headMap, requestParamObj);
		}
		return response;
	}

	private static Object get(String url, Map<String, String> headMap, Object requestParamObj) {
		Object obj = null;
		List<BasicNameValuePair> params = null;

		Map<String, String> requestParamMap = (Map<String, String>) requestParamObj;

		if(requestParamMap!=null && !requestParamMap.isEmpty()){
			params = new ArrayList<>();
			for(Map.Entry<String, String> entry : requestParamMap.entrySet()){
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		try
		{
			Response response = OkHttpClientManager.getInstance().syncGet(url, headMap, params);
			if(response.isSuccessful()){
				String responseStr = response.body().string();
				Log.v(TAG+":get:responseresult", responseStr);
				obj = responseStr;
			}else{
				Log.e(TAG+":get:error", response.code() + ":" +response.message());
				obj = response.code();
			}
		}
		catch (IOException e)
		{
			if(e.getClass().getName().equals(SocketTimeoutException.class.getName())){
				obj = RequestCommonError.SOCKET_TIMEOUT.getErrorCode();
			}
			e.printStackTrace();
		}
		return obj;
	}

	private static Object post(String url, Map<String, String> headMap, Object requestParamObj) {
		Object obj = null;
		Log.v(TAG+":post:url", url);
		try {
			Response response = null;
			if(requestParamObj instanceof HashMap){
				HashMap<String, String> requestParams = (HashMap) requestParamObj;
				List<BasicNameValuePair> params = new ArrayList<>();
				if(requestParams!=null && !requestParams.isEmpty()) {
					for(Map.Entry<String, String> entry : requestParams.entrySet()){
						Log.v(TAG+":post:requestparams", entry.getKey()+"="+entry.getValue());
						params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
				}
				response = OkHttpClientManager.getInstance().syncPost(url, headMap, params);
			} else {
				String params = JSON.toJSONString(requestParamObj);
				Log.v(TAG + ":post:requestparams", params);
				response = OkHttpClientManager.getInstance().syncPost(url, headMap, params);
			}
			if (response.isSuccessful()) {
				String responseStr = response.body().string();
				Log.v(TAG + ":post:responseresult", responseStr);
				obj = responseStr;
			} else {
				int responseCode = response.code();
				Log.e(TAG + ":post:error", responseCode + ":" + response.message());
				obj = responseCode;
			}
		} catch (IOException e) {
			if (e.getClass().getName().equals(SocketTimeoutException.class.getName())) {
				obj = RequestCommonError.SOCKET_TIMEOUT.getErrorCode();
			}
			e.printStackTrace();
		}
		return obj;
	}
}
