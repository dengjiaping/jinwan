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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class ProductUnGrouponActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {
	// XListView
	private XListView XlvGrouponList;
	private MyListAdapter mylistAdapter;
	private Handler mHandler;
	private List<String> productIds = new ArrayList<String>(); // 存放某个商品ID
	private List<ProductBean> productList = new ArrayList<ProductBean>(); // 存放接口数据
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;

	static class ViewHolder {
		ImageView ivProductImg;
		TextView tvProductName;
		TextView tvProductCategory;
		TextView tvProductPrice;
		ImageView ivtuangou;
		ImageView ivcuxiao;
		ImageView ivtuangou2;
		ImageView ivcuxiao2;

	}

	// 暂无促销
	private TextView tvNoData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupon_list_layout);

		findView();
	}

	public void findView() {
		super.findView();
		// ListView
		XlvGrouponList = (XListView) findViewById(R.id.XlvGrouponList);
		XlvGrouponList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(ProductUnGrouponActivity.this);

		XlvGrouponList.setAdapter(mylistAdapter);
		XlvGrouponList.setOnItemClickListener(this);
		XlvGrouponList.setXListViewListener(this);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		mHandler = new Handler();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void onMyResume() {
		begin = 1;
		total = 1; // 判断是否还要继续下拉刷新
		totalLinShi = 1;
		productList.clear();
		new LoadUnGrouponListTask(SharedPrefUtil.getToken(this), SharedPrefUtil
				.getShopBean(this).getId(), "true", "false", begin, "addTime",
				"true").execute();
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
	 * 未团购商品列表适配器(ListView)
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

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.groupon_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.ivProductImg = (ImageView) convertView
						.findViewById(R.id.ivProductImg);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.tvProductCategory = (TextView) convertView
						.findViewById(R.id.tvProductCategory);
				viewHolder.tvProductPrice = (TextView) convertView
						.findViewById(R.id.tvProductPrice);
				viewHolder.ivcuxiao = (ImageView) convertView
						.findViewById(R.id.ivcuxiao);
				viewHolder.ivtuangou = (ImageView) convertView
						.findViewById(R.id.ivtuangou);
				viewHolder.ivcuxiao2 = (ImageView) convertView
						.findViewById(R.id.ivcuxiao2);
				viewHolder.ivtuangou2 = (ImageView) convertView
						.findViewById(R.id.ivtuangou2);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			productBean = productList.get(position);
			((CommonApplication) getApplicationContext())
					.getImgLoader()
					.DisplayImage(productBean.getImg(), viewHolder.ivProductImg);
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
			} else {
				viewHolder.ivcuxiao.setVisibility(View.VISIBLE);
				viewHolder.ivcuxiao2.setVisibility(View.GONE);
			}
			if (productBean.getIsGroupon().equals("true")) {

				viewHolder.ivtuangou2.setVisibility(View.VISIBLE);
				viewHolder.ivtuangou.setVisibility(View.GONE);
			} else {
				viewHolder.ivtuangou.setVisibility(View.VISIBLE);
				viewHolder.ivtuangou2.setVisibility(View.GONE);
			}

			viewHolder.tvProductPrice.setText(productBean.getPrice());
			return convertView;

		}
	}

	/**
	 * 未团购列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadUnGrouponListTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String enabled;
		private String isGroupon;
		private int begin;
		private String order;
		private String desc;

		protected LoadUnGrouponListTask(String accessToken, String shopId,
				String enabled, String isGroupon, int begin, String order,
				String desc) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.enabled = enabled;
			this.isGroupon = isGroupon;
			this.begin = begin;
			this.order = order;
			this.desc = desc;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			try {
				// 返回一个 JSONObject
				return new ProductHelper().UnGrouponList(accessToken, shopId,
						enabled, isGroupon, begin, order, desc);
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
						mylistAdapter.notifyDataSetChanged();
						total = result.getInt("totalPage");
						if (total == 1) {
							XlvGrouponList.setPullLoadEnable(false);
						}
						// productIds.clear();
						// productIds.add("表头占用项");
						// for (ProductBean post : productList) {
						// productIds.add(post.getId() + "");
						// }
						tvNoData.setVisibility(View.GONE);
						progressbar.setVisibility(View.GONE);
						onLoad();
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUnGrouponActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ProductUnGrouponActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent itemintent = new Intent(ProductUnGrouponActivity.this,
				UnGroupoonUpdateActivity.class);
		itemintent.putExtra("ProductId", productList.get(arg2 - 1).getId());
		startActivity(itemintent);
	}

	@Override
	public void onRefresh() {
		XlvGrouponList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadUnGrouponListTask(SharedPrefUtil
							.getToken(ProductUnGrouponActivity.this),
							SharedPrefUtil.getShopBean(
									ProductUnGrouponActivity.this).getId(),
							"true", "false", begin, "addTime", "true")
							.execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvGrouponList.setPullLoadEnable(false);
			XlvGrouponList.noLoadMore();
		}
	}

	private void onLoad() {
		XlvGrouponList.stopRefresh();
		XlvGrouponList.stopLoadMore(total);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

}