package net.cc.qbaseframework.coreutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import net.cc.qbaseframework.corelogger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName CrashHandlerUtils
 * @Description 程序异常处理及异常信息收集工具
 * @author chen chao
 */

public class CrashHandlerUtils implements UncaughtExceptionHandler
{
	public static final String TAG = CrashHandlerUtils.class.getName();

	private UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandlerUtils INSTANCE;
	private String crashMessage = "程序出错,即将退出！";
	private Context mContext;
	public  String mSavePath;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);

	private CrashHandlerUtils(){}

	public static CrashHandlerUtils getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new CrashHandlerUtils();
		}
		return INSTANCE;
	}

	public void initialize(Context context, String savePath, boolean isDebug)
	{
		mContext = context;
		this.mSavePath = savePath;
		if(!isDebug){
			mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(this);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			mDefaultHandler.uncaughtException(thread, ex);
		}
		else
		{
			try {
				Thread.sleep(2000);
				System.exit(0);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 *
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex)
	{
		if (ex == null)
		{
			return true;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run()
			{
				Looper.prepare();
				ToastUtils.showLongToast(mContext, crashMessage);
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 *
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx)
	{
		try
		{
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null)
			{
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		}
		catch (NameNotFoundException e)
		{
			Logger.logAndThrowException(TAG, "Error while collect package info!" + e.getMessage(), e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			}
			catch (Exception e)
			{
				Logger.logAndThrowException(TAG, "Error while collect crash info!" + e.getMessage(), e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 *
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private void saveCrashInfo2File(Throwable ex)
	{
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null)
		{
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try
		{
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".txt";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				File dir = new File(mSavePath);
				if (!dir.exists())
				{
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(mSavePath + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
		}
		catch (Exception e)
		{
			Logger.logAndThrowException(TAG, "an error occured while writing report file!" + e.getMessage(), e);
		}
	}
}
