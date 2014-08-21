package com.bangqu.yinwan.shop.ui;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

/**
 * 添加促销
 */
public class ProductUnPromotedUpdateActivity extends UIBaseActivity implements
		OnClickListener {
	private int YYYY;
	private int MM;
	private int DD;

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;
	private String id;
	private Button btnPromotionSaveUpdate;

	private TextView tvvippromotion;
	private EditText etPrommotionName;
	private TextView tvProductPrice;
	private EditText etPromotionPrice;
	private ImageView ivproduct;
	private TextView tvPromotionStartTime;
	private TextView tvPromotionEndTime;
	public static final int CATEGORY_SELECT = 11210;

	private ProductBean productBean;

	private TextView tvPromotionEndTime2;
	private TextView tvPromotionStartTime2;
	private TextView tvpromotioncategory;
	private int HH;
	private int MIN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productunpromotion_update_layout);

		findView();
		id = getIntent().getStringExtra("ProductId");
		new LoadProductViewTask(id).execute();
	}

	public void fillData() {
		super.fillData();
		if (!StringUtil.isBlank(productBean.getVipPrice())) {
			tvvippromotion.setText("￥" + productBean.getVipPrice());
		} else {
			tvvippromotion.setText("暂无");
		}
		if (!StringUtil.isBlank(productBean.getProductCategory().getName())) {
			tvpromotioncategory.setText("类别："
					+ productBean.getProductCategory().getName());
		}

		etPrommotionName.setText(productBean.getName());
		tvProductPrice.setText("￥" + productBean.getPrice());

		((CommonApplication) getApplicationContext()).getImgLoader()
				.DisplayImage(productBean.getImg(), ivproduct);
	}

	public void findView() {
		super.findView();

		tvPromotionEndTime2 = (TextView) findViewById(R.id.tvPromotionEndTime2);
		tvPromotionEndTime2.setOnClickListener(this);
		tvPromotionStartTime2 = (TextView) findViewById(R.id.tvPromotionStartTime2);
		tvPromotionStartTime2.setOnClickListener(this);
		tvpromotioncategory = (TextView) findViewById(R.id.tvpromotioncategory);
		tvvippromotion = (TextView) findViewById(R.id.tvvippromotion);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("添加促销商品");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);
		ivproduct = (ImageView) findViewById(R.id.ivproduct);
		btnPromotionSaveUpdate = (Button) findViewById(R.id.btnPromotionSaveUpdate);
		etPrommotionName = (EditText) findViewById(R.id.etPrommotionName);

		tvProductPrice = (TextView) findViewById(R.id.tvProductPrice);
		etPromotionPrice = (EditText) findViewById(R.id.etPromotionPrice);

		tvPromotionStartTime = (TextView) findViewById(R.id.tvPromotionStartTime);
		tvPromotionEndTime = (TextView) findViewById(R.id.tvPromotionEndTime);

		tvPromotionStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (StringUtil.isBlank(tvPromotionStartTime.getText()
						.toString().trim()
						+ "")) {
					Calendar c = Calendar.getInstance();
					YYYY = c.get(Calendar.YEAR);
					MM = c.get(Calendar.MONTH);
					DD = c.get(Calendar.DAY_OF_MONTH);
				} else if (tvPromotionStartTime.getText().length() == 10) {

					YYYY = Integer.parseInt(tvPromotionStartTime.getText()
							.toString().trim().substring(0, 4));
					MM = Integer.parseInt(tvPromotionStartTime.getText()
							.toString().trim().substring(5, 7)) - 1;
					DD = Integer.parseInt(tvPromotionStartTime.getText()
							.toString().trim().substring(8, 10));
				}

				// Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(ProductUnPromotedUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {

								tvPromotionStartTime.setText(year + "-"
										+ StringUtil.addzero(month + 1) + "-"
										+ StringUtil.addzero(dayOfMonth));
							}
						}
						// 设置初试日期
						, YYYY, MM, DD).show();
			}
		});

		tvPromotionEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (StringUtil.isBlank(tvPromotionEndTime.getText().toString()
						.trim()
						+ "")) {
					Calendar c = Calendar.getInstance();
					YYYY = c.get(Calendar.YEAR);
					MM = c.get(Calendar.MONTH);
					DD = c.get(Calendar.DAY_OF_MONTH);
				} else if (tvPromotionEndTime.getText().length() == 10) {
					System.out.println(tvPromotionEndTime.getText().toString()
							.trim().length());
					YYYY = Integer.parseInt(tvPromotionEndTime.getText()
							.toString().trim().substring(0, 4));
					MM = Integer.parseInt(tvPromotionEndTime.getText()
							.toString().trim().substring(5, 7)) - 1;
					DD = Integer.parseInt(tvPromotionEndTime.getText()
							.toString().trim().substring(8, 10));
				}

				// Calendar c = Calendar.getInstance();
				// 直接诶创建一个DatePickDialog对话框实例，并将他们显示出来
				new DatePickerDialog(ProductUnPromotedUpdateActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dayOfMonth) {

								tvPromotionEndTime.setText(year + "-"
										+ StringUtil.addzero(month + 1) + "-"
										+ StringUtil.addzero(dayOfMonth));
							}
						}
						// 设置初试日期
						, YYYY, MM, DD).show();
			}
		});

		btnPromotionSaveUpdate.setOnClickListener(this);
	}

	public static final int CHOOSE_CATEGORY = 1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tvPromotionEndTime2:
			Calendar cl = Calendar.getInstance();
			if (!StringUtil.isBlank(tvPromotionEndTime2.getText().toString())) {
				HH = Integer.parseInt(tvPromotionEndTime2.getText().toString()
						.substring(0, 2));
				MIN = Integer.parseInt(tvPromotionEndTime2.getText().toString()
						.substring(3, 5));
			} else {
				HH = cl.get(Calendar.HOUR_OF_DAY);
				MIN = cl.get(Calendar.MINUTE);
			}

			// 创建一个TimoPickerDialog实例，并把他们显示出来
			new TimePickerDialog(ProductUnPromotedUpdateActivity.this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker tp, int hourOfDay,
								int minute) {
							tvPromotionEndTime2.setText(StringUtil
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
		case R.id.tvPromotionStartTime2:
			Calendar c = Calendar.getInstance();
			if (!StringUtil.isBlank(tvPromotionStartTime2.getText().toString())) {
				HH = Integer.parseInt(tvPromotionStartTime2.getText()
						.toString().substring(0, 2));
				MIN = Integer.parseInt(tvPromotionStartTime2.getText()
						.toString().substring(3, 5));
			} else {
				HH = c.get(Calendar.HOUR_OF_DAY);
				MIN = c.get(Calendar.MINUTE);
			}
			new TimePickerDialog(ProductUnPromotedUpdateActivity.this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker tp, int hourOfDay,
								int minute) {
							tvPromotionStartTime2.setText(StringUtil
									.addzero(hourOfDay)
									+ ":"
									+ StringUtil.addzero(minute));
						}
					}, HH, MIN, true).show();
			break;
		case R.id.btnLeft:
			finish();
			break;

		case R.id.btnPromotionSaveUpdate:
			String strPromPrice = etPromotionPrice.getText().toString().trim();
			String strStarttime = tvPromotionStartTime.getText().toString()
					+ " " + tvPromotionStartTime2.getText().toString() + ":00";
			String strEndtime = tvPromotionEndTime.getText().toString() + " "
					+ tvPromotionEndTime2.getText().toString() + ":00";

			if (StringUtil.isBlank(strPromPrice)) {
				Toast.makeText(ProductUnPromotedUpdateActivity.this, "请输入促销价格",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Double.parseDouble(strPromPrice) >= Double
					.parseDouble(productBean.getPrice())) {
				Toast.makeText(ProductUnPromotedUpdateActivity.this,
						"促销价不能高于商品价", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvPromotionStartTime.getText().toString())) {
				Toast.makeText(ProductUnPromotedUpdateActivity.this,
						"请选择促销开始日期", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvPromotionStartTime2.getText().toString())) {
				Toast.makeText(ProductUnPromotedUpdateActivity.this,
						"请选择促销开始时间", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvPromotionEndTime.getText().toString())) {
				Toast.makeText(ProductUnPromotedUpdateActivity.this,
						"请选择促销结束日期", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvPromotionEndTime2.getText().toString())) {
				Toast.makeText(ProductUnPromotedUpdateActivity.this,
						"请选择促销结束时间", Toast.LENGTH_SHORT).show();
				return;
			}

			String strYY = (String) tvPromotionStartTime.getText()
					.subSequence(0, 4).toString().trim();
			String StrYY2 = (String) tvPromotionEndTime.getText()
					.subSequence(0, 4).toString().trim();
			String strMM = (String) tvPromotionStartTime.getText()
					.subSequence(5, 7).toString().trim();
			String strMM2 = (String) tvPromotionEndTime.getText()
					.subSequence(5, 7).toString().trim();
			String strDD = (String) tvPromotionStartTime.getText()
					.subSequence(8, 10).toString().trim();
			String strDD2 = (String) tvPromotionEndTime.getText()
					.subSequence(8, 10).toString().trim();
			if (Double.parseDouble(strYY) > Double.parseDouble(StrYY2)) {
				Toast.makeText(ProductUnPromotedUpdateActivity.this,
						"开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
				return;
			} else if (Double.parseDouble(strYY) == Double.parseDouble(StrYY2)) {
				if (Double.parseDouble(strMM) > Double.parseDouble(strMM2)) {
					Toast.makeText(ProductUnPromotedUpdateActivity.this,
							"开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
					return;
				} else if (Double.parseDouble(strMM) == Double
						.parseDouble(strMM2)) {
					if (Double.parseDouble(strDD) > Double.parseDouble(strDD2)) {
						Toast.makeText(ProductUnPromotedUpdateActivity.this,
								"开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
						return;
					} else {
						if (Double.parseDouble(tvPromotionStartTime2.getText()
								.toString().substring(0, 2)
								+ tvPromotionStartTime2.getText().toString()
										.substring(3, 5)) >= Double
								.parseDouble(tvPromotionEndTime2.getText()
										.toString().substring(0, 2)
										+ tvPromotionEndTime2.getText()
												.toString().substring(3, 5))) {

							Toast.makeText(
									ProductUnPromotedUpdateActivity.this,
									"开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				}
			}

			new LoadPromotionAddTask(
					SharedPrefUtil
							.getToken(ProductUnPromotedUpdateActivity.this),
					id, strPromPrice, strStarttime, strEndtime).execute();
			break;

		default:
			break;
		}
	}

	/**
	 * 添加促销信息
	 * 
	 * @author Administrator
	 */
	class LoadPromotionAddTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String productId;
		private String price;
		private String startTime;
		private String endTime;

		protected LoadPromotionAddTask(String accessToken, String productId,
				String price, String startTime, String endTime) {
			this.accessToken = accessToken;
			this.productId = productId;
			this.price = price;
			this.startTime = startTime;
			this.endTime = endTime;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						ProductUnPromotedUpdateActivity.this, "正在添加促销……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().addPromotion(accessToken, productId,
						price, startTime, endTime);
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
						Toast.makeText(ProductUnPromotedUpdateActivity.this,
								"添加成功", Toast.LENGTH_LONG).show();
						// Constants.ProductCategory = "";
						// Intent finishintent = new Intent(
						// ProductUnPromotedUpdateActivity.this,
						// PromotionManageActivity.class);
						// finishintent.putExtra("init", "0");
						// startActivity(finishintent);
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductUnPromotedUpdateActivity.this,
								"修改失败", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUnPromotedUpdateActivity.this,
							"数据加载失败", Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductUnPromotedUpdateActivity.this, "数据加载失败2",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	/**
	 * 预览未促销商品信息
	 * 
	 * @author Administrator
	 */
	class LoadProductViewTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadProductViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
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
					if (result.getInt("status") == Constants.SUCCESS) {
						productBean = JSON.parseObject(
								result.getJSONObject("product").toString(),
								ProductBean.class);
						fillData();
						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						Toast.makeText(ProductUnPromotedUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUnPromotedUpdateActivity.this,
							"数据加载失败", Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductUnPromotedUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

}
