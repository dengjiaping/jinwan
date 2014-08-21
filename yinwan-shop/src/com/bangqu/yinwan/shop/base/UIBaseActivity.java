package com.bangqu.yinwan.shop.base;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

public class UIBaseActivity extends Activity {
	protected Handler mHandler = null;

	// 提交进度条
	protected ProgressDialog pd;
	// 加载等待界面
	protected View progressbar;

	/**
	 * 获取或初始化组件
	 */
	public void findView() {
		// 加载等待界面
		progressbar = (View) findViewById(R.id.progressbar);
	}

	/**
	 * 填充组件数据(测试)
	 */
	public void fillData() {
	}

}
