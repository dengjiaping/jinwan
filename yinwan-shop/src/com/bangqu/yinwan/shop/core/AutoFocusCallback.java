package com.bangqu.yinwan.shop.core;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

final class AutoFocusCallback implements Camera.AutoFocusCallback {

	private static final String TAG = AutoFocusCallback.class.getSimpleName();
	private static final long AUTOFOCUS_INTERVAL_MS = 1500L;
	private Handler autoFocusHandler;
	private int autoFocusMessage;

	void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
		this.autoFocusHandler = autoFocusHandler;
		this.autoFocusMessage = autoFocusMessage;
	}

	public void onAutoFocus(boolean success, Camera camera) {
		if (autoFocusHandler != null) {
			Message message = autoFocusHandler.obtainMessage(autoFocusMessage,
					success);
			// 通过发送一个焦点请求每一autofocus_interval_ms毫秒模拟连续自动对焦。

			// Log.d(TAG, "Got auto-focus callback; requesting another");
			autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
			autoFocusHandler = null;
		} else {
			Log.d(TAG, "有自动对焦的回调，但没有处理它");
		}
	}

}
