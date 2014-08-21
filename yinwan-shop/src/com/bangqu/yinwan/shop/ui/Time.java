package com.bangqu.yinwan.shop.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.UIBaseActivity;

public class Time extends UIBaseActivity {
	// 时间倒计时
	int minute = 0;
	int second = 0;
	final static String tag = "tag";
	private TextView timeView;
	Timer timer;
	TimerTask timerTask;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (minute == 0) {
				if (second == 0) {
					timeView.setText("Time out !");
					if (timer != null) {
						timer.cancel();
						timer = null;
					}
					if (timerTask != null) {
						timerTask = null;
					}
				} else {
					second--;
					if (second >= 10) {
						timeView.setText("0" + minute + ":" + second);
					} else {
						timeView.setText("0" + minute + ":0" + second);
					}
				}
			} else {
				if (second == 0) {
					second = 59;
					minute--;
					if (minute >= 10) {
						timeView.setText(minute + ":" + second);
					} else {
						timeView.setText("0" + minute + ":" + second);
					}
				} else {
					second--;
					if (second >= 10) {
						if (minute >= 10) {
							timeView.setText(minute + ":" + second);
						} else {
							timeView.setText("0" + minute + ":" + second);
						}
					} else {
						if (minute >= 10) {
							timeView.setText(minute + ":0" + second);
						} else {
							timeView.setText("0" + minute + ":0" + second);
						}
					}
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_layout);
		timeView = (TextView) findViewById(R.id.tvtime);
		minute = 59;
		second = 59;
		timeView.setText(minute + ":" + second);
		timerTask = new TimerTask() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		};

		timer = new Timer();
		timer.schedule(timerTask, 0, 1000);
	}

	protected void onDestroy() {
		Log.v(tag, "log---------->onDestroy!");
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask = null;
		}
		minute = -1;
		second = -1;
		super.onDestroy();
	}
}
