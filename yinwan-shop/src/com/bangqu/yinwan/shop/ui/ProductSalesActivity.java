package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.bean.ProductImgBean;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class ProductSalesActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;

	private TextView tvNoData;

	private XListView XlvproductSalesList;
	private MyListAdapter mylistAdapter;
	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;

	private List<ProductImgBean> productImgList = new ArrayList<ProductImgBean>();
	private List<ProductBean> productList = new ArrayList<ProductBean>();
	private List<String> productIdList = new ArrayList<String>();

	static class ViewHolder {
		TextView tvSalesNo;
		ImageView ivproductImg;
		TextView tvProductName;
		TextView tvProductCategory;
		TextView tvProductPrice;
		ImageView ivtuangou;
		ImageView ivtuangou2;
		ImageView ivcuxiao;
		ImageView ivcuxiao2;
		TextView tvMonthSales;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_sales_list_layout);

		findView();
		productIdList.clear();
		new LoadProductSalesTask(SharedPrefUtil.getToken(this), SharedPrefUtil
				.getShopBean(this).getId(), "true", "monthSales", true, begin)
				.execute();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void findView() {
		super.findView();
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("销量排行");
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);
		tvNoData = (TextView) findViewById(R.id.tvNoData);

		XlvproductSalesList = (XListView) findViewById(R.id.XlvproductSalesList);
		mylistAdapter = new MyListAdapter(this);
		XlvproductSalesList.setPullLoadEnable(true);
		XlvproductSalesList.setAdapter(mylistAdapter);
		XlvproductSalesList.setOnItemClickListener(this);
		XlvproductSalesList.setXListViewListener(this);
		mHandler = new Handler();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 商品销量排行列表适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		ProductBean productBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return productList.size();
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
						R.layout.product_sales_item_layout, null);

				viewHolder = new ViewHolder();
				viewHolder.tvSalesNo = (TextView) convertView
						.findViewById(R.id.tvSalesNo);
				viewHolder.ivproductImg = (ImageView) convertView
						.findViewById(R.id.ivproductImg);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.tvProductCategory = (TextView) convertView
						.findViewById(R.id.tvProductCategory);
				viewHolder.tvProductPrice = (TextView) convertView
						.findViewById(R.id.tvProductPrice);
				viewHolder.ivtuangou = (ImageView) convertView
						.findViewById(R.id.ivtuangou);
				viewHolder.ivtuangou2 = (ImageView) convertView
						.findViewById(R.id.ivtuangou2);
				viewHolder.ivcuxiao = (ImageView) convertView
						.findViewById(R.id.ivcuxiao);
				viewHolder.ivcuxiao2 = (ImageView) convertView
						.findViewById(R.id.ivcuxiao2);

				viewHolder.tvMonthSales = (TextView) convertView
						.findViewById(R.id.tvMonthSales);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			productBean = productList.get(position);
			viewHolder.tvSalesNo.setText(position + 1 + ".");
			((CommonApplication) getApplicationContext())
					.getImgLoader()
					.DisplayImage(productBean.getImg(), viewHolder.ivproductImg);
			viewHolder.tvProductName.setText(productBean.getName());

			if (!(productBean.getProductCategory() + "").equals("null")) {
				viewHolder.tvProductCategory.setText("类别："
						+ productBean.getProductCategory().getName());
			} else {
				viewHolder.tvProductCategory.setText("暂无分类");
			}
			if (productBean.getIsPromotion().equals("true")) {
				viewHolder.ivcuxiao.setVisibility(View.GONE);
				viewHolder.ivcuxiao2.setVisibility(View.VISIBLE);
			}
			if (productBean.getIsGroupon().equals("true")) {
				viewHolder.ivtuangou.setVisibility(View.GONE);
				viewHolder.ivtuangou2.setVisibility(View.VISIBLE);
			}

			viewHolder.tvProductPrice.setText(productBean.getPrice());

			viewHolder.tvMonthSales.setText("月销量："
					+ productBean.getMonthSales());
			// 件数
			// + productBean.getUnit().substring(1,
			// productBean.getUnit().length())
			// System.out.println(productBean.getUnit());
			// System.out.println(productBean.getUnit().substring(1,
			// productBean.getUnit().length()));

			return convertView;
		}
	}

	/**
	 * 商品销量排行列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadProductSalesTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String enabled;
		private String order;
		private Boolean desc;
		private int begin;

		protected LoadProductSalesTask(String accessToken, String shopId,
				String enabled, String order, Boolean desc, int begin) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.enabled = enabled;
			this.order = order;
			this.desc = desc;
			this.begin = begin;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productSales(accessToken, shopId,
						enabled, order, desc, begin);
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
						List<ProductBean> temp = ProductBean
								.constractList(result.getJSONArray("products"));
						productList.addAll(temp);

						total = result.getInt("totalPage");
						if (total == 1) {
							XlvproductSalesList.setPullLoadEnable(false);
						}

						// productIdList.clear();
						for (ProductBean productBean : productList) {
							productIdList.add(productBean.getId());
						}
						mylistAdapter.notifyDataSetChanged();

						progressbar.setVisibility(View.GONE);
						onLoad();

					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						Toast.makeText(ProductSalesActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductSalesActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ProductSalesActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Toast.makeText(ProductSalesActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}
	}

	/**
	 * 预览商品图片
	 * 
	 * @author Administrator
	 */
	class LoadProductImgViewTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadProductImgViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productImgView(id);
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
						List<ProductImgBean> temp = ProductImgBean
								.constractList(result
										.getJSONArray("productImgs"));

						productImgList = temp;

						// for (ProductImgBean post : productImgList) {
						// productIds.add(post.getId() + "");
						// }
						fillData();
						progressbar.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductSalesActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductSalesActivity.this, "数据加载失败11",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(ProductSalesActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	private static final int UPDATE_PRODUCT = 1;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		System.out.println(arg2);
		Intent itemintent = new Intent(ProductSalesActivity.this,
				ProductUpdateActivity.class);
		itemintent.putExtra("ProductId", productList.get(arg2 - 1).getId());

		if (!(productList.get(arg2 - 1).getProductCategory() + "").equals("")
				&& !(productList.get(arg2 - 1).getProductCategory() + "")
						.equals("null")) {
			itemintent.putExtra("productcategoruIs", productList.get(arg2 - 1)
					.getProductCategory().getId());
		} else {
			itemintent.putExtra("productcategoruIs", "");
		}
		startActivityForResult(itemintent, UPDATE_PRODUCT);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent result) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case UPDATE_PRODUCT:

				progressbar.setVisibility(View.VISIBLE);
				begin = 1;
				totalLinShi = 1;
				total = 1;
				mylistAdapter.notifyDataSetChanged();
				productList.clear();
				new LoadProductSalesTask(
						SharedPrefUtil.getToken(ProductSalesActivity.this),
						SharedPrefUtil.getShopBean(ProductSalesActivity.this)
								.getId(), "true", "monthSales", true, begin)
						.execute();

				break;

			default:
				break;
			}
		}
	}

	public void onRefresh() {
		XlvproductSalesList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;

					new LoadProductSalesTask(SharedPrefUtil
							.getToken(ProductSalesActivity.this),
							SharedPrefUtil.getShopBean(
									ProductSalesActivity.this).getId(), "true",
							"monthSales", true, begin).execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvproductSalesList.noLoadMore();
		}
	}

	private void onLoad() {
		XlvproductSalesList.stopRefresh();
		XlvproductSalesList.stopLoadMore(total);
	}
}
