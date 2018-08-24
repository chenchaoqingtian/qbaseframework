package net.cc.qbaseframework.corenet.okhttp;

import android.util.Log;
import okhttp3.Response;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p/>Description：OKHttp工具类
 * <p/>author： chen chao
 * <p/>date： 2017/11/15 14:23
 * <p/>update： (date)
 * <p/>version： V1.0
 */
public class OkHttpUtil {

    public static Object syncRequest(String requestType, String url, Map<String, String> headMap, Object requestParamObj, FormFile[] files){
        if(files!=null&&files.length>0){
            Map<String, String> requestParamMap = (Map<String, String>) requestParamObj;
            return fileUpload(url, headMap, requestParamMap, files);
        }
        return SyncHttpRequest.request(requestType, url, headMap, requestParamObj);
    }

    public static void asyncRequest(String requestType, String url, Map<String, String> headMap, Object requestParamObj, AsyncHttpRequest.ResultCallBack resultCb){
        AsyncHttpRequest.request(requestType, url, headMap, requestParamObj, resultCb);
    }

    private static Object fileUpload(String url, Map<String, String> headMap, Map<String, String> requestParamMap, FormFile[] files) {
        Object obj = null;
        List<BasicNameValuePair> params = null;
        Response response = null;
        Log.v("upload:url", url);
        if(requestParamMap!=null && !requestParamMap.isEmpty()){
            params = new ArrayList<>();
            for(Map.Entry<String, String> entry : requestParamMap.entrySet()){
                Log.v("uplaod:params", entry.getKey()+"="+entry.getValue());
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            response = OkHttpClientManager.getInstance().syncUploadFiles(url, headMap, params, files);
            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                Log.v("upload:responseresult", responseStr);
                obj = responseStr;
            } else {
                int responseCode = response.code();
                Log.e("upload:error", responseCode + ":" + response.message());
                obj = responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
