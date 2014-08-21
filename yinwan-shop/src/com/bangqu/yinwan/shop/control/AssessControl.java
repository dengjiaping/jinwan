package com.bangqu.yinwan.shop.control;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bangqu.yinwan.shop.ui.AssessActivity;

public class AssessControl {
	private AssessActivity assessActivity;

	public AssessControl(AssessActivity activity) {
		this.assessActivity = activity;
	};

	public OnClickListener getbackonclClickListener() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				assessActivity.finish();
			}
		};
	}

	public OnItemClickListener getOnitemClick() {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		};
	}
}
