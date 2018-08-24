package net.cc.qbaseframework.coreutils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import net.cc.qbaseframework.R;

import java.io.IOException;

/**
 * @Description 系统功能工具类
 * @author chen chao
 */
public class SysFunctionUtils
{
	public static final int IMAGE = 0;
	public static final int AUDIO = 1;
	public static final int PDF = 2;
	public static final int PPT = 3;
	public static final int WORD = 4;
	public static final int EXCEL = 5;
	public static final int CHM = 6;
	public static final int HTML = 7;
	public static final int TEXT = 8;
	public static final int VIDEO = 9;
	public static final int APK = 10;

	/**
	 * @Title: getLocalMediaFile 
	 * @Description: 在activity中获取本地媒体文件
	 * @param activity 
	 * @param requestCode 请求码 
	 * @param type  0：获取图片媒体，1：获取音频媒体
	 * @throws
	 */
	public static void getLocalMediaFile(Activity activity, int requestCode, int type)
	{
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		if (type == 0)
		{
			intent.setType("image/*");
		}
		if (type == 1)
		{
			intent.setType("audio/*");
		}
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * @Title: getLocalMediaFile 
	 * @Description: 在fragment中获取本地媒体文件
	 * @param fragment 
	 * @param requestCode 请求码 
	 * @param type  0：获取图片媒体，1：获取音频媒体
	 * @throws
	 */
	public static void getLocalMediaFile(Fragment fragment, int requestCode, int type)
	{
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		if (type == 0)
		{
			intent.setType("image/*");
		}
		if (type == 1)
		{
			intent.setType("audio/*");
		}
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * 安装应用
	 */
	public static void installPackage(Context context, String filePath)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 创建快捷方式
	 */
	public static void makeShortcut(Context context, String paramString1, int dataType, String paramString2)
	{
		Intent intent1 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		intent1.putExtra("android.intent.extra.shortcut.NAME", paramString1);
		Intent intent2 = new Intent("android.intent.action.MAIN");
		intent2.addCategory("android.intent.category.LAUNCHER");
		intent2.setComponent(new ComponentName(context.getPackageName(), paramString2));
		intent1.putExtra("duplicate", false);
		intent1.putExtra("android.intent.extra.shortcut.INTENT", intent2);
		intent1.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(context, dataType));
		context.sendBroadcast(intent1);
	}

	/**
	 *  声音警报
	 */
	public static void soundReceiveDataAlarm(Context context)
	{
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		if (uri == null)
		{
			uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			if (uri == null)
			{
				uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		MediaPlayer mediaPlayer = new MediaPlayer();
		try
		{
			mediaPlayer.setDataSource(context, uri);
			if (((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(4) != 0)
			{
				mediaPlayer.setAudioStreamType(4);
				mediaPlayer.setLooping(false);
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
			return;
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return;
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			return;
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
			return;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 浏览文件
	 */
	public static void viewFile(Activity activity, int dataType, String filePath)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		Uri uri = Uri.parse("file://" + filePath);
		if (dataType == IMAGE)
		{
			intent.setDataAndType(uri, "image/*");
		}
		if (dataType == AUDIO)
		{
			intent.setDataAndType(uri, "audio/*");
		}
		if (dataType == PDF)
		{
			intent.setDataAndType(uri, "application/pdf");
		}
		if (dataType == PPT)
		{
			intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		}
		if (dataType == WORD)
		{
			intent.setDataAndType(uri, "application/msword");
		}
		if (dataType == EXCEL)
		{
			intent.setDataAndType(uri, "application/vnd.ms-excel");
		}
		if (dataType == CHM)
		{
			intent.setDataAndType(uri, "application/x-chm");
		}
		if (dataType == HTML)
		{
			intent.setDataAndType(uri, "text/html");
		}
		if (dataType == TEXT)
		{
			intent.setDataAndType(uri, "text/plain");
		}
		if (dataType == VIDEO)
		{
			intent.setDataAndType(uri, "video/*");
		}
		if (dataType == APK)
		{
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
		}
		activity.startActivity(intent);
	}

	/**
	 *  调用系统的网络设置面板
	 */
	public static void openWirelessSettings(Context context)
	{
		Intent intent;
		if (Build.VERSION.SDK_INT > 10)
		{
			intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		}
		else
		{
			intent = new Intent();
			intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.WirelessSettings"));
			intent.setAction("android.intent.action.VIEW");
		}
		context.startActivity(intent);
	}

	/**
	 *  调用系统gps设置面板
	 */
	public static void openGpsSettings(Context context)
	{
		context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	}

	/**
	 * 获取设备ID
	 */
	public static String getDeviceId(Context context){
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	/**
	 * 拨打电话
	 */
	public static void call(Context context , String telNumber){
		Intent intent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telNumber));
		try
		{
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			ToastUtils.showLongToast(context, R.string.device_call_disable);
		}
	}

	/**
	 * 发送短信
	 */
	public static void sendMessage(Context context , String telNumber){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.putExtra("address", telNumber);
		intent.putExtra("sms_body", "");
		intent.setType("vnd.android-dir/mms-sms");
		try
		{
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			ToastUtils.showLongToast(context, R.string.device_sendmsg_disable);
		}
	}
}
