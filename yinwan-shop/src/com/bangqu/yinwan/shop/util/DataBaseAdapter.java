/**
 * 
 */
package com.bangqu.yinwan.shop.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.bangqu.yinwan.shop.bean.CategoryBean;
import com.bangqu.yinwan.shop.bean.CityBean;
import com.bangqu.yinwan.shop.bean.ShopBean;

/**
 * 数据库操作类
 * 
 * @author Arvin 说明： 1、数据库操作类 2、定义好数据表名，数据列，数据表创建语句 3、操作表的方法紧随其后
 */
public class DataBaseAdapter {
	/**
	 * 数据库版本(更新数据库版本时改变这个值)
	 */
	private static final int DATABASE_VERSION = 1;
	/**
	 * 数据库名称
	 */
	private static final String DATABASE_NAME = "yinwan.db";
	/**
	 * 数据库表id
	 */
	public static final String RECORD_ID = "_id";

	private SQLiteDatabase db;
	private ReaderDbOpenHelper dbOpenHelper;

	public DataBaseAdapter(Context context) {
		this.dbOpenHelper = new ReaderDbOpenHelper(context, DATABASE_NAME,
				null, DATABASE_VERSION);
	}

	public void open() {
		this.db = dbOpenHelper.getWritableDatabase();
	}

	public void close() {
		if (db != null) {
			db.close();
		}
		if (dbOpenHelper != null) {
			dbOpenHelper.close();
		}
	}

	private class ReaderDbOpenHelper extends SQLiteOpenHelper {

		public ReaderDbOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			// super(context, name, factory, version);
			// CursorFactory设置为null,使用默认值
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			// 创建表
			_db.execSQL(CREATE_SQL_HISTORY_SHOP);
			_db.execSQL(CREATE_SQL_SERVICE);
			_db.execSQL(CREATE_SQL_PRODUCT);
			_db.execSQL(CREATE_SQL_CITY_VERSION);
			_db.execSQL(CREATE_SQL_HISTORY_KEYWORDS);

		}

		/**
		 * 升级应用时，有数据库改动在此方法中修改(如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,
		 * 即会调用onUpgrade)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
			System.out.println("数据库版本更新");
		}
	}

	/************************************************** 店铺最近浏览表 ********************************************************/

	/**
	 * 店铺最近浏览表
	 */
	public static final String TABLE_NAME_CATEGORIES = "t_categories";

	/**
	 * 店铺最近浏览表中的列定义
	 * 
	 * @author Aizhimin
	 */
	public interface CategoriesColumns {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String STARTTIME = "startTime";
		public static final String ENDTIME = "endTime";
		public static final String SENDPRICE = "sendPrice";
		public static final String ADDRESS = "addRess";
		public static final String IMG = "img";
	}

	/**
	 * 商品分类表查询列
	 */
	public static final String[] PROJECTION_CATEGORIES = new String[] {
			RECORD_ID, CategoriesColumns.ID, CategoriesColumns.NAME,
			CategoriesColumns.STARTTIME, CategoriesColumns.ENDTIME,
			CategoriesColumns.SENDPRICE, CategoriesColumns.ADDRESS,
			CategoriesColumns.IMG };

	/**
	 * 商品分类表的创建语句
	 */
	public static final String CREATE_SQL_HISTORY_SHOP = "create table "
			+ TABLE_NAME_CATEGORIES + " (" + RECORD_ID
			+ " integer primary key autoincrement," + CategoriesColumns.ID
			+ " text, " + CategoriesColumns.NAME + " text, "
			+ CategoriesColumns.STARTTIME + " text, "
			+ CategoriesColumns.ENDTIME + " text, "
			+ CategoriesColumns.SENDPRICE + " text, "
			+ CategoriesColumns.ADDRESS + " text, " + CategoriesColumns.IMG
			+ " text " + ");";

	/**
	 * 批量插入店铺实体类信息
	 * 
	 * @param scbList
	 */

