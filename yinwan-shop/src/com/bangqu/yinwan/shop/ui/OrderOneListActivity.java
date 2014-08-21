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
import com.bangqu.yinwan.shop.bean.OrderBean;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.helper.OrderHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class OrderOneListActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {
	// ListView
	private XListView XlvOrderList;
	private MyListAdapter mylistAdapter;
	private List<OrderBean> orderList = new ArrayList<OrderBean>();
	private List<ProductBean> priductList = new ArrayList<ProductBean>();

	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;
	private String strState;
	private String id;

	private TextView tvTittle;
	private Button btnLeft;
	private Button btnRight;

	private TextView tvNoData;

	static class ViewHolder {
		TextView tvAdress;
		TextView tvMobile;
		TextView tvRemark;
		TextView tvSend;
		Button btnCancel;
		Button btnSend;

		public static TextView get(View convertView, int tvproductname) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dingdan_list_layout);
		findView();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		super.findView();
		tvNoData = (TextView) findViewById(R.id.tvNoData);

		// XListView
		XlvOrderList = (XListView) findViewById(R.id.XlvOrderList);
		XlvOrderList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(OrderOneListActivity.this);
		XlvOrderList.setAdapter(mylistAdapter);
		XlvOrderList.setOnItemClickListener(this);
		XlvOrderList.setXListViewListener(this);
		mHandler = new Handler();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/**
	 * 未处理订单列表适配器(ListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		OrderBean orderBean;

		public MyListAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderList.size();
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
						R.layout.ordernew_layout, null);
				viewHolder.tvAdress = (TextView) convertView
						.findViewById(R.id.tvAdress);
				viewHolder.tvMobile = (TextView) convertView
						.findViewById(R.id.tvMobile);
				viewHolder.tvRemark = (TextView) convertView
						.findViewById(R.id.tvRemark);
				viewHolder.tvSend = (TextView) convertView
						.findViewById(R.id.tvSend);
				viewHolder.btnCancel = (Button) convertView
						.findViewById(R.id.btnCancel);
				viewHolder.btnSend = (Button) convertView
						.findViewById(R.id.btnSend);

				viewHolder = new ViewHolder();

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			orderBean = orderList.get(position);
			viewHolder.tvAdress.setText(orderBean.getAddress().getAddr());
			viewHolder.tvMobile.setText(orderBean.getAddress().getAddr());
			viewHolder.tvMobile.setText(orderBean.getAddress().getMobile()
					.toString());

			return convertView;
		}
	}

	/**
	 * 商品订单列表适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListitemAdapter extends BaseAdapter {
		private Context mContext;

		private ProductBean productBean;

		public MyListitemAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return priductList.size();
		}

		@Override
		public ProductBean getItem(int position) {
			// TODO Auto-generated method stub
			return priductList.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
			// TODO Auto-generated method stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.order_new_item, null);
			}

			TextView tvProductName = ViewHolder.get(convertView,
					R.id.tvProductName);
			TextView tvAmount = ViewHolder.get(convertView, R.id.tvAmount);
			TextView tvprice = ViewHolder.get(convertView, R.id.tvprice);

			productBean = getItem(position);
			tvProductName.setText("");
			tvAmount.setText("");
			tvprice.setText("");

			return convertView;
		}

	}

	/**
	 * 订单列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadOrderListTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String states;
		private int begin;
		private String order;
		private String desc;

		protected LoadOrderListTask(String accessToken, String shopId,
				String states, int begin, String order, String desc) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.states = states;
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
				return new OrderHelper().OrderComments(accessToken, shopId,
						states, begin, order, desc);
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
						List<OrderBean> temp = OrderBean.constractList(result
								.getJSONArray("orders"));
						orderList.addAll(temp);
						mylistAdapter.notifyDataSetChanged();
						total = result.getInt("totalPage");
						if (total == 1) {
							XlvOrderList.setPullLoadEnable(false);
						}
						progressbar.setVisibility(View.GONE);
						onLoad();

					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(OrderOneListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(OrderOneListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
			}

		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	private void onLoad() {
		XlvOrderList.stopRefresh();
		XlvOrderList.stopLoadMore(total);
	}

}
