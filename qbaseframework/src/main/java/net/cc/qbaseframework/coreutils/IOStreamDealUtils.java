package net.cc.qbaseframework.coreutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName IOStreamDealUtils
 * @Description IO流处理工具
 * @author chen chao
 */

public class IOStreamDealUtils
{
	/**
	 * 把输入流转成字节数组
	 */
	public static byte[] inputStreamToByteArray(InputStream is)
	{
		byte[] returnByte = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = 0;
		byte[] b = new byte[1024];
		try
		{
			while ((len = is.read(b, 0, b.length)) != -1)
			{
				baos.write(b, 0, len);
			}
			returnByte = baos.toByteArray();
			baos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return returnByte;
	}
}
