package com.bangqu.yinwan.shop.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

public class LogoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.logo_layout, null);

		if (SharedPrefUtil.iscleanall(LogoActivity.this)) {
			SharedPrefUtil.clearUserBean(LogoActivity.this);
			SharedPrefUtil.clearShopBean(LogoActivity.this);
			SharedPrefUtil.clearvip(LogoActivity.this);
			SharedPrefUtil.cleardeviceToken(LogoActivity.this);
		}
		SharedPrefUtil.setcleanall(LogoActivity.this);

		setContentView(view);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				if (SharedPrefUtil.isFistLogin(LogoActivity.this)) {
					startActivity(new Intent(LogoActivity.this,
							WelcomeActivity.class));
					finish();
				} else {
					startActivity(new Intent(LogoActivity.this,
							HomeActivity.class));
					finish();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});

	}
}
