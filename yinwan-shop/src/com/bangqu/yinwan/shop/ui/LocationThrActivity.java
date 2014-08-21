package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.LocationBean;
import com.bangqu.yinwan.shop.helper.LocationHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

public class LocationThrActivity extends UIBaseActivity implements
		OnClickListener, OnInflateListener {
	private ListView lvSearchOne;
	private List<LocationBean> SearchXiaoQuOneList = new ArrayList<LocationBean>();
	private MyListAdapter mylistAdapter;
	private TextView tvNoData;

	static class ViewHolder {
		TextView tvLocationName;
		Button btnApplyJoin;
		TextView tvendtime;
		TextView tvdistrict;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_xiaoqu_list_one_layout);
		findView();
		new LoadSearchJoinTask(
				SharedPrefUtil.getToken(LocationThrActivity.this),
				SharedPrefUtil.getShopBean(LocationThrActivity.this).getId(),
				"1000", -2).execute();
	}

	@Override
	public void findView() {
		super.findView();
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		mylistAdapter = new MyListAdapter(LocationThrActivity.this);
		lvSearchOne = (ListView) findViewById(R.id.lvSearchOne);
		lvSearchOne.setAdapter(mylistAdapter);

	}

	@Override
	public void onClick(View v) {

	}

	/**
	 * 已驳回小区列表
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		LocationBean locationBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return SearchXiaoQuOneList.size();
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
						R.layout.xiaoqu_detail_one_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvLocationName = (TextView) convertView
						.findViewById(R.id.tvLocationName);
				viewHolder.tvendtime = (TextView) convertView
						.findViewById(R.id.tvendtime);
				viewHolder.tvdistrict = (TextView) convertView
						.findViewById(R.id.tvdistrict);
				viewHolder.btnApplyJoin = (Button) convertView
						.findViewById(R.id.btnApplyJoin);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			locationBean = SearchXiaoQuOneList.get(position);
			viewHolder.tvLocationName.setText(locationBean.getName());
			viewHolder.tvdistrict.setText(locationBean.getDistrict().getName());
			viewHolder.tvendtime.setText(locationBean.getAddTime());
			viewHolder.btnApplyJoin.setText("已驳回");
			/*viewHolder.btnApplyJoin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("locationId", SearchXiaoQuOneList.get(position)
							.getName());
					Log.i("locationId", SearchXiaoQuOneList.get(position)
							.getId());

					new LoadApplyJoinTask(SharedPrefUtil
							.getToken(LocationThrActivity.this), SharedPrefUtil
							.getShopBean(LocationThrActivity.this).getId(),
							SearchXiaoQuOneList.get(position).getId())
							.execute();
				}
			});*/
			return convertView;
		}
	}

	/**
	 * 申请加入小区
	 */
	class LoadApplyJoinTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String locationId;

		protected LoadApplyJoinTask(String accessToken, String shopId,
				String locationId) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.locationId = locationId;
		}

		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new LocationHelper().ApplyJoin(accessToken, shopId,
						locationId);
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
						Toast.makeText(LocationThrActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(LocationThrActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("SearchXiaoQuThreeActivity", "JSONException");
				}
			} else {
				Log.i("SearchXiaoQuThreeActivity", "result==null");
			}
		}
	}

	/**
	 * 已加入小区列表
	 */
	class LoadSearchJoinTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String pagesize;
		private int state;

		protected LoadSearchJoinTask(String accessToken, String shopId,
				String pagesize, int state) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.pagesize = pagesize;
			this.state = state;
		}

		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new LocationHelper().SearcgJoin(accessToken, shopId,
						pagesize, state);
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
						List<LocationBean> temp = LocationBean
								.constractList(result.getJSONArray("locations"));
						SearchXiaoQuOneList = temp;

						progressbar.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("SearchXiaoQuThreeActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Log.i("SearchXiaoQuThreeActivity", "SystemException");
				}
			} else {
				Log.i("SearchXiaoQuThreeActivity", "result==null");
			}

		}

	}

	@Override
	public void onInflate(ViewStub stub, View inflated) {

	}

}
