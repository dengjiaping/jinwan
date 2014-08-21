package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.CategoryBean;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.helper.CategoryHelper;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class ProductListActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {
	// 顶部title
	private Button btnLeft;
	private TextView tvTittle;
	private Button btnRight;

	private TextView tvNoData;
	private String CategoryId = "";

	private ImageView imlistcategory;
	private LinearLayout lllist;

	// XListView
	private XListView XlvProductList;
	private MyListAdapter mylistAdapter;
	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;
	private List<ProductBean> productList = new ArrayList<ProductBean>(); // 存放接口数据

	private List<CategoryBean> categoryList = new ArrayList<CategoryBean>();
	// Popupwindow
	private PopupWindow popupWindow;
	private Button btnSelf;
	private Button btnSelect;
	private LinearLayout popup;
	private GridView glfenleipopup;

	static class ViewHolder {
		ImageView ivProductImg;
		TextView tvProductName;
		TextView tvyuanvip;
		TextView tvVipPrice;
		TextView tvProductCategory;
		TextView tvProductPrice;
		ImageView ivtuangou;
		ImageView ivcuxiao;
		ImageView ivtuangou2;
		ImageView ivcuxiao2;

	}

	private PopupWindow popupWindowon;
	private FLListAdapter fllAdapter;
	private TextView tvNo;

	static class ViewHolder1 {
		TextView tvcategorName;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_layout);

		findView();
		productList.clear();
		new LoadProductListTask(SharedPrefUtil.getShopBean(this).getId(),
				begin, "product", CategoryId, "addTime", "true").execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void findView() {
		super.findView();
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		lllist = (LinearLayout) findViewById(R.id.lllist);
		lllist.setOnClickListener(this);
		// tvTittle.setOnClickListener(this);
		tvTittle.setText("商品列表");
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("添加");
		btnRight.setOnClickListener(this);
		tvNoData = (TextView) findViewById(R.id.tvNoData);

		// ListView
		XlvProductList = (XListView) findViewById(R.id.XlvProductList);
		XlvProductList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(ProductListActivity.this);
		XlvProductList.setAdapter(mylistAdapter);
		XlvProductList.setOnItemClickListener(this);
		XlvProductList.setXListViewListener(this);
		imlistcategory = (ImageView) findViewById(R.id.imlistcategory);
		imlistcategory.setVisibility(View.VISIBLE);
		mHandler = new Handler();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnRight:
			Intent addintent = new Intent(ProductListActivity.this,
					ProductAddActivity.class);
			startActivityForResult(addintent, ADD_PRODUCT);
			// initPopWindow();
			break;
		case R.id.lllist:
			categoryList.clear();
			new LoadCategorycatListTask1(SharedPrefUtil.getShopBean(this)
					.getId()).execute();

			break;

		default:
			break;

		}
	}

	private void initPopWindow() {
		final PopupWindow popupwindow;
		popupWindow = new PopupWindow(ProductListActivity.this);
		popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		View contentView = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.popupwindow, null);
		popupWindow.setContentView(contentView);

		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.group_bg3));
		popupWindow.setFocusable(true);
		popupWindow.showAsDropDown(btnRight);

		popup = (LinearLayout) contentView.findViewById(R.id.popup);

		btnSelf = (Button) contentView.findViewById(R.id.btnSelf);
		btnSelect = (Button) contentView.findViewById(R.id.btnSelect);
		btnSelf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent addintent = new Intent(ProductListActivity.this,
						ProductAddActivity.class);
				startActivity(addintent);
				popupWindow.dismiss();
			}
		});
		btnSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchintent = new Intent(ProductListActivity.this,
						SearchProductActivity.class);
				startActivity(searchintent);
				popupWindow.dismiss();
			}
		});
	}

	/**
	 * 商品列表适配器(XListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		ProductBean productBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return productList.size();
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
						R.layout.product_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.ivProductImg = (ImageView) convertView
						.findViewById(R.id.ivProductImg);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.tvProductCategory = (TextView) convertView
						.findViewById(R.id.tvProductCategory);
				viewHolder.tvProductPrice = (TextView) convertView
						.findViewById(R.id.tvProductPrice);
				viewHolder.ivtuangou = (ImageView) convertView
						.findViewById(R.id.ivtuangou);
				viewHolder.ivcuxiao = (ImageView) convertView
						.findViewById(R.id.ivcuxiao);
				viewHolder.ivtuangou2 = (ImageView) convertView
						.findViewById(R.id.ivtuangou2);
				viewHolder.ivcuxiao2 = (ImageView) convertView
						.findViewById(R.id.ivcuxiao2);
				viewHolder.tvVipPrice = (TextView) convertView
						.findViewById(R.id.tvVipPrice);
				viewHolder.tvyuanvip = (TextView) convertView
						.findViewById(R.id.tvyuanvip);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			productBean = productList.get(position);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productBean.getImg() + "!small.jpg",
							viewHolder.ivProductImg);
			viewHolder.tvProductName.setText(productBean.getName());
			if (!(productBean.getProductCategory() + "").equals("null")) {
				viewHolder.tvProductCategory.setText("类别："
						+ productBean.getProductCategory().getName());
			} else {
				viewHolder.tvProductCategory.setText("暂无分类");
			}
			viewHolder.tvProductPrice.setText(productBean.getPrice());
			if (!StringUtil.isBlank(productBean.getVipPrice())) {
				if (productBean.getVipPrice().equals(".00")
						|| productBean.getVipPrice().equals("0")
						|| StringUtil.isBlank(productBean.getVipPrice())) {
					viewHolder.tvVipPrice.setText("无");
					viewHolder.tvyuanvip.setVisibility(View.GONE);
				} else {
					viewHolder.tvVipPrice.setText(productBean.getVipPrice());
				}
			}

			if (productBean.getIsPromotion().equals("true")) {
				viewHolder.ivcuxiao.setVisibility(View.GONE);
				viewHolder.ivcuxiao2.setVisibility(View.VISIBLE);
			} else {
				viewHolder.ivcuxiao.setVisibility(View.VISIBLE);
				viewHolder.ivcuxiao2.setVisibility(View.GONE);
			}
			if (productBean.getIsGroupon().equals("true")) {

				viewHolder.ivtuangou2.setVisibility(View.VISIBLE);
				viewHolder.ivtuangou.setVisibility(View.GONE);
			} else {
				viewHolder.ivtuangou.setVisibility(View.VISIBLE);
				viewHolder.ivtuangou2.setVisibility(View.GONE);
			}

			return convertView;
		}
	}

	/**
	 * 商品列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadProductListTask extends AsyncTask<String, Void, JSONObject> {
		private String shopId;
		private int begin;
		private String type;
		private String productCategoryId;
		private String order;
		private String desc;

		protected LoadProductListTask(String shopId, int begin, String type,
				String productCategoryId, String order, String desc) {
			this.shopId = shopId;
			this.begin = begin;
			this.type = type;
			this.productCategoryId = productCategoryId;
			this.order = order;
			this.desc = desc;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productList(shopId, begin, type,
						productCategoryId, order, desc);
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
						List<ProductBean> temp = ProductBean
								.constractList(result.getJSONArray("products"));
						productList.addAll(temp);
						// productList.add()
						// mylistAdapter.notifyDataSetChanged();

						tvNoData.setVisibility(View.GONE);
						total = result.getInt("totalPage");
						if (total == 1) {
							XlvProductList.setPullLoadEnable(false);
						}
						progressbar.setVisibility(View.GONE);
						onLoad();

					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
						progressbar.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductListActivity.this, "数据加载失败1",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ProductListActivity.this, "数据加载失败2",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Toast.makeText(ProductListActivity.this, "数据加载失败3",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	// 商品分类搜索列表
	private void initPopWindowone() {
		final PopupWindow popupwindowone = null;
		popupWindowon = new PopupWindow(ProductListActivity.this);
		popupWindowon.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		popupWindowon.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		View contentView1 = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.fenleipop, null);
		popupWindowon.setContentView(contentView1);
		popupWindowon.setFocusable(true);
		popupWindowon.showAsDropDown(tvTittle);
		// tvNo = (TextView) findViewById(R.id.tvNo);
		glfenleipopup = (GridView) contentView1
				.findViewById(R.id.glfenleipopup);
		fllAdapter = new FLListAdapter(ProductListActivity.this);
		glfenleipopup.setAdapter(fllAdapter);

		glfenleipopup.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("null")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				tvTittle.setText(categoryList.get(arg2).getName() + "");
				System.out.println(categoryList.get(arg2).getId());
				productList.clear();
				progressbar.setVisibility(View.VISIBLE);
				new LoadProductListTask(SharedPrefUtil.getShopBean(
						ProductListActivity.this).getId(), begin, "product",
						categoryList.get(arg2).getId(), "addTime", "true")
						.execute();
				popupWindowon.dismiss();
			}
		});

	}

	/**
	 * 商品分类列表适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class FLListAdapter extends BaseAdapter {
		private Context FLContext;
		CategoryBean categoryBean;

		public FLListAdapter(Context context) {
			this.FLContext = context;
		}

		public int getCount() {
			return categoryList.size();
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position1, View convertView1, ViewGroup parent1) {
			ViewHolder1 viewHolder1 = null;
			if (convertView1 == null) {
				convertView1 = LayoutInflater.from(FLContext).inflate(
						R.layout.fenleipopup_item, null);
				viewHolder1 = new ViewHolder1();

				viewHolder1.tvcategorName = (TextView) convertView1
						.findViewById(R.id.tvfenlei);

				convertView1.setTag(viewHolder1);
			} else {
				viewHolder1 = (ViewHolder1) convertView1.getTag();
			}

			categoryBean = categoryList.get(position1);
			viewHolder1.tvcategorName.setText(categoryBean.getName());

			return convertView1;
		}
	}

	/**
	 * 商品分类列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadCategorycatListTask1 extends AsyncTask<String, Void, JSONObject> {
		private String shopId;

		protected LoadCategorycatListTask1(String shopId) {
			this.shopId = shopId;

		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CategoryHelper().ProductCategoryList(shopId);
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
										.getJSONArray("productCategories"));
						categoryList.addAll(temp);
						initPopWindowone();
						fllAdapter.notifyDataSetChanged();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductListActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ProductListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Toast.makeText(ProductListActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		System.out.println(arg2);
		// progressbar.setVisibility(View.VISIBLE);

		Intent itemintent = new Intent(ProductListActivity.this,
				ProductUpdateActivity.class);
		itemintent.putExtra("ProductId", productList.get(arg2 - 1).getId());
		itemintent.putExtra("info", "修改商品信息");
		if (!(productList.get(arg2 - 1).getProductCategory() + "").equals("")
				&& !(productList.get(arg2 - 1).getProductCategory() + "")
						.equals("null")) {
			itemintent.putExtra("productcategoruIs", productList.get(arg2 - 1)
					.getProductCategory().getId());
		} else {
			itemintent.putExtra("productcategoruIs", "");
		}
		Constants.productname = productList.get(arg2 - 1).getName();
		startActivityForResult(itemintent, UPDATE_PRODUCT);
	}

	private static final int ADD_PRODUCT = 89328;
	private static final int UPDATE_PRODUCT = 111;

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent result) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case UPDATE_PRODUCT:
				progressbar.setVisibility(View.VISIBLE);
				begin = 1;
				totalLinShi = 1;
				total = 1;
				mylistAdapter.notifyDataSetChanged();
				productList.clear();
				new LoadProductListTask(SharedPrefUtil.getShopBean(this)
						.getId(), begin, "product", CategoryId, "addTime",
						"true").execute();
				break;

			case ADD_PRODUCT:
				progressbar.setVisibility(View.VISIBLE);
				begin = 1;
				totalLinShi = 1;
				total = 1;

				mylistAdapter.notifyDataSetChanged();
				productList.clear();
				new LoadProductListTask(SharedPrefUtil.getShopBean(this)
						.getId(), begin, "product", CategoryId, "addTime",
						"true").execute();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onRefresh() {
		XlvProductList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadProductListTask(SharedPrefUtil.getShopBean(
							ProductListActivity.this).getId(), begin,
							"product", CategoryId, "addTime", "true").execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			// XlvProductList.setPullLoadEnable(false);
			XlvProductList.noLoadMore();
		}
	}

	private void onLoad() {
		XlvProductList.stopRefresh();
		XlvProductList.stopLoadMore(total);
	}

}