	public synchronized void bantchCategories(ShopBean shopBean) {

		Cursor cc = db.query(TABLE_NAME_CATEGORIES, PROJECTION_CATEGORIES,
				null, null, null, null, RECORD_ID);
		if (cc.getCount() > 9) {
			cc.moveToFirst();
			String str = cc.getString(0);
			db.delete(TABLE_NAME_CATEGORIES, "_id=?", new String[] { str });
		}

		ArrayList<String> list = findCategories(shopBean);
		SQLiteDatabase localDb = db;
		if (list == null || list.size() <= 0) { // 是否有重复关键词

			try {
				localDb.beginTransaction();
				// localDb.delete(TABLE_NAME_CATEGORIES, null, null);
				String sql = "insert into " + TABLE_NAME_CATEGORIES + " ("
						+ CategoriesColumns.ID + "," + CategoriesColumns.NAME
						+ "," + CategoriesColumns.STARTTIME + ","
						+ CategoriesColumns.ENDTIME + ","
						+ CategoriesColumns.SENDPRICE + ","
						+ CategoriesColumns.ADDRESS + ","
						+ CategoriesColumns.IMG + ") values(?,?,?,?,?,?,?)";
				localDb.execSQL(sql,
						new Object[] { shopBean.getId(), shopBean.getName(),
								shopBean.getStartTime(), shopBean.getEndTime(),
								shopBean.getSendPrice(), shopBean.getAddress(),
								shopBean.getImg() });
				localDb.setTransactionSuccessful();
			} finally {
				localDb.endTransaction();
			}
		} else {
			db.delete(TABLE_NAME_CATEGORIES, "_id=?",
					new String[] { list.get(0) });
			try {
				localDb.beginTransaction();
				// localDb.delete(TABLE_NAME_CATEGORIES, null, null);
				String sql = "insert into " + TABLE_NAME_CATEGORIES + " ("
						+ CategoriesColumns.ID + "," + CategoriesColumns.NAME
						+ "," + CategoriesColumns.STARTTIME + ","
						+ CategoriesColumns.ENDTIME + ","
						+ CategoriesColumns.SENDPRICE + ","
						+ CategoriesColumns.ADDRESS + ","
						+ CategoriesColumns.IMG + ") values(?,?,?,?,?,?,?)";
				localDb.execSQL(sql,
						new Object[] { shopBean.getId(), shopBean.getName(),
								shopBean.getStartTime(), shopBean.getEndTime(),
								shopBean.getSendPrice(), shopBean.getAddress(),
								shopBean.getImg() });
				localDb.setTransactionSuccessful();
			} finally {
				localDb.endTransaction();
			}
		}
		cc.close();

	}

	/**
	 * 根据关键词查询
	 * 
	 * @param keyword
	 * @return
	 */
	public ArrayList<String> findCategories(ShopBean categoriesList) {
		Cursor c = db.query(TABLE_NAME_CATEGORIES, PROJECTION_CATEGORIES, null,
				null, null, null, RECORD_ID);
		ArrayList<String> historyKeywordsList = new ArrayList<String>();

		while (c.moveToNext()) {

			if (categoriesList.getId().equals(c.getString(1))) {
				historyKeywordsList.add(c.getString(0));
			}

		}
		c.close();
		return historyKeywordsList;
	}

