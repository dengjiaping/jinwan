package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.BankBean;
import com.bangqu.yinwan.shop.helper.ShopFinanceHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;

public class AccountActivity extends UIBaseActivity implements OnClickListener {
	private TextView tvBankName;
	private TextView tvBankNumber;
	private TextView tvUserName;
	private ImageView ivBankHead;
	private ImageView ivBank;
	private ImageView ivBanktwo;
	private ImageView ivalipy;
	private ImageView ivalipytwo;
	private TextView tvbankcard;

	private LinearLayout llAddbank;// 添加银行卡
	private Button btAddBank;
	private View vicard;
	private Button btnbankedit;// 银行卡编辑
	private Button btneditallipay;// 编辑支付宝
	private Button btAddAlipay;
	private LinearLayout llAddAlipay;
	private LinearLayout llalipay;// 支付宝
	private LinearLayout llAlipayInfo;// 支付宝卡信息
	private LinearLayout llbank;// 银行卡信息
	private BankBean bankBean;
	// 支付宝信息
	private TextView tvAlipayNum;
	private TextView tvAlipayUser;

	// 标题栏
	private TextView tvTittle;
	private Button btnLeft;
	private Button btnRightic;

	private Button btAccount;
	private EditText etInPut;
	private String strway = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_out_layout);
		findView();
		new LoadBankCardTask(SharedPrefUtil.getToken(AccountActivity.this))
				.execute();
	}

	@Override
	public void findView() {
		super.findView();
		btAddAlipay = (Button) findViewById(R.id.btAddAlipay);
		btAddAlipay.setOnClickListener(this);
		btAddBank = (Button) findViewById(R.id.btAddBank);
		btAddBank.setOnClickListener(this);
		vicard = (View) findViewById(R.id.vicard);
		ivBank = (ImageView) findViewById(R.id.ivBank);
		ivBanktwo = (ImageView) findViewById(R.id.ivBanktwo);
		ivalipytwo = (ImageView) findViewById(R.id.ivalipytwo);
		ivalipy = (ImageView) findViewById(R.id.ivalipy);
		ivBankHead = (ImageView) findViewById(R.id.ivBankHead);
		tvBankName = (TextView) findViewById(R.id.tvBankName);
		tvBankNumber = (TextView) findViewById(R.id.tvBankNumber);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		llAddbank = (LinearLayout) findViewById(R.id.llAddbank);
		llAddbank.setOnClickListener(this);
		llbank = (LinearLayout) findViewById(R.id.llbank);
		llbank.setOnClickListener(this);

		tvbankcard = (TextView) findViewById(R.id.tvbankcard);
		btnbankedit = (Button) findViewById(R.id.btnbankedit);
		btnbankedit.setOnClickListener(this);

		tvAlipayNum = (TextView) findViewById(R.id.tvAlipayNum);
		tvAlipayUser = (TextView) findViewById(R.id.tvAlipayUser);
		llAlipayInfo = (LinearLayout) findViewById(R.id.llAlipayInfo);
		llAlipayInfo.setOnClickListener(this);

		btneditallipay = (Button) findViewById(R.id.btneditallipay);
		btneditallipay.setOnClickListener(this);
		llalipay = (LinearLayout) findViewById(R.id.llalipay);

		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("账户提现");
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		btnRightic = (Button) findViewById(R.id.btnRightic);
		btnRightic.setOnClickListener(this);

		btAccount = (Button) findViewById(R.id.btAccount);
		btAccount.setOnClickListener(this);
		etInPut = (EditText) findViewById(R.id.etInPut);

		llAddAlipay = (LinearLayout) findViewById(R.id.llAddAlipay);

	}

	@Override
	protected void onResume() {
		super.onResume();
		new LoadBankCardTask(SharedPrefUtil.getToken(AccountActivity.this))
				.execute();
		if (!StringUtil.isBlank(SharedPrefUtil.getAlipay(AccountActivity.this))) {
			tvAlipayNum.setText(SharedPrefUtil.getAlipay(AccountActivity.this));
			if (!StringUtil.isBlank(SharedPrefUtil.getRealname(this))) {
				tvAlipayUser.setText(SharedPrefUtil.getRealname(this));
			}
			llalipay.setVisibility(View.VISIBLE);
			llAlipayInfo.setVisibility(View.VISIBLE);
			llAddAlipay.setVisibility(View.GONE);

		} else {
			llAlipayInfo.setVisibility(View.GONE);
			llalipay.setVisibility(View.GONE);
			llAddAlipay.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llbank:
			ivBank.setVisibility(View.VISIBLE);
			ivBanktwo.setVisibility(View.GONE);
			ivalipytwo.setVisibility(View.VISIBLE);
			ivalipy.setVisibility(View.GONE);
			strway = "银行卡";
			break;
		case R.id.llAlipayInfo:
			ivBank.setVisibility(View.GONE);
			ivBanktwo.setVisibility(View.VISIBLE);
			ivalipy.setVisibility(View.VISIBLE);
			ivalipytwo.setVisibility(View.GONE);
			strway = "支付宝";
			break;
		case R.id.btAddBank:
			startActivity(new Intent(AccountActivity.this,
					AddBankCardActivity.class));
			break;
		case R.id.btAddAlipay:
			startActivity(new Intent(AccountActivity.this,
					AddAlipayActivity.class));
			break;
		case R.id.btneditallipay:
			startActivity(new Intent(AccountActivity.this,
					AddAlipayActivity.class));
			break;
		case R.id.btnbankedit:
			startActivity(new Intent(AccountActivity.this,
					AddBankCardActivity.class));
			break;
		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnRightic:
			startActivity(new Intent(this, AccountListActivity.class));
			break;
		case R.id.btAccount:
			String strprice = etInPut.getText().toString().trim();
			if (StringUtil.isBlank(strprice)) {
				Toast.makeText(AccountActivity.this, "请输入提现金额",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (Double.parseDouble(strprice) <= 0) {
				Toast.makeText(AccountActivity.this, "提现金额不能为零",
						Toast.LENGTH_SHORT).show();
				return;
			}
			new LoadAccountTask(SharedPrefUtil.getToken(AccountActivity.this),
					strprice, SharedPrefUtil.getShopBean(AccountActivity.this)
							.getId(), strway).execute();
			break;
		default:
			break;
		}
	}

	@Override
	public void fillData() {
		super.fillData();
		((CommonApplication) getApplicationContext()).getImgLoader()
				.DisplayImage(bankBean.getBankIcon(), ivBankHead);
		tvBankName.setText(bankBean.getBankName());
		if (bankBean.getCardNo().length() >= 4) {
			tvBankNumber.setText(bankBean.getCardNo().substring(
					bankBean.getCardNo().length() - 4,
					bankBean.getCardNo().length()));
		} else {
			tvBankNumber.setText(bankBean.getCardNo());
		}

		tvUserName.setText(bankBean.getAccountName());
		llAddbank.setVisibility(View.GONE);
		vicard.setVisibility(View.GONE);
		tvbankcard.setText("银行卡");
	}

	/**
	 * 银行卡信息
	 */
	class LoadBankCardTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;

		protected LoadBankCardTask(String accessToken) {
			this.accessToken = accessToken;

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopFinanceHelper().bank(accessToken);
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

						bankBean = JSON.parseObject(result
								.getJSONObject("bank").toString(),
								BankBean.class);
						strway = "银行卡";
						ivBanktwo.setVisibility(View.GONE);
						ivBank.setVisibility(View.VISIBLE);
						ivalipy.setVisibility(View.GONE);
						ivalipytwo.setVisibility(View.VISIBLE);
						fillData();
						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						llbank.setVisibility(View.GONE);
						btnbankedit.setVisibility(View.GONE);
						llAddbank.setVisibility(View.VISIBLE);
						if (!StringUtil.isBlank(SharedPrefUtil
								.getAlipay(AccountActivity.this))) {
							ivalipy.setVisibility(View.VISIBLE);
							ivalipytwo.setVisibility(View.GONE);
						}
						progressbar.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("LoginActivity", "JSONException");
				}
			} else {
				Log.i("LoginActivity", "result==null");
			}

		}
	}

	/**
	 * 提现
	 */
	class LoadAccountTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String price;
		private String shopId;
		private String way;

		protected LoadAccountTask(String accessToken, String price,
				String shopId, String way) {
			this.accessToken = accessToken;
			this.price = price;
			this.shopId = shopId;
			this.way = way;

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopFinanceHelper().WithdrawSave(accessToken, price,
						shopId, way);
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
						Toast.makeText(AccountActivity.this,
								result.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(AccountActivity.this,
								result.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("LoginActivity", "JSONException");
				}
			} else {
				Log.i("LoginActivity", "result==null");
			}

		}
	}
}
