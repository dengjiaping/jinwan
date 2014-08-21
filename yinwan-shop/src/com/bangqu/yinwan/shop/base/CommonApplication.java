package com.bangqu.yinwan.shop.base;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.bangqu.yinwan.shop.core.ImageLoaderConfig;
import com.bangqu.yinwan.shop.imageload.ImageLoader;
import com.bangqu.yinwan.shop.util.DataBaseAdapter;

/**
 * 应用全局变量 说明： 1、可以缓存一些应用全局变量。比如数据库操作对象
 */
public class CommonApplication extends Application {

	// *************************
	public String mStrKey = "aHSqqErEUwjMmNRVxbd6axdh";
	public boolean m_bKeyRight = true;

	public BMapManager mBMapMan = null;
	/**
	 * Singleton pattern
	 */
	private static CommonApplication mInstance = null;

	// 图片缓存
	public ImageLoader imageLoader;
	// 缓存一些对象数据(可以用于传递到下一个activity)
	private HashMap<String, Object> hmCacheObject = new HashMap<String, Object>();

	/**
	 * 数据库操作类（xxxx）
	 * 
	 * @return
	 */
	private DataBaseAdapter dataBaseAdapter;

	public static CommonApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		imageLoader = new ImageLoader(this, true);
		dataBaseAdapter = new DataBaseAdapter(this);
		dataBaseAdapter.open();
		initEngineManager(this);
		// ********************
		mBMapMan = new BMapManager(this);
		mBMapMan.init(mStrKey, new MyGeneralListener());
		mBMapMan.start();
		ImageLoaderConfig.initImageLoader(this, BASE_IMAGE_CACHE);
	}

	private void initEngineManager(Context context) {
		// TODO Auto-generated method stub
		if (mBMapMan == null) {
			mBMapMan = new BMapManager(context);
		}

		if (!mBMapMan.init(mStrKey, new MyGeneralListener())) {
			Toast.makeText(
					CommonApplication.getInstance().getApplicationContext(),
					"mBMapMan  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			// 非零值表示key验证未通过
			if (iError != 0) {
				CommonApplication.getInstance().m_bKeyRight = false;
				// Toast.makeText(
				// CommonApplication.getInstance().getApplicationContext(),
				// "key值错误", Toast.LENGTH_LONG).show();
				Log.e("" + iError, "请检查文件输入正确的授权Key,并检查您的网络连接是否正常");
			} else {
				CommonApplication.getInstance().m_bKeyRight = true;
				// Toast.makeText(
				// CommonApplication.getInstance().getApplicationContext(),
				// "key值正确", Toast.LENGTH_LONG).show();
				Log.i(iError + "", "key认证成功");
			}
		}
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

	public ImageLoader getImgLoader() {
		return this.imageLoader;
	}

	/**
	 * 获得数据库操作对象
	 * 
	 * @return
	 */
	public DataBaseAdapter getDbAdapter() {
		return this.dataBaseAdapter;
	}

	/**
	 * 缓存对象
	 * 
	 * @param key
	 * @param value
	 */
	public void sethmCache(String key, Object value) {
		hmCacheObject.put(key, value);
	}

	/**
	 * 获取对象
	 * 
	 * @param key
	 * @return
	 */
	public Object gethmCache(String key) {
		return hmCacheObject.get(key);
	}

	/**
	 * 删除当前缓存的对象
	 * 
	 * @param key
	 */
	public void deletehmCache(String key) {
		hmCacheObject.remove(key);
	}

	// *************************************************************
	// ******二维码扫描**********************************************
	// SDCard路径
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	// 图片存储路径
	public static final String BASE_PATH = SD_PATH + "/ZxingDemo/";
	// 缓存图片路径
	public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";

}
