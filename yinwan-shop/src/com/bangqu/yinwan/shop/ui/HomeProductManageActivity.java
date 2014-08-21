package com.bangqu.yinwan.shop.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.control.HomeProductMessageControl;

/**
 * 商品服务管理
 */
public class HomeProductManageActivity extends UIBaseActivity {
	private HomeProductMessageControl control;
	private TextView btnLeft;
	private Button btnRight;
	private TextView tvTittle;

	private TextView tvproductManage;
	private TextView tvproductCategory;
	private TextView tvxiaoliang;
	private TextView tvSevice;
	private TextView tvassess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_product_layout);
		control = new HomeProductMessageControl(this);
		findView();
	}

	@Override
	public void findView() {
		super.findView();
		tvassess = (TextView) findViewById(R.id.tvassess);
		tvassess.setOnClickListener(control.getintent());
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(control.getbackClickListener());
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("商品服务管理");

		tvproductManage = (TextView) findViewById(R.id.tvproductManage);
		tvproductCategory = (TextView) findViewById(R.id.tvproductCategory);
		tvxiaoliang = (TextView) findViewById(R.id.tvxiaoliang);
		tvSevice = (TextView) findViewById(R.id.tvSevice);

		tvproductManage.setOnClickListener(control.getintent());
		tvproductCategory.setOnClickListener(control.getintent());
		tvxiaoliang.setOnClickListener(control.getintent());
		tvSevice.setOnClickListener(control.getintent());
	}

}