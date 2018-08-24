package net.cc.qbaseframework.coreutils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 字符串工具
 * @author chen chao
 */
public class StringUtils
{
	public static boolean checkLength(String paramString, int min, int max)
	{
		int i = paramString.length();
		if (i >= min && i <= max)
		{
			return true;
		}
		return false;
	}

	public static boolean isBoolean(String paramString)
	{
		if (isEmpty(paramString))
		{
			return false;
		}
		try
		{
			Boolean.parseBoolean(paramString.trim());
			return true;
		}
		catch (Exception localException)
		{
			return false;
		}
	}

	public static boolean isBooleans(String[] paramArrayOfString)
	{
		for (int i = 0; i < paramArrayOfString.length; i++)
		{
			if (!isBoolean(paramArrayOfString[i]))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean isDouble(String paramString)
	{
		if (isEmpty(paramString))
		{
			return false;
		}
		try
		{
			Double.parseDouble(paramString.trim());
			return true;
		}
		catch (Exception localException)
		{
			return false;
		}
	}

	public static boolean isEmpty(Object paramObject)
	{
		if (paramObject == null || ((paramObject instanceof String) && (paramObject.toString().trim().length() == 0)))
		{
			return true;
		}
		return false;
	}

	public static boolean isEmptys(Object[] paramArrayOfObject)
	{
		for (int j = 0; j < paramArrayOfObject.length; j++)
		{
			if (!isEmpty(paramArrayOfObject[j]))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean isInteger(String paramString)
	{
		if (isEmpty(paramString))
		{
			return false;
		}
		try
		{
			Integer.parseInt(paramString.trim());
			return true;
		}
		catch (Exception localException)
		{
			return false;
		}
	}

	public static boolean isIntegers(String[] paramArrayOfString)
	{
		for (int i = 0; i < paramArrayOfString.length; i++)
		{
			if (!isInteger(paramArrayOfString[i]))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean isLong(String paramString)
	{
		if (isEmpty(paramString))
		{
			return false;
		}
		try
		{
			Long.parseLong(paramString.trim());
			return true;
		}
		catch (Exception localException)
		{
			return false;
		}
	}

	public static boolean isLongs(String[] paramArrayOfString)
	{
		for (int i = 0; i < paramArrayOfString.length; i++)
		{
			if (!isLong(paramArrayOfString[i]))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 字符串处理,如果字符串为null,则返回一个空字符串,否则返回原字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getLegalString(String str) {

		return str == null ? "" : str;
	}
	
	/**
	 * 将泛型为String类型的集合转化为逗号间隔字符串形式
	 * 
	 */
	public static String formatStringList(List<String> stringList) {
		if (stringList == null || stringList.size() <= 0) {
			return "";
		}
		return stringList.toString().replaceAll("^\\[| |\\]$", "");
	}

	/**
	 * 数字位补齐
	 * 
	 * @param number
	 *            数字
	 * @param count
	 *            位数
	 * @return 补齐后的数字字符串
	 */
	public static String numberToDigit(int number, int count) {
		String format = "";
		for (int i = 0; i < count; i++) {
			format += "0";
		}
		DecimalFormat df = new DecimalFormat(format);
		String numberString = df.format(number);
		return numberString;

	}

	/**
	 * 判断手机格式是否正确
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNumber(String mobiles) {
		Pattern p = Pattern.compile("1\\d{10}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	/**
	 * 判断email格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
}
