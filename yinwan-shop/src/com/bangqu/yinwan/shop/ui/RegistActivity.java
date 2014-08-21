package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

/**
 * 注册界面
 */
public class RegistActivity extends UIBaseActivity implements OnClickListener {

	private Button btnLeft;
	private TextView tvTittle2;
	private Button btnRight;
	private Button btnRegist;
	private EditText etUserName;
	private EditText etPassword;
	private EditText etPassword2;
	private EditText etUNnicheng;
	private TextView tvDeal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist_layout);
		findview();
	}

	private void findview() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle2 = (TextView) findViewById(R.id.tvTittle);
		tvTittle2.setText("用户注册");
		tvDeal = (TextView) findViewById(R.id.tvDeal);
		tvDeal.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		btnRegist = (Button) findViewById(R.id.btnRegist);
		btnRegist.setOnClickListener(this);

		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPassword2 = (EditText) findViewById(R.id.etPassword2);
		etUNnicheng = (EditText) findViewById(R.id.etUNnicheng);

	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnLeft:
			RegistActivity.this.finish();
			break;
		case R.id.tvDeal:
			System.out.println("ok");
			Intent feedbackintent = new Intent(RegistActivity.this, UrlWebView.class);
			feedbackintent.putExtra("url", "http://api.yinwan.bangqu.com/shop/agreement");
			feedbackintent.putExtra("title", "银湾社区生活网服务协议");
			startActivity(feedbackintent);
			break;

		case R.id.btnRegist:

			String strUserName = etUserName.getText().toString().trim();
			String strPassword = etPassword.getText().toString().trim();
			String strPassword2 = etPassword2.getText().toString().trim();
			String strnicheng = etUNnicheng.getText().toString().trim();

			if (StringUtil.isBlank(strUserName)) {
				Toast.makeText(RegistActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!StringUtil.isMobile(strUserName)) {
				Toast.makeText(RegistActivity.this, "用户名必须为手机", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(strnicheng)) {
				Toast.makeText(RegistActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringUtil.isBlank(strPassword)) {
				Toast.makeText(RegistActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(strPassword2)) {
				Toast.makeText(RegistActivity.this, "请确认密码！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!strPassword.equals(strPassword2)) {
				Toast.makeText(RegistActivity.this, "两次输入密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
				etPassword.setText("");
				etPassword2.setText("");
				return;
			}
			new LoadUserSignupTask(strUserName, strPassword, strnicheng, "SHOP").execute();

			break;

		}
	}

	/**
	 * 用户注册
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadUserSignupTask extends AsyncTask<String, Void, JSONObject> {
		private String username;
		private String password;
		private String nickname;
		private String type;

		protected LoadUserSignupTask(String username, String password, String nickname, String type) {
			this.username = username;
			this.password = password;
			this.nickname = nickname;
			this.type = type;
		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(RegistActivity.this, "正在注册……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new UserHelper().usersignup(username, password, nickname, type);
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
						Toast.makeText(RegistActivity.this, "注册成功，您现在可以用此帐号登录！", Toast.LENGTH_LONG)
								.show();
						JSONObject json = result.getJSONObject("user");
						UserBean userBean = JSON.parseObject(json.toString(), UserBean.class);
						SharedPrefUtil.setNameCache(RegistActivity.this, userBean.getUsername());
						startActivity(new Intent(RegistActivity.this, LoginActivity.class));

						
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(RegistActivity.this, result.getString("msg"),
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(RegistActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
					Log.i("RegistActivity", "JSONException");
				}
			} else {
				Toast.makeText(RegistActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
				Log.i("RegistActivity", "result==null");
			}

		}
	}

}
