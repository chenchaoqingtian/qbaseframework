package net.cc.qbaseframework.corenet.okhttp;

import net.cc.qbaseframework.R;

/**
 * @ClassName RequestCommonError
 * @Description 请求响应错误码
 * @author chen chao
 * @date 2016-4-27
 */

public enum RequestCommonError
{
	RES_NO_FIND(404, R.string.res_no_find),
	NO_NETWORK(10000,R.string.no_internet_msg),
	UNKOWN_EXCEPTION(10001,R.string.internet_error_msg),
	SOCKET_TIMEOUT(10002,R.string.socket_timeout_msg),
	URL_ERROR(10003, R.string.url_error_msg);

	private final int errorCode;
	private final int errorMsgRes;

	private RequestCommonError(int errorCode, int errorMsgRes){
		this.errorCode = errorCode;
		this.errorMsgRes = errorMsgRes;
	}

	public static int getErrorMsgRes(int errorCode){
		switch (errorCode)
		{
			case 404:
				return R.string.res_no_find;
			case 10000:
				return R.string.no_internet_msg;
			case 10001:
				return R.string.internet_error_msg;
			case 10002:
				return R.string.socket_timeout_msg;
			case 10003:
				return R.string.url_error_msg;
			default:
				return R.string.unkown_exception;
		}
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public int getErrorMsgRes()
	{
		return errorMsgRes;
	}
}
