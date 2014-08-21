package com.bangqu.yinwan.shop.ui;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.OrderNotifyBean;
import com.bangqu.yinwan.shop.helper.OrderHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

/**
 * 订单首页
 * 
 * @author banxin
 * 
 */
public class HomeOrderManageActivity extends UIBaseActivity implements
		OnClickListener {

	private TextView btnLeft;
	private Button btnRight;
	private TextView tvTittle;

	private LinearLayout llweichuli;
	private LinearLayout llpeisongzhong;
	private LinearLayout llyiwancheng;
	private LinearLayout llyiquxiao;
	private LinearLayout llyituikuan;
	private LinearLayout lltuikuanzhong;

	private OrderNotifyBean orderNotifyBean;
	private TextView tvUndealToday;
	private TextView tvUndealTotal;
	private TextView tvIndealToday;
	private TextView tvFinishToday;
	private TextView tvFinishMonth;
	private TextView tvCancelToday;
	private TextView tvCancelMonth;
	private TextView tvtuikuanzhong;
	private TextView tvyituikuan;

	private String clickdate = "";
	private String clickTime = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_order_layout);
		findView();
		Constants.NOTIFICATION = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (SharedPrefUtil.getLastClickTime(this) != null) {
			clickdate = SharedPrefUtil.getLastClickTime(this);
			System.out.println("上一次点击的时间：" + clickdate);
			new LoadOrderInfoViewTask(SharedPrefUtil.getToken(this),
					SharedPrefUtil.getShopBean(this).getId(), clickdate)
					.execute();
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			clickdate = formatter.format(curDate);
			new LoadOrderInfoViewTask(SharedPrefUtil.getToken(this),
					SharedPrefUtil.getShopBean(this).getId(), clickdate)
					.execute();
		}
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		super.findView();
		btnLeft = (TextView) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("订单管理");

		llweichuli = (LinearLayout) findViewById(R.id.llweichuli);
		llpeisongzhong = (LinearLayout) findViewById(R.id.llpeisongzhong);
		llyiwancheng = (LinearLayout) findViewById(R.id.llyiwancheng);
		llyiquxiao = (LinearLayout) findViewById(R.id.llyiquxiao);
		llyituikuan = (LinearLayout) findViewById(R.id.llyituikuan);
		lltuikuanzhong = (LinearLayout) findViewById(R.id.lltuikuanzhong);

		llweichuli.setOnClickListener(this);
		llpeisongzhong.setOnClickListener(this);
		llyiwancheng.setOnClickListener(this);
		llyiquxiao.setOnClickListener(this);
		llyituikuan.setOnClickListener(this);
		lltuikuanzhong.setOnClickListener(this);

		tvUndealToday = (TextView) findViewById(R.id.tvUndealToday);
		tvUndealTotal = (TextView) findViewById(R.id.tvUndealTotal);
		tvIndealToday = (TextView) findViewById(R.id.tvIndealToday);
		tvFinishToday = (TextView) findViewById(R.id.tvFinishToday);
		tvFinishMonth = (TextView) findViewById(R.id.tvFinishMonth);
		tvCancelToday = (TextView) findViewById(R.id.tvCancelToday);
		tvCancelMonth = (TextView) findViewById(R.id.tvCancelMonth);
		tvtuikuanzhong = (TextView) findViewById(R.id.tvtuikuanzhong);
		tvyituikuan = (TextView) findViewById(R.id.tvyituikuan);
	}

	public void fillData() {
		// TODO Auto-generated method stub
		super.fillData();
		// tvUndealToday.setText("今日新单：" + orderNotifyBean.getUnreadZero());
		int i = Integer.parseInt(orderNotifyBean.getUnreadZero());
		if (i == 0) {
			tvUndealToday.setVisibility(View.INVISIBLE);
		} else if (i > 9) {
			tvUndealToday.setText("9+");
		} else {
			tvUndealToday.setText(orderNotifyBean.getUnreadZero());
		}
		tvUndealTotal.setText("待配送订单数量：" + orderNotifyBean.getAllZero());
		tvIndealToday.setText("配送中：" + orderNotifyBean.getTwo());
		tvFinishToday.setText("今日完成：" + orderNotifyBean.getTodayOne());
		tvFinishMonth.setText("本月交易量：" + orderNotifyBean.getMonthOne());
		tvCancelToday.setText("今日取消：" + orderNotifyBean.getTodayNegativeOne());
		tvCancelMonth.setText("本月取消量：" + orderNotifyBean.getMonthNegativeOne());

		tvtuikuanzhong.setText("退款中数量：" + orderNotifyBean.getSix());
		tvyituikuan.setText("已退款数量：" + orderNotifyBean.getSeven());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLeft:
			HomeOrderManageActivity.this.finish();
			break;
		case R.id.llweichuli:
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			clickTime = formatter.format(curDate);
			SharedPrefUtil.setLastClickTime(HomeOrderManageActivity.this,
					clickTime);
			Intent weichuliintent = new Intent(this, DingDanOne.class);
			weichuliintent.putExtra("init", "0");
			startActivity(weichuliintent);
			break;
		case R.id.llpeisongzhong:
			Intent peisongzhongintent = new Intent(this, DingDanTwo.class);
			peisongzhongintent.putExtra("init", "1");
			startActivity(peisongzhongintent);
			break;
		case R.id.llyiwancheng:
			Intent yiwanchengintent = new Intent(this, DingDanThree.class);
			yiwanchengintent.putExtra("init", "2");
			startActivity(yiwanchengintent);
			break;
		case R.id.llyiquxiao:
			Intent yiquxiaointent = new Intent(this, DingDanFour.class);
			yiquxiaointent.putExtra("init", "3");
			startActivity(yiquxiaointent);
			break;
		case R.id.lltuikuanzhong:
			Intent tuikuanzhonginten = new Intent(this, DingDanSix.class);
			tuikuanzhonginten.putExtra("init", "6");
			startActivity(tuikuanzhonginten);
			break;
		case R.id.llyituikuan:
			Intent yituikuaninten = new Intent(this, DingDanSeven.class);
			yituikuaninten.putExtra("init", "7");
			startActivity(yituikuaninten);
			break;

		default:
			break;
		}

	}

	/**
	 * 获取订单条数信息
	 * 
	 * @author Administrator
	 */
	class LoadOrderInfoViewTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String clickdate;

		protected LoadOrderInfoViewTask(String accessToken, String shopId,
				String clickdate) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.clickdate = clickdate;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new OrderHelper().OrderInfo(accessToken, shopId,
						clickdate);
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
						orderNotifyBean = JSON.parseObject(result
								.getJSONObject("notify").toString(),
								OrderNotifyBean.class);
						fillData();
						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						orderNotifyBean = JSON.parseObject(result
								.getJSONObject("notify").toString(),
								OrderNotifyBean.class);
						fillData();
						progressbar.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(HomeOrderManageActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(HomeOrderManageActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}
}