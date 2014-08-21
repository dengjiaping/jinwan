/**************************************************************************************
 * [Project]
 *       MyProgressDialog
 * [Package]
 *       com.lxd.widgets
 * [FileName]
 *       CustomProgressDialog.java
 * [Copyright]
 *       Copyright 2012 LXD All Rights Reserved.
 * [History]
 *       Version          Date              Author                        Record
 *--------------------------------------------------------------------------------------
 *       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
 **************************************************************************************/

package com.bangqu.yinwan.shop.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.bangqu.yinwan.shop.R;

public class ProgressDialog extends Dialog {
	private Context context = null;
	private static ProgressDialog customProgressDialog = null;

	public ProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static ProgressDialog createLoadingDialog(Context context, String msg) {
		customProgressDialog = new ProgressDialog(context,
				R.style.loading_dialog);
		// customProgressDialog = new ProgressDialog(context);
		customProgressDialog.setContentView(R.layout.progress_dialog_layout);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);

		tvMsg.setText(msg);

		return customProgressDialog;
	}

	// public static Dialog createLoadingDialog(Context context, String msg) {
	//
	// LayoutInflater inflater = LayoutInflater.from(context);
	// View v = inflater.inflate(R.layout.progress_dialog_layout, null);//
	// 得到加载view
	// LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);//
	// 加载布局
	// // main.xml中的ImageView
	// ImageView spaceshipImage = (ImageView) v
	// .findViewById(R.id.loadingImageView);
	// TextView tipTextView = (TextView)
	// v.findViewById(R.id.id_tv_loadingmsg);// 提示文字
	// // // 加载动画
	// // Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
	// // context, R.anim.loading_animation);
	// // // 使用ImageView显示动画
	// // spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	// tipTextView.setText(msg);// 设置加载信息
	//
	// Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);//
	// 创建自定义样式dialog
	//
	// loadingDialog.setCancelable(true);// 不可以用“返回键”取消
	// loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.FILL_PARENT,
	// LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
	// return loadingDialog;
	//
	// }

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		// ImageView imageView = (ImageView) customProgressDialog
		// .findViewById(R.id.loadingImageView);
		// AnimationDrawable animationDrawable = (AnimationDrawable) imageView
		// .getBackground();
		// animationDrawable.start();
	}
	//
	// public ProgressDialog setTitile(String strTitle){
	// return customProgressDialog;
	// }
	//
	// public ProgressDialog setMessage(String strMessage){
	// TextView tvMsg =
	// (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
	//
	// if (tvMsg != null){
	// tvMsg.setText(strMessage);
	// }
	//
	// return customProgressDialog;
	// }
}
