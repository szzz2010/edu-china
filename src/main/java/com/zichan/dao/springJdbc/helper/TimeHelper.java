package com.zichan.dao.springJdbc.helper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.ObjectUtils;
public class TimeHelper extends DateUtils {

	private static SimpleDateFormat sdf;

	public final static String[] parsePatterns = {"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss", "yyyyMMdd","yyyy-MM-dd HH:mm","yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
			"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

	public static String getCurrentTime() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	public static String getCurrentDate() {
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static String getCurrentTimeMills() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(new Date());
	}

	public static String getCurrentTimeByFormat(String format) {
		try {
			sdf = new SimpleDateFormat(format);
		} catch (Exception e) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		return sdf.format(new Date());
	}
	

	public static String transTimeByFormat(Date dateTime,String fomatTo) {
		String return_time = "";
		try {
			sdf = new SimpleDateFormat(fomatTo);
			return_time = sdf.format(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return return_time;
	}
	

	public static String transTimeByFormat(String dateTime,String fomatTo) {
		String return_time = "";
		try {
			sdf = new SimpleDateFormat(TimeHelper.parsePatterns[1]);
			Date date = sdf.parse(dateTime);
			SimpleDateFormat sdf2 = new SimpleDateFormat(fomatTo);
			return_time = sdf2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return return_time;
	}
	
	public static String transTimeByFormat(String dateTime,String formatFrom,String fomatTo) {
		String return_time = "";
		try {
			sdf = new SimpleDateFormat(formatFrom);
			SimpleDateFormat sdf2 = new SimpleDateFormat(fomatTo);
			Date date = sdf.parse(dateTime);
			return_time = sdf2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return return_time;
	}
	
	
	/**
	 * 获取两个日期之间的年数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static float getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000f * 60 * 60 * 24 * 365);
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static float getDistanceOfTwoDateByDay(Date before, Date after) {
		int day = 0;
		try {
			long beforeTime = before.getTime();
			long afterTime = after.getTime();
			Float ff = (afterTime - beforeTime) / (1000f * 60 * 60 * 24);
			day = ff.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;
	}
	
	/**
	 * 获取两个日期之间的秒数
	 *
	 * @param before
	 * @param after
	 * @return
	 */
	public static int fistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (int)(afterTime - beforeTime) / 1000;
	}

	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取当天零点日期
	 *
	 * @return
	 */
	public static Date getCurrentDateZero() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		System.out.println(calendar.getTime());
		return calendar.getTime();
	}

	//获取时间到今天之间的年数
	public static String getYearsToToday(String dateStr, String format){
		Float years = 0f;
		SimpleDateFormat sdf = null ;
		if(!ObjectUtils.isEmpty(format)){
			sdf = new SimpleDateFormat(format);
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		Date date = null;
		Date today = new Date();
		try {
			date = sdf.parse(dateStr);
			years = TimeHelper.getDistanceOfTwoDate(date, today);	
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return String.valueOf(years);
	}

	/**
	 * 获取指定日期date前dates天的日期
	 *
	 * @param date
	 * @param dates
	 * @return
	 */
	public static Date getFrontDate(Date date, int dates) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - dates);
		Date endDate = null;
		try {
			endDate = calendar.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return endDate;
	}
}
