package net.cc.qbaseframework.coreutils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName FileUtils
 * @Description 文件处理工具
 * @author chen chao
 */

public class FileUtils
{
	private static final int BUFF_SIZE = 4096;

	public static void deleteFile(File file)
	{
		if (file != null)
		{
			if (file.isDirectory())
			{
				File[] arrayOfFile = file.listFiles();
				if (arrayOfFile.length == 0)
				{
					file.delete();
					return;
				}
				else
				{
					for (int i = 0; i < arrayOfFile.length; i++)
					{
						deleteFile(arrayOfFile[i]);
					}
				}
				file.delete();
			}
			else
			{
				file.delete();
			}
		}
	}

	public static void copyFile(File file1, File file2) throws IOException
	{
		String str = file2.getParent();
		if (str != null)
		{
			new File(str).mkdirs();
		}
		if (!file2.exists())
		{
			file2.createNewFile();
		}
		FileChannel fileChannel1 = null;
		FileChannel fileChannel2 = null;
		try
		{
			fileChannel1 = new FileInputStream(file1).getChannel();
			fileChannel2 = new FileOutputStream(file2).getChannel();
			fileChannel2.transferFrom(fileChannel1, 0L, fileChannel1.size());
			return;
		}
		finally
		{
			if (fileChannel1 != null)
			{
				fileChannel1.close();
			}
			if (fileChannel2 != null)
			{
				fileChannel2.close();
			}
		}
	}

	/**
	 * 得到远程文件的大小
	 */
	public static int getFileSize(String fileUrl) throws Exception
	{
		URL url = new URL(fileUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(8000);
		conn.setReadTimeout(8000);
		return conn.getContentLength();
	}

	/**
	 * 文件长度转换
	 */
	public static String convertFileSize(long filesize)
	{
		String strUnit = "Bytes";
		String strAfterComma = "";
		int intDivisor = 1;
		if (filesize >= 1024 * 1024)
		{
			strUnit = "MB";
			intDivisor = 1024 * 1024;
		}
		else if (filesize >= 1024)
		{
			strUnit = "KB";
			intDivisor = 1024;
		}
		if (intDivisor == 1)
			return filesize + " " + strUnit;
		strAfterComma = "" + 100 * (filesize % intDivisor) / intDivisor;
		if ("".equals(strAfterComma))
			strAfterComma = ".0";
		return filesize / intDivisor + "." + strAfterComma + " " + strUnit;
	}

	public static boolean copyFile(String filePath1, String filePath2)
	{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try
		{
			fis = new FileInputStream(filePath1);
			fos = new FileOutputStream(filePath2);
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = fis.read(bytes)) != -1)
			{
				fos.write(bytes, 0, len);
			}
			fis.close();
			fos.close();
			return true;
		}
		catch (IOException localIOException)
		{
			return false;
		}
	}

	public static void downloadFile(String fileRemotePath, String fileSavePath) throws Exception
	{
		URL url = new URL(fileRemotePath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(8000);
		InputStream is = conn.getInputStream();
		File file = new File(fileSavePath);
		String str = file.getParent();
		if (str != null)
		{
			new File(str).mkdirs();
		}
		if (file.exists())
		{
			file.delete();
		}
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = new byte[1024];
		int len = 0;
		while ((len = is.read(bytes)) != -1)
		{
			fos.write(bytes, 0, len);
		}
		fos.close();
		is.close();
	}

	public static boolean isExitsFile(String path)
	{
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 
	 * @param baseDir
	 *            创建文件所在目录
	 * @param surfix
	 *            文件后缀（含“.”）
	 * @return
	 */
	public static String generateRandomFile(String baseDir, String surfix)
	{
		File file = null;
		do
		{
			file = new File(baseDir, UUID.randomUUID().toString() + surfix);
		}
		while (file.exists());

		createParentDirs(file.getAbsolutePath());

		return file.getAbsolutePath();
	}

	public static void createParentDirs(String filePath)
	{
		File parentFile = new File(filePath).getParentFile();
		if (!parentFile.exists())
		{
			parentFile.mkdirs();
		}
	}

	public static boolean renameFile(File oldFile, File newFile, boolean override)
	{
		if (!oldFile.exists())
		{
			return false;
		}
		if (newFile.exists())
		{
			if (newFile.equals(oldFile))
			{
				return true;
			}
			if (override)
			{
				deleteFile(newFile);
			}
			else
			{
				return true;
			}
		}
		return oldFile.renameTo(newFile);
	}

	public static String writeStreamToFile(InputStream inputStream, File targetFile, boolean overwrite) throws FileNotFoundException, IOException
	{
		if (!targetFile.exists())
		{
			if (!targetFile.getParentFile().exists())
			{
				targetFile.getParentFile().mkdirs();
			}
			targetFile.createNewFile();
		}
		else if (!overwrite)
		{
			return targetFile.getPath();
		}
		byte[] buffer = new byte[BUFF_SIZE];
		int length;
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		BufferedOutputStream outputStream = null;
		try
		{
			outputStream = new BufferedOutputStream(new FileOutputStream(targetFile), BUFF_SIZE);
			while ((length = bufferedInputStream.read(buffer, 0, BUFF_SIZE)) != -1)
			{
				outputStream.write(buffer, 0, length);
			}

			outputStream.flush();
		}
		catch (FileNotFoundException ex)
		{
			throw ex;
		}
		catch (IOException exception)
		{
			throw exception;
		}
		finally
		{
			CloseableObjectUtils.close(outputStream);
		}
		return targetFile.getPath();
	}

	/**
	 * 删除文件
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名，要在系统内保持唯一
	 * @return boolean 存储成功的标志
	 */
	public static boolean deleteFile(Context context, String fileName)
	{
		return context.deleteFile(fileName);
	}

	/**
	 * 文件是否存在
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static boolean exists(Context context, String fileName)
	{
		return new File(context.getFilesDir(), fileName).exists();
	}

	/**
	 * 存储文本数据
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名，要在系统内保持唯一
	 * @param content
	 *            文本内容
	 * @return boolean 存储成功的标志
	 */
	public static boolean writeFile(Context context, String fileName, String content)
	{
		boolean success = false;
		FileOutputStream fos = null;
		try
		{
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			byte[] byteContent = content.getBytes();
			fos.write(byteContent);

			success = true;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (fos != null) fos.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}

		return success;
	}

	/**
	 * 存储文本数据
	 * 
	 * @param filePath
	 *            文件路径，要在系统内保持唯一
	 * @param content
	 *            文本内容
	 * @return boolean 存储成功的标志
	 */
	public static boolean writeFile(String filePath, String content)
	{
		boolean success = false;
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(filePath);
			byte[] byteContent = content.getBytes();
			fos.write(byteContent);

			success = true;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (fos != null) fos.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}

		return success;
	}

	/**
	 * 读取文本数据
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名
	 * @return String, 读取到的文本内容，失败返回null
	 */
	public static String readFile(Context context, String fileName)
	{
		if (!exists(context, fileName)) { return null; }
		FileInputStream fis = null;
		String content = null;
		try
		{
			fis = context.openFileInput(fileName);
			if (fis != null)
			{

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true)
				{
					int readLength = fis.read(buffer);
					if (readLength == -1) break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				fis.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			content = null;
		}
		finally
		{
			try
			{
				if (fis != null) fis.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 读取文本数据
	 *
	 * @param filePath
	 *            文件路径
	 * @return String, 读取到的文本内容，失败返回null
	 */
	public static String readFile(String filePath)
	{
		if (filePath == null || !new File(filePath).exists()) { return null; }
		FileInputStream fis = null;
		String content = null;
		try
		{
			fis = new FileInputStream(filePath);
			if (fis != null)
			{

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true)
				{
					int readLength = fis.read(buffer);
					if (readLength == -1) break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				fis.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());

			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			content = null;
		}
		finally
		{
			try
			{
				if (fis != null) fis.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 读取文本数据
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名
	 * @return String, 读取到的文本内容，失败返回null
	 */
	public static String readAssets(Context context, String fileName)
	{
		InputStream is = null;
		String content = null;
		try
		{
			is = context.getAssets().open(fileName);
			if (is != null)
			{

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true)
				{
					int readLength = is.read(buffer);
					if (readLength == -1) break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				is.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			content = null;
		}
		finally
		{
			try
			{
				if (is != null) is.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 存储单个Parcelable对象
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名，要在系统内保持唯一
	 * @param parcelObject
	 *            对象必须实现Parcelable
	 * @return boolean 存储成功的标志
	 */
	public static boolean writeParcelable(Context context, String fileName, Parcelable parcelObject)
	{
		boolean success = false;
		FileOutputStream fos = null;
		try
		{
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			Parcel parcel = Parcel.obtain();
			parcel.writeParcelable(parcelObject, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
			byte[] data = parcel.marshall();
			fos.write(data);
			success = true;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}

		return success;
	}

	/**
	 * 存储List对象
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名，要在系统内保持唯一
	 * @param list
	 *            对象数组集合，对象必须实现Parcelable
	 * @return boolean 存储成功的标志
	 */
	public static boolean writeParcelableList(Context context, String fileName, List<Parcelable> list)
	{
		boolean success = false;
		FileOutputStream fos = null;
		try
		{
			if (list instanceof List)
			{
				fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				Parcel parcel = Parcel.obtain();
				parcel.writeList(list);
				byte[] data = parcel.marshall();
				fos.write(data);
				success = true;
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}

		return success;
	}

	/**
	 * 读取单个数据对象
	 * @param context  程序上下文
	 * @param fileName 文件名
	 * @return Parcelable, 读取到的Parcelable对象，失败返回null
	 */
	public static Parcelable readParcelable(Context context, String fileName, ClassLoader classLoader)
	{
		Parcelable parcelable = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try
		{
			fis = context.openFileInput(fileName);
			if (fis != null)
			{
				bos = new ByteArrayOutputStream();
				byte[] b = new byte[4096];
				int bytesRead;
				while ((bytesRead = fis.read(b)) != -1)
				{
					bos.write(b, 0, bytesRead);
				}
				byte[] data = bos.toByteArray();
				Parcel parcel = Parcel.obtain();
				parcel.unmarshall(data, 0, data.length);
				parcel.setDataPosition(0);
				parcelable = parcel.readParcelable(classLoader);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			parcelable = null;
		}
		finally
		{
			if (fis != null) try
			{
				fis.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (bos != null) try
			{
				bos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return parcelable;
	}

	/**
	 * 读取数据对象列表
	 * 
	 * @param context
	 *            程序上下文
	 * @param fileName
	 *            文件名
	 * @return List, 读取到的对象数组，失败返回null
	 */
	@SuppressWarnings("unchecked")
	public static List<Parcelable> readParcelableList(Context context, String fileName, ClassLoader classLoader)
	{
		List<Parcelable> results = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try
		{
			fis = context.openFileInput(fileName);
			if (fis != null)
			{
				bos = new ByteArrayOutputStream();
				byte[] b = new byte[4096];
				int bytesRead;
				while ((bytesRead = fis.read(b)) != -1)
				{
					bos.write(b, 0, bytesRead);
				}
				byte[] data = bos.toByteArray();
				Parcel parcel = Parcel.obtain();
				parcel.unmarshall(data, 0, data.length);
				parcel.setDataPosition(0);
				results = parcel.readArrayList(classLoader);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			results = null;
		}
		finally
		{
			if (fis != null) try
			{
				fis.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (bos != null) try
			{
				bos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return results;
	}

	public static boolean saveSerializable(Context context, String fileName, Serializable data)
	{
		boolean success = false;
		ObjectOutputStream oos = null;
		try
		{
			oos = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
			oos.writeObject(data);
			success = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (oos != null)
			{
				try
				{
					oos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	public static Serializable readSerialLizable(Context context, String fileName)
	{
		Serializable data = null;
		ObjectInputStream ois = null;
		try
		{
			ois = new ObjectInputStream(context.openFileInput(fileName));
			data = (Serializable) ois.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (ois != null)
			{
				try
				{
					ois.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	/**
	 * 从assets里边读取字符串
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getFromAssets(Context context, String fileName)
	{
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		String result = "";
		try
		{
			inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
			bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null){
				result += line;
			}
			bufReader.close();
			inputReader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
