package com.bangqu.yinwan.shop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.bean.ProductImgBean;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.helper.ShopHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.qiniu.IO;
import com.bangqu.yinwan.shop.qiniu.JSONObjectRet;
import com.bangqu.yinwan.shop.qiniu.PutExtras;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

public class ProductUpdateActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;
	private String id;
	private String info;
	private Button btnxiajia;
	private Button btnSaveUpdate;

	private EditText etProductName;
	private String tvProductCategoryID = "";
	private LinearLayout llVipPrice;
	private ImageView ivProImg1;
	private ImageView ivProImg2;
	private ImageView ivProImg3;
	private ImageView ivProImg4;
	private ImageView ivProImg5;
	private ImageView ivProImgAdd;
	private TextView tvProductUnit;
	private EditText etProductPrice;
	private EditText etVipPrice;
	private EditText etProductDetail;

	private int intresult = 0;

	private TextView tvProductCategory;

	private ProductBean productBean;
	private List<ProductImgBean> productImgList = new ArrayList<ProductImgBean>();
	public static final int CATEGORY_SELECT = 11210;

	private Boolean isChanged = false;
	// 商品单位
	private String[] unit = { "个", "件", "盒", "斤", "公斤", "克", "箱", "包", "项",
			"份", "瓶", "次", "只", "套", "台", "条", "两", "打", "组", "杯", "片", "块" };
	private int defaultItem = 0;
	// qiniu Start
	private int TouchNum = 0;
	private int ImgUriX;
	// 图片的临时地址
	private String ImgUri1 = "";
	private String ImgUri2 = "";
	private String ImgUri3 = "";
	private String ImgUri4 = "";
	private String ImgUri5 = "";
	private int ImgSum = 0;
	private String strunit = "";
	// 商品主图片、所有图片地址
	private String MainImgUri = "";
	private String ImgUri = "";
	// 在七牛绑定的对应bucket的域名. 默认是bucket.qiniudn.com
	// public static String bucketName = "bucketName";
	public static String bucketName = "yinwan";
	public static String domain = bucketName + ".qiniudn.com";
	// upToken 这里需要自行获取. SDK 将不实现获取过程. 当token过期后才再获取一遍
	public String uptoken = "";
	public String TimeName = "";
	public String backUri = "";
	Uri finalUri = Uri.parse("file:///sdcard/temp.jpg");
	// Uri finalUri = Uri.parse(Environment.getExternalStorageDirectory() +
	// "/temp.jpg");
	public static final int TAKE_WITH_CAMERA = 3023;
	public static final int PICK_IMAGE_VIEW = 3020;
	public static final int PICTURE_LOCAL = 0;
	public static final int LOCATION_ACTION = 10;
	Bitmap cameraBitmap = null;

	int image = -1;
	private LoadingCircleView ivloadingone;
	private LoadingCircleView ivloadingtwo;
	private LoadingCircleView ivloadingthr;
	private LoadingCircleView ivloadingfour;
	private LoadingCircleView ivloadingfive;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ivloadingone.setProgress(msg.what);
			ivloadingtwo.setProgress(msg.what);
			ivloadingthr.setProgress(msg.what);
			ivloadingfour.setProgress(msg.what);
			ivloadingfive.setProgress(msg.what);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_update_layout);
		tvProductCategoryID = getIntent().getStringExtra("productcategoruIs");
		findView();
		id = getIntent().getStringExtra("ProductId");
		new LoadProductViewTask(id).execute();
		new LoadProductImgViewTask(id).execute();

	}

	public void findView() {
		super.findView();
		ivloadingone = (LoadingCircleView) findViewById(R.id.ivloadingone);
		ivloadingtwo = (LoadingCircleView) findViewById(R.id.ivloadingtwo);
		ivloadingthr = (LoadingCircleView) findViewById(R.id.ivloadingthr);
		ivloadingfour = (LoadingCircleView) findViewById(R.id.ivloadingfour);
		ivloadingfive = (LoadingCircleView) findViewById(R.id.ivloadingfive);
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		info = getIntent().getStringExtra("info");
		tvTittle.setText(info);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnSaveUpdate = (Button) findViewById(R.id.btnSaveUpdate);
		btnxiajia = (Button) findViewById(R.id.btnxiajia);

		etProductName = (EditText) findViewById(R.id.etProductName);
		tvProductCategory = (TextView) findViewById(R.id.tvProductCategory);
		tvProductCategory.setOnClickListener(this);

		ivProImg1 = (ImageView) findViewById(R.id.ivProImg1);
		ivProImg2 = (ImageView) findViewById(R.id.ivProImg2);
		ivProImg3 = (ImageView) findViewById(R.id.ivProImg3);
		ivProImg4 = (ImageView) findViewById(R.id.ivProImg4);
		ivProImg5 = (ImageView) findViewById(R.id.ivProImg5);
		ivProImgAdd = (ImageView) findViewById(R.id.ivProImgAdd);

		ivProImg1.setOnClickListener(this);
		ivProImg2.setOnClickListener(this);
		ivProImg3.setOnClickListener(this);
		ivProImg4.setOnClickListener(this);
		ivProImg5.setOnClickListener(this);
		ivProImgAdd.setOnClickListener(this);

		etProductPrice = (EditText) findViewById(R.id.etProductPrice);
		tvProductUnit = (TextView) findViewById(R.id.tvProductUnit);
		etProductDetail = (EditText) findViewById(R.id.etProductDetail);
		etVipPrice = (EditText) findViewById(R.id.etVipPriceo);
		llVipPrice = (LinearLayout) findViewById(R.id.llVipPrice);
		llVipPrice.setVisibility(View.VISIBLE);
		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);
		btnSaveUpdate.setOnClickListener(this);
		btnxiajia.setOnClickListener(this);

	}

	private void TextChanged() {
		etProductName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				isChanged = true;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		tvProductCategory.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				isChanged = true;

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		etProductPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				isChanged = true;

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		tvProductUnit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				isChanged = true;

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		etProductDetail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				isChanged = true;

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		etVipPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				isChanged = true;

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	}

	public void fillData() {
		super.fillData();
		ImgSum = productImgList.size();
		switch (ImgSum) {
		case 1:
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(0).getImg(), ivProImg1);
			ImgUri1 = productImgList.get(0).getImg() + ",";
			MainImgUri = ImgUri1;
			ImgUri = ImgUri1;
			break;
		case 2:
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(0).getImg(), ivProImg1);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(1).getImg(), ivProImg2);
			ImgUri1 = productImgList.get(0).getImg() + ",";
			ImgUri2 = productImgList.get(1).getImg() + ",";
			MainImgUri = ImgUri1;
			ImgUri = ImgUri1 + ImgUri2;
			break;
		case 3:
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(0).getImg(), ivProImg1);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(1).getImg(), ivProImg2);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(2).getImg(), ivProImg3);
			ImgUri1 = productImgList.get(0).getImg() + ",";
			ImgUri2 = productImgList.get(1).getImg() + ",";
			ImgUri3 = productImgList.get(2).getImg() + ",";
			MainImgUri = ImgUri1;
			ImgUri = ImgUri1 + ImgUri2 + ImgUri3;
			break;
		case 4:
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(0).getImg(), ivProImg1);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(1).getImg(), ivProImg2);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(2).getImg(), ivProImg3);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(3).getImg(), ivProImg4);
			ImgUri1 = productImgList.get(0).getImg() + ",";
			ImgUri2 = productImgList.get(1).getImg() + ",";
			ImgUri3 = productImgList.get(2).getImg() + ",";
			ImgUri4 = productImgList.get(3).getImg() + ",";
			MainImgUri = ImgUri1;
			ImgUri = ImgUri1 + ImgUri2 + ImgUri3 + ImgUri4;
			break;
		case 5:
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(0).getImg(), ivProImg1);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(1).getImg(), ivProImg2);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(2).getImg(), ivProImg3);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(3).getImg(), ivProImg4);
			((CommonApplication) getApplicationContext()).getImgLoader()
					.DisplayImage(productImgList.get(4).getImg(), ivProImg5);
			ImgUri1 = productImgList.get(0).getImg() + ",";
			ImgUri2 = productImgList.get(1).getImg() + ",";
			ImgUri3 = productImgList.get(2).getImg() + ",";
			ImgUri4 = productImgList.get(3).getImg() + ",";
			ImgUri5 = productImgList.get(4).getImg() + ",";

			ImgUri = ImgUri1 + ImgUri2 + ImgUri3 + ImgUri4 + ImgUri5;
			if (!ImgUri1.equals("")) {
				MainImgUri = ImgUri1;
			} else if (!ImgUri2.equals("")) {
				MainImgUri = ImgUri2;
			} else if (!ImgUri3.equals("")) {
				MainImgUri = ImgUri3;
			} else if (!ImgUri4.equals("")) {
				MainImgUri = ImgUri4;
			} else if (!ImgUri5.equals("")) {
				MainImgUri = ImgUri5;
			}

			break;

		default:
			break;
		}
		etProductName.setText(productBean.getName());

		etProductPrice.setText(productBean.getPrice());
		if (!(productBean.getProductCategory() + "").equals("null")) {
			tvProductCategory.setText(productBean.getProductCategory()
					.getName());
		}
		if (!StringUtil.isBlank(productBean.getVipPrice())) {
			etVipPrice.setText(productBean.getVipPrice());
		}

		tvProductUnit.setText(productBean.getUnit());
		tvProductUnit.setOnClickListener(this);
		if (!StringUtil.isBlank(productBean.getContent())) {
			etProductDetail.setText(productBean.getContent());

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		new loadGetTokenTask().execute();
		new loadGetTimeNameTask().execute();
	}

	public static final int CHOOSE_CATEGORY = 1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivProImg1:
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setTitle("图片操作");
			builder1.setItems(new String[] { "上传", "删除" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:// 上传
								ImgUriX = 1;
								image = 1;
								uploadimg();
								break;
							case 1:// 删除
								if (!ImgUri1.equals("")) {
									// 删除操作代码
									ivProImg1
											.setImageResource(R.drawable.addpro);
									ImgUri1 = "";
									Constants.IMG_SUCCESS = true;
									Toast.makeText(ProductUpdateActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductUpdateActivity.this,
											"暂无图片，请先添加！", Toast.LENGTH_SHORT)
											.show();
								}
								break;
							}
						}
					});

			builder1.setNegativeButton("取消", null);
			builder1.show();
			break;
		case R.id.ivProImg2:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("图片操作");
			builder2.setItems(new String[] { "上传", "删除" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:// 上传
								ImgUriX = 2;
								image = 2;
								uploadimg();
								break;
							case 1:// 删除
								if (!ImgUri2.equals("")) {
									// 删除操作代码
									ivProImg2
											.setImageResource(R.drawable.addpro);
									ImgUri2 = "";
									Toast.makeText(ProductUpdateActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductUpdateActivity.this,
											"暂无图片，请先添加！", Toast.LENGTH_SHORT)
											.show();
								}
								break;
							}
						}
					});

			builder2.setNegativeButton("取消", null);
			builder2.show();
			break;
		case R.id.ivProImg3:
			AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
			builder3.setTitle("图片操作");
			builder3.setItems(new String[] { "上传", "删除" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:// 上传
								ImgUriX = 3;
								image = 3;
								uploadimg();
								break;
							case 1:// 删除
								if (!ImgUri3.equals("")) {
									// 删除操作代码
									ivProImg3
											.setImageResource(R.drawable.addpro);
									ImgUri3 = "";
									Toast.makeText(ProductUpdateActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductUpdateActivity.this,
											"暂无图片，请先添加！", Toast.LENGTH_SHORT)
											.show();
								}
								break;
							}
						}
					});

			builder3.setNegativeButton("取消", null);
			builder3.show();
			break;
		case R.id.ivProImg4:
			AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
			builder4.setTitle("图片操作");
			builder4.setItems(new String[] { "上传", "删除" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:// 上传
								ImgUriX = 4;
								image = 4;
								uploadimg();
								break;
							case 1:// 删除
								if (!ImgUri4.equals("")) {
									// 删除操作代码
									ivProImg4
											.setImageResource(R.drawable.addpro);
									ImgUri4 = "";
									Toast.makeText(ProductUpdateActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductUpdateActivity.this,
											"暂无图片，请先添加！", Toast.LENGTH_SHORT)
											.show();
								}
								break;
							}
						}
					});

			builder4.setNegativeButton("取消", null);
			builder4.show();
			break;
		case R.id.ivProImg5:
			AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
			builder5.setTitle("图片操作");
			builder5.setItems(new String[] { "上传", "删除" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:// 上传
								ImgUriX = 5;
								image = 5;
								uploadimg();
								break;
							case 1:// 删除
								if (!ImgUri5.equals("")) {
									// 删除操作代码
									ivProImg5
											.setImageResource(R.drawable.addpro);
									ImgUri5 = "";
									Toast.makeText(ProductUpdateActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductUpdateActivity.this,
											"暂无图片，请先添加！", Toast.LENGTH_SHORT)
											.show();
								}
								break;
							}
						}
					});

			builder5.setNegativeButton("取消", null);
			builder5.show();
			break;

		case R.id.rlshop:
			Intent intentshop = new Intent(ProductUpdateActivity.this,
					HomeMoreShopActivity.class);
			startActivity(intentshop);
			break;
		case R.id.btnLeft:
			System.out.println(isChanged);
			if (isChanged) {
				AlertDialog.Builder backbuilder = new AlertDialog.Builder(
						ProductUpdateActivity.this);
				backbuilder.setTitle("要放弃修改商品吗？");
				backbuilder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});
				backbuilder.setNegativeButton("否",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				backbuilder.create().show();
			} else {
				finish();
			}

			break;
		case R.id.tvProductCategory:
			Intent categoryintent = new Intent(ProductUpdateActivity.this,
					ProductCategoryListActivity.class);
			categoryintent.putExtra("IntentValue", "notfromhome");
			startActivityForResult(categoryintent, CATEGORY_SELECT);
			break;
		case R.id.tvProductUnit:
			// 风格
			// buildere.setAdapter(new ArrayAdapter<String>(this,
			// android.R.layout.simple_list_item_1, unit),

			final View view = (LinearLayout) getLayoutInflater().inflate(
					R.layout.alertdialog_uitl, null);
			final EditText searchC = (EditText) view.findViewById(R.id.searchC);
			AlertDialog.Builder buildere = new AlertDialog.Builder(this);
			buildere.setTitle("单位选择");
			buildere.setSingleChoiceItems(unit, // 单选框有几项,各是什么名字
					defaultItem, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							defaultItem = which;
							tvProductUnit.setText(unit[which]);
							strunit = unit[which];
							dialog.dismiss();
						}
					});
			buildere.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			buildere.setPositiveButton("确定",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							System.out.println(searchC.getText().toString()
									.trim());
							if (StringUtil.isBlank(searchC.getText().toString()
									.trim())) {
								if (StringUtil.isBlank(strunit)) {
									if (!StringUtil.isBlank(productBean
											.getUnit())) {
										tvProductUnit.setText(productBean
												.getUnit());
									} else {
										tvProductUnit.setText("");
									}
								} else {
									tvProductUnit.setText(strunit);
								}

							} else {
								tvProductUnit.setText(searchC.getText()
										.toString().trim());
							}
						}
					});
			buildere.setView(view);
			buildere.create().show();

			break;
		case R.id.ivProImgAdd:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("上传商品图片");
			builder.setItems(new String[] { "拍摄", "手机相册" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:// 相机拍摄
								Constants.IMG_SUCCESS = false;
								Intent takephoto = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								takephoto.putExtra(MediaStore.EXTRA_OUTPUT,
										finalUri);
								startActivityForResult(takephoto,
										TAKE_WITH_CAMERA);
								break;
							case 1:// 手机相册
								Constants.IMG_SUCCESS = false;
								Intent intent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(intent, PICK_IMAGE_VIEW);
								break;
							}
						}
					});
			builder.setNegativeButton("取消", null);
			builder.show();
			break;
		case R.id.btnxiajia:
			AlertDialog.Builder builder6 = new AlertDialog.Builder(
					ProductUpdateActivity.this);
			builder6.setTitle("删除");
			builder6.setMessage("确定删除此商品么？");
			builder6.setPositiveButton("确定",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							new LoadProductDeleteTask(SharedPrefUtil
									.getToken(ProductUpdateActivity.this), id)
									.execute();
						}
					});
			builder6.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder6.create().show();
			break;

		case R.id.btnSaveUpdate:

			if (StringUtil.isBlank(etProductName.getText().toString().trim())) {
				Toast.makeText(ProductUpdateActivity.this, "请填写商品名称",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(etProductPrice.getText().toString().trim())) {
				Toast.makeText(ProductUpdateActivity.this, "请输入店铺价格",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(etVipPrice.getText().toString().trim())) {
				Toast.makeText(ProductUpdateActivity.this, "请输入会员价格",
						Toast.LENGTH_LONG).show();
				return;
			}
			String strProductName = etProductName.getText().toString().trim();
			String strproductCategoryId = tvProductCategoryID;
			String strProductPrice = etProductPrice.getText().toString().trim();

			String strVipPrice = etVipPrice.getText().toString().trim();
			String strProductunit = tvProductUnit.getText().toString().trim();
			String strProductDetail = etProductDetail.getText().toString()
					.trim();

			if (Double.parseDouble(strVipPrice) > Double
					.parseDouble(strProductPrice)) {
				Toast.makeText(ProductUpdateActivity.this, "会员价不能高于店铺价",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!ImgUri1.equals("")) {
				MainImgUri = ImgUri1;
			} else if (!ImgUri2.equals("")) {
				MainImgUri = ImgUri2;
			} else if (!ImgUri3.equals("")) {
				MainImgUri = ImgUri3;
			} else if (!ImgUri4.equals("")) {
				MainImgUri = ImgUri4;
			} else if (!ImgUri5.equals("")) {
				MainImgUri = ImgUri5;
			}
			ImgUri = ImgUri1 + ImgUri2 + ImgUri3 + ImgUri4 + ImgUri5;

			if (Constants.IMG_SUCCESS) {
				new LoadProductImgUpdateTask(id, ImgUri).execute();
				new LoadProductUpdateTask(SharedPrefUtil.getToken(this),
						getIntent().getStringExtra("ProductId"),
						strProductName, strproductCategoryId, strProductPrice,
						strProductunit, strProductDetail, strVipPrice)
						.execute();
				System.out.println(strProductDetail);
				System.out.println(getIntent().getStringExtra("ProductId")
						+ "IDS呼出");

			} else {
				Toast.makeText(ProductUpdateActivity.this, "请等待图片后台上传完成",
						Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	private void uploadimg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("上传商品图片");
		builder.setItems(new String[] { "相机拍摄", "手机相册" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:// 相机拍摄
							Constants.IMG_SUCCESS = false;
							Intent takephoto = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							takephoto.putExtra(MediaStore.EXTRA_OUTPUT,
									finalUri);
							startActivityForResult(takephoto, TAKE_WITH_CAMERA);
							break;
						case 1:// 手机相册
							Constants.IMG_SUCCESS = false;
							Intent intent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(intent, PICK_IMAGE_VIEW);
							break;
						}
					}
				});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isChanged) {
				AlertDialog.Builder backbuilder = new AlertDialog.Builder(
						ProductUpdateActivity.this);
				backbuilder.setTitle("要放弃修改商品吗？");
				backbuilder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});
				backbuilder.setNegativeButton("否",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				backbuilder.create().show();
			} else {
				finish();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void onTakePhotoFinished(int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			return;
		} else if (resultCode != RESULT_OK) {

		} else {
			doUpload(finalUri);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_WITH_CAMERA:// 拍照取得图片
			try {
				cropImage(finalUri);
			} catch (Exception e) {
				Constants.IMG_SUCCESS = true;
			}
			break;
		case PICK_IMAGE_VIEW:// 本地选取图片
			try {
				cropImage(data.getData());
			} catch (Exception e) {
				Constants.IMG_SUCCESS = true;
			}

			break;
		case PICTURE_LOCAL:
			// 获取裁剪后的图片,上传到七牛
			onTakePhotoFinished(resultCode, data);
			System.gc();
			break;

		case CATEGORY_SELECT:
			if (StringUtil.isBlank(Constants.ProductCategory)) {
				if (!StringUtil.isBlank(productBean.getProductCategory() + "")) {
					tvProductCategory.setText(productBean.getProductCategory()
							.getName());
				}

			} else {
				tvProductCategory.setText(Constants.ProductCategory);
				tvProductCategoryID = Constants.productCategoryId;
			}

			break;

		default:
			break;
		}
	}

	// 拍照后裁剪功能
	private void cropImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		//intent.putExtra("aspectX", 1);
		//intent.putExtra("aspectY", 1);
		//intent.putExtra("outputX", 800);
		//intent.putExtra("outputY", 800);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, finalUri);
		intent.putExtra("return-data", false);
		startActivityForResult(intent, PICTURE_LOCAL);
	}

	private Bitmap decodeUriAsBitmap(Uri imageUri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(imageUri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	boolean uploading = false;

	/**
	 * 七牛普通上传文件
	 * 
	 * @param uri
	 */
	private void doUpload(Uri uri) {
		if (uploading) {
			return;
		}

		uploading = true;
		String key = TimeName; // 自动生成key
		PutExtras extra = new PutExtras();
		extra.params = new HashMap<String, String>();
		IO.putFile(this, uptoken, key, uri, extra, new JSONObjectRet() {
			@Override
			public void onProcess(long current, long total) {

				loading(current, total);
				switch (image) {
				case 1:
					ivProImg1.setVisibility(View.GONE);
					ivloadingone.setVisibility(View.VISIBLE);
					break;
				case 2:
					ivProImg2.setVisibility(View.GONE);
					ivloadingtwo.setVisibility(View.VISIBLE);
					break;
				case 3:
					ivProImg3.setVisibility(View.GONE);
					ivloadingthr.setVisibility(View.VISIBLE);
					break;
				case 4:
					ivProImg4.setVisibility(View.GONE);
					ivloadingfour.setVisibility(View.VISIBLE);
					break;
				case 5:
					ivProImg5.setVisibility(View.GONE);
					ivloadingfive.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(JSONObject resp) {
				uploading = false;
				String key = resp.optString("key", "");
				String hash = resp.optString("hash", "");
				String value = resp.optString("x:a", "");
				backUri = "http://" + domain + "/" + key;
				Bitmap bitmap = decodeUriAsBitmap(finalUri);
				switch (image) {
				case 1:
					ivProImg1.setVisibility(View.VISIBLE);
					ivloadingone.setVisibility(View.GONE);
					break;
				case 2:
					ivProImg2.setVisibility(View.VISIBLE);
					ivloadingtwo.setVisibility(View.GONE);
					break;
				case 3:
					ivProImg3.setVisibility(View.VISIBLE);
					ivloadingthr.setVisibility(View.GONE);
					break;
				case 4:
					ivProImg4.setVisibility(View.VISIBLE);
					ivloadingfour.setVisibility(View.GONE);
					break;
				case 5:
					ivProImg5.setVisibility(View.VISIBLE);
					ivloadingfive.setVisibility(View.GONE);
					break;
				default:
					break;
				}
				ImgSum++;
				switch (ImgUriX) {
				case 1:
					ImgUri1 = backUri + ",";
					ivProImg1.setImageBitmap(bitmap);
					break;
				case 2:
					ImgUri2 = backUri + ",";
					ivProImg2.setImageBitmap(bitmap);
					break;
				case 3:
					ImgUri3 = backUri + ",";
					ivProImg3.setImageBitmap(bitmap);
					break;
				case 4:
					ImgUri4 = backUri + ",";
					ivProImg4.setImageBitmap(bitmap);
					break;
				case 5:
					ImgUri5 = backUri + ",";
					ivProImg5.setImageBitmap(bitmap);
					break;

				default:
					break;
				}
				Toast.makeText(ProductUpdateActivity.this, "图片上传成功",
						Toast.LENGTH_SHORT).show();
				System.gc();
				Constants.IMG_SUCCESS = true;
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/temp.jpg");
				f.delete();
			}

			@Override
			public void onFailure(Exception ex) {
				uploading = false;
				// 拍照但不进行裁剪，返回后若异常，不=TRUE会一直提示等待图片上传完毕
				Constants.IMG_SUCCESS = true;
				System.out.println("错误: " + ex.getMessage());
			}
		});
	}

	/**
	 * 获得七牛upToken
	 */
	class loadGetTokenTask extends AsyncTask<String, Void, JSONObject> {
		protected loadGetTokenTask() {
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopHelper().getToken();
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
						uptoken = result.getString("msg");

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	/**
	 * 获得七牛时间命名
	 */
	class loadGetTimeNameTask extends AsyncTask<String, Void, JSONObject> {
		protected loadGetTimeNameTask() {
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopHelper().getTimeName();
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
						TimeName = result.getString("msg");

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 修改商品信息
	 * 
	 * @author Administrator
	 */
	class LoadProductUpdateTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;
		private String productname;
		private String productCategoryId;
		private String productprice;
		private String productunit;
		private String productcontent;
		private String vipPrice;

		protected LoadProductUpdateTask(String accessToken, String id,
				String productname, String productCategoryId,
				String productprice, String productunit, String productcontent,
				String vipPrice) {
			this.accessToken = accessToken;
			this.id = id;
			this.productname = productname;
			this.productCategoryId = productCategoryId;
			this.productprice = productprice;
			this.productunit = productunit;
			this.productcontent = productcontent;
			this.vipPrice = vipPrice;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						ProductUpdateActivity.this, "正在保存修改……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productUpdate(accessToken, id,
						productname, productCategoryId, productprice,
						productunit, productcontent, vipPrice);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pd != null)
				pd.dismiss();
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(ProductUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_SHORT)
								.show();
						setResult(RESULT_OK);
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductUpdateActivity.this, "修改失败",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**
	 * 修改商品图片
	 * 
	 * @author Administrator
	 */
	class LoadProductImgUpdateTask extends AsyncTask<String, Void, JSONObject> {
		private String id;
		private String productUrls;

		protected LoadProductImgUpdateTask(String id, String productUrls) {
			this.id = id;
			this.productUrls = productUrls;
		}

		@Override
		protected void onPreExecute() {
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						ProductUpdateActivity.this, "正在保存修改……");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productImgUpdate(id, productUrls);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if (pd != null)
				pd.dismiss();
			if (result != null) {
				try {
					if (result.getInt("status") == Constants.SUCCESS) {
						Toast.makeText(ProductUpdateActivity.this, "修改成功",
								Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK);
						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductUpdateActivity.this, "修改失败",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("ProductListActivity", "JSONException");
				}
			}
		}
	}

	/**
	 * 预览商品信息
	 * 
	 * @author Administrator
	 */
	class LoadProductViewTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadProductViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productView(id);
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
						intresult = result.getInt("status");
						productBean = JSON.parseObject(
								result.getJSONObject("product").toString(),
								ProductBean.class);
						// fillData();
						// progressbar.setVisibility(View.GONE);
					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductUpdateActivity.this, "暂无数据",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_SHORT).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_SHORT).show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	/**
	 * 预览商品图片
	 * 
	 * @author Administrator
	 */
	class LoadProductImgViewTask extends AsyncTask<String, Void, JSONObject> {
		private String id;

		protected LoadProductImgViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productImgView(id);
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
						List<ProductImgBean> temp = ProductImgBean
								.constractList(result
										.getJSONArray("productImgs"));

						productImgList = temp;
						fillData();
						progressbar.setVisibility(View.GONE);
						TextChanged();
					} else if (result.getInt("status") == Constants.FAIL) {
						if (intresult == 1) {
							fillData();
							progressbar.setVisibility(View.GONE);
						} else {
							Toast.makeText(ProductUpdateActivity.this,
									"数据加载失败", Toast.LENGTH_SHORT).show();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_SHORT).show();
					Log.i("ProductListActivity", "JSONException");
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_SHORT).show();
				Log.i("ProductListActivity", "result==null");
			}

		}

	}

	/**
	 * 删除商品信息
	 * 
	 * @author Administrator
	 */
	class LoadProductDeleteTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;

		protected LoadProductDeleteTask(String accessToken, String id) {
			this.accessToken = accessToken;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().productDelete(accessToken, id);
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
						Toast.makeText(ProductUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
						setResult(RESULT_OK);
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductUpdateActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(ProductUpdateActivity.this, "数据加载失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void loading(final long current, final long total) {
		Thread t = new Thread() {
			public void run() {
				if (current <= total) {
					handler.sendEmptyMessage((int) (current / total * 100));

				}
				super.run();
			}
		};
		t.start();
		if ((current / total * 100) == 100) {

		}
	}
}
