/**
 * 
 */
package com.elastica.beatle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.Reporter;

/**
 * @author anuvrath
 *
 */
public class DateUtils {

	/**
	 * @return
	 */
	public static String getCurrentTime() {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String tsfrom = currentTime.toString(df);
		return tsfrom;
	}

	/**
	 * @return
	 */
	public static DateTime getCurrentDateAndTime(){		
		return DateTime.now(DateTimeZone.UTC);
	}

	/**
	 * @return
	 */
	public static String getCurrentDate() {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
		String tsfrom = currentTime.toString(df);
		return tsfrom;
	}

	/**
	 * @param minutes
	 * @return
	 */
	public static String getMinutesFromCurrentTime(int minutes) {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String tsto   = currentTime.plusMinutes(minutes).toString(df);
		return tsto;
	}

	/**
	 * @param days
	 * @return
	 */
	public static String getDaysFromCurrentTime(int days) {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String tsto   = currentTime.plusDays(days).toString(df);
		return tsto;
	}

	/**
	 * @param days
	 * @return
	 */
	public static String getDateFromCurrentTime(int days) {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
		String tsto   = currentTime.plusDays(days).toString(df);
		return tsto;
	}

	/**
	 * @param numberofMonths
	 * @return
	 */
	public static String getPreviousDayDateFromCurrentTime(int numberOfDays){
		return DateTime.now(DateTimeZone.UTC).minusDays(numberOfDays).toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));	
	}

	/**
	 * @param numberofMonths
	 * @return
	 */
	public static String getPreviousMonthDateFromCurrentTime(int numberofMonths){
		return DateTime.now(DateTimeZone.UTC).minusMonths(numberofMonths).toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));	
	}

	/**
	 * @param numberofMinutes
	 * @return
	 */
	public static String getPreviousMinuteDateFromCurrentTime(int numberofMinutes){
		return DateTime.now(DateTimeZone.UTC).minusMinutes(numberofMinutes).toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));	
	}

	/**
	 * @param numberofHours
	 * @return
	 */
	public static String getPreviousHoursDateFromCurrentTime(int numberofHours){
		return DateTime.now(DateTimeZone.UTC).minusHours(numberofHours).toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}


	public static String getDateTime(String pattern) {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern(pattern);
		String tsfrom = currentTime.toString(df);
		return tsfrom;
	}

	public static double timeDiff(String ts1, String ts2) {
		DateTime dt1 = new DateTime(ts1);
		DateTime dt2 = new DateTime(ts2);
		long diff = Math.abs(dt1.getMillis() - dt2.getMillis());
		return diff / 60000.0 ;
	}
	public static String getCurrentDate(String sdf) throws Exception{
		DateFormat dateFormat = new SimpleDateFormat(sdf, Locale.ENGLISH);
		Date date=new Date();
		String curDate=dateFormat.format(date);
		return curDate;
	}
	
	public static String getCurrentTimeWithoutT() {
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
		String tsfrom = currentTime.toString(df);
		return tsfrom;
	}

}
