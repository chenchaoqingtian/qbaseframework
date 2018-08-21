package net.cc.qbaseframework.coreutils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class AppFilePathUtils {

	private static String getImgRootDirectory(Context context){
		File rootFile = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

		if(!rootFile.exists()){
			rootFile.mkdirs();
		}

		return rootFile.getAbsolutePath();
	}

	public static String getLocalPathOfImg(Context context, String remotePath){
		if (StringUtils.isEmpty(remotePath)) {
			return null;
		}

		Uri uri = Uri.parse(remotePath);
		String path = uri.getPath() + ".idb";

		File file = new File(getImgRootDirectory(context) + path);

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		return file.getAbsolutePath();
	}

	public static String getTempPathOfImg(Context context){
		File file = new File(getImgRootDirectory(context) + "/temp");

		if(!file.exists()){
			file.mkdirs();
		}

		return file.getAbsolutePath();
	}

	public static String getPhotoPath(Context context){
		File file = new File(getImgRootDirectory(context) + "/camera");

		if(!file.exists()){
			file.mkdirs();
		}

		return file.getAbsolutePath();
	}

	public static String getAudioPath(Context context){
		File rootFile = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);

		File file = new File(rootFile.getAbsolutePath() + "/audio");

		if(!file.exists()){
			file.mkdirs();
		}

		return file.getAbsolutePath();
	}

	public static String getApplicationDirectory(Context context){
		File rootFile = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
		File result = new File(rootFile.getAbsolutePath() + "/apps");

		if(!result.exists()){
			rootFile.mkdirs();
		}

		return result.getAbsolutePath();
	}

	public static String getTempPath(Context context){
		String path = context.getExternalCacheDir() + "/temp/";
		if(!SdCardUtils.exist()){
			path = context.getFilesDir().getPath() + "/temp/";
		}
		return path;
	}

	public static String getDownloadPath(Context context)
	{
		String path = context.getExternalCacheDir()+ "/download/";
		if(!SdCardUtils.exist()){
			path = context.getFilesDir().getPath() + "/download/";
		}
		return path;
	}

	public static String getCrashLogPath(Context context)
	{
		String path = context.getExternalCacheDir()+ "/crash/";
		if(!SdCardUtils.exist()){
			path = context.getFilesDir().getPath() + "/crash/";
		}
		return path;
	}

	public static String getSystemLogPath(Context context)
	{
		String path = context.getExternalCacheDir() + "/log/";
		if(!SdCardUtils.exist()){
			path = context.getFilesDir().getPath() + "/log/";
		}
		return path;
	}

	public static String getPluginFilePath(Context context)
	{
		return context.getFilesDir().getPath() + "/plugin/";
	}

}
