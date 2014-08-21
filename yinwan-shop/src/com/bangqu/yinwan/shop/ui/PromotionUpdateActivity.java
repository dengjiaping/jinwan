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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.PromotionBean;
import com.bangqu.yinwan.shop.helper.PromotionHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class PromotionUpdateActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;

	private String id;

	private Boolean isPromotion;

	private Button btnPromCancel;
	private Button btnPromSaveUpdate;

	private EditText etPromName;
	private TextView tvPromCategory;
	private EditText etPromPrice;
	private LinearLayout llPromCategory;
	private EditText etPromDetail;

	// Spinner
	private static final String[] mAPDU = { "个", "件", "盒", "斤", "公斤", "克", "箱",
			"包", "项", "份", "瓶", "次", "只", "套", "台", "条", "两", "打", "组" };
	private Spinner Spinnerunit;
	private String selectunit = "";
	private ArrayAdapter<String> adapter;
	private static int mApduIndex = -1;

	private TextView tvPromStartTime;
	private TextView tvPromEndTime;
	private TextView tvPromStartTime2;
	private TextView tvPromEndTime2;

	private String CategoryName;
	private String newStr = "";

	private PromotionBean promotionBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promotion_update_layout);

		findView();
		id = getIntent().getStringExtra("PromotionId");
		new LoadPromotionViewTask(id).execute();
		// fillData();
	}

	public void fillData() {
		// TODO Auto-generated method stub
		super.fillData();

		etPromName.setText(promotionBean.getProduct().getName());
		tvPromCategory.setText(promotionBean.getProduct().getProductCategory()
				.getName());
		etPromPrice.setText(promotionBean.getPrice());
		tvPromStartTime.setText(promotionBean.getAddTime().substring(0, 10));
		tvPromStartTime2.setText(promotionBean.getAddTime().substring(11,
				promotionBean.getAddTime().length()));
		tvPromEndTime.setText(promotionBean.getEndTime().substring(0, 10));
		tvPromEndTime2.setText(promotionBean.getEndTime().substring(11,
				promotionBean.getEndTime().length()));
		etPromDetail.setText(promotionBean.getProduct().getContent());

		// etProductPrice.setText(promotionBean.getPrice());
		// if
		// (StringUtil.isBlank(promotionBean.getProduct().getProductCategory()
		// .toString())) {
		// tvPromCategory.setText(Constants.ProductCategory);
		// } else {
		// tvPromCategory.setText(promotionBean.getProduct()
		// .getProductCategory().toString());
		// }

	}

	public void findView() {
		super.findView();
		// TODO Auto-generated method stub
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("修改商品信息");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnPromSaveUpdate = (Button) findViewById(R.id.btnPromSaveUpdate);
		btnPromCancel = (Button) findViewById(R.id.btnPromCancel);

		etPromName = (EditText) findViewById(R.id.etPromName);
		tvPromCategory = (TextView) findViewById(R.id.tvPromCategory);
		// tvProductCategory.setOnClickListener(this);
		llPromCategory = (LinearLayout) findViewById(R.id.llPromCategory);
		llPromCategory.setOnClickListener(this);

		etPromPrice = (EditText) findViewById(R.id.etPromPrice);
		Spinnerunit = (Spinner) findViewById(R.id.Spinnerunit);
		// 将可选内容与ArrayAdapter连接起来
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mAPDU);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		Spinnerunit.setAdapter(adapter);
		// 添加事件Spinner事件监听
		Spinnerunit.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectunit = Spinnerunit.getSelectedItem().toString();
				newStr = selectunit.substring(1, selectunit.length());
				// System.out.println(newStr);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		tvPromStartTime = (TextView) findViewById(R.id.tvPromStartTime);
		tvPromEndTime = (TextView) findViewById(R.id.tvPromEndTime);
		tvPromStartTime2 = (TextView) findViewById(R.id.tvPromStartTime2);
		tvPromEndTime2 = (TextView) findViewById(R.id.tvPromEndTime2);

		etPromDetail = (EditText) findViewById(R.id.etPromDetail);

		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);

		tvPromStartTime.setOnClickListener(this);
		tvPromStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(PromotionUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {
								// TODO Auto-generated method stub
								tvPromStartTime.setText(year + "-"
										+ (month + 1) + "-" + dayOfMonth);
							}
						}
						// 设置初试日期
						, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		// 为“设置时间”按钮绑定监听器
		tvPromStartTime2.setOnClickListener(this);
		tvPromStartTime2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 创建一个TimoPickerDialog实例，并把他们显示出来
				new TimePickerDialog(PromotionUpdateActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {
								// TODO Auto-generated method stub
								tvPromStartTime2.setText(hourOfDay + ":"
										+ minute + ":00");
							}
						}
						// 设置初始时间
						, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)
						// true表示采用24小时制
						, true).show();
			}
		});
		tvPromEndTime.setOnClickListener(this);
		tvPromEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(PromotionUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {
								// TODO Auto-generated method stub

								tvPromEndTime.setText(year + "-" + (month + 1)
										+ "-" + dayOfMonth + " ");
							}
						}
						// 设置初试日期
						, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		// 为“设置时间”按钮绑定监听器
		tvPromEndTime2.setOnClickListener(this);
		tvPromEndTime2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 创建一个TimoPickerDialog实例，并把他们显示出来
				new TimePickerDialog(PromotionUpdateActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {
								// TODO Auto-generated method stub
								tvPromEndTime2.setText(hourOfDay + ":" + minute
										+ ":00");
							}
						}
						// 设置初始时间
						, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)
						// true表示采用24小时制
						, true).show();
			}
		});

		btnPromSaveUpdate.setOnClickListener(this);
		btnPromCancel.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tvPromCategory.setText(Constants.ProductCategory);
	}

	public static final int CHOOSE_CATEGORY = 1;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlshop:
			Intent intentshop = new Intent(PromotionUpdateActivity.this,
					HomeMoreShopActivity.class);
			startActivity(intentshop);
			break;

		case R.id.btnLeft:
			finish();
			break;

		case R.id.llProductCategory:
			Intent categoryintent = new Intent(PromotionUpdateActivity.this,
					ProductCategoryListActivity.class);
			categoryintent.putExtra("IntentValue", "FromProductUpdate");
			startActivity(categoryintent);
			break;
		case R.id.btnPromCancel:
			new LoadPromotionDeleteTask(
					SharedPrefUtil.getToken(PromotionUpdateActivity.this), id)
					.execute();
			break;

		case R.id.btnPromSaveUpdate:
			String strPromName = etPromName.getText().toString().trim();
			String strPromCategory = tvPromCategory.getText().toString().trim();
			// String strpromCategoryId = Constants.productCategoryId;
			String strPromPrice = etPromPrice.getText().toString().trim();
			String strPromDetail = etPromDetail.getText().toString().trim();

			String strAddtime = tvPromStartTime.getText().toString().trim()
					+ " " + tvPromStartTime2.getText().toString().trim();
			String strEndtime = tvPromEndTime.getText().toString().trim() + " "
					+ tvPromEndTime2.getText().toString().trim();
			String strUnit = newStr;
			if (StringUtil.isBlank(strPromName)) {
				Toast.makeText(PromotionUpdateActivity.this, "请填写商品名称",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strPromPrice)) {
				Toast.makeText(PromotionUpdateActivity.this, "请输入商品价格",
						Toast.LENGTH_LONG).show();
				return;
			}

			new LoadPromotionUpdateTask(SharedPrefUtil.getToken(this), id,
					strAddtime, strEndtime, strPromPrice, strUnit).execute();
			break;

		default:
			break;
		}
	}

	/**
	 * 修改促销信息
	 * 
	 * @author Administrator
	 */
	class LoadPromotionUpdateTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;
		private String addTime;
		private String endTime;
		private String price;
		private String unit;

		protected LoadPromotionUpdateTask(String accessToken, String id,
				String addTime, String endTime, String price, String unit) {
			this.accessToken = accessToken;
			this.id = id;
			this.addTime = addTime;
			this.endTime = endTime;
			this.price = price;
			this.unit = unit;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						PromotionUpdateActivity.this, "正在保存修改……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new PromotionHelper().PromotionUpdate(accessToken, id,
						addTime, endTime, price, unit);
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
						Toast.makeText(PromotionUpdateActivity.this, "修改成功",
								Toast.LENGTH_LONG).show();
						Constants.ProductCategory = "";
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(PromotionUpdateActivity.this, "修改失败",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(PromotionUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(PromotionUpdateActivity.this, "数据加载失败2",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	/**
	 * 预览促销信息
	 * 
	 * @author Administrator
	 */
	class LoadPromotionViewTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadPromotionViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new PromotionHelper().PromotionView(id);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// Log.i("result", result + "");
			if (result != null) {

				try {
					// System.out.println(result);
					if (result.getInt("status") == Constants.SUCCESS) {
						promotionBean = JSON.parseObject(
								result.getJSONObject("promotion").toString(),
								PromotionBean.class);
						fillData();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(PromotionUpdateActivity.this, "msg",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(PromotionUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(PromotionUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	/**
	 * 删除促销
	 * 
	 * @author Administrator
	 */
	class LoadPromotionDeleteTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;

		protected LoadPromotionDeleteTask(String accessToken, String id) {
			this.accessToken = accessToken;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new PromotionHelper().PromotionDel(accessToken, id);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// Log.i("result", result + "");
			if (result != null) {

				try {
					System.out.println(result);
					if (result.getInt("status") == Constants.SUCCESS) {
						// promotionBean = JSON.parseObject(
						// result.getJSONObject("promotion").toString(),
						// PromotionBean.class);
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(PromotionUpdateActivity.this, "msg",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(PromotionUpdateActivity.this, "数据加载失败11",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(PromotionUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

}
