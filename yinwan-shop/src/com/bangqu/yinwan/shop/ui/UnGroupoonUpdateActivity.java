package com.bangqu.yinwan.shop.ui;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class UnGroupoonUpdateActivity extends UIBaseActivity implements
		OnClickListener {
	private int YYYY;
	private int MM;
	private int DD;
	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;
	private ImageView ivproduct;

	private String id;
	private Button btnGrouponJoin;

	private EditText etGrouponName;
	private EditText etGrouponPrice;
	private EditText etMinimum;
	private TextView tvGrouponPriceold;
	private TextView tvgroupvip;

	private TextView tvGrouponStartTime;
	private TextView tvGrouponEndTime;
	public static final int CATEGORY_SELECT = 11210;

	private ProductBean productBean;
	private int HH;
	private int MIN;

	private TextView tvGrouStartTime2;
	private TextView tvGrouEndTime2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ungroupon_update_layout);

		findView();
		id = getIntent().getStringExtra("ProductId");
		new LoadGrouponDetailTask(id).execute();
		// fillData();
	}

	public void findView() {
		super.findView();
		tvGrouEndTime2 = (TextView) findViewById(R.id.tvGrouEndTime2);
		tvGrouEndTime2.setOnClickListener(this);
		tvGrouStartTime2 = (TextView) findViewById(R.id.tvGrouStartTime2);
		tvGrouStartTime2.setOnClickListener(this);
		// 顶部title
		tvgroupvip = (TextView) findViewById(R.id.tvgroupvip);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("添加团购信息");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnGrouponJoin = (Button) findViewById(R.id.btnGrouponJoin);
		ivproduct = (ImageView) findViewById(R.id.ivproduct);
		tvGrouponPriceold = (TextView) findViewById(R.id.tvGrouponPriceold);

		etGrouponName = (EditText) findViewById(R.id.etGrouponName);
		etGrouponPrice = (EditText) findViewById(R.id.etGrouponPrice);
		etMinimum = (EditText) findViewById(R.id.etMinimum);
		tvGrouponStartTime = (TextView) findViewById(R.id.tvPromStartTime);
		tvGrouponEndTime = (TextView) findViewById(R.id.tvPromEndTime);

		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);

		tvGrouponStartTime.setOnClickListener(this);
		tvGrouponStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (StringUtil.isBlank(tvGrouponStartTime.getText().toString()
						.trim()
						+ "")) {
					Calendar c = Calendar.getInstance();
					YYYY = c.get(Calendar.YEAR);
					MM = c.get(Calendar.MONTH);
					DD = c.get(Calendar.DAY_OF_MONTH);
				} else if (tvGrouponStartTime.getText().length() == 10) {
					System.out.println(tvGrouponStartTime.getText().toString()
							.trim().length());
					YYYY = Integer.parseInt(tvGrouponStartTime.getText()
							.toString().trim().substring(0, 4));
					MM = Integer.parseInt(tvGrouponStartTime.getText()
							.toString().trim().substring(5, 7)) - 1;
					DD = Integer.parseInt(tvGrouponStartTime.getText()
							.toString().trim().substring(8, 10));
				}

				// Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(UnGroupoonUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {

								tvGrouponStartTime.setText(year + "-"
										+ StringUtil.addzero(month + 1) + "-"
										+ StringUtil.addzero(dayOfMonth));
							}
						}
						// 设置初试日期
						, YYYY, MM, DD).show();
			}
		});
		tvGrouponEndTime.setOnClickListener(this);
		tvGrouponEndTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (StringUtil.isBlank(tvGrouponEndTime.getText().toString()
						.trim()
						+ "")) {
					Calendar c = Calendar.getInstance();
					YYYY = c.get(Calendar.YEAR);
					MM = c.get(Calendar.MONTH);
					DD = c.get(Calendar.DAY_OF_MONTH);
				} else if (tvGrouponEndTime.getText().length() == 10) {
					System.out.println(tvGrouponEndTime.getText().toString()
							.trim().length());
					YYYY = Integer.parseInt(tvGrouponEndTime.getText()
							.toString().trim().substring(0, 4));
					MM = Integer.parseInt(tvGrouponEndTime.getText().toString()
							.trim().substring(5, 7)) - 1;
					DD = Integer.parseInt(tvGrouponEndTime.getText().toString()
							.trim().substring(8, 10));
				}

				// Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(UnGroupoonUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {

								tvGrouponEndTime.setText(year + "-"
										+ StringUtil.addzero(month + 1) + "-"
										+ StringUtil.addzero(dayOfMonth));
							}
						}
						// 设置初试日期
						, YYYY, MM, DD).show();
			}
		});

		btnGrouponJoin.setOnClickListener(this);

	}

	public void fillData() {
		super.fillData();
		etGrouponName.setText(productBean.getName());
		((CommonApplication) getApplicationContext()).getImgLoader()
				.DisplayImage(productBean.getImg(), ivproduct);
		tvGrouponPriceold.setText("￥" + productBean.getPrice());
		if (!StringUtil.isBlank(productBean.getVipPrice())) {
			tvgroupvip.setText("￥" + productBean.getVipPrice());
		} else {
			tvgroupvip.setText("暂无");
		}

	}

	public static final int CHOOSE_CATEGORY = 1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvGrouEndTime2:
			Calendar c = Calendar.getInstance();
			if (!StringUtil.isBlank(tvGrouEndTime2.getText().toString())) {
				HH = Integer.parseInt(tvGrouEndTime2.getText().toString()
						.substring(0, 2));
				MIN = Integer.parseInt(tvGrouEndTime2.getText().toString()
						.substring(3, 5));
			} else {
				HH = c.get(Calendar.HOUR_OF_DAY);
				MIN = c.get(Calendar.MINUTE);
			}

			// 创建一个TimoPickerDialog实例，并把他们显示出来
			new TimePickerDialog(UnGroupoonUpdateActivity.this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker tp, int hourOfDay,
								int minute) {
							tvGrouEndTime2.setText(StringUtil
									.addzero(hourOfDay)
									+ ":"
									+ StringUtil.addzero(minute));
						}
					}, HH, MIN, true).show();
			break;

		case R.id.tvGrouStartTime2:
			Calendar cl = Calendar.getInstance();
			if (!StringUtil.isBlank(tvGrouStartTime2.getText().toString())) {
				HH = Integer.parseInt(tvGrouStartTime2.getText().toString()
						.substring(0, 2));
				MIN = Integer.parseInt(tvGrouStartTime2.getText().toString()
						.substring(3, 5));
			} else {
				HH = cl.get(Calendar.HOUR_OF_DAY);
				MIN = cl.get(Calendar.MINUTE);
			}

			// 创建一个TimoPickerDialog实例，并把他们显示出来
			new TimePickerDialog(UnGroupoonUpdateActivity.this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker tp, int hourOfDay,
								int minute) {
							tvGrouStartTime2.setText(StringUtil
									.addzero(hourOfDay)
									+ ":"
									+ StringUtil.addzero(minute));
						}
					}, HH, MIN, true).show();
			break;
		case R.id.rlshop:
			Intent intentshop = new Intent(UnGroupoonUpdateActivity.this,
					HomeMoreShopActivity.class);
			startActivity(intentshop);
			break;

		case R.id.btnLeft:
			finish();
			break;

		case R.id.tvGrouponCategory:
			Intent categoryintent = new Intent(UnGroupoonUpdateActivity.this,
					ProductCategoryListActivity.class);
			categoryintent.putExtra("IntentValue", "notfromhome");
			startActivityForResult(categoryintent, CATEGORY_SELECT);
			break;

		case R.id.btnGrouponJoin:
			String strGrouponPrice = etGrouponPrice.getText().toString().trim();
			String strMinimum = etMinimum.getText().toString().trim();
			String strStartTime = tvGrouponStartTime.getText().toString() + " "
					+ tvGrouStartTime2.getText().toString() + ":00";
			String strEndtime = tvGrouponEndTime.getText().toString() + " "
					+ tvGrouEndTime2.getText().toString() + ":00";
			if (StringUtil.isBlank(strMinimum)) {
				Toast.makeText(UnGroupoonUpdateActivity.this, "请输入开团人数",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(strGrouponPrice)) {
				Toast.makeText(UnGroupoonUpdateActivity.this, "请输入团购价格",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Double.parseDouble(strGrouponPrice) >= Double
					.parseDouble(productBean.getPrice())) {
				Toast.makeText(UnGroupoonUpdateActivity.this, "团购价不能高于商品价",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringUtil.isBlank(tvGrouponStartTime.getText().toString())) {
				Toast.makeText(UnGroupoonUpdateActivity.this, "请选择团购开始日期",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvGrouStartTime2.getText().toString())) {
				Toast.makeText(UnGroupoonUpdateActivity.this, "请选择团购开始时间",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvGrouponEndTime.getText().toString())) {
				Toast.makeText(UnGroupoonUpdateActivity.this, "请选择团购结束日期",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvGrouEndTime2.getText().toString())) {
				Toast.makeText(UnGroupoonUpdateActivity.this, "请选择团购结束时间",
						Toast.LENGTH_SHORT).show();
				return;
			}
			String strYY = (String) tvGrouponStartTime.getText()
					.subSequence(0, 4).toString().trim();
			String StrYY2 = (String) tvGrouponEndTime.getText()
					.subSequence(0, 4).toString().trim();
			String strMM = (String) tvGrouponStartTime.getText()
					.subSequence(5, 7).toString().trim();
			String strMM2 = (String) tvGrouponEndTime.getText()
					.subSequence(5, 7).toString().trim();
			String strDD = (String) tvGrouponStartTime.getText()
					.subSequence(8, 10).toString().trim();
			String strDD2 = (String) tvGrouponEndTime.getText()
					.subSequence(8, 10).toString().trim();
			if (Double.parseDouble(strYY) > Double.parseDouble(StrYY2)) {

				Toast.makeText(UnGroupoonUpdateActivity.this, "开始时间不能大于结束时间",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (Double.parseDouble(strYY) == Double.parseDouble(StrYY2)) {
				if (Double.parseDouble(strMM) > Double.parseDouble(strMM2)) {
					Toast.makeText(UnGroupoonUpdateActivity.this,
							"开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
					return;
				} else if (Double.parseDouble(strMM) == Double
						.parseDouble(strMM2)) {
					if (Double.parseDouble(strDD) > Double.parseDouble(strDD2)) {
						Toast.makeText(UnGroupoonUpdateActivity.this,
								"开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}
			new LoadGrouponAddTask(SharedPrefUtil.getToken(this), id,
					strStartTime, strEndtime, strGrouponPrice, strMinimum)
					.execute();

			break;

		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CATEGORY_SELECT:
			break;

		default:
			break;
		}
	}

	/**
	 * 商品加入团购
	 * 
	 * @author Administrator
	 */
	class LoadGrouponAddTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String productId;
		private String startTime;
		private String endTime;
		private String price;
		private String minimum;

		protected LoadGrouponAddTask(String accessToken, String productId,
				String startTime, String endTime, String price, String minimum) {
			this.accessToken = accessToken;
			this.productId = productId;
			this.price = price;
			this.minimum = minimum;
			this.startTime = startTime;
			this.endTime = endTime;

		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						UnGroupoonUpdateActivity.this, "正在保存修改……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().addgroupon(accessToken, productId,
						startTime, endTime, price, minimum);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pd != null)
				pd.dismiss();

			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(UnGroupoonUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(UnGroupoonUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(UnGroupoonUpdateActivity.this, "数据加载失败1",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(UnGroupoonUpdateActivity.this, "数据加载失败2",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	/**
	 * 预览未团购商品信息
	 * 
	 * @author Administrator
	 */
	class LoadGrouponDetailTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadGrouponDetailTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productView(id);
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
					System.out.println(result);
					if (result.getInt("status") == Constants.SUCCESS) {

						productBean = JSON.parseObject(
								result.getJSONObject("product").toString(),
								ProductBean.class);
						fillData();
						progressbar.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(UnGroupoonUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(UnGroupoonUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(UnGroupoonUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

}
