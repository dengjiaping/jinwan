package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.bangqu.yinwan.shop.bean.PromotionBean;
import com.bangqu.yinwan.shop.helper.PromotionHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class ProductIsPromotedActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {

	// XListView
	private MyListAdapter mylistAdapter;
	private List<PromotionBean> promotionList = new ArrayList<PromotionBean>();
	private XListView XlvPromotionList;
	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;
	PromotionBean promotionBean;

	static class ViewHolder {
		ImageView ivProductImg;
		TextView tvProductName;
		TextView tvProductPrice;
		TextView tvPromotionPrice;
		ImageView ivtuangou;
		ImageView ivcuxiao;
		ImageView ivcuxiao2;
		ImageView ivtuangou2;
		TextView tvMinimum;
		TextView tvProductVipPrice;
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
		begin = 1;
		total = 1; // 判断是否还要继续下拉刷新
		totalLinShi = 1;
		promotionList.clear();
		new LoadPromotionListTask(SharedPrefUtil.getToken(this), SharedPrefUtil
				.getShopBean(this).getId(), "true", begin, "addTime", "true")
				.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void findView() {
		super.findView();
		// XListView
		XlvPromotionList = (XListView) findViewById(R.id.XlvPromotionList);
		mylistAdapter = new MyListAdapter(ProductIsPromotedActivity.this);
		XlvPromotionList.setAdapter(mylistAdapter);
		XlvPromotionList.setOnItemClickListener(this);
		XlvPromotionList.setPullLoadEnable(true);
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
	 * 已促销适配器
	 */
	protected PagerAdapter MyListAdapter = new PagerAdapter() {

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		public int getCount() {
			return promotionList.size();
		}

		public Object instantiateItem(View container, int position) {
			return bindGalleryAdapterItemView(container, position);
		}

		private Object bindGalleryAdapterItemView(View container, int position) {
			return null;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		};

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(android.os.Parcelable state, ClassLoader loader) {

		};

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	};

	/**
	 * 已促销商品列表适配器(ListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		PromotionBean promotionBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return promotionList.size();
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
						R.layout.promotion_item2_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.ivProductImg = (ImageView) convertView
						.findViewById(R.id.ivProductImg);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.tvProductPrice = (TextView) convertView
						.findViewById(R.id.tvProductPrice);
				viewHolder.tvPromotionPrice = (TextView) convertView
						.findViewById(R.id.tvPromotionPrice);
				viewHolder.tvProductVipPrice = (TextView) convertView
						.findViewById(R.id.tvProductVipPrice);

				viewHolder.ivcuxiao = (ImageView) convertView
						.findViewById(R.id.ivcuxiao);
				viewHolder.ivcuxiao2 = (ImageView) convertView
						.findViewById(R.id.ivcuxiao2);
				viewHolder.ivtuangou2 = (ImageView) convertView
						.findViewById(R.id.ivtuangou2);
				viewHolder.ivtuangou = (ImageView) convertView
						.findViewById(R.id.ivtuangou);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			promotionBean = promotionList.get(position);

			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(
							promotionList.get(position).getProduct().getImg(),
							viewHolder.ivProductImg);
			viewHolder.tvProductName.setText(promotionList.get(position)
					.getProduct().getName());
			if (promotionBean.getProduct().getIsPromotion().equals("true")) {
				viewHolder.ivcuxiao.setVisibility(View.GONE);
				viewHolder.ivcuxiao2.setVisibility(View.VISIBLE);
			} else {
				viewHolder.ivcuxiao.setVisibility(View.VISIBLE);
				viewHolder.ivcuxiao2.setVisibility(View.GONE);
			}
			if (promotionBean.getProduct().getIsGroupon().equals("true")) {
				viewHolder.ivtuangou2.setVisibility(View.VISIBLE);
				viewHolder.ivtuangou.setVisibility(View.GONE);
			} else {
				viewHolder.ivtuangou2.setVisibility(View.GONE);
				viewHolder.ivtuangou.setVisibility(View.VISIBLE);
			}

			viewHolder.tvProductPrice
					.setText("店铺价："
							+ promotionList.get(position).getProduct()
									.getPrice() + "元");
			viewHolder.tvProductVipPrice.setText("会员价："
					+ promotionList.get(position).getProduct().getVipPrice()
					+ "元");
			viewHolder.tvPromotionPrice.setText(promotionList.get(position)
					.getPrice());
			viewHolder.tvProductVipPrice.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.tvProductPrice.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
			return convertView;

		}
	}

	/**
	 * 促销列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadPromotionListTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String enabled;
		private int begin;
		private String order;
		private String desc;

		protected LoadPromotionListTask(String accessToken, String shopId,
				String enabled, int begin, String order, String desc) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.enabled = enabled;
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
				return new PromotionHelper().PromotionList(accessToken, shopId,
						enabled, begin, order, desc);
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
						List<PromotionBean> temp = PromotionBean
								.constractList(result
										.getJSONArray("promotions"));
						promotionList.addAll(temp);
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
					Toast.makeText(ProductIsPromotedActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ProductIsPromotedActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent itemintent = new Intent(ProductIsPromotedActivity.this,
				ProductIsPromotedUpdateActivity.class);
		itemintent.putExtra("ProductId", promotionList.get(arg2 - 1).getId());
		startActivityForResult(itemintent, 1234);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1234:
			promotionList.clear();
			new LoadPromotionListTask(SharedPrefUtil.getToken(this),
					SharedPrefUtil.getShopBean(this).getId(), "true", begin,
					"addTime", "true").execute();
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		XlvPromotionList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadPromotionListTask(SharedPrefUtil
							.getToken(ProductIsPromotedActivity.this),
							SharedPrefUtil.getShopBean(
									ProductIsPromotedActivity.this).getId(),
							"true", begin, "addTime", "true").execute();
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

}