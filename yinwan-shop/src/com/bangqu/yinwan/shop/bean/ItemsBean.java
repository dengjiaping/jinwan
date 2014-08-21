package com.bangqu.yinwan.shop.bean;

/**
 *评价实体类
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class ItemsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String no;
	private String type;
	private String price;
	private String quantity;
	private Boolean comment;
	private ProductBean product;

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

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Boolean getComment() {
		return comment;
	}

	public void setComment(Boolean comment) {
		this.comment = comment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 将多个ItemsBean封装近list里面，返回出来
	 * 
	 * @param jsonArray
	 * @return
	 * @throws SystemException
	 */
	public static List<ItemsBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<ItemsBean> list = new ArrayList<ItemsBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(), ItemsBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public ItemsBean() {
		// TODO Auto-generated constructor stub
		super();
	}

}
