package com.bangqu.yinwan.shop.helper;

/**
 * 订单相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class OrderHelper extends BusinessHelper {

	/**
	 * 各类订单基本信息（条数）
	 * 
	 * @throws SystemException
	 */
	public JSONObject OrderInfo(String accessToken, String shopId,
			String clickdate) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/order/remind.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("clickdate", clickdate),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 待配送订单列表
	 * 
	 * @throws SystemException
	 */

	public JSONObject OrderComments(String accessToken, String shopId,
			String states, int begin, String order, String desc)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/order/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.states", states),
						new PostParameter("query.begin", begin),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 未处理订单详情页
	 * 
	 * @throws SystemException
	 */
	public JSONObject OrderDetail(String accessToken, String id)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "order/view.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 取消订单
	 * 
	 * @throws SystemException
	 */
	public JSONObject OrderCancel(String accessToken, String id, String content)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/order/cancel.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("content", content),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 未处理订单改为配送中订单
	 * 
	 * @throws SystemException
	 */
	public JSONObject OrderDeal(String accessToken, String id)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/order/send.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 订单状态列表
	 * 
	 * @param level
	 * @param parentId
	 * @return
	 * @throws SystemException
	 */
	public JSONObject search(String accessToken, String state)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "order/comment.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.state", state),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 物流详情
	 * 
	 * @throws SystemException
	 */
	public JSONObject SendDetail(String accessToken, String orderId)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "step/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("orderId", orderId),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 订单取消理由
	 * 
	 * @param accessToken
	 * @param orderId
	 * @param state
	 * @return
	 * @throws SystemException
	 */
	public JSONObject stepreason(String accessToken, String orderId,
			String state) throws SystemException {
		return httpClient.post(
				BASE_URL + "step/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.orderId", orderId),
						new PostParameter("query.state", state),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

}
