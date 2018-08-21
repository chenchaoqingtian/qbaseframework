package net.cc.qbaseframework.corelogger;

import android.util.Log;

import net.cc.qbaseframework.coreutils.DateTimeUtils;
import net.cc.qbaseframework.coreutils.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Description 日志基类
 */

public class Logger
{
	private static String mSavePath;
    private static boolean mIsDebug = false;

	private static final SimpleDateFormat CURRENT_TIME = new SimpleDateFormat("[HH:mm:ss]", Locale.CHINA);

	public static void initialize(String savePath, boolean isDebug){
		Logger.mSavePath = savePath;
		Logger.mIsDebug = isDebug;
	}

	private static Writer mWriter;

	public static void logException(String tag, String message)
	{
		Log.e(tag, CURRENT_TIME.format(new Date()) + ":" + message);
		if(!mIsDebug){
			printLog(LogType.Error, tag, message);
		}
	}

	public static void logAndThrowException(String tag, String message, Throwable tr)
	{
		logException(tag, message);
		try
		{
			throw tr;
		}
		catch (Throwable throwable)
		{
			throwable.printStackTrace();
			return;
		}
	}

	public static void logInformation(String tag, String message)
	{
		Log.i(tag, CURRENT_TIME.format(new Date()) + ":" + message);
		if(!mIsDebug){
			printLog(LogType.Information, tag, message);
		}
	}

	public static void logWarning(String tag, String message)
	{
		Log.w(tag, CURRENT_TIME.format(new Date()) + ":" + message);
		if(!mIsDebug){
			printLog(LogType.Warning, tag, message);
		}
	}

	public static void logError(String tag, String message)
	{
		Log.e(tag, CURRENT_TIME.format(new Date()) + ":" + message);
		if(!mIsDebug){
			printLog(LogType.Error, tag, message);
		}
	}

	private static void printLog(LogType paramLogType, String tag, String message)
	{
		if(StringUtils.isEmpty(mSavePath)) {
			Log.e("logger", "Logger is not initialization!");
			return;
		}
		String filePath = mSavePath + DateTimeUtils.getDateFormatForFile() + "_" + "log.txt";
		File logFile = new File(filePath);
		String logFileDirPath = logFile.getParent();
		if (logFileDirPath != null)
		{
			new File(logFileDirPath).mkdirs();
		}
		try
		{
			if (!logFile.exists())
			{
				logFile.createNewFile();
			}
			mWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true)));
			mWriter.write(CURRENT_TIME.format(new Date()));
			if (paramLogType == LogType.Information)
			{
				mWriter.write("Info/" + tag + ":" + message);
			}
			else if (paramLogType == LogType.Warning)
			{
				mWriter.write("Warning/" + tag + ":" + message);
			}
			else if (paramLogType == LogType.Error)
			{
				mWriter.write("Error/" + tag + ":" + message);
			}
			mWriter.write('\n');
			mWriter.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				mWriter.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
