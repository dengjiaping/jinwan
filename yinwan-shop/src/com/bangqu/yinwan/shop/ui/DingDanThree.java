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
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ItemsBean;
import com.bangqu.yinwan.shop.bean.OrderBean;
import com.bangqu.yinwan.shop.helper.OrderHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

/**
 * 已完成 state=1
 */
public class DingDanThree extends UIBaseActivity implements OnClickListener,
		IXListViewListener {

	// 外部列表
	// 外部列表
	private XListView XlvOrderList;
	private MyListAdapter mylistAdapter;
	private List<OrderBean> orderList = new ArrayList<OrderBean>();
	// 内部列表
	private ItemListAdapter itemlistAdapter;
	private List<ItemsBean> itemsList = new ArrayList<ItemsBean>();

	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;

	private TextView tvTittle;
	private Button btnLeft;
	private Button btnRight;
	OrderBean orderBean;
	ItemsBean itemsBean;

	// XlvOrderList
	static class ViewHolder {
		TextView tvAdress;
		TextView tvMobile;
		TextView tvRemark;
		TextView tvSend;
		TextView timeView;
		TextView tvaddtime;
		TextView tvAllmoney;
		TextView tvNumber;
		Button btnCancel;
		Button btnSend;
		ListView lvitem;
		LinearLayout llevent;
		LinearLayout lllistall;
		TextView tvallcount;
		TextView tvstate;
		TextView tvpayment;
	}

	// lvitem
	static class ViewHolder2 {
		TextView tvprice;
		TextView tvAmount;
		TextView tvProductName;
		LinearLayout llall;

	}

	// 暂无订单
	private TextView tvNoData;

	// 0待配送，2配送中，3已发货，1已完成，-1已取消
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dingdan_list_layout);

		findView();
		new LoadOrderListTask(SharedPrefUtil.getToken(this), SharedPrefUtil
				.getShopBean(this).getId(), "1", begin, "addTime", "true")
				.execute();
	}

	protected void onMyResume() {
		orderList.clear();
		new LoadOrderListTask(SharedPrefUtil.getToken(this), SharedPrefUtil
				.getShopBean(this).getId(), "1", begin, "addTime", "true")
				.execute();
	}

	public void findView() {
		super.findView();
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("已完成");
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);

		// XListView
		XlvOrderList = (XListView) findViewById(R.id.XlvOrderList);
		XlvOrderList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(DingDanThree.this);
		XlvOrderList.setAdapter(mylistAdapter);
		XlvOrderList.setXListViewListener(this);

		mHandler = new Handler();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			DingDanThree.this.finish();
			break;

		default:
			break;
		}

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
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return orderList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.ordernew_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.tvAdress = (TextView) convertView
						.findViewById(R.id.tvAdress);
				viewHolder.tvMobile = (TextView) convertView
						.findViewById(R.id.tvMobile);
				viewHolder.tvRemark = (TextView) convertView
						.findViewById(R.id.tvRemark);
				viewHolder.tvAllmoney = (TextView) convertView
						.findViewById(R.id.tvAllmoney);
				viewHolder.tvaddtime = (TextView) convertView
						.findViewById(R.id.tvaddtime);
				viewHolder.tvSend = (TextView) convertView
						.findViewById(R.id.tvSend);
				viewHolder.tvNumber = (TextView) convertView
						.findViewById(R.id.tvNumber);
				viewHolder.btnCancel = (Button) convertView
						.findViewById(R.id.btnCancel);
				viewHolder.btnCancel.setVisibility(View.GONE);
				viewHolder.btnSend = (Button) convertView
						.findViewById(R.id.btnSend);
				viewHolder.btnSend.setText("联系买家");
				viewHolder.llevent = (LinearLayout) convertView
						.findViewById(R.id.llevent);
				viewHolder.lllistall = (LinearLayout) convertView
						.findViewById(R.id.lllistall);
				viewHolder.tvallcount = (TextView) convertView
						.findViewById(R.id.tvallcount);

				viewHolder.lvitem = (ListView) convertView
						.findViewById(R.id.lvitemone);
				viewHolder.tvpayment = (TextView) convertView
						.findViewById(R.id.tvpayment);
				viewHolder.tvstate = (TextView) convertView
						.findViewById(R.id.tvstate);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			orderBean = orderList.get(position);
			// 点击事件要写在下面，不然orderList.get(position).getId()绘制显示当前视图内的ID
			viewHolder.lllistall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent itemintent = new Intent(DingDanThree.this,
							DingDanItemActivity.class);
					System.out.println("订单列表点击");
					itemintent.putExtra("OrderId", orderList.get(position)
							.getId());
					System.out.println(orderList.get(position).getId()
							+ "position当前ID判断");

					itemintent.putExtra("state", "1");
					startActivity(itemintent);
				}
			});
			switch (Integer.parseInt(orderBean.getPayment())) {
			case 1:
				viewHolder.tvpayment.setText("现金支付");
				break;
			case 2:
				viewHolder.tvpayment.setText("会员卡支付");
				break;
			case 3:
				viewHolder.tvpayment.setText("在线支付");
				break;
			case 4:
				viewHolder.tvpayment.setText("POS机支付");
				break;

			default:
				break;
			}
			viewHolder.tvstate.setText(orderBean.getStatements());
			viewHolder.tvallcount.setText("X" + orderBean.getProductSize());
			viewHolder.tvAdress.setText(orderBean.getLocation().getName() + "-"
					+ orderBean.getAddress().getAddr());
			viewHolder.tvMobile.setText(orderBean.getAddress().getMobile());
			viewHolder.tvaddtime.setText(orderBean.getAddTime());
			viewHolder.tvAllmoney.setText("￥" + orderBean.getPrice());
			viewHolder.tvNumber.setText(orderBean.getShopNo() + "号");
			viewHolder.tvSend.setText("订单已完成");
			if ((orderBean.getRemark() + "").equals("")
					|| (orderBean.getRemark() + "").equals("null")) {
				viewHolder.tvRemark.setText("备注：" + "暂无备注");
			} else {
				viewHolder.tvRemark.setText("备注：" + orderBean.getRemark());
			}
			viewHolder.btnSend.setTag(position);
			viewHolder.btnSend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final int index = (Integer) v.getTag();
					String TelNo = orderList.get(position).getAddress()
							.getMobile();

					Intent Callintent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:" + TelNo));

					Callintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(Callintent);

				}
			});
			/**
			 * 崩溃处
			 * 
			 * 提示内部list为空，初步判定为未找到list
			 */
			itemsList = orderList.get(position).getItems();
			itemlistAdapter = new ItemListAdapter(DingDanThree.this);
			// 此处改动
			setListViewHeight(viewHolder.lvitem);
			viewHolder.lvitem.setAdapter(itemlistAdapter);

			convertView.setTag(viewHolder);

			return convertView;
		}

		/**
		 * 内部列表高度设置
		 * 
		 * @param lvitem
		 */
		// 此处改动
		private void setListViewHeight(ListView lvitem) {
			if (itemlistAdapter == null) {
				return;
			}

			int totalHeight = 0;
			for (int i = 0; i < itemlistAdapter.getCount(); i++) {
				View listItem = itemlistAdapter.getView(i, null, lvitem);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = lvitem.getLayoutParams();
			params.height = totalHeight
					+ (lvitem.getDividerHeight() * (itemlistAdapter.getCount() - 1));
			((MarginLayoutParams) params).setMargins(20, 10, 20, 10);
			lvitem.setLayoutParams(params);
		}

	}

	/**
	 * item列表适配器(ListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class ItemListAdapter extends BaseAdapter {
		private Context mContext;
		ItemsBean itemsBean;

		// liOrderBean orderBean;

		public ItemListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return itemsList.size();
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

			ViewHolder2 viewHolder2 = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.order_new_item, null);
				viewHolder2 = new ViewHolder2();
				viewHolder2.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder2.tvAmount = (TextView) convertView
						.findViewById(R.id.tvAmount);
				viewHolder2.tvprice = (TextView) convertView
						.findViewById(R.id.tvprice);
				viewHolder2.llall = (LinearLayout) convertView
						.findViewById(R.id.llall);
				viewHolder2.llall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("点击了");
					}
				});
			} else {
				viewHolder2 = (ViewHolder2) convertView.getTag();
			}
			// orderBean = orderList.get(position);
			itemsBean = itemsList.get(position);

			viewHolder2.tvprice.setText("￥" + itemsBean.getPrice());
			viewHolder2.tvProductName.setText(itemsBean.getProduct().getName());
			viewHolder2.tvAmount.setText("X" + itemsBean.getQuantity());
			// for (int i = 0; i < orderBean.getItems().size(); i++) {
			// itemsList.add(orderList.get(position).getItems().get(i));
			// }

			// for (int j = 0; j < itemsList.size(); j++) {
			// viewHolder2.tvprice.setText("￥" + itemsList.get(j).getPrice());
			// viewHolder2.tvProductName.setText(itemsList.get(j).getProduct()
			// .getName());
			// viewHolder2.tvAmount.setText("X"
			// + itemsList.get(j).getQuantity());
			// }

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
						// mylistAdapter.notifyDataSetChanged();

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
					Toast.makeText(DingDanThree.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(DingDanThree.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
			}

		}
	}

	/**
	 * 未处理订单改为配送中订单
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadOrderDealTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;

		protected LoadOrderDealTask(String accessToken, String id) {
			this.accessToken = accessToken;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(DingDanThree.this,
						"正在提交请求……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new OrderHelper().OrderDeal(accessToken, id);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pd != null)
				pd.dismiss();
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(DingDanThree.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(DingDanThree.this, "msg",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(DingDanThree.this, "数据加载失败.",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(DingDanThree.this, "数据加载失败", Toast.LENGTH_LONG)
						.show();
				Log.i("ProductListActivity", "result==null");
			}

		}
	}

	/**
	 * 取消订单
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadOrderCancelTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;
		private String content;

		protected LoadOrderCancelTask(String accessToken, String id,
				String content) {
			this.accessToken = accessToken;
			this.id = id;
			this.content = content;
		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(DingDanThree.this,
						"正在提交请求……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new OrderHelper().OrderCancel(accessToken, id, content);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pd != null)
				pd.dismiss();
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(DingDanThree.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(DingDanThree.this, "msg",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(DingDanThree.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(DingDanThree.this, "数据加载失败", Toast.LENGTH_LONG)
						.show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	@Override
	public void onRefresh() {
		XlvOrderList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadOrderListTask(SharedPrefUtil
							.getToken(DingDanThree.this), SharedPrefUtil
							.getShopBean(DingDanThree.this).getId(), "1",
							begin, "addTime", "true").execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvOrderList.setPullLoadEnable(false);
		}
	}

	private void onLoad() {
		XlvOrderList.stopRefresh();
		XlvOrderList.stopLoadMore(total);
	}

}