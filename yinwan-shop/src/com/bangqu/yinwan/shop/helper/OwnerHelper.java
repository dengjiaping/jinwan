package com.bangqu.yinwan.shop.helper;

/**
 * 业主相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class OwnerHelper extends BusinessHelper {

	/**
	 * 业主列表
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject OwnerList(String accessToken, String shopId, int begin,
			String order, String desc) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/owner/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.begin", begin),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 业主详细信息
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject OwnerView(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/owner/view.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 业主订单记录（未处理）
	 * 
	 */
	public JSONObject Ownerorder0(String id, String state, int begin)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/order/owner.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("query.state", state),
						new PostParameter("query.begin", begin),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 业主订单记录（配送中）
	 * 
	 */
	public JSONObject Ownerorder2(String id, String state, int begin)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/order/owner.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("query.state", state),
						new PostParameter("query.begin", begin),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 业主订单记录（已完成）
	 * 
	 */
	public JSONObject Ownerorder1(String id, String state, String shopId)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/order/owner.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("query.state", state),
						new PostParameter("query.shopId", shopId),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 业主订单记录（已取消）
	 * 
	 */
	public JSONObject Ownerorder11(String id, String state, String shopId)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/order/owner.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("query.state", state),
						new PostParameter("query.shopId", shopId),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}
}
