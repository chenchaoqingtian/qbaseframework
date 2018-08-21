package net.cc.qbaseframework.coreutils;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName DateTimeUtils
 * @Description 日期时间工具
 * @author chen chao
 * @date 2016-4-25
 */

public class DateTimeUtils
{
	private static String mCurrntDay;

	public static String addDay(String paramString, int paramInt)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		Date date2 = null;
		try
		{
			date1 = simpleDateFormat.parse(paramString);
			if (date1 == null)
			{
				return "";
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			calendar.add(Calendar.DAY_OF_YEAR, paramInt);
			date2 = calendar.getTime();
		}
		catch (ParseException parseException)
		{
			parseException.printStackTrace();

		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date2);
	}

	public static String addMonth(String paramString, int paramInt)
	{
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM");
		Date date1 = null;
		Date date2 = null;
		SimpleDateFormat simpleDateFormat2 = null;
		try
		{
			date1 = simpleDateFormat1.parse(paramString);
			if (date1 == null)
			{
				return "";
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			calendar.add(Calendar.MONTH, paramInt);
			date2 = calendar.getTime();
			simpleDateFormat2 = new SimpleDateFormat("yyyy-MM");
			mCurrntDay = simpleDateFormat2.format(new Date());
			if (Integer.parseInt(mCurrntDay.replace("-", "")) <= Integer.parseInt(simpleDateFormat2.format(date2).replace("-", "")))
			{
				return "-1";
			}
		}
		catch (ParseException parseException)
		{
			parseException.printStackTrace();

		}
		return simpleDateFormat2.format(date2);
	}

	public static String addMonth(String paramString, int paramInt, String format)
	{
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(format);
		Date date1 = null;
		Date date2 = null;
		SimpleDateFormat simpleDateFormat2 = null;
		try
		{
			date1 = simpleDateFormat1.parse(paramString);
			if (date1 == null)
			{
				return "";
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			calendar.add(Calendar.MONTH, paramInt);
			date2 = calendar.getTime();
			simpleDateFormat2 = new SimpleDateFormat(format);
			mCurrntDay = simpleDateFormat2.format(new Date());
			if (Integer.parseInt(mCurrntDay.replace("-", "")) <= Integer.parseInt(simpleDateFormat2.format(date2).replace("-", "")))
			{
				return "-1";
			}
		}
		catch (ParseException parseException)
		{
			parseException.printStackTrace();

		}
		return simpleDateFormat2.format(date2);
	}

	public static String addYear(String paramString, int paramInt)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		Date date1 = null;
		Date date2 = null;
		try
		{
			date1 = simpleDateFormat.parse(paramString);
			if (date1 == null)
			{
				return "";
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			calendar.add(Calendar.YEAR, paramInt);
			date2 = calendar.getTime();
			mCurrntDay = simpleDateFormat.format(new Date());
			if (Integer.parseInt(mCurrntDay.replace("-", "")) <= Integer.parseInt(simpleDateFormat.format(date2).replace("-", "")))
			{
				return "-1";
			}
		}
		catch (ParseException parseException)
		{
			parseException.printStackTrace();
		}
		return simpleDateFormat.format(date2);
	}

	public static String addYear(String paramString, int paramInt, String format)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date1 = null;
		Date date2 = null;
		try
		{
			date1 = simpleDateFormat.parse(paramString);
			if (date1 == null)
			{
				return "";
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date1);
			calendar.add(Calendar.YEAR, paramInt);
			date2 = calendar.getTime();
			mCurrntDay = simpleDateFormat.format(new Date());
			if (Integer.parseInt(mCurrntDay.replace("-", "")) <= Integer.parseInt(simpleDateFormat.format(date2).replace("-", "")))
			{
				return "-1";
			}
		}
		catch (ParseException parseException)
		{
			parseException.printStackTrace();
		}
		return simpleDateFormat.format(date2);
	}

	public static String format(String paramString)
	{
		Date date1;
		try
		{
			date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(paramString);
			if (date1 == null)
			{
				date1 = new Date();
			}
		}
		catch (ParseException parseException)
		{
			date1 = null;
		}
		return formatDate(date1);
	}

	public static String formatTime(String paramString)
	{
		Date date1;
		try
		{
			date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(paramString);
			if (date1 == null)
			{
				date1 = new Date();
			}
		}
		catch (ParseException parseException)
		{
			date1 = null;
		}
		return formatTime(date1);
	}

	public static String format(Timestamp paramTimestamp)
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(paramTimestamp);
	}