	/**
	 * 分组获得商品分类子类
	 * 
	 * @return
	 */
	public List<ShopBean> findAllChildCategories() {

		ArrayList<ShopBean> listshop = new ArrayList<ShopBean>();
		Cursor cur = db.query(TABLE_NAME_CATEGORIES, PROJECTION_CATEGORIES,
				null, null, null, null, RECORD_ID);

		// while (cur.moveToNext()) {
		// ShopBean cat = new ShopBean();
		//
		// cat.setId(Integer.parseInt(cur.getString(1)));
		// cat.setName(cur.getString(2));
		// cat.setStartTime(cur.getString(3));
		// cat.setEndTime(cur.getString(4));
		// cat.setSendPrice(cur.getString(5));
		// cat.setPhone(cur.getString(6));
		// cat.setImg(cur.getString(7));
		//
		// listshop.add(cat);
		//
		// }
		// cur.close();
		// Log.i("list.size()", listshop.size() + "");
		// return listshop;

		for (cur.moveToLast(); !cur.isBeforeFirst() && listshop.size() < 10; cur
				.moveToPrevious()) {

			ShopBean cat = new ShopBean();

			cat.setId(cur.getString(1));
			cat.setName(cur.getString(2));
			cat.setStartTime(cur.getString(3));
			cat.setEndTime(cur.getString(4));
			cat.setSendPrice(cur.getString(5));
			cat.setAddress(cur.getString(6));
			cat.setImg(cur.getString(7));

			listshop.add(cat);
		}

		cur.close();
		return listshop;
	}

	// public void clearSort() {
	// Cursor cc = db.query(TABLE_NAME_CATEGORIES, PROJECTION_CATEGORIES,
	// null, null, null, null, RECORD_ID);
	// while (cc.moveToNext()) {
	// String str = cc.getString(0);
	// db.delete(TABLE_NAME_CATEGORIES, "_id=?", new String[] { str });
	// }
	// }
	//
	// public void clearItem(String id) {
	// Cursor cc = db.query(TABLE_NAME_CATEGORIES, PROJECTION_CATEGORIES,
	// null, null, null, null, RECORD_ID);
	// while (cc.moveToNext()) {
	// if (id.equals(cc.getString(0))) {
	// db.delete(TABLE_NAME_CATEGORIES, "_id=?", new String[] { id });
	// return;
	// }
	// }
	// }

	/************************************************** 分类服务缓存表 ********************************************************/

	/**
	 * 分类服务缓存表
	 */
	public static final String TABLE_SERVICE = "t_service";

	/**
	 * 分类服务缓存表中的列定义
	 * 
	 * @author zyn
	 */
	public interface ServiceColumns {
		public static final String SERVICEID = "serviceid";
		public static final String SERVICENAME = "servicename";
	}

	/**
	 * 分类服务缓存表查询列
	 */
	public static final String[] SERVICE_CATEGORIES = new String[] { RECORD_ID,
			ServiceColumns.SERVICEID, ServiceColumns.SERVICENAME };

	/**
	 * 分类服务缓存表的创建语句
	 */
	public static final String CREATE_SQL_SERVICE = "create table "
			+ TABLE_SERVICE + " (" + RECORD_ID
			+ " integer primary key autoincrement," + ServiceColumns.SERVICEID
			+ " text, " + ServiceColumns.SERVICENAME + " text " + ");";

