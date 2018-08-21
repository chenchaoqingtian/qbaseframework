/**  
* @Project: pda-common-for-eclipse
* @Title: IOStreamDealUtils.java
* @Package com.zteict.pda.common.utils
* @author chen chao
* @date 2016-4-25 下午4:03:58
* @Copyright: 2016 湖南中兴网信科技有限公司 Inc. All rights reserved.
* @version 1.1  
*/

package net.cc.qbaseframework.coreutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName IOStreamDealUtils
 * @Description IO流处理工具
 * @author chen chao
 * @date 2016-4-25
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
