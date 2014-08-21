package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.CategoryBean;
import com.bangqu.yinwan.shop.bean.ShopBean;
import com.bangqu.yinwan.shop.bean.ShopStateBean;
import com.bangqu.yinwan.shop.helper.CategoryHelper;
import com.bangqu.yinwan.shop.helper.LocationHelper;
import com.bangqu.yinwan.shop.helper.ShopHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.CommonDialog;

public class ShopDetailActivity extends UIBaseActivity implements
		OnClickListener {
	private List<CategoryBean> categoryList = new ArrayList<CategoryBean>();

	private TextView tvbarleft;
	private TextView tvTittle;
	private Button btnRight;
	private LinearLayout llbarback;
	private TextView tvhangye;
	private TextView tvedit;// 编辑
	private TextView tvaddproduct;// 添加商品
	private TextView tvdetailshopname;// 店铺名称
	private TextView tvintegrity;// 信息完善度
	private ImageView ivdetailhead;
	private LinearLayout llhangye;
	ShopBean shopBean;
	String shopid = "";
	ShopStateBean shopStateBean;
	private TextView tvstateone;// 已加入个数
	private TextView tvstatezeo;// 待审核个数
	private TextView tvstatetwo;// 已驳回个数
	private LinearLayout llstateone;
	private LinearLayout llstatezeo;
	private LinearLayout llstatetwo;
	private ImageView ivred;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_detail_layout);
		findView();
		shopid = getIntent().getStringExtra("shopid");

	}

	@Override
	protected void onResume() {
		super.onResume();
		categoryList.clear();
		new LoadCategoryViewTask(
				SharedPrefUtil.getToken(ShopDetailActivity.this), getIntent()
						.getStringExtra("shopid")).execute();
		new LoadShopViewTask(getIntent().getStringExtra("shopid")).execute();
		new LoadDistributionCountTask(getIntent().getStringExtra("shopid"))
				.execute();
	}

	@Override
	public void findView() {
		super.findView();
		ivred = (ImageView) findViewById(R.id.ivred);
		llstateone = (LinearLayout) findViewById(R.id.llstateone);
		llstateone.setOnClickListener(this);
		llstatezeo = (LinearLayout) findViewById(R.id.llstatezeo);
		llstatezeo.setOnClickListener(this);
		llstatetwo = (LinearLayout) findViewById(R.id.llstatetwo);
		llstatetwo.setOnClickListener(this);
		tvstateone = (TextView) findViewById(R.id.tvstateone);
		tvstatezeo = (TextView) findViewById(R.id.tvstatezeo);
		tvstatetwo = (TextView) findViewById(R.id.tvstatetwo);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvbarleft = (TextView) findViewById(R.id.tvbarleft);
		tvbarleft.setText("切换");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("说明");
		btnRight.setOnClickListener(this);
		llbarback = (LinearLayout) findViewById(R.id.llbarback);
		llbarback.setOnClickListener(this);
		tvhangye = (TextView) findViewById(R.id.tvhangye);
		tvedit = (TextView) findViewById(R.id.tvedit);
		tvedit.setOnClickListener(this);
		tvaddproduct = (TextView) findViewById(R.id.tvaddproduct);
		tvaddproduct.setOnClickListener(this);
		tvdetailshopname = (TextView) findViewById(R.id.tvdetailshopname);
		ivdetailhead = (ImageView) findViewById(R.id.ivdetailhead);
		tvintegrity = (TextView) findViewById(R.id.tvintegrity);

		llhangye = (LinearLayout) findViewById(R.id.llhangye);
		llhangye.setOnClickListener(this);
	}

	@Override
	public void fillData() {
		super.fillData();
		switch (categoryList.size()) {
		case 0:
			tvhangye.setText("暂无店铺分类");
			break;
		case 1:
			tvhangye.setText(categoryList.get(0).getName());
			break;
		case 2:
			tvhangye.setText(categoryList.get(0).getName() + ","
					+ categoryList.get(1).getName());
			break;
		case 3:
			tvhangye.setText(categoryList.get(0).getName() + ","
					+ categoryList.get(1).getName() + ","
					+ categoryList.get(2).getName());
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnRight:
			Intent urlintent = new Intent(ShopDetailActivity.this,
					UrlWebView.class);
			urlintent.putExtra("url", "http://api.yinwan.bangqu.com/shop/rule");
			urlintent.putExtra("title", "使用说明");
			startActivity(urlintent);
			break;
		case R.id.tvedit:
			if (SharedPrefUtil.getShopBean(ShopDetailActivity.this).getId()
					.equals(getIntent().getStringExtra("shopid"))) {
				startActivity(new Intent(ShopDetailActivity.this,
						ShopInfomationActivity.class));
			} else {
				showAlertDialog();
			}
			break;
		case R.id.llbarback:
			finish();
			break;
		case R.id.llstateone:
			if (SharedPrefUtil.getShopBean(ShopDetailActivity.this).getId()
					.equals(getIntent().getStringExtra("shopid"))) {
				startActivity(new Intent(ShopDetailActivity.this,
						SearchXiaoQuActivity.class));

			} else {
				showAlertDialoglocation();
			}
			break;
		case R.id.llstatezeo:
			if (SharedPrefUtil.getShopBean(ShopDetailActivity.this).getId()
					.equals(getIntent().getStringExtra("shopid"))) {
				startActivity(new Intent(ShopDetailActivity.this,
						SearchXiaoQuActivity.class));

			} else {
				showAlertDialoglocation();
			}
			break;
		case R.id.llstatetwo:
			if (SharedPrefUtil.getShopBean(ShopDetailActivity.this).getId()
					.equals(getIntent().getStringExtra("shopid"))) {
				startActivity(new Intent(ShopDetailActivity.this,
						SearchXiaoQuActivity.class));

			} else {
				showAlertDialoglocation();
			}
			break;

		case R.id.tvaddproduct:
			if (SharedPrefUtil.getShopBean(ShopDetailActivity.this).getId()
					.equals(getIntent().getStringExtra("shopid"))) {
				startActivity(new Intent(ShopDetailActivity.this,
						ProductListActivity.class));
			} else {
				showAlertDialoglocation();
			}

			break;
		case R.id.llhangye:
			if (SharedPrefUtil.getShopBean(ShopDetailActivity.this).getId()
					.equals(getIntent().getStringExtra("shopid"))) {
				startActivity(new Intent(ShopDetailActivity.this,
						HomeFenLeiActivity.class));
			} else {
				showAlertDialoghangye();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 预览商店分类
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadCategoryViewTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String id;

		protected LoadCategoryViewTask(String accessToken, String id) {
			this.accessToken = accessToken;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CategoryHelper().ShopCategoryView(accessToken, id);

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

						List<CategoryBean> temp = CategoryBean
								.constractList(result
										.getJSONArray("categories"));
						categoryList.addAll(temp);
						fillData();

					} else if (result.getInt("status") == Constants.FAIL) {
						tvhangye.setText("暂无店铺分类");

					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	/**
	 * 预览小区个数
	 */
	class LoadDistributionCountTask extends AsyncTask<String, Void, JSONObject> {

		private String shopId;

		protected LoadDistributionCountTask(String shopId) {
			this.shopId = shopId;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new LocationHelper().distributioncount(shopId);

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
						shopStateBean = JSON.parseObject(
								result.getJSONObject("shopState").toString(),
								ShopStateBean.class);
						tvstateone.setText(shopStateBean.getOne() + "个小区");
						tvstatezeo.setText(shopStateBean.getZero() + "个小区");
						tvstatetwo.setText(shopStateBean.getNegativeTwo()
								+ "个小区");
						ivred.setVisibility(View.VISIBLE);
					} else if (result.getInt("status") == Constants.FAIL) {
						tvstateone.setText("暂未添加小区");
						tvstatezeo.setText("暂未添加小区");
						tvstatetwo.setText("暂未添加小区");
						ivred.setVisibility(View.GONE);

					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	/**
	 * 预览店铺信息
	 */
	class LoadShopViewTask extends AsyncTask<String, Void, JSONObject> {

		private String id;

		protected LoadShopViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopHelper().ShopView(id);
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
						shopBean = JSON.parseObject(result
								.getJSONObject("shop").toString(),
								ShopBean.class);
						tvdetailshopname.setText(shopBean.getName());
						((CommonApplication) getApplicationContext())
								.getImgLoader().DisplayImage(shopBean.getImg(),
										ivdetailhead);
						if (shopBean.getIntegrity().equals("100")) {
							tvintegrity.setTextColor(getResources().getColor(
									R.color.color_grey2));
						}
						tvintegrity.setText("资料完善度" + shopBean.getIntegrity()
								+ "%");
						tvTittle.setText(shopBean.getName());

						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("ShopDetailActivity", "JSONException");
				}
			} else {
				Log.i("ShopDetailActivity", "result==null");
			}

		}
	}

	public void showAlertDialog() {
		CommonDialog.Builder builder = new CommonDialog.Builder(this);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPrefUtil.setShopBean(ShopDetailActivity.this,
								shopBean);
						SharedPrefUtil.setVip(ShopDetailActivity.this, shopBean
								.getVip().toString());

						dialog.dismiss();
						startActivity(new Intent(ShopDetailActivity.this,
								ShopInfomationActivity.class));
					}
				});

		builder.create().show();

	}

	public void showAlertDialogproduct() {
		CommonDialog.Builder builder = new CommonDialog.Builder(this);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPrefUtil.setShopBean(ShopDetailActivity.this,
								shopBean);
						SharedPrefUtil.setVip(ShopDetailActivity.this, shopBean
								.getVip().toString());
						dialog.dismiss();
						startActivity(new Intent(ShopDetailActivity.this,
								ProductListActivity.class));
					}
				});

		builder.create().show();

	}

	public void showAlertDialoglocation() {
		CommonDialog.Builder builder = new CommonDialog.Builder(this);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPrefUtil.setShopBean(ShopDetailActivity.this,
								shopBean);
						SharedPrefUtil.setVip(ShopDetailActivity.this, shopBean
								.getVip().toString());
						dialog.dismiss();
						startActivity(new Intent(ShopDetailActivity.this,
								SearchXiaoQuActivity.class));
					}
				});

		builder.create().show();

	}

	public void showAlertDialoghangye() {
		CommonDialog.Builder builder = new CommonDialog.Builder(this);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});

		builder.setNegativeButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPrefUtil.setShopBean(ShopDetailActivity.this,
								shopBean);
						SharedPrefUtil.setVip(ShopDetailActivity.this, shopBean
								.getVip().toString());
						dialog.dismiss();
						startActivity(new Intent(ShopDetailActivity.this,
								HomeFenLeiActivity.class));

					}
				});

		builder.create().show();

	}
}
