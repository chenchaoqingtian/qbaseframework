package net.cc.qbaseframework.coreutils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * @Description 网络环境检测工具
 * @author chen chao
 */
public class NetworkDetectUtils
{
	/**
	 * @Title: detectNetWork
	 * @Description: 检测网络是否可以可用
	 * @param context
	 * @return boolean
	 */
	public static boolean isConnectivityActive(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
		{
			return false;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable())
		{
			return false;
		}
		return true;
	}

	/**
	 * @Title: networkIs3GType
	 * @Description: 检测网络类型是否为3G
	 * @param activity
	 * @return boolean
	 */
	public static boolean networkIs3GType(Activity activity)
	{
		ConnectivityManager manager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
		{
			return false;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		int networkType = networkInfo.getType();
		int netSubtype = networkInfo.getSubtype();
		// 是否为3G网络
		if (networkType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS)
		{
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * @Title: networkIsWifiType
	 * @Description: 检测网络类型是否为wifi
	 * @param activity
	 * @return boolean
	 */
	public static boolean networkIsWifiType(Activity activity)
	{
		ConnectivityManager manager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
		{
			return false;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		int networkType = networkInfo.getType();
		// 是否为wifi网络
		if (networkType == ConnectivityManager.TYPE_WIFI)
		{
			return networkInfo.isConnected();
		}
		return false;
	}
}
