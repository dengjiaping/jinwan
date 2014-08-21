package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.BankBean;
import com.bangqu.yinwan.shop.helper.ShopFinanceHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

/**
 * 添加银行卡
 */

public class AddBankCardActivity extends UIBaseActivity implements
		OnClickListener {
	private EditText etCardNumber;
	private TextView tvBankchoice;
	private EditText etUserName;
	private EditText etMobile;
	private Button btAddCard;
	private BankBean bankBean;
	private int item = 0;
	private String stradress = "";

	private TextView tvTittle;
	private Button btnLeft;
	private Button btnRight;
	private String[] bankname = { "中国农业银行", "中国建设银行", "中国工商银行", "交通银行" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bankcard_layout);
		findView();
		new LoadBankCardTask(SharedPrefUtil.getToken(AddBankCardActivity.this))
				.execute();
	}

	@Override
	public void findView() {
		super.findView();
		etCardNumber = (EditText) findViewById(R.id.etCardNumber);
		tvBankchoice = (TextView) findViewById(R.id.tvBankchoice);
		tvBankchoice.setOnClickListener(this);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etMobile = (EditText) findViewById(R.id.etMobile);
		if (!StringUtil.isBlank(SharedPrefUtil.getUserBean(
				AddBankCardActivity.this).getMobile())) {
			etMobile.setText(SharedPrefUtil.getUserBean(
					AddBankCardActivity.this).getMobile());
		}
		btAddCard = (Button) findViewById(R.id.btAddCard);
		btAddCard.setOnClickListener(AddBankCardActivity.this);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("银行卡");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
	}

	@Override
	public void fillData() {
		super.fillData();
		if (!StringUtil.isBlank(bankBean.getAccountName())) {
			etUserName.setText(bankBean.getAccountName());
		}
		if (!StringUtil.isBlank(bankBean.getBankName())) {
			tvBankchoice.setText(bankBean.getBankName());
			if (bankBean.getBankName().equals("中国农业银行")) {
				item = 0;
			} else if (bankBean.getBankName().equals("中国建设银行")) {
				item = 1;
			} else if (bankBean.getBankName().equals("中国工商银行")) {
				item = 2;
			} else if (bankBean.getBankName().equals("交通银行")) {
				item = 3;
			}

		}

		if (!StringUtil.isBlank(bankBean.getCardNo())) {
			etCardNumber.setText(bankBean.getCardNo());

		}
		if (!StringUtil.isBlank(bankBean.getMobile())) {
			etMobile.setText(bankBean.getMobile());
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvBankchoice:
			new AlertDialog.Builder(this)
					.setTitle("银行选择")
					.setSingleChoiceItems(bankname, item,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									item = which;
									tvBankchoice.setText(bankname[which]);
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();
			break;

		case R.id.btAddCard:
			String strCardNumber = etCardNumber.getText().toString().trim();
			String strBankName = tvBankchoice.getText().toString().trim();
			String strUserName = etUserName.getText().toString().trim();
			String strMobile = etMobile.getText().toString().trim();
			if (StringUtil.isBlank(strUserName)) {
				Toast.makeText(AddBankCardActivity.this, "请输入持卡人姓名",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(strCardNumber)) {
				Toast.makeText(AddBankCardActivity.this, "请输入银行卡号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (strCardNumber.length() <= 7) {
				Toast.makeText(AddBankCardActivity.this, "您输入的银行卡位数不足",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringUtil.isBlank(strBankName)) {
				Toast.makeText(AddBankCardActivity.this, "请选择发卡银行",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (strBankName.equals("中国农业银行")) {
				stradress = "http://gezidan.qiniudn.com/abc.png";
			} else if (strBankName.equals("中国建设银行")) {
				stradress = "http://gezidan.qiniudn.com/ccb.png";
			} else if (strBankName.equals("中国工商银行")) {
				stradress = "http://gezidan.qiniudn.com/icbc.png";
			} else if (strBankName.equals("交通银行")) {
				stradress = "http://gezidan.qiniudn.com/bcm.png";
			} else {
			}
			System.out.println(stradress);
			if (StringUtil.isBlank(strMobile)) {
				Toast.makeText(AddBankCardActivity.this, "请输入手机号码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!StringUtil.isMobile(strMobile)) {
				Toast.makeText(AddBankCardActivity.this, "请输入正确的手机号码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			new LoadAddCardTask(SharedPrefUtil.getToken(this), stradress,
					strBankName, strCardNumber, strUserName, strMobile)
					.execute();
			break;
		case R.id.btnLeft:
			AddBankCardActivity.this.finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 添加银行卡
	 * 
	 */
	class LoadAddCardTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String bankIcon;
		private String bankName;
		private String accountName;
		private String cardNo;
		private String mobile;

		protected LoadAddCardTask(String accessToken, String bankIcon,
				String bankName, String cardNo, String accountName,
				String mobile) {
			this.accessToken = accessToken;
			this.bankIcon = bankIcon;
			this.bankName = bankName;
			this.cardNo = cardNo;
			this.accountName = accountName;
			this.mobile = mobile;

		}

		@Override
		protected void onPreExecute() {

			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						AddBankCardActivity.this, "正在修改……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopFinanceHelper().banksave(accessToken, bankIcon,
						bankName, cardNo, accountName, mobile);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pd != null) {
				pd.dismiss();
			}
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(AddBankCardActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();

						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(AddBankCardActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(AddBankCardActivity.this, "加载数据失败",
							Toast.LENGTH_LONG).show();
					Log.i("LoginActivity", "JSONException");
				}
			} else {
				Toast.makeText(AddBankCardActivity.this, "加载数据失败",
						Toast.LENGTH_LONG).show();
				Log.i("LoginActivity", "result==null");
			}

		}

	}

	/**
	 * 我的银行卡
	 * 
	 * @author Administrator
	 * 
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
				// 返回一个 JSONObject
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
						fillData();
						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						progressbar.setVisibility(View.GONE);
					}
				} catch (Exception e) {

					e.printStackTrace();
					Toast.makeText(AddBankCardActivity.this, "加载数据失败",
							Toast.LENGTH_LONG).show();
				}
			}
		}

	}
}
