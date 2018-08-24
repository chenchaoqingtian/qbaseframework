package net.cc.qbaseframework.coreutils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * @Description SD卡管理工具
 * @author chen chao
 */

public class SdCardUtils
{
	/**
	 * SD卡是否挂载
	 */
	public static boolean exist()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
	}

	/**
	 * sd卡是否有足够的空间，单位字节
	 */
	public static boolean hasEnoughSpace(long size)
	{
		long freeSize = getSDFreeSize();

		return freeSize >= size + 4 * 1024 * 1024 ? true : false;
	}

	/**
	 * 获取SD卡的剩余空间， 单位字节
	 */
	public static long getSDFreeSize()
	{
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		return freeBlocks * blockSize; // 单位Byte
	}

	/**
	 * 获取SD卡的总容量，单位字节
	 */
	public static long getSDAllSize()
	{
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		return allBlocks * blockSize; // 单位Byte
	}
}
