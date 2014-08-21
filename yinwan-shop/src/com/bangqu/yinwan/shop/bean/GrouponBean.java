package com.bangqu.yinwan.shop.bean;

/**
 *团购实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class GrouponBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String addTime;
	private String startTime;
	private String endTime;
	private String price;
	private String count;
	private String minimum;
	private String sales;
	private String state;
	private String enabled;
	private ProductBean product;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getMinimum() {
		return minimum;
	}

	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 将多个FinanceBean封装近list里面，返回出来
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<GrouponBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<GrouponBean> list = new ArrayList<GrouponBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(),
						GrouponBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GrouponBean() {
		super();
	}
}
