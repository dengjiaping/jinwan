package com.bangqu.yinwan.shop.helper;

/**
 * 商品相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class ProductHelper extends BusinessHelper {
	/**
	 * 将商品添加到促销列表里
	 */
	public JSONObject addPromotion(String accessToken, String productId,
			String price, String startTime, String endTime)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/promotion/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("productId", productId),
						new PostParameter("promotion.price", price),
						new PostParameter("promotion.startTime", startTime),
						new PostParameter("promotion.endTime", endTime),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 修改促销列表
	 * 
	 * @param accessToken
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param price
	 * @param unit
	 * @return
	 */
	public JSONObject xiuPromotion(String accessToken, String id,
			String startTime, String endTime, String price)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/promotion/update.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("promotion.startTime", startTime),
						new PostParameter("promotion.endTime", endTime),
						new PostParameter("promotion.price", price),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 将商品添加到团购列表里
	 * 
	 */
	public JSONObject addgroupon(String accessToken, String productId,
			String startTime, String endTime, String price, String minimum)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/groupon/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("productId", productId),
						new PostParameter("groupon.startTime", startTime),
						new PostParameter("groupon.endTime", endTime),
						new PostParameter("groupon.price", price),
						new PostParameter("groupon.minimum", minimum),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 修改团购商品
	 * 
	 */

	public JSONObject grouponUpdate(String accessToken, String id, String name,
			String price, String startTime, String endTime, String minimum)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/groupon/update.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("groupon.name", name),
						new PostParameter("groupon.price", price),
						new PostParameter("groupon.startTime", startTime),
						new PostParameter("groupon.endTime", endTime),
						new PostParameter("groupon.minimum", minimum),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 商铺中添加商品
	 * 
	 */
	public JSONObject addProduct(String accessToken, String name, String price,
			String vipPrice, String unit, String img, String productUrls,
			String shopid, String isPromotion, String isGroupon,
			String Categoryid, String content, String type)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product/save.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("product.name", name),
						new PostParameter("product.price", price),
						new PostParameter("product.vipPrice", vipPrice),
						new PostParameter("product.unit", unit),
						new PostParameter("product.img", img),
						new PostParameter("productUrls", productUrls),
						new PostParameter("product.shop.id", shopid),
						new PostParameter("product.isPromotion", isPromotion),
						new PostParameter("product.isGroupon", isGroupon),
						new PostParameter("product.productCategory.id",
								Categoryid),
						new PostParameter("product.content", content),
						new PostParameter("product.type", type),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 商品、服务列表
	 */
	public JSONObject productList(String shopId, int begin, String type,
			String CategoryId, String order, String desc)
			throws SystemException {
		return httpClient
				.post(BASE_URL + "product/search.json",
						new PostParameter[] {
								new PostParameter("query.shopId", shopId),
								new PostParameter("query.begin", begin),
								new PostParameter("query.type", type),
								new PostParameter("query.productCategoryId",
										CategoryId),
								new PostParameter("query.order", order),
								new PostParameter("query.desc", desc),
								new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 查看商品信息
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject productView(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "product/view.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 查看某促销商品信息
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject promotionView(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "promotion/view.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 查看商品图片
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject productImgView(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "product-img/search.json",
				new PostParameter[] { new PostParameter("query.productId", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 修改商品图片
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject productImgUpdate(String id, String productUrls)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product-img/update.json",
				new PostParameter[] { new PostParameter("query.productId", id),
						new PostParameter("productUrls", productUrls),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 修改商品信息
	 */
	public JSONObject productUpdate(String accessToken, String id,
			String productname, String productCategoryId, String productprice,
			String productunit, String productcontent, String vipPrice)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product/update.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("product.name", productname),
						new PostParameter("product.productCategory.id",
								productCategoryId),
						new PostParameter("product.price", productprice),
						new PostParameter("product.unit", productunit),
						new PostParameter("product.content", productcontent),
						new PostParameter("product.vipPrice", vipPrice),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 删除团购商品
	 * 
	 */
	public JSONObject grouponDelete(String accessToken, String id)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/groupon/delete.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 删除促销商品
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject promotionDelete(String accessToken, String id)
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
	 * 删除商品
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject productDelete(String accessToken, String id)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "shop/product/delete.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 查看商品销量排行
	 * 
	 * @param grade
	 * @return
	 * @throws SystemException
	 */
	public JSONObject productSales(String accessToken, String shopId,
			String enabled, String order, Boolean desc, int begin)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "product/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.enabled", enabled),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("query.begin", begin),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 单个团购商品详情
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject GrouponView(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "groupon/view.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 单个团购商品详情
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public JSONObject TuanGouProductView(String id) throws SystemException {
		return httpClient.post(
				BASE_URL + "groupon/view.json",
				new PostParameter[] { new PostParameter("id", id),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 团购商品列表
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject GrouponList(String shopId, int begin, String order,
			String desc) throws SystemException {
		return httpClient.post(
				BASE_URL + "groupon/search.json",
				new PostParameter[] {
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.begin", begin),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 未团购商品列表
	 * 
	 * @return
	 * @throws SystemException
	 */
	public JSONObject UnGrouponList(String accessToken, String shopId,
			String enabled, String isGroupon, int begin, String order,
			String desc) throws SystemException {
		return httpClient.post(
				BASE_URL + "product/search.json",
				new PostParameter[] {
						new PostParameter("accessToken", accessToken),
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.enabled", enabled),
						new PostParameter("query.isGroupon", isGroupon),
						new PostParameter("query.begin", begin),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

	/**
	 * 商品搜索列表
	 */
	public JSONObject productSearch(String nameOrNumber, String name,
			String number) throws SystemException {
		return httpClient.post(
				BASE_URL_PRODUCT + "product/search.json",
				new PostParameter[] {
						new PostParameter("query.nameOrNumber", nameOrNumber),
						new PostParameter("query.name", name),
						new PostParameter("query.number", number) })
				.asJSONObject();
	}

	/**
	 * 评价列表
	 */
	public JSONObject comment(int shopId, String order, boolean desc, int begin)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "product-comment/search.json",
				new PostParameter[] {
						new PostParameter("query.shopId", shopId),
						new PostParameter("query.order", order),
						new PostParameter("query.desc", desc),
						new PostParameter("query.begin", begin) })
				.asJSONObject();
	}
}
