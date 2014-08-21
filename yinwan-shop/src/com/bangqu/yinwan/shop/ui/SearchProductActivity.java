package com.bangqu.yinwan.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.util.StringUtil;

public class SearchProductActivity extends UIBaseActivity implements
		OnClickListener, OnCheckedChangeListener {
	private Button btnBack;
	private EditText etSearch;
	private ImageView ivProductSearch;
	private Button btnSearch;

	// 清空搜索框的
	private Button ivDeleteSearch;

	// 商品搜索
	private List<ProductBean> productList = new ArrayList<ProductBean>();

	// 暂无搜索结果
	private LinearLayout llNoData;

	private ListView lvProductList;
	private MyListAdapter mylistAdapter;

	static class ViewHolder {
		ImageView ivProductimg;
		TextView tvProductName;
		Button btnSelect;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_product_layout);

		findView();
		fillData();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		etSearch.setText(Constants.NewProductName);
		Constants.NewProductName = "";
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		super.findView();
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		ivProductSearch = (ImageView) findViewById(R.id.ivProductSearch);
		ivProductSearch.setOnClickListener(this);
		ivDeleteSearch = (Button) findViewById(R.id.ivDeleteSearch);
		ivDeleteSearch.setOnClickListener(this);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);
		etSearch = (EditText) findViewById(R.id.etSearch);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				lvProductList.setVisibility(View.GONE);
				// llNoData.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				new LoadProductSearchTask("true", etSearch.getText().toString()
						.trim(), etSearch.getText().toString().trim())
						.execute();
			}
		});

		// ListView
		lvProductList = (ListView) findViewById(R.id.lvProductList);
		mylistAdapter = new MyListAdapter(SearchProductActivity.this);
		lvProductList.setAdapter(mylistAdapter);
	}

	@Override
	public void fillData() {
		// TODO Auto-generated method stub
		super.fillData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBack:
			SearchProductActivity.this.finish();
			break;
		case R.id.ivDeleteSearch:
			etSearch.setText("");
			break;
		case R.id.ivProductSearch:
			String SearchName = etSearch.getText().toString().trim();
			if (!StringUtil.isBlank(SearchName)) {
				System.out.println("获取输入文字，调用搜索接口");
				new LoadProductSearchTask("true", SearchName, SearchName)
						.execute();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 搜索商品列表适配器(ListView)
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyListAdapter extends BaseAdapter {
		private Context mContext;

		public MyListAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return productList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
			// TODO Auto-generated method stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.search_product_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.ivProductimg = (ImageView) convertView
						.findViewById(R.id.ivProductimg);
				viewHolder.tvProductName = (TextView) convertView
						.findViewById(R.id.tvProductName);
				viewHolder.btnSelect = (Button) convertView
						.findViewById(R.id.btnSelect);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productList.get(position).getImg(),
							viewHolder.ivProductimg);
			viewHolder.tvProductName.setText(productList.get(position)
					.getName());
			viewHolder.btnSelect.setTag(position);
			viewHolder.btnSelect.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Integer index = (Integer) arg0.getTag();
					Intent productIntent = new Intent(
							SearchProductActivity.this,
							ProductAddActivity.class);
					productIntent.putExtra("JIESHOUproductlist",
							productList.get(index));
					Constants.IS_SELF = false;
					startActivity(productIntent);
				}
			});

			return convertView;
		}
	}

	/*
	 * 搜索下拉框列表(商品搜索)
	 */
	class LoadProductSearchTask extends AsyncTask<String, Void, JSONObject> {
		private String nameOrNumber;
		private String name;
		private String number;

		public LoadProductSearchTask(String nameOrNumber, String name,
				String number) {
			this.nameOrNumber = nameOrNumber;
			this.name = name;
			this.number = number;
		}

		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productSearch(nameOrNumber, name,
						number);
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
						List<ProductBean> temp = ProductBean
								.constractList(result.getJSONArray("products"));
						productList.clear();

						productList = temp;
						mylistAdapter.notifyDataSetChanged();
						lvProductList.setVisibility(View.VISIBLE);
						// llNoData.setVisibility(View.GONE);

					} else if (result.getInt("status") == Constants.FAIL) {
						lvProductList.setVisibility(View.GONE);
						// llNoData.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					e.printStackTrace();
					Log.i("ProductListActivity", "SystemException");
				}
			} else {
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}
}
