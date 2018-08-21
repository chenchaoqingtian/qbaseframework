package net.cc.qbaseframework.corenet.okhttp;

/**
 * @ClassName HttpRequest
 * @Description Http请求抽象类
 * @author chen chao
 * @date 2016-4-28
 */

public abstract class HttpRequest
{
	public static final String POST_METHOD = "POST";
	public static final String GET_METHOD = "GET";
	public static final String DELETE_METHOD = "DELETE";

	public static final String CONTENT_TYPE_JSON = "json";
	public static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED = "x-www-form-urlencoded";
}
