package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.UserBean;
import com.bangqu.yinwan.shop.helper.UserHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class HomeMoreActivity extends UIBaseActivity implements OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;
	// 版本号
	private TextView tvAppVersion;

	// 具体N个选项卡
	private RelativeLayout rlchangeshop;
	private RelativeLayout rlshop;
	private RelativeLayout rlxiaoquManage;
	private RelativeLayout rlfenlei;
	private RelativeLayout rlintro;
	private RelativeLayout rlhelp;
	private RelativeLayout rlfeedback;
	private RelativeLayout rlupdate;
	private RelativeLayout rlshare;
	// 退出登录
	private Button btnExitLogin;
	private UserBean userBean;

	// 友盟意见反馈
	// FeedbackAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_more_layout);

		findView();
		fillData();
	}

	public void fillData() {

	}

	public void findView() {
		super.findView();
		// 自动更新版本号
		tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);

			tvAppVersion.setText("当前版本 " + packageInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("更多");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);

		rlchangeshop = (RelativeLayout) findViewById(R.id.rlchangeshop);
		rlshop = (RelativeLayout) findViewById(R.id.rlshop);
		rlxiaoquManage = (RelativeLayout) findViewById(R.id.rlxiaoquManage);
		rlfenlei = (RelativeLayout) findViewById(R.id.rlfenlei);
		rlintro = (RelativeLayout) findViewById(R.id.rlintro);
		rlhelp = (RelativeLayout) findViewById(R.id.rlhelp);
		rlfeedback = (RelativeLayout) findViewById(R.id.rlfeedback);
		rlupdate = (RelativeLayout) findViewById(R.id.rlupdate);
		rlshare = (RelativeLayout) findViewById(R.id.rlshare);

		rlchangeshop.setOnClickListener(this);
		rlshop.setOnClickListener(this);
		rlxiaoquManage.setOnClickListener(this);
		rlfenlei.setOnClickListener(this);
		rlintro.setOnClickListener(this);
		rlhelp.setOnClickListener(this);
		rlfeedback.setOnClickListener(this);
		rlupdate.setOnClickListener(this);
		rlshare.setOnClickListener(this);

		// 退出登录
		btnExitLogin = (Button) findViewById(R.id.btnExitLogin);
		btnExitLogin.setOnClickListener(this);

		// 友盟意见反馈
		// agent = new FeedbackAgent(this);
		// agent.sync();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (SharedPrefUtil.checkLogin(HomeMoreActivity.this)) {
			btnExitLogin.setText("退出登录");
		} else {
			btnExitLogin.setText("我要登录");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.rlchangeshop:
			if (SharedPrefUtil.checkLogin(HomeMoreActivity.this)) {
				Intent intentchange = new Intent(HomeMoreActivity.this,
						SelectShopActivity.class);
				startActivity(intentchange);
			} else {
				startActivity(new Intent(HomeMoreActivity.this,
						LoginActivity.class));
			}

			break;
		case R.id.rlshop:
			if (SharedPrefUtil.checkLogin(HomeMoreActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeMoreActivity.this)) {
					Intent intentshop = new Intent(HomeMoreActivity.this,
							ShopInfomationActivity.class);
					startActivity(intentshop);
				} else {
					startActivity(new Intent(HomeMoreActivity.this,
							SelectShopActivity.class));
				}
			} else {
				startActivity(new Intent(HomeMoreActivity.this,
						LoginActivity.class));
			}

			break;
		case R.id.rlxiaoquManage:
			if (SharedPrefUtil.checkLogin(HomeMoreActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeMoreActivity.this)) {
					Intent search = new Intent(HomeMoreActivity.this,
							SearchXiaoQuActivity.class);
					search.putExtra("init", "0");
					startActivity(search);
				} else {
					startActivity(new Intent(HomeMoreActivity.this,
							SelectShopActivity.class));
				}

			} else {
				startActivity(new Intent(HomeMoreActivity.this,
						LoginActivity.class));
			}

			break;

		case R.id.rlfenlei:
			if (SharedPrefUtil.checkLogin(HomeMoreActivity.this)) {
				if (SharedPrefUtil.checkShop(HomeMoreActivity.this)) {
					SharedPrefUtil.setFistcategorylist(HomeMoreActivity.this);
					Intent intentfenlei = new Intent(HomeMoreActivity.this,
							HomeFenLeiActivity.class);
					startActivity(intentfenlei);
				} else {
					startActivity(new Intent(HomeMoreActivity.this,
							SelectShopActivity.class));
				}
			} else {
				startActivity(new Intent(HomeMoreActivity.this,
						LoginActivity.class));
			}

			break;
		case R.id.rlintro:
			Intent welcomintent = new Intent(HomeMoreActivity.this,
					WelcomeActivity.class);
			startActivity(welcomintent);
			break;
		case R.id.rlhelp:
			Intent urlintent = new Intent(HomeMoreActivity.this,
					UrlWebView.class);
			urlintent.putExtra("url", "http://api.yinwan.bangqu.com/shop/help");
			urlintent.putExtra("title", "使用帮助");
			startActivity(urlintent);
			break;

		case R.id.rlfeedback:
			if (SharedPrefUtil.checkLogin(HomeMoreActivity.this)) {
				Intent feedbackintent = new Intent(HomeMoreActivity.this,
						UrlWebView.class);
				feedbackintent.putExtra(
						"url",
						"http://api.yinwan.bangqu.com/feedback?username="
								+ SharedPrefUtil.getUserBean(
										HomeMoreActivity.this).getUsername());
				feedbackintent.putExtra("title", "意见反馈");
				startActivity(feedbackintent);
			} else {
				Intent feedbackintent = new Intent(HomeMoreActivity.this,
						UrlWebView.class);
				feedbackintent.putExtra("url",
						"http://api.yinwan.bangqu.com/feedback");
				feedbackintent.putExtra("title", "意见反馈");
				startActivity(feedbackintent);
			}
			break;
		case R.id.rlupdate:
			UmengUpdateAgent.update(this);
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus,
						UpdateResponse updateInfo) {
					switch (updateStatus) {
					case 0: // has update
						UmengUpdateAgent.showUpdateDialog(
								HomeMoreActivity.this, updateInfo);
						break;
					case 1: // has no update
						Toast.makeText(HomeMoreActivity.this, "当前为最新版本",
								Toast.LENGTH_SHORT).show();
						break;
					// case 2: // none wifi
					// Toast.makeText(HomeMoreActivity.this,
					// "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT)
					// .show();
					// break;
					case 3: // time out
						Toast.makeText(HomeMoreActivity.this, "超时",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			});
			break;

		case R.id.rlshare:
			shareText(
					HomeMoreActivity.this,
					"分享",
					"由物业公司运营的小区购物平台，让我们的店铺通过银湾社区生活网直达小区业主。提升我们店铺的销售额。下载银湾商户版，http://m.yinwan.com/download-sh.jsp");
			break;

		case R.id.btnRight:
			Intent intentSearch = new Intent(HomeMoreActivity.this,
					HomeMoreShopActivity.class);
			startActivity(intentSearch);
			break;
		case R.id.btnExitLogin:
			if (SharedPrefUtil.checkLogin(this)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						HomeMoreActivity.this);
				builder.setTitle("退出登录")
						.setMessage("您确定要退出吗！")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										new LoadDeleteTokenTask(
												SharedPrefUtil
														.getToken(HomeMoreActivity.this))
												.execute();
										SharedPrefUtil
												.clearUserBean(HomeMoreActivity.this);
										SharedPrefUtil
												.clearShopBean(HomeMoreActivity.this);
										SharedPrefUtil
												.clearvip(HomeMoreActivity.this);
										SharedPrefUtil
												.cleardeviceToken(HomeMoreActivity.this);

										SharedPrefUtil
												.clearuserid(HomeMoreActivity.this);
										btnExitLogin.setText("我要登录");
										Toast.makeText(HomeMoreActivity.this,
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
			} else {
				// Toast.makeText(HomeMoreActivity.this, "您还未登录",
				// Toast.LENGTH_LONG).show();
				Intent intentLogin = new Intent(HomeMoreActivity.this,
						LoginActivity.class);
				startActivity(intentLogin);
			}
			break;

		default:
			break;
		}
	}

	public static void shareText(Context context, String title, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		context.startActivity(Intent.createChooser(intent, title));

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
}
