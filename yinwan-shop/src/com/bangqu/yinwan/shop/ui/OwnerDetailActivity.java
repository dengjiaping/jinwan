package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXTextObject;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.OwnerBean;
import com.bangqu.yinwan.shop.helper.OwnerHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

public class OwnerDetailActivity extends UIBaseActivity implements
		OnClickListener, IWXAPIEventHandler {
	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;

	private String id;

	private TextView tvOwnerName;
	private TextView tvOwnerMobile;
	private TextView tvOrderAddr;
	private TextView tvWuye;
	private TextView tvlocationName;
	private TextView tvAddTime;
	private TextView tvUpdateTime;
	private TextView tvMonthPrice;
	private TextView tvPrice;
	private ImageView ivCall;
	private ImageView ivMessage;
	private ImageView ivWeixin;
	private Button btnViewAll;
	private OwnerBean ownerBean;

	// APP_ID替换为你的应用从官方网站申请的合法appid
	// private static final String APP_ID = "wxeb5bc69c320723d8";
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	// // APP_ID
	// private void regToWx() {
	// // 通过WXAPIFactory工厂，获取IWXAPI的实例
	// api = WXAPIFactory.createWXAPI(this, APP_ID, true);
	// // 将应用的appid注册到微信
	// api.registerApp(APP_ID);
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ownerdetail_layout);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
		// api.registerApp(Constants.APP_ID);

		findView();
		id = getIntent().getStringExtra("OwnerId");
		new LoadOwnerViewTask(id).execute();

	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		super.findView();

		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("客户信息");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);

		tvOwnerName = (TextView) findViewById(R.id.tvOwnerName);
		tvOwnerMobile = (TextView) findViewById(R.id.tvOwnerMobile);
		tvOrderAddr = (TextView) findViewById(R.id.tvOwnerAddr);
		tvWuye = (TextView) findViewById(R.id.tvWuye);
		tvlocationName = (TextView) findViewById(R.id.tvlocationName);
		tvAddTime = (TextView) findViewById(R.id.tvAddTime);
		tvUpdateTime = (TextView) findViewById(R.id.tvUpdateTime);
		tvMonthPrice = (TextView) findViewById(R.id.tvMonthPrice);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		ivCall = (ImageView) findViewById(R.id.ivCall);
		ivCall.setOnClickListener(this);
		ivMessage = (ImageView) findViewById(R.id.ivMessage);
		ivMessage.setOnClickListener(this);
		ivWeixin = (ImageView) findViewById(R.id.ivWeixin);
		ivWeixin.setOnClickListener(this);
		btnViewAll = (Button) findViewById(R.id.btnViewAll);
		btnViewAll.setOnClickListener(this);
	}

	@Override
	public void fillData() {
		// TODO Auto-generated method stub
		super.fillData();
		tvOwnerName.setText(ownerBean.getName());
		tvOwnerMobile.setText(ownerBean.getMobile());
		tvOrderAddr.setText(ownerBean.getAddress());
		tvlocationName.setText(ownerBean.getLocationName());
		tvWuye.setText(ownerBean.getCompanyName());
		tvAddTime.setText(ownerBean.getAddTime());
		tvUpdateTime.setText(ownerBean.getUpdateTime());
		tvMonthPrice.setText(ownerBean.getMonthPrice() + "元");
		tvPrice.setText(ownerBean.getPrice() + "元");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnViewAll:
			SharedPrefUtil.setOwnerID(OwnerDetailActivity.this,
					ownerBean.getId());
			Intent allintent = new Intent(OwnerDetailActivity.this,
					OwnerDingDanActivity.class);
			allintent.putExtra("init", "0");
			startActivity(allintent);
			break;
		case R.id.ivMessage:
			Intent Messageintent = new Intent(Intent.ACTION_SENDTO,
					Uri.parse("smsto:" + ownerBean.getMobile()));
			Messageintent.putExtra("sms_body", "");
			// Messageintent.setType("vnd.adnroid-dir/mms-sms");
			startActivity(Messageintent);
			break;
		case R.id.ivCall:
			Intent Callintent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ tvOwnerMobile.getText()));
			Callintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Callintent);
			break;

		case R.id.ivWeixin:
			api.registerApp(Constants.APP_ID);
			api.openWXApp();
			// System.out.println(api.openWXApp());
			// Toast.makeText(OwnerDetailActivity.this, "launch result = " +
			// api.openWXApp(),
			// Toast.LENGTH_LONG).show();
			api.handleIntent(getIntent(), this);
			break;

		default:
			break;
		}
	}

	/**
	 * 业主详细信息
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadOwnerViewTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadOwnerViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new OwnerHelper().OwnerView(id);
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
						ownerBean = JSON.parseObject(
								result.getJSONObject("owner").toString(),
								OwnerBean.class);
						fillData();
						progressbar.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
						Toast.makeText(OwnerDetailActivity.this,
								result.getInt("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(OwnerDetailActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(OwnerDetailActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}

		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			// goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			// goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

}
