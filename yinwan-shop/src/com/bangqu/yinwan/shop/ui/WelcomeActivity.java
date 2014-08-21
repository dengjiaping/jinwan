package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.tuiguangyuan.sdk.activity.TuiGuangYuanActivateAgent;

public class WelcomeActivity extends Activity implements OnClickListener {

	private ViewPager mViewPager;
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;
	private Button btnWelCome;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		SharedPrefUtil.clearuserid(WelcomeActivity.this);
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);
		mPage3 = (ImageView) findViewById(R.id.page3);

		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.wel_1, null);
		View view2 = mLi.inflate(R.layout.wel_2, null);
		View view3 = mLi.inflate(R.layout.wel_3, null);
		View view4 = mLi.inflate(R.layout.wel_4, null);
		btnWelCome = (Button) view4.findViewById(R.id.btnWelCome);
		btnWelCome.setOnClickListener(this);

		// LayoutParams params;
		// params = mPage0.getLayoutParams();
		// params.height = 10;
		// params.width = 10;
		// LayoutParams params2;
		// params2 = mPage1.getLayoutParams();
		// params2.height = 10;
		// params2.width = 10;
		// LayoutParams params3;
		// params3 = mPage2.getLayoutParams();
		// params3.height = 10;
		// params3.width = 10;
		// LayoutParams params4;
		// params4 = mPage3.getLayoutParams();
		// params4.height = 10;
		// params4.width = 10;

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mViewPager.setAdapter(mPagerAdapter);
	}

	protected void onMyResume() {
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 1:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 2:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 3:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnWelCome:
			if (SharedPrefUtil.isFistLogin(WelcomeActivity.this)) {
				finish();
				startActivity(new Intent(WelcomeActivity.this,
						HomeActivity.class));
				 myhandler.postDelayed(actirunable, 1000);
			} else {
				finish();
			}
			break;

		default:
			break;
		}
	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// // TODO Auto-generated method stub
	// // startActivity(new Intent(WelcomeActivity.this,StartActivity.class));
	// // overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	// // finish();
	// return false;
	// }

	Handler myhandler = new Handler();
	Runnable actirunable = new Runnable() {

		@Override
		public void run() {
			// 该方法功能获取设备信息并提交，可直接调用。切忌放在onCreate方法中，会因为加载时间过长发生ANR报错，最好用线程启动
			TuiGuangYuanActivateAgent bb = new TuiGuangYuanActivateAgent(
					WelcomeActivity.this);
			bb.Activate();
		}
	};

}
