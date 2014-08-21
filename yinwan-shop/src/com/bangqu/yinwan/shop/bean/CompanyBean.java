package com.bangqu.yinwan.shop.bean;

/**
 * 物业公司实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class CompanyBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String addTime;
	private String name;
	private String description;
	private String img;
	private String phone;
	private String state;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 将多个CompanyBean封装近list里面，返回出来
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<CompanyBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<CompanyBean> list = new ArrayList<CompanyBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(),
						CompanyBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public CompanyBean() {
		// TODO Auto-generated constructor stub
		super();
	}

}
