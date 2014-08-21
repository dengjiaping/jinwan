package com.bangqu.yinwan.shop.helper;

/**
 * 店铺相关
 */

import org.json.JSONObject;

import com.alibaba.fastjson.serializer.BooleanArraySerializer;
import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class ShopHelper extends BusinessHelper {

	/**
	 * 获取七牛时间命名
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject getTimeName() throws SystemException {
		return httpClient.post(BASE_URL + "qiniu/key.json").asJSONObject();
	}

	/**
	 * 获取七牛upToken
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject getToken() throws SystemException {
		return httpClient.post(BASE_URL + "qiniu/uptoken.json").asJSONObject();
	}

	/**
	 * 查看店铺
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ShopSearch(String accessToken) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/shop/mine.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 预览店铺
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ShopView(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/view.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 创建店铺
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject CreatShop(String accessToken, String name, String phone,
			String linkman, String address, String img, String lng, String lat,
			String sendPrice, String startDate, String endDate,
			String startTime, String endTime, String description,
			String payment, Boolean companyDelivery, Boolean deliver,
			String cityid, String districtid) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/shop/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("shop.name", name),
						new PostParameter("shop.phone", phone),
						new PostParameter("shop.linkman", linkman),
						new PostParameter("shop.address", address),
						new PostParameter("shop.img", img),
						new PostParameter("shop.lng", lng),
						new PostParameter("shop.lat", lat),
						new PostParameter("shop.sendPrice", sendPrice),
						new PostParameter("shop.startDate", startDate),
						new PostParameter("shop.endDate", endDate),
						new PostParameter("shop.startTime", startTime),
						new PostParameter("shop.endTime", endTime),
						new PostParameter("shop.description", description),
						new PostParameter("shop.payment", payment),
						new PostParameter("shop.companyDelivery",
								companyDelivery),
						new PostParameter("shop.deliver", deliver),
						new PostParameter("shop.city.id", cityid),
						new PostParameter("shop.district.id", districtid),

						new PostParameter("from", "android-shop") })
				.asJSONObject();

	}

	/**
	 * 修改店铺
	 */
	public JSONObject UpdateShop(String accessToken, String id, String img,
			String name, String phone, String linkman, String address,
			String lng, String lat, String startDate, String endDate,
			String startTime, String endTime, Boolean deliver,
			String sendPrice, Boolean companyDelivery, String description,
			Boolean groupon, Boolean promotion, Boolean vip, String payment,
			String cityid, String districtid) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/shop/update.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("shop.img", img),
						new PostParameter("shop.name", name),
						new PostParameter("shop.phone", phone),
						new PostParameter("shop.linkman", linkman),
						new PostParameter("shop.address", address),
						new PostParameter("shop.lng", lng),
						new PostParameter("shop.lat", lat),
						new PostParameter("shop.startDate", startDate),
						new PostParameter("shop.endDate", endDate),
						new PostParameter("shop.startTime", startTime),
						new PostParameter("shop.endTime", endTime),
						new PostParameter("shop.deliver", deliver),
						new PostParameter("shop.sendPrice", sendPrice),
						new PostParameter("shop.companyDelivery",
								companyDelivery),
						new PostParameter("shop.description", description),
						new PostParameter("shop.groupon", groupon),
						new PostParameter("shop.promotion", promotion),
						new PostParameter("shop.vip", vip),
						new PostParameter("shop.payment", payment),
						new PostParameter("shop.city.id", cityid),
						new PostParameter("shop.district.id", districtid),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

}
