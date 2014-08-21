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
import com.bangqu.yinwan.shop.ui.ProductListActivity.ViewHolder1;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class ServiceListActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {
	// 顶部title
	private Button btnLeft;
	private TextView tvTittle;
	private Button btnRight;

	private TextView tvNoData;

	// XListView
	private XListView XlvProductList;
	private MyListAdapter mylistAdapter;
	private Handler mHandler;
	private int begin = 1;
	private int total = 1; // 判断是否还要继续下拉刷新
	private int totalLinShi = 1;
	private List<ProductBean> productList = new ArrayList<ProductBean>();

	private List<CategoryBean> categoryList = new ArrayList<CategoryBean>();

	// Popupwindow
	private PopupWindow popupWindow;

	private PopupWindow popupWindowon;
	private GridView glfenleipopup;
	private FLListAdapter fllAdapter;
	private Button btnSelf;
	private Button btnSelect;
	private LinearLayout popup;
	private String CategoryId = "";

	private LinearLayout lllist;
	private ImageView imlistcategory;

	static class ViewHolder {
		ImageView ivProductImg;
		TextView tvProductName;
		TextView tvProductCategory;
		TextView tvProductPrice;
		ImageView ivtuangou;
		ImageView ivtuangou2;
		ImageView ivcuxiao2;
		ImageView ivcuxiao;
		LinearLayout llVip;
		TextView tvVipPrice;
		TextView tvyuanvip;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_layout);

		findView();
		productList.clear();
		new LoadProductListTask(SharedPrefUtil.getShopBean(this).getId(),
				begin, "service", CategoryId, "addTime", "true").execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void findView() {
		super.findView();
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("服务列表");
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("添加");
		btnRight.setOnClickListener(this);
		tvNoData = (TextView) findViewById(R.id.tvNoData);
		lllist = (LinearLayout) findViewById(R.id.lllist);
		lllist.setOnClickListener(this);

		imlistcategory = (ImageView) findViewById(R.id.imlistcategory);
		imlistcategory.setVisibility(View.VISIBLE);

		// ListView
		XlvProductList = (XListView) findViewById(R.id.XlvProductList);
		XlvProductList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(ServiceListActivity.this);
		XlvProductList.setAdapter(mylistAdapter);
		XlvProductList.setOnItemClickListener(this);
		XlvProductList.setXListViewListener(this);
		mHandler = new Handler();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnRight:
			Intent addintent = new Intent(ServiceListActivity.this,
					ServiceAddActivity.class);
			startActivityForResult(addintent, ADD_SERVICE);
			// initPopWindow();
			break;
		case R.id.lllist:
			categoryList.clear();
			new LoadCategorycatListTask1(SharedPrefUtil.getToken(this),
					SharedPrefUtil.getShopBean(this).getId(), "true").execute();
			initPopWindowone();
			break;
		default:
			break;

		}
	}

	private void initPopWindow() {
		// TODO Auto-generated method stub
		final PopupWindow popupwindow;
		popupWindow = new PopupWindow(ServiceListActivity.this);
		// popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setWidth(200);
		// popupWindow.setHeight(180);

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
				Intent addintent = new Intent(ServiceListActivity.this,
						ServiceAddActivity.class);
				startActivity(addintent);
			}
		});
		btnSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchintent = new Intent(ServiceListActivity.this,
						SearchProductActivity.class);
				startActivity(searchintent);
			}
		});
	}

	/**
	 * 服务列表适配器(XListView)
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
				viewHolder.llVip = (LinearLayout) convertView
						.findViewById(R.id.llVip);
				viewHolder.tvVipPrice = (TextView) convertView
						.findViewById(R.id.tvVipPrice);
				viewHolder.tvyuanvip = (TextView) convertView
						.findViewById(R.id.tvyuanvip);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			productBean = productList.get(position);
			((CommonApplication) getApplicationContext())
					.getImgLoader()
					.DisplayImage(productBean.getImg(), viewHolder.ivProductImg);
			viewHolder.tvProductName.setText(productBean.getName());
			if (!(productBean.getProductCategory() + "").equals("null")) {
				viewHolder.tvProductCategory.setText("类别："
						+ productBean.getProductCategory().getName());
			} else {
				viewHolder.tvProductCategory.setText("暂无分类");
			}
			// 小数点后保留两位
			// DecimalFormat df = new DecimalFormat("#.00");
			// df.format(Double.parseDouble(productBean.getPrice()));
			// viewHolder.tvProductPrice.setText(df.format(Double
			// .parseDouble(productBean.getPrice())));

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
	 * 服务列表
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
						// productList.clear();
						productList.addAll(temp);
						mylistAdapter.notifyDataSetChanged();

						total = result.getInt("totalPage");
						if (total == 1) {
							XlvProductList.setPullLoadEnable(false);
						}
						// productIds.clear();
						// productIds.add("表头占用项");
						// for (ProductBean post : productList) {
						// productIds.add(post.getId() + "");
						// }
						tvNoData.setVisibility(View.GONE);
						progressbar.setVisibility(View.GONE);
						onLoad();

					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ServiceListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ServiceListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Toast.makeText(ServiceListActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	// 商品分类搜索列表
	private void initPopWindowone() {
		final PopupWindow popupwindowone = null;
		popupWindowon = new PopupWindow(ServiceListActivity.this);
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

		fllAdapter = new FLListAdapter(ServiceListActivity.this);
		glfenleipopup.setAdapter(fllAdapter);

		glfenleipopup.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("null")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(ProductListActivity.this,
				// categoryList.get(arg2).getId(), 100).show();

				tvTittle.setText(categoryList.get(arg2).getName() + "");
				System.out.println(categoryList.get(arg2).getId());
				productList.clear();
				progressbar.setVisibility(View.VISIBLE);
				new LoadProductListTask(SharedPrefUtil.getShopBean(
						ServiceListActivity.this).getId(), begin, "service",
						categoryList.get(arg2).getId(), "addTime", "true")
						.execute();
				popupWindowon.dismiss();
			}
		});

	}

	/**
	 * 服务分类列表适配器
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
	 * 服务分类列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadCategorycatListTask1 extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String shopId;
		private String enabled;

		protected LoadCategorycatListTask1(String accessToken, String shopId,
				String enabled) {
			this.accessToken = accessToken;
			this.enabled = enabled;
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
						fllAdapter.notifyDataSetChanged();

					} else if (result.getInt("status") == Constants.FAIL) {
						// tvNo.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ServiceListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(ServiceListActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Toast.makeText(ServiceListActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		Intent itemintent = new Intent(ServiceListActivity.this,
				ProductUpdateActivity.class);
		itemintent.putExtra("ProductId", productList.get(arg2 - 1).getId());
		itemintent.putExtra("info", "修改服务信息");
		if (!(productList.get(arg2 - 1).getProductCategory() + "").equals("")
				&& !(productList.get(arg2 - 1).getProductCategory() + "")
						.equals("null")) {
			itemintent.putExtra("productcategoruIs", productList.get(arg2 - 1)
					.getProductCategory().getId());
		} else {
			itemintent.putExtra("productcategoruIs", "");
		}

		startActivityForResult(itemintent, UPDATE_PRODUCT);
	}

	private static final int UPDATE_PRODUCT = 1;
	private static final int ADD_SERVICE = 58768;

	/**
	 * 复写onActivityResult，这个方法 是要等到第二个Activity点了提交过后才会执行的
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent result) {
		// 可以根据多个请求代码来作相应的操作
		// if (UPDATE_PRODUCT == requestCode) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case UPDATE_PRODUCT:
				progressbar.setVisibility(View.VISIBLE);
				begin = 1;
				totalLinShi = 1;
				total = 1;
				productList.clear();
				mylistAdapter.notifyDataSetChanged();
				new LoadProductListTask(

				SharedPrefUtil.getShopBean(ServiceListActivity.this).getId(),
						begin, "service", CategoryId, "addTime", "true")
						.execute();
				break;
			case ADD_SERVICE:
				progressbar.setVisibility(View.VISIBLE);
				begin = 1;
				totalLinShi = 1;
				total = 1;
				productList.clear();
				mylistAdapter.notifyDataSetChanged();
				new LoadProductListTask(

				SharedPrefUtil.getShopBean(ServiceListActivity.this).getId(),
						begin, "service", CategoryId, "addTime", "true")
						.execute();
				break;
			default:
				break;
			}
		}
	}

	// }

	@Override
	public void onRefresh() {
		XlvProductList.stopRefresh();
	}

	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadProductListTask(SharedPrefUtil.getShopBean(
							ServiceListActivity.this).getId(), begin,
							"service", CategoryId, "addTime", "true").execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvProductList.setPullLoadEnable(false);
			XlvProductList.noLoadMore();
		}
	}

	private void onLoad() {
		XlvProductList.stopRefresh();
		XlvProductList.stopLoadMore(total);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
