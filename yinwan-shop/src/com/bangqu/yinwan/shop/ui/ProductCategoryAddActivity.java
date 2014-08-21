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

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.helper.CategoryHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class ProductCategoryAddActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;

	private Button btnSaveCategory;
	private EditText etCategoryName;
	private EditText etCategoryDetail;

	static class ViewHolder {
		TextView tvCategoryNo;
		TextView tvCategoryName;
		ImageView ivCategoryDel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_category_add_layout);

		findView();
		fillData();
	}

	public void fillData() {

	}

	public void findView() {
		super.findView();
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("商品服务分类");
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("清空");
		btnSaveCategory = (Button) findViewById(R.id.btnSaveCategory);

		etCategoryName = (EditText) findViewById(R.id.etCategoryName);
		etCategoryDetail = (EditText) findViewById(R.id.etCategoryDetail);

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
			etCategoryName.setText("");
			etCategoryDetail.setText("");
			break;
		case R.id.btnSaveCategory:
			String stretCategoryName = etCategoryName.getText().toString()
					.trim();
			String stretCategoryDetail = etCategoryDetail.getText().toString()
					.trim();
			if (StringUtil.isBlank(stretCategoryName)) {
				Toast.makeText(ProductCategoryAddActivity.this, "分类名称不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}

			new LoadProductCategoryAddTask(SharedPrefUtil.getToken(this),
					SharedPrefUtil.getShopBean(this).getId(),
					stretCategoryName, stretCategoryDetail).execute();

			break;

		default:
			break;
		}
	}

	/**
	 * 添加商品分类
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadProductCategoryAddTask extends
			AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String shopId;
		private String name;
		private String description;

		protected LoadProductCategoryAddTask(String accessToken, String shopId,
				String name, String description) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.name = name;
			this.description = description;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						ProductCategoryAddActivity.this, "正在添加……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CategoryHelper().ProductCategoryAdd(accessToken,
						shopId, name, description);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			// Log.i("result", result + "");
			if (pd != null)
				pd.dismiss();
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(ProductCategoryAddActivity.this, "添加成功",
								Toast.LENGTH_LONG).show();
						// setResult(RESULT_OK);
						setResult(523);
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductCategoryAddActivity.this,
								"不能重复添加分类", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductCategoryAddActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductCategoryAddActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

}
