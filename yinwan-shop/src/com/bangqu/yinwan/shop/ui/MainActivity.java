package com.bangqu.yinwan.shop.ui;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnClickListener {
	TabHost tabHost;
	private RelativeLayout rlHomeOne;
	private RelativeLayout rlHomeTwo;
	private RelativeLayout rlHomeThree;
	private RelativeLayout rlHomeFour;

	private ImageView ivHomeOne;
	private ImageView ivHomeTwo;
	private ImageView ivHomeThree;
	private ImageView ivHomeFour;

	private TextView tvHomeOne;
	private TextView tvHomeTwo;
	private TextView tvHomeThree;
	private TextView tvHomeFour;

	private RadioButton rbHomeOne;
	private RadioButton rbHomeTwo;
	private RadioButton rbHomeThree;
	private RadioButton rbHomeFour;

	ColorStateList csl_normal;
	ColorStateList csl_pressed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_foot);

		initTab();
		findView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void onMyResume() {
	}

	public void initTab() {
		tabHost = getTabHost();
		// 首页
		Intent intent = new Intent(MainActivity.this, HomeActivity.class);
		tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
				.setContent(intent));

		Intent intent2 = new Intent(MainActivity.this,
				HomeOrderManageActivity.class);
		tabHost.addTab(tabHost.newTabSpec("order").setIndicator("order")
				.setContent(intent2));

		Intent intent3 = new Intent(MainActivity.this,
				HomeProductManageActivity.class);
		tabHost.addTab(tabHost.newTabSpec("product").setIndicator("product")
				.setContent(intent3));

		Intent intent4 = new Intent(MainActivity.this, HomeMoreActivity.class);
		tabHost.addTab(tabHost.newTabSpec("more").setIndicator("more")
				.setContent(intent4));
	}

	public void findView() {
		// TODO Auto-generated method stub
		rlHomeOne = (RelativeLayout) findViewById(R.id.rlHomeOne);
		rlHomeTwo = (RelativeLayout) findViewById(R.id.rlHomeTwo);
		rlHomeThree = (RelativeLayout) findViewById(R.id.rlHomeThree);
		rlHomeFour = (RelativeLayout) findViewById(R.id.rlHomeFour);

		ivHomeOne = (ImageView) findViewById(R.id.ivHomeOne);
		ivHomeTwo = (ImageView) findViewById(R.id.ivHomeTwo);
		ivHomeThree = (ImageView) findViewById(R.id.ivHomeThree);
		ivHomeFour = (ImageView) findViewById(R.id.ivHomeFour);

		tvHomeOne = (TextView) findViewById(R.id.tvHomeOne);
		tvHomeTwo = (TextView) findViewById(R.id.tvHomeTwo);
		tvHomeThree = (TextView) findViewById(R.id.tvHomeThree);
		tvHomeFour = (TextView) findViewById(R.id.tvHomeFour);

		rbHomeOne = (RadioButton) findViewById(R.id.rbHomeOne);
		rbHomeTwo = (RadioButton) findViewById(R.id.rbHomeTwo);
		rbHomeThree = (RadioButton) findViewById(R.id.rbHomeThree);
		rbHomeFour = (RadioButton) findViewById(R.id.rbHomeFour);

		rbHomeOne.setOnClickListener(this);
		rbHomeTwo.setOnClickListener(this);
		rbHomeThree.setOnClickListener(this);
		rbHomeFour.setOnClickListener(this);

		Resources resource = (Resources) getBaseContext().getResources();
		csl_normal = (ColorStateList) resource
				.getColorStateList(R.color.color_home_normal);
		csl_pressed = (ColorStateList) resource
				.getColorStateList(R.color.color_home_pressed);

	}

	public void fillData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rbHomeOne:
			tabHost.setCurrentTabByTag("home");
			setBackground(1);
			break;

		case R.id.rbHomeTwo:
			if (SharedPrefUtil.checkLogin(MainActivity.this)) {
				if (SharedPrefUtil.checkShop(MainActivity.this)) {
					tabHost.setCurrentTabByTag("order");
					setBackground(2);
				} else {
					startActivity(new Intent(MainActivity.this,
							SelectShopActivity.class));
					Toast.makeText(MainActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("是否登录")
						.setMessage("您要登录吗？")
						.setPositiveButton("登录",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										startActivity(new Intent(
												MainActivity.this,
												LoginActivity.class));
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel(); // 删除对话框
									}
								});
				AlertDialog alert = builder.create();// 创建对话框
				alert.show();// 显示对话框
			}
			break;

		case R.id.rbHomeThree:
			if (SharedPrefUtil.checkLogin(MainActivity.this)) {
				if (SharedPrefUtil.checkShop(MainActivity.this)) {
					tabHost.setCurrentTabByTag("product");
					setBackground(3);
				} else {
					startActivity(new Intent(MainActivity.this,
							SelectShopActivity.class));
					Toast.makeText(MainActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("是否登录")
						.setMessage("您要登录吗？")
						.setPositiveButton("登录",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										startActivity(new Intent(
												MainActivity.this,
												LoginActivity.class));
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel(); // 删除对话框
									}
								});
				AlertDialog alert = builder.create();// 创建对话框
				alert.show();// 显示对话框
			}
			break;

		case R.id.rbHomeFour:
			if (SharedPrefUtil.checkLogin(MainActivity.this)) {
				if (SharedPrefUtil.checkShop(MainActivity.this)) {
					tabHost.setCurrentTabByTag("more");
					setBackground(4);
				} else {
					startActivity(new Intent(MainActivity.this,
							SelectShopActivity.class));
					Toast.makeText(MainActivity.this, "请选择您要操作的店铺",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("是否登录")
						.setMessage("您要登录吗？")
						.setPositiveButton("登录",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										startActivity(new Intent(
												MainActivity.this,
												LoginActivity.class));
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel(); // 删除对话框
									}
								});
				AlertDialog alert = builder.create();// 创建对话框
				alert.show();// 显示对话框
			}
			break;

		default:
			break;
		}
	}

	private void setBackground(int id) {
		// TODO Auto-generated method stub

		ivHomeOne.setBackgroundResource(R.drawable.home_bottom_one_normal);
		ivHomeTwo.setBackgroundResource(R.drawable.home_bottom_two_normal);
		ivHomeThree.setBackgroundResource(R.drawable.home_bottom_three_normal);
		ivHomeFour.setBackgroundResource(R.drawable.home_bottom_five_normal);

		rlHomeOne.setBackgroundResource(0);
		rlHomeTwo.setBackgroundResource(0);
		rlHomeThree.setBackgroundResource(0);
		rlHomeFour.setBackgroundResource(0);

		tvHomeOne.setTextColor(csl_normal);
		tvHomeTwo.setTextColor(csl_normal);
		tvHomeThree.setTextColor(csl_normal);
		tvHomeFour.setTextColor(csl_normal);

		switch (id) {
		case 1:
			rlHomeOne.setBackgroundResource(R.drawable.home_bottom_check);
			ivHomeOne.setBackgroundResource(R.drawable.home_bottom_one_pressed);
			tvHomeOne.setTextColor(csl_pressed);
			break;
		case 2:
			rlHomeTwo.setBackgroundResource(R.drawable.home_bottom_check);
			ivHomeTwo.setBackgroundResource(R.drawable.home_bottom_two_pressed);
			tvHomeTwo.setTextColor(csl_pressed);
			break;
		case 3:
			rlHomeThree.setBackgroundResource(R.drawable.home_bottom_check);
			ivHomeThree
					.setBackgroundResource(R.drawable.home_bottom_three_pressed);
			tvHomeThree.setTextColor(csl_pressed);
			break;
		case 4:
			rlHomeFour.setBackgroundResource(R.drawable.home_bottom_check);
			ivHomeFour
					.setBackgroundResource(R.drawable.home_bottom_five_pressed);
			tvHomeFour.setTextColor(csl_pressed);
			break;

		default:
			break;
		}
	}

	private long mExitTime;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				// finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}