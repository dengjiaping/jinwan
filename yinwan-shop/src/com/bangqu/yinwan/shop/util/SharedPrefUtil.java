package com.bangqu.yinwan.shop.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.bean.ShopBean;
import com.bangqu.yinwan.shop.bean.UserBean;

/**
 * SharedPreferences工具类
 */

public class SharedPrefUtil {
	// 用户第一次登陆
	public static final String IS_FIRST_LOGIN = "isfirstlogin";
	// 用户第一次选择小区
	public static final String IS_FIRST_SEARCH = "isfirstsearch";
	// 商户上一次点击未处理订单的时间
	public static final String LAST_CLICK_TIME = "lastClickTime";
	// 是否选择店铺
	// public static final String IS_CHOOSE_SHOP = "ischooseshop";
	// userBean
	public static final String IS_VIP = "vip";
	public static final String ACCESSTOKEN = "accessToken";
	public static final String ID = "id";// 昵称
	public static final String USERNAME = "username";// 用户名
	public static final String PASSWORD = "password";// 密码
	public static final String NICKNAME = "nickname";// 昵称
	public static final String MOBILE = "mobile";// 手机
	public static final String PHOTO = "photo";// 头像

	public static final String AGE = "age";// 年龄
	public static final String MALE = "male";// 性别
	public static final String INTRO = "intro";// 个性签名

	public static final String IS_FIRST_CATEGORY_VERSION = "isfirstcategory";
	public static final String CATEGORY_LIST_VERSIONS = "category_list_versions";// 保存分类列表版本号

	// 存储用户名
	public static final String USERNAME_CACHE = "username_cache";

	// 存储店铺相关信息
	public static final String OWNERID = "ownerid";
	// 存储店铺相关信息
	public static final String SHOP_ID = "id";
	public static final String SHOP_NAME = "shopName";

	// 存储所选城市和小区
	public static final String CITY_NAME = "cityname";
	public static final String LOCATION_NAME = "locationName";

	// 存储当前所在经纬度
	public static final String LAT = "lat";
	public static final String LNG = "lng";

	// 判断是否第一次加载分类界面数据
	public static final String IS_FIRST_LOAD_FENLEI = "isfirstloadfenlei";
	// 保存分类版本号
	public static final String FENLEI_VERSION = "fenlei_version";
	// 判断是否第一次加载开通城市数据
	public static final String IS_FIRST_CITY_OPEN_VERSIONs = "isfirstcityopen";
	public static final String CITY_OPEN_VERSIONS = "cityopen_versions";// 保存开通城市版本号

	//
	public static final String FIRST_CITY_NAME = "firstcityname";

	public static final String USERID = "userid";
	public static final String ALIPAY = "alipay";
	public static final String REALNAME = "realname";// 真实姓名
	public static final String DEVICETOKEN = "deviceToken";// 推送设备token
	public static final String ZHANGHAO = "zhanghao";// 账号
	public static final String PASSWD = "passwd";// 密码

	public static final String CLEANALL = "cleanall";

	public static final String IS_FIRST_HOMEFENL = "isfirstfenlei";

	public static final String IS_FIRST_DATABASE = "isfirstdatabase";

	public static final String GPSCITY = "gpscity";

	/**
	 * 设置定位城市
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setGpscity(Context context, String gpscity) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (gpscity != null) {
			e.putString(GPSCITY, gpscity);
		}
		e.commit();
	}

	/**
	 * 获得定位城市
	 * 
	 * @param context
	 * @return
	 */
	public static String getgpscity(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(GPSCITY, null);

	}

