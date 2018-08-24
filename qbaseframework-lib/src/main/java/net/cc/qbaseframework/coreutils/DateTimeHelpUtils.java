package net.cc.qbaseframework.coreutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @ClassName DateTimeHelpUtils
 * @Description 日期时间相关帮助工具
 * @author chen chao
 */

public class DateTimeHelpUtils
{
	/** 
	 * 计算指定年度共有多少个周。 
	 */  
	public static int getWeekNumByYear(int year){    
		int result = 52;//每年至少有52个周 ，最多有53个周。   
		String date = getYearWeekFirstDay(year,53);  
		if(date.substring(0, 4).equals(year+"")){ //判断年度是否相符，如果相符说明有53个周。   
			result = 53;  
		}  
		return result;  
	}  

	/** 
	 * 计算某年某周的开始日期 
	 */  
	public static String getYearWeekFirstDay(int year,int week)  {  
		Calendar cal = Calendar.getInstance();  
		cal.setFirstDayOfWeek(Calendar.SUNDAY); //设置每周的第一天为星期一   
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//每周从周天开始   
		cal.setMinimalDaysInFirstWeek(7);  //设置每周最少为7天        
		cal.set(Calendar.YEAR, year);  
		cal.set(Calendar.WEEK_OF_YEAR, week-1);  
		//分别取得当前日期的年、月、日   
		return DateTimeUtils.formatDate(cal.getTime());
	}  

	/** 
	 * 计算某年某周的结束日期 
	 */  
	public static String getYearWeekEndDay(int year,int week)  {         
		Calendar cal = Calendar.getInstance();  
		cal.setFirstDayOfWeek(Calendar.SUNDAY); //设置每周的第一天为星期天   
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);//每周在周六结束
		//       上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。          
		cal.setMinimalDaysInFirstWeek(7);  //设置每周最少为7天            
		cal.set(Calendar.YEAR, year);  
		cal.set(Calendar.WEEK_OF_YEAR, week-1);  
		return DateTimeUtils.formatDate(cal.getTime());
	}  

	/**
	 * 计算某年某月的开始日期
	 */
	public static String getYearMonthFirstDay(int year,int month,int day){
		Calendar cal = Calendar.getInstance();  
		cal.set(year, month, day);
		return DateTimeUtils.formatDate(cal.getTime());
	}

	/**
	 * 计算某年某月的结束日期
	 */
	public static String getYearMonthEndDay(int year,int month){
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
		cal.set(Calendar.DAY_OF_MONTH,day);
		return DateTimeUtils.formatDate(cal.getTime());
	}

	/**
	 * 计算某年某季度的开始日期
	 */
	public static String getYearQuarterFirstDay(int year,int quarter){
		if(quarter>4){
			return "";
		}
		if(quarter==1){
			return year+"-01-01";
		}
		if(quarter==2){
			return year+"-04-01";
		}
		if(quarter==3){
			return year+"-07-01";
		}
		if(quarter==4){
			return year+"-10-01";
		}
		return "";
	}

	/**
	 * 计算某年某季度的结束日期
	 */
	public static String getYearQuaterEndDay(int year,int quarter){
		if(quarter>4){
			return "";
		}
		if(quarter==1){
			return year+"-03-31";
		}
		if(quarter==2){
			return year+"-06-30";
		}
		if(quarter==3){
			return year+"-09-30";
		}
		if(quarter==4){
			return year+"-12-31";
		}
		return "";
	}

	/**
	 * 计算某年某半年的开始日期
	 */
	public static String getYearHalfFirstDay(int year,int half){
		if(half>2){
			return "";
		}
		if(half==1){
			return year+"-01-01";
		}
		if(half==2){
			return year+"-07-01";
		}
		return "";
	}

	/**
	 * 计算某年某半年的结束日期
	 */
	public static String getYearHalfEndDay(int year,int half){
		if(half>2){
			return "";
		}
		if(half==1){
			return year+"-06-30";
		}
		if(half==2){
			return year+"-12-31";
		}
		return "";
	}

	/**
	 * 计算某某年的开始日期
	 */
	public static String getYearFirstDay(int year){
		return year+"-01-01";
	}

	/**
	 * 计算某某年的结束日期
	 */
	public static String getYearEndDay(int year){
		return year+"-12-31";
	}

	/**
	 * 根据时间长度（秒为单位）得到格式化的“分:秒”
	 * @param timeLength
	 * @return
	 */
	public static String getTimeBySecondLength(int timeLength) {
		int minute = timeLength / 60;

		if (minute >= 60) {

			minute = minute % 60;
		}
		int second = timeLength % 60;

		return String.format(Locale.CHINA, "%02d:%02d", minute, second);
	}

	/**
	 * 计算年龄
	 * @param brithDay
	 * @return
	 */
	public static String getAge(String brithDay) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
		Date date;
		try {
			date = dateFormat.parse(brithDay);

			int age = 0;

			Calendar currentTime = Calendar.getInstance();
			Calendar brithTime = Calendar.getInstance();
			brithTime.setTime(date);
			if (currentTime.get(Calendar.MONTH) > brithTime.get(Calendar.MONTH)) {
				age = currentTime.get(Calendar.YEAR) - brithTime.get(Calendar.YEAR);
			} else if (currentTime.get(Calendar.MONTH) == brithTime.get(Calendar.MONTH)) {
				if (currentTime.get(Calendar.DAY_OF_MONTH) > brithTime.get(Calendar.DAY_OF_MONTH)) {
					age = currentTime.get(Calendar.YEAR) - brithTime.get(Calendar.YEAR);
				} else {
					age = currentTime.get(Calendar.YEAR) - brithTime.get(Calendar.YEAR) - 1;
				}
			} else {
				age = currentTime.get(Calendar.YEAR) - brithTime.get(Calendar.YEAR) - 1;
			}

			return String.valueOf(age);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
