package com.bangqu.yinwan.shop.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.bangqu.yinwan.shop.R;

public class myTimerDialog extends Dialog {
	// 分钟
	static String[] minuts = new String[] { "00", "30" };

	public myTimerDialog(Context mcontext) {
		super(mcontext);
	}

	public myTimerDialog(Context mcontext, int theme) {
		super(mcontext, theme);
	}

	public static class Builder {
		private Context context;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonTextClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonTextClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonTextClickListener = listener;
			return this;
		}

		public myTimerDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final myTimerDialog dialog = new myTimerDialog(context,
					R.style.deviceDialog);
			final View layout = inflater.inflate(R.layout.mytimerpicker_layout,
					null);
			TimePicker myTimePicker = ((TimePicker) layout
					.findViewById(R.id.timePicker1));
			myTimePicker.setIs24HourView(true);
			myTimePicker.setCurrentHour(0);
			myTimePicker.setCurrentMinute(0);
			setNumberPickerTextSize(myTimePicker);

			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.btnSave))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.btnSave))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
								}
							});
				}
			} else {
				layout.findViewById(R.id.btnSave).setVisibility(View.GONE);
			}
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.btnCancletime))
						.setText(negativeButtonText);
				if (negativeButtonTextClickListener != null) {
					((Button) layout.findViewById(R.id.btnCancletime))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonTextClickListener.onClick(
											dialog,
											DialogInterface.BUTTON_NEGATIVE);

								}
							});
				}
			} else {
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}

	/**
	 * 查找timePicker里面的android.widget.NumberPicker组件 ，并对其进行时间间隔设置
	 * 
	 * @param viewGroup
	 *            TimePicker timePicker
	 */
	public static void setNumberPickerTextSize(ViewGroup viewGroup) {
		List<NumberPicker> npList = findNumberPicker(viewGroup);
		if (null != npList) {
			for (NumberPicker mMinuteSpinner : npList) {
				// System.out.println("mMinuteSpinner.toString()="+mMinuteSpinner.toString());
				if (mMinuteSpinner.toString().contains("id/minute")) {// 对分钟进行间隔设置

					mMinuteSpinner.setMinValue(0);
					mMinuteSpinner.setMaxValue(minuts.length - 1);
					mMinuteSpinner.setDisplayedValues(minuts); // 分钟显示数组
				}
			}
		}
	}

	/**
	 * 得到timePicker里面的android.widget.NumberPicker组件
	 * （有两个android.widget.NumberPicker组件--hour，minute）
	 * 
	 * @param viewGroup
	 * @return
	 */
	private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;

		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}
		return npList;
	}

}
