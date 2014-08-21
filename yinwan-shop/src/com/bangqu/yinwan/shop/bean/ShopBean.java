package com.bangqu.yinwan.shop.bean;

/**
 * 商铺实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class ShopBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String addTime;
	private String updateTime;
	private String name;
	private String alias;
	private String phone;
	private String linkman;
	private String address;
	private String img;
	private String count;
	private String callCount;
	private String lng;
	private String lat;
	private String sendPrice;
	private String moods;
	private String content;
	private String promo;
	private String score;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String description;
	private String locationSize;
	private String productSize;
	private String collectSize;

	private Boolean deliver;
	private String payment;
	private Boolean companyDelivery;

	private String state;
	private String integrity;// 信息完整度
	private Boolean enabled;
	private Boolean groupon; // 是否团购
	private Boolean promotion; // 是否促销
	private Boolean vip; // 是否vip
	ProductBean productBean;
	CityBean city;// 城市
	DistrictBean district;

	public DistrictBean getDistrict() {
		return district;
	}

	public void setDistrict(DistrictBean district) {
		this.district = district;
	}

	public CityBean getCity() {
		return city;
	}

	public void setCity(CityBean city) {
		this.city = city;
	}

	public ProductBean getProductBean() {
		return productBean;
	}

	public void setProductBean(ProductBean productBean) {
		this.productBean = productBean;
	}

	public ShopBean(String id, String name) {
		super();

		this.id = id;
		this.name = name;
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCallCount() {
		return callCount;
	}

	public void setCallCount(String callCount) {
		this.callCount = callCount;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(String sendPrice) {
		this.sendPrice = sendPrice;
	}

	public String getMoods() {
		return moods;
	}

	public void setMoods(String moods) {
		this.moods = moods;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPromo() {
		return promo;
	}

	public void setPromo(String promo) {
		this.promo = promo;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocationSize() {
		return locationSize;
	}

	public void setLocationSize(String locationSize) {
		this.locationSize = locationSize;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public String getCollectSize() {
		return collectSize;
	}

	public void setCollectSize(String collectSize) {
		this.collectSize = collectSize;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Boolean getDeliver() {
		return deliver;
	}

	public void setDeliver(Boolean deliver) {
		this.deliver = deliver;
	}

	public Boolean getCompanyDelivery() {
		return companyDelivery;
	}

	public void setCompanyDelivery(Boolean companyDelivery) {
		this.companyDelivery = companyDelivery;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIntegrity() {
		return integrity;
	}

	public void setIntegrity(String integrity) {
		this.integrity = integrity;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getGroupon() {
		return groupon;
	}

	public void setGroupon(Boolean groupon) {
		this.groupon = groupon;
	}

	public Boolean getPromotion() {
		return promotion;
	}

	public void setPromotion(Boolean promotion) {
		this.promotion = promotion;
	}

	public Boolean getVip() {
		return vip;
	}

	public void setVip(Boolean vip) {
		this.vip = vip;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 将多个shopbean封装近list里面，返回出来,在bean里有list的时候会用到
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */

	public static List<ShopBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<ShopBean> list = new ArrayList<ShopBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(), ShopBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public ShopBean() {
		super();
	}

}
