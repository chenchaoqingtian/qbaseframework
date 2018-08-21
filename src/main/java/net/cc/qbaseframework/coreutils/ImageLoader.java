package net.cc.qbaseframework.coreutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ImageLoader {
	private static String TAG = ImageLoader.class.getSimpleName();

	private static ConcurrentHashMap<String, SoftReference<Bitmap>> mImageCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	private static ConcurrentHashMap<String, SoftReference<Bitmap>> mRoundImageCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

	public static Bitmap loadBitmap(String path, int scale) {
		// 查看缓存中是否已有该图片
		Bitmap bitmap = loadCacheBitmap(path);
		if (bitmap != null) {
			return bitmap;
		}

		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inSampleSize = scale;

		try {
			bitmap = BitmapFactory.decodeFile(path, options);

			if (bitmap != null) {
				mImageCache.put(path, new SoftReference<Bitmap>(bitmap));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.getMessage() + "");
		}

		return bitmap;
	}

	public static Bitmap loadBitmap(String path, int targetWidth, int targetHeight) {
		// 查看缓存中是否已有该图片
		Bitmap bitmap = loadCacheBitmap(path);
		if (bitmap != null) {
			return bitmap;
		}

		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inSampleSize = computeInsideSize(path, targetWidth, targetHeight);

		try {
			bitmap = BitmapFactory.decodeFile(path, options);

			if (bitmap != null) {
				mImageCache.put(path, new SoftReference<Bitmap>(bitmap));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.getMessage() + "");
		}

		return bitmap;
	}

	private static int computeInsideSize(String path, int dstWidth, int dstHeight) {
		Options outOptions = new Options();
		outOptions.inJustDecodeBounds = true;
		outOptions.inSampleSize = 1;
		BitmapFactory.decodeFile(path, outOptions);

		int yRatio = (int) Math.floor(1.0 * outOptions.outHeight / dstHeight);
		int xRatio = (int) Math.floor(1.0 * outOptions.outWidth / dstWidth);

		int result = 1;

		if (yRatio > 1 || xRatio > 1) {
			result = yRatio > xRatio ? yRatio : xRatio;
		}

		return result;
	}

	private static int computeCropSize(String path, int dstWidth, int dstHeight) {
		// 获取图片边界大小
		Options outOptions = new Options();
		outOptions.inJustDecodeBounds = true;
		outOptions.inSampleSize = 1;
		BitmapFactory.decodeFile(path, outOptions);

		int yRatio = (int) Math.floor(1.0 * outOptions.outHeight / dstHeight);
		int xRatio = (int) Math.floor(1.0 * outOptions.outWidth / dstWidth);

		int result = 1;

		if (yRatio > 1 || xRatio > 1) {
			result = yRatio > xRatio ? xRatio : yRatio;
		}

		return result;
	}

	public static Bitmap loadCacheBitmap(String path) {
		if (mImageCache.containsKey(path)) {
			SoftReference<Bitmap> softReference = mImageCache.get(path);
			Bitmap bitmap = softReference.get();
			if (null != bitmap) {
				return bitmap;
			}
		}

		return null;
	}

	public static Bitmap loadCacheRoundBitmap(String path) {
		if (mRoundImageCache.containsKey(path)) {
			SoftReference<Bitmap> softReference = mRoundImageCache.get(path);
			Bitmap bitmap = softReference.get();
			if (null != bitmap) {
				return bitmap;
			}
		}

		return null;
	}

	public static synchronized Bitmap loadFromLocal(String localPath, int width, int height, int roundSize) {
		Bitmap bitmap = (roundSize > 0) ? loadCacheRoundBitmap(localPath) : loadCacheBitmap(localPath);

		if (bitmap == null) {
			bitmap = (roundSize > 0) ? loadRoundBitmap(localPath, width, height, roundSize)
					: loadBitmap(localPath, width, height);
		}

		return bitmap;
	}

	public static Bitmap loadServerBitmap(String urlStr, String localPath, int width, int height, int roundSize) {
		Bitmap bitmap = null;
		File file = new File(localPath);

		if (file.exists()) {
			bitmap = loadFromLocal(localPath, width, height, roundSize);
		} else {
			try {
				FileUtils.downloadFile(urlStr, localPath);
				bitmap = loadFromLocal(localPath, width, height, roundSize);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage() + "");
			}
		}

		return bitmap;
	}

	public static synchronized Bitmap loadCodeBitmap(String urlStr, int width, int height) {
		Bitmap result = null;

		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();

			InputStream inputStream = conn.getInputStream();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

				if (bitmap != null) {
					result = zoomInBitmap(bitmap, width, height);
					if (result == null) {
						result = bitmap;
					} else {
						bitmap.recycle();
						bitmap = null;
					}
				}
			}

		} catch (Exception e) {
			Log.e(TAG, e.getMessage() + "");
		}

		return result;
	}

	private static Bitmap loadRoundBitmap(String path, int targetWidth, int targetHeight, int roundSize) {
		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inSampleSize = computeCropSize(path, targetWidth, targetHeight);

		Bitmap bitmap = null;

		try {
			bitmap = BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "" + e.getMessage());
		}

		if (bitmap == null) {
			return null;
		}

		Bitmap newBitmap = cropCenterBitmap(bitmap, targetWidth, targetHeight);

		Bitmap result = null;

		if (newBitmap != null) {
			result = createRoundBitmap(newBitmap, roundSize);
		}

		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}

		if (newBitmap != null) {
			newBitmap.recycle();
			newBitmap = null;
		}

		if (result != null) {
			mRoundImageCache.put(path, new SoftReference<Bitmap>(result));
		}

		return result;
	}

	private static Bitmap createRoundBitmap(Bitmap source, int roundSize) {
		Bitmap result = null;
		try{
			result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
			
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			
			Canvas canvas = new Canvas(result);
			RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
			canvas.drawRoundRect(rect, roundSize, roundSize, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(source, 0, 0, paint);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	public static void removeBitmapFromMemory(String path) {
		if (!StringUtils.isEmpty(path)) {
			mImageCache.remove(path);
			mRoundImageCache.remove(path);
		}
	}

	public static void clearCache() {
		mImageCache.clear();
	}

	public static void clearRoundImageCache() {
		mRoundImageCache.clear();
	}

	/**
	 * 
	 * @param bitmap
	 *            原图
	 * 
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap cropCenterBitmap(Bitmap bitmap, int width, int height) {
		if (null == bitmap || width <= 0 || height <= 0) {
			return null;
		}

		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		int scaledWidth;
		int scaledHeight;

		if ((widthOrg >= width && heightOrg >= height) || (widthOrg < width && heightOrg < height)) {
			int modifiedW = widthOrg * height / heightOrg;
			int modifiedH = heightOrg * width / widthOrg;

			scaledWidth = (modifiedW > width) ? modifiedW : width;
			scaledHeight = (modifiedW > width) ? height : modifiedH;

		} else if (widthOrg >= width) {
			scaledWidth = widthOrg * height / heightOrg;
			scaledHeight = height;

		} else {
			scaledWidth = width;
			scaledHeight = heightOrg * width / widthOrg;
		}

		Bitmap result = null;
		Bitmap scaledBitmap = null;

		try {
			scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
		} catch (Exception e) {
			// ignore
		}

		if (scaledBitmap != null) {
			// 从图中截取正中间的部分。
			int xTopLeft = (scaledWidth - width) / 2;
			int yTopLeft = (scaledHeight - height) / 2;
			
			boolean needRecycled = true;

			try {
				if(xTopLeft > 0 || yTopLeft > 0){
					result = Bitmap.createBitmap(scaledBitmap, xTopLeft < 0 ? 0 : xTopLeft, yTopLeft < 0 ? 0 : yTopLeft,
							width, height);
					needRecycled = true;
				}else{
					result = scaledBitmap;
					needRecycled =  false;
				}
				
			} catch (Exception e) {
				// ignore
			} finally {
				if(needRecycled){
					scaledBitmap.recycle();
					scaledBitmap = null;
				}
			}
		}

		return result;
	}

	public static byte[] bitmapToByteArray(Bitmap bmp) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.WEBP, 100, output);

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Bitmap loadNoCacheBitmap(String path, int scale) {
		Bitmap bitmap = null;

		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inSampleSize = scale;

		try {
			bitmap = BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "" + e.getMessage());
		}

		return bitmap;
	}

	private static Bitmap loadNoCacheBitmap(String path, int targetWidth, int targetHeight) {
		// 获取图片边界大小
		Bitmap bitmap = null;

		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inSampleSize = computeInsideSize(path, targetWidth, targetHeight);

		try {
			bitmap = BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "" + e.getMessage());
		}

		return bitmap;
	}

	private static Bitmap zoomInBitmap(Bitmap bitmap, int width, int height) {
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		Bitmap result = null;

		if (width > widthOrg && height > heightOrg) {
			int modifiedW = widthOrg * height / heightOrg;
			int modifiedH = heightOrg * width / widthOrg;

			int scaledWidth = (modifiedW > width) ? width : modifiedW;
			int scaledHeight = (modifiedW > width) ? modifiedH : height;

			try {
				result = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
			} catch (Exception e) {
				// ignore
			}
		}

		return result;
	}

	private static Bitmap zoomOutBitmap(Bitmap bitmap, int width, int height) {
		Bitmap result = null;

		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg > width && heightOrg > height) {
			int modifiedW = widthOrg * height / heightOrg;
			int modifiedH = heightOrg * width / widthOrg;

			int scaledWidth = (modifiedW > width) ? width : modifiedW;
			int scaledHeight = (modifiedW > width) ? modifiedH : height;

			try {
				result = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
			} catch (Exception e) {
				// ignore
			}
		}

		return result;
	}

	public static Bitmap zoomOutImage(String path, int width, int height) {
		Bitmap bitmap = loadNoCacheBitmap(path, width, height);

		if (bitmap == null) {
			return null;
		}

		Bitmap result = zoomOutBitmap(bitmap, width, height);

		if (result == null) {
			result = bitmap;
		} else {
			bitmap.recycle();
			bitmap = null;
		}

		return result;
	}

	public static String limitImageSize(Context context, String path, CompressFormat format) {
		final int width = 1080;
		final int height = 1920;

		Bitmap bitmap = zoomOutImage(path, width, height);

		if (bitmap == null) {
			return null;
		}

		String imagePath = FileUtils.generateRandomFile(AppFilePathUtils
				.getTempPathOfImg(context), "." + format.name());
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(imagePath);
			bitmap.compress(format, 75, out);
			out.flush();
		} catch (Exception e) {
			imagePath = null;
		} finally {
			CloseableObjectUtils.close(out);
		}

		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}

		return imagePath;
	}

	public static Bitmap compressBitmap(String path, int size) {
		Options outOptions = new Options();
		outOptions.inJustDecodeBounds = true;
		outOptions.inSampleSize = 1;
		BitmapFactory.decodeFile(path, outOptions);

		if (outOptions.outWidth * outOptions.outHeight * 2 <= size) {
			return loadNoCacheBitmap(path, 1);
		}

		int width = (int) Math.floor(Math.sqrt(size * outOptions.outWidth / (2.0 * outOptions.outHeight)));
		int height = width * outOptions.outHeight / outOptions.outWidth;

		Bitmap result = zoomOutImage(path, width, height);

		return result;
	}

}
