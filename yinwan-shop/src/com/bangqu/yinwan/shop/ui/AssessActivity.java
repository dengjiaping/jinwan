package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ProductCommentBean;
import com.bangqu.yinwan.shop.control.AssessControl;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.XListView;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class AssessActivity extends UIBaseActivity implements
		IXListViewListener, OnItemClickListener {
	private AssessControl assessControl;
	private XListView XlvAssessList;
	private MyListAdapter mylistAdapter;
	private Handler mHandler;
	private int begin = 1;
	private int total = 1;
	private int totalLinShi = 1;
	private List<ProductCommentBean> assessList = new ArrayList<ProductCommentBean>();
	private TextView tvbarleft;
	private TextView tvTittle;
	private Button btnRight;
	private LinearLayout llbarback;
	private TextView tvnodate;

	static class ViewHolder {
		ImageView ivuserhead;
		ImageView ivproducthead;
		TextView tvusername;
		TextView tvproductname;
		TextView tvcategory;
		TextView tvshopprice;
		TextView tvvipprice;
		TextView tvpromotionprice;
		TextView tvtime;
		TextView tvcoutent;
		RatingBar RatingBar;
		ImageView ivtuan;
		ImageView ivtuan2;
		ImageView ivcu;
		ImageView ivcu2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assess_list_layout);
		assessControl = new AssessControl(this);
		findView();
		new LoadAssessListTask(Integer.parseInt(SharedPrefUtil
				.getShopBean(this).getId())).execute();
	}

	@Override
	public void findView() {
		super.findView();
		tvnodate = (TextView) findViewById(R.id.tvnodate);
		llbarback = (LinearLayout) findViewById(R.id.llbarback);
		llbarback.setOnClickListener(assessControl.getbackonclClickListener());
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("评价列表");
		tvbarleft = (TextView) findViewById(R.id.tvbarleft);
		tvbarleft.setText("商品管理");
		XlvAssessList = (XListView) findViewById(R.id.XlvAssessList);
		XlvAssessList.setPullLoadEnable(true);
		mylistAdapter = new MyListAdapter(AssessActivity.this);
		XlvAssessList.setAdapter(mylistAdapter);
		XlvAssessList.setOnItemClickListener(assessControl.getOnitemClick());
		XlvAssessList.setXListViewListener(this);
		mHandler = new Handler();
	}

	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		ProductCommentBean productCommentBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		public int getCount() {
			return assessList.size();
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.assess_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.ivuserhead = (ImageView) convertView
						.findViewById(R.id.ivuserhead);
				viewHolder.ivproducthead = (ImageView) convertView
						.findViewById(R.id.ivproducthead);
				viewHolder.tvusername = (TextView) convertView
						.findViewById(R.id.tvusername);
				viewHolder.tvproductname = (TextView) convertView
						.findViewById(R.id.tvproductname);
				viewHolder.tvcategory = (TextView) convertView
						.findViewById(R.id.tvcategory);
				viewHolder.tvshopprice = (TextView) convertView
						.findViewById(R.id.tvshopprice);
				viewHolder.tvvipprice = (TextView) convertView
						.findViewById(R.id.tvvipprice);
				viewHolder.tvpromotionprice = (TextView) convertView
						.findViewById(R.id.tvpromotionprice);
				viewHolder.tvtime = (TextView) convertView
						.findViewById(R.id.tvtime);
				viewHolder.tvcoutent = (TextView) convertView
						.findViewById(R.id.tvcoutent);
				viewHolder.RatingBar = (RatingBar) convertView
						.findViewById(R.id.RatingBar);
				viewHolder.ivtuan = (ImageView) convertView
						.findViewById(R.id.ivtuan);
				viewHolder.ivtuan2 = (ImageView) convertView
						.findViewById(R.id.ivtuan2);
				viewHolder.ivcu = (ImageView) convertView
						.findViewById(R.id.ivcu);
				viewHolder.ivcu2 = (ImageView) convertView
						.findViewById(R.id.ivcu2);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			productCommentBean = assessList.get(position);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(
							productCommentBean.getProduct().getImg()
									+ "!small.jpg", viewHolder.ivproducthead);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(
							productCommentBean.getUser().getPhoto()
									+ "!small.jpg", viewHolder.ivuserhead);
			viewHolder.tvusername.setText(productCommentBean.getUser()
					.getNickname());
			viewHolder.tvproductname.setText(productCommentBean.getProduct()
					.getName());
			if (StringUtil.isBlank(productCommentBean.getProduct()
					.getProductCategory().getName())) {
				viewHolder.tvcategory.setText(productCommentBean.getProduct()
						.getProductCategory().getName());
			} else {
				viewHolder.tvcategory.setText("无分类");
			}
			if (productCommentBean.getProduct().getIsGroupon().equals("true")) {
				viewHolder.ivtuan.setVisibility(View.VISIBLE);
				viewHolder.ivtuan2.setVisibility(View.GONE);
			} else {
				viewHolder.ivtuan.setVisibility(View.GONE);
				viewHolder.ivtuan2.setVisibility(View.VISIBLE);
			}
			if (productCommentBean.getProduct().getIsPromotion().equals("true")) {
				viewHolder.ivcu.setVisibility(View.VISIBLE);
				viewHolder.ivcu2.setVisibility(View.GONE);
			} else {
				viewHolder.ivcu.setVisibility(View.GONE);
				viewHolder.ivcu2.setVisibility(View.VISIBLE);
			}

			viewHolder.tvpromotionprice.setText(productCommentBean.getProduct()
					.getVipPrice());
			viewHolder.tvshopprice.setText("店铺价："
					+ productCommentBean.getProduct().getPrice());
			viewHolder.tvtime.setText(productCommentBean.getAddTime()
					.substring(0, 16));
			viewHolder.tvcoutent.setText(productCommentBean.getContent());
			viewHolder.RatingBar.setRating(productCommentBean.getScore());
			viewHolder.RatingBar.setEnabled(false);
			return convertView;
		}
	}

	class LoadAssessListTask extends AsyncTask<String, Void, JSONObject> {
		private int shopId;

		protected LoadAssessListTask(int shopId) {
			this.shopId = shopId;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().comment(shopId, "addTime", true,
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
						List<ProductCommentBean> temp = ProductCommentBean
								.constractList(result
										.getJSONArray("productComments"));
						assessList.addAll(temp);

						total = result.getInt("totalPage");
						if (total == 1) {
							XlvAssessList.setPullLoadEnable(false);
						}
						progressbar.setVisibility(View.GONE);
						tvnodate.setVisibility(View.GONE);
						onLoad();
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						tvnodate.setVisibility(View.VISIBLE);
						XlvAssessList.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(AssessActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(AssessActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		XlvAssessList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		if (totalLinShi < total) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					begin += 1;
					new LoadAssessListTask(Integer.parseInt(SharedPrefUtil
							.getShopBean(AssessActivity.this).getId()))
							.execute();
				}
			}, 1000);
			totalLinShi++;
		} else {
			XlvAssessList.setPullLoadEnable(false);
			XlvAssessList.noLoadMore();
		}
	}

	private void onLoad() {
		XlvAssessList.stopRefresh();
		XlvAssessList.stopLoadMore(total);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

}
