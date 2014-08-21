package com.bangqu.yinwan.shop.bean;

/**
 *促销实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class PromotionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String addTime;
	private String startTime;

	private String endTime;
	private String price;
	private String unit;
	private String state;
	private ProductBean product;
	CategoryBean productCategory;

	public CategoryBean getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(CategoryBean productCategory) {
		this.productCategory = productCategory;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductBean getProduct() {
		return product;
	}

	public void setProduct(ProductBean product) {
		this.product = product;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 将多个PromotionBean封装近list里面
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<PromotionBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<PromotionBean> list = new ArrayList<PromotionBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(),
						PromotionBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public PromotionBean() {
		super();
	}
}
