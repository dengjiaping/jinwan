package com.bangqu.yinwan.shop.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.CityBean;
import com.bangqu.yinwan.shop.helper.CityHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.CharacterParser;
import com.bangqu.yinwan.shop.util.ClearEditText;
import com.bangqu.yinwan.shop.util.DataBaseAdapter;
import com.bangqu.yinwan.shop.util.NetUtil;
import com.bangqu.yinwan.shop.util.PinyinComparator;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.SortAdapter;
import com.bangqu.yinwan.shop.util.SortModel;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.SideBar;
import com.bangqu.yinwan.shop.widget.SideBar.OnTouchingLetterChangedListener;

public class CityChangeActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener {

	private Button btnLeft;
	private Button btnRight;
	private TextView tvTittle;

	LocationManager lm;

	// ListView
	// 全部城市列表
	private List<CityBean> cityList = new ArrayList<CityBean>();
	private List<CityBean> cityList1 = new ArrayList<CityBean>();

	// 定位城市
	private Button btnGPSCity;
	public double lat;
	public double lng;
	private String cityName = "正在定位...";
	private LinearLayout llgps;
	private LinearLayout llGPSCity;

	// private LocationManagerProxy aMapLocManager = null;
	// private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler startHandler;

	private TextView tvHotCity1;
	private TextView tvHotCity2;
	private TextView tvHotCity3;
	private TextView tvHotCity4;
	private TextView tvHotCity5;
	private String strHotCity1;
	private String strHotCity2;
	private String strHotCity3;
	private String strHotCity4;
	private String strHotCity5;
	private List<CityBean> hotcityList = new ArrayList<CityBean>();

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	// 数据库缓存
	private DataBaseAdapter dataBaseAdapter;
	private CityBean cityBean;
	private ListView sortListView;
	private View headview;

	static class ViewHolder {
		TextView tvCityName;
	}

	// 所有城市中文检索列表
	private LinearLayout llNoEdit;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private LinearLayout llclearsearch;

