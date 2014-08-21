package com.bangqu.yinwan.shop.ui;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {

	private Button btnLeft;
	private TextView tvTittle2;
	private Button btnRight;

	private TextView tvNoData;

	// XListView
	private XListView XlvCategoryList;
	private MyListAdapter mylistAdapter;
	private Handler mHandler;

	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;

	static class ViewHolder {
		TextView tvProductNO;
		TextView tvProductName;
		ImageView ivProductImg;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_layout);

		findview();

	}

	private void findview() {
		// TODO Auto-generated method stub
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle2 = (TextView) findViewById(R.id.tvTittle);
		tvTittle2.setText("分类首页");
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("添加");

		// ListView
		XlvCategoryList = (XListView) findViewById(R.id.Xlvcategory);
		XlvCategoryList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(CategoryActivity.this);
		XlvCategoryList.setAdapter(mylistAdapter);
		XlvCategoryList.setOnItemClickListener(this);
		mHandler = new Handler();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btnLeft:
			CategoryActivity.this.finish();
			break;

		default:
			break;

		}
	}

	/**
	 * 商品分类列表适配器(XListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;

		// ShopBean tradeBean;

		public MyListAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.category_item_layout, null);
				viewHolder = new ViewHolder();

				viewHolder.tvProductNO = (TextView) convertView
						.findViewById(R.id.tvProductNO);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.ivProductImg = (ImageView) convertView
						.findViewById(R.id.ivProductImg);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			// viewHolder.tvProductNO.setText(addressBean.getLocationName());
			// viewHolder.tvProductName.setText(addressBean.getName());
			// viewHolder.ivProductImg.setText(addressBean.getMobile());

			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// final int index = (Integer) v.getTag();
		Intent itemintent = new Intent(CategoryActivity.this,
				CategoryActivity.class);
		// itemintent.putExtra("AddressId", addressList.get(index).getId());
		// startActivityForResult(itemintent, HOME_MINE_LOGIN);
		startActivity(itemintent);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		XlvCategoryList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (totalLinShi < total) {
			Log.i("是否加载更多了", total + "");
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					// new LoadShopListTask().execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvCategoryList.setPullLoadEnable(false);
		}
	}

	private void onLoad() {
		XlvCategoryList.stopRefresh();
		XlvCategoryList.stopLoadMore(total);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}
