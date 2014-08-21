package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.helper.ShopFinanceHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class AddAlipayActivity extends UIBaseActivity implements
		OnClickListener {
	private Button btAddAlipay;
	private EditText etAddAlipay;
	private EditText etrealname;
	private Button btnLeft;
	private Button btnRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_alipay_layout);
		findView();
	}

	@Override
	public void findView() {
		super.findView();
		etrealname = (EditText) findViewById(R.id.etrealname);
		etrealname.setText(SharedPrefUtil.getRealname(this));
		btAddAlipay = (Button) findViewById(R.id.btAddAlipay);
		btAddAlipay.setOnClickListener(this);
		etAddAlipay = (EditText) findViewById(R.id.etAddAlipay);
		etAddAlipay.setText(SharedPrefUtil.getAlipay(this));
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btAddAlipay:
			String strAlipay = etAddAlipay.getText().toString().trim();
			String strRealname = etrealname.getText().toString().trim();

			if (StringUtil.isBlank(strRealname)) {
				Toast.makeText(this, "请输入真实姓名", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(strAlipay)) {
				Toast.makeText(this, "请输入支付宝账号", Toast.LENGTH_SHORT).show();
				return;
			}

			new LoadUpdateAlipayTask(
					SharedPrefUtil.getToken(AddAlipayActivity.this),
					strRealname, strAlipay).execute();
			break;
		case R.id.btnLeft:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 修改资料
	 * 
	 */
	class LoadUpdateAlipayTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String realname;
		private String alipay;

		protected LoadUpdateAlipayTask(String accessToken, String realname,
				String alipay) {
			this.accessToken = accessToken;
			this.realname = realname;
			this.alipay = alipay;
		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(AddAlipayActivity.this,
						"正在提交……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				// 返回一个 JSONObject
				return new ShopFinanceHelper().updatealipay(accessToken,
						realname, alipay);
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
						Toast.makeText(AddAlipayActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						SharedPrefUtil.setAlipay(AddAlipayActivity.this,
								etAddAlipay.getText().toString().trim());
						SharedPrefUtil.setrealname(AddAlipayActivity.this,
								etrealname.getText().toString().trim());
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(AddAlipayActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {

					e.printStackTrace();
					Toast.makeText(AddAlipayActivity.this, "加载数据失败",
							Toast.LENGTH_LONG).show();
				}
			}
		}

	}

}
