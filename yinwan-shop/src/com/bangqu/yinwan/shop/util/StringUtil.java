package com.bangqu.yinwan.shop.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.format.Time;

/**
 * 字符串工具类
 * 
 * @author Aizhimin 说明：处理一下字符串的常用操作，字符串校验等
 */
public class StringUtil {
	/**
	 * 判断字符串是否为空或者空字符串 如果字符串是空或空字符串则返回true，否则返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str) || "null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证邮箱输入是否合法
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		// String strPattern =
		// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/**
	 * 验证是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * MD5加密
	 * 
	 * @param secret_key
	 * @return
	 */
	public static String createSign(String secret_key) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(secret_key.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 字符全角化
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 判断是否是中文
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否超过指定字符数
	 * 
	 * @param content
	 * @param stringNum
	 *            指定字符数 如：140
	 * @return
	 */
	public static boolean countStringLength(String content, int stringNum) {
		int result = 0;
		if (content != null && !"".equals(content)) {
			char[] contentArr = content.toCharArray();
			if (contentArr != null) {
				for (int i = 0; i < contentArr.length; i++) {
					char c = contentArr[i];
					if (isChinese(c)) {
						result += 3;
					} else {
						result += 1;
					}
				}
			}
		}
		if (result > stringNum * 3) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将网络图片路径md5加密作为文件名
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static String createImageName(String imageUrl) {
		return createSign(imageUrl) + ".jpg";
	}

	/**
	 * 将网络图片路径md5加密作为文件名,可以设置图片类型
	 * 
	 * @param imageUrl
	 * @param imgSuffix
	 * @return
	 */
	public static String createImageName(String imageUrl, String imgSuffix) {
		return createSign(imageUrl) + imgSuffix;
	}

	/**
	 * 将null转换为""
	 * 
	 * @param str
	 * @return
	 */
	public static String trimNull(String str) {
		if (str == null || "null".equalsIgnoreCase(str))
			return "";
		else
			return str;
	}

	/**
	 * 把异常信息打印出来
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionInfo(Exception e) {
		String result = "";
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		result = e.getMessage() + "/r/n" + sw.toString();
		pw.close();
		try {
			sw.close();
		} catch (IOException e1) {

		}
		return result;
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 头像文件名
	 * 
	 * @param sid
	 * @return
	 */
	public static String createAvatarFileName(String sid) {
		return "avatar_" + sid + ".jpg";
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return
	 */
	public static String getTime() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。

		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;

		return hour + ":" + minute + ":" + second;
	}

	/**
	 * 获取系统当前小时
	 * 
	 * @return
	 */
	public static int getHour() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		return t.hour; // 0-23
	}

	/**
	 * 小于十数字补零
	 * 
	 * @param i
	 * @return
	 */
	public static String addzero(int i) {
		// TODO Auto-generated method stub
		String f;
		if (i < 10) {
			f = "0" + i;
		} else {
			f = i + "";
		}
		return f;
	}

	/**
	 * 保留小数点后两位数字
	 * 
	 * @return
	 */
	public static String getDecimalFormat(String str) {
		DecimalFormat df = new DecimalFormat("#.00");
		String douStr = df.format(str);
		return douStr;
	}

	/**
	 * 名字处理类
	 * 
	 * @author yangyao
	 * @version 1.0
	 */
	public static class NameUtil {

		/**
		 * 获取姓氏
		 * 
		 * @param name
		 *            需要取出姓氏的名字
		 * @return String类型的匹配姓氏（例：'王刚',返回'王'）
		 */
		public static String getFamiliyName(String name) {
			for (String str : getFamilyNames()) {
				if (name.substring(0, 2).equals(str))
					return name.substring(0, 2).toString();
			}
			return name.substring(0, 1).toString();
		}

		/**
		 * 获取尊称
		 * 
		 * @param name
		 *            需要获得尊称的名字
		 * @param male
		 *            需要获得尊称的性别
		 *            支持Boolean(true男，false女),Integer(0男,1女),String(男,女)
		 * @return String类型的匹配尊称（例：'王刚',返回'王先生';'王美丽',返回'王小姐'）
		 */
		public static String getPrefixName(String name, Object male) {
			String prefixName = null;
			for (String str : getFamilyNames()) {
				if (name.substring(0, 2).equals(str)) {
					prefixName = name.substring(0, 2);
					break;
				}
			}
			if (prefixName == null)
				prefixName = name.substring(0, 1);
			return prefixName += (male instanceof Boolean ? (Boolean) male ? "男"
					: "女"
					: male instanceof Integer ? (Integer) male == 0 ? "男" : "女"
							: male instanceof String ? (String) male : null)
					.equals("男") ? "先生" : "女士";
		}

		/**
		 * 获取复姓姓氏集合
		 * 
		 * @return 返回List<String> 对象
		 */
		private static List<String> getFamilyNames() {
			List<String> familyNames = new ArrayList<String>();
			String names = "欧阳、太史、端木、上官、司马、东方、独孤、南宫、万俟、闻人、夏侯、"
					+ "诸葛、尉迟、公羊、赫连、澹台、皇甫、宗政、濮阳、公冶、太叔、申屠、公孙、慕容、"
					+ "仲孙、钟离、长孙、宇文、司徒、鲜于、司空、闾丘、子车、亓官、司寇、巫马、公西、颛孙、壤驷、"
					+ "公良、漆雕、乐正、宰父、谷梁、拓跋、夹谷、轩辕、令狐、段干、百里、呼延、东郭、南门、羊舌、"
					+ "微生、公户、公玉、公仪、梁丘、公仲、公上、公门、公山、公坚、左丘、公伯、西门、公祖、第五、"
					+ "公乘、贯丘、公皙、南荣、东里、东宫、仲长、子书、子桑、即墨、达奚、褚师";
			String[] list = names.split("、");
			for (int i = 0; i < list.length; i++) {
				familyNames.add(list[i]);
			}
			return familyNames;
		}
	}

}
