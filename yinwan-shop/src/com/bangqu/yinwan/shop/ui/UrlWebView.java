package com.bangqu.yinwan.shop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.util.StringUtil;

@SuppressLint("SetJavaScriptEnabled")
public class UrlWebView extends Activity implements OnClickListener {
	private Button btnLeft;
	private Button btnRight;
	private TextView tvTittle;

	private View progressbar;

	private WebView webView;
	private String BangQuUrl = "";

	private LinearLayout llProgress;
	ProgressBar myprogressBar;

	// 当前进度条里的值
	private int progress = 0;
	private RemoteViews view = null;
	private Notification notification = new Notification();
	private NotificationManager nManager = null;
	private PendingIntent pIntent = null;
//	private Handler handle = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			view.setProgressBar(R.id.pb, 100, progress, false);
//			view.setTextViewText(R.id.tv, "下载" + progress + "%");// 关键部分，如果你不重新更新通知，进度条是不会更新的
//			notification.contentView = view;
//			notification.flags = Notification.FLAG_AUTO_CANCEL;
//			// notification.contentIntent = pIntent;
//			nManager.notify(0, notification);
//			super.handleMessage(msg);
//
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		if (!StringUtil.isBlank(getIntent().getStringExtra("url"))) {
			BangQuUrl = getIntent().getStringExtra("url");
		}

		findView();
		fillDate();
	}

	private void findView() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText(getIntent().getStringExtra("title"));
		webView = (WebView) findViewById(R.id.wvBangQu);

		progressbar = (View) findViewById(R.id.progressbar);

		myprogressBar = new ProgressBar(this, null,
				android.R.attr.progressBarStyleHorizontal);
		myprogressBar.setBackgroundColor(Color.rgb(255, 255, 255));
		myprogressBar.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 3));
		llProgress = (LinearLayout) findViewById(R.id.llProgress);
		llProgress.addView(myprogressBar);

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void fillDate() {
		webView.getSettings().setJavaScriptEnabled(true);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.requestFocus();// 使WebView内的输入框等获得焦点
		webView.getSettings().setBuiltInZoomControls(true);// 设置支持缩放
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);// 屏幕自适应网页，如果没有这个在低分辨率手机上显示会异常
		webView.loadUrl(BangQuUrl);
		webView.setDownloadListener(new MyWebViewDownLoadListener());
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new WebChromeClient());

	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(View.GONE);
			} else {
				if (progressbar.getVisibility() == View.GONE)
					progressbar.setVisibility(view.VISIBLE);
				myprogressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	// 内部类
	public class MyWebViewClient extends WebViewClient {
		// 如果页面中链接，如果希望点击链接继续在当前browser中响应，
		// 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。
		public boolean shouldOverviewUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// showProgressDialog();
			progressbar.setVisibility(View.VISIBLE);
		}

		public void onPageFinished(WebView view, String url) {
			// closeProgressDialog();
			progressbar.setVisibility(View.GONE);
		}

		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// closeProgressDialog();
			progressbar.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			webView.goBack(); // goBack()表示返回webView的上一页面，而不直接关闭WebView
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			UrlWebView.this.finish();
			break;

		default:
			break;
		}
	}

	// 内部类
	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Toast t = Toast.makeText(UrlWebView.this, "暂无SD卡",
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
				return;
			}
			DownloaderTask task = new DownloaderTask();
			task.execute(url);
			Toast.makeText(UrlWebView.this, "下载中……", Toast.LENGTH_LONG).show();

			// notification.icon = R.drawable.ic;
			// view.setImageViewResource(R.id.image, R.drawable.ic);//
			// 启用一个线程来更新progress
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// for (int i = 0; i < 100; i++) {
			// progress = (i + 1) * 5;
			// try {
			// Thread.sleep(1000 * 1);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// Message msg = new Message();
			// handle.sendMessage(msg);
			// }
			//
			// }
			// }).start();
		}
	}

	// 内部类
	private class DownloaderTask extends AsyncTask<String, Void, String> {

		public DownloaderTask() {
		}

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			// Log.i("tag", "url="+url);
			String fileName = url.substring(url.lastIndexOf("/") + 1);
			fileName = URLDecoder.decode(fileName);
			Log.i("tag", "fileName=" + fileName);

			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, fileName);
			if (file.exists()) {
				Log.i("tag", "The file has already exists.");
				return fileName;
			}
			try {
				HttpClient client = new DefaultHttpClient();
				// client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时
				HttpGet get = new HttpGet(url);
				HttpResponse response = client.execute(get);
				if (HttpStatus.SC_OK == response.getStatusLine()
						.getStatusCode()) {
					HttpEntity entity = response.getEntity();
					InputStream input = entity.getContent();

					writeToSDCard(fileName, input);

					input.close();
					// entity.consumeContent();
					return fileName;
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// closeProgressDialog();
			progressbar.setVisibility(View.GONE);
			if (result == null) {
				Toast t = Toast.makeText(UrlWebView.this, "连接错误！请稍后再试！",
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
				return;
			}

			// Toast t = Toast.makeText(UrlWebView.this, "已保存到SD卡。",
			// Toast.LENGTH_LONG);
			// t.setGravity(Gravity.CENTER, 0, 0);
			// t.show();
			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, result);
			Log.i("tag", "Path=" + file.getAbsolutePath());

			Intent intent = getFileIntent(file);

			startActivity(intent);

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// showProgressDialog();
			progressbar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

	}

	private ProgressDialog mDialog;

	private void showProgressDialog() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(UrlWebView.this);
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
			mDialog.setMessage("正在加载 ，请等待...");
			mDialog.setIndeterminate(false);// 设置进度条是否为不明确
			mDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					mDialog = null;
				}
			});
			mDialog.show();
		}
	}

	private void closeProgressDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public Intent getFileIntent(File file) {
		// Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
		Uri uri = Uri.fromFile(file);
		String type = getMIMEType(file);
		Log.i("tag", "type=" + type);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, type);
		return intent;
	}

	public void writeToSDCard(String fileName, InputStream input) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, fileName);
			// if(file.exists()){
			// Log.i("tag", "The file has already exists.");
			// return;
			// }
			try {
				FileOutputStream fos = new FileOutputStream(file);
				byte[] b = new byte[2048];
				int j = 0;
				while ((j = input.read(b)) != -1) {
					fos.write(b, 0, j);
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.i("tag", "NO SDCard.");
		}
	}

	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 依扩展名的类型决定MimeType */
		if (end.equals("pdf")) {
			type = "application/pdf";//
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio/*";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video/*";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image/*";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		}
		// else if(end.equals("pptx")||end.equals("ppt")){
		// type = "application/vnd.ms-powerpoint";
		// }else if(end.equals("docx")||end.equals("doc")){
		// type = "application/vnd.ms-word";
		// }else if(end.equals("xlsx")||end.equals("xls")){
		// type = "application/vnd.ms-excel";
		// }
		else {
			// /*如果无法直接打开，就跳出软件列表给用户选择 */
			type = "*/*";
		}
		return type;
	}

}