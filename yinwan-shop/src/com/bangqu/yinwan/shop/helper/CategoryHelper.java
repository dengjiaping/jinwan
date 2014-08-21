package com.bangqu.yinwan.shop.helper;

/**
 * 分类相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class CategoryHelper extends BusinessHelper {

	/**
	 * 商品分类列表
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ProductCategoryList(String shopId) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product-category/list.json",
				new PostParameter[] {
						new PostParameter("query.shopId", shopId),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 单个商品分类列表
	 * 
	 */
	public JSONObject CategoryViewList(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product-category/view.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 添加商品分类
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ProductCategoryAdd(String accessToken, String shopId,
			String name, String description) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product-category/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("shopId", shopId),
						new PostParameter("productCategory.name", name),
						new PostParameter("productCategory.description",
								description),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 修改分类
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ProductCategoryUpdate(String accessToken, String id,
			String name, String description) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product-category/update.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("productCategory.name", name),
						new PostParameter("productCategory.description",
								description),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 预览分类
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ProductCategoryView(String accessToken, String id)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product-category/view.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 删除商品分类
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ProductCategoryDel(String accessToken, String id)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product-category/delete.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 预览店铺分类
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */

	public JSONObject ShopCategoryView(String accessToken, String id)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/shop-category/view.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 添加店铺分类
	 * 
	 * @param accessToken
	 * @return
	 * @throws SystemException
	 */
	public JSONObject ShopCategoryAdd(String accessToken, String id,
			String categoryIds) throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/shop-category/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("categoryIds", categoryIds),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}
}
