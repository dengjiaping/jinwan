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
import com.bangqu.yinwan.shop.helper.PromotionHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class ProductUnPromotedActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {
	// ListView
	private XListView XlvPromotionList;
	private MyListAdapter mylistAdapter;
	private List<String> productIds = new ArrayList<String>(); // 存放某个商品ID
	private List<ProductBean> productList = new ArrayList<ProductBean>();
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;

	static class ViewHolder {
		ImageView ivProductImg;
		TextView tvProductName;
		TextView tvProductPrice;
		TextView tvProductVipPrice;
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
		setContentView(R.layout.promotion_list_layout);

		findView();

	}

	protected void onMyResume() {
		System.out.println("onmyresume2");

		begin = 1;
		total = 1; // 判断是否还要继续下拉刷新
		totalLinShi = 1;
		productList.clear();
		new LoadUnPromotionListTask(SharedPrefUtil.getToken(this),
				SharedPrefUtil.getShopBean(this).getId(), "true", "false",
				"addTime", "true", begin).execute();
	}

	public void findView() {
		super.findView();
		XlvPromotionList = (XListView) findViewById(R.id.XlvPromotionList);
		XlvPromotionList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(ProductUnPromotedActivity.this);

		XlvPromotionList.setAdapter(mylistAdapter);
		XlvPromotionList.setOnItemClickListener(this);
		XlvPromotionList.setXListViewListener(this);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
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
	 * 未促销商品列表适配器(ListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		ProductBean productBean;

		// ShopBean tradeBean;

		public MyListAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return productList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
			// TODO Auto-generated method stub
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.promotion_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.ivProductImg = (ImageView) convertView
						.findViewById(R.id.ivProductImg);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.tvProductPrice = (TextView) convertView
						.findViewById(R.id.tvProductPrice);
				viewHolder.tvProductVipPrice = (TextView) convertView
						.findViewById(R.id.tvProductVipPrice);
				viewHolder.ivtuangou = (ImageView) convertView
						.findViewById(R.id.ivtuangou);
				viewHolder.ivcuxiao = (ImageView) convertView
						.findViewById(R.id.ivcuxiao);
				viewHolder.ivtuangou2 = (ImageView) convertView
						.findViewById(R.id.ivtuangou2);
				viewHolder.ivcuxiao2 = (ImageView) convertView
						.findViewById(R.id.ivcuxiao2);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			productBean = productList.get(position);
			((CommonApplication) getApplicationContext())
					.getImgLoader()
					.DisplayImage(productBean.getImg(), viewHolder.ivProductImg);
			viewHolder.tvProductName.setText(productBean.getName());
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
				viewHolder.ivtuangou2.setVisibility(View.GONE);
				viewHolder.ivtuangou.setVisibility(View.VISIBLE);
			}
			viewHolder.tvProductPrice.setText(productBean.getPrice());
			viewHolder.tvProductVipPrice.setText(productBean.getVipPrice());

			return convertView;

		}
	}

	/**
	 * 未促销列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadUnPromotionListTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String enabled;
		private String isPromotion;
		private String order;
		private String desc;
		private int begin;

		// private int begin;

		protected LoadUnPromotionListTask(String accessToken, String shopId,
				String enabled, String isPromotion, String order, String desc,
				int begin) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.enabled = enabled;
			this.isPromotion = isPromotion;
			this.order = order;
			this.desc = desc;
			this.begin = begin;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			try {
				// 返回一个 JSONObject
				return new PromotionHelper().UnPromotionList(accessToken,
						shopId, enabled, isPromotion, order, desc, begin);
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
							XlvPromotionList.setPullLoadEnable(false);
						}
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.GONE);
						onLoad();
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUnPromotedActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ProductUnPromotedActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		System.out.println("onrefresh()");
		XlvPromotionList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					// productList.clear();
					new LoadUnPromotionListTask(SharedPrefUtil
							.getToken(ProductUnPromotedActivity.this),
							SharedPrefUtil.getShopBean(
									ProductUnPromotedActivity.this).getId(),
							"true", "false", "addTime", "true", begin)
							.execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			// XlvPromotionList.setPullLoadEnable(false);
			XlvPromotionList.noLoadMore();
		}
	}

	private void onLoad() {
		XlvPromotionList.stopRefresh();
		XlvPromotionList.stopLoadMore(total);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent itemintent = new Intent(ProductUnPromotedActivity.this,
				ProductUnPromotedUpdateActivity.class);
		itemintent.putExtra("ProductId", productList.get(arg2 - 1).getId());
		startActivity(itemintent);
	}

}