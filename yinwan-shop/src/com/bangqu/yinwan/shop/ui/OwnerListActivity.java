package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.OwnerBean;
import com.bangqu.yinwan.shop.helper.OwnerHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

/**
 * 客户列表
 */
public class OwnerListActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {

	private Button btnLeft;
	private TextView tvTittle;
	private Button btnRight;

	private TextView tvNoData;

	// XListView
	private XListView XlvOwnerList;
	private MyListAdapter mylistAdapter;
	private Handler mHandler;
	private ListView lvOwnerList;
	private List<String> ownerIds = new ArrayList<String>();
	private List<OwnerBean> ownerList = new ArrayList<OwnerBean>();

	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;

	static class ViewHolder {
		TextView tvOwnerName;
		TextView tvOwnerLocation;
		TextView tvOwnerWuye;
		ImageView ivCall;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.owner_list_layout);

		findView();
		ownerList.clear();
		new LoadOwnerListTask(SharedPrefUtil.getToken(this), SharedPrefUtil
				.getShopBean(this).getId(), begin, "addTime", "true").execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void findView() {
		super.findView();
		// TODO Auto-generated method stub
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("客户管理");
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);

		tvNoData = (TextView) findViewById(R.id.tvNoData);

		// ListView
		XlvOwnerList = (XListView) findViewById(R.id.XlvOwnerList);
		XlvOwnerList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(OwnerListActivity.this);
		XlvOwnerList.setAdapter(mylistAdapter);
		XlvOwnerList.setOnItemClickListener(this);
		XlvOwnerList.setXListViewListener(this);
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
	 * 业主列表适配器(XListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		OwnerBean ownerBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return ownerList.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.owner_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.tvOwnerName = (TextView) convertView
						.findViewById(R.id.tvOwnerName);
				viewHolder.tvOwnerLocation = (TextView) convertView
						.findViewById(R.id.tvOwnerLocation);
				viewHolder.tvOwnerWuye = (TextView) convertView
						.findViewById(R.id.tvOwnerWuye);
				viewHolder.ivCall = (ImageView) convertView
						.findViewById(R.id.ivCall);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// for (int i = 0; i < ownerList.size(); i++) {
			// ownerBean = ownerList.get(i);
			// }
			ownerBean = ownerList.get(position);

			viewHolder.tvOwnerName.setText(ownerList.get(position).getName());
			viewHolder.tvOwnerLocation.setText(ownerList.get(position)
					.getLocationName());
			viewHolder.tvOwnerWuye.setText("("
					+ ownerList.get(position).getCompanyName() + ")");
			viewHolder.ivCall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent Callintent = new Intent(Intent.ACTION_DIAL,
							Uri.parse("tel:"
									+ ownerList.get(position).getMobile()));

					// ownerBean.getMobile()
					Callintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(Callintent);
				}
			});
			return convertView;
		}
	}

	/**
	 * 业主列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadOwnerListTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private int begin;
		private String order;
		private String desc;

		protected LoadOwnerListTask(String accessToken, String shopId,
				int begin, String order, String desc) {
			this.accessToken = accessToken;
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
				return new OwnerHelper().OwnerList(accessToken, shopId, begin,
						order, desc);
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
						List<OwnerBean> temp = OwnerBean.constractList(result
								.getJSONArray("owners"));
						ownerList.addAll(temp);
						mylistAdapter.notifyDataSetChanged();
						total = result.getInt("totalPage");
						if (total == 1) {
							XlvOwnerList.setPullLoadEnable(false);
						}
						progressbar.setVisibility(View.GONE);
						onLoad();
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(OwnerListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ownerListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(OwnerListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ownerListActivity", "SystemException");
				}
			} else {
				Toast.makeText(OwnerListActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ownerListActivity", "result==null");
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent itemintent = new Intent(OwnerListActivity.this,
				OwnerDetailActivity.class);
		itemintent.putExtra("OwnerId", ownerList.get(arg2 - 1).getId());
		startActivity(itemintent);
	}

	@Override
	public void onRefresh() {
		XlvOwnerList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadOwnerListTask(SharedPrefUtil
							.getToken(OwnerListActivity.this), SharedPrefUtil
							.getShopBean(OwnerListActivity.this).getId(),
							begin, "addTime", "true").execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvOwnerList.setPullLoadEnable(false);
			// XlvOwnerList.noLoadMore();
		}
	}

	private void onLoad() {
		XlvOwnerList.stopRefresh();
		XlvOwnerList.stopLoadMore(total);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
