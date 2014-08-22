package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ShopBean;
import com.bangqu.yinwan.shop.helper.ShopHelper;
import com.bangqu.yinwan.shop.helper.UserHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.widget.XListView.IXListViewListener;

public class SelectShopActivity extends UIBaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {

	private Button btnRight;
	private LinearLayout llAddShop;
	private LinearLayout llbarback;

	private String canCreate;
	private TextView tvsave;
	private LinearLayout llisshop;// 店铺
	private LinearLayout llnoshop;// 无店铺
	private Button btncreateshop;
	private TextView tvbarleft;
	private ListView XlvShopList;
	private MyListAdapter mylistAdapter;
	ShopBean shopBean;
	private List<ShopBean> shopList = new ArrayList<ShopBean>();

	static class ViewHolder {
		ImageView ivshopselecthead;
		TextView tvshopselectname;
		TextView tvdetail;
		TextView tvstategreen;
		TextView tvstatered;
		ImageView ivgren;
		ImageView ivred;
		TextView tvshopdetail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectshop_layout);
		findView();

	}

	@Override
	protected void onResume() {
		super.onResume();
		new LoadShopListTask(SharedPrefUtil.getToken(SelectShopActivity.this))
				.execute();
	}

	@Override
	public void findView() {
		super.findView();
		tvbarleft = (TextView) findViewById(R.id.tvbarleft);
		tvbarleft.setText("切换店铺");
		llisshop = (LinearLayout) findViewById(R.id.llisshop);
		llnoshop = (LinearLayout) findViewById(R.id.llnoshop);
		btncreateshop = (Button) findViewById(R.id.btncreateshop);
		btncreateshop.setOnClickListener(this);
		llbarback = (LinearLayout) findViewById(R.id.llbarback);
		llbarback.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("退出");
		btnRight.setOnClickListener(this);
		llAddShop = (LinearLayout) findViewById(R.id.llAddShop);
		llAddShop.setOnClickListener(this);
		tvsave = (TextView) findViewById(R.id.tvsave);
		// ListView
		XlvShopList = (ListView) findViewById(R.id.XlvShopList);
		mylistAdapter = new MyListAdapter(SelectShopActivity.this);
		XlvShopList.setAdapter(mylistAdapter);
		XlvShopList.setOnItemClickListener(this);
	}

	@Override
	public void fillData() {
		super.fillData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.llbarback:

			SelectShopActivity.this.finish();
			break;
		case R.id.btncreateshop:
			startActivity(new Intent(SelectShopActivity.this,
					CreateShopActivity.class));
			break;
		case R.id.btnRight:
			if (SharedPrefUtil.checkLogin(SelectShopActivity.this)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SelectShopActivity.this);
				builder.setTitle("退出登录")
						.setMessage("您确定要退出吗！")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										new LoadDeleteTokenTask(
												SharedPrefUtil
														.getToken(SelectShopActivity.this))
												.execute();
										SharedPrefUtil
												.clearUserBean(SelectShopActivity.this);
										SharedPrefUtil
												.clearShopBean(SelectShopActivity.this);
										SharedPrefUtil
												.clearuserid(SelectShopActivity.this);
										SharedPrefUtil
												.cleardeviceToken(SelectShopActivity.this);
										// XGPushManager
										// .unregisterPush(getApplicationContext());
										Toast.makeText(SelectShopActivity.this,
												"帐号已退出，店铺信息已清除。",
												Toast.LENGTH_LONG).show();
										finish();
									}
								})
						.setNegativeButton("否",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel(); // 删除对话框
									}
								});
				AlertDialog alert = builder.create();// 创建对话框
				alert.show();// 显示对话框

			}
			break;

		case R.id.llAddShop:
			if (canCreate.equals("0")) {
				Toast.makeText(SelectShopActivity.this,
						"您还有商铺未通过审核，审核完成才可创建新店铺", Toast.LENGTH_SHORT).show();
			} else {
				Intent createshopintent = new Intent(SelectShopActivity.this,
						CreateShopActivity.class);
				startActivityForResult(createshopintent, CREATESHOP);

			}

			break;

		default:
			break;

		}
	}

	private static final int CREATESHOP = 10022;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == CREATESHOP) {
			new LoadShopListTask(
					SharedPrefUtil.getToken(SelectShopActivity.this)).execute();
		}
	}

	/**
	 * 店铺列表适配器(XListView)
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;
		ShopBean shopBean;

		public MyListAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return shopList.size();
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
						R.layout.shop_select_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvshopselectname = (TextView) convertView
						.findViewById(R.id.tvshopselectname);
				viewHolder.tvdetail = (TextView) convertView
						.findViewById(R.id.tvdetail);
				viewHolder.tvstategreen = (TextView) convertView
						.findViewById(R.id.tvstategreen);
				viewHolder.tvstatered = (TextView) convertView
						.findViewById(R.id.tvstatered);
				viewHolder.ivshopselecthead = (ImageView) convertView
						.findViewById(R.id.ivshopselecthead);
				viewHolder.ivgren = (ImageView) convertView
						.findViewById(R.id.ivgren);
				viewHolder.ivred = (ImageView) convertView
						.findViewById(R.id.ivred);
				viewHolder.tvshopdetail = (TextView) convertView
						.findViewById(R.id.tvshopdetail);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			shopBean = shopList.get(position);
			switch (Integer.parseInt(shopBean.getState())) {
			case 0:
				viewHolder.tvstategreen.setVisibility(View.GONE);
				viewHolder.tvstatered.setVisibility(View.VISIBLE);
				viewHolder.tvstatered.setText("未提交");
				viewHolder.ivred.setVisibility(View.VISIBLE);
				viewHolder.ivgren.setVisibility(View.GONE);
				viewHolder.tvshopdetail.setText("请添加小区");
				viewHolder.tvshopdetail.setTextColor(getResources().getColor(
						R.color.color_red_two));
				break;

			case 1:
				viewHolder.tvstategreen.setVisibility(View.VISIBLE);
				viewHolder.tvstatered.setVisibility(View.GONE);
				viewHolder.tvstategreen.setText("已审核");
				viewHolder.ivgren.setVisibility(View.VISIBLE);
				viewHolder.ivred.setVisibility(View.GONE);

				viewHolder.tvshopdetail.setText("资料完善度"
						+ shopBean.getIntegrity() + "%");
				viewHolder.tvshopdetail.setTextColor(getResources().getColor(
						R.color.color_grey2));
				break;
			case 2:
				viewHolder.tvstategreen.setVisibility(View.GONE);
				viewHolder.tvstatered.setVisibility(View.VISIBLE);
				viewHolder.tvstatered.setText("审核中");
				viewHolder.ivgren.setVisibility(View.GONE);
				viewHolder.ivred.setVisibility(View.VISIBLE);
				viewHolder.tvshopdetail.setText("小区审核中");
				viewHolder.tvshopdetail.setTextColor(getResources().getColor(
						R.color.color_red_two));
				break;
			case 3:
				viewHolder.tvstategreen.setVisibility(View.GONE);
				viewHolder.tvstatered.setVisibility(View.VISIBLE);
				viewHolder.tvstatered.setText("暂停营业");
				break;
			case 4:
				viewHolder.tvstategreen.setVisibility(View.GONE);
				viewHolder.tvstatered.setVisibility(View.VISIBLE);
				viewHolder.tvstatered.setText("已关闭");
				break;
			case -1:
				viewHolder.tvstategreen.setVisibility(View.GONE);
				viewHolder.tvstatered.setVisibility(View.VISIBLE);
				viewHolder.tvstatered.setText("已删除");
				break;

			default:
				break;
			}
			viewHolder.tvdetail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent shopdetail = new Intent(SelectShopActivity.this,
							ShopDetailActivity.class);
					shopdetail.putExtra("shopid", shopList.get(position)
							.getId());
					startActivity(shopdetail);
				}
			});
			viewHolder.tvshopselectname.setText(shopBean.getName());
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(shopBean.getImg(),
							viewHolder.ivshopselecthead);
			return convertView;
		}
	}

	/**
	 * 店铺列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadShopListTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;

		// private int begin;
		protected LoadShopListTask(String accessToken) {
			this.accessToken = accessToken;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopHelper().ShopSearch(accessToken);
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
					canCreate = result.getString("isCreate");
					if (canCreate.equals("0")) {
						tvsave.setText("您创建的店铺正在审核中");
					}
					if (result.getInt("status") == Constants.SUCCESS) {

						List<ShopBean> temp = ShopBean.constractList(result
								.getJSONArray("shops"));
						shopList.clear();
						shopList.addAll(temp);
						progressbar.setVisibility(View.GONE);
						llisshop.setVisibility(View.VISIBLE);
						llnoshop.setVisibility(View.GONE);
						mylistAdapter.notifyDataSetChanged();
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						llnoshop.setVisibility(View.VISIBLE);
						llisshop.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(SelectShopActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("SelectShopActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(SelectShopActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("SelectShopActivity", "SystemException");
				}
			} else {
				Toast.makeText(SelectShopActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("SelectShopActivity", "result==null");
			}
		}
	}

	/**
	 * 删除信鸽token
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadDeleteTokenTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;

		protected LoadDeleteTokenTask(String accessToken) {
			this.accessToken = accessToken;

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new UserHelper().deleteToken(accessToken);
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

					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		SharedPrefUtil.setShopBean(SelectShopActivity.this,
				shopList.get(position));
		SharedPrefUtil.setVip(SelectShopActivity.this, shopList.get(position)
				.getVip().toString());

		Toast.makeText(SelectShopActivity.this, "店铺切换成功！", Toast.LENGTH_SHORT)
				.show();
		/*if (SharedPrefUtil.isFistSearch(SelectShopActivity.this)) {
			startActivity(new Intent(SelectShopActivity.this,
					SearchXiaoQuActivity.class));
			finish();
		} else {
			finish();
		}*/
		//第一次登陆选择店铺后直接跳到首页
		finish();
	}

}
