package com.bangqu.yinwan.shop.ui;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.util.StringUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePasswordActivity extends UIBaseActivity implements
		OnClickListener {

	private Button btnLeft;
	private TextView tvTittle2;
	private Button btnRight;
	private Button btnPasswordUpdate;
	private EditText etOldPassword;
	private EditText etNewPassword;
	private EditText etNewPassword2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_update_layout);

		findview();

	}

	private void findview() {
		// TODO Auto-generated method stub
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle2 = (TextView) findViewById(R.id.tvTittle);
		tvTittle2.setText("修改密码");
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		btnPasswordUpdate = (Button) findViewById(R.id.btnPasswordUpdate);
		btnPasswordUpdate.setOnClickListener(this);

		etOldPassword = (EditText) findViewById(R.id.etOldPassword);
		etNewPassword = (EditText) findViewById(R.id.etNewPassword);
		etNewPassword2 = (EditText) findViewById(R.id.etNewPassword2);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btnLeft:
			UpdatePasswordActivity.this.finish();
			break;

		case R.id.btnPasswordUpdate:

			String strOldPassword = etOldPassword.getText().toString().trim();
			String strNewPassword = etNewPassword.getText().toString().trim();
			String strNewPassword2 = etNewPassword2.getText().toString().trim();
			if (StringUtil.isBlank(strOldPassword)) {
				Toast.makeText(UpdatePasswordActivity.this, "请输入原密码",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strNewPassword)) {
				Toast.makeText(UpdatePasswordActivity.this, "请输入新密码！",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strNewPassword2)) {
				Toast.makeText(UpdatePasswordActivity.this, "请确认新密码！",
						Toast.LENGTH_LONG).show();
				return;
			}
			// if (!strOldPassword.equals(SharedPrefUtil.getUserBean(
			// UpdatePasswordActivity.this).getPassword())) {
			// Toast.makeText(UpdatePasswordActivity.this, "原密码不正确！请认真输入！",
			// Toast.LENGTH_LONG).show();
			// return;
			// }

			// if (strNewPassword.equals(strNewPassword2)) {
			// new LoadPasswordTask(
			// SharedPrefUtil.getToken(UpdatePasswordActivity.this),
			// strNewPassword, strOldPassword).execute();
			// } else {
			// Toast.makeText(UpdatePasswordActivity.this, "两次密码不一致！请核对！",
			// Toast.LENGTH_LONG).show();
			// }

			break;

		}
	}

	/**
	 * 修改密码
	 * 
	 * @author Administrator
	 * 
	 */
	// class LoadPasswordTask extends AsyncTask<String, Void, JSONObject> {
	//
	// private String accessToken;
	// private String password;
	// private String oldpassword;
	//
	// protected LoadPasswordTask(String accessToken, String password,
	// String oldpassword) {
	// this.accessToken = accessToken;
	// this.password = password;
	// this.oldpassword = oldpassword;
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// // loadPB.setVisibility(View.VISIBLE);
	// if (pd == null) {
	// pd = new ProgressDialog(UpdatePasswordActivity.this);
	// pd.setMessage("正在保存修改...");
	// }
	// pd.show();
	// }
	//
	// @Override
	// protected JSONObject doInBackground(String... params) {
	// try {
	// return new UserHelper().password(accessToken, password,
	// oldpassword);
	// } catch (SystemException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONObject result) {
	// super.onPostExecute(result);
	// if (pd != null)
	// pd.dismiss();
	// if (result != null) {
	// try {
	//
	// if (result.getInt("status") == Constants.SUCCESS) {
	// Toast.makeText(UpdatePasswordActivity.this,
	// "密码修改成功，请重新登录！", Toast.LENGTH_LONG).show();
	//
	// // 清空已经登录的用户信息
	// ((CommonApplication) getApplicationContext())
	// .getImgLoader().clearCache();
	// Toast.makeText(UpdatePasswordActivity.this,
	// "密码修改成功，原用户信息已清除，请重新登录！", Toast.LENGTH_LONG)
	// .show();
	//
	// // 清空用户信息之后跳转到登录页面，重新登录
	// Intent UpdatePasswordIntent = new Intent(
	// UpdatePasswordActivity.this,
	// LoginActivity.class);
	// startActivity(UpdatePasswordIntent);
	// finish();
	//
	// } else if (result.getInt("status") == Constants.FAIL) {
	// Toast.makeText(UpdatePasswordActivity.this,
	// "密码输入有误，请认真核对！", Toast.LENGTH_LONG).show();
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// Toast.makeText(UpdatePasswordActivity.this, "数据加载失败",
	// Toast.LENGTH_LONG).show();
	// Log.i("ProductListActivity", "JSONException");
	// }
	// } else {
	// Toast.makeText(UpdatePasswordActivity.this, "数据加载失败",
	// Toast.LENGTH_LONG).show();
	// Log.i("ProductListActivity", "result==null");
	// }
	//
	// }
	// }
}
