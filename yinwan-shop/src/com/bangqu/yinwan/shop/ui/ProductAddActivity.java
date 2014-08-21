package com.bangqu.yinwan.shop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

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

import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ProductBean;
import com.bangqu.yinwan.shop.bean.ShopBean;
import com.bangqu.yinwan.shop.helper.ProductHelper;
import com.bangqu.yinwan.shop.helper.ShopHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.qiniu.IO;
import com.bangqu.yinwan.shop.qiniu.JSONObjectRet;
import com.bangqu.yinwan.shop.qiniu.PutExtras;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;

@SuppressLint("ShowToast")
public class ProductAddActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnLeft;
	private Button btnRight;
	private TextView tvTittle;

	private EditText etProductName;
	private TextView tvProductCategory;
	private EditText etShopPrice;
	private EditText etVIPPrice;
	private TextView tvProductUnit;
	private ImageView ivProImg1;
	private ImageView ivProImg2;
	private ImageView ivProImg3;
	private ImageView ivProImg4;
	private ImageView ivProImg5;
	private EditText etProductDetail;
	private LinearLayout llVip;

	private String tvProductCategoryId = "";

	private String isPromotion = "false";
	private String isGroupon = "false";
	// 商品单位
	private String[] unit = { "个", "件", "盒", "斤", "公斤", "克", "箱", "包", "项",
			"份", "瓶", "次", "只", "套", "台", "条", "两", "打", "组", "杯", "片", "块" };
	private int defaultItem = 0;

	// qiniu Start
	private int ImgUriX;
	// 图片的临时地址
	private String ImgUri1 = "";
	private String ImgUri2 = "";
	private String ImgUri3 = "";
	private String ImgUri4 = "";
	private String ImgUri5 = "";
	private int ImgSum = 0;
	// 商品主图片、所有图片地址
	private String MainImgUri = "";
	private String ImgUri = "";

	public static String bucketName = "yinwan";
	public static String domain = bucketName + ".qiniudn.com";
	public String uptoken = "";
	public String TimeName = "";
	public String backUri = "";
	@SuppressLint("SdCardPath")
	Uri finalUri = Uri.parse("file:///sdcard/temp.jpg");
	public static final int VIEW_IMAGE = 302223;
	public static final int TAKE_WITH_CAMERA = 3023;
	public static final int PICK_IMAGE_VIEW = 3020;
	public static final int PICTURE_LOCAL = 11;
	public static final int LOCATION_ACTION = 10;
	Bitmap cameraBitmap = null;

	// qiniu end
	public static final int SELECT_CATEGORY = 22233;
	private Button btnAddproduct;
	private ShopBean shopBean = new ShopBean();
	private ProductBean productBean;

	int image = -1;
	private LoadingCircleView loadingimageone;
	private LoadingCircleView loadingimagetwo;
	private LoadingCircleView loadingimagethr;
	private LoadingCircleView loadingimagefour;
	private LoadingCircleView loadingimagefive;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			loadingimageone.setProgress(msg.what);
			loadingimagetwo.setProgress(msg.what);
			loadingimagethr.setProgress(msg.what);
			loadingimagefour.setProgress(msg.what);
			loadingimagefive.setProgress(msg.what);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_add_layout);
		findView();
		if (!Constants.IS_SELF) {
			productBean = (ProductBean) getIntent().getSerializableExtra(
					"JIESHOUproductlist");
			fillData();
		}
		Constants.IS_SELF = true;
	}

	public void findView() {
		super.findView();
		loadingimageone = (LoadingCircleView) findViewById(R.id.loadingimageone);
		loadingimagetwo = (LoadingCircleView) findViewById(R.id.loadingimagetwo);
		loadingimagethr = (LoadingCircleView) findViewById(R.id.loadingimagethr);
		loadingimagefour = (LoadingCircleView) findViewById(R.id.loadingimagefour);
		loadingimagefive = (LoadingCircleView) findViewById(R.id.loadingimagefive);

		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("添加商品信息");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnAddproduct = (Button) findViewById(R.id.btnAddproduct);
		llVip = (LinearLayout) findViewById(R.id.llVip);
		llVip.setVisibility(View.VISIBLE);
		btnRight.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnAddproduct.setOnClickListener(this);

		etProductName = (EditText) findViewById(R.id.etProductName);
		tvProductCategory = (TextView) findViewById(R.id.tvProductCategory);
		tvProductCategory.setOnClickListener(this);
		etShopPrice = (EditText) findViewById(R.id.etShopPrice);
		etVIPPrice = (EditText) findViewById(R.id.etVIPPrice);
		tvProductUnit = (TextView) findViewById(R.id.tvProductUnit);
		tvProductUnit.setOnClickListener(this);

		ivProImg1 = (ImageView) findViewById(R.id.ivProImg1);
		ivProImg2 = (ImageView) findViewById(R.id.ivProImg2);
		ivProImg3 = (ImageView) findViewById(R.id.ivProImg3);
		ivProImg4 = (ImageView) findViewById(R.id.ivProImg4);
		ivProImg5 = (ImageView) findViewById(R.id.ivProImg5);

		ivProImg1.setOnClickListener(this);
		ivProImg2.setOnClickListener(this);
		ivProImg3.setOnClickListener(this);
		ivProImg4.setOnClickListener(this);
		ivProImg5.setOnClickListener(this);

		etProductDetail = (EditText) findViewById(R.id.etProductDetail);
	}

	public void fillData() {
		etProductName.setText(productBean.getName());

		tvProductUnit.setText(productBean.getUnit());
		etProductDetail.setText(productBean.getContent());
		ivProImg1.setVisibility(View.VISIBLE);
		((CommonApplication) getApplicationContext()).getImgLoader()
				.DisplayImage(productBean.getImg(), ivProImg1);
		ImgUri1 = productBean.getImg();
		MainImgUri = productBean.getImg();
		ImgSum++;
	}

	@Override
	protected void onResume() {
		super.onResume();
		new loadGetTokenTask().execute();
		new loadGetTimeNameTask().execute();
	}

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
								image = 1;
								ImgUriX = 1;
								uploadimg();
								break;
							case 1:// 删除
								if (!ImgUri1.equals("")) {
									// 删除操作代码
									ivProImg1
											.setImageResource(R.drawable.noneimg);
									ImgUri1 = "";
									Toast.makeText(ProductAddActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductAddActivity.this,
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
								image = 2;
								ImgUriX = 2;
								uploadimg();
								break;
							case 1:// 删除
								if (!ImgUri2.equals("")) {
									// 删除操作代码
									ivProImg2
											.setImageResource(R.drawable.noneimg);
									ImgUri2 = "";
									Toast.makeText(ProductAddActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductAddActivity.this,
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
											.setImageResource(R.drawable.noneimg);
									ImgUri3 = "";
									Toast.makeText(ProductAddActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductAddActivity.this,
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
											.setImageResource(R.drawable.noneimg);
									ImgUri4 = "";
									Toast.makeText(ProductAddActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductAddActivity.this,
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
											.setImageResource(R.drawable.noneimg);
									ImgUri5 = "";
									Toast.makeText(ProductAddActivity.this,
											"删除图片成功！", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(ProductAddActivity.this,
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
		case R.id.tvProductCategory:
			Intent categoryintent = new Intent(ProductAddActivity.this,
					ProductCategoryListActivity.class);
			categoryintent.putExtra("IntentValue", "notfromhome");
			startActivityForResult(categoryintent, SELECT_CATEGORY);
			break;
		case R.id.tvProductUnit:
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
							tvProductUnit.setText(searchC.getText().toString()
									.trim());
						}
					});
			buildere.setView(view);
			buildere.create().show();

			break;
		case R.id.btnLeft:
			AlertDialog.Builder backbuilder = new AlertDialog.Builder(
					ProductAddActivity.this);
			backbuilder.setTitle("要放弃添加商品吗？");
			backbuilder.setPositiveButton("是",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			backbuilder.setNegativeButton("否",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// 设置你的操作事项
						}
					});
			backbuilder.create().show();
			break;

		case R.id.btnAddproduct:
			String strProductName = etProductName.getText().toString().trim();

			String strProductDetail = etProductDetail.getText().toString()
					.trim();
			String strunit = tvProductUnit.getText().toString().trim();
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
			String strimg = MainImgUri;
			String strUrls = ImgUri;
			if (StringUtil.isBlank(strProductName)) {
				Toast.makeText(ProductAddActivity.this, "请填写商品名称",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(tvProductCategoryId)) {
				Toast.makeText(ProductAddActivity.this, "请选择商品类别",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 小数点后保留两位
			if (StringUtil.isBlank(etShopPrice.getText().toString().trim())) {
				Toast.makeText(ProductAddActivity.this, "请输入店铺价",
						Toast.LENGTH_LONG).show();
				return;
			}

			String strShopPrice = etShopPrice.getText().toString().trim();

			String strVIPPrice = etVIPPrice.getText().toString().trim();
			if (StringUtil.isBlank(etVIPPrice.getText().toString().trim())) {
				Toast.makeText(ProductAddActivity.this, "请输入会员价",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (Double.parseDouble(strVIPPrice) > Double
					.parseDouble(strShopPrice)) {
				Toast.makeText(ProductAddActivity.this, "会员价不能高于店铺价",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (Constants.IMG_SUCCESS) {
				new LoadAddProductTask(SharedPrefUtil.getToken(this),
						strProductName, strShopPrice, strVIPPrice, strunit,
						strimg, strUrls, SharedPrefUtil.getShopBean(this)
								.getId(), isPromotion, isGroupon,
						tvProductCategoryId, strProductDetail, "product")
						.execute();
				System.out.println(strProductDetail + "描述");
			} else {
				Toast.makeText(ProductAddActivity.this, "请等待图片上传完成",
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

	private void onTakePhotoFinished(int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			return;
		} else if (resultCode != RESULT_OK) {

		} else {
			Constants.IMG_SUCCESS = false;
			doUpload(finalUri);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_WITH_CAMERA:
			// 拍照取得图片
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

			onTakePhotoFinished(resultCode, data);

			System.gc();
			break;

		case SELECT_CATEGORY:
			tvProductCategoryId = Constants.productCategoryId;
			tvProductCategory.setText(Constants.ProductCategory);
			break;
		default:
			break;
		}
	}

	// 裁剪图片
	private void cropImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 800);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
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
			public void onProcess(final long current, final long total) {

				loading(current, total);
				switch (image) {
				case 1:
					loadingimageone.setVisibility(View.VISIBLE);
					ivProImg1.setVisibility(View.GONE);
					break;
				case 2:
					loadingimagetwo.setVisibility(View.VISIBLE);
					ivProImg2.setVisibility(View.GONE);
					break;
				case 3:
					loadingimagethr.setVisibility(View.VISIBLE);
					ivProImg3.setVisibility(View.GONE);
					break;

				case 4:
					loadingimagefour.setVisibility(View.VISIBLE);
					ivProImg4.setVisibility(View.GONE);
					break;

				case 5:
					loadingimagefive.setVisibility(View.VISIBLE);
					ivProImg5.setVisibility(View.GONE);

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

				Constants.IMG_SUCCESS = true;
				switch (image) {
				case 1:
					loadingimageone.setVisibility(View.GONE);
					ivProImg1.setVisibility(View.VISIBLE);
					break;

				case 2:
					loadingimagetwo.setVisibility(View.GONE);
					ivProImg2.setVisibility(View.VISIBLE);
					break;

				case 3:
					loadingimagethr.setVisibility(View.GONE);
					ivProImg3.setVisibility(View.VISIBLE);
					break;

				case 4:
					loadingimagefour.setVisibility(View.GONE);
					ivProImg4.setVisibility(View.VISIBLE);
					break;

				case 5:
					loadingimagefive.setVisibility(View.GONE);
					ivProImg5.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}

				Toast.makeText(ProductAddActivity.this, "图片上传成功",
						Toast.LENGTH_LONG).show();
				Bitmap bitmap = decodeUriAsBitmap(finalUri);
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
				// 如果图片还未回收，强制回收图片，释放内存
				// if (bitmap != null && !bitmap.isRecycled()) {
				// bitmap.recycle();
				// bitmap = null;
				// }
				System.gc();
				Constants.IMG_SUCCESS = true;
				File f = new File(Environment.getExternalStorageDirectory()
						+ "temp.jpg");
				f.delete();

			}

			@Override
			public void onFailure(Exception ex) {
				uploading = false;
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
						Toast.makeText(ProductAddActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductAddActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(ProductAddActivity.this, "数据加载失败",
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
						Toast.makeText(ProductAddActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductAddActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(ProductAddActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 添加商品
	 * 
	 * @author Administrator
	 */
	class LoadAddProductTask extends AsyncTask<String, Void, JSONObject> {

		private String accessToken;
		private String name;
		private String price;
		private String vipPrice;
		private String unit;
		private String img;
		private String productUrls;
		private String shopid;
		private String isPromotion;
		private String isGroupon;
		private String Categoryid;
		private String content;
		private String type;

		protected LoadAddProductTask(String accessToken, String name,
				String price, String vipPrice, String unit, String img,
				String productUrls, String shopid, String isPromotion,
				String isGroupon, String Categoryid, String content, String type) {
			this.accessToken = accessToken;
			this.name = name;
			this.price = price;
			this.vipPrice = vipPrice;
			this.unit = unit;
			this.img = img;
			this.productUrls = productUrls;
			this.shopid = shopid;
			this.isPromotion = isPromotion;
			this.isGroupon = isGroupon;
			this.Categoryid = Categoryid;
			this.content = content;
			this.type = type;
		}

		@Override
		protected void onPreExecute() {
			// loadPB.setVisibility(View.VISIBLE);
			if (pd == null) {
				pd = ProgressDialog.createLoadingDialog(
						ProductAddActivity.this, "正在添加");
			}
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ProductHelper().addProduct(accessToken, name, price,
						vipPrice, unit, img, productUrls, shopid, isPromotion,
						isGroupon, Categoryid, content, type);
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
						Toast.makeText(ProductAddActivity.this, "添加成功",
								Toast.LENGTH_LONG).show();
						setResult(RESULT_OK);
						// 添加商品成功之后把常量中的商品分类和商品分类ID清空
						Constants.ProductCategory = "";
						Constants.productCategoryId = "";
						finish();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ProductAddActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ProductAddActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ProductAddActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder backbuilder = new AlertDialog.Builder(
					ProductAddActivity.this);
			backbuilder.setTitle("要放弃添加商品吗？");
			backbuilder.setPositiveButton("是",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			backbuilder.setNegativeButton("否",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// 设置你的操作事项
						}
					});
			backbuilder.create().show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
