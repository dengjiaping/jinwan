package com.bangqu.yinwan.shop.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bangqu.yinwan.shop.R;

public class WebViewActivity extends Activity {
	private WebView mWebView;
	private LinearLayout llPro;
	private String url;
	ProgressBar progressBar;
	private TextView tvTittle;
	private Button btnRight;
	private Button btnLeft;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);

		url = getIntent().getStringExtra("url");
		getIntent().getStringExtra("tittleName");
		progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setBackgroundColor(Color.rgb(255, 255, 255));
		progressBar.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 3));

		findview();
		fillDate();
	};

	private void findview() {
		// TODO Auto-generated method stub
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setVisibility(View.INVISIBLE);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mWebView = (WebView) findViewById(R.id.wvWebView);
		llPro = (LinearLayout) findViewById(R.id.llPro);
		llPro.addView(progressBar);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void fillDate() {
		// TODO Auto-generated method stub
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.requestFocus();// 使WebView内的输入框等获得焦点
		mWebView.loadUrl(url);
		mWebView.setWebViewClient(new WebViewClient() {
			// 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// 可以让webView处理https请求
			@Override
			public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler,
					android.net.http.SslError error) {
				handler.proceed();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if (mWebView.getContentHeight() != 0) {
					// 这个时候网页才显示
					llPro.setVisibility(view.GONE);
				}
			};
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				// 这里将textView换成你的progress来设置进度
				progressBar.setProgress(newProgress);
				progressBar.postInvalidate();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			mWebView.goBack();
			// goBack()表示返回webView的上一页面，而不直接关闭WebView
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
