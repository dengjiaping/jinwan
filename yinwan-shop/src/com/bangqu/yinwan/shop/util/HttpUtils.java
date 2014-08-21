package com.bangqu.yinwan.shop.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.Date;

import com.bangqu.yinwan.shop.util.NetworkControl.NetType;

import android.content.Context;

public class HttpUtils {

	// public final static String IP = "apk.21253.com";
	public final static String IP = "192.168.1.162";
	public final static String port = "8089";

	public final static String downloadURL = "http://www.21253.com/D/SuShang.apk";
	public final static String URL = "http://" + IP + ":" + port + "/SuShang_Android_Server/";

	private static String staticStr = "SuShangInformationInfo";
	private static String dynamicStr = "";

	public static HttpURLConnection getUC(Context context, String urlString) {
		URL url;
		HttpURLConnection uc = null;
		dynamicStr = TimeConverter.getFormatTime(new Date());
		String combinationStr = staticStr + dynamicStr;
		try {
			url = new URL(URL + urlString);
			NetType netType = NetworkControl.getNetType(context);
			if (netType != null && netType.isWap()) {
				Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(netType.getProxy(),
						netType.getPort()));
				uc = (HttpURLConnection) url.openConnection(proxy);
			} else {
				uc = (HttpURLConnection) url.openConnection();
			}
			// 添加头信息
			uc.addRequestProperty("combinationStr", MD5Encrypt.EncodingMD5(combinationStr));
			uc.addRequestProperty("dynamicStr", dynamicStr);
			uc.setConnectTimeout(20000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uc;
	}

	/** 关闭InputStreame */
	public static void close(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 关闭OutputStream */
	public static void close(OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
