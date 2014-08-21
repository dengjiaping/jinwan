package com.bangqu.yinwan.shop.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapStatus;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.util.StringUtil;

public class LocationActivity extends Activity implements MapCallBack,
		OnClickListener {

	public static final int QUERY_LOCATION = 1;
	public static final int SEND_LOCATION = 2;
	// 关于反地理位置延迟的处理
	public static final int HANDLE_MSG_DELAY = 3;
	public static final int DELAY_TIME = 1500;

	// key
	public static final String BAIDU_KEY = "qt4K053yu1tOAOLuzlRogAAO";
	// 定位相关
	private LocationClient mLocClient;
	private LocationData locData = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	// 定位图层
	private MyLocationOverlay myLocationOverlay = null;
	private PopupOverlay pop;

	private MyMapView mMapView = null; // 地图View
	private MapController mMapController = null;
	private BMapManager mBMapManager;
	private MKSearchListener mSearchListener;
	private MKMapViewListener mMapViewListener;
	private MKMapTouchListener mTouchListener;
	private LinearLayout mAddInfoBar;
	private TextView mAddTxt;
	private TextView mBackBtn;
	private Button mFinishedBtn;
	private Button mRelocBtn;
	private ImageView mIcon;
	private ProgressBar mWaitingBar;
	private View viewCache;
	private GeoPoint showPoint;
	private GeoPoint myPoint;
	private int mType;
	private boolean m_bKeyRight = true;
	private EditText etsearch;

	private double latFinish;
	private double lngFinish;
	private String adressfinsh = "";
	private boolean first = true;
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private Button btsearch;
	private boolean mylocation = true;

	private Button btnLeft;
	private Button btnRight;
	private String strgetintent = "";
	/**
	 * 判断地址获取是否成功
	 */
	private boolean getAddr = false;
	private int getAddrCount = 0;
	private String mAddInfo;
	private TextView mLocationInfo;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == HANDLE_MSG_DELAY) {
				if (!getAddr && getAddrCount < 8) {
					mHandler.sendEmptyMessageDelayed(HANDLE_MSG_DELAY,
							DELAY_TIME);
					mSearch.reverseGeocode(myPoint);
					getAddrCount++;
				} else if (getAddrCount >= 8) {
					setPopInfo(getResources().getString(
							R.string.getlocation_error));
				}
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initBaiduMap();
		if (m_bKeyRight) {
			setContentView(R.layout.activity_location);
			strgetintent = getIntent().getStringExtra("location");
			mLocationInfo = (TextView) findViewById(R.id.location_info);
			mAddInfoBar = (LinearLayout) findViewById(R.id.add_info_bar);
			mAddTxt = (TextView) findViewById(R.id.add_info_txt);
			mBackBtn = (TextView) findViewById(R.id.location_back);
			mFinishedBtn = (Button) findViewById(R.id.location_right_btn);
			mRelocBtn = (Button) findViewById(R.id.reloc_btn);
			mFinishedBtn.setEnabled(false);
			mIcon = (ImageView) findViewById(R.id.add_icon);
			mWaitingBar = (ProgressBar) findViewById(R.id.add_progress);
			mBackBtn.setOnClickListener(this);
			mFinishedBtn.setOnClickListener(this);
			mRelocBtn.setOnClickListener(this);
			etsearch = (EditText) findViewById(R.id.etsearch);
			if (StringUtil.isBlank(Constants.GPSADRESS)) {
				etsearch.setText(getIntent().getStringExtra("location"));
			} else {
				etsearch.setText(Constants.GPSADRESS);
			}

			btnRight = (Button) findViewById(R.id.btnRight);
			btnRight.setText("确定");
			btnRight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
					System.out.println("精度：Constants.LAT" + Constants.LAT
							+ "纬度Constants.LNG" + Constants.LNG);
				}
			});
			btnLeft = (Button) findViewById(R.id.btnLeft);
			btnLeft.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
					System.out.println("精度：Constants.LAT" + Constants.LAT
							+ "纬度Constants.LNG" + Constants.LNG);
				}
			});
			btsearch = (Button) findViewById(R.id.btsearch);
			btsearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!StringUtil.isBlank(etsearch.getText().toString())) {
						first = true;
						mylocation = true;
						SearchButtonProcess();
					} else {
						Toast.makeText(LocationActivity.this, "请输入地址",
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			initMap();
			initClient();
			initListener();
			ItemizedOverlay<OverlayItem> itemOverlay = new ItemizedOverlay<OverlayItem>(
					null, mMapView);
			// 定位图层初始
			myLocationOverlay = new MyLocationOverlay(mMapView);
			// 设置定位数据
			myLocationOverlay.setData(locData);
			// 添加定位图层
			myLocationOverlay.enableCompass();
			// 修改定位数据后刷新图层生
			mMapView.refresh();

		} else {
		}
	}

	/**
	 * 查看百度地图是否使用正常
	 */
	private void initBaiduMap() {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(LocationActivity.this);
		}
		mBMapManager.init(BAIDU_KEY, new MyGeneralListener());
	}

	/**
	 * 初始化地
	 */
	private void initMap() {
		// 地图初始
		mMapView = (MyMapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		mSearch = new MKSearch();
		mSearchListener = new MySearchListener();
		mSearch.init(mBMapManager, mSearchListener);
	}

	protected void SearchButtonProcess() {
		// Geo搜索
		mSearch.geocode(etsearch.getText().toString(), "");

	}

	/**
	 * 定位初始化
	 */
	private void initClient() {
		mLocClient = new LocationClient(this);
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(500);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 监听初始
	 */
	private void initListener() {
		mMapViewListener = new MyMapViewListener();
		mMapView.regMapViewListener(mBMapManager, mMapViewListener);
		mMapView.setMapCallBack(this);
		mTouchListener = new MyTouchListener();
		mMapView.regMapTouchListner(mTouchListener);
	}

	@Override
	public void mapstartMoving() {
		Log.i("start moving=====>", "=====startmoving");
		mAddInfoBar.setVisibility(View.GONE);
		mFinishedBtn.setClickable(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.location_back:
			finish();
			break;
		case R.id.location_right_btn:
			if (myPoint != null) {
				Intent resultIntent = new Intent(LocationActivity.this,
						MainActivity.class);
				resultIntent.putExtra("lat", myPoint.getLatitudeE6());
				resultIntent.putExtra("lon", myPoint.getLongitudeE6());
				resultIntent.putExtra("addr", mAddInfo);
				setResult(0, resultIntent);
				finish();
			} else {
				Toast.makeText(LocationActivity.this, "地址获取不成功，请重试",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.reloc_btn:
			requestLocClick();
			break;

		default:
			break;
		}
	}

	/**
	 * 手动触发定位请求
	 */
	public void requestLocClick() {
		getAddrCount = 0;
		getAddr = false;
		mRelocBtn.setClickable(false);
		mLocClient.requestLocation();
		Toast.makeText(LocationActivity.this, "正在定位…", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 修改位置图标
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// 当传入marker为null时，使用默认图标绘制
		myLocationOverlay.setMarker(marker);
		// 修改图层，需要刷新MapView生效
		mMapView.refresh();
	}

	private void shakeAnima() {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.pop_shake);
		shake.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mAddInfoBar.setVisibility(View.VISIBLE);
				mAddTxt.setVisibility(View.GONE);
				mWaitingBar.setVisibility(View.VISIBLE);
				getAddrCount = 0;
				getAddr = false;
				mHandler.sendEmptyMessageDelayed(HANDLE_MSG_DELAY, DELAY_TIME);
			}
		});
		mIcon.startAnimation(shake);
	}

	private void setPopInfo(String msg) {
		mAddInfoBar.setVisibility(View.VISIBLE);
		mWaitingBar.setVisibility(View.GONE);
		mAddTxt.setVisibility(View.VISIBLE);
		mAddTxt.setText(msg);
		Constants.GPSADRESS = msg;

	}

	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	private Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// 如果不显示定位精度圈，将accuracy0即可
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			Log.i("lat is===>", "..." + locData.latitude);
			Log.i("lon is===>", "..." + locData.longitude);
			// 更新定位数据
			myLocationOverlay.setData(locData);
			// 更新图层数据执行刷新后生
			mMapView.refresh();
			// 移动到定位点
			if (StringUtil.isBlank(etsearch.getText().toString())) {
				mylocation = false;
				GeoPoint point = new GeoPoint((int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6));
				myPoint = point;
			}
			mMapController.animateTo(myPoint);
			if (!StringUtil.isBlank(etsearch.getText().toString())) {
				SearchButtonProcess();
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	class MyMapViewListener implements MKMapViewListener {

		@Override
		public void onMapMoveFinish() {
			Log.i("finished", "................stop");
			MKMapStatus status = mMapView.getMapStatus();
			// move结束,重置参数
			mMapView.mapMovingEnd();
			if (status != null) {
				myPoint = status.targetGeo;
				int x = myPoint.getLatitudeE6();
				int y = myPoint.getLongitudeE6();
				Log.i("centerPoint x is", "" + x);
				Log.i("centerPoint y is", "" + y);

				shakeAnima();
			}
		}

		@Override
		public void onMapLoadFinish() {
		}

		@Override
		public void onClickMapPoi(MapPoi arg0) {
		}

		@Override
		public void onGetCurrentMap(Bitmap arg0) {
		}

		@Override
		public void onMapAnimationFinish() {
			shakeAnima();
		}

	}

	class MyTouchListener implements MKMapTouchListener {

		@Override
		public void onMapClick(GeoPoint arg0) {

		}

		@Override
		public void onMapDoubleClick(GeoPoint point) {
			mAddTxt.setVisibility(View.GONE);
			mWaitingBar.setVisibility(View.VISIBLE);
			myPoint = point;
		}

		@Override
		public void onMapLongClick(GeoPoint arg0) {

		}
	}

	/**
	 * 搜索反地坐标点转换文字地
	 */
	class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo info, int arg1) {
			if (arg1 != 0) {
				String str = String.format("错误号：%d", arg1);
				initMap();
				initClient();
				initListener();
				etsearch.setText("");
				return;
			}
			if (mylocation) {
				mMapView.getController().animateTo(info.geoPt);
				if (info.type == MKAddrInfo.MK_GEOCODE) {
					// 地理编码：通过地址检索坐标点
					String strInfo = String.format("纬度：%f 经度：%f",
							info.geoPt.getLatitudeE6() / 1e6,
							info.geoPt.getLongitudeE6() / 1e6);
					latFinish = info.geoPt.getLatitudeE6() / 1e6;
					lngFinish = info.geoPt.getLongitudeE6() / 1e6;

					Constants.LAT = lngFinish;
					Constants.LNG = latFinish;
					adressfinsh = info.strAddr;
					Constants.GPSADRESS = adressfinsh;
					first = false;
					System.out.println("first");
					mylocation = false;
					GeoPoint point = new GeoPoint((int) (latFinish * 1e6),
							(int) (lngFinish * 1e6));
					myPoint = point;
					System.out.println("精度：Constants.LAT" + Constants.LAT
							+ "纬度Constants.LNG" + Constants.LNG);
				}
				return;
			} else {
				getAddr = true;
				mAddInfo = info.strAddr;
				setPopInfo(mAddInfo);
				MKMapStatus status = mMapView.getMapStatus();
				if (status != null) {
					myPoint = status.targetGeo;
				}
				mFinishedBtn.setEnabled(true);
				mMapView.mapMovingEnd();
				mFinishedBtn.setClickable(true);
				Log.i("address is===>", mAddInfo);

				latFinish = info.geoPt.getLatitudeE6() / 1e6;
				lngFinish = info.geoPt.getLongitudeE6() / 1e6;
				Constants.LAT = lngFinish;
				Constants.LNG = latFinish;
				System.out.println("精度：Constants.LAT" + Constants.LAT
						+ "纬度Constants.LNG" + Constants.LNG);
				System.out.println("second++++++++++++++++");
			}
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
				int arg2) {
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		}

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 销毁
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(LocationActivity.this, "请检查您的网络！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(LocationActivity.this, "输入正确的检索条件!",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 处理授权key的问题
				// Toast.makeText(LocationActivity.this,
				// "无法验证您的授权，或许您的网络模式不适合百度地图工作", Toast.LENGTH_LONG).show();
				m_bKeyRight = false;
			}
		}
	}
}

class MyMapView extends MapView {
	private MapCallBack mCallBack;
	private boolean isMoving = false;
	private boolean isLocked = false;

	public MyMapView(Context arg0) {
		super(arg0);
	}

	public MyMapView(Context context, AttributeSet set) {
		super(context, set);
	}

	public MyMapView(Context context, AttributeSet set, int defStyle) {
		super(context, set, defStyle);
	}

	/**
	 * 设置回调函数
	 * 
	 * @param callBack
	 */
	public void setMapCallBack(MapCallBack callBack) {
		mCallBack = callBack;
	}

	/**
	 * move结束
	 */
	public void mapMovingEnd() {
		isMoving = false;
	}

	/**
	 * 锁住touch event
	 * 
	 * @param isLock
	 */
	public void setLocked(boolean isLock) {
		isLocked = isLock;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (!isMoving && !isLocked) {
				isMoving = true;
				mCallBack.mapstartMoving();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

}
