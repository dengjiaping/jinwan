package com.bangqu.yinwan.shop.control;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.ui.AssessActivity;
import com.bangqu.yinwan.shop.ui.HomeProductManageActivity;
import com.bangqu.yinwan.shop.ui.ProductCategoryListActivity;
import com.bangqu.yinwan.shop.ui.ProductListActivity;
import com.bangqu.yinwan.shop.ui.ProductSalesActivity;
import com.bangqu.yinwan.shop.ui.ServiceListActivity;

public class HomeProductMessageControl {
	private HomeProductManageActivity activity;

	public HomeProductMessageControl(HomeProductManageActivity activity) {
		this.activity = activity;
	}

	public OnClickListener getbackClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		};
	}

	public OnClickListener getintent() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tvassess:
					activity.startActivity(new Intent(activity,
							AssessActivity.class));
					break;
				case R.id.btnLeft:
					activity.finish();
					break;
				case R.id.tvproductManage:
					Intent productManageintent = new Intent(activity,
							ProductListActivity.class);
					activity.startActivity(productManageintent);
					break;
				case R.id.tvproductCategory:
					Intent productCategoryintent = new Intent(activity,
							ProductCategoryListActivity.class);
					productCategoryintent.putExtra("IntentValue", "fromhome");
					activity.startActivity(productCategoryintent);
					break;
				case R.id.tvxiaoliang:
					Intent xiaoliangintent = new Intent(activity,
							ProductSalesActivity.class);
					activity.startActivity(xiaoliangintent);
					break;
				case R.id.tvSevice:
					Intent seviceintent = new Intent(activity,
							ServiceListActivity.class);
					activity.startActivity(seviceintent);
					break;

				default:
					break;
				}
			}
		};
	}

}
