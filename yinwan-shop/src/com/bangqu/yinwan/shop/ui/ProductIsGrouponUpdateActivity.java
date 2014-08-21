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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.GrouponBean;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class ProductIsGrouponUpdateActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;
	private String id;

	private Button btnGrouponSaveUpdate;
	private Button btnGrouponCancel;

	private TextView tvGrouponName;
	private TextView tvGrouponCategory;
	private TextView tvProductPrice;
	private EditText etGrouponPrice;
	private ImageView ivproduct;
	ProductBean productBean;

	private TextView tvGrouponStartTime;
	private TextView tvVipPrice;
	private TextView tvGrouponEndTime;
	private TextView tvGrouponStartTime2;
	private TextView tvGrouponEndTime2;
	private LinearLayout llrenshu;
	private EditText ettuangourenshu;
	public static final int CATEGORY_SELECT = 11210;

	private GrouponBean grouponBean;
	private int HH;
	private int MIN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productisgroupon_update_layout);
		findView();
		id = getIntent().getStringExtra("ProductId");
		new LoadGrouponViewTask(id).execute();
		// fillData();
	}

	public void fillData() {
		super.fillData();
		if (!StringUtil.isBlank(grouponBean.getProduct().getVipPrice())) {
			tvVipPrice
					.setText("会员价：￥" + grouponBean.getProduct().getVipPrice());
		} else {
			tvVipPrice.setText("会员价：" + "暂无");
		}

		tvGrouponName.setText(grouponBean.getProduct().getName());
		if (!StringUtil.isBlank(grouponBean.getProduct().getProductCategory()
				+ "")) {
			tvGrouponCategory.setText("类别："
					+ grouponBean.getProduct().getProductCategory().getName());
		}

		tvProductPrice.setText("￥" + grouponBean.getProduct().getPrice());
		etGrouponPrice.setText(grouponBean.getPrice());
		ettuangourenshu.setText(grouponBean.getMinimum());
		if (!(grouponBean.getStartTime() + "").equals("null")) {
			tvGrouponStartTime.setText(grouponBean.getStartTime().substring(0,
					10));
			tvGrouponStartTime2.setText(grouponBean.getStartTime().substring(
					11, 16));
		}
		if (!(grouponBean.getEndTime() + "").equals("null")) {
			tvGrouponEndTime.setText(grouponBean.getEndTime().substring(0, 10));
			tvGrouponEndTime2.setText(grouponBean.getEndTime()
					.substring(11, 16));
		}
		((CommonApplication) getApplicationContext()).getImgLoader()
				.DisplayImage(grouponBean.getProduct().getImg(), ivproduct);

	}

	public void findView() {
		super.findView();
		tvVipPrice = (TextView) findViewById(R.id.tvVipPrice);
		llrenshu = (LinearLayout) findViewById(R.id.llrenshu);
		llrenshu.setVisibility(View.VISIBLE);
		ettuangourenshu = (EditText) findViewById(R.id.ettuangourenshu);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("修改团购商品信息");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);
		ivproduct = (ImageView) findViewById(R.id.ivproduct);

		btnGrouponSaveUpdate = (Button) findViewById(R.id.btnGrouponSaveUpdate);
		btnGrouponSaveUpdate.setOnClickListener(this);
		btnGrouponCancel = (Button) findViewById(R.id.btnGrouponCancel);
		btnGrouponCancel.setOnClickListener(this);
		tvGrouponName = (TextView) findViewById(R.id.tvGrouponName);

		tvGrouponCategory = (TextView) findViewById(R.id.tvGrouponCategory);
		tvProductPrice = (TextView) findViewById(R.id.tvProductPrice);

		etGrouponPrice = (EditText) findViewById(R.id.etGrouponPrice);

		tvGrouponStartTime = (TextView) findViewById(R.id.tvGrouponStartTime);
		tvGrouponEndTime = (TextView) findViewById(R.id.tvGrouponEndTime);
		tvGrouponStartTime2 = (TextView) findViewById(R.id.tvGrouponStartTime2);
		tvGrouponStartTime2.setOnClickListener(this);
		tvGrouponEndTime2 = (TextView) findViewById(R.id.tvGrouponEndTime2);
		tvGrouponEndTime2.setOnClickListener(this);
		tvGrouponStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(
						ProductIsGrouponUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {
								tvGrouponStartTime.setText(year + "-"
										+ StringUtil.addzero(month + 1) + "-"
										+ StringUtil.addzero(dayOfMonth) + "");
							}
						}
						// 设置初试日期
						, Integer.parseInt(tvGrouponStartTime.getText()
								.toString().trim().substring(0, 4)),
						Integer.parseInt(tvGrouponStartTime.getText()
								.toString().trim().substring(5, 7)) - 1,
						Integer.parseInt(tvGrouponStartTime.getText()
								.toString().trim().substring(8, 10))).show();
			}
		});
		tvGrouponEndTime.setOnClickListener(this);
		tvGrouponEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(ProductIsGrouponUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {

								tvGrouponEndTime.setText(year + "-"
										+ StringUtil.addzero(month + 1) + "-"
										+ StringUtil.addzero(dayOfMonth) + " ");
							}
						}
						// 设置初试日期
						, Integer.parseInt(tvGrouponEndTime.getText()
								.toString().trim().substring(0, 4)), Integer
								.parseInt(tvGrouponEndTime.getText().toString()
										.trim().substring(5, 7)) - 1, Integer
								.parseInt(tvGrouponEndTime.getText().toString()
										.trim().substring(8, 10))).show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public static final int CHOOSE_CATEGORY = 1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tvGrouponEndTime2:
			Calendar c = Calendar.getInstance();
			if (!StringUtil.isBlank(tvGrouponEndTime2.getText().toString())) {
				HH = Integer.parseInt(tvGrouponEndTime2.getText().toString()
						.substring(0, 2));
				MIN = Integer.parseInt(tvGrouponEndTime2.getText().toString()
						.substring(3, 5));
			} else {
				HH = c.get(Calendar.HOUR_OF_DAY);
				MIN = c.get(Calendar.MINUTE);
			}

			// 创建一个TimoPickerDialog实例，并把他们显示出来
			new TimePickerDialog(ProductIsGrouponUpdateActivity.this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker tp, int hourOfDay,
								int minute) {
							tvGrouponEndTime2.setText(StringUtil
									.addzero(hourOfDay)
									+ ":"
									+ StringUtil.addzero(minute));
						}
					}
					// 设置开始营业初始时间
					, HH, MIN
					// true表示采用24小时制
					, true).show();
			break;
		case R.id.tvGrouponStartTime2:
			Calendar cl = Calendar.getInstance();
			if (!StringUtil.isBlank(tvGrouponStartTime2.getText().toString())) {
				HH = Integer.parseInt(tvGrouponStartTime2.getText().toString()
						.substring(0, 2));
				MIN = Integer.parseInt(tvGrouponStartTime2.getText().toString()
						.substring(3, 5));
			} else {
				HH = cl.get(Calendar.HOUR_OF_DAY);
				MIN = cl.get(Calendar.MINUTE);
			}

			// 创建一个TimoPickerDialog实例，并把他们显示出来
			new TimePickerDialog(ProductIsGrouponUpdateActivity.this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker tp, int hourOfDay,
								int minute) {
							tvGrouponStartTime2.setText(StringUtil
									.addzero(hourOfDay)
									+ ":"
									+ StringUtil.addzero(minute));
						}
					}
					// 设置开始营业初始时间
					, HH, MIN
					// true表示采用24小时制
					, true).show();
			break;
		case R.id.btnLeft:
			finish();
			break;

		case R.id.tvGrouponCategory:
			Intent categoryintent = new Intent(
					ProductIsGrouponUpdateActivity.this,
					ProductCategoryListActivity.class);
			categoryintent.putExtra("IntentValue", "notfromhome");
			startActivityForResult(categoryintent, CATEGORY_SELECT);
			break;

		case R.id.btnGrouponSaveUpdate:
			String strGrouponName = tvGrouponName.getText().toString();
			String strtvtuangourenshu = ettuangourenshu.getText().toString()
					.trim();
			String strGrouponPrice = etGrouponPrice.getText().toString().trim();

			String strStartTime = tvGrouponStartTime.getText().toString() + " "
					+ tvGrouponStartTime2.getText().toString() + ":00";
			String strEndtime = tvGrouponEndTime.getText().toString() + " "
					+ tvGrouponEndTime2.getText().toString() + ":00";
			if (StringUtil.isBlank(strGrouponPrice)) {
				Toast.makeText(ProductIsGrouponUpdateActivity.this, "请输入团购价格",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Double.parseDouble(strGrouponPrice) > Double
					.parseDouble(grouponBean.getProduct().getPrice())) {
				Toast.makeText(ProductIsGrouponUpdateActivity.this,
						"团购价不能高于商品价", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvGrouponStartTime.getText().toString())) {
				Toast.makeText(ProductIsGrouponUpdateActivity.this,
						"请选择团购开始日期", Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(tvGrouponStartTime2.getText().toString())) {
				Toast.makeText(ProductIsGrouponUpdateActivity.this,
						"请选择团购开始时间", Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(tvGrouponEndTime.getText().toString())) {
				Toast.makeText(ProductIsGrouponUpdateActivity.this,
						"请选择团购结束日期", Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(tvGrouponEndTime2.getText().toString())) {
				Toast.makeText(ProductIsGrouponUpdateActivity.this,
						"请选择团购结束时间", Toast.LENGTH_LONG).show();
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

				Toast.makeText(ProductIsGrouponUpdateActivity.this,
						"开始日期不能大于结束日期", Toast.LENGTH_SHORT).show();
				return;
			} else if (Double.parseDouble(strYY) == Double.parseDouble(StrYY2)) {
				if (Double.parseDouble(strMM) > Double.parseDouble(strMM2)) {
					Toast.makeText(ProductIsGrouponUpdateActivity.this,
							"开始日期不能大于结束日期", Toast.LENGTH_SHORT).show();
					return;
				} else if (Double.parseDouble(strMM) == Double
						.parseDouble(strMM2)) {
					if (Double.parseDouble(strDD) > Double.parseDouble(strDD2)) {
						Toast.makeText(ProductIsGrouponUpdateActivity.this,
								"开始日期不能大于结束日期", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}
			// System.out
			// .println(Double.parseDouble(tvGrouponStartTime2.getText()
			// .toString().substring(0, 2)
			// + tvGrouponStartTime2.getText().toString()
			// .substring(3, 5)));
			// System.out.println(Double.parseDouble(tvGrouponEndTime2.getText()
			// .toString().substring(0, 2)
			// + tvGrouponEndTime2.getText().toString().substring(3, 5)));

			new LoadGrouponUpdateTask(
					SharedPrefUtil
							.getToken(ProductIsGrouponUpdateActivity.this),
					id, strGrouponName, strGrouponPrice, strStartTime,
					strEndtime, strtvtuangourenshu).execute();
			break;
		case R.id.btnGrouponCancel:
			new LoadGrouponDeleteTask(SharedPrefUtil.getToken(this), id)
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
			tvGrouponCategory.setText(Constants.ProductCategory);
			break;

		default:
			break;
		}
	}

	/**
	 * 添加团购信息
	 * 
	 * @author Administrator
	 */
	class LoadGrouponUpdateTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;
		private String name;
		private String price;
		private String endTime;
		private String startTime;
		private String minimum;

		protected LoadGrouponUpdateTask(String accessToken, String id,
				String name, String price, String startTime, String endTime,
				String minimum) {
			this.accessToken = accessToken;
			this.id = id;
			this.name = name;
			this.price = price;
			this.startTime = startTime;
			this.endTime = endTime;
			this.minimum = minimum;
		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						ProductIsGrouponUpdateActivity.this, "正在修改团购……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().grouponUpdate(accessToken, id, name,
						price, startTime, endTime, minimum);
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
						Toast.makeText(ProductIsGrouponUpdateActivity.this,
								"修改成功", Toast.LENGTH_LONG).show();
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductIsGrouponUpdateActivity.this,
								"修改失败", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductIsGrouponUpdateActivity.this,
							"数据加载失败", Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductIsGrouponUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	/**
	 * 删除促销商品信息
	 * 
	 * @author Administrator
	 */
	class LoadGrouponDeleteTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;

		protected LoadGrouponDeleteTask(String accessToken, String id) {
			this.accessToken = accessToken;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().grouponDelete(accessToken, id);
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
						Toast.makeText(ProductIsGrouponUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductIsGrouponUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductIsGrouponUpdateActivity.this,
							"数据加载失败", Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductIsGrouponUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	/**
	 * 预览团购商品信息
	 * 
	 * @author Administrator
	 */
	class LoadGrouponViewTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadGrouponViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().GrouponView(id);
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
						grouponBean = JSON.parseObject(
								result.getJSONObject("groupon").toString(),
								GrouponBean.class);
						fillData();
						progressbar.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {

						Toast.makeText(ProductIsGrouponUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						progressbar.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductIsGrouponUpdateActivity.this,
							"数据加载失败", Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductIsGrouponUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}
}
