package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class SearchXiaoQuTwoActivity extends UIBaseActivity implements
		OnClickListener, IXListViewListener {
	private Double lat;
	private Double lng;
	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;
	// ListView
	private XListView XlvLocation;
	private List<LocationBean> SearchXiaoQuTwoList = new ArrayList<LocationBean>();
	private MyListAdapter mylistAdapter;

	static class ViewHolder {
		TextView tvLocationName;
		Button btnApplyJoin;
		TextView tvendtime;
		TextView tvdistrict;
		LinearLayout llDetail;
	}

	public static SearchXiaoQuTwoActivity instance = null;

	private List<LocationBean> SearchTwoList = new ArrayList<LocationBean>();
	private MySearchAdapter mysearchAdapter;
	private ListView lvSearchTwo;
	private TextView tvNoData;
	private EditText etSearchtwo;
	private Button btnDeleteSearch;
	private Button btncity;
	private LinearLayout llgocity;

	// private Handler handler = new Handler();

	LocationManager lm;
	private String cityName = "";
	private int num = 1;
	private String name = "";
	// private String

	private Handler startHandler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_xiaoqu_list_two_layout);
		instance = this;
		findview();
		if (StringUtil.isBlank(SharedPrefUtil
				.getCityName(SearchXiaoQuTwoActivity.this))) {
			SharedPrefUtil.setCityName(SearchXiaoQuTwoActivity.this, "");
		}
		// startHandler.sendEmptyMessage(1);
		// if (Constants.FINISH.equals("2")) {
		// SearchXiaoQuTwoList.clear();
		// new LoadSearchXiaoQuTwoListTask(SharedPrefUtil.getShopBean(
		// SearchXiaoQuTwoActivity.this).getId(),
		// SharedPrefUtil.getCityName(SearchXiaoQuTwoActivity.this),
		// begin).execute();
		// }
		if (!StringUtil.isBlank(SharedPrefUtil
				.getCityName(SearchXiaoQuTwoActivity.this))) {
			SearchXiaoQuTwoList.clear();
			new LoadSearchXiaoQuTwoListTask(SharedPrefUtil.getShopBean(
					SearchXiaoQuTwoActivity.this).getId(),
					SharedPrefUtil.getCityName(SearchXiaoQuTwoActivity.this),
					begin).execute();
		} else {
			SearchXiaoQuTwoList.clear();
			new LoadSearchXiaoQuTwoListTask(SharedPrefUtil.getShopBean(
					SearchXiaoQuTwoActivity.this).getId(), "", begin).execute();
		}
	}

	private void findview() {
		super.findView();
		lvSearchTwo = (ListView) findViewById(R.id.lvSearchTwo);

		btncity = (Button) findViewById(R.id.btncity);
		btncity.setText("全国");
		btncity.setOnClickListener(this);
		llgocity = (LinearLayout) findViewById(R.id.llgocity);
		llgocity.setOnClickListener(this);
		btnDeleteSearch = (Button) findViewById(R.id.btnDeleteSearch);
		btnDeleteSearch.setOnClickListener(this);

		XlvLocation = (XListView) findViewById(R.id.XlvLocation);
		XlvLocation.setPullLoadEnable(true);
		XlvLocation.setXListViewListener(this);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		mHandler = new Handler();
		etSearchtwo = (EditText) findViewById(R.id.etSearchtwo);
		etSearchtwo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!StringUtil.isBlank(etSearchtwo.getText().toString())) {
					XlvLocation.setVisibility(View.GONE);
					lvSearchTwo.setVisibility(View.VISIBLE);
					// converterToFirstSpell(etSearchtwo.getText().toString());
					new LoadLocationListTask(SharedPrefUtil.getShopBean(
							SearchXiaoQuTwoActivity.this).getId(), etSearchtwo
							.getText().toString()).execute();
				} else {
					XlvLocation.setVisibility(View.VISIBLE);
					lvSearchTwo.setVisibility(View.GONE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// startHandler = new Handler();
		// startHandler.postDelayed(runnable, 500);
	}

	// private Handler startHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	// case 1:
	// // startHandler.postDelayed(runnable, 200);
	// runnable.run();
	// break;
	// case 2:
	// String strcityname = (String) msg.obj;
	// btncity.setText(strcityname);
	// break;
	// default:
	// break;
	// }
	// };
	// };

	@Override
	protected void onResume() {
		super.onResume();
		if (!StringUtil.isBlank(SharedPrefUtil
				.getCityName(SearchXiaoQuTwoActivity.this))) {
			btncity.setText(SharedPrefUtil
					.getCityName(SearchXiaoQuTwoActivity.this));
		} else {
			btncity.setText("全国");
		}
	}

	/**
	 * 全部小区列表适配器(XListView)
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		List<LocationBean> locationList;
		LocationBean locationBean;

		public MyListAdapter(Context context, List<LocationBean> locationList) {
			this.mContext = context;
			this.locationList = locationList;

		}

		@Override
		public int getCount() {
			return locationList.size();

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
						R.layout.xiaoqu_detail_one_list_item, null);

				viewHolder = new ViewHolder();
				viewHolder.llDetail = (LinearLayout) convertView
						.findViewById(R.id.llDetail);
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
			locationBean = locationList.get(position);
			viewHolder.llDetail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("");
				}
			});
			viewHolder.tvendtime.setText(locationBean.getName());
			viewHolder.tvLocationName.setText(locationBean.getName());
			viewHolder.tvdistrict.setText(locationBean.getDistrict().getName());
			if (!(locationBean.getDistribution() + "").equals("null")) {
				if ((locationBean.getDistribution().getEnabled() + "")
						.equals("true")) {
					if (locationBean.getDistribution().getState().equals("1")) {
						viewHolder.btnApplyJoin.setText("已加入");
						viewHolder.btnApplyJoin
								.setBackgroundResource(R.drawable.added);
						viewHolder.btnApplyJoin
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Toast.makeText(
												SearchXiaoQuTwoActivity.this,
												"您已加入，请勿重复加入",
												Toast.LENGTH_SHORT).show();
									}
								});

					}
					if (locationBean.getDistribution().getState().equals("0")) {
						viewHolder.btnApplyJoin.setText("审核中");
						viewHolder.btnApplyJoin
								.setBackgroundResource(R.drawable.delete_ok_selector);
						viewHolder.btnApplyJoin
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Toast.makeText(
												SearchXiaoQuTwoActivity.this,
												"正在审核中", Toast.LENGTH_SHORT)
												.show();
									}
								});
					}
					if (locationBean.getDistribution().getState().equals("-2")) {
						viewHolder.btnApplyJoin.setText("已驳回");
						viewHolder.btnApplyJoin
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(
										SearchXiaoQuTwoActivity.this,
										"已驳回不能再申请", Toast.LENGTH_SHORT)
										.show();
							}
						});
					}

				}
			} else {
				viewHolder.btnApplyJoin.setText("申请加入");
				viewHolder.btnApplyJoin
						.setBackgroundResource(R.drawable.delete_ok_selector);
				viewHolder.btnApplyJoin.setTag(position);
				viewHolder.btnApplyJoin
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								SharedPrefUtil
										.setFistSearch(SearchXiaoQuTwoActivity.this);
								final int index = (Integer) v.getTag();
								new LoadApplyJoinTask(
										SharedPrefUtil
												.getToken(SearchXiaoQuTwoActivity.this),
										SharedPrefUtil.getShopBean(
												SearchXiaoQuTwoActivity.this)
												.getId(), locationList.get(
												index).getId()).execute();

							}
						});
			}
			
			
			return convertView;
		}

	}

	/**
	 * 全部小区列表
	 */
	class LoadSearchXiaoQuTwoListTask extends
			AsyncTask<String, Void, JSONObject> {
		private String shopId;
		private String cityName;
		private int begin;

		protected LoadSearchXiaoQuTwoListTask(String shopId, String cityName,
				int begin) {
			this.shopId = shopId;
			this.begin = begin;
			this.cityName = cityName;
		}

		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new LocationHelper().AllLocations(shopId, cityName,
						begin);
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
						// SearchXiaoQuTwoList.clear();
						//返回的小区的json数据
						//System.out.println(result.getJSONArray("locations"));
						
						SearchXiaoQuTwoList.addAll(temp);

						// 适配器加载
						// if (mylistAdapter == null)
						mylistAdapter = new MyListAdapter(
								SearchXiaoQuTwoActivity.this,
								SearchXiaoQuTwoList);
						XlvLocation.setAdapter(mylistAdapter);
						total = result.getInt("totalPage");
						if (total == 1) {
							XlvLocation.setPullLoadEnable(false);
						}
						progressbar.setVisibility(View.GONE);
						onLoad();
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
						Toast.makeText(SearchXiaoQuTwoActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(SearchXiaoQuTwoActivity.this,
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
	 * 输入搜索小区下拉框列表
	 */
	class LoadLocationListTask extends AsyncTask<String, Void, JSONObject> {
		private String shopId;
		private String name;

		protected LoadLocationListTask(String shopId, String name) {
			this.shopId = shopId;
			this.name = name;
		}

		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new LocationHelper().SearchLocations(shopId, name);
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
						SearchTwoList.clear();
						SearchTwoList.addAll(temp);
						mysearchAdapter = new MySearchAdapter(
								SearchXiaoQuTwoActivity.this, SearchTwoList);
						lvSearchTwo.setAdapter(mysearchAdapter);
					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	/**
	 * 
	 */
	private class MySearchAdapter extends BaseAdapter {
		private Context mContext;
		List<LocationBean> locationList;
		LocationBean locationBean;

		// ShopBean tradeBean;

		public MySearchAdapter(Context context, List<LocationBean> locationList) {
			this.mContext = context;
			this.locationList = locationList;

		}

		@Override
		public int getCount() {
			return locationList.size();

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
			locationBean = locationList.get(position);
			viewHolder.tvendtime.setText(locationBean.getName());
			viewHolder.tvLocationName.setText(locationBean.getName());
			viewHolder.tvdistrict.setText(locationBean.getDistrict().getName());
			if (!(locationBean.getDistribution() + "").equals("null")) {
				if ((locationBean.getDistribution().getEnabled() + "")
						.equals("true")) {
					if (locationBean.getDistribution().getState().equals("1")) {
						viewHolder.btnApplyJoin.setText("已加入");
						viewHolder.btnApplyJoin
						.setBackgroundResource(R.drawable.added);
						viewHolder.btnApplyJoin
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Toast.makeText(
												SearchXiaoQuTwoActivity.this,
												"您已加入，请勿重复加入",
												Toast.LENGTH_SHORT).show();
									}
								});

					}
					if (locationBean.getDistribution().getState().equals("0")) {
						viewHolder.btnApplyJoin.setText("审核中");
						viewHolder.btnApplyJoin
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Toast.makeText(
												SearchXiaoQuTwoActivity.this,
												"正在审核中", Toast.LENGTH_SHORT)
												.show();
									}
								});
					}
					if (locationBean.getDistribution().getState().equals("-2")) {
						viewHolder.btnApplyJoin.setText("已驳回");
						viewHolder.btnApplyJoin
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(
										SearchXiaoQuTwoActivity.this,
										"已驳回不能再申请", Toast.LENGTH_SHORT)
										.show();
							}
						});
					}

				}
			} else {
				viewHolder.btnApplyJoin.setText("申请加入");
				viewHolder.btnApplyJoin.setTag(position);
				viewHolder.btnApplyJoin
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								SharedPrefUtil
										.setFistSearch(SearchXiaoQuTwoActivity.this);
								final int index = (Integer) v.getTag();
								new LoadApplyJoinTask(
										SharedPrefUtil
												.getToken(SearchXiaoQuTwoActivity.this),
										SharedPrefUtil.getShopBean(
												SearchXiaoQuTwoActivity.this)
												.getId(), locationList.get(
												index).getId()).execute();
							}
						});
			}
			return convertView;
		}
	}

	private static final int CITYCHANGE = 446;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnDeleteSearch:
			etSearchtwo.setText("");
			etSearchtwo.setHint("请输入小区名称");
			break;
		case R.id.llgocity:
			Intent citychange = new Intent(SearchXiaoQuTwoActivity.this,
					CityChangeActivity.class);
			startActivityForResult(citychange, CITYCHANGE);
			break;
		case R.id.btncity:
			Intent citychange1 = new Intent(SearchXiaoQuTwoActivity.this,
					CityChangeActivity.class);
			Constants.CITYGPS = false;
			startActivityForResult(citychange1, CITYCHANGE);

			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent result) {
		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case CITYCHANGE:
				begin = 1;
				totalLinShi = 1;
				total = 1;
				SearchXiaoQuTwoList.clear();
				tvNoData.setVisibility(View.GONE);
				XlvLocation.setVisibility(View.VISIBLE);
				XlvLocation.setPullLoadEnable(true);
				new LoadSearchXiaoQuTwoListTask(SharedPrefUtil.getShopBean(
						SearchXiaoQuTwoActivity.this).getId(),
						SharedPrefUtil
								.getCityName(SearchXiaoQuTwoActivity.this),
						begin).execute();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onRefresh() {
		XlvLocation.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadSearchXiaoQuTwoListTask(SharedPrefUtil.getShopBean(
							SearchXiaoQuTwoActivity.this).getId(),
							SharedPrefUtil
									.getCityName(SearchXiaoQuTwoActivity.this),
							begin).execute();

				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvLocation.noLoadMore();
			XlvLocation.setPullLoadEnable(false);
		}
	}

	private void onLoad() {
		XlvLocation.stopRefresh();
		XlvLocation.stopLoadMore(total);
	}

	/**
	 * 定位当前所在城市
	 */
	// Runnable runnable = new Runnable() {
	//
	// @Override
	// public void run() {
	// getLocation();
	//
	// }
	// };

	// public void getLocation() {
	// lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	// // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
	// List<String> lp = lm.getAllProviders();
	// for (String item : lp) {
	// Log.i("8023", "可用位置服务：" + item);
	// }
	//
	// if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
	// Toast.makeText(SearchXiaoQuTwoActivity.this, "您的网络定位未打开！",
	// Toast.LENGTH_SHORT).show();
	// SearchXiaoQuTwoActivity.this.startActivity(new Intent(
	// Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	// }
	//
	// String providerName = lm.NETWORK_PROVIDER;
	// Log.i("8023", "------位置服务：" + providerName);
	// Location location = lm.getLastKnownLocation(providerName);
	// updateView(location);
	//
	// lm.requestLocationUpdates(providerName, 120000, 8,
	// new LocationListener() {
	//
	// @Override
	// public void onStatusChanged(String arg0, int arg1,
	// Bundle arg2) {
	// }
	//
	// @Override
	// public void onProviderEnabled(String arg0) {
	// updateView(lm.getLastKnownLocation(arg0));
	// }
	//
	// @Override
	// public void onProviderDisabled(String arg0) {
	//
	// }
	//
	// @Override
	// public void onLocationChanged(Location arg0) {
	// // 当定位信息发生改变时，更新位置
	// updateView(arg0);
	// }
	// });
	// }
	//
	// public void updateView(Location newLocation) {
	//
	// if (newLocation != null) {
	//
	// Log.i("8023", "-------" + newLocation);
	// // 获取维度信息
	// double latitude = newLocation.getLatitude();
	// // 获取经度信息
	// double longitude = newLocation.getLongitude();
	// // 获取当前所在城市
	// String latLongString = "纬度:" + latitude + "\n经度:" + longitude;
	// List<Address> addList = null;
	// Geocoder ge = new Geocoder(SearchXiaoQuTwoActivity.this);
	// try {
	// addList = ge.getFromLocation(latitude, longitude, 1);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// System.out.println("详细地址信息" + addList);
	// if (addList != null && addList.size() > 0) {
	// for (int i = 0; i < addList.size(); i++) {
	// Address ad = addList.get(i);
	// latLongString += "\n";
	// latLongString += ad.getCountryName() + ";"
	// + ad.getLocality();
	// cityName = ad.getLocality();
	// }
	// }
	//
	// System.out.println("纬度:" + latitude + "\n经度:" + longitude);
	// cityName = cityName.substring(0, cityName.length() - 1);
	// SharedPrefUtil.setLat(SearchXiaoQuTwoActivity.this, latitude + "");
	// SharedPrefUtil.setLng(SearchXiaoQuTwoActivity.this, longitude + "");
	// // Message msg = startHandler.obtainMessage();
	// // msg.what = 2;
	// // msg.obj = cityName;
	// // startHandler.sendMessage(msg);
	//
	// // btncity.setText(cityName);
	// // SearchXiaoQuTwoList.clear();
	// // new LoadSearchXiaoQuTwoListTask(SharedPrefUtil.getShopBean(
	// // SearchXiaoQuTwoActivity.this).getId(), cityName, begin)
	// // .execute();
	//
	// }
	// }
}