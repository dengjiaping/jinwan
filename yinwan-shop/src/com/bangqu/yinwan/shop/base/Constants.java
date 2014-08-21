package com.bangqu.yinwan.shop.base;

import android.os.Environment;

public class Constants {

	// SDCard路径
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	// 图片存储路径
	public static final String BASE_PATH = SD_PATH + "/yinwan/";

	// 缓存图片路径
	public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";

	// 判断首页是否需要动态更新
	public static Boolean isRefreshHome = false;

	// 是否从购物车界面进入商品详情(加入购物车改为取消购物车)
	public static Boolean isCartProduct = false;

	// 是否从提交订单页面进入地址列表
	public static Boolean isChooseAddress = false;

	// 上一次所选择的城市名
	public static String oldCityName = ""; // 上一次所选择的城市名

	// 临时存储购物车某件商品数量
	public static String productNumber = "1";
	public static int index = 0;

	// 保存当前所选送货地址和id
	public static String addRessName = "";
	public static String addRessId = "";

	// 判断是否退出登录
	public static Boolean isExitLogin = false;
	// 判断是否选择店铺
	// public static Boolean isChooseShop = false;

	// 接口数据访问状态
	public static final int SUCCESS = 1;// 返回成功
	public static final int FAIL = 0;// 返回失败

	public static String ProductCategory = "";
	public static String productCategoryId = "";

	// 商品名称
	public static String productname = "";

	// 当前地址所选小区
	public static String VillageName;//
	// 取消订单相关
	public static String cancelContent = "没货了";
	// 定位的经纬度信息
	public static double LNG;// 纬度
	public static double LAT;// 经度
	public static String adressfinsh = "";// 地址
	// 七牛
	public static String TimeName = "";// 七牛命名
	public static String upToken = "";// 七牛授权码
	// 七牛图片上传是否成功
	public static Boolean IMG_SUCCESS = true;
	public static Boolean IS_TAKEPHOTO = false;

	// 判断添加商品是从商品库中添加的还是手动添加的
	public static Boolean IS_SELF = true;
	// 搜索商品时，用到暂时存储
	public static String NewProductName = "";
	public static String APP_ID = "wxeb5bc69c320723d8";

	public static Boolean isAddressToLocation = false;//

	// 店铺地址
	public static String SHOP_ADRESS = "";
	public static boolean PUSH = false;
	public static boolean NOTIFICATION = false;
	public static boolean CITYGPS = false;// 定位显示
	public static int ADRDSS = 1;

	public static String DISTRICTID = "";
	public static String DISTRICTNANME = "";
	public static String CITYCHANGEID = "";
	public static String CITYCHANGENANME = "";

	public static String GPSADRESS = "";

}
