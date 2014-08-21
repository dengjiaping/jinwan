package com.bangqu.yinwan.shop.helper;

/**
 * 团购相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class GrouponHelper extends BusinessHelper {

	/**
	 * 某个团购商品详情
	 * 
	 * @throws SystemException
	 */
	public JSONObject GrouponDeail(String id) throws SystemException {
		return httpClient.post(BASE_URL + "groupon/search.json?from=android-shop",
				new PostParameter[] { new PostParameter("id", id) }).asJSONObject();
	}

	/**
	 * 修改团购商品
	 * 
	 * @throws SystemException
	 */
	public JSONObject GrouponUpdate(String accessToken, String id, String addTime, String endTime,
			String price, String minimum) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/groupon/update.json?from=android-shop",
				new PostParameter[] { new PostParameter("accessToken", accessToken),
						new PostParameter("id", id), new PostParameter("groupon.addTime", addTime),
						new PostParameter("groupon.endTime", endTime),
						new PostParameter("groupon.price", price),
						new PostParameter("groupon.minimum", minimum) }).asJSONObject();
	}

	/**
	 * 删除团购商品
	 * 
	 * @throws SystemException
	 */
	public JSONObject GrouponDel(String accessToken, String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/groupon/delete.json?from=android-shop",
				new PostParameter[] { new PostParameter("accessToken", accessToken),
						new PostParameter("id", id) }).asJSONObject();
	}
}
