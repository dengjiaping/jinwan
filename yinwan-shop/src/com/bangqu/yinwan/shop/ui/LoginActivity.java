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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.UserBean;
import com.bangqu.yinwan.shop.helper.UserHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class LoginActivity extends UIBaseActivity implements OnClickListener {

	private Button btnLeft;
	private TextView tvTittle2;
	private Button btnRight;
	private Button btnLogin;
	private EditText etUserName;
	private EditText etPassword;
	private TextView tvgetPWD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		findview();

	}

	private void findview() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle2 = (TextView) findViewById(R.id.tvTittle);
		tvTittle2.setText("用户登录");
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setText("注册");
		btnRight.setOnClickListener(this);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		tvgetPWD = (TextView) findViewById(R.id.tvgetPWD);
		tvgetPWD.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!StringUtil
				.isBlank(SharedPrefUtil.getNameCache(LoginActivity.this))) {
			etUserName.setText(SharedPrefUtil.getNameCache(LoginActivity.this));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnLeft:
			// startActivity(new Intent(LoginActivity.this,
			// HomeActivity.class));
			LoginActivity.this.finish();
			break;
		case R.id.btnRight:
			Intent registintent = new Intent(LoginActivity.this,
					RegistActivity.class);
			startActivity(registintent);
			break;

		case R.id.btnLogin:
			String strUserName = etUserName.getText().toString().trim();
			String strPassword = etPassword.getText().toString().trim();
			if (StringUtil.isBlank(strUserName)) {
				Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (!StringUtil.isMobile(strUserName)) {
				Toast.makeText(LoginActivity.this, "请输入正确的手机号码",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strPassword)) {
				Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_LONG)
						.show();
				return;
			}

			SharedPrefUtil.setpasswd(LoginActivity.this, strPassword);
			SharedPrefUtil.setzhanghao(LoginActivity.this, strUserName);
			if (StringUtil.isBlank(SharedPrefUtil
					.getdeviceToken(LoginActivity.this))) {
				new LoadUserLoginTask(strUserName, strPassword, "").execute();
			} else {
				new LoadUserLoginTask(strUserName, strPassword,
						SharedPrefUtil.getdeviceToken(LoginActivity.this))
						.execute();
			}

			break;
		case R.id.tvgetPWD:
			Intent getbackintent = new Intent(LoginActivity.this,
					PasswordActivity.class);
			startActivity(getbackintent);
			break;

		default:
			break;

		}
	}

	/**
	 * 用户登录
	 * 
	 * @author Administrator
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
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(LoginActivity.this,
						"正在登录……");
			}
			pd.show();
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
			if (pd != null)
				pd.dismiss();
			if (result != null) {
				try {

					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(LoginActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						JSONObject json = result.getJSONObject("user");
						UserBean userBean = JSON.parseObject(json.toString(),
								UserBean.class);

						String accessToken = result
								.getJSONObject("accessToken").getString(
										"accessToken");
						SharedPrefUtil.setUserID(LoginActivity.this,
								userBean.getId());
						SharedPrefUtil
								.setToken(LoginActivity.this, accessToken);
						SharedPrefUtil
								.setUserBean(LoginActivity.this, userBean);
						SharedPrefUtil.setNameCache(LoginActivity.this,
								userBean.getUsername());
						SharedPrefUtil.setAlipay(LoginActivity.this,
								userBean.getAlipay());
						SharedPrefUtil.setrealname(LoginActivity.this,
								userBean.getRealname());
						Constants.isExitLogin = false;
						setResult(RESULT_OK);
						Toast.makeText(LoginActivity.this, "您已登录成功，请选择店铺！",
								Toast.LENGTH_SHORT).show();
						Intent selectshopintent = new Intent(
								LoginActivity.this, SelectShopActivity.class);
						startActivity(selectshopintent);

						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(LoginActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(LoginActivity.this, "用户名或密码错误",
							Toast.LENGTH_LONG).show();
					Log.i("LoginActivity", "JSONException");
				}
			} else {
				Toast.makeText(LoginActivity.this, "用户名或密码错误",
						Toast.LENGTH_LONG).show();
				Log.i("LoginActivity", "result==null");
			}

		}
	}
}
