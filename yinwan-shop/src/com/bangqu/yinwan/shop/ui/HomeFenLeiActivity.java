package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.CategoryBean;
import com.bangqu.yinwan.shop.helper.CategoryHelper;
import com.bangqu.yinwan.shop.helper.FenLeiHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.DataBaseAdapter;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.GrapeGridview;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class HomeFenLeiActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;

	private EditText etCatagory1;
	private String Str1 = "";
	private String Str2 = "";
	private String Str3 = "";
	private String Str4 = "";
	private String ID1 = "";
	private String ID2 = "";
	private String ID3 = "";
	private String ID4 = "";
	private String SumCategory = "";
	private String SumId = "";
	private int Sum = 0;

	// 数据库缓存
	// private DataBaseAdapter dataBaseAdapter;

	private ScrollView scrollView;

	// 两个gridview(商品和服务)
	static class ViewHolderOne {
		TextView tvname;
		LinearLayout llFenLeiItem;
	}

	static class ViewHolderTwo {
		TextView tvname;
	}

	private String strfenleione = "";
	private String strfenleitwo = "";
	private String strfenleithr = "";
	private String strfenleifour = "";

	// 社区服务
	private GrapeGridview gvFenLeiOne;
	private MyListOneAdapter mylistOneAdapter;
	private List<CategoryBean> OneList = new ArrayList<CategoryBean>();
	private List<String> OneIdList = new ArrayList<String>();

	// 商品服务
	private GrapeGridview gvFenLeiTwo;
	private MyListTwoAdapter mylistTwoAdapter;
	private List<CategoryBean> TwoList = new ArrayList<CategoryBean>();
	private List<String> TwoIdList = new ArrayList<String>();

	private List<CategoryBean> categoryList = new ArrayList<CategoryBean>();
	CategoryBean categoryBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_fenlei_layout);

		findView();
		OneList.clear();
		new LoadFenLeiListTask(2, 1, "11").execute();
		TwoList.clear();
		new LoadFenLeiListTask(2, 2, "11").execute();
		new LoadCategoryViewTask(
				SharedPrefUtil.getToken(HomeFenLeiActivity.this),
				SharedPrefUtil.getShopBean(HomeFenLeiActivity.this).getId())
				.execute();
	}

	@Override
	public void findView() {
		super.findView();
		etCatagory1 = (EditText) findViewById(R.id.etCatagory1);

		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("行业管理");

		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		btnRight.setText("提交");
		btnRight.setOnClickListener(this);
		scrollView = (ScrollView) findViewById(R.id.scrollView);

		// 服务
		gvFenLeiOne = (GrapeGridview) findViewById(R.id.gvFenLeiOne);
		gvFenLeiOne.setOnItemClickListener(this);

		mylistOneAdapter = new MyListOneAdapter(HomeFenLeiActivity.this);
		gvFenLeiOne.setAdapter(mylistOneAdapter);

		// 商品
		gvFenLeiTwo = (GrapeGridview) findViewById(R.id.gvFenLeiTwo);
		gvFenLeiTwo.setOnItemClickListener(this);

		mylistTwoAdapter = new MyListTwoAdapter(HomeFenLeiActivity.this);
		gvFenLeiTwo.setAdapter(mylistTwoAdapter);

		// 数据库缓存
		// dataBaseAdapter = ((CommonApplication) getApplicationContext())
		// .getDbAdapter();
	}

	@Override
	public void fillData() {
		super.fillData();
		etCatagory1.setText(SumCategory);
		switch (categoryList.size()) {
		//case 0:
			
		//	break;
		
		case 1:
			strfenleione = categoryList.get(0).getId() + ",";
			break;
		case 2:
			strfenleione = categoryList.get(0).getId() + ",";
			//strfenleitwo = categoryList.get(1).getName() + ",";
			strfenleitwo = categoryList.get(1).getId() + ",";
			
			break;
		case 3:
			strfenleione = categoryList.get(0).getId() + ",";
			strfenleitwo = categoryList.get(1).getId() + ",";
			strfenleithr = categoryList.get(2).getId() + ",";
			break;

		default:
			break;
		}
		mylistOneAdapter.notifyDataSetChanged();
		mylistTwoAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnRight:

			if (!strfenleione.equals(",")) {
				SumId = strfenleione;
			}
			if (!strfenleione.equals(",") && !strfenleitwo.equals(",")) {
				SumId = strfenleione + strfenleitwo;
			}
			if (!strfenleione.equals(",") && !strfenleitwo.equals(",")
					&& !strfenleitwo.equals(",")) {
				SumId = strfenleione + strfenleitwo + strfenleithr;
			}

			if (!StringUtil.isBlank(SumId)) {
				new LoadSubmitCategoryTask(SharedPrefUtil.getToken(this),
						SharedPrefUtil.getShopBean(this).getId(), SumId)
						.execute();
			} else {
				finish();
			}

			break;

		default:
			break;
		}

	}

	/**
	 * gridviewOne适配器
	 * 
	 * @author Administrator
	 */
	private class MyListOneAdapter extends BaseAdapter {
		private Context mContext;
		CategoryBean categorieBean;

		public MyListOneAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return OneList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) { // shop_detial_items
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderOne viewHolder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.fenlei_item, null);
				viewHolder = new ViewHolderOne();
				viewHolder.tvname = (TextView) convertView
						.findViewById(R.id.tvName);
				viewHolder.llFenLeiItem = (LinearLayout) convertView
						.findViewById(R.id.llFenLeiItem);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderOne) convertView.getTag();
			}

			categorieBean = OneList.get(position);
			viewHolder.tvname.setText(categorieBean.getName());
			if ((OneList.get(position).getName() + ",").equals(strfenleione)) {
				viewHolder.tvname.setBackground(getResources().getDrawable(
						R.drawable.shopdetail_addproduct));
				Toast.makeText(HomeFenLeiActivity.this, "成功",
						Toast.LENGTH_SHORT).show();
			}
			if ((OneList.get(position).getId() + ",").equals(strfenleione)
					|| (OneList.get(position).getId() + ",")
							.equals(strfenleitwo)
					|| (OneList.get(position).getId() + ",")
							.equals(strfenleithr)
					|| (OneList.get(position).getId() + ",")
							.equals(strfenleifour)) {
				viewHolder.tvname.setBackground(getResources().getDrawable(
						R.drawable.home_category_bg));
				viewHolder.tvname.setTextColor(getResources().getColor(
						R.color.color_white));
			} else {
				viewHolder.tvname.setBackgroundColor(getResources().getColor(
						R.color.color_bg));
				viewHolder.tvname.setTextColor(getResources().getColor(
						R.color.color_grey3));
			}

			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		switch (parent.getId()) {
		case R.id.gvFenLeiOne:
			Sum++;
			if (Sum >= 4)
				Sum = 4;
			if (Sum == 1) {
				Str1 = OneList.get(position).getName() + ",";
				ID1 = OneList.get(position).getId() + ",";
				strfenleione = ID1;
			}
			if (Sum == 2) {
				Str2 = OneList.get(position).getName() + ",";
				ID2 = OneList.get(position).getId() + ",";
				strfenleitwo = ID2;
				if (ID1.equals(ID2)) {
					Str1 = Str2;
					ID1 = ID2;
					Str2 = "";
					ID2 = "";
					Sum = Sum - 1;
					strfenleitwo = "";
					strfenleione = "";
					Sum = 0;
				}
			}
			if (Sum == 3) {
				Str3 = OneList.get(position).getName() + ",";
				ID3 = OneList.get(position).getId() + ",";
				strfenleithr = ID3;
				if (ID1.equals(ID3)) {
					Str1 = Str2;
					ID1 = ID2;
					Str2 = Str3;
					ID2 = ID3;
					Str3 = "";
					ID3 = "";
					Sum = Sum - 1;

					strfenleithr = "";
					strfenleione = "";
					strfenleione = strfenleitwo;
					strfenleitwo = "";
					Sum = 1;
				} else if (ID2.equals(ID3)) {
					Str2 = Str3;
					ID2 = ID3;
					Str3 = "";
					ID3 = "";
					Sum = Sum - 1;
					strfenleitwo = "";
					strfenleithr = "";
					Sum = 1;
				}
			}
			if (Sum == 4) {
				Str4 = OneList.get(position).getName() + ",";
				ID4 = OneList.get(position).getId() + ",";
				strfenleifour = ID4;
				if (ID1.equals(ID4)) {
					Str1 = Str2;
					ID1 = ID2;
					Str2 = Str3;
					ID2 = ID3;
					Str3 = Str4;
					ID3 = ID4;
					Str4 = "";
					ID4 = "";

					strfenleione = strfenleitwo;
					strfenleitwo = strfenleithr;
					strfenleithr = "";
					strfenleifour = "";
					Sum = 2;
				} else if (ID2.equals(ID4)) {
					Str2 = Str3;
					ID2 = ID3;
					Str3 = Str4;
					ID3 = ID4;
					Str4 = "";
					ID4 = "";

					strfenleitwo = strfenleithr;
					strfenleithr = "";
					strfenleifour = "";
					Sum = 2;
				} else if (ID3.equals(ID4)) {
					Str3 = Str4;
					ID3 = ID4;
					Str4 = "";
					ID4 = "";
					strfenleithr = "";
					strfenleifour = "";
					Sum = 2;
				} else {
					Str1 = Str2;
					ID1 = ID2;
					Str2 = Str3;
					ID2 = ID3;
					Str3 = Str4;
					ID3 = ID4;
					Str4 = "";
					ID4 = "";
					strfenleione = strfenleitwo;
					strfenleitwo = strfenleithr;
					strfenleithr = strfenleifour;
					strfenleifour = "";
				}
			}

			if (Sum <= 3) {
				SumCategory = Str1 + Str2 + Str3;
				etCatagory1.setText(Str1 + Str2 + Str3);
				SumId = (ID1 + ID2 + ID3);
			}
			if (Sum > 3) {
				SumCategory = Str1 + Str2 + Str3;
				etCatagory1.setText(Str1 + Str2 + Str3);
				SumId = (ID1 + ID2 + ID3);
			}

			break;

		case R.id.gvFenLeiTwo:
			Sum++;
			if (Sum >= 4)
				Sum = 4;
			if (Sum == 1) {
				Str1 = TwoList.get(position).getName() + ",";
				ID1 = TwoList.get(position).getId() + ",";
				strfenleione = ID1;
			}
			if (Sum == 2) {
				Str2 = TwoList.get(position).getName() + ",";
				ID2 = TwoList.get(position).getId() + ",";
				strfenleitwo = ID2;
				if (ID1.equals(ID2)) {
					Str1 = Str2;
					ID1 = ID2;
					Str2 = "";
					ID2 = "";
					Sum = Sum - 1;
					strfenleitwo = "";
					strfenleione = "";
					Sum = 0;
				}
			}
			if (Sum == 3) {
				Str3 = TwoList.get(position).getName() + ",";
				ID3 = TwoList.get(position).getId() + ",";
				strfenleithr = ID3;
				if (ID1.equals(ID3)) {
					Str1 = Str2;
					ID1 = ID2;
					Str2 = Str3;
					ID2 = ID3;
					Str3 = "";
					ID3 = "";
					Sum = Sum - 1;
					strfenleithr = "";
					strfenleione = "";
					strfenleione = strfenleitwo;
					strfenleitwo = "";
					Sum = 1;
				} else if (ID2.equals(ID3)) {
					Str2 = Str3;
					ID2 = ID3;
					Str3 = "";
					ID3 = "";
					Sum = Sum - 1;
					strfenleitwo = "";
					strfenleithr = "";
					Sum = 1;
				}
			}
			if (Sum == 4) {
				Str4 = TwoList.get(position).getName() + ",";
				ID4 = TwoList.get(position).getId() + ",";
				strfenleifour = ID4;
				if (ID1.equals(ID4)) {
					Str1 = Str2;
					ID1 = ID2;
					Str2 = Str3;
					ID2 = ID3;
					Str3 = Str4;
					ID3 = ID4;
					Str4 = "";
					ID4 = "";
					strfenleione = strfenleitwo;
					strfenleitwo = strfenleithr;
					strfenleithr = "";
					strfenleifour = "";
					Sum = 2;
				} else if (ID2.equals(ID4)) {
					Str2 = Str3;
					ID2 = ID3;
					Str3 = Str4;
					ID3 = ID4;
					Str4 = "";
					ID4 = "";
					strfenleitwo = strfenleithr;
					strfenleithr = "";
					strfenleifour = "";
					Sum = 2;
				} else if (ID3.equals(ID4)) {
					Str3 = Str4;
					ID3 = ID4;
					Str4 = "";
					ID4 = "";
					strfenleithr = "";
					strfenleifour = "";
					Sum = 2;
				} else {
					Str1 = Str2;
					ID1 = ID2;
					Str2 = Str3;
					ID2 = ID3;
					Str3 = Str4;
					ID3 = ID4;
					Str4 = "";
					ID4 = "";
					strfenleione = strfenleitwo;
					strfenleitwo = strfenleithr;
					strfenleithr = strfenleifour;
					strfenleifour = "";
				}
			}

			if (Sum <= 3) {
				SumCategory = Str1 + Str2 + Str3;
				etCatagory1.setText(Str1 + Str2 + Str3);
				SumId = (ID1 + ID2 + ID3);
			}
			if (Sum > 3) {
				SumCategory = Str1 + Str2 + Str3;
				etCatagory1.setText(Str1 + Str2 + Str3);
				SumId = (ID1 + ID2 + ID3);

			}
			break;

		default:
			break;
		}
		mylistOneAdapter.notifyDataSetChanged();
		mylistTwoAdapter.notifyDataSetChanged();
	}

	/**
	 * 预览商店分类
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadCategoryViewTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String id;

		protected LoadCategoryViewTask(String accessToken, String id) {
			this.accessToken = accessToken;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CategoryHelper().ShopCategoryView(accessToken, id);

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
						List<CategoryBean> temp = CategoryBean
								.constractList(result
										.getJSONArray("categories"));
						categoryList.addAll(temp);
						fillCategoryData();
						fillData();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(HomeFenLeiActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(HomeFenLeiActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(HomeFenLeiActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			}
		}

		private void fillCategoryData() {
			switch (categoryList.size()) {
			case 0:
				break;
			case 1:
				Str1 = categoryList.get(0).getName() + ",";
				ID1 = categoryList.get(0).getId() + ",";
				Str2 = "";
				ID2 = "";
				Str3 = "";
				ID3 = "";
				Sum = 1;
				SumCategory = Str1 + Str2 + Str3;
				etCatagory1.setText(Str1 + Str2 + Str3);
				SumId = (ID1 + ID2 + ID3);
				break;
			case 2:
				Str1 = categoryList.get(0).getName() + ",";
				Str2 = categoryList.get(1).getName() + ",";
				ID1 = categoryList.get(0).getId() + ",";
				ID2 = categoryList.get(1).getId() + ",";
				Str3 = "";
				ID3 = "";
				Sum = 2;
				SumCategory = Str1 + Str2 + Str3;
				etCatagory1.setText(Str1 + Str2 + Str3);
				SumId = (ID1 + ID2 + ID3);
				break;
			case 3:
				Str1 = categoryList.get(0).getName() + ",";
				Str2 = categoryList.get(1).getName() + ",";
				Str3 = categoryList.get(2).getName() + ",";
				ID1 = categoryList.get(0).getId() + ",";
				ID2 = categoryList.get(1).getId() + ",";
				ID3 = categoryList.get(2).getId() + ",";
				Sum = 3;
				SumCategory = Str1 + Str2 + Str3;
				etCatagory1.setText(Str1 + Str2 + Str3);
				SumId = (ID1 + ID2 + ID3);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 添加商店分类
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadSubmitCategoryTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String id;
		private String categoryIds;

		protected LoadSubmitCategoryTask(String accessToken, String id,
				String categoryIds) {
			this.accessToken = accessToken;
			this.id = id;
			this.categoryIds = categoryIds;
		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						HomeFenLeiActivity.this, "正在添加……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CategoryHelper().ShopCategoryAdd(accessToken, id,
						categoryIds);

			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pd != null)
				pd.dismiss();
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(HomeFenLeiActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(HomeFenLeiActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(HomeFenLeiActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(HomeFenLeiActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}
	}

	/**
	 * gridviewTwo适配器
	 * 
	 * @author Administrator
	 */
	private class MyListTwoAdapter extends BaseAdapter {
		private Context mContext;
		CategoryBean categorieBean;

		public MyListTwoAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return TwoList.size();
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
			ViewHolderOne viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.fenlei_item, null);
				viewHolder = new ViewHolderOne();
				viewHolder.tvname = (TextView) convertView
						.findViewById(R.id.tvName);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderOne) convertView.getTag();
			}
			categorieBean = TwoList.get(position);
			viewHolder.tvname.setText(categorieBean.getName());
			if ((TwoList.get(position).getId() + ",").equals(strfenleione)
					|| (TwoList.get(position).getId() + ",")
							.equals(strfenleitwo)
					|| (TwoList.get(position).getId() + ",")
							.equals(strfenleithr)
					|| (TwoList.get(position).getId() + ",")
							.equals(strfenleifour)) {
				viewHolder.tvname.setBackground(getResources().getDrawable(
						R.drawable.home_category_bg));
				viewHolder.tvname.setTextColor(getResources().getColor(
						R.color.color_white));
			} else {
				viewHolder.tvname.setBackgroundColor(getResources().getColor(
						R.color.color_bg));
				viewHolder.tvname.setTextColor(getResources().getColor(
						R.color.color_grey3));
			}
			return convertView;
		}

	}

	/**
	 * 社区和商品服务列表
	 * 
	 * @author Administrator
	 */
	class LoadFenLeiListTask extends AsyncTask<String, Void, JSONObject> {
		private int level;
		private int parentId;
		private String version;

		protected LoadFenLeiListTask(int level, int parentId, String version) {
			this.level = level;
			this.parentId = parentId;
			this.version = version;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new FenLeiHelper().list(level, parentId, version);
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
						SharedPrefUtil.setcategorylists(
								HomeFenLeiActivity.this,
								result.getString("version"));
						List<CategoryBean> temp = CategoryBean
								.constractList(result
										.getJSONArray("categories"));
						switch (parentId) {
						case 1:
							OneList = temp;
							// for (CategoryBean post : OneList) {
							// dataBaseAdapter.clearServiceTab();
							// OneIdList.add(post.getId() + "");
							// dataBaseAdapter.insertService(post);
							// }
							break;
						case 2:
							TwoList = temp;
							// for (CategoryBean post : TwoList) {
							// dataBaseAdapter.clearProductTab();
							// TwoIdList.add(post.getId() + "");
							// dataBaseAdapter.insertProduct(post);
							// }
							break;
						default:
							break;
						}
						// SharedPrefUtil.setFistfenlei(HomeFenLeiActivity.this);
						mylistOneAdapter.notifyDataSetChanged();
						mylistTwoAdapter.notifyDataSetChanged();
						scrollView.smoothScrollTo(0, 0);
						progressbar.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {
						// if (!SharedPrefUtil
						// .isFistfenlei(HomeFenLeiActivity.this)) {
						// OneList = dataBaseAdapter.findAllService();
						// TwoList = dataBaseAdapter.findAllProduct();
						// mylistOneAdapter.notifyDataSetChanged();
						// mylistTwoAdapter.notifyDataSetChanged();
						// }
						progressbar.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(HomeFenLeiActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			}

		}
	}

}
