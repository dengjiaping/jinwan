package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.TurnoversBean;
import com.bangqu.yinwan.shop.helper.UserHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class DayListActivity extends UIBaseActivity implements OnClickListener,
		OnItemClickListener, IXListViewListener {
	private TextView tvTittle;
	private TextView tvdayyingye;
	private Button btnLeft;
	private Button btnRight;
	private XListView Xlvdaylist;
	private DayListAdapter daylistadapter;
	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;
	private List<TurnoversBean> daylist = new ArrayList<TurnoversBean>();
	private TextView tvnoaccount;

	static class ViewHolder {
		TextView tvmoneyitem;
		TextView tvdayitem;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.day_money_layout);
		findView();
		new LoadDayListTask(SharedPrefUtil.getToken(this), SharedPrefUtil
				.getShopBean(this).getId(), begin).execute();
		// new LoadDayListTask("56adb3a792a12be8a31aaf892943f21c", "1", begin)
		// .execute();
	}

	@Override
	public void findView() {
		super.findView();
		tvdayyingye = (TextView) findViewById(R.id.tvdayyingye);
		tvdayyingye.setText(getIntent().getStringExtra("yingye"));
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("每日营业额");
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		daylistadapter = new DayListAdapter(DayListActivity.this);
		Xlvdaylist = (XListView) findViewById(R.id.Xlvdaylist);
		Xlvdaylist.setAdapter(daylistadapter);
		Xlvdaylist.setPullLoadEnable(true);
		Xlvdaylist.setOnItemClickListener(this);
		Xlvdaylist.setXListViewListener(this);
		tvnoaccount = (TextView) findViewById(R.id.tvnoaccount);
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
	 * 每日营业额适配器
	 * 
	 */
	private class DayListAdapter extends BaseAdapter {
		private Context mContext;
		TurnoversBean turnoversBean;

		public DayListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return daylist.size();
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
						R.layout.day_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvmoneyitem = (TextView) convertView
						.findViewById(R.id.tvmoneyitem);
				viewHolder.tvdayitem = (TextView) convertView
						.findViewById(R.id.tvdayitem);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			turnoversBean = daylist.get(position);
			if (position == 0) {
				viewHolder.tvmoneyitem.setTextColor(getResources().getColor(
						R.color.color_red_two));
			} else {
				viewHolder.tvmoneyitem.setTextColor(getResources().getColor(
						R.color.color_text_black));
			}
			viewHolder.tvmoneyitem.setText("+" + turnoversBean.getPrice()
					+ " 元");
			viewHolder.tvdayitem.setText(turnoversBean.getAddTime()
					.subSequence(0, 10));
			convertView.setTag(viewHolder);
			return convertView;
		}
	}

	/**
	 * 每日营业额列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadDayListTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private int begin;

		protected LoadDayListTask(String accessToken, String shopId, int begin) {
			this.accessToken = accessToken;
			this.shopId = shopId;
			this.begin = begin;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			try {
				// 返回一个 JSONObject
				return new UserHelper().turnover(accessToken, shopId, begin,
						"addTime", true);
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
						List<TurnoversBean> temp = TurnoversBean
								.constractList(result.getJSONArray("turnovers"));
						daylist.addAll(temp);
						daylistadapter.notifyDataSetChanged();
						total = result.getInt("totalPage");
						if (total == 1) {
							Xlvdaylist.setPullLoadEnable(false);
						}
						onLoad();
						progressbar.setVisibility(View.GONE);
						Xlvdaylist.setVisibility(View.VISIBLE);
						tvnoaccount.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						Xlvdaylist.setVisibility(View.GONE);
						tvnoaccount.setVisibility(View.VISIBLE);
						Xlvdaylist.setPullLoadEnable(false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(DayListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("DayListActivity", "SystemException");
				} catch (SystemException e) {
					e.printStackTrace();
					Log.i("DayListActivity", "SystemException");
				}
			}

		}
	}

	@Override
	public void onRefresh() {
		Xlvdaylist.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadDayListTask(SharedPrefUtil
							.getToken(DayListActivity.this), SharedPrefUtil
							.getShopBean(DayListActivity.this).getId(), begin)
							.execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			Xlvdaylist.setPullLoadEnable(false);
			Xlvdaylist.noLoadMore();
		}
	}

	private void onLoad() {
		Xlvdaylist.stopRefresh();
		Xlvdaylist.stopLoadMore(total);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
