package net.cc.qbaseframework.corenet.okhttp;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AsyncHttpRequest
 * @Description 异步请求
 * @author chen chao
 * @date 2016-4-26
 */

public class AsyncHttpRequest extends HttpRequest
{
	public static final String TAG = AsyncHttpRequest.class.getSimpleName();

	public static void request(String requestType, String url, Map<String, String> headMap, Object requestParamObj, ResultCallBack resultCb){
		if(GET_METHOD.equals(requestType)){
			get(url, headMap, requestParamObj, resultCb);
		}
		else if(POST_METHOD.equals(requestType)){
			post(url, headMap, requestParamObj, resultCb);
		}
	}

	private static void get(String url, Map<String, String> headMap, Object requestParamObj, final ResultCallBack resultCb)
	{
		List<BasicNameValuePair> params = null;
		Log.v(TAG+":get:url", url);
		Map<String, String> requestParamMap = (Map<String, String>) requestParamObj;
		if(requestParamMap!=null && !requestParamMap.isEmpty()){
			params = new ArrayList<>();
			for(Map.Entry<String, String> entry : requestParamMap.entrySet()){
				Log.v(TAG+":get:requestparams", entry.getKey()+"="+entry.getValue());
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		OkHttpClientManager.getInstance().asyncGet(url, headMap, params, new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				String responseStr = response.body().string();
				Log.v(TAG+":get:responseresult", responseStr);
				resultCb.onSuccess(responseStr);
			}

			@Override
			public void onFailure(Call call, IOException e)
			{
				resultCb.onError(call.request(), e);
				e.printStackTrace();
			}
		});
	}

	private static void post(String url, Map<String, String> headMap, Object requestParamObj, final ResultCallBack resultCb)
	{
		Log.v(TAG+":post:url", url);
		String jsonParams = JSON.toJSONString(requestParamObj);
		Log.v(TAG+":post:requestparams", jsonParams);
		OkHttpClientManager.getInstance().asyncPost(url, headMap, jsonParams, new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				String responseStr = response.body().string();
				Log.v(TAG+":get:responseresult", responseStr);
				resultCb.onSuccess(responseStr);
			}

			@Override
			public void onFailure(Call call, IOException e)
			{
				resultCb.onError(call.request(), e);
				e.printStackTrace();
			}
		});
	}

	public interface ResultCallBack{
		void onSuccess(Object object);
		void onError(Request request, Exception e);
	}
}
