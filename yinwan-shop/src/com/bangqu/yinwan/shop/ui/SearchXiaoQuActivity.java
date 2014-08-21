package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

public class SearchXiaoQuActivity extends UIBaseActivity implements
		OnClickListener {

	// 选择小区三模块
	private FrameLayout flChooseSearch;

	// 主体的viewpager
	private ViewPager vpMyCollect;// 页卡内容
	private TextView tvMyCollectOne;
	private TextView tvMyCollectTwo;
	private TextView tvMyCollectThr;
	private ArrayList<View> list;

	private int offset;// 偏移量
	private int imageWidth;// 图片宽度
	private ImageView activeBar = null;

	LocalActivityManager manager = null;
	ColorStateList csl;

	private Button btnLeft;
	private Button btnRight;
	private TextView tvbarleft;

	// 添加
	private Button btnaddmore;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_xiaoqu_layout);
		SharedPrefUtil.setFistSearch(SearchXiaoQuActivity.this);
		// viewpager
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		findView();
	}

	@Override
	public void findView() {
		super.findView();
		btnaddmore = (Button) findViewById(R.id.btnaddmore);
		btnaddmore.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		tvbarleft = (TextView) findViewById(R.id.tvbarleft);
		tvbarleft.setText("小区管理");
		flChooseSearch = (FrameLayout) findViewById(R.id.flChooseSearch);

		// 初始化动画
		// 获取该激活条的宽度
		activeBar = (ImageView) findViewById(R.id.ivCursor);
		this.imageWidth = BitmapFactory.decodeResource(getResources(),
				R.drawable.title_bg_viewpager_item).getWidth();
		// 获取屏幕的宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		// 计算出偏移量
		this.offset = (screenWidth / 3 - imageWidth) / 2;
		// 设置图片初始值
		Matrix matrixInit = new Matrix();
		matrixInit.postTranslate(this.offset, 0);
		activeBar.setImageMatrix(matrixInit);

		// 初始化头标
		tvMyCollectOne = (TextView) findViewById(R.id.tvMyCollect1);
		tvMyCollectTwo = (TextView) findViewById(R.id.tvMyCollect2);
		tvMyCollectThr = (TextView) findViewById(R.id.tvMyCollect3);

		tvMyCollectOne.setOnClickListener(this);
		tvMyCollectTwo.setOnClickListener(this);
		tvMyCollectThr.setOnClickListener(this);

		Resources resource = (Resources) getBaseContext().getResources();
		csl = (ColorStateList) resource.getColorStateList(R.color.color_grey2);

		// 初始化viewpager
		vpMyCollect = (ViewPager) findViewById(R.id.vpMyCollect);
		list = new ArrayList<View>();
		Intent intent = new Intent(SearchXiaoQuActivity.this,
				LocationOneActivity.class);
		list.add(getView("locationone", intent));
		Intent intent2 = new Intent(SearchXiaoQuActivity.this,
				LocationTwoActivity.class);
		list.add(getView("locationtwo", intent2));
		Intent intent3 = new Intent(SearchXiaoQuActivity.this,
				LocationThrActivity.class);
		list.add(getView("locationthr", intent3));

		vpMyCollect.setAdapter(new MyViewPagerAdapter(list));
		vpMyCollect.setCurrentItem(0);
		vpMyCollect.setOnPageChangeListener(new MyOnPageChangeListener(
				this.activeBar, this.offset, this.imageWidth));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			SearchXiaoQuActivity.this.finish();
			break;

		case R.id.tvMyCollect1:
			vpMyCollect.setCurrentItem(0);
			break;

		case R.id.tvMyCollect2:
			vpMyCollect.setCurrentItem(1);
			break;
		case R.id.tvMyCollect3:
			vpMyCollect.setCurrentItem(2);
			break;
		case R.id.btnLeft:
			finish();
			break;
		case R.id.btnaddmore:
			startActivity(new Intent(SearchXiaoQuActivity.this,
					SearchXiaoQuTwoActivity.class));
			break;
		default:
			break;
		}

	}

	/**
	 * 通过activity获取视图
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/**
	 * viewpager适配器
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		private ImageView activeBar = null;
		private int offset;
		private int imageWidth;

		public MyOnPageChangeListener(ImageView activeBar, int offset, int iw) {
			this.activeBar = activeBar;
			this.offset = offset;
			this.imageWidth = iw;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

			Matrix matrix = new Matrix();
			// 设置激活条的最终位置
			switch (arg0) {
			case 0:
				// 使用set直接设置到那个位置
				matrix.setTranslate(this.offset, 0);
				break;
			case 1:
				matrix.setTranslate(this.offset * 3 + this.imageWidth, 0);
				break;
			case 2:
				matrix.setTranslate(this.offset * 5 + this.imageWidth * 2, 0);
				break;
			}
			// 在滑动的过程中，计算出激活条应该要滑动的距离
			float t = (this.offset * 2 + this.imageWidth) * arg1;
			// 使用post追加数值
			matrix.postTranslate(t, 0);
			this.activeBar.setImageMatrix(matrix);
		}

		public void onPageSelected(int arg0) {

			switch (arg0) {
			case 0:
				tvMyCollectOne.setTextColor(Color.BLACK);
				tvMyCollectTwo.setTextColor(csl);
				tvMyCollectThr.setTextColor(csl);
				Activity _activity = manager.getActivity("locationone");
				if (_activity != null
						&& _activity instanceof ProductUnPromotedActivity) {
					((ProductUnPromotedActivity) _activity).onMyResume();
				}
				break;
			case 1:
				tvMyCollectOne.setTextColor(csl);
				tvMyCollectTwo.setTextColor(Color.BLACK);
				tvMyCollectThr.setTextColor(csl);
				Activity _activity1 = manager.getActivity("locationtwo");
				if (_activity1 != null
						&& _activity1 instanceof ProductUnPromotedActivity) {
					((ProductUnPromotedActivity) _activity1).onMyResume();
				}
				break;
			case 2:
				tvMyCollectOne.setTextColor(csl);
				tvMyCollectTwo.setTextColor(csl);
				tvMyCollectThr.setTextColor(Color.BLACK);
				Activity _activity2 = manager.getActivity("locationthr");
				if (_activity2 != null
						&& _activity2 instanceof ProductUnPromotedActivity) {
					((ProductUnPromotedActivity) _activity2).onMyResume();
				}
				break;

			}
		}
	}
}
