package com.bangqu.yinwan.shop.ui;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.FinanceBean;
import com.bangqu.yinwan.shop.bean.ShopBean;
import com.bangqu.yinwan.shop.bean.UserBean;
import com.bangqu.yinwan.shop.helper.ShopHelper;
import com.bangqu.yinwan.shop.helper.UserHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

public class HomeActivity extends UIBaseActivity implements OnClickListener {

	// 顶部title
	private Button btnRight1;
	private TextView tvTittle1;
	private TextView tvLeft1;
	private ShopBean shopBean;
	private UserBean userBean;
	private FinanceBean financeBean;

	private LinearLayout llOrderManage;
	private LinearLayout llProductManage;
	private LinearLayout llPromotionManage;
	private LinearLayout llGroupManage;
	private LinearLayout llOwnerManage;
	private LinearLayout lltop;
	private LinearLayout lltopno;

	private LinearLayout llMore;
	private TextView tvname;
	private TextView tvid;
	private ImageView ivhead;
	private String id;
	private TextView tvzhanghu;
	private TextView tvdongjie;
	private TextView tvyingye;
	String strzhanghu;

	private Button btlogin;
	private Button btregist;

	// 营业额
	private LinearLayout lldaylist;
	private LinearLayout llzhanghu;
	private LinearLayout lldongjie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		Constants.PUSH = false;
		SharedPrefUtil.setFistLogined(this);
		// 如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		// 如果您同时使用了手动更新和自动检查更新，为了避免更新回调被多次调用，请加上下面这句代码
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.update(this);

		// 强制更新
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