	private EditText etsearch;
	private ListView lvEditSearch;
	private MyListAdapter myListAdapter;;
	private FrameLayout flid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏视图
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_change_layout);

		findView();

		if (NetUtil.checkNet(this)) {
			new LoadCityListTask("", SharedPrefUtil.getopenversion(this))
					.execute();
			new LoadHotCityListTask("shopSize", "true").execute();
		} else {
			Toast.makeText(CityChangeActivity.this, "请检查网络！", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void findView() {
		super.findView();

		llclearsearch = (LinearLayout) findViewById(R.id.llclearsearch);
		flid = (FrameLayout) findViewById(R.id.flid);
		lvEditSearch = (ListView) findViewById(R.id.lvEditSearch);
		lvEditSearch.setOnItemClickListener(this);
		myListAdapter = new MyListAdapter(this);
		lvEditSearch.setAdapter(myListAdapter);
		etsearch = (EditText) findViewById(R.id.etsearch);
		if (Constants.CITYGPS) {
			llclearsearch.setVisibility(View.GONE);
			etsearch.setVisibility(View.VISIBLE);
			flid.setVisibility(View.VISIBLE);
		} else {
			llclearsearch.setVisibility(View.VISIBLE);
			etsearch.setVisibility(View.GONE);
		}

		etsearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (StringUtil.isBlank(etsearch.getText().toString())) {
					flid.setVisibility(View.VISIBLE);
					lvEditSearch.setVisibility(View.GONE);
				} else {
					flid.setVisibility(View.GONE);
					lvEditSearch.setVisibility(View.VISIBLE);
					new LoadCityList1Task(etsearch.getText().toString().trim(),
							"11").execute();
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
		// 数据库缓存
		dataBaseAdapter = ((CommonApplication) getApplicationContext())
				.getDbAdapter();
		cityList = dataBaseAdapter.findAllChildCity();
		// 标题栏
		btnLeft = (Button) findViewById(R.id.btnLeft2);
		btnLeft.setOnClickListener(this);
		tvTittle = (TextView) findViewById(R.id.tvTittle2);
		tvTittle.setText("城市选择");
		btnRight = (Button) findViewById(R.id.btnRight2);
		btnRight.setVisibility(View.INVISIBLE);
		llNoEdit = (LinearLayout) findViewById(R.id.llNoEdit);

		sortListView = (ListView) findViewById(R.id.lvSvCityList);

		headview = getLayoutInflater().inflate(R.layout.include_location_title,
				null);
		llNoEdit = (LinearLayout) headview.findViewById(R.id.llNoEdit);
		sortListView.addHeaderView(headview, null, true);
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				SharedPrefUtil.setCityName(CityChangeActivity.this,
						((SortModel) adapter.getItem(position - 1)).getName());
				// id缓存
				Constants.CITYCHANGEID = SourceDateList.get(position - 1).getId();
				Constants.CITYCHANGENANME = SourceDateList.get(position - 1)
						.getName();
				System.out.println(SourceDateList.get(position - 1).getName());
				System.out.println(SourceDateList.get(position - 1).getId());

				setResult(RESULT_OK);
				CityChangeActivity.this.finish();
			}
		});
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// llhotno.setVisibility(View.GONE);
				// 当输入框里面的值为空，更新为原来的s列表，否则为过滤数据列表
				filterData(s.toString());
				if (!StringUtil.isBlank(mClearEditText.getText().toString()
						.trim())) {
					sortListView.setHeaderDividersEnabled(true);
					llNoEdit.setVisibility(View.GONE);
				} else {
					llNoEdit.setVisibility(View.VISIBLE);
					sortListView.setHeaderDividersEnabled(false);
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
		// 定位城市
		btnGPSCity = (Button) findViewById(R.id.btnGPSCity);
		btnGPSCity.setOnClickListener(this);

		btnGPSCity.setText(cityName);
		if (!StringUtil.isBlank(SharedPrefUtil.getgpscity(this))) {
			btnGPSCity.setText(SharedPrefUtil.getgpscity(this));
		}
		llGPSCity = (LinearLayout) findViewById(R.id.llGPSCity);
		llgps = (LinearLayout) findViewById(R.id.llgps);
		if (Constants.CITYGPS) {
			llgps.setVisibility(View.GONE);
			llGPSCity.setVisibility(View.GONE);
		} else {
			llgps.setVisibility(View.VISIBLE);
			llGPSCity.setVisibility(View.VISIBLE);
		}
		Constants.oldCityName = SharedPrefUtil
				.getCityName(CityChangeActivity.this);
		tvHotCity1 = (TextView) findViewById(R.id.tvHotCity1);
		tvHotCity2 = (TextView) findViewById(R.id.tvHotCity2);
		tvHotCity3 = (TextView) findViewById(R.id.tvHotCity3);
		tvHotCity4 = (TextView) findViewById(R.id.tvHotCity4);
		tvHotCity5 = (TextView) findViewById(R.id.tvHotCity5);
		tvHotCity1.setOnClickListener(this);
		tvHotCity2.setOnClickListener(this);
		tvHotCity3.setOnClickListener(this);
		tvHotCity4.setOnClickListener(this);
		tvHotCity5.setOnClickListener(this);

		startHandler = new Handler();
		startHandler.postDelayed(runnable, 100);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position + 1);
				}
			}
		});
		if (!SharedPrefUtil.isFistDatabase(this)) {
			fillData();
		}

	}

	@Override
	public void fillData() {
		super.fillData();
		// SourceDateList = filledDataone(getResources().getStringArray(
		// R.array.citylist));
		progressbar.setVisibility(View.GONE);
		SourceDateList = filledData(cityList);
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(CityChangeActivity.this, SourceDateList);
		sortListView.setAdapter(adapter);
	}

	/**
	 * 热门城市列表（5个）
	 * 
	 */
	class LoadHotCityListTask extends AsyncTask<String, Void, JSONObject> {
		private String order;
		private String desc;

		protected LoadHotCityListTask(String order, String desc) {
			this.order = order;
			this.desc = desc;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CityHelper().hotcity(order, desc);
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
						List<CityBean> temp2 = CityBean.constractList(result
								.getJSONArray("cities"));
						hotcityList = temp2;
						tvHotCity1.setText(hotcityList.get(0).getName());
						tvHotCity2.setText(hotcityList.get(1).getName());
						tvHotCity3.setText(hotcityList.get(2).getName());
						tvHotCity4.setText(hotcityList.get(3).getName());
						tvHotCity5.setText(hotcityList.get(4).getName());

						strHotCity1 = hotcityList.get(0).getId() + "";
						strHotCity2 = hotcityList.get(1).getId() + "";
						strHotCity3 = hotcityList.get(2).getId() + "";
						strHotCity4 = hotcityList.get(3).getId() + "";
						strHotCity5 = hotcityList.get(4).getId() + "";

						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(CityChangeActivity.this, "无数据",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CityChangeActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(CityChangeActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	/**
	 * 为sortModel填充数据
	 * 
	 * @param cityList2
	 * @return
	 */
	public List<SortModel> filledData(List<CityBean> cityList2) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < cityList2.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(cityList2.get(i).getName());
			sortModel.setId(cityList2.get(i).getId());
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(cityList2.get(i)
					.getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;

		} else {
			filterDateList.clear();
			int count = 0;
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
					count++;
				}
			}

			// 这里是1 说明你的筛选条件有问题
			System.out.println(count);
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft2:
			finish();
			break;
		case R.id.tvHotCity1:

			SharedPrefUtil.setCityName(CityChangeActivity.this, tvHotCity1
					.getText().toString().trim());
			Constants.CITYCHANGEID = hotcityList.get(0).getId();
			Constants.CITYCHANGENANME = hotcityList.get(0).getName();
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.tvHotCity2:
			SharedPrefUtil.setCityName(CityChangeActivity.this, tvHotCity2
					.getText().toString().trim());
			Constants.CITYCHANGEID = hotcityList.get(1).getId();
			Constants.CITYCHANGENANME = hotcityList.get(1).getName();
			setResult(RESULT_OK);
			CityChangeActivity.this.finish();

			break;
		case R.id.tvHotCity3:
			SharedPrefUtil.setCityName(CityChangeActivity.this, tvHotCity3
					.getText().toString().trim());
			Constants.CITYCHANGEID = hotcityList.get(2).getId();
			Constants.CITYCHANGENANME = hotcityList.get(2).getName();
			setResult(RESULT_OK);
			CityChangeActivity.this.finish();

			break;
		case R.id.tvHotCity4:
			SharedPrefUtil.setCityName(CityChangeActivity.this, tvHotCity4
					.getText().toString().trim());
			Constants.CITYCHANGEID = hotcityList.get(3).getId();
			Constants.CITYCHANGENANME = hotcityList.get(3).getName();
			setResult(RESULT_OK);
			CityChangeActivity.this.finish();

			break;
		case R.id.tvHotCity5:
			SharedPrefUtil.setCityName(CityChangeActivity.this, tvHotCity5
					.getText().toString().trim());
			Constants.CITYCHANGEID = hotcityList.get(4).getId();
			Constants.CITYCHANGENANME = hotcityList.get(4).getName();
			setResult(RESULT_OK);
			CityChangeActivity.this.finish();

			break;
		case R.id.btnGPSCity:
			if (!cityName.equals("正在定位...") && !cityName.equals("正在定位..")
					&& !cityName.equals("正在定位.") && !cityName.equals("正在定位")) {
				System.out.println(cityName);
				SharedPrefUtil.setCityName(CityChangeActivity.this, cityName);
				setResult(RESULT_OK);
				CityChangeActivity.this.finish();
			} else {
				Toast.makeText(CityChangeActivity.this, "正在定位中...",
						Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 全部城市列表
	 * 
	 */
	class LoadCityListTask extends AsyncTask<String, Void, JSONObject> {
		private String name;
		private String version;

		protected LoadCityListTask(String name, String version) {
			this.name = name;
			this.version = version;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CityHelper().city1("1", name, version);
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
						SharedPrefUtil.setcityopen(CityChangeActivity.this,
								result.getString("version"));
						List<CityBean> temp = CityBean.constractList(result
								.getJSONArray("cities"));
						cityList.addAll(temp);
						dataBaseAdapter.clearVersionTable();
						dataBaseAdapter.insertCityVersion(temp);
						// for (CityBean cityBean : temp) {
						// dataBaseAdapter.insertCityVersion(cityBean);
						// }
						SharedPrefUtil.setFistDatabase(CityChangeActivity.this);
						progressbar.setVisibility(View.GONE);
						fillData();

					} else if (result.getInt("status") == Constants.FAIL) {
						System.out.println("失败输出");
						fillData();
						progressbar.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CityChangeActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(CityChangeActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	/**
	 * 全部城市列表
	 * 
	 */
	class LoadCityList1Task extends AsyncTask<String, Void, JSONObject> {
		private String name;
		private String version;

		protected LoadCityList1Task(String name, String version) {
			this.name = name;
			this.version = version;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CityHelper().city1("1", name, version);
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
						List<CityBean> temp = CityBean.constractList(result
								.getJSONArray("cities"));
						cityList1.addAll(temp);
						myListAdapter.notifyDataSetChanged();
					} else if (result.getInt("status") == Constants.FAIL) {

					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CityChangeActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(CityChangeActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		CityBean cityBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return cityList1.size();
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
						R.layout.city_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.tvCityName = (TextView) convertView
						.findViewById(R.id.tvCityName);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			cityBean = cityList1.get(position);
			viewHolder.tvCityName.setText(cityBean.getName());
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		Constants.CITYCHANGEID = cityList1.get(position).getId();
		Constants.CITYCHANGENANME = cityList1.get(position).getName();
		setResult(RESULT_OK);
		CityChangeActivity.this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// mWindowManager.removeView(mDialogText);
	}

	/**
	 * 定位当前所在城市
	 */
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			getLocation();
		}
	};

	public void getLocation() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
		List<String> lp = lm.getAllProviders();
		for (String item : lp) {
			Log.i("8023", "可用位置服务：" + item);
		}

		if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Toast.makeText(CityChangeActivity.this, "您的网络定位未打开！",
					Toast.LENGTH_SHORT).show();
			CityChangeActivity.this.startActivity(new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}

		// Criteria criteria = new Criteria();
		// criteria.setCostAllowed(false);
		// // 设置位置服务免费
		// criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
		// // getBestProvider 只有允许访问调用活动的位置供应商将被返回
		// String providerName = lm.getBestProvider(criteria, true);
		String providerName = lm.NETWORK_PROVIDER;
		Log.i("8023", "------位置服务：" + providerName);
		Location location = lm.getLastKnownLocation(providerName);
		updateView(location);

		// 设置每3秒获取一次
		lm.requestLocationUpdates(providerName, 3000, 8,
				new LocationListener() {

					@Override
					public void onStatusChanged(String arg0, int arg1,
							Bundle arg2) {
					}

					@Override
					public void onProviderEnabled(String arg0) {
						updateView(lm.getLastKnownLocation(arg0));
					}

					@Override
					public void onProviderDisabled(String arg0) {

					}

					@Override
					public void onLocationChanged(Location arg0) {
						// 当定位信息发生改变时，更新位置
						updateView(arg0);
					}
				});

	}

	public void updateView(Location newLocation) {

		if (newLocation != null) {
			Log.i("8023", "-------" + newLocation);
			// 获取维度信息
			double latitude = newLocation.getLatitude();
			// 获取经度信息
			double longitude = newLocation.getLongitude();
			// 获取当前所在城市
			String latLongString = "纬度:" + latitude + "\n经度:" + longitude;
			List<Address> addList = null;
			Geocoder ge = new Geocoder(CityChangeActivity.this);
			try {
				addList = ge.getFromLocation(latitude, longitude, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("详细地址信息" + addList);
			if (addList != null && addList.size() > 0) {
				for (int i = 0; i < addList.size(); i++) {
					Address ad = addList.get(i);
					latLongString += "\n";
					latLongString += ad.getCountryName() + ";"
							+ ad.getLocality();
					cityName = ad.getLocality();

				}
			}

			System.out.println("纬度:" + latitude + "\n经度:" + longitude);

			cityName = cityName.substring(0, cityName.length() - 1);
			SharedPrefUtil.setLat(CityChangeActivity.this, latitude + "");
			SharedPrefUtil.setLng(CityChangeActivity.this, longitude + "");
			SharedPrefUtil.setGpscity(CityChangeActivity.this, cityName);
			btnGPSCity.setText(cityName);
		}
	}
}