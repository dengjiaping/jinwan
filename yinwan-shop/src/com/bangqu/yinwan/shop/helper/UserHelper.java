package com.bangqu.yinwan.shop.helper;

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

/**
 * 用户信息相关
 */
public class UserHelper extends BusinessHelper {
	/**
	 * 每日营业额
	 */
	public JSONObject turnover(String accessToken, String shopId, int begin,
			String order, boolean desc) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/turnover/search.json",
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
	 * 商户账户
	 */
	public JSONObject userzhanghu(String accessToken, String shopId)
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
	 * 用户登录
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject userlogin(String username, String password,
			String deviceToken) throws SystemException {
		return httpClient.post(
				BASE_URL + "user/login.json",
				new PostParameter[] {
						new PostParameter("user.username", username),
						new PostParameter("user.password", password),
						new PostParameter("user.deviceToken", deviceToken),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 用户退出
	 * 
	 * @return
	 */
	public JSONObject deleteToken(String accessToken) throws SystemException {
		return httpClient.post(
				BASE_URL + "user/deleteToken.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 用户注册
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject usersignup(String username, String password,
			String nickname, String type) throws SystemException {
		return httpClient.post(
				BASE_URL + "user/signup.json",
				new PostParameter[] {
						new PostParameter("user.username", username),
						new PostParameter("user.password", password),
						new PostParameter("user.nickname", nickname),
						new PostParameter("user.type", type),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 用户发出找回密码请求
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject getPwd(String username) throws SystemException {
		return httpClient.post(
				BASE_URL + "forget-password/user.json",
				new PostParameter[] { new PostParameter("username", username),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 用户修改密码
	 */
	public JSONObject resetpwd(String code, String password,
			String confirmPassword) throws SystemException {
		return httpClient.post(
				BASE_URL + "forget-password/password.json",
				new PostParameter[] { new PostParameter("code", code),
						new PostParameter("password", password),
						new PostParameter("confirmPassword", confirmPassword),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

}
