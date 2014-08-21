package com.bangqu.yinwan.shop.helper;

/**
 * 分类相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class LocationHelper extends BusinessHelper {

	/**
	 * 小区个数
	 * 
	 * @param city
	 * @return
	 */
	public JSONObject distributioncount(String shopId) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/distribution/auditing.json",
				new PostParameter[] {
						new PostParameter("query.shopId", shopId),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 小区下拉框列表
	 * 
	 * @param city
	 * @return
	 * @throws SystemException
	 */
	public JSONObject SearchLocations(String shopId, String name)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/location/search.json",
				new PostParameter[] { new PostParameter("shopId", shopId),
						new PostParameter("query.name", name),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 全部小区列表
	 * 
	 * @param city
	 */
	public JSONObject AllLocations(String shopId, String cityName, int begin)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/location/search.json",
				new PostParameter[] { new PostParameter("shopId", shopId),
						new PostParameter("city", cityName),
						new PostParameter("query.begin", begin),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 周边小区列表
	 * 
	 * @param city
	 * @return
	 * @throws SystemException
	 */
	public JSONObject nearby(String cityName, String lat, String lng)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "location/search.json",
				new PostParameter[] { new PostParameter("cityName", cityName),
						new PostParameter("query.lat", lat),
						new PostParameter("query.lng", lng),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 申请加入小区
	 * 
	 * @param city
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ApplyJoin(String accessToken, String shopId,
			String locationId) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/distribution/apply.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("shopId", shopId),
						new PostParameter("locationId", locationId),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 查看已加入小区
	 * 
	 * @param city
	 * @return
	 * @throws SystemException
	 */
	public JSONObject SearcgJoin(String accessToken, String shopId,
			String pagesize, int state) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/distribution/location.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.pagesize", pagesize),
						new PostParameter("query.state", state),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

}
