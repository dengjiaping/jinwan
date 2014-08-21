package com.bangqu.yinwan.shop.util;

import org.json.JSONObject;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bangqu.yinwan.shop.internet.HttpClient;
import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

/**
 * @author Aizhimin 远程接口访问帮助类
 */
public class StarappHelper {
	private static final String BASE_URL = "http://222.73.182.110:8080";
	HttpClient httpClient = new HttpClient();

	public JSONObject postLocationToServer(String shopCode, GeoPoint srcPoint,
			GeoPoint rectifyPoint, String userID) throws SystemException {
		PostParameter[] parametersArr = new PostParameter[] {
				new PostParameter("shopcode", shopCode),
				new PostParameter("lat", (srcPoint.getLatitudeE6() / 1e6) + ""),
				new PostParameter("lng", (srcPoint.getLongitudeE6() / 1e6) + ""),
				new PostParameter("rectifylat",
						(rectifyPoint.getLatitudeE6() / 1e6) + ""),
				new PostParameter("rectifylng",
						(rectifyPoint.getLongitudeE6() / 1e6) + ""),
				new PostParameter("uid", userID) };
		JSONObject jsonObject = httpClient.post(
				BASE_URL + "/CollectShop/shop/save/", parametersArr)
				.asJSONObject();
		return jsonObject;
	}

	public JSONObject login(String loginName, String passwd)
			throws SystemException {
		PostParameter[] parametersArr = new PostParameter[] {
				new PostParameter("loginName", loginName),
				new PostParameter("password", passwd), };
		JSONObject jsonObject = httpClient.post(
				BASE_URL + "/CollectShop/loginJson", parametersArr)
				.asJSONObject();
		return jsonObject;
	}
}
