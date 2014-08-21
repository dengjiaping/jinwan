package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ItemsBean;
import com.bangqu.yinwan.shop.bean.OrderBean;
import com.bangqu.yinwan.shop.bean.StepBean;
import com.bangqu.yinwan.shop.helper.OrderHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.OrderCancelDialog;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class DingDanItemActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener {
	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;

	private Button btnCancel;
	private Button btnDeal;
	private View view;

	// 填充订单详情数据
	private TextView tvOrderState;
	private String strState;
	private String id = "";
	private TextView tvUserName;
	private TextView tvUserTel;
	private TextView tvUserLocation;
	private TextView tvUserAddr;
	private TextView tvOrderNo;
	private TextView tvSumMoney;
	private LinearLayout llorderbeiz;
	private TextView tvbeizhu;
	private TextView tvOrderData;

	// ListView
	private ListView lvOrderList;
	private MyOrderDetailsListAdapter myorderlistAdapter;
	OrderBean orderBean;

	private List<ItemsBean> itemsList = new ArrayList<ItemsBean>();
	private List<StepBean> stepsList = new ArrayList<StepBean>();

	static class ViewHolder {
		ImageView ivProductImg;
		ImageView ivtuangouY;
		ImageView ivcuxiaoY;
		TextView tvProductName;
		ImageView ivtuangou;
		ImageView ivcuxiao;
		TextView tvNum;
		TextView tvProductPrice;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dingdanitem_layout);

		findView();
		id = getIntent().getStringExtra("OrderId");
		// System.out.println(getIntent().getStringExtra("OrderId") +
		// "订单列表传值id");
		// System.out.println(id + "订单item的id");
		new LoadOrderDetailTask(SharedPrefUtil.getToken(this), id).execute();
	}

	@Override
	public void findView() {
		super.findView();
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("订单详情");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		btnDeal = (Button) findViewById(R.id.btnDeal);
		btnDeal.setOnClickListener(this);

		view = (View) findViewById(R.id.view);

		strState = getIntent().getStringExtra("state");
		System.out.println(strState + "订单状态传值");
		// 固定项
		tvOrderState = (TextView) findViewById(R.id.tvOrderState);

		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserTel = (TextView) findViewById(R.id.tvUserTel);
		tvUserLocation = (TextView) findViewById(R.id.tvUserLocation);
		tvUserAddr = (TextView) findViewById(R.id.tvUserAddr);
		tvOrderNo = (TextView) findViewById(R.id.tvOrderNo);
		tvOrderData = (TextView) findViewById(R.id.tvOrderData);
		tvSumMoney = (TextView) findViewById(R.id.tvSumMoney);

		llorderbeiz = (LinearLayout) findViewById(R.id.llorderbeiz);
		tvbeizhu = (TextView) findViewById(R.id.tvbeizhu);
	}

	@Override
	public void fillData() {
		super.fillData();

		// 0未处理，2处理中，1已完成，-1已取消

		if ((orderBean.getState() + "").equals("0")) {
			tvOrderState.setText("待配送");
			btnDeal.setText("处理订单");
			llorderbeiz.setVisibility(View.VISIBLE);
		} else if ((orderBean.getState() + "").equals("2")) {
			tvOrderState.setText("配送中");
			btnDeal.setText("联系买家");
		} else if ((orderBean.getState() + "").equals("1")) {
			tvOrderState.setText("已完成");
			view.setVisibility(View.GONE);
			btnCancel.setVisibility(View.GONE);
			btnDeal.setText("联系买家");
		} else if ((orderBean.getState() + "").equals("-1")) {
			tvOrderState.setText("已取消");
			btnCancel.setText("查看理由");
			btnDeal.setText("联系买家");
		} else if ((orderBean.getState() + "").equals("6")) {
			tvOrderState.setText("退款中");
			btnCancel.setText("查看理由");
			btnCancel.setVisibility(View.GONE);
			view.setVisibility(View.GONE);
			btnDeal.setText("联系买家");
		} else if ((orderBean.getState() + "").equals("7")) {
			tvOrderState.setText("已退款");
			btnCancel.setText("查看理由");
			btnCancel.setVisibility(View.GONE);
			view.setVisibility(View.GONE);
			btnDeal.setText("联系买家");
		} else if ((orderBean.getState() + "").equals("5")) {
			tvOrderState.setText("已支付");
			btnCancel.setText("取消订单");
			btnDeal.setText("处理订单");
			llorderbeiz.setVisibility(View.VISIBLE);
		}

		tvUserName.setText(orderBean.getAddress().getName());
		tvUserTel.setText(orderBean.getAddress().getMobile());
		if ((orderBean.getLocation() + "").equals("")
				|| (orderBean.getLocation() + "").equals("null")
				|| (orderBean.getLocation() + "").equals(null)) {
			tvUserLocation.setText("暂无小区");
		} else {
			if ((orderBean.getCompany() + "").equals("")
					|| (orderBean.getCompany() + "").equals("null")
					|| (orderBean.getCompany() + "").equals(null)) {
				tvUserLocation.setText(orderBean.getLocation().getName());
			} else {
				tvUserLocation.setText(orderBean.getLocation().getName() + "("
						+ orderBean.getCompany().getName() + ")");
			}
		}

		tvUserAddr.setText(orderBean.getAddress().getAddr());
		tvOrderData.setText(orderBean.getAddTime());
		tvOrderNo.setText(orderBean.getNo());
		tvSumMoney.setText(orderBean.getPrice());
		if (!StringUtil.isBlank(orderBean.getRemark())) {
			tvbeizhu.setText(orderBean.getRemark());
		}

		itemsList = orderBean.getItems();
		lvOrderList = (ListView) findViewById(R.id.lvOrderList);
		myorderlistAdapter = new MyOrderDetailsListAdapter(
				DingDanItemActivity.this);
		setListViewHeight();
		lvOrderList.setAdapter(myorderlistAdapter);

	}

	private void setListViewHeight() {
		if (myorderlistAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < myorderlistAdapter.getCount(); i++) {
			View listItem = myorderlistAdapter.getView(i, null, lvOrderList);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = lvOrderList.getLayoutParams();
		params.height = totalHeight
				+ (lvOrderList.getDividerHeight() * (myorderlistAdapter
						.getCount() - 1));
		((MarginLayoutParams) params).setMargins(20, 10, 20, 10);
		lvOrderList.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnDeal:
			System.out.println(strState + "点击状态值判断");
			if (strState.equals("0,5")) {
				AlertDialog.Builder backbuilder = new AlertDialog.Builder(
						DingDanItemActivity.this);
				backbuilder.setTitle("确定要处理订单吗？");
				backbuilder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new LoadOrderDealTask(SharedPrefUtil
										.getToken(DingDanItemActivity.this), id)
										.execute();
							}
						});
				backbuilder.setNegativeButton("否",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				backbuilder.create().show();

			} else {
				String TelNo = orderBean.getAddress().getMobile().toString();
				Intent Callintent = new Intent(Intent.ACTION_DIAL,
						Uri.parse("tel:" + TelNo));
				Callintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Callintent);
			}
			break;

		case R.id.btnCancel:
			System.out.println("点击率");
			if (strState.equals("-1")) {
				// 查看取消理由
				System.out.println("点击率");
				new LoadReasonTask(
						SharedPrefUtil.getToken(DingDanItemActivity.this), id,
						"-1").execute();
			}

			if (strState.equals("2")) {
				OrderCancelDialog.Builder builder = new OrderCancelDialog.Builder(
						DingDanItemActivity.this);
				builder.setTitle("取消订单");
				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.setNegativeButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								System.out.println("确定");
								new LoadOrderCancelTask(SharedPrefUtil
										.getToken(DingDanItemActivity.this),
										id, Constants.cancelContent).execute();
							}
						});
				builder.create().show();
			}
			if (strState.equals("0,5")) {
				OrderCancelDialog.Builder builder = new OrderCancelDialog.Builder(
						DingDanItemActivity.this);
				builder.setTitle("取消订单");
				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.setNegativeButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								System.out.println("确定");
								new LoadOrderCancelTask(SharedPrefUtil
										.getToken(DingDanItemActivity.this),
										id, Constants.cancelContent).execute();
							}
						});
				builder.create().show();
			}
			break;
		default:
			break;
		}
	}

	private void CancelResult(String result) {
		AlertDialog.Builder abuilder = new AlertDialog.Builder(
				DingDanItemActivity.this);
		abuilder.setTitle("取消理由:");
		if (!StringUtil.isBlank(result)) {
			abuilder.setMessage(result);
		} else {
			abuilder.setMessage("暂无理由");
		}
		abuilder.create().show();
	}

	/**
	 * 订单详情页面、商品列表适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyOrderDetailsListAdapter extends BaseAdapter {
		private Context mContext;
		ItemsBean itemsBean;

		public MyOrderDetailsListAdapter(Context context) {
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.order_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.ivProductImg = (ImageView) convertView
						.findViewById(R.id.ivProductImg);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.ivtuangou = (ImageView) convertView
						.findViewById(R.id.ivtuangou);
				viewHolder.ivcuxiao = (ImageView) convertView
						.findViewById(R.id.ivcuxiao);
				viewHolder.tvNum = (TextView) convertView
						.findViewById(R.id.tvNum);
				viewHolder.tvProductPrice = (TextView) convertView
						.findViewById(R.id.tvProductPrice);
				viewHolder.ivcuxiaoY = (ImageView) convertView
						.findViewById(R.id.ivcuxiaoY);
				viewHolder.ivtuangouY = (ImageView) convertView
						.findViewById(R.id.ivtuangouY);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			itemsBean = itemsList.get(position);
			if (itemsBean.getProduct().getIsGroupon().equals(true)
					|| itemsBean.getProduct().getIsGroupon().equals("true")) {
				viewHolder.ivtuangouY.setVisibility(View.VISIBLE);
				viewHolder.ivtuangou.setVisibility(View.GONE);
			} else {
				viewHolder.ivtuangouY.setVisibility(View.GONE);
				viewHolder.ivtuangou.setVisibility(View.VISIBLE);
			}
			if (itemsBean.getProduct().getIsPromotion().equals(true)
					|| itemsBean.getProduct().getIsPromotion().equals("true")) {
				viewHolder.ivcuxiaoY.setVisibility(View.VISIBLE);
				viewHolder.ivcuxiao.setVisibility(View.GONE);
			} else {
				viewHolder.ivcuxiaoY.setVisibility(View.GONE);
				viewHolder.ivcuxiao.setVisibility(View.VISIBLE);
			}
			viewHolder.tvProductName.setText(itemsBean.getProduct().getName());
			viewHolder.tvNum.setText(itemsBean.getQuantity());
			viewHolder.tvProductPrice.setText("￥" + itemsBean.getPrice() + "元");
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(itemsBean.getProduct().getImg(),
							viewHolder.ivProductImg);

			return convertView;
		}
	}

	/**
	 * 订单详细信息
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadOrderDetailTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;

		protected LoadOrderDetailTask(String accessToken, String id) {
			this.accessToken = accessToken;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new OrderHelper().OrderDetail(accessToken, id);
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
						orderBean = JSON.parseObject(
								result.getJSONObject("order").toString(),
								OrderBean.class);

						fillData();
						progressbar.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						Toast.makeText(DingDanItemActivity.this,
								result.getInt("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(DingDanItemActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(DingDanItemActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
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
				pd = ProgressDialog.createLoadingDialog(
						DingDanItemActivity.this, "正在提交请求……");
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
						Toast.makeText(DingDanItemActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(DingDanItemActivity.this, "msg",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(DingDanItemActivity.this, "数据加载失败.",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(DingDanItemActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
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
				pd = ProgressDialog.createLoadingDialog(
						DingDanItemActivity.this, "正在提交请求……");
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
						Toast.makeText(DingDanItemActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(DingDanItemActivity.this, "msg",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(DingDanItemActivity.this, "数据加载失败.",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(DingDanItemActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}
	}

	/**
	 * 订单取消理由
	 * 
	 * 
	 */
	class LoadReasonTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String orderId;
		private String state;

		protected LoadReasonTask(String accessToken, String orderId,
				String state) {
			this.accessToken = accessToken;
			this.orderId = orderId;
			this.state = state;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			try {
				// 返回一个 JSONObject
				return new OrderHelper()
						.stepreason(accessToken, orderId, state);
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

						List<StepBean> temp = StepBean.constractList(result
								.getJSONObject("order").getJSONArray("steps"));
						stepsList.addAll(temp);
						// orderBean = JSON.parseObject(
						// result.getJSONObject("order").toString(),
						// OrderBean.class);
						// orderBean.getItems().
						// mylistAdapter.notifyDataSetChanged();
						CancelResult(stepsList.get(0).getContent());
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(DingDanItemActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				} catch (SystemException e) {
					e.printStackTrace();
				}
			} else {
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

}
