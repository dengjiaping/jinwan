package com.bangqu.yinwan.shop.helper;

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

/**
 * 账户
 * 
 */
public class ShopFinanceHelper extends BusinessHelper {
	/**
	 * 提现
	 * 
	 * @param accessToken
	 */
	public JSONObject WithdrawSave(String accessToken, String price,
			String shopId, String way) throws SystemException {
		return httpClient.post(
				BASE_URL + "withdraw/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("withdraw.price", price),
						new PostParameter("withdraw.shop.id", shopId),
						new PostParameter("withdraw.way", way),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 账户中心
	 * 
	 */
	public JSONObject financemine(String accessToken, String shopId)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/finance/mine.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 银行卡信息
	 * 
	 */
	public JSONObject bank(String accessToken) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/bank/mine.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 提现记录
	 * 
	 */
	public JSONObject billsearch(String accessToken, String shopId,
			String order, boolean desc, int begin) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/bill/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("query.begin", begin),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 添加银行卡
	 * 
	 */
	public JSONObject banksave(String accessToken, String bankIcon,
			String bankName, String cardNo, String accountName, String mobile)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/bank/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("bank.bankIcon", bankIcon),
						new PostParameter("bank.bankName", bankName),
						new PostParameter("bank.cardNo", cardNo),
						new PostParameter("bank.accountName", accountName),
						new PostParameter("bank.mobile", mobile),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 修改用户资料(修改支付宝)
	 */
	public JSONObject updatealipay(String accessToken, String realname,
			String alipay) throws SystemException {
		return httpClient.post(
				BASE_URL + "user/update.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("user.realname", realname),
						new PostParameter("user.alipay", alipay),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}
}
