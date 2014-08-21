package com.bangqu.yinwan.shop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeConverter {
	static SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat otherDateFM = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat mdDateFM = new SimpleDateFormat("MM-dd");
	static SimpleDateFormat shortDateFM = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static String getOtherFormationTime(Date date) {
		return otherDateFM.format(date);
	}

	public static String getFormatTime(Date date) {
		// ��ʽ����ǰϵͳ����
		String dateTime = dateFm.format(date);
		return dateTime;
	}

	public static String getshortFormationTime(Date date) {
		return shortDateFM.format(date);
	}

	public static String getMdFormationTime(Date date) {
		return mdDateFM.format(date);
	}

	public static Date stringToDate(String str) {
		Date date = null;
		try {
			date = dateFm.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static Date otherStringToDate(String str) {
		Date date = null;
		try {
			date = otherDateFM.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	// ����ʱ���
	public static String getBeapartDate(String oldDateStr) {
		String returnTime = "";
		long nd = 1000 * 24 * 60 * 60;// һ��ĺ�����
		long nh = 1000 * 60 * 60;// һСʱ�ĺ�����
		long nm = 1000 * 60;// һ���ӵĺ�����
		long ns = 1000;// һ���ӵĺ�����
		long diff;// �����������ĺ�����
		try {
			Date oldDate = otherStringToDate(oldDateStr);
			Calendar oldCa = Calendar.getInstance();
			oldCa.setTime(oldDate);
			Date newDate = new Date();
			Calendar newCa = Calendar.getInstance();
			newCa.setTime(newDate);
			long oldDatem = oldDate.getTime();
			long newDatem = newDate.getTime();// ��ǰʱ��
			diff = newDatem - oldDatem;
			long day = diff / nd;
			long hour = diff / nh;
			long min = diff / nm;
			long sec = diff / ns;
			if (oldCa.get(Calendar.YEAR) != newCa.get(Calendar.YEAR)) {// ����һ���
				returnTime = getshortFormationTime(oldDate);
			} else {
				if (day < 7) {// ����һ������
					if (day < 1) {// һ����
						if (hour == 0) {
							if (min == 0) {
								returnTime = sec + "����ǰ";
							} else {
								returnTime = min + "����ǰ";
							}
						} else {
							returnTime = hour + "Сʱǰ";
						}
					} else {
						returnTime = day + "��ǰ";
					}
				} else {
					returnTime = getMdFormationTime(oldDate);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returnTime;
	}
}