	public static long formatDate(String paramString)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		try
		{
			date1 = simpleDateFormat.parse(paramString);
			if (date1 == null)
			{
				date1 = new Date();
			}
		}
		catch (ParseException parseException)
		{
			parseException.printStackTrace();
		}
		return date1.getTime();
	}

	public static long formatDateTime(String paramString)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		try
		{
			date1 = simpleDateFormat.parse(paramString);
			if (date1 == null)
			{
				date1 = new Date();
			}
		}
		catch (ParseException parseException)
		{
			parseException.printStackTrace();
		}
		return date1.getTime();
	}

	public static String formatDate(Date paramDate)
	{
		return new SimpleDateFormat("yyyy-MM-dd").format(paramDate);
	}

	public static String formatTime(Date paramDate){
		return new SimpleDateFormat("HH:mm:ss").format(paramDate);
	}

	public static String formatDate(Date paramDate, String formatStr)
	{
		return new SimpleDateFormat(formatStr).format(paramDate);
	}

	public static String formatDate(int year, int month, int day)
	{
		int i = month + 1;
		String str1 = String.valueOf(i);
		String str2 = String.valueOf(day);
		if (i < 10)
		{
			str1 = "0" + i;
		}
		if (day < 10)
		{
			str2 = "0" + day;
		}
		return year + "-" + str1 + "-" + str2;
	}

	public static String formatDateTime(long paramLong)
	{
		return formatDateTime(new Date(paramLong));
	}

	public static String formatDateTime(Date paramDate)
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(paramDate);
	}

	public static String formatDateTime(int year, int month, int day, int hour, int min)
	{
		int i = month + 1;
		String str0 = String.valueOf(year);
		String str1 = String.valueOf(i);
		String str2 = String.valueOf(day);
		String str3 = String.valueOf(hour);
		String str4 = String.valueOf(min);
		if (i < 10)
		{
			str1 = "0" + i;
		}
		if (day < 10)
		{
			str2 = "0" + day;
		}
		if (hour < 10)
		{
			str3 = "0" + hour;
		}
		if (min < 10)
		{
			str4 = "0" + min;
		}
		return str0 + "-" + str1 + "-" + str2 + " " + str3 + ":" + str4 + ":" + "00";
	}

	public static String getCurrentDate()
	{
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static String getCurrentTime()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String getDateFormatForFile()
	{
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	public static String getDateTimeFormatForFile()
	{
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public static String getDateTimeFormatForFile(String paramString)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		try
		{
			date1 = simpleDateFormat.parse(paramString);
			if (date1 == null)
			{
				date1 = new Date();
			}
			simpleDateFormat.applyPattern("yyyyMMddHHmmss");
		}
		catch (ParseException parseException)
		{
			parseException.printStackTrace();
		}
		return simpleDateFormat.format(date1);
	}

	public static String formatTimer(int time)
	{
		int i = time / 3600;
		int j = time % 3600;
		int k = j % 60;
		int l = j / 60;
		String str1;
		String str2;
		String str3;
		if (i < 10)
		{
			str1 = "" + "0" + i + ":";
		}
		else
		{
			str1 = "" + i + ":";
		}

		if (l < 10)
		{
			str2 = str1 + "0" + l + ":";
		}
		else
		{
			str2 = str1 + l + ":";
		}

		if (k < 10)
		{
			str3 = str2 + "0" + k;
		}
		else
		{
		}

		if (k < 10)
		{
			str3 = str2 + "0" + k;
		}
		else
		{
			str3 = str2 + k;
		}
		return str3;
	}

	public static boolean between(String datetime, String fromDateTime, String toDateTime){
		long datetimeL = formatDateTime(datetime);
		long fromDateTimeL = formatDateTime(fromDateTime);
		long toDateTimeL = formatDateTime(toDateTime);
		if(datetimeL>=fromDateTimeL && datetimeL<toDateTimeL){
			return true;
		}
		return false;
	}
}
