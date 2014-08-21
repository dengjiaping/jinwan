package com.bangqu.yinwan.shop.bean;

/**
 *订单实体类
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class OrderBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String addTime;
	private String no;
	private String price;
	private String quantity;
	private String productSize;
	private String state;
	private String remark;
	private String shopNo;
	private String statements;// 状态
	private String payment;// 支付方式

	private String type;
	private String img;
	private String locationName;
	private String content;
	private String companyName;
	private String deliveryTime;
	private ShopBean shop;
	CompanyBean company;
	LocationBean location;
	StepBean steps;

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getStatements() {
		return statements;
	}

	public void setStatements(String statements) {
		this.statements = statements;
	}

	public LocationBean getLocation() {
		return location;
	}

	public void setLocation(LocationBean location) {
		this.location = location;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private ProductBean productBean;
	private List<ProductBean> products;

	public ProductBean getProductBean() {
		return productBean;
	}

	public List<ProductBean> getProducts() {
		return products;
	}

	public void setProducts(List<ProductBean> products) {
		this.products = products;
	}

	public void setProductBean(ProductBean productBean) {
		this.productBean = productBean;
	}

	private AddressBean address;
	private List<ItemsBean> items;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public List<ItemsBean> getItems() {
		return items;
	}

	public void setItems(List<ItemsBean> items) {
		this.items = items;
	}

	public AddressBean getAddress() {
		return address;
	}

	public void setAddress(AddressBean address) {
		this.address = address;
	}

	public void setState(String state) {
		this.state = state;
	}

	public ShopBean getShop() {
		return shop;
	}

	public String getAddTime() {
		return addTime;
	}

	public String getState() {
		return state;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setShop(ShopBean shop) {
		this.shop = shop;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public StepBean getSteps() {
		return steps;
	}

	public void setSteps(StepBean steps) {
		this.steps = steps;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public CompanyBean getCompany() {
		return company;
	}

	public void setCompany(CompanyBean company) {
		this.company = company;
	}

	/**
	 * 将多个OrderBean封装近list里面，返回出来
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<OrderBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<OrderBean> list = new ArrayList<OrderBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(), OrderBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public OrderBean() {
		super();
	}

}
