package com.bangqu.yinwan.shop.helper;

import com.bangqu.yinwan.shop.internet.HttpClient;

public class BusinessHelper {

	/**
	 * 网络访问路径
	 */
	 protected static final String BASE_URL = "http://api.yinwan.bangqu.com/";

	/**
	 * 测试接口
	 */
//	protected static final String BASE_URL = "http://api2.yinwan.bangqu.com/";

	// protected static final String BASE_URL_TWO =
	// "http://192.168.1.147:8080/yinwan-api/";
	protected static final String BASE_URL_PRODUCT = "http://api.product.bangqu.com/";
	// protected static final String BASE_URL_PRODUCT_NEI =
	// "http://192.168.1.142:8081/shangpinku/api/";

	protected HttpClient httpClient = new HttpClient();

}
