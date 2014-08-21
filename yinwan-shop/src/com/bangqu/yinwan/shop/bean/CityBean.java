package com.bangqu.yinwan.shop.bean;

/**
 *已开通城市列表实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class CityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	String name;
	String state;
	String provinceBean;
	String shopsize;

	public String getShopsize() {
		return shopsize;
	}

	public void setShopsize(String shopsize) {
		this.shopsize = shopsize;
	}

	/**
	 * 将多个CategoryBean封装近list里面，返回出来
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<CityBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<CityBean> list = new ArrayList<CityBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(), CityBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProvinceBean() {
		return provinceBean;
	}

	public void setProvinceBean(String provinceBean) {
		this.provinceBean = provinceBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
