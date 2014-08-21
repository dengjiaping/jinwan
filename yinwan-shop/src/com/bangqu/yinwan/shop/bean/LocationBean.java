package com.bangqu.yinwan.shop.bean;

/**
 * 周边小区列表实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class LocationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String addTime;
	private String updateTime;
	private String name;
	private String phone;
	private String state;
	private DistrictBean district;
	private DistributionBean distribution;
	private CompanyBean company;
	private CityBean city;

	public DistributionBean getDistribution() {
		return distribution;
	}

	public void setDistribution(DistributionBean distribution) {
		this.distribution = distribution;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DistrictBean getDistrict() {
		return district;
	}

	public void setDistrict(DistrictBean district) {
		this.district = district;
	}

	public CompanyBean getCompany() {
		return company;
	}

	public void setCompany(CompanyBean company) {
		this.company = company;
	}

	public CityBean getCity() {
		return city;
	}

	public void setCity(CityBean city) {
		this.city = city;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 将多个LocationBean封装近list里面，返回出来
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<LocationBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<LocationBean> list = new ArrayList<LocationBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(),
						LocationBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public LocationBean() {
		// TODO Auto-generated constructor stub
		super();
	}

}