	/**
	 * 插入搜索关键词数据
	 * 
	 * @param scbList
	 */
	public void insertService(CategoryBean bean) {

		Cursor cc = db.query(TABLE_SERVICE, SERVICE_CATEGORIES, null, null,
				null, null, RECORD_ID);
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			String sql = "insert into " + TABLE_SERVICE + " ("
					+ ServiceColumns.SERVICEID + ","
					+ ServiceColumns.SERVICENAME + ") values(?,?)";
			localDb.execSQL(sql, new Object[] { bean.getId(), bean.getName() });
			localDb.setTransactionSuccessful();
		} finally {
			localDb.endTransaction();
		}
		cc.close();
		
		

	}

	/**
	 * 分组获得商品分类子类
	 * 
	 * @return
	 */
	public List<CategoryBean> findAllService() {

		ArrayList<CategoryBean> listshop = new ArrayList<CategoryBean>();
		Cursor cur = db.query(TABLE_SERVICE, SERVICE_CATEGORIES, null, null,
				null, null, RECORD_ID);

		// for (cur.moveToLast(); !cur.isBeforeFirst() && listshop.size() < 10;
		// cur
		// .moveToPrevious()) {
		while (cur.moveToNext()) {

			CategoryBean cat = new CategoryBean();

			cat.setId(cur.getString(1));
			cat.setName(cur.getString(2));

			listshop.add(cat);
		}
		cur.close();
		return listshop;
	}

	/*************************************************** 开通城市缓存表 ********************************************************/

	/**
	 * 已开通城市列表
	 */
	public static final String TABLE_CITYVERSION = "t_openversion";

	/**
	 * 已开通城市缓存表中的列定义
	 */
	public interface CityColumns {
		public static final String CITYID = "cityid";
		public static final String CITYNAME = "cityname";
	}

	/**
	 * 已开通城市缓存表查询列
	 */
	public static final String[] CITYVERSION_CATEGORIES = new String[] {
			RECORD_ID, CityColumns.CITYID, CityColumns.CITYNAME };
	/**
	 * 已开通城市缓存表的创建语句
	 */
	public static final String CREATE_SQL_CITY_VERSION = "create table "
			+ TABLE_CITYVERSION + " (" + RECORD_ID
			+ " integer primary key autoincrement," + CityColumns.CITYID
			+ " text, " + CityColumns.CITYNAME + " text" + ");";

	/**
	 * 插入语句
	 * 
	 * @param scbList
	 */
	public void insertCityVersion(List<CityBean> temp) {
		CityBean citybean = null;
		Cursor cc = db.query(TABLE_CITYVERSION, CITYVERSION_CATEGORIES, null,
				null, null, null, RECORD_ID);
		SQLiteDatabase localDb = db;
		localDb.beginTransaction();
		for (int i = 0; i < temp.size(); i++) {
			citybean = temp.get(i);
			if (cc.getCount() > 400) {
				cc.moveToFirst();
				String str = cc.getString(0);
				db.delete(TABLE_CITYVERSION, "_id=?", new String[] { str });
			}
			ArrayList<String> list = findcity(citybean);

			if (list == null || list.size() <= 0) {
				try {
					// localDb.beginTransaction();
					String sql = "insert into " + TABLE_CITYVERSION + " ("
							+ CityColumns.CITYID + "," + CityColumns.CITYNAME
							+ ") values(?,?)";
					localDb.execSQL(sql, new Object[] { citybean.getId(),
							citybean.getName() });
					// localDb.setTransactionSuccessful();
				} finally {
					// localDb.endTransaction();
				}
			} else {
				db.delete(TABLE_CITYVERSION, "_id=?",
						new String[] { list.get(0) });
				try {
					localDb.beginTransaction();
					String sql = "insert into " + TABLE_CITYVERSION + " ("
							+ CityColumns.CITYID + "," + CityColumns.CITYNAME
							+ ") values(?,?)";
					localDb.execSQL(sql, new Object[] { citybean.getId(),
							citybean.getName() });
					// localDb.setTransactionSuccessful();
				} finally {
					localDb.endTransaction();
				}
			}

		}
		localDb.setTransactionSuccessful();
		localDb.endTransaction();
		cc.close();

	}

	/**
	 * 根据关键词查询
	 * 
	 * @param keyword
	 * @return
	 */
	private ArrayList<String> findcity(CityBean citylist) {
		Cursor c = db.query(TABLE_CITYVERSION, CITYVERSION_CATEGORIES, null,
				null, null, null, RECORD_ID);
		ArrayList<String> historyKeywordsList = new ArrayList<String>();

		while (c.moveToNext()) {

			if (citylist.getId().equals(c.getString(1))) {
				historyKeywordsList.add(c.getString(0));
			}
		}
		c.close();
		return null;
	}

	/**
	 * 已开通城市列表获取子类
	 * 
	 * @return
	 */
	public List<CityBean> findAllChildCity() {
		ArrayList<CityBean> listcity = new ArrayList<CityBean>();
		Cursor cur = db.query(TABLE_CITYVERSION, CITYVERSION_CATEGORIES, null,
				null, null, null, RECORD_ID);

		for (cur.moveToLast(); !cur.isBeforeFirst() && listcity.size() < 1000; cur
				.moveToPrevious()) {
			CityBean cat = new CityBean();
			cat.setId(cur.getString(1));
			cat.setName(cur.getString(2));
			listcity.add(cat);
		}

		cur.close();
		return listcity;
	}

	/************************************************** 分类商品缓存表 ********************************************************/

	/**
	 * 分类服务缓存表
	 */
	public static final String TABLE_PRODUCT = "t_product";

	/**
	 * 分类服务缓存表中的列定义
	 * 
	 * @author zyn
	 */
	public interface ProductColumns {
		public static final String PRODUCTID = "productid";
		public static final String PRODUCTNAME = "productname";
	}

	/**
	 * 分类服务缓存表查询列
	 */
	public static final String[] PRODUCT_CATEGORIES = new String[] { RECORD_ID,
			ProductColumns.PRODUCTID, ProductColumns.PRODUCTNAME };

	/**
	 * 分类服务缓存表的创建语句
	 */
	public static final String CREATE_SQL_PRODUCT = "create table "
			+ TABLE_PRODUCT + " (" + RECORD_ID
			+ " integer primary key autoincrement," + ProductColumns.PRODUCTID
			+ " text, " + ProductColumns.PRODUCTNAME + " text " + ");";

	/**
	 * 插入搜索关键词数据
	 * 
	 * @param scbList
	 */
	public void insertProduct(CategoryBean bean) {

		Cursor cc = db.query(TABLE_PRODUCT, PRODUCT_CATEGORIES, null, null,
				null, null, RECORD_ID);
		SQLiteDatabase localDb = db;
		try {
			localDb.beginTransaction();
			// localDb.delete(TABLE_NAME_CATEGORIES, null, null);
			String sql = "insert into " + TABLE_PRODUCT + " ("
					+ ProductColumns.PRODUCTID + ","
					+ ProductColumns.PRODUCTNAME + ") values(?,?)";
			localDb.execSQL(sql, new Object[] { bean.getId(), bean.getName() });
			localDb.setTransactionSuccessful();
		} finally {
			localDb.endTransaction();
		}
		cc.close();

	}

	/**
	 * 分组获得商品分类子类
	 * 
	 * @return
	 */
	public List<CategoryBean> findAllProduct() {

		ArrayList<CategoryBean> listshop = new ArrayList<CategoryBean>();
		Cursor cur = db.query(TABLE_PRODUCT, PRODUCT_CATEGORIES, null, null,
				null, null, RECORD_ID);

		while (cur.moveToNext()) {

			CategoryBean cat = new CategoryBean();

			cat.setId(cur.getString(1));
			cat.setName(cur.getString(2));

			listshop.add(cat);
		}
		cur.close();
		return listshop;
	}

	/************************************************** 常用小区缓存表 ********************************************************/
	/**
	 * 
	 * 常用小区表
	 */
	public static final String TABLE_NAME_HISTORY_KEYWORDS = "t_history_keyword";

	/**
	 * 常用小区表中的列定义
	 * 
	 * @author Aizhimin
	 */
	public interface HistoryKeywordsColumns {
		public static final String KEYWORD = "enabled";

	}

	/**
	 * 常用小区表查询列
	 */
	public static final String[] PROJECTION_HISTORY_KEYWORDS = new String[] {
			RECORD_ID, HistoryKeywordsColumns.KEYWORD };
	/**
	 * 常用小区表的创建语句
	 */
	public static final String CREATE_SQL_HISTORY_KEYWORDS = "create table "
			+ TABLE_NAME_HISTORY_KEYWORDS + " (" + RECORD_ID
			+ " integer primary key autoincrement,"
			+ HistoryKeywordsColumns.KEYWORD + " text " + ");";

	/**
	 * 插入搜索关键词数据
	 * 
	 * @param scbList
	 */
	public void addHistoryKeyword(String keyword) {

		Cursor cc = db.query(TABLE_NAME_HISTORY_KEYWORDS,
				PROJECTION_HISTORY_KEYWORDS, null, null, null, null, RECORD_ID);
		if (cc.getCount() > 9) {
			cc.moveToFirst();
			String str = cc.getString(0);
			db.delete(TABLE_NAME_HISTORY_KEYWORDS, "_id=?",
					new String[] { str });
		}

		ArrayList<String> list = findHistoryKeyword(keyword);
		if (list == null || list.size() <= 0) { // 是否有重复关键词
			ContentValues values = new ContentValues();
			values.put(HistoryKeywordsColumns.KEYWORD, keyword);
			db.insert(TABLE_NAME_HISTORY_KEYWORDS, RECORD_ID, values);
		} else {
			db.delete(TABLE_NAME_HISTORY_KEYWORDS, "_id=?",
					new String[] { list.get(0) });
			ContentValues values = new ContentValues();
			values.put(HistoryKeywordsColumns.KEYWORD, keyword);
			db.insert(TABLE_NAME_HISTORY_KEYWORDS, RECORD_ID, values);
		}
	}

	/**
	 * 根据关键词查询
	 * 
	 * @param keyword
	 * @return
	 */
	public ArrayList<String> findHistoryKeyword(String keyword) {
		Cursor c = db
				.query(TABLE_NAME_HISTORY_KEYWORDS,
						PROJECTION_HISTORY_KEYWORDS,
						HistoryKeywordsColumns.KEYWORD + "=?",
						new String[] { keyword }, null, null, RECORD_ID);
		ArrayList<String> historyKeywordsList = new ArrayList<String>();
		while (c.moveToNext()) {
			String historyKeyword = c.getString(0);
			historyKeywordsList.add(historyKeyword);
		}
		c.close();
		return historyKeywordsList;

	}

	/**
	 * 获取所有关键词历史数据
	 * 
	 * @return
	 */
	public ArrayList<String> findAllHistoryKeywords() {
		String historyKeyword;
		Cursor c = db.query(TABLE_NAME_HISTORY_KEYWORDS,
				PROJECTION_HISTORY_KEYWORDS, null, null, null, null, RECORD_ID);
		ArrayList<String> historyKeywordsList = new ArrayList<String>();

		for (c.moveToLast(); !c.isBeforeFirst()
				&& historyKeywordsList.size() < 10; c.moveToPrevious()) {

			historyKeyword = c.getString(1);
			historyKeywordsList.add(historyKeyword);
		}

		c.close();
		return historyKeywordsList;

	}

	/**
	 * 清除服务分类表
	 */
	public void clearServiceTab() {
		Cursor cc = db.query(TABLE_SERVICE, SERVICE_CATEGORIES, null, null,
				null, null, RECORD_ID);
		while (cc.moveToNext()) {
			String str = cc.getString(0);
			db.delete(TABLE_SERVICE, "_id=?", new String[] { str });
		}
	}

	/**
	 * 清除商品分类表
	 */
	public void clearProductTab() {
		Cursor cc = db.query(TABLE_PRODUCT, PRODUCT_CATEGORIES, null, null,
				null, null, RECORD_ID);
		while (cc.moveToNext()) {
			String str = cc.getString(0);
			db.delete(TABLE_PRODUCT, "_id=?", new String[] { str });
		}
	}

	/**
	 * 清除开通城市表
	 */

	public void clearVersionTable() {
		System.out.println("刷新数据库");
		Cursor cc = db.query(TABLE_CITYVERSION, CITYVERSION_CATEGORIES, null,
				null, null, null, RECORD_ID);
		while (cc.moveToNext()) {
			String str = cc.getString(0);
			db.delete(TABLE_CITYVERSION, "_id=?", new String[] { str });
		}

	}

}
