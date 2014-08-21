package com.bangqu.yinwan.shop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	/**
	 * 根据日期计算这个月的第一天是几号
	 * 
	 * @param queryDate
	 * @return String
	 * @throws ParseException
	 */
	public static String calcBeginMonth(String queryDate) throws ParseException {
		String date;
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		gc.setTime(df.parse(queryDate));
		int i = gc.get(Calendar.MONTH);
		// 11表示第12月
		while (gc.get(Calendar.MONTH) != (i == 0 ? 11 : i - 1)) {
			gc.add(5, -1);
		}
		gc.add(5, 1);
		date = df.format(gc.getTime()) + " 00:00:00";
		return date;
	}

	/**
	 * 根据日期计算这个月的最后一天是几号
	 * 
	 * @param queryDate
	 * @return String
	 * @throws ParseException
	 */
	public static String calcEndMonth(String queryDate) throws ParseException {
		String date;
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		gc.setTime(df.parse(queryDate));
		int i = gc.get(Calendar.MONTH);
		// 11表示第12月
		while (gc.get(Calendar.MONTH) != (i == 11 ? 0 : i + 1)) {
			gc.add(5, 1);
		}
		gc.add(5, -1);
		date = df.format(gc.getTime()) + " 00:00:00";
		return date;
	}

	public static String weekRange() {
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
		gc.setTime(new Date());
		gc.add(5, -1);
		while (gc.get(7) != 1) {
			gc.add(5, -1);
		}
		gc.add(5, 1);

		GregorianCalendar gc2 = new GregorianCalendar();
		SimpleDateFormat df2 = new SimpleDateFormat("dd日");
		gc2.add(5, -1);
		while (gc2.get(7) != 7) {
			gc2.add(5, 1);
		}
		gc2.add(5, 1);

		return df.format(gc.getTime()) + "~" + df2.format(gc2.getTime());
	}

	public static void main(String[] args) {
		System.out.println(weekRange());
	}

}
