package com.bangqu.yinwan.shop.bean;

import java.io.Serializable;

public class ShopStateBean implements Serializable {
	private static final long serialVersionUID = 1L;
	String zero;// 审核中
	String one;// 已审核
	String negativeTwo;// 已驳回

	public String getZero() {
		return zero;
	}

	public void setZero(String zero) {
		this.zero = zero;
	}

	public String getOne() {
		return one;
	}

	public void setOne(String one) {
		this.one = one;
	}

	public String getNegativeTwo() {
		return negativeTwo;
	}

	public void setNegativeTwo(String negativeTwo) {
		this.negativeTwo = negativeTwo;
	}

}
