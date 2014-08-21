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

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.helper.UserHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class PasswordActivity extends UIBaseActivity implements OnClickListener {

	private Button btnLeft;
	private TextView tvTittle2;
	private Button btnRight;

	private Button btnGetPWD;
	private EditText etzhanghao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_layout);

		findview();

	}

	private void findview() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle2 = (TextView) findViewById(R.id.tvTittle);
		tvTittle2.setText("找回密码");
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		btnGetPWD = (Button) findViewById(R.id.btnGetPWD);
		btnGetPWD.setOnClickListener(this);
		etzhanghao = (EditText) findViewById(R.id.etzhanghao);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnLeft:
			PasswordActivity.this.finish();
			break;

		case R.id.btnGetPWD:
			String strzhanghao = etzhanghao.getText().toString().trim();
			if (StringUtil.isBlank(strzhanghao)) {

				Toast.makeText(PasswordActivity.this, "请输入用户名",
						Toast.LENGTH_LONG).show();
				return;
			} else {
				if (!StringUtil.isMobile(strzhanghao)) {
					Toast.makeText(PasswordActivity.this, "请输正确的手机号",
							Toast.LENGTH_LONG).show();
					return;
				}

			}

			new LoadGetPWDTask(strzhanghao).execute();
			break;

		default:
			break;

		}
	}

	/**
	 * 找回密码
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadGetPWDTask extends AsyncTask<String, Void, JSONObject> {

		private String username;

		protected LoadGetPWDTask(String username) {
			this.username = username;

		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(PasswordActivity.this,
						"正在提交……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new UserHelper().getPwd(username);
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
						startActivity(new Intent(PasswordActivity.this,
								ReSetPWDActivity.class));
						PasswordActivity.this.finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(PasswordActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(PasswordActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("LoginActivity", "JSONException");
				}
			} else {
				Toast.makeText(PasswordActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("LoginActivity", "result==null");
			}

		}
	}
}