	/**
	 * 是否第一次加载数据库
	 * 
	 * @param context
	 */
	public static void setFistDatabase(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_DATABASE, false);
		e.commit();
	}

	/**
	 * 是否第一次加载数据库
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFistDatabase(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_DATABASE, true);
	}

	/**
	 * 分类
	 * 
	 * @param context
	 */
	public static void setFistfenlei(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_LOGIN, false);
		e.commit();
	}

	/**
	 * 判断是否是第一次进入分类
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFistfenlei(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_LOGIN, true);
	}

	/**
	 * 清除缓存
	 * 
	 * @param context
	 */
	public static void setcleanall(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(CLEANALL, false);
		e.commit();
	}

	/**
	 * 清除缓存
	 * 
	 * @param context
	 * @return
	 */
	public static boolean iscleanall(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(CLEANALL, true);
	}

	/**
	 * 密码
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setpasswd(Context context, String passwd) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (passwd != null) {
			e.putString(PASSWD, passwd);
		}
		e.commit();
	}

	/**
	 * 获取密码
	 * 
	 * @param context
	 * @return
	 */
	public static String getpasswd(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(PASSWD, null);
	}

	/**
	 * 账号
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setzhanghao(Context context, String zhanghao) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (zhanghao != null) {
			e.putString(ZHANGHAO, zhanghao);
		}
		e.commit();
	}

	/**
	 * 获取账号
	 * 
	 * @param context
	 * @return
	 */
	public static String getzhanghao(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(ZHANGHAO, null);
	}

	/**
	 * deviceToken
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setdeviceToken(Context context, String deviceToken) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (deviceToken != null) {
			e.putString(DEVICETOKEN, deviceToken);
		}
		e.commit();
	}

	/**
	 * deviceToken
	 * 
	 * @param context
	 * @return
	 */
	public static String getdeviceToken(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(DEVICETOKEN, null);
	}

	/**
	 * 真实姓名
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setrealname(Context context, String realname) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (realname != null) {
			e.putString(REALNAME, realname);
		}
		e.commit();
	}

	/**
	 * 获得真实姓名
	 * 
	 * @param context
	 * @return
	 */
	public static String getRealname(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(REALNAME, null);
	}

	/**
	 * 支付宝
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setAlipay(Context context, String alipay) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (alipay != null) {
			e.putString(ALIPAY, alipay);
		}
		e.commit();
	}

	/**
	 * 获得支付宝
	 * 
	 * @param context
	 * @return
	 */
	public static String getAlipay(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(ALIPAY, null);
	}

	/**
	 * 设置业主ID
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setUserID(Context context, String userid) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (userid != null) {
			e.putString(USERID, userid);
		}
		e.commit();
	}

	/**
	 * 获得业主ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserID(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(USERID, null);
	}

	/**
	 * 如果已经进入应用，则设置第一次登录为false
	 * 
	 * @param context
	 */
	public static void setFistLogined(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_LOGIN, false);
		e.commit();
	}

	/**
	 * 判断是否是第一次进入应用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFistLogin(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_LOGIN, true);
	}

	/**
	 * 如果已经选择，则设置第一次选择为false
	 * 
	 * @param context
	 */
	public static void setFistSearch(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_SEARCH, false);
		e.commit();
	}

	/**
	 * 判断是否是第一次选择小区
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFistSearch(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_SEARCH, true);
	}

	/**
	 * 设置业主ID
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setOwnerID(Context context, String ownerid) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (ownerid != null) {
			e.putString(OWNERID, ownerid);
		}
		e.commit();
	}

	/**
	 * 获得业主ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getOwnerID(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(OWNERID, null);
	}

	/**
	 * 设置cityName
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setCityName(Context context, String cityname) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(CITY_NAME, cityname);
		e.commit();
	}

	/**
	 * 获得cityName
	 * 
	 * @param context
	 * @return
	 */
	public static String getCityName(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(CITY_NAME, null);

	}

	/**
	 * 设置shopName
	 * 
	 */
	public static void setshopName(Context context, String shopName) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (shopName != null) {
			e.putString(SHOP_NAME, shopName);
		}
		e.commit();
	}

	/**
	 * 设置shopID
	 * 
	 */
	public static void setshopID(Context context, String id) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (id != null) {
			e.putString(SHOP_ID, id);
		}
		e.commit();
	}

	/**
	 * 设置lat
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setLat(Context context, String lat) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (lat != null) {
			e.putString(LAT, lat);
		}
		e.commit();
	}

	/**
	 * 获得lat
	 * 
	 * @param context
	 * @return
	 */
	public static String getLat(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(LAT, null);

	}

	/**
	 * 设置lng
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setLng(Context context, String lng) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (lng != null) {
			e.putString(LNG, lng);
		}
		e.commit();
	}

	/**
	 * 获得lng
	 * 
	 * @param context
	 * @return
	 */
	public static String getLng(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(LNG, null);

	}

	/**
	 * 如果已经加载完成，则设置第一次加载分类界面为false
	 * 
	 * @param context
	 */
	public static void setFistLoadFenLei(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_LOAD_FENLEI, false);
		e.commit();
	}

	/**
	 * 判断是否是第一次加载分类界面
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFistLoadFenLei(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_LOAD_FENLEI, true);
	}

	/**
	 * 设置locationName
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setLocationName(Context context, String locationName) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (locationName != null) {
			e.putString(LOCATION_NAME, locationName);
		}
		e.commit();
	}

	/**
	 * 获得locationName
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocationName(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(LOCATION_NAME, null);

	}

	/**
	 * 设置token
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setToken(Context context, String accessToken) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (accessToken != null) {
			e.putString(ACCESSTOKEN, accessToken);
		}
		e.commit();
	}

	/**
	 * 获得token
	 * 
	 * @param context
	 * @return
	 */
	public static String getToken(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(ACCESSTOKEN, null);

	}

	/**
	 * 设置token
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setID(Context context, String id) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (id != null) {
			e.putString(ID, id);
		}
		e.commit();
	}

	/**
	 * 获得token
	 * 
	 * @param context
	 * @return
	 */
	public static String getID(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(ID, null);
	}

	/**
	 * 设置店铺是否为VIP
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setVip(Context context, String vip) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (vip != null) {
			e.putString(IS_VIP, vip);
		}
		e.commit();
	}

	/**
	 * 获得VIP
	 * 
	 * @param context
	 * @return
	 */
	public static String getVIP(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(IS_VIP, null);

	}

	/**
	 * 设置上一次点击未处理订单的时间
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setLastClickTime(Context context, String lastClickTime) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (lastClickTime != null) {
			e.putString(LAST_CLICK_TIME, lastClickTime);
		}
		e.commit();
	}

	/**
	 * 获得上一次点击未处理订单的时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getLastClickTime(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(LAST_CLICK_TIME, null);

	}

	/**
	 * 设置userName_cache
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setNameCache(Context context, String userName_Cache) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (userName_Cache != null) {
			e.putString(USERNAME_CACHE, userName_Cache);
		}
		e.commit();
	}

	/**
	 * 获得userName_cache
	 * 
	 * @param context
	 * @return
	 */
	public static String getNameCache(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(USERNAME_CACHE, null);

	}

	/**
	 * 设置登录用户信息(在Editor中填充数据)
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setUserBean(Context context, UserBean userBean) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();

		if (userBean.getUsername() != null) {
			e.putString(USERNAME, userBean.getUsername());
		}
		if (userBean.getNickname() != null) {
			e.putString(NICKNAME, userBean.getNickname());
		}
		if (userBean.getPassword() != null) {
			e.putString(PASSWORD, userBean.getPassword());
		}
		if (userBean.getMale() != null) {
			e.putString(MALE, userBean.getMale());
		}
		if (userBean.getAge() != null) {
			e.putString(AGE, userBean.getAge());
		}
		if (userBean.getIntro() != null) {
			e.putString(INTRO, userBean.getIntro());
		}
		e.commit();
	}

	/**
	 * 获得登录用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static UserBean getUserBean(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		String id = sp.getString(ID, "");
		String username = sp.getString(USERNAME, "");
		String password = sp.getString(PASSWORD, "");
		String nickname = sp.getString(NICKNAME, "");
		String mobile = sp.getString(MOBILE, "");
		String photo = sp.getString(PHOTO, "");
		String age = sp.getString(AGE, "");
		String male = sp.getString(MALE, "");
		String intro = sp.getString(INTRO, "");
		if (!StringUtil.isBlank(username)) {
			return new UserBean(id, username, password, nickname, mobile,
					photo, age, male, intro);
		} else {
			return null;
		}
	}

	/**
	 * 设置选择的店铺信息
	 * 
	 * @param context
	 * @param uid
	 */
	public static void setShopBean(Context context, ShopBean shopBean) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (shopBean.getId() != null) {
			e.putString(SHOP_ID, shopBean.getId());
		}
		if (shopBean.getName() != null) {
			e.putString(SHOP_NAME, shopBean.getName());
		}

		e.commit();
	}

	/**
	 * 获得选择的店铺信息
	 * 
	 * @param context
	 * @return
	 */
	public static ShopBean getShopBean(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		String id = sp.getString(SHOP_ID, "");
		String name = sp.getString(SHOP_NAME, "");
		String vip = sp.getString(IS_VIP, "");
		if (!StringUtil.isBlank(SHOP_ID)) {
			return new ShopBean(id, name);
		} else {
			return null;
		}
	}

	/**
	 * 清除登录用户信息
	 * 
	 * @param context
	 */
	public static void clearUserBean(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		sp.edit().remove(ACCESSTOKEN).remove(NICKNAME).remove(USERNAME)
				.remove(PASSWORD).remove(AGE).remove(MALE).remove(INTRO)
				.commit();
		Constants.isExitLogin = true;
	}

	/**
	 * 清除已选店铺信息
	 * 
	 * @param context
	 */
	public static void clearShopBean(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		sp.edit().remove(SHOP_ID).remove(SHOP_NAME).commit();
		// Constants.isChooseShop = false;
	}

	/**
	 * 清除会员信息
	 * 
	 * @param context
	 */
	public static void clearvip(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		sp.edit().remove(IS_VIP).commit();
		// Constants.isChooseShop = false;
	}

	/**
	 * 清除用户ID
	 * 
	 * @param context
	 */
	public static void clearuserid(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		sp.edit().remove(USERID).commit();
	}

	public static void cleardeviceToken(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		sp.edit().remove(DEVICETOKEN).commit();
		Constants.isExitLogin = true;
	}

	/**
	 * 检测是否登录
	 * 
	 * @return
	 */
	public static boolean checkLogin(Context context) {
		UserBean userBean = SharedPrefUtil.getUserBean(context);
		if (userBean == null) {// 未登录
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检测是否选择店铺
	 * 
	 * @return
	 */
	public static boolean checkShop(Context context) {
		ShopBean shopBean = SharedPrefUtil.getShopBean(context);
		if (StringUtil.isBlank(shopBean.getId())) {// 未选择店铺
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 设置分类版本号
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setVersion(Context context, String version) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (version != null) {
			e.putString(FENLEI_VERSION, version);
		}
		e.commit();
	}

	/**
	 * 获得分类版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVerSion(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(FENLEI_VERSION, null);

	}

	/**
	 * 设置城市版本号
	 */
	public static void setcityopen(Context context, String cityopen_versions) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (cityopen_versions != null) {
			e.putString(CITY_OPEN_VERSIONS, cityopen_versions);
		}
		e.commit();
	}

	/**
	 * 获取城市版本号
	 */
	public static String getopenversion(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(CITY_OPEN_VERSIONS, "1");

	}

	/**
	 * 设置已分类列表版本号
	 */
	public static void setcategorylists(Context context,
			String category_list_versions) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		if (category_list_versions != null) {
			e.putString(CATEGORY_LIST_VERSIONS, category_list_versions);
		}
		e.commit();
	}

	/**
	 * 获取分类列表版本号
	 */
	public static String getcategorylists(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(CATEGORY_LIST_VERSIONS, "0");

	}

	/**
	 * 如果已经加载完成，则设置第一次加载分类界面false
	 * 
	 */
	public static void setFistcategorylist(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_CATEGORY_VERSION, false);
		e.commit();
	}

	/**
	 * 判断是否是第一次加载分类列表界面
	 * 
	 */
	public static boolean isFistcategorylist(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_CATEGORY_VERSION, true);
	}

	/**
	 * 如果已经加载完成，则设置第一次加载开通城市界面为false
	 * 
	 */
	public static void setFistcityopens(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putBoolean(IS_FIRST_CITY_OPEN_VERSIONs, false);
		e.commit();
	}

	/**
	 * 判断是否是第一次加载开通城市界面
	 * 
	 */
	public static boolean isFistcityopens(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(IS_FIRST_CITY_OPEN_VERSIONs, true);
	}

	/**
	 * 设置已选城市名字
	 * 
	 * @param context
	 * @param accessToken
	 */
	public static void setFirstCityName(Context context, String firstcityname) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(FIRST_CITY_NAME, firstcityname);
		e.commit();
	}

	/**
	 * 获得已选城市名字
	 * 
	 * @param context
	 * @return
	 */
	public static String getFirstCityName(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context); // 获取Preferences的操作对象
		return sp.getString(FIRST_CITY_NAME, null);

	}

}