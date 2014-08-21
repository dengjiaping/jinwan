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
import com.bangqu.yinwan.shop.bean.GrouponBean;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class ProductIsGrouponActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {

	// XListView
	private MyListAdapter mylistAdapter;
	private List<String> groupIds = new ArrayList<String>();
	private List<GrouponBean> grouponList = new ArrayList<GrouponBean>();
	private XListView XlvGrouponList;
	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;

	static class ViewHolder {
		ImageView ivProductImg;
		TextView tvProductName;
		TextView tvProductPrice;
		TextView tvGrouponPrice;
		ImageView ivtuangou;
		ImageView ivcuxiao;
		ImageView ivtuangou2;
		ImageView ivcuxiao2;
		TextView tvMinimum;
	}

	// 暂无团购
	private TextView tvNoData;

	// query.enabled=true 已促销(可用)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupon_list_layout);

		findView();
		// grouponList.clear();
		// new LoadGrouponListTask(SharedPrefUtil.getShopBean(this).getId(),
		// begin, "addTime", "true").execute();

	}

	protected void onMyResume() {
		begin = 1;
		total = 1; // 判断是否还要继续下拉刷新
		totalLinShi = 1;
		grouponList.clear();
		new LoadGrouponListTask(SharedPrefUtil.getShopBean(this).getId(),
				begin, "addTime", "true").execute();
	}

	public void findView() {
		super.findView();

		// ListView
		XlvGrouponList = (XListView) findViewById(R.id.XlvGrouponList);
		mylistAdapter = new MyListAdapter(ProductIsGrouponActivity.this);
		XlvGrouponList.setAdapter(mylistAdapter);
		XlvGrouponList.setOnItemClickListener(this);
		XlvGrouponList.setPullLoadEnable(true);
		XlvGrouponList.setXListViewListener(this);
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
	 * 团购商品列表适配器(ListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		GrouponBean grouponBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return grouponList.size();
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
						R.layout.groupon_item2_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.ivProductImg = (ImageView) convertView
						.findViewById(R.id.ivProductImg);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.tvProductPrice = (TextView) convertView
						.findViewById(R.id.tvProductPrice);
				viewHolder.tvGrouponPrice = (TextView) convertView
						.findViewById(R.id.tvGrouponPrice);

				viewHolder.ivtuangou = (ImageView) convertView
						.findViewById(R.id.ivtuangou);
				viewHolder.ivcuxiao = (ImageView) convertView
						.findViewById(R.id.ivcuxiao);
				viewHolder.ivtuangou2 = (ImageView) convertView
						.findViewById(R.id.ivtuangou2);
				viewHolder.ivcuxiao2 = (ImageView) convertView
						.findViewById(R.id.ivcuxiao2);
				viewHolder.tvMinimum = (TextView) convertView
						.findViewById(R.id.tvMinimum);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			grouponBean = grouponList.get(position);

			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(
							grouponList.get(position).getProduct().getImg()
									+ "!small.jpg", viewHolder.ivProductImg);
			viewHolder.tvProductName.setText(grouponList.get(position)
					.getProduct().getName());
			viewHolder.tvProductPrice.setText("店铺价："
					+ grouponList.get(position).getProduct().getPrice() + "元");
			viewHolder.tvGrouponPrice.setText(grouponList.get(position)
					.getPrice());
			if (grouponBean.getProduct().getIsPromotion().equals("true")) {
				viewHolder.ivcuxiao.setVisibility(View.GONE);
				viewHolder.ivcuxiao2.setVisibility(View.VISIBLE);
			} else {
				viewHolder.ivcuxiao.setVisibility(View.VISIBLE);
				viewHolder.ivcuxiao2.setVisibility(View.GONE);
			}
			if (grouponBean.getProduct().getIsGroupon().equals("true")) {

				viewHolder.ivtuangou2.setVisibility(View.VISIBLE);
				viewHolder.ivtuangou.setVisibility(View.GONE);
			} else {
				viewHolder.ivtuangou.setVisibility(View.VISIBLE);
				viewHolder.ivtuangou2.setVisibility(View.GONE);
			}
			viewHolder.tvMinimum
					.setText(grouponList.get(position).getMinimum());
			viewHolder.tvProductPrice.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);

			return convertView;

		}
	}

	/**
	 * 团购列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadGrouponListTask extends AsyncTask<String, Void, JSONObject> {
		private String shopId;
		private int begin;
		private String order;
		private String desc;

		protected LoadGrouponListTask(String shopId, int begin, String order,
				String desc) {
			this.shopId = shopId;
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
				return new ProductHelper().GrouponList(shopId, begin, order,
						desc);
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
						List<GrouponBean> temp = GrouponBean
								.constractList(result.getJSONArray("groupons"));
						// grouponList.clear();
						grouponList.addAll(temp);
						mylistAdapter.notifyDataSetChanged();
						total = result.getInt("totalPage");
						if (total == 1) {
							XlvGrouponList.setPullLoadEnable(false);
						}
						// groupIds.clear();
						// groupIds.add("表头占用项");
						// for (GrouponBean post : grouponList) {
						// groupIds.add(post.getId() + "");
						// }
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.GONE);
						onLoad();
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductIsGrouponActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ProductIsGrouponActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
			}

		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent itemintent = new Intent(ProductIsGrouponActivity.this,
				ProductIsGrouponUpdateActivity.class);
		itemintent.putExtra("ProductId", grouponList.get(arg2 - 1).getId());
		System.out.println(grouponList.get(arg2 - 1).getId() + "团购di");
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
					new LoadGrouponListTask(SharedPrefUtil.getShopBean(
							ProductIsGrouponActivity.this).getId(), begin,
							"addTime", "true").execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			// XlvGrouponList.setPullLoadEnable(false);
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