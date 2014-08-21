package com.bangqu.yinwan.shop.bean;

/**
 * 单件商品实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class ProductBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String addTime;
	private String updateTime;
	private String type;
	private String name;
	private String number;
	private String price;
	private String marketPrice;
	private String vipPrice;
	private String monthSales;
	private String unit;
	private String img;
	private String content;
	private String count;
	private String state;
	private String isPromotion;
	private String isGroupon;
	private String enabled;
	CategoryBean productCategory;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(String vipPrice) {
		this.vipPrice = vipPrice;
	}

	public String getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(String monthSales) {
		this.monthSales = monthSales;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIsPromotion() {
		return isPromotion;
	}

	public void setIsPromotion(String isPromotion) {
		this.isPromotion = isPromotion;
	}

	public String getIsGroupon() {
		return isGroupon;
	}

	public void setIsGroupon(String isGroupon) {
		this.isGroupon = isGroupon;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public CategoryBean getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(CategoryBean productCategory) {
		this.productCategory = productCategory;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 将多个ProductBean封装近list里面，返回出来
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<ProductBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<ProductBean> list = new ArrayList<ProductBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(),
						ProductBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public ProductBean() {
		// TODO Auto-generated constructor stub
		super();
	}

}
