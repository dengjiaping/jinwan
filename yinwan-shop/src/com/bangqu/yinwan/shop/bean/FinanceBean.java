package com.bangqu.yinwan.shop.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.internet.SystemException;

public class FinanceBean implements Serializable {

	private static final long serialVersionUID = 1L;
	String id;
	String updateTime;
	String balance;// 余额
	String frozen;// 冻结金额
	String recharge;// 充值金额
	String withdraw;// 总提现金额
	String turnover;// 总营业额
	String rebate;// 返点比例
	String todayTurnover;// 今日营业额
	String weekTurnover;// 本周营业额
	String monthTurnover;// 本月营业额
	String yearTurnover;// 本年营业额
	String rebateMoney;// 总返点金额

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getFrozen() {
		return frozen;
	}

	public void setFrozen(String frozen) {
		this.frozen = frozen;
	}

	public String getRecharge() {
		return recharge;
	}

	public void setRecharge(String recharge) {
		this.recharge = recharge;
	}

	public String getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(String withdraw) {
		this.withdraw = withdraw;
	}

	public String getTurnover() {
		return turnover;
	}

	public void setTurnover(String turnover) {
		this.turnover = turnover;
	}

	public String getRebate() {
		return rebate;
	}

	public void setRebate(String rebate) {
		this.rebate = rebate;
	}

	public String getTodayTurnover() {
		return todayTurnover;
	}

	public void setTodayTurnover(String todayTurnover) {
		this.todayTurnover = todayTurnover;
	}

	public String getWeekTurnover() {
		return weekTurnover;
	}

	public void setWeekTurnover(String weekTurnover) {
		this.weekTurnover = weekTurnover;
	}

	public String getMonthTurnover() {
		return monthTurnover;
	}

	public void setMonthTurnover(String monthTurnover) {
		this.monthTurnover = monthTurnover;
	}

	public String getYearTurnover() {
		return yearTurnover;
	}

	public void setYearTurnover(String yearTurnover) {
		this.yearTurnover = yearTurnover;
	}

	public String getRebateMoney() {
		return rebateMoney;
	}

	public void setRebateMoney(String rebateMoney) {
		this.rebateMoney = rebateMoney;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static List<FinanceBean> constractList(JSONArray jsonArray)
			throws SystemException {
		try {
			List<FinanceBean> list = new ArrayList<FinanceBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JSON.parseObject(
						jsonArray.getJSONObject(i).toString(),
						FinanceBean.class));
			}
			return list;

		} catch (JSONException je) {
			throw new SystemException(je.getMessage());
		}
	}

	public FinanceBean() {
		// TODO Auto-generated constructor stub
		super();
	}
}
