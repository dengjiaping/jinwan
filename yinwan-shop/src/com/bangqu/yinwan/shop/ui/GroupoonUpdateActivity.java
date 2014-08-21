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
import com.bangqu.yinwan.shop.bean.GrouponBean;
import com.bangqu.yinwan.shop.helper.GrouponHelper;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.helper.PromotionHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class GroupoonUpdateActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;

	private String id;

	private Boolean isGroupon;

	private Button btnGrouponCancel;
	private Button btnGrouponSaveUpdate;

	private EditText etGrouponName;
	private TextView tvGrouponCategory;
	private EditText etGrouponPrice;
	private LinearLayout llGrouponCategory;
	private EditText etGrouponDetail;
	private EditText etMinimum;

	// Spinner
	private static final String[] mAPDU = { "/个", "/件", "/盒", "/斤", "/公斤",
			"/克", "/箱", "/包", "/项", "/份", "/瓶", "/次", "/只", "/套", "/台", "/条",
			"/两", "/打", "/组" };
	private Spinner Spinnerunit;
	private String selectunit = "";
	private ArrayAdapter<String> adapter;
	private static int mApduIndex = -1;

	private TextView tvGrouponStartTime;
	private TextView tvGrouponEndTime;
	private TextView tvGrouponStartTime2;
	private TextView tvGrouponEndTime2;

	private String CategoryName;
	private String newStr = "";

	private GrouponBean grouponBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupon_update_layout);

		findView();
		id = getIntent().getStringExtra("GrouponId");
		new LoadGrouponViewTask(id).execute();
		// fillData();
	}

	public void fillData() {
		// TODO Auto-generated method stub
		super.fillData();
		etGrouponName.setText(grouponBean.getProduct().getName());
		tvGrouponCategory.setText(grouponBean.getProduct().getProductCategory()
				.getName());
		etGrouponPrice.setText(grouponBean.getPrice());
		tvGrouponStartTime.setText(grouponBean.getAddTime().substring(0, 10));
		tvGrouponStartTime2.setText(grouponBean.getAddTime().substring(11,
				grouponBean.getAddTime().length()));
		tvGrouponEndTime.setText(grouponBean.getAddTime().substring(0, 10));
		tvGrouponEndTime2.setText(grouponBean.getEndTime().substring(11,
				grouponBean.getEndTime().length()));
		etGrouponDetail.setText(grouponBean.getProduct().getContent());
		etMinimum.setText(grouponBean.getMinimum());

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
		tvTittle.setText("修改团购信息");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnGrouponSaveUpdate = (Button) findViewById(R.id.btnGrouponSaveUpdate);
		btnGrouponCancel = (Button) findViewById(R.id.btnGrouponCancel);

		etGrouponName = (EditText) findViewById(R.id.etProductName);
		tvGrouponCategory = (TextView) findViewById(R.id.tvGrouponCategory);
		// tvProductCategory.setOnClickListener(this);
		llGrouponCategory = (LinearLayout) findViewById(R.id.llGrouponCategory);
		llGrouponCategory.setOnClickListener(this);

		etGrouponPrice = (EditText) findViewById(R.id.etGrouponPrice);
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

		tvGrouponStartTime = (TextView) findViewById(R.id.tvGrouponStartTime);
		tvGrouponEndTime = (TextView) findViewById(R.id.tvGrouponEndTime);
		tvGrouponStartTime2 = (TextView) findViewById(R.id.tvGrouponStartTime2);
		tvGrouponEndTime2 = (TextView) findViewById(R.id.tvGrouponEndTime2);

		etGrouponDetail = (EditText) findViewById(R.id.etGrouponDetail);
		etMinimum = (EditText) findViewById(R.id.etMinimum);

		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);

		tvGrouponStartTime.setOnClickListener(this);
		tvGrouponStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(GroupoonUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {
								// TODO Auto-generated method stub
								tvGrouponStartTime.setText(year + "-"
										+ (month + 1) + "-" + dayOfMonth);
							}
						}
						// 设置初试日期
						, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		// 为“设置时间”按钮绑定监听器
		tvGrouponStartTime2.setOnClickListener(this);
		tvGrouponStartTime2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 创建一个TimoPickerDialog实例，并把他们显示出来
				new TimePickerDialog(GroupoonUpdateActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {
								// TODO Auto-generated method stub
								tvGrouponStartTime2.setText(hourOfDay + ":"
										+ minute + ":00");
							}
						}
						// 设置初始时间
						, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)
						// true表示采用24小时制
						, true).show();
			}
		});
		tvGrouponEndTime.setOnClickListener(this);
		tvGrouponEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(GroupoonUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {
								// TODO Auto-generated method stub

								tvGrouponEndTime.setText(year + "-"
										+ (month + 1) + "-" + dayOfMonth + " ");
							}
						}
						// 设置初试日期
						, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		// 为“设置时间”按钮绑定监听器
		tvGrouponEndTime2.setOnClickListener(this);
		tvGrouponEndTime2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 创建一个TimoPickerDialog实例，并把他们显示出来
				new TimePickerDialog(GroupoonUpdateActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {
								// TODO Auto-generated method stub
								tvGrouponEndTime2.setText(hourOfDay + ":"
										+ minute + ":00");
							}
						}
						// 设置初始时间
						, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)
						// true表示采用24小时制
						, true).show();
			}
		});

		btnGrouponSaveUpdate.setOnClickListener(this);
		btnGrouponCancel.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tvGrouponCategory.setText(Constants.ProductCategory);
	}

	public static final int CHOOSE_CATEGORY = 1;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlshop:
			Intent intentshop = new Intent(GroupoonUpdateActivity.this,
					HomeMoreShopActivity.class);
			startActivity(intentshop);
			break;

		case R.id.btnLeft:
			finish();
			break;

		case R.id.llProductCategory:
			Intent categoryintent = new Intent(GroupoonUpdateActivity.this,
					ProductCategoryListActivity.class);
			categoryintent.putExtra("IntentValue", "FromProductUpdate");
			startActivity(categoryintent);
			break;
		case R.id.btnGrouponCancel:
			new LoadGrouponDeleteTask(
					SharedPrefUtil.getToken(GroupoonUpdateActivity.this), id)
					.execute();
			break;

		case R.id.btnGrouponSaveUpdate:
			String strPromName = etGrouponName.getText().toString().trim();
			String strPromCategory = tvGrouponCategory.getText().toString()
					.trim();
			// String strpromCategoryId = Constants.productCategoryId;
			String strPromPrice = etGrouponPrice.getText().toString().trim();
			String strPromDetail = etGrouponDetail.getText().toString().trim();

			String strAddtime = tvGrouponStartTime.getText().toString().trim()
					+ " " + tvGrouponStartTime2.getText().toString().trim();
			String strEndtime = tvGrouponEndTime.getText().toString().trim()
					+ " " + tvGrouponEndTime2.getText().toString().trim();
			String strMinimum = etMinimum.getText().toString().trim();
			if (StringUtil.isBlank(strPromName)) {
				Toast.makeText(GroupoonUpdateActivity.this, "请填写商品名称",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strPromPrice)) {
				Toast.makeText(GroupoonUpdateActivity.this, "请输入商品价格",
						Toast.LENGTH_LONG).show();
				return;
			}

			new LoadGrouponUpdateTask(SharedPrefUtil.getToken(this), id,
					strAddtime, strEndtime, strPromPrice, strMinimum).execute();
			break;

		default:
			break;
		}
	}

	/**
	 * 修改团购信息
	 * 
	 * @author Administrator
	 */
	class LoadGrouponUpdateTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;
		private String addTime;
		private String endTime;
		private String price;
		private String minimum;

		protected LoadGrouponUpdateTask(String accessToken, String id,
				String addTime, String endTime, String price, String minimum) {
			this.accessToken = accessToken;
			this.id = id;
			this.addTime = addTime;
			this.endTime = endTime;
			this.price = price;
			this.minimum = minimum;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						GroupoonUpdateActivity.this, "正在添加……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new GrouponHelper().GrouponUpdate(accessToken, id,
						addTime, endTime, price, minimum);
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
						Toast.makeText(GroupoonUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						Constants.ProductCategory = "";
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(GroupoonUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(GroupoonUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(GroupoonUpdateActivity.this, "数据加载失败2",
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
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new GrouponHelper().GrouponDeail(id);
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
					System.out.println("1111");
					if (result.getInt("status") == Constants.SUCCESS) {
						System.out.println("2222");
						grouponBean = JSON.parseObject(
								result.getJSONObject("groupon").toString(),
								GrouponBean.class);
						System.out.println("3333");
						fillData();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(GroupoonUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(GroupoonUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(GroupoonUpdateActivity.this, "数据加载失败",
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
	class LoadGrouponDeleteTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;

		protected LoadGrouponDeleteTask(String accessToken, String id) {
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
				return new GrouponHelper().GrouponDel(accessToken, id);
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
						Toast.makeText(GroupoonUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(GroupoonUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(GroupoonUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(GroupoonUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

}
