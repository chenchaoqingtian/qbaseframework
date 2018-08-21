/**  
 * @Project: pda-common-for-eclipse
 * @Title: ImageUtils.java
 * @Package com.zteict.pda.common.utils
 * @author chen chao
 * @date 2016-6-23 上午10:00:30
 * @Copyright: 2016 深圳中兴网信科技有限公司 Inc. All rights reserved.
 * @version 1.1  
 */

package net.cc.qbaseframework.coreutils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName ImageUtils
 * @Description 图片处理工具
 * @author chen chao
 * @date 2016-6-23
 */

public class ImageUtils
{
	public static String TEMP_DIR;
	public static final String TMP_NAME = "tmp.jpg";
	private static List<String> CAPTURE_SMALL_SIZE_IMAGE_DEVICES = new ArrayList<String>();
	public static final int DEFAULT_IMAGE_WIDTH = 480;
	public static final int DEFAULT_IMAGE_HEIGHT = 640;

	private static boolean shouldCaptureSmallImage()
	{
		return CAPTURE_SMALL_SIZE_IMAGE_DEVICES.contains(Build.BRAND + "/"
				+ Build.PRODUCT + "/" + Build.DEVICE);
	}

	public static void showImageCapture(Activity activity, int paramInt, String tempDir)
	{
		TEMP_DIR = tempDir;
		Intent intent = new Intent();
		intent.setAction("android.media.action.IMAGE_CAPTURE");
		if (!shouldCaptureSmallImage())
		{
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createTempFile()));
		}
		activity.startActivityForResult(intent, paramInt);
	}

	public static void showImageCapture(Fragment fragment, int paramInt, String tempDir)
	{
		TEMP_DIR = tempDir;
		Intent intent = new Intent();
		intent.setAction("android.media.action.IMAGE_CAPTURE");
		if (!shouldCaptureSmallImage())
		{
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createTempFile()));
		}
		fragment.startActivityForResult(intent, paramInt);
	}

	private static File createTempFile(){
		File file = new File(TEMP_DIR, TMP_NAME);
		String dir = file.getParent();
		if(dir!=null){
			new File(dir).mkdirs();
		}
		if(!file.exists()){
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}

	public static void saveBitmap(Bitmap bm, String dir, String photoName) {
		try {
			if (!FileUtils.isExitsFile(dir)) {
				new File(dir).mkdirs();
			}
			File file = new File(dir, photoName); 
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 	


	/**
	 * @Title: handlePhotoForIntent 
	 * @Description: 根据意图处理照片
	 * @param @param activity
	 * @param @param intent
	 * @param @param width
	 * @param @param height
	 * @return Bitmap
	 */
	public static Bitmap handlePhotoForIntent(Activity activity,Intent intent)
	{
		if (shouldCaptureSmallImage())
		{
			Bitmap bitmap1 = null;
			if (intent != null)
			{
				Bundle bundle = intent.getExtras();
				if (bundle != null)
				{
					Parcelable parcelable = bundle.getParcelable("data");
					if (parcelable != null)
					{
						Bitmap bitmap2 = (Bitmap) bundle.getParcelable("data");
						if ((bitmap2 != null)
								&& (bitmap2.getWidth() > bitmap2.getHeight()))
						{
							bitmap2 = rotateBitmap(bitmap2, 90.0F);
						}
						bitmap1 = zoomBitmap(bitmap2, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
					}
				}
			}
			return bitmap1;
		}
		return handlePhotoForFile(TEMP_DIR+TMP_NAME);
	}


	/**
	 * @Title: handlePhotoForFile 
	 * @Description: 根据文件处理照片
	 * @param @param filePath
	 * @return Bitmap
	 */
	public static Bitmap handlePhotoForFile(String filePath)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = Math.max(options.outWidth / DEFAULT_IMAGE_WIDTH,
				options.outHeight / DEFAULT_IMAGE_HEIGHT);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		if ((bitmap != null) && (bitmap.getWidth() > bitmap.getHeight()))
		{
			bitmap = rotateBitmap(bitmap, 90.0F);
		}
		return zoomBitmap(bitmap, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
	}

	public static Bitmap rotateBitmap(Bitmap paramBitmap, float paramFloat)
	{
		if (paramBitmap != null)
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(paramFloat);
			int i = paramBitmap.getWidth();
			int j = paramBitmap.getHeight();
			Bitmap bitmap = Bitmap.createBitmap(paramBitmap, 0, 0, i, j,
					matrix, true);
			paramBitmap.recycle();
			if (bitmap != paramBitmap)
			{
				paramBitmap.recycle();
				paramBitmap = bitmap;
			}
		}
		return paramBitmap;
	}

	public static Bitmap zoomBitmap(Bitmap paramBitmap, int width,
			int height)
	{
		if ((paramBitmap != null)
				&& (((paramBitmap.getWidth() != width) || (paramBitmap
						.getHeight() != height))))
		{
			Bitmap bitmap = Bitmap.createScaledBitmap(paramBitmap, width,
					height, false);
			if (paramBitmap != bitmap)
			{
				paramBitmap.recycle();
				paramBitmap = bitmap;
			}
		}
		return paramBitmap;
	}

	public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {      
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小      
		BitmapFactory.Options options = new BitmapFactory.Options();      
		options.inJustDecodeBounds = true;    
		BitmapFactory.decodeFile(filePath, options);
		// 调用上面定义的方法计算inSampleSize值       
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);  
		// 使用获取到的inSampleSize值再次解析图片       
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);   
	} 

	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) { 
		// 源图片的高度和宽度    
		final int height = options.outHeight; 
		final int width = options.outWidth;   
		int inSampleSize = 1;      
		if (height > reqHeight || width > reqWidth) {    
			// 计算出实际宽高和目标宽高的比率       
			final int heightRatio = Math.round((float) height / (float) reqHeight); 
			final int widthRatio = Math.round((float) width / (float) reqWidth);     
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高           // 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      
		}       
		return inSampleSize;  
	} 
}
