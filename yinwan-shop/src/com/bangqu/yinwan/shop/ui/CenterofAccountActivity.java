package com.bangqu.yinwan.shop.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.FinanceBean;
import com.bangqu.yinwan.shop.helper.ShopFinanceHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.DateUtil;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

public class CenterofAccountActivity extends UIBaseActivity implements
		OnClickListener {
	private Button btnRight, btnLeft;
	private TextView tvTittle;
	FinanceBean financeBean;
	private TextView todayTurnover, tvtodayTurnoverend;// 日
	private TextView tvturnover;
	private TextView tvwithdraw;// 已提现
	private TextView tvrebateMoney;// 返点
	private TextView tvweekTurnover;// 周
	private TextView tvmonthTurnover;// 月
	private TextView tvyearTurnover;// 年
	private TextView tvtoday, tvyear, tvmonth, tvweek;// 时间
	private TextView tvallyue;
	private TextView tvunbalanced;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhanghu_layout);
		findView();
		new LoadCenterTask(
				SharedPrefUtil.getToken(CenterofAccountActivity.this),
				SharedPrefUtil.getShopBean(CenterofAccountActivity.this)
						.getId()).execute();
	}

	@Override
	public void findView() {
		super.findView();
		tvallyue = (TextView) findViewById(R.id.tvallyue);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("我要提现");
		btnRight.setOnClickListener(this);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("账户中心");
		todayTurnover = (TextView) findViewById(R.id.todayTurnover);
		tvtodayTurnoverend = (TextView) findViewById(R.id.tvtodayTurnoverend);
		tvturnover = (TextView) findViewById(R.id.tvturnover);
		tvwithdraw = (TextView) findViewById(R.id.tvwithdraw);
		tvrebateMoney = (TextView) findViewById(R.id.tvrebateMoney);
		tvweekTurnover = (TextView) findViewById(R.id.tvweekTurnover);
		tvmonthTurnover = (TextView) findViewById(R.id.tvmonthTurnover);
		tvyearTurnover = (TextView) findViewById(R.id.tvyearTurnover);
		tvtoday = (TextView) findViewById(R.id.tvtoday);
		tvyear = (TextView) findViewById(R.id.tvyear);
		tvmonth = (TextView) findViewById(R.id.tvmonth);
		tvallyue = (TextView) findViewById(R.id.tvallyue);
		tvweek = (TextView) findViewById(R.id.tvweek);
		tvunbalanced = (TextView) findViewById(R.id.tvunbalanced);

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日 HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		tvtoday.setText(formatter.format(curDate).substring(0, 12));
		tvyear.setText(formatter.format(curDate).substring(0, 5));
		tvmonth.setText(formatter.format(curDate).substring(5, 8));
		tvweek.setText(DateUtil.weekRange());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRight:
			startActivity(new Intent(CenterofAccountActivity.this,
					AccountActivity.class));
			break;
		case R.id.btnLeft:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void fillData() {
		super.fillData();
		todayTurnover.setText(financeBean.getTodayTurnover() + "元");
		tvtodayTurnoverend.setText("+" + financeBean.getTodayTurnover() + "元");
		tvturnover.setText(financeBean.getTurnover() + "元");
		tvwithdraw.setText(financeBean.getWithdraw() + "元");
		tvrebateMoney.setText(financeBean.getRebateMoney() + "元");
		tvweekTurnover.setText("+" + financeBean.getWeekTurnover() + "元");
		tvmonthTurnover.setText("+" + financeBean.getMonthTurnover() + "元");
		tvyearTurnover.setText("+" + financeBean.getYearTurnover() + "元");
		tvallyue.setText(financeBean.getBalance() + "元");
		if (financeBean.getRebate().equals("0")) {
			tvunbalanced.setText("0元");
		} else {
			tvunbalanced
					.setText((Double.parseDouble(financeBean.getTurnover()) - Double
							.parseDouble(financeBean.getRebateMoney())
							/ Double.parseDouble(financeBean.getRebate())
							* (1 - Double.parseDouble(financeBean.getRebate())))
							+ financeBean.getRebateMoney() + "元");
		}

	}

	/**
	 * 账户中心
	 */
	class LoadCenterTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String shopId;

		protected LoadCenterTask(String accessToken, String shopId) {
			this.accessToken = accessToken;
			this.shopId = shopId;

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopFinanceHelper().financemine(accessToken, shopId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						financeBean = JSON.parseObject(
								result.getJSONObject("finance").toString(),
								FinanceBean.class);
						fillData();
						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(CenterofAccountActivity.this,
								result.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("LoginActivity", "JSONException");
				}
			} else {
				Log.i("LoginActivity", "result==null");
			}

		}
	}

}
