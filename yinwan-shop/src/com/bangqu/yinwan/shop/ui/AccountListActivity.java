package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.BillsBean;
import com.bangqu.yinwan.shop.helper.ShopFinanceHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.ViewHolder;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class AccountListActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {

	private TextView tvnoaccount;
	private Button btnLeft;
	private Button btnRight;
	private TextView tvTittle;

	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;
	private int begin = 1;
	private Handler mHandler;
	private XListView XlvAccountList;
	private AccountListAdapter accountlistAdapter;
	private List<BillsBean> Accountlist = new ArrayList<BillsBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_list_layout);

		findView();
		new LoadAccountListTask(SharedPrefUtil.getToken(this), SharedPrefUtil
				.getShopBean(this).getId(), "addTime", true, begin).execute();
		// new LoadAccountListTask("56adb3a792a12be8a31aaf892943f21c", "1",
		// "addTime", true, begin).execute();
	}

	@Override
	public void findView() {
		super.findView();
		tvnoaccount = (TextView) findViewById(R.id.tvnoaccount);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("结算记录");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		XlvAccountList = (XListView) findViewById(R.id.XlvAccountList);
		XlvAccountList.setPullLoadEnable(true);
		accountlistAdapter = new AccountListAdapter(AccountListActivity.this);
		XlvAccountList.setAdapter(accountlistAdapter);
		XlvAccountList.setOnItemClickListener(this);
		XlvAccountList.setXListViewListener(this);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);

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
	 * 提现记录列表适配器
	 * 
	 * 
	 */
	private class AccountListAdapter extends BaseAdapter {
		private Context mContext;
		private BillsBean billsBean;

		public AccountListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return Accountlist.size();
		}

		@Override
		public BillsBean getItem(int position) {
			return Accountlist.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.account_list_item, null);
			}
			TextView tvstate = ViewHolder.get(convertView, R.id.tvstate);
			TextView tvtime = ViewHolder.get(convertView, R.id.tvtime);
			TextView tvmoney = ViewHolder.get(convertView, R.id.tvmoney);
			billsBean = getItem(position);
			tvtime.setText(billsBean.getAddTime().substring(0, 10));
			tvmoney.setText("-" + billsBean.getReceive());
			if (billsBean.getState().equals("1")) {
				tvstate.setText("已成功");
				tvstate.setTextColor(getResources().getColor(R.color.green));
			} else if (billsBean.getState().equals("0")) {
				tvstate.setText("失败");
				tvstate.setTextColor(getResources().getColor(
						R.color.color_red_two));
			}
			return convertView;

		}
	}

	/**
	 * 提现记录
	 */
	class LoadAccountListTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String order;
		private boolean desc;
		private int begin;

		protected LoadAccountListTask(String accessToken, String shopId,
				String order, boolean desc, int begin) {
			this.accessToken = accessToken;
			this.shopId = shopId;
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
				return new ShopFinanceHelper().billsearch(accessToken, shopId,
						order, desc, begin);
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
						List<BillsBean> temp = BillsBean.constractList(result
								.getJSONArray("bills"));
						Accountlist.addAll(temp);
						accountlistAdapter.notifyDataSetChanged();
						total = result.getInt("totalPage");
						if (total == 1) {
							XlvAccountList.setPullLoadEnable(false);
						}
						onLoad();
						XlvAccountList.setVisibility(View.VISIBLE);
						tvnoaccount.setVisibility(View.GONE);
						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						XlvAccountList.setVisibility(View.GONE);
						tvnoaccount.setVisibility(View.VISIBLE);
						progressbar.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(AccountListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				} catch (SystemException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(AccountListActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void onRefresh() {
		XlvAccountList.stopRefresh();
	}

	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadAccountListTask(SharedPrefUtil
							.getToken(AccountListActivity.this), SharedPrefUtil
							.getShopBean(AccountListActivity.this).getId(),
							"addTime", true, begin).execute();

				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvAccountList.setPullLoadEnable(false);
			XlvAccountList.noLoadMore();

		}
	}

	private void onLoad() {
		XlvAccountList.stopRefresh();
		XlvAccountList.stopLoadMore(total);
	}

	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

}
