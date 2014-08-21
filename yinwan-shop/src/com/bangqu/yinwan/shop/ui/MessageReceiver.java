package com.bangqu.yinwan.shop.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.tencent.android.tpush.XGBasicPushNotificationBuilder;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MessageReceiver extends XGPushBaseReceiver {
	String strtext;
	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
	// private Intent intent = new
	// Intent("com.tencent.android.tpush.action.PUSH_MESSAGE");
	private Intent intent2 = new Intent(
			"android.content.Context.NOTIFICATION_SERVICE");
	public static final String LogTag = "TPushReceiver";

	private void show(Context context, String text) {
		// Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

	}

	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {

			return;
		}
		XGBasicPushNotificationBuilder build = new XGBasicPushNotificationBuilder();
		// 设置自定义样式属性，该属性对对应的编号生效，指定后不能修改。
		build.setIcon(R.drawable.push_bg);
		build.setSound(Uri.parse("android.resource://"
				+ context.getPackageName() + "/" + R.raw.keguan));// 设置声音
		
		build.setDefaults(Notification.DEFAULT_VIBRATE); // 振动

		// build.setFlags(Notification.FLAG_AUTO_CANCEL); // 是否可清除
		XGPushManager.setPushNotificationBuilder(context, 2, build);

		XGBasicPushNotificationBuilder build2 = new XGBasicPushNotificationBuilder();
		// 设置自定义样式属性，该属性对对应的编号生效，指定后不能修改。
		build2.setIcon(R.drawable.push_bg);
		build2.setDefaults(Notification.DEFAULT_SOUND); // 声音
		build2.setDefaults(Notification.DEFAULT_VIBRATE); // 振动

		// 设置通知样式，样式编号为2，即build_id为2，可通过后台脚本指定
		XGPushManager.setPushNotificationBuilder(context, 4, build2);
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "反注册成功";
		} else {
			text = "反注册失败" + errorCode;
		}
		// Log.d(LogTag, text);
		// show(context, text);
	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
		// Log.d(LogTag, text);
		// show(context, text);

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}
		// Log.d(LogTag, text);
		// show(context, text);

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = null;
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// text = "通知被打开 :" + message;
			Constants.NOTIFICATION = true;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			// text = "通知被清除 :" + message;
		}

		// Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
		// Toast.LENGTH_SHORT).show();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理的过程。。。
		// Log.d(LogTag, text);
		// show(context, text);
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			String token = message.getToken();
		} else {
			text = message + "注册失败，错误码：" + errorCode;
		}
		// Log.d(LogTag, text);
		// show(context, text);
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "收到消息:" + message.toString();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					// Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理消息的过程...
		// Log.d(LogTag, text);
		// show(context, text);
	}

}
