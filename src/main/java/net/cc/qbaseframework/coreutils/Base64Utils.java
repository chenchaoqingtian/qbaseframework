package net.cc.qbaseframework.coreutils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description Base64编解码工具
 */

public class Base64Utils
{
	/**
	 * 文件读取缓冲区大小
	 */
	private static final int CACHE_SIZE = 1024;

	/**
	 * BASE64字符串解码为二进制数据
	 * @param base64
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode(String base64) throws Exception {
		return Base64.decode(base64.getBytes(), Base64.DEFAULT);
	}

	/**
	 * 二进制数据编码为BASE64字符串
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encode(byte[] bytes) throws Exception {
		return new String(Base64.encode(bytes, Base64.DEFAULT));
	}

	/**
	 * <p>
	 * 将文件编码为BASE64字符串
	 * </p>
	 * 大文件慎用，可能会导致内存溢出
	 * </p>
	 * @param filePath 文件绝对路径
	 * @return
	 * @throws Exception
	 */
	public static String encodeFile(String filePath) throws Exception {
		byte[] bytes = fileToByte(filePath);
		return encode(bytes);
	}

	/**
	 * BASE64字符串转回文件
	 * @param filePath 文件绝对路径
	 * @param base64 编码字符串
	 * @throws Exception
	 */
	public static void decodeToFile(String filePath, String base64) throws Exception {
		byte[] bytes = decode(base64);
		byteArrayToFile(bytes, filePath);
	}

	/**
	 * @Title: bitmaptoString
	 * @Description: Bitmap 转 字符串
	 * @param @param bitmap
	 * @param @throws Exception    设定文件 
	 * @return String Base编码的字符串
	 * @throws
	 */
	public static String bitmapToString(Bitmap bitmap) throws Exception{
		String string=null;
		ByteArrayOutputStream bStream=new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG,100,bStream);
		byte[]bytes=bStream.toByteArray();
		string = encode(bytes);
		return string;
	}

	/**
	 * @Title: stringToBitmap
	 * @Description: 字符串 转 Bitmap
	 * @param @param base64
	 * @param @throws Exception 设定文件 
	 * @return Bitmap    返回类型 
	 * @throws
	 */
	public static Bitmap stringToBitmap(String base64) throws Exception{
		byte[] bytes = decode(base64);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 文件转换为二进制数组
	 * @param filePath 文件路径
	 * @return
	 * @throws Exception
	 */
	public static byte[] fileToByte(String filePath) throws Exception {
		byte[] data = new byte[0];
		File file = new File(filePath);
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
			byte[] cache = new byte[CACHE_SIZE];
			int nRead = 0;
			while ((nRead = in.read(cache)) != -1) {
				out.write(cache, 0, nRead);
				out.flush();
			}
			out.close();
			in.close();
			data = out.toByteArray();
		}
		return data;
	}

	/**
	 * 二进制数据转文件
	 * @param bytes 二进制数据
	 * @param filePath 文件生成目录
	 */
	public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
		InputStream in = new ByteArrayInputStream(bytes);
		File destFile = new File(filePath);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		destFile.createNewFile();
		OutputStream out = new FileOutputStream(destFile);
		byte[] cache = new byte[CACHE_SIZE];
		int nRead = 0;
		while ((nRead = in.read(cache)) != -1) {
			out.write(cache, 0, nRead);
			out.flush();
		}
		out.close();
		in.close();
	}
}
