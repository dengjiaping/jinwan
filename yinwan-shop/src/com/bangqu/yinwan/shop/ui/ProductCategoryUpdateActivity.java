package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.CategoryBean;
import com.bangqu.yinwan.shop.helper.CategoryHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class ProductCategoryUpdateActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;
	private String ID;

	private Button btnSaveCategory;
	private EditText etCategoryNameup;
	private EditText etCategoryDetailup;

	private CategoryBean categoryBean;

	static class ViewHolder {
		TextView tvCategoryNo;
		TextView tvCategoryName;
		ImageView ivCategoryDel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_category_update_layout);
		ID = getIntent().getStringExtra("CategoryID");
		System.out.println(ID + "ID");
		System.out.println(getIntent().getStringExtra("CategoryID")
				+ "getIntent().getStringExtra");

		findView();

		new LoadroductCategoryViewTask(
				SharedPrefUtil.getToken(ProductCategoryUpdateActivity.this), ID)
				.execute();
	}

	public void fillData() {
		// TODO Auto-generated method stub
		etCategoryNameup.setText(categoryBean.getName());
		System.out.println(categoryBean.getName() + "categoryBean.getName()");

		etCategoryDetailup.setText(categoryBean.getDescription());
		System.out.println(categoryBean.getDescription()
				+ "categoryBean.getDescription()");
		progressbar.setVisibility(View.GONE);
	}

	public void findView() {
		super.findView();
		// TODO Auto-generated method stub
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("商品分类");
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("清空");
		btnSaveCategory = (Button) findViewById(R.id.btnSaveCategory);

		etCategoryNameup = (EditText) findViewById(R.id.etCategoryNameup);
		etCategoryDetailup = (EditText) findViewById(R.id.etCategoryDetailup);

		btnSaveCategory.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		btnLeft.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnRight:
			etCategoryNameup.setText("");
			etCategoryNameup.setText("");
			break;
		case R.id.btnSaveCategory:
			String stretCategoryName = etCategoryNameup.getText().toString()
					.trim();
			String stretCategoryDetail = etCategoryNameup.getText().toString()
					.trim();
			if (StringUtil.isBlank(stretCategoryName)) {
				Toast.makeText(ProductCategoryUpdateActivity.this, "分类名称不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			new LoadProductCategoryUpdateTask(SharedPrefUtil.getToken(this),
					ID, stretCategoryName, stretCategoryDetail).execute();
			break;

		default:
			break;
		}
	}

	/**
	 * 预览商品分类
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadroductCategoryViewTask extends
			AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;

		protected LoadroductCategoryViewTask(String accessToken, String id) {
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
				return new CategoryHelper()
						.ProductCategoryView(accessToken, id);
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
						categoryBean = JSON.parseObject(
								result.getJSONObject("productcategory")
										.toString(), CategoryBean.class);
						System.out.println("接口执行");
						fillData();

						progressbar.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductCategoryUpdateActivity.this,
							"数据加载失败", Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductCategoryUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	/**
	 * 修改商品分类
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadProductCategoryUpdateTask extends
			AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String id;
		private String name;
		private String description;

		protected LoadProductCategoryUpdateTask(String accessToken, String id,
				String name, String description) {
			this.accessToken = accessToken;
			this.id = id;
			this.name = name;
			this.description = description;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						ProductCategoryUpdateActivity.this, "正在修改……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CategoryHelper().ProductCategoryUpdate(accessToken,
						id, name, description);
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
						Toast.makeText(ProductCategoryUpdateActivity.this,
								"修改成功", Toast.LENGTH_LONG).show();
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductCategoryUpdateActivity.this,
								"修改失败", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductCategoryUpdateActivity.this,
							"数据加载失败", Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductCategoryUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

}