			@Override
			public void onClick(int status) {
				switch (status) {
				case UpdateStatus.Update:
					break;
				default:
					finish();
				}
			}
		});

		/**
		 * 开启logcat输出，方便debug，发布时请关闭
		 * 
		 */
		findView();

		if (Constants.NOTIFICATION) {
			startActivity(new Intent(HomeActivity.this,
					HomeOrderManageActivity.class));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!Constants.PUSH) {
			XGPushManager.registerPush(getApplicationContext(),
					new XGIOperateCallback() {

						@Override
						public void onSuccess(Object arg0, int arg1) {
							System.out.println(arg0 + "注册成功" + arg1);
							SharedPrefUtil.setdeviceToken(HomeActivity.this,
									arg0.toString());
							Constants.PUSH = true;
							if (!StringUtil.isBlank(SharedPrefUtil
									.getzhanghao(HomeActivity.this))
									&& !StringUtil.isBlank(SharedPrefUtil
											.getpasswd(HomeActivity.this))) {
								new LoadUserLoginTask(
										SharedPrefUtil
												.getzhanghao(HomeActivity.this),
										SharedPrefUtil
												.getpasswd(HomeActivity.this),
										SharedPrefUtil
												.getdeviceToken(HomeActivity.this))
										.execute();
							}

						}

						@Override
						public void onFail(Object arg0, int arg1, String arg2) {
							System.out.println(arg0 + "注册失败" + arg1 + "=="
									+ arg2);
							Constants.PUSH = false;
						}
					});
		}
		if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
			btnRight1.setText("切换");
			tvname.setText("未选择店铺");
			lltop.setVisibility(View.VISIBLE);
			lltopno.setVisibility(View.GONE);
			new LoadUserzhangTask(SharedPrefUtil.getToken(HomeActivity.this),
					SharedPrefUtil.getShopBean(HomeActivity.this).getId())
					.execute();

			if (SharedPrefUtil.checkShop(HomeActivity.this)) {

				tvzhanghu.setVisibility(View.VISIBLE);
				tvdongjie.setVisibility(View.VISIBLE);
				tvyingye.setVisibility(View.VISIBLE);
				btnRight1.setVisibility(View.VISIBLE);
				btnRight1.setText("切换");
				tvname.setText(SharedPrefUtil.getShopBean(HomeActivity.this)
						.getName());

				new LoadShopViewTask(SharedPrefUtil.getShopBean(
						HomeActivity.this).getId()).execute();
				new LoadUserzhangTask(
						SharedPrefUtil.getToken(HomeActivity.this),
						SharedPrefUtil.getShopBean(HomeActivity.this).getId())
						.execute();
				System.out.println(SharedPrefUtil.getToken(HomeActivity.this));
			} else {
				tvzhanghu.setVisibility(View.GONE);
				tvdongjie.setVisibility(View.GONE);
				tvyingye.setVisibility(View.GONE);
				btnRight1.setText("切换");
				btnRight1.setVisibility(View.VISIBLE);
				tvname.setText("未选择店铺");
				tvid.setText("店铺信息");
			}
		} else {
			btnRight1.setVisibility(View.GONE);
			tvname.setText("未登录");
			lltop.setVisibility(View.GONE);
			lltopno.setVisibility(View.VISIBLE);
		}
	}

	public void findView() {
		super.findView();

		lldongjie = (LinearLayout) findViewById(R.id.lldongjie);
		lldongjie.setOnClickListener(this);
		llzhanghu = (LinearLayout) findViewById(R.id.llzhanghu);
		llzhanghu.setOnClickListener(this);

		tvTittle1 = (TextView) findViewById(R.id.tvTittle1);
		tvTittle1.setText("银湾社区生活网商户版");

		btnRight1 = (Button) findViewById(R.id.btnRight1);
		// btnRight1.setText("切换");
		if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
			btnRight1.setText("切换");
		}
		btnRight1.setOnClickListener(this);

		tvLeft1 = (TextView) findViewById(R.id.tvLeft1);

		llOrderManage = (LinearLayout) findViewById(R.id.llOrderManage);
		llProductManage = (LinearLayout) findViewById(R.id.llProductManage);
		llPromotionManage = (LinearLayout) findViewById(R.id.llPromotionManage);
		llGroupManage = (LinearLayout) findViewById(R.id.llGroupManage);
		llOwnerManage = (LinearLayout) findViewById(R.id.llOwnerManage);
		llMore = (LinearLayout) findViewById(R.id.llMore);
		lltop = (LinearLayout) findViewById(R.id.lltop);
		lltopno = (LinearLayout) findViewById(R.id.lltopno);

		llOrderManage.setOnClickListener(this);
		llProductManage.setOnClickListener(this);
		llPromotionManage.setOnClickListener(this);
		llGroupManage.setOnClickListener(this);
		llOwnerManage.setOnClickListener(this);
		llMore.setOnClickListener(this);

		ivhead = (ImageView) findViewById(R.id.ivhead);
		ivhead.setOnClickListener(this);
		tvname = (TextView) findViewById(R.id.tvname);
		tvid = (TextView) findViewById(R.id.tvid);
		tvzhanghu = (TextView) findViewById(R.id.tvzhanghu);
		tvdongjie = (TextView) findViewById(R.id.tvdongjie);
		tvdongjie.setOnClickListener(this);
		tvyingye = (TextView) findViewById(R.id.tvyingye);

		btlogin = (Button) findViewById(R.id.btlogin);
		btlogin.setOnClickListener(this);
		btregist = (Button) findViewById(R.id.btregist);
		btregist.setOnClickListener(this);
		lldaylist = (LinearLayout) findViewById(R.id.lldaylist);
		lldaylist.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.lldongjie:
			// startActivity(new Intent(HomeActivity.this,
			// AssessActivity.class));

			break;
		case R.id.lldaylist:

			if (SharedPrefUtil.checkShop(HomeActivity.this)) {
				Intent daylist = new Intent(HomeActivity.this,
						DayListActivity.class);
				daylist.putExtra("yingye", tvyingye.getText().toString().trim());
				startActivity(daylist);
			} else {
				startActivity(new Intent(HomeActivity.this,
						SelectShopActivity.class));
			}

			break;
		case R.id.llzhanghu:
			if (SharedPrefUtil.checkShop(HomeActivity.this)) {
				startActivity(new Intent(HomeActivity.this,
						CenterofAccountActivity.class));
			} else {
				startActivity(new Intent(HomeActivity.this,
						SelectShopActivity.class));
			}

			break;
		case R.id.ivhead:
			if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeActivity.this)) {
					startActivity(new Intent(HomeActivity.this,
							ShopInfomationActivity.class));
				}
			}
			break;

		case R.id.btnRight1:
			if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeActivity.this)) {
					Intent selectintent = new Intent(this,
							SelectShopActivity.class);
					selectintent.putExtra("init", "0");
					startActivity(selectintent);

				} else {
					startActivity(new Intent(HomeActivity.this,
							SelectShopActivity.class));
					Toast.makeText(HomeActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				startActivity(new Intent(this, RegistActivity.class));
			}

			break;
		case R.id.llOrderManage:
			if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeActivity.this)) {
					Intent weichuliintent = new Intent(this,
							HomeOrderManageActivity.class);
					weichuliintent.putExtra("init", "0");
					startActivity(weichuliintent);
				} else {
					startActivity(new Intent(HomeActivity.this,
							SelectShopActivity.class));
					Toast.makeText(HomeActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.llProductManage:
			if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeActivity.this)) {
					Intent weichuliintent = new Intent(this,
							HomeProductManageActivity.class);
					weichuliintent.putExtra("init", "0");
					startActivity(weichuliintent);
				} else {
					startActivity(new Intent(HomeActivity.this,
							SelectShopActivity.class));
					Toast.makeText(HomeActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.llPromotionManage:
			if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeActivity.this)) {
					Intent weichuliintent = new Intent(this,
							PromotionManageActivity.class);
					weichuliintent.putExtra("init", "0");
					// weichuliintent.putExtra("init", 0);
					startActivity(weichuliintent);
				} else {
					startActivity(new Intent(HomeActivity.this,
							SelectShopActivity.class));
					Toast.makeText(HomeActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.llGroupManage:
			if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeActivity.this)) {
					Intent weichuliintent = new Intent(this,
							GrouponManageActivity.class);
					weichuliintent.putExtra("init", "0");
					startActivity(weichuliintent);
				} else {
					startActivity(new Intent(HomeActivity.this,
							SelectShopActivity.class));
					Toast.makeText(HomeActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.llOwnerManage:
			if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeActivity.this)) {
					Intent weichuliintent = new Intent(this,
							OwnerListActivity.class);
					weichuliintent.putExtra("init", "0");
					startActivity(weichuliintent);
				} else {
					startActivity(new Intent(HomeActivity.this,
							SelectShopActivity.class));
					Toast.makeText(HomeActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.llMore:
			if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeActivity.this)) {
					Intent weichuliintent = new Intent(this,
							HomeMoreActivity.class);
					weichuliintent.putExtra("init", "0");
					startActivity(weichuliintent);
				} else {
					startActivity(new Intent(HomeActivity.this,
							SelectShopActivity.class));
					Toast.makeText(HomeActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				startActivity(new Intent(HomeActivity.this, LoginActivity.class));
			}
			break;
		case R.id.btlogin:
			Intent intentlogin = new Intent(HomeActivity.this,
					LoginActivity.class);
			startActivity(intentlogin);
			// finish();
			break;
		case R.id.btregist:
			Intent intentregist = new Intent(HomeActivity.this,
					RegistActivity.class);
			startActivity(intentregist);
			break;

		default:
			break;
		}
	}

	public void fillData() {
		super.fillData();
		if (SharedPrefUtil.checkLogin(HomeActivity.this)) {
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(shopBean.getImg(), ivhead);
		}

	}

	/**
	 * 获取用户信息
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadUserInfoTask extends AsyncTask<String, Void, JSONObject> {

		private String username;
		private String password;
		private String deviceToken;

		protected LoadUserInfoTask(String username, String password,
				String deviceToken) {
			this.username = username;
			this.password = password;
			this.deviceToken = deviceToken;

		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new UserHelper().userlogin(username, password,
						deviceToken);
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
						userBean = JSON.parseObject(result
								.getJSONObject("user").toString(),
								UserBean.class);

						// tvname.setText(userBean.getNickname());
						// tvname.setText(userBean.getUsername());

						// fillData();
					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
			}

		}
	}

	/**
	 * 预览店铺信息
	 * 
	 */
	class LoadShopViewTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadShopViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopHelper().ShopView(id);
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

						shopBean = JSON.parseObject(result
								.getJSONObject("shop").toString(),
								ShopBean.class);
						if ((shopBean.getVip() + "").equals("true")) {
							tvid.setText("会员店铺");
						} else {
							tvid.setText("普通店铺");
						}

						fillData();
					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取商户金额
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadUserzhangTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String shopId;

		protected LoadUserzhangTask(String accessToken, String shopId) {
			this.accessToken = accessToken;
			this.shopId = shopId;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new UserHelper().userzhanghu(accessToken, shopId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pd != null)
				pd.dismiss();
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						financeBean = JSON.parseObject(
								result.getJSONObject("finance").toString(),
								FinanceBean.class);
						if (financeBean.getBalance().equals("0")) {
							tvzhanghu.setText("0元");
						} else {
							DecimalFormat df = new DecimalFormat("0.00");
							df.format(Double.parseDouble(financeBean
									.getBalance()));
							tvzhanghu.setText(df.format(Double
									.parseDouble(financeBean.getBalance()))
									+ "元");
						}
						if (financeBean.getFrozen().equals("0")) {
							tvdongjie.setText("0元");
						} else {
							DecimalFormat df = new DecimalFormat("0.00");
							df.format(Double.parseDouble(financeBean
									.getFrozen()));
							tvdongjie.setText(df.format(Double
									.parseDouble(financeBean.getFrozen()))
									+ "元");
						}
						if (financeBean.getTurnover().equals("0")) {
							tvyingye.setText("0元");
						} else {
							DecimalFormat df = new DecimalFormat("0.00");
							df.format(Double.parseDouble(financeBean
									.getTurnover()));
							tvyingye.setText(df.format(Double
									.parseDouble(financeBean.getTurnover()))
									+ "元");
						}

						// tvzhanghu.setText(financeBean.getBalance());
						// fillData();
					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
			}

		}
	}

	/**
	 * 用户登录
	 * 
	 */
	class LoadUserLoginTask extends AsyncTask<String, Void, JSONObject> {

		private String username;
		private String password;
		private String deviceToken;

		protected LoadUserLoginTask(String username, String password,
				String deviceToken) {
			this.username = username;
			this.password = password;
			this.deviceToken = deviceToken;

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new UserHelper().userlogin(username, password,
						deviceToken);
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
						JSONObject json = result.getJSONObject("user");
						UserBean userBean = JSON.parseObject(json.toString(),
								UserBean.class);

						String accessToken = result
								.getJSONObject("accessToken").getString(
										"accessToken");
						SharedPrefUtil.setUserID(HomeActivity.this,
								userBean.getId());
						SharedPrefUtil.setToken(HomeActivity.this, accessToken);
						SharedPrefUtil.setUserBean(HomeActivity.this, userBean);
						System.out.println("二次登陆");

					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private long mExitTime;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
