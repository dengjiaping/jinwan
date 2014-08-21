package com.bangqu.yinwan.shop.helper;

/**
 * 促销相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class PromotionHelper extends BusinessHelper {

	/**
	 * 已促销商品列表
	 * 
	 * @throws SystemException
	 */
	public JSONObject PromotionList(String accessToken, String shopId,
			String enabled, int begin, String order, String desc)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "promotion/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.enabled", enabled),
						new PostParameter("query.begin", begin),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 未促销商品列表
	 * 
	 * @throws SystemException
	 */
	public JSONObject UnPromotionList(String accessToken, String shopId,
			String enabled, String isPromotion, String order, String desc,
			int begin) throws SystemException {
		return httpClient.post(
				BASE_URL + "product/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.enabled", enabled),
						new PostParameter("query.isPromotion", isPromotion),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("query.begin", begin),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 添加促销商品
	 * 
	 * @throws SystemException
	 */
	public JSONObject PromotionAdd(String accessToken, String productId,
			String price, String unit) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/promotion/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("productId", productId),
						new PostParameter("promotion.price", price),
						new PostParameter("promotion.unit", unit),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 修改促销商品
	 * 
	 * @throws SystemException
	 */
	public JSONObject PromotionUpdate(String accessToken, String id,
			String addTime, String endTime, String price, String unit)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/promotion/update.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("promotion.addTime", addTime),
						new PostParameter("promotion.addTime", addTime),
						new PostParameter("promotion.endTime", endTime),
						new PostParameter("promotion.price", price),
						new PostParameter("promotion.unit", unit),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 删除促销商品
	 * 
	 * @throws SystemException
	 */
	public JSONObject PromotionDel(String accessToken, String id)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/promotion/delete.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 预览促销商品
	 * 
	 * @throws SystemException
	 */
	public JSONObject PromotionView(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "promotion/view.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}
}
