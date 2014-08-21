package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.CategoryBean;
import com.bangqu.yinwan.shop.helper.CategoryHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

public class ProductCategoryListActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener {

	// 顶部title
	private TextView tvTittle;
	private Button btnLeft;

	private static final int CATEGORY_UPDATE = 12344;
	private String IntentValue = "";
	private TextView tvNoData;
	private ListView lvCategoryList;
	private MyListAdapter mylistAdapter;
	private List<CategoryBean> categoryList = new ArrayList<CategoryBean>();
	private List<String> categroyIdList = new ArrayList<String>();
	private ImageView ivadd;
	private Button btnRight;

	static class ViewHolder {
		TextView tvCategoryNo;
		TextView tvCategoryName;
		Button btnCategoryDel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_category_list_layout);
		IntentValue = getIntent().getStringExtra("IntentValue");
		findView();
		Constants.ProductCategory = "";
		Constants.productCategoryId = "";
	}

	public void fillData() {
		super.fillData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		categoryList.clear();
		categroyIdList.clear();
		new LoadCategoryListTask(SharedPrefUtil.getShopBean(this).getId())
				.execute();
	}

	public void findView() {
		super.findView();
		ivadd = (ImageView) findViewById(R.id.ivadd);
		ivadd.setOnClickListener(this);
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("商品服务分类");
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		tvNoData = (TextView) findViewById(R.id.tvNoData);

		// ListView
		lvCategoryList = (ListView) findViewById(R.id.lvCategoryList);
		mylistAdapter = new MyListAdapter(this);
		lvCategoryList.setAdapter(mylistAdapter);
		lvCategoryList.setOnItemClickListener(this);

		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case CATEGORY_ADD:
			categoryList.clear();
			mylistAdapter.notifyDataSetChanged();
			new LoadCategoryListTask(SharedPrefUtil.getShopBean(this).getId())
					.execute();
			break;
		case CATEGORY_UPDATE:
			new LoadCategoryListTask(SharedPrefUtil.getShopBean(this).getId())
					.execute();
			break;

		default:
			break;

		}
	}

	public static final int CATEGORY_ADD = 523;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.ivadd:
			Intent intentSearch = new Intent(ProductCategoryListActivity.this,
					ProductCategoryAddActivity.class);
			startActivityForResult(intentSearch, CATEGORY_ADD);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (IntentValue.equals("notfromhome")) {
			// 将分类名称和分类ID存入常量中
			Constants.ProductCategory = categoryList.get(arg2).getName();
			Constants.productCategoryId = categoryList.get(arg2).getId();
			finish();
		} else if (IntentValue.equals("fromhome")) {
			Intent updateintent = new Intent(ProductCategoryListActivity.this,
					ProductCategoryUpdateActivity.class);
			updateintent.putExtra("CategoryID", categoryList.get(arg2).getId());
			startActivityForResult(updateintent, CATEGORY_UPDATE);
		}

	}

	/**
	 * 商品分类列表适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		CategoryBean categoryBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return categoryList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.product_category_item_layout, null);

				viewHolder = new ViewHolder();
				viewHolder.tvCategoryNo = (TextView) convertView
						.findViewById(R.id.tvCategoryNo);
				viewHolder.tvCategoryName = (TextView) convertView
						.findViewById(R.id.tvCategoryName);
				viewHolder.btnCategoryDel = (Button) convertView
						.findViewById(R.id.btnCategoryDel);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			categoryBean = categoryList.get(position);

			viewHolder.tvCategoryNo.setText(position + 1 + ".");
			viewHolder.tvCategoryName.setText(categoryBean.getName());

			viewHolder.btnCategoryDel.setTag(position);
			viewHolder.btnCategoryDel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final int index = (Integer) v.getTag();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ProductCategoryListActivity.this);
					builder.setTitle("删除")
							.setMessage("确定要删除该分类吗？")
							.setPositiveButton("是",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											new LoadProductCategoryDelTask(
													SharedPrefUtil
															.getToken(ProductCategoryListActivity.this),
													categoryList.get(index)
															.getId() + "")
													.execute();
											categoryList.remove(index);
											categroyIdList.remove(index);
											mylistAdapter
													.notifyDataSetChanged();

										}
									}).setNegativeButton("否", null);

					AlertDialog alert = builder.create();// 创建对话框
					alert.show();// 显示对话框
				}
			});

			return convertView;
		}

	}

	/**
	 * 商品分类列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadCategoryListTask extends AsyncTask<String, Void, JSONObject> {
		private String shopId;

		protected LoadCategoryListTask(String shopId) {
			this.shopId = shopId;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CategoryHelper().ProductCategoryList(shopId);
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
										.getJSONArray("productCategories"));
						categoryList = temp;
						categroyIdList.clear();
						for (CategoryBean categoryBean : categoryList) {
							categroyIdList.add(categoryBean.getId());
						}

						mylistAdapter.notifyDataSetChanged();
						lvCategoryList.setVisibility(View.VISIBLE);
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						lvCategoryList.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductCategoryListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ProductCategoryListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Toast.makeText(ProductCategoryListActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}
	}

	/**
	 * 删除商品分类
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadProductCategoryDelTask extends
			AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;

		protected LoadProductCategoryDelTask(String accessToken, String id) {
			this.accessToken = accessToken;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CategoryHelper().ProductCategoryDel(accessToken, id);
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
						Toast.makeText(ProductCategoryListActivity.this,
								"删除成功", Toast.LENGTH_LONG).show();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductCategoryListActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductCategoryListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductCategoryListActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

}
