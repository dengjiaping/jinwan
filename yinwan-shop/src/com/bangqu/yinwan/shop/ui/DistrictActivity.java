package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.DistrictBean;
import com.bangqu.yinwan.shop.helper.CityHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;

public class DistrictActivity extends UIBaseActivity implements
		OnItemClickListener, OnClickListener {
	private ListView lvdistrict;
	private List<DistrictBean> districtlist = new ArrayList<DistrictBean>();
	private FLListAdapter fllAdapter;
	private Button btnLeft;
	private Button btnRight;

	static class ViewHolder {
		TextView tvdistrict;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.district_layout);
		findView();
		districtlist.clear();
		new LoadDistrictListTask(getIntent().getStringExtra("cityid"))
				.execute();
	}

	@Override
	public void findView() {
		super.findView();
		lvdistrict = (ListView) findViewById(R.id.lvdistrict);
		fllAdapter = new FLListAdapter(this);
		lvdistrict.setAdapter(fllAdapter);
		lvdistrict.setOnItemClickListener(this);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(DistrictActivity.this);
	}

	/**
	 * 区县适配器
	 */
	private class FLListAdapter extends BaseAdapter {
		private Context FLContext;
		DistrictBean districtBean;

		public FLListAdapter(Context context) {
			this.FLContext = context;
		}

		public int getCount() {
			return districtlist.size();
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent1) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(FLContext).inflate(
						R.layout.district_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.tvdistrict = (TextView) convertView
						.findViewById(R.id.tvdistrict);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			districtBean = districtlist.get(position);
			viewHolder.tvdistrict.setText(districtBean.getName());
			return convertView;
		}
	}

	/**
	 * 区县
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadDistrictListTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadDistrictListTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new CityHelper().district(id);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						List<DistrictBean> temp = DistrictBean
								.constractList(result.getJSONArray("districts"));
						districtlist.addAll(temp);

						fllAdapter.notifyDataSetChanged();
						progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(DistrictActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(DistrictActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Toast.makeText(DistrictActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Toast.makeText(DistrictActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Constants.DISTRICTID = districtlist.get(arg2).getId();
		Constants.DISTRICTNANME = districtlist.get(arg2).getName();
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;

		default:
			break;
		}
	}

}
