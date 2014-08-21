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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.OrderBean;
import com.bangqu.yinwan.shop.helper.OwnerHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView;

public class OwnerDingDanThree extends UIBaseActivity implements
		OnClickListener, OnItemClickListener {
	private XListView XlvOrderList;
	private MyListAdapter mylistAdapter;
	private List<OrderBean> orderList = new ArrayList<OrderBean>();
	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;
	private RelativeLayout llCommon;

	static class ViewHolder {
		TextView tvOrderNO;
		TextView tvProductSize;
		TextView tvOrderMoney;
		TextView tvOrderTime;
		TextView tvState;
		ImageView ivdingdan;

		TextView tvTime;
		Button btlijichuli;
		GridView gridview;
		TextView tvzhuangtai;
	}

	// 暂无订单
	private TextView tvNoData;

	// 0未处理，2处理中，3已发货，1已完成，-1已取消

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dingdan_list_layout);
		llCommon = (RelativeLayout) findViewById(R.id.llCommon);
		llCommon.setVisibility(View.GONE);

		findView();
	}

	protected void onMyResume() {
		orderList.clear();
		new LoadOrderListTask(SharedPrefUtil.getOwnerID(this), "1", begin)
				.execute();
	}

	public void findView() {
		super.findView();
		tvNoData = (TextView) findViewById(R.id.tvNoData);

		// XListView
		XlvOrderList = (XListView) findViewById(R.id.XlvOrderList);
		XlvOrderList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(OwnerDingDanThree.this);
		XlvOrderList.setAdapter(mylistAdapter);
		XlvOrderList.setOnItemClickListener(this);
		mHandler = new Handler();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			OwnerDingDanThree.this.finish();
			break;

		case R.id.btnRight:
			Intent intent = new Intent(OwnerDingDanThree.this, DingDanOne.class);
			startActivity(intent);
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
						R.layout.dingdan_item, null);
				viewHolder = new ViewHolder();

				viewHolder.tvOrderNO = (TextView) convertView
						.findViewById(R.id.tvOrderNO);
				viewHolder.tvProductSize = (TextView) convertView
						.findViewById(R.id.tvProductSize);
				viewHolder.tvOrderMoney = (TextView) convertView
						.findViewById(R.id.tvOrderMoney);
				viewHolder.tvOrderTime = (TextView) convertView
						.findViewById(R.id.tvOrderTime);
				viewHolder.ivdingdan = (ImageView) convertView
						.findViewById(R.id.ivdingdan);
				viewHolder.btlijichuli = (Button) convertView
						.findViewById(R.id.btlijichuli);
				viewHolder.tvzhuangtai = (TextView) convertView
						.findViewById(R.id.tvzhuangtai);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			orderBean = orderList.get(position);

			// imgList.clear();
			// for (ProductBean productBean : orderBean.getProducts()) {
			// imgList.add(productBean.getImg() + "");
			// }
			viewHolder.tvOrderNO.setText(orderBean.getNo());
			viewHolder.tvProductSize.setText(orderBean.getProductSize()
					+ "个包裹(" + orderBean.getQuantity() + "件商品)");
			viewHolder.tvOrderMoney.setText("￥" + orderBean.getPrice());
			viewHolder.btlijichuli.setVisibility(View.GONE);
			// viewHolder.tvzhuangtai.setText("已完成");
			if ((orderBean.getState() + "").equals("0")) {
				viewHolder.tvzhuangtai.setText("待配送");
			} else if ((orderBean.getState() + "").equals("2")) {
				viewHolder.tvzhuangtai.setText("配送中");
			} else if ((orderBean.getState() + "").equals("1")) {
				viewHolder.tvzhuangtai.setText("已完成");
			} else if ((orderBean.getState() + "").equals("-1")) {
				viewHolder.tvzhuangtai.setText("已取消");
			} else if ((orderBean.getState() + "").equals("6")) {
				viewHolder.tvzhuangtai.setText("退款中");
			} else if ((orderBean.getState() + "").equals("7")) {
				viewHolder.tvzhuangtai.setText("已退款");
			}
			// viewHolder.tvState.setText("未处理");
			viewHolder.tvOrderTime.setText(orderBean.getAddTime());
			for (int i = 0; i < Integer.parseInt(orderBean.getProductSize()); i++) {
				((CommonApplication) getApplicationContext()).getImgLoader()
						.DisplayImage(
								orderList.get(position).getProducts().get(i)
										.getImg(), viewHolder.ivdingdan);
			}
			viewHolder.btlijichuli.setTag(position);
			viewHolder.btlijichuli.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final int index = (Integer) v.getTag();
					Intent itemintent = new Intent(OwnerDingDanThree.this,
							DingDanItemActivity.class);
					itemintent
							.putExtra("OrderId", orderList.get(index).getId());
					itemintent.putExtra("state", "1");
					startActivity(itemintent);
				}
			});
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
		private String id;
		private String state;
		private int begin;

		protected LoadOrderListTask(String id, String state, int begin) {
			this.id = id;
			this.state = state;
			this.begin = begin;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			try {
				// 返回一个 JSONObject
				return new OwnerHelper().Ownerorder0(id, state, begin);
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
					Toast.makeText(OwnerDingDanThree.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(OwnerDingDanThree.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
			}

		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent itemintent = new Intent(OwnerDingDanThree.this,
				DingDanItemActivity.class);
		itemintent.putExtra("OrderId", orderList.get(arg2 - 1).getId());
		itemintent.putExtra("state", "1");
		startActivity(itemintent);
	}

	public void onRefresh() {
		XlvOrderList.stopRefresh();
	}

	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadOrderListTask(SharedPrefUtil
							.getOwnerID(OwnerDingDanThree.this), "1", begin)
							.execute();
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}