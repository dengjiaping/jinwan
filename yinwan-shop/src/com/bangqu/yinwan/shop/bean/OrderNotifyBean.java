package com.bangqu.yinwan.shop.bean;

/**
 *订单详情实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class OrderNotifyBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String unreadZero;
	private String allZero;
	private String two;
	private String todayOne;
	private String monthOne;
	private String todayNegativeOne;
	private String monthNegativeOne;
	private String six;
	private String seven;

	/**
	 * 构造方法来存数据,数据缓存时用到(订单页用到)
	 * 
	 * @param unreadZero
	 * @param allZero
	 * @param two
	 * @param todayOne
	 * @param monthOne
	 * @param todayNegativeOne
	 * @param monthNegativeOne
	 * @param six
	 * @param seven
	 */
	public OrderNotifyBean(String unreadZero, String allZero, String two,
			String todayOne, String monthOne, String todayNegativeOne,
			String monthNegativeOne, String six, String seven) {
		super();
		this.unreadZero = unreadZero;
		this.allZero = allZero;
		this.two = two;
		this.todayOne = todayOne;
		this.monthOne = monthOne;
		this.todayNegativeOne = todayNegativeOne;
		this.monthNegativeOne = monthNegativeOne;
		this.six = six;
		this.seven = seven;
	}

	public static List<OrderNotifyBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<OrderNotifyBean> list = new ArrayList<OrderNotifyBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(),
						OrderNotifyBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public String getSix() {
		return six;
	}

	public void setSix(String six) {
		this.six = six;
	}

	public String getSeven() {
		return seven;
	}

	public void setSeven(String seven) {
		this.seven = seven;
	}

	public String getUnreadZero() {
		return unreadZero;
	}

	public void setUnreadZero(String unreadZero) {
		this.unreadZero = unreadZero;
	}

	public String getAllZero() {
		return allZero;
	}

	public void setAllZero(String allZero) {
		this.allZero = allZero;
	}

	public String getTwo() {
		return two;
	}

	public void setTwo(String two) {
		this.two = two;
	}

	public String getTodayOne() {
		return todayOne;
	}

	public void setTodayOne(String todayOne) {
		this.todayOne = todayOne;
	}

	public String getMonthOne() {
		return monthOne;
	}

	public void setMonthOne(String monthOne) {
		this.monthOne = monthOne;
	}

	public String getTodayNegativeOne() {
		return todayNegativeOne;
	}

	public void setTodayNegativeOne(String todayNegativeOne) {
		this.todayNegativeOne = todayNegativeOne;
	}

	public String getMonthNegativeOne() {
		return monthNegativeOne;
	}

	public void setMonthNegativeOne(String monthNegativeOne) {
		this.monthNegativeOne = monthNegativeOne;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public OrderNotifyBean() {
		super();
	}

}
