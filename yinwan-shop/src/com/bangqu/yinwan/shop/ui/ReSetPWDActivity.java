package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

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

public class ReSetPWDActivity extends UIBaseActivity implements OnClickListener {

	private Button btnLeft;
	private TextView tvTittle2;
	private Button btnRight;

	private Button btnGetPWDone;
	private EditText etCode;
	private EditText etNewPWD;
	private EditText etNewPWD2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resetpwd_layout);

		findview();
	}

	private void findview() {
		// TODO Auto-generated method stub
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle2 = (TextView) findViewById(R.id.tvTittle);
		tvTittle2.setText("重置密码");
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		btnGetPWDone = (Button) findViewById(R.id.btnGetPWDone);
		btnGetPWDone.setOnClickListener(this);

		etCode = (EditText) findViewById(R.id.etCode);
		etNewPWD = (EditText) findViewById(R.id.etNewPWD);
		etNewPWD2 = (EditText) findViewById(R.id.etNewPWD2);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btnLeft:
			ReSetPWDActivity.this.finish();
			break;

		case R.id.btnGetPWDone:

			String strcode = etCode.getText().toString().trim();
			String strpwd1 = etNewPWD.getText().toString().trim();
			String strpwd2 = etNewPWD2.getText().toString().trim();
			if (StringUtil.isBlank(strcode)) {
				Toast.makeText(ReSetPWDActivity.this, "请输入验证码",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strpwd1)) {
				Toast.makeText(ReSetPWDActivity.this, "请输入新密码",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strpwd2)) {
				Toast.makeText(ReSetPWDActivity.this, "请确认密码",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (!strpwd1.equals(strpwd2)) {
				Toast.makeText(ReSetPWDActivity.this, "两次密码不一致",
						Toast.LENGTH_LONG).show();
				return;
			}
			new LoadResetPWDTask(strcode, strpwd1, strpwd2).execute();
			break;

		default:
			break;

		}
	}

	/**
	 * 修改密码（重置）
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadResetPWDTask extends AsyncTask<String, Void, JSONObject> {

		private String code;
		private String password;
		private String confirmPassword;

		protected LoadResetPWDTask(String code, String password,
				String confirmPassword) {
			this.code = code;
			this.password = password;
			this.confirmPassword = confirmPassword;

		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(ReSetPWDActivity.this,
						"正在提交……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new UserHelper().resetpwd(code, password,
						confirmPassword);
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
						Toast.makeText(ReSetPWDActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();

						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ReSetPWDActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ReSetPWDActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("LoginActivity", "JSONException");
				}
			} else {
				Toast.makeText(ReSetPWDActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("LoginActivity", "result==null");
			}

		}
	}
}
