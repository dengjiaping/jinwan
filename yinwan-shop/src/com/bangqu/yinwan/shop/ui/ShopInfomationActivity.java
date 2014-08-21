package com.bangqu.yinwan.shop.ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bangqu.yinwan.shop.R;
import com.bangqu.yinwan.shop.base.CommonApplication;
import com.bangqu.yinwan.shop.base.Constants;
import com.bangqu.yinwan.shop.base.UIBaseActivity;
import com.bangqu.yinwan.shop.bean.ShopBean;
import com.bangqu.yinwan.shop.helper.ShopHelper;
import com.bangqu.yinwan.shop.internet.SystemException;
import com.bangqu.yinwan.shop.qiniu.IO;
import com.bangqu.yinwan.shop.qiniu.JSONObjectRet;
import com.bangqu.yinwan.shop.qiniu.PutExtras;
import com.bangqu.yinwan.shop.util.SharedPrefUtil;
import com.bangqu.yinwan.shop.util.StringUtil;
import com.bangqu.yinwan.shop.widget.ProgressDialog;
import com.bangqu.yinwan.shop.widget.myTimerDialog;

public class ShopInfomationActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;
	private LinearLayout llHeadImg;
	private ImageView ivShopHead;
	private EditText etShopName;
	private EditText etShopPhone;
	private EditText etLinkMan;
	private EditText etShopAddress;
	private LinearLayout llDetail;
	private TextView etDetail;

	private Boolean deliver = false;
	private Boolean companyDelivery = false;
	private Boolean groupon = true;
	private Boolean promotion = true;
	private Boolean vip = false;

	private EditText etStartTime;
	private EditText etEndTime;
	private EditText etStartDate;
	private EditText etEndDate;

	private RadioGroup rgISSend;
	private RadioButton rbSend1;
	private RadioButton rbSend2;
	private LinearLayout llISSend;
	private LinearLayout llSendPrice;
	private EditText etSendPrice;

	private RadioGroup rgISCompanySend;
	private RadioButton rbCompanySend1;
	private RadioButton rbCompanySend2;
	private LinearLayout llISCompanySend;
	private EditText etdescription;
	private String id;
	private boolean one = false;
	private boolean two = false;
	private boolean thr = false;
	private boolean four = false;

	// 是否改变了店铺信息
	private Boolean isChanged = false;
	// 支付方式
	private String[] payitem = { "现金支付", "会员卡支付", "在线支付", "POS机支付" };
	final String[] Noitem = { "1", "2", "3", "4" };

	private TextView tvpayment;
	private StringBuffer strpay;
	private String paymentone;

	private String[] payitem3;
	// 起始日期
	private String[] date = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天" };
	private int defaultItem = 0;

	private Button btnSubmitShopInfo;
	private ShopBean shopBean;

	// qiniu Start
	// 在七牛绑定的对应bucket的域名. 默认是bucket.qiniudn.com
	public static String bucketName = "yinwan";
	public static String domain = bucketName + ".qiniudn.com";
	// upToken 这里需要自行获取. SDK 将不实现获取过程. 当token过期后才再获取一遍
	public String uptoken = "";
	public String TimeName = "";
	public String backUri = "";
	Uri finalUri = Uri.parse("file:///sdcard/temp.jpg");
	public static final int TAKE_WITH_CAMERA = 3023;
	public static final int PICK_IMAGE_VIEW = 3020;
	public static final int PICTURE_LOCAL = 0;
	public static final int LOCATION_ACTION = 10;
	Bitmap cameraBitmap = null;
	private LoadingCircleView loadinghead;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			loadinghead.setProgress(msg.what);
		};
	};
	private TextView tvshopcitychange;
	private TextView tvshopdistrict;
	private String cityid = "";
	private String districtid = "";
	private String strcityid = "";

	// 修改分钟间隔
	String[] minuts = new String[] { "00", "05", "10", "15", "20", "25", "30",
			"35", "40", "45", "50", "55" };
	private static final int DISTRICTCHANGE = 1001;
	private static final int CITYCHANGE = 1234;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopinfomation_layout);
		id = SharedPrefUtil.getShopBean(this).getId();
		findView();
		new LoadShopViewTask(id).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

		new loadGetTokenTask().execute();
		new loadGetTimeNameTask().execute();
	}

	public void findView() {
		super.findView();

		tvshopcitychange = (TextView) findViewById(R.id.tvshopcitychange);

		tvshopcitychange.setOnClickListener(this);
		tvshopdistrict = (TextView) findViewById(R.id.tvshopdistrict);
		tvshopdistrict.setOnClickListener(this);
		tvpayment = (TextView) findViewById(R.id.tvpayment);
		tvpayment.setOnClickListener(this);
		loadinghead = (LoadingCircleView) findViewById(R.id.loadinghead);
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		tvTittle.setText("店铺资料");
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnSubmitShopInfo = (Button) findViewById(R.id.btnSubmitShopInfo);

		llHeadImg = (LinearLayout) findViewById(R.id.llHeadImg);
		llHeadImg.setOnClickListener(this);

		btnRight.setVisibility(View.GONE);
		btnLeft.setOnClickListener(this);
		btnSubmitShopInfo.setOnClickListener(this);

		// 店铺信息
		ivShopHead = (ImageView) findViewById(R.id.ivShopHead);
		ivShopHead.setOnClickListener(this);
		etShopName = (EditText) findViewById(R.id.etShopName);

		etShopPhone = (EditText) findViewById(R.id.etShopPhone);
		etLinkMan = (EditText) findViewById(R.id.etLinkMan);
		etShopAddress = (EditText) findViewById(R.id.etShopAddress);
		llDetail = (LinearLayout) findViewById(R.id.llDetail);
		llDetail.setOnClickListener(this);
		etDetail = (TextView) findViewById(R.id.etDetail);
		etDetail.setOnClickListener(this);
		etdescription = (EditText) findViewById(R.id.etdescription);

		etStartDate = (EditText) findViewById(R.id.etStartDate);
		etStartDate.setOnClickListener(this);
		etEndDate = (EditText) findViewById(R.id.etEndDate);
		etEndDate.setOnClickListener(this);

		etStartTime = (EditText) findViewById(R.id.etStartTime);
		etStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showAlertDialog();
				Calendar c = Calendar.getInstance();
				// 创建一个TimoPickerDialog实例，并把他们显示出来
				new TimePickerDialog(ShopInfomationActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {
								setNumberPickerTextSize(tp);
								etStartTime.setText(StringUtil
										.addzero(hourOfDay)
										+ ":"
										+ StringUtil.addzero(minute));
							}
						}
						// 设置初始时间
						, Integer.parseInt(etStartTime.getText().toString()
								.trim().substring(0, 2)), Integer
								.parseInt(etStartTime.getText().toString()
										.trim().substring(3, 5))
						// true表示采用24小时制
						, true).show();
			}
		});

		etEndTime = (EditText) findViewById(R.id.etEndTime);
		etEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				// 创建一个TimoPickerDialog实例，并把他们显示出来
				new TimePickerDialog(ShopInfomationActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {

								etEndTime.setText(StringUtil.addzero(hourOfDay)
										+ ":" + StringUtil.addzero(minute));
							}
						}

						// 设置初始时间
						, Integer.parseInt(etEndTime.getText().toString()
								.trim().substring(0, 2)), Integer
								.parseInt(etEndTime.getText().toString().trim()
										.substring(3, 5))
						// true表示采用24小时制
						, true).show();
			}
		});

		// 是否送货
		rgISSend = (RadioGroup) findViewById(R.id.rgISSend);
		rbSend1 = (RadioButton) findViewById(R.id.rbSend1);
		rbSend2 = (RadioButton) findViewById(R.id.rbSend2);
		llISSend = (LinearLayout) findViewById(R.id.llISSend);
		llSendPrice = (LinearLayout) findViewById(R.id.llSendPrice);
		etSendPrice = (EditText) findViewById(R.id.etSendPrice);
		rgISSend.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.rbSend1) {
					deliver = true;
					llSendPrice.setVisibility(View.VISIBLE);
					llISSend.setBackgroundResource(R.drawable.common_bg_single_with_top);
				} else if (arg1 == R.id.rbSend2) {
					deliver = false;
					llSendPrice.setVisibility(View.GONE);
				}
			}
		});

		// 是否支持物业送货
		rgISCompanySend = (RadioGroup) findViewById(R.id.rgISCompanySend);
		rbCompanySend1 = (RadioButton) findViewById(R.id.rbCompanySend1);
		rbCompanySend2 = (RadioButton) findViewById(R.id.rbCompanySend2);
		llISCompanySend = (LinearLayout) findViewById(R.id.llISCompanySend);
		rgISCompanySend
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						if (arg1 == R.id.rbCompanySend1) {
							companyDelivery = true;
							llISCompanySend
									.setBackgroundResource(R.drawable.common_bg_single_with_top);
						} else if (arg1 == R.id.rbCompanySend2) {
							companyDelivery = false;
						}
					}
				});
	}

	public void fillData() {
		super.fillData();
		if (!StringUtil.isBlank(shopBean.getPayment())) {
			paymentone = shopBean.getPayment();
		}
		Constants.CITYCHANGEID = shopBean.getCity().getId();
		Constants.CITYCHANGENANME = shopBean.getCity().getName();
		Constants.DISTRICTID = shopBean.getDistrict().getId();
		Constants.DISTRICTNANME = shopBean.getDistrict().getName();
		strcityid = shopBean.getCity().getId();
		cityid = shopBean.getCity().getId();
		districtid = shopBean.getDistrict().getId();
		tvshopcitychange.setText(shopBean.getCity().getName());
		tvshopdistrict.setText(shopBean.getDistrict().getName());
		((CommonApplication) getApplicationContext()).getImgLoader()
				.DisplayImage(shopBean.getImg(), ivShopHead);
		etShopName.setText(shopBean.getName());
		etShopPhone.setText(shopBean.getPhone());
		etLinkMan.setText(shopBean.getLinkman());
		etShopAddress.setText(shopBean.getAddress());
		Constants.SHOP_ADRESS = etShopAddress.getText().toString().trim();
		Constants.LNG = Double.parseDouble(shopBean.getLng());
		Constants.LAT = Double.parseDouble(shopBean.getLat());
		etDetail.setHint("已定位,点击可更新");
		etStartDate.setText(shopBean.getStartDate());
		etEndDate.setText(shopBean.getEndDate());
		etStartTime.setText(shopBean.getStartTime());
		etEndTime.setText(shopBean.getEndTime());
		if (shopBean.getDeliver()) {
			rbSend1.setChecked(true);
		}
		etSendPrice.setText(shopBean.getSendPrice());

		if (shopBean.getCompanyDelivery()) {
			rbCompanySend1.setChecked(true);
		}
		etdescription.setText(shopBean.getDescription() + "");
		progressbar.setVisibility(View.GONE);
	}

	private void onTakePhotoFinished(int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			return;
		} else if (resultCode != RESULT_OK) {

		} else {
			Bitmap bitmap = decodeUriAsBitmap(finalUri);
			ivShopHead.setImageBitmap(bitmap);
			Constants.IMG_SUCCESS = false;
			doUpload(finalUri);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PICK_IMAGE_VIEW:// 本地选取图片
			try {
				cropImage(data.getData());
			} catch (Exception e) {
				Constants.IMG_SUCCESS = true;
			}
			break;
		case TAKE_WITH_CAMERA:// 拍照取得图片
			try {
				cropImage(finalUri);
			} catch (Exception e) {
				Constants.IMG_SUCCESS = true;
			}
			break;
		case PICTURE_LOCAL:// 获取裁剪后的图片,上传到七牛
			onTakePhotoFinished(resultCode, data);

			// 如果图片还未回收，强制回收图片，释放内存
			// if (bitmap != null && !bitmap.isRecycled()) {
			// bitmap.recycle();
			// bitmap = null;
			// }
			System.gc();
			break;
		case LOCATION_ACTION:
			etDetail.setText("已完成定位");
			break;
		case DISTRICTCHANGE:
			if (!StringUtil.isBlank(Constants.DISTRICTID)) {
				districtid = Constants.DISTRICTID;
				tvshopdistrict.setText(Constants.DISTRICTNANME);

			}
			break;
		case CITYCHANGE:
			if (!StringUtil.isBlank(Constants.CITYCHANGEID)) {
				cityid = Constants.CITYCHANGEID;
				tvshopcitychange.setText(Constants.CITYCHANGENANME);
				if (!Constants.CITYCHANGEID.equals(strcityid)) {
					districtid = "";
					tvshopdistrict.setText("");
					strcityid = Constants.CITYCHANGEID;
				}
			}

			break;
		}
	}

	// 拍照后裁剪功能
	private void cropImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 800);
		intent.putExtra("scale",true);
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
			Constants.IMG_SUCCESS = true;
			return null;
		}
		return bitmap;
	}

	public static final int INDUSTRY_ACTION = 3;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvshopcitychange:
			Constants.CITYGPS = true;

			Intent cityIntent = new Intent(ShopInfomationActivity.this,
					CityChangeActivity.class);
			startActivityForResult(cityIntent, CITYCHANGE);
			break;
		case R.id.tvshopdistrict:
			if (StringUtil.isBlank(Constants.CITYCHANGEID)) {
				Toast.makeText(ShopInfomationActivity.this, "请先选择城市",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent district = new Intent(ShopInfomationActivity.this,
						DistrictActivity.class);
				district.putExtra("cityid", Constants.CITYCHANGEID);
				startActivityForResult(district, DISTRICTCHANGE);
			}
			break;

		case R.id.btnLeft:
			System.out.println(isChanged);
			if (isChanged) {
				AlertDialog.Builder backbuilder = new AlertDialog.Builder(
						ShopInfomationActivity.this);
				backbuilder.setTitle("放弃修改店铺？");
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
		case R.id.etStartDate:
			new AlertDialog.Builder(this).setTitle("开始营业日期")
					.setSingleChoiceItems(date,
					// 单选框有几项,各是什么名字
							defaultItem, // 默认的选项
							new DialogInterface.OnClickListener() { // 点击单选框后的处理
								public void onClick(DialogInterface dialog,
										int which) { // 点击了哪一项
									defaultItem = which;
									etStartDate.setText(date[which]);
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.etEndDate:
			new AlertDialog.Builder(this).setTitle("结束营业日期")
					.setSingleChoiceItems(date,
					// 单选框有几项,各是什么名字
							defaultItem, // 默认的选项
							new DialogInterface.OnClickListener() { // 点击单选框后的处理
								public void onClick(DialogInterface dialog,
										int which) { // 点击了哪一项
									defaultItem = which;
									etEndDate.setText(date[which]);
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.ivShopHead:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("选择头像");
			builder.setItems(new String[] { "相机拍摄", "手机相册" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:// 相机拍摄
								Intent takephoto = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								takephoto.putExtra(MediaStore.EXTRA_OUTPUT,
										finalUri);
								startActivityForResult(takephoto,
										TAKE_WITH_CAMERA);
								break;
							case 1:// 手机相册
								Intent intent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(intent,PICK_IMAGE_VIEW );//PICK_IMAGE_VIEW
								break;
							}
						}
					});
		case R.id.llHeadImg:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("选择头像");
			builder2.setItems(new String[] { "相机拍摄", "手机相册" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:// 相机拍摄
								Intent takephoto = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								takephoto.putExtra(MediaStore.EXTRA_OUTPUT,
										finalUri);
								startActivityForResult(takephoto,
										TAKE_WITH_CAMERA);
								break;
							case 1:// 手机相册
								Intent intent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(intent, PICK_IMAGE_VIEW);
								break;
							}
						}
					});
			builder2.setNegativeButton("取消", null);
			builder2.show();
			break;
		case R.id.etDetail:
			// Log.i("定位", "定位");
			// Intent addressintent = new Intent(ShopInfomationActivity.this,
			// LocationOverlayDemo.class);
			// addressintent.putExtra("location", etShopAddress.getText()
			// .toString());
			// startActivityForResult(addressintent, LOCATION_ACTION);
			if (StringUtil.isBlank(tvshopcitychange.getText().toString())) {
				Toast.makeText(ShopInfomationActivity.this, "请先选择所在市",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvshopdistrict.getText().toString())) {
				Toast.makeText(ShopInfomationActivity.this, "请先选择所在区县",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent addressintent = new Intent(ShopInfomationActivity.this,
					LocationActivity.class);

			addressintent.putExtra("location", tvshopcitychange.getText()
					.toString()
					+ tvshopdistrict.getText().toString()
					+ etShopAddress.getText().toString());
			startActivity(addressintent);
			break;

		case R.id.tvpayment:
			final boolean[] selected = new boolean[] { one, two, thr, four };
			new AlertDialog.Builder(ShopInfomationActivity.this)
					.setTitle("请选择支付方式")
					.setMultiChoiceItems(payitem, selected,
							new OnMultiChoiceClickListener() {

								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									selected[which] = isChecked;
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									strpay = new StringBuffer();

									for (int i = 0; i < 4; i++) {
										if (selected[i]) {
											strpay.append(Noitem[i] + ",");
										}
									}
									if (strpay.length() > 0) {
										strpay.deleteCharAt(strpay.length() - 1);
										paymentone = strpay.toString();
									} else {
										paymentone = "";
									}

									tvpayment.setText("支付方式已选，点击可切换");
									if (strpay.length() > 0) {

										String payitem2[] = strpay.toString()
												.split(",");
										payitem3 = new String[payitem2.length];
										one = false;
										two = false;
										thr = false;
										four = false;
										System.out.println("+++++++++++");
										for (int i = 0; i < payitem2.length; i++) {
											switch (Integer
													.parseInt(payitem2[i])) {

											case 1:
												one = true;
												break;
											case 2:
												two = true;

												break;
											case 3:
												thr = true;
												break;
											case 4:
												four = true;
												break;
											default:
												break;
											}

										}
									} else {
										one = false;
										two = false;
										thr = false;
										four = false;
									}
								}

							}).setNegativeButton("取消", null).show();
			break;
		case R.id.btnSubmitShopInfo:
			String strShopName = etShopName.getText().toString().trim();
			String strShopPhone = etShopPhone.getText().toString().trim();
			String strLinkMan = etLinkMan.getText().toString().trim();
			String straddress = etShopAddress.getText().toString().trim();
			String strlat = Constants.LAT + "";
			String strlng = Constants.LNG + "";
			String strStartDate = etStartDate.getText().toString().trim();
			String strEndDate = etEndDate.getText().toString().trim();
			String strStartTime = etStartTime.getText().toString().trim();
			String strEndTime = etEndTime.getText().toString().trim();
			String strSendPrice = etSendPrice.getText().toString().trim();
			String strdescription = etdescription.getText().toString().trim();

			if (StringUtil.isBlank(straddress)) {
				Toast.makeText(ShopInfomationActivity.this, "店铺名称不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(cityid)) {
				Toast.makeText(ShopInfomationActivity.this, "请选择城市",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(districtid)) {
				Toast.makeText(ShopInfomationActivity.this, "请选择区县",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strShopName)) {
				Toast.makeText(ShopInfomationActivity.this, "店铺地址不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (StringUtil.isBlank(strlat)) {
				Toast.makeText(ShopInfomationActivity.this, "请定位您的店铺",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strlng)) {
				Toast.makeText(ShopInfomationActivity.this, "请定位您的店铺",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (backUri.equals("")) {
				backUri = shopBean.getImg();
			}
			if (Constants.IMG_SUCCESS) {
				new LoadShopUpdateTask(SharedPrefUtil.getToken(this), id,
						backUri, strShopName, strShopPhone, strLinkMan,
						straddress, strlng, strlat, strStartDate, strEndDate,
						strStartTime, strEndTime, deliver, strSendPrice,
						companyDelivery, strdescription, groupon, promotion,
						vip, paymentone, cityid, districtid).execute();

			} else {
				Toast.makeText(ShopInfomationActivity.this, "请等待图片上传完成",
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
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
		// String key = IO.UNDEFINED_KEY; // 自动生成key
		String key = TimeName; // 自动生成key
		PutExtras extra = new PutExtras();
		extra.params = new HashMap<String, String>();
		// extra.params.put("x:a", TimeName);// 上传成功的资源名，即Key
		IO.putFile(this, uptoken, key, uri, extra, new JSONObjectRet() {
			@Override
			public void onProcess(long current, long total) {
				if (!Constants.IMG_SUCCESS) {
					loading(current, total);
					ivShopHead.setVisibility(View.GONE);
					loadinghead.setVisibility(View.VISIBLE);
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
				loadinghead.setVisibility(View.GONE);
				ivShopHead.setVisibility(View.VISIBLE);
				Toast.makeText(ShopInfomationActivity.this, "图片上传成功",
						Toast.LENGTH_LONG).show();
				// Intent intent = new Intent(Intent.ACTION_VIEW,
				// Uri.parse(backUri));
				// startActivity(intent);
			}

			@Override
			public void onFailure(Exception ex) {
				Constants.IMG_SUCCESS = true;
				uploading = false;
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
						System.out.println(uptoken + "七牛图片地址返回");

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ShopInfomationActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ShopInfomationActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(ShopInfomationActivity.this, "数据加载失败",
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
						Toast.makeText(ShopInfomationActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ShopInfomationActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(ShopInfomationActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	/**
	 * 修改店铺信息
	 */
	class LoadShopUpdateTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String id;
		private String img;
		private String name;
		private String phone;
		private String linkman;
		private String address;
		private String lat;
		private String lng;
		private String startDate;
		private String endDate;
		private String startTime;
		private String endTime;
		private Boolean deliver;
		private String sendPrice;
		private Boolean companyDelivery;
		private String description;
		private Boolean groupon;
		private Boolean promotion;
		private Boolean vip;
		private String payment;
		private String cityid;
		private String districtid;

		protected LoadShopUpdateTask(String accessToken, String id, String img,
				String name, String phone, String linkman, String address,
				String lng, String lat, String startDate, String endDate,
				String startTime, String endTime, Boolean deliver,
				String sendPrice, Boolean companyDelivery, String description,
				Boolean groupon, Boolean promotion, Boolean vip,
				String payment, String cityid, String districtid) {
			this.accessToken = accessToken;
			this.id = id;
			this.img = img;
			this.name = name;
			this.phone = phone;
			this.linkman = linkman;
			this.address = address;
			this.lng = lng;
			this.lat = lat;
			this.startDate = startDate;
			this.endDate = endDate;
			this.startTime = startTime;
			this.endTime = endTime;
			this.deliver = deliver;
			this.sendPrice = sendPrice;
			this.companyDelivery = companyDelivery;
			this.description = description;
			this.groupon = groupon;
			this.promotion = promotion;
			this.vip = vip;
			this.payment = payment;
			this.cityid = cityid;
			this.districtid = districtid;
		}

		@Override
		protected void onPreExecute() {
			if (pd == null)
				pd = ProgressDialog.createLoadingDialog(
						ShopInfomationActivity.this, "正在保存修改……");
			pd.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopHelper().UpdateShop(accessToken, id, img, name,
						phone, linkman, address, lng, lat, startDate, endDate,
						startTime, endTime, deliver, sendPrice,
						companyDelivery, description, groupon, promotion, vip,
						payment, cityid, districtid);
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

						SharedPrefUtil.setshopID(ShopInfomationActivity.this,
								id);
						SharedPrefUtil.setshopName(ShopInfomationActivity.this,
								name);

						finish();
						Toast.makeText(ShopInfomationActivity.this,
								result.getString("msg"), Toast.LENGTH_SHORT)
								.show();

					} else if (result.getInt("status") == Constants.FAIL) {
						Toast.makeText(ShopInfomationActivity.this,
								result.getString("msg"), Toast.LENGTH_SHORT)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ShopInfomationActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(ShopInfomationActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	/**
	 * 预览店铺信息
	 * 
	 * @author Administrator
	 */
	class LoadShopViewTask extends AsyncTask<String, Void, JSONObject> {

		private String id;

		protected LoadShopViewTask(String id) {
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopHelper().ShopView(id);
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
						shopBean = JSON.parseObject(result
								.getJSONObject("shop").toString(),
								ShopBean.class);

						if (!StringUtil.isBlank(shopBean.getPayment())) {

							String payitem2[] = shopBean.getPayment()
									.split(",");
							payitem3 = new String[payitem2.length];

							System.out.println(payitem3 + "支付方式");
							for (int i = 0; i < payitem2.length; i++) {
								switch (Integer.parseInt(payitem2[i])) {
								case 1:
									one = true;

									break;
								case 2:
									two = true;
									break;
								case 3:
									thr = true;
									break;
								case 4:
									four = true;
									break;

								default:
									break;
								}
							}
						}
						fillData();

						// 判断店铺信息是否改变
						TextChanged();
					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(ShopInfomationActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
					Log.i("ProductListActivity", "JSONException");
				}
			} else {
				Toast.makeText(ShopInfomationActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
				Log.i("ProductListActivity", "result==null");
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

	/**
	 * 得到timePicker里面的android.widget.NumberPicker组件
	 * 
	 * @param viewGroup
	 * @return
	 */
	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isChanged) {
				AlertDialog.Builder backbuilder = new AlertDialog.Builder(
						ShopInfomationActivity.this);
				backbuilder.setTitle("放弃修改店铺？");
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

	/**
	 * 查找timePicker里面的android.widget.NumberPicker组件 ，并对其进行时间间隔设置
	 * 
	 * @param viewGroup
	 *            TimePicker timePicker
	 */

	@SuppressWarnings("unused")
	private void setNumberPickerTextSize(ViewGroup viewGroup) {
		List<NumberPicker> npList = findNumberPicker(viewGroup);
		if (null != npList) {
			for (NumberPicker mMinuteSpinner : npList) {
				if (mMinuteSpinner.toString().contains("id/minute")) {// 对分钟进行间隔设置
					mMinuteSpinner.setMinValue(0);
					mMinuteSpinner.setMaxValue(minuts.length - 1);
					mMinuteSpinner.setDisplayedValues(minuts); // 分钟显示数组
				}
			}
		}
	}

	public void showAlertDialog() {

		myTimerDialog.Builder builder = new myTimerDialog.Builder(this);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		builder.create().show();

	}

	// 判断店铺信息是否改变

	private void TextChanged() {
		etShopName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				isChanged = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		etLinkMan.addTextChangedListener(new TextWatcher() {

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
		etShopPhone.addTextChangedListener(new TextWatcher() {

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
		tvshopdistrict.addTextChangedListener(new TextWatcher() {

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
		etShopAddress.addTextChangedListener(new TextWatcher() {

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
		tvshopcitychange.addTextChangedListener(new TextWatcher() {

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
		etStartDate.addTextChangedListener(new TextWatcher() {

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
		etEndDate.addTextChangedListener(new TextWatcher() {

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
		etStartTime.addTextChangedListener(new TextWatcher() {

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
		etEndTime.addTextChangedListener(new TextWatcher() {

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
		etdescription.addTextChangedListener(new TextWatcher() {

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
		tvpayment.addTextChangedListener(new TextWatcher() {

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

}
