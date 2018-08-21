package net.cc.qbaseframework.corenet.okhttp;

import android.util.Log;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OkHttpClientManager
 * @Description OKHttpClient管理
 * @author chen chao
 * @date 2016-4-26
 */

public class OkHttpClientManager
{
	private OkHttpClient mOkHttpClient;
	private static OkHttpClientManager okHttpClientManager;

	public static final String ENCODE_UTF8 = "UTF-8";

	private OkHttpClientManager(){
		mOkHttpClient = new OkHttpClient();

	}

	public static OkHttpClientManager getInstance(){
		if(okHttpClientManager == null){
			synchronized(OkHttpClientManager.class){
				okHttpClientManager = new OkHttpClientManager();
			}
		}
		return okHttpClientManager;
	}

	/*
	 * 同步执行
	 */
	private Response execute(Request request) throws IOException{
		return mOkHttpClient.newCall(request).execute();
	}

	/*
	 * 异步执行
	 */
	private void enqueue(Request request,Callback callBack){
		mOkHttpClient.newCall(request).enqueue(callBack);
	}

	/**
	 *
	 * @Title: syncGet
	 * @Description: 同步get请求
	 * @param  url 请求url
	 * @param  headMap 请求头信息
	 * @param  params 请求参数
	 * @return String 返回类型 
	 * @throws IOException
	 */
	public Response syncGet(String url,Map<String, String> headMap,List<BasicNameValuePair> params) throws IOException{
		return execute(buildGetRequest(url, headMap, params));
	}

	/**
	 * @Title: asyncGet
	 * @Description: 异步get请求
	 * @param  url 请求url
	 * @param  headMap 请求头信息
	 * @param  params 请求参数
	 * @param  callBack 回调
	 * @return void
	 */
	public void asyncGet(String url,Map<String, String> headMap,List<BasicNameValuePair> params, Callback callBack){
		enqueue(buildGetRequest(url, headMap, params), callBack);
	}

	/**
	 * @Title: syncPost
	 * @Description: 同步post请求
	 * @param  url 请求url
	 * @param  headMap 请求头信息
	 * @param  params 请求参数
	 * @return Response
	 * @throws IOException
	 */
	public Response syncPost(String url,Map<String, String> headMap, String params) throws IOException{
		Request request = buildPostRequest(url, headMap, params);
		return execute(request);
	}

	/**
	 * @Title: syncPost
	 * @Description: 同步post请求（带文件上传）
	 * @param  url 请求url
	 * @param  headMap 请求头信息
	 * @param  params 请求参数
	 * @return Response
	 * @throws IOException
	 */
	public Response syncUploadFiles(String url,Map<String, String> headMap, List<BasicNameValuePair> params, FormFile[] files) throws IOException{
		Request request = buildMultipartPostRequest(url, headMap, params, files);
		return execute(request);
	}

	/**
	 * @Title: asyncPost
	 * @Description: 异步post请求（带文件上传）
	 * @param  url 请求url
	 * @param  headMap 请求头信息
	 * @param  params 请求参数
	 * @param  callBack 回调
	 * @return void
	 */
	public void asyncPost(String url,Map<String, String> headMap, String params, Callback callBack){
		Request request = buildPostRequest(url, headMap, params);
		enqueue(request, callBack);
	}

	/**
	 * @Title: asyncPost
	 * @Description: 异步post请求（带文件上传）
	 * @param  url 请求url
	 * @param  headMap 请求头信息
	 * @param  params 请求参数
	 * @param  callBack 回调
	 * @return void
	 */
	public void asyncUploadFiles(String url,Map<String, String> headMap,List<BasicNameValuePair> params, FormFile[] files, Callback callBack){
		Request request = buildMultipartPostRequest(url, headMap, params, files);
		enqueue(request, callBack);
	}

	/*
	 * 构造get请求
	 */
	private Request buildGetRequest(String url,Map<String, String> headMap,List<BasicNameValuePair> params){
		Builder builder = new Request.Builder();
		if(headMap!=null && !headMap.isEmpty()){
			addHeaders(builder, headMap);
		}
		if(params!=null && !params.isEmpty()){
			url = getGetUrl(url, params);
		}
		Log.v("GetRequest:url", url);
		return builder.url(url).build();
	}

	/*
	 * 构造post请求
	 */
	private Request buildPostRequest(String url, Map<String, String> headMap, String params){
		Builder builder = new Request.Builder();
		if(headMap!=null && !headMap.isEmpty()){
			addHeaders(builder, headMap);
		}
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JSON, params);

		return builder.url(url).post(body).build();
	}

	/*
	 * 构造post请求
	 */
	private Request buildPostRequest(String url, Map<String, String> headMap, List<BasicNameValuePair> params){
		Builder builder = new Request.Builder();
		if(headMap!=null && !headMap.isEmpty()){
			addHeaders(builder, headMap);
		}
		okhttp3.FormBody.Builder formBodyBuilder = new FormBody.Builder();
		if(params!=null && !params.isEmpty()){
			for (BasicNameValuePair param : params)
			{
				formBodyBuilder.add(param.getName(), param.getValue());
			}
		}
		FormBody body = formBodyBuilder.build();
		return builder.url(url).post(body).build();
	}

	/*
	 * 构造带文件上传的post请求
	 */
	private Request buildMultipartPostRequest(String url, Map<String, String> headMap, List<BasicNameValuePair> params, FormFile []files){
		Builder builder = new Request.Builder();
		if(headMap!=null && !headMap.isEmpty()){
			addHeaders(builder, headMap);
		}
		okhttp3.MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		if(params!=null && !params.isEmpty()){
			for (BasicNameValuePair param : params)
			{
				multipartBodyBuilder.addFormDataPart(param.getName(), param.getValue());
			}
		}
		if(files!=null && files.length>0){
			for (FormFile formFile : files)
			{
				RequestBody fileRequestBody = RequestBody.create(MediaType.parse(formFile.getContentType()), formFile.getFile());
				multipartBodyBuilder.addFormDataPart(formFile.getParameterName(), formFile.getFile().getName(), fileRequestBody);
			}
		}
		RequestBody body = multipartBodyBuilder.build();
		return builder.url(url).post(body).build();
	}

	/*
	 * 设置头信息
	 */
	private Builder addHeaders(Builder builder, Map<String, String> headMap){
		for (Map.Entry<String, String> entry : headMap.entrySet())
		{
			builder.addHeader(entry.getKey(), entry.getValue());
		}
		return builder;
	}

	/*
	 * 获得get请求url
	 */
	private String getGetUrl(String url, List<BasicNameValuePair> params){
		return url + "?" + URLEncodedUtils.format(params, ENCODE_UTF8);
	}
}