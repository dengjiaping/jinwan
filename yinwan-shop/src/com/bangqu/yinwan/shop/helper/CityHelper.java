package com.bangqu.yinwan.shop.helper;

/**
 * 商品相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class CityHelper extends BusinessHelper {

	/**
	 * 区县
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject district(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "district/list.json",
				new PostParameter[] {
						new PostParameter("query.cityId", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 全部城市列表
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject city1(String state, String name, String version)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "city/list.json",
				new PostParameter[] { new PostParameter("query.state", state),
						new PostParameter("query.name", name),
						new PostParameter("query.version", version),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 热门城市列表
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject hotcity(String order, String desc) throws SystemException {
		return httpClient.post(
				BASE_URL + "city/hot.json",
				new PostParameter[] { new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 所有城市中文检索列表
	 * 
	 * @param name
	 * @return
	 * @throws SystemException
	 */
	public JSONObject cityAll(String state, String name) throws SystemException {
		return httpClient.post(
				BASE_URL + "city/list.json",
				new PostParameter[] { new PostParameter("query.state", state),
						new PostParameter("query.name", name),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

}
