package com.bangqu.yinwan.shop.ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bangqu.yinwan.shop.R;
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

public class CreateShopActivity extends UIBaseActivity implements
		OnClickListener {

	// 顶部title
	private Button btnRight;
	private TextView tvTittle;
	private Button btnLeft;
	//
	private Boolean deliver = true;
	private Boolean companyDelivery = true;

	// 内容
	private ImageView ivShopHead;
	private LinearLayout llHeadImg;
	private EditText etShopName;
	private EditText etShopPhone;
	private EditText etLinkMan;
	private EditText etShopAddress;
	private TextView tvDetail;
	private TextView tvpayment;
	private EditText etStartTime;
	private EditText etEndTime;
	private EditText etStartDate;
	private EditText etEndDate;
	private EditText etSendPrice;
	private EditText etShopContent;
	private EditText etdescription;
	private LoadingCircleView loading;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			loading.setProgress(msg.what);
		};
	};
	private String payment;
	private StringBuffer strpay;
	// 支付方式
	final String[] payitem = { "现金支付", "会员卡支付", "在线支付", "POS机支付" };
	final String[] Noitem = { "1", "2", "3", "4" };
	final boolean[] selected = new boolean[] { false, true, true, false };
	// 起始日期
	private String[] date = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天" };
	private String[] date2 = { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private int defaultItem = 0;

	private Button btnCreateShop;
	private List<ShopBean> shopList = new ArrayList<ShopBean>();

	private static final int DISTRICTCHANGE = 1001;
	private static final int CITYCHANGE = 1234;
	// qiniu Start
	// 在七牛绑定的对应bucket的域名. 默认是bucket.qiniudn.com
	// public static String bucketName = "bucketName";
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
	// 是否送货
	private RadioGroup rgISSendcreate;
	private RadioButton rbSendcreat1;
	private RadioButton rbSendcreat2;
	private TextView tvsong;
	private LinearLayout llsongmoney;
	// 是否物业送货
	private RadioGroup rgISSendwuye;
	private RadioButton rbSendwuye1;
	private RadioButton rbSendwuye2;
	private LinearLayout llcompanyDelivery;
	private View vi1;
	private View vi2;
	private TextView tvcitychange;
	private TextView tvdistrict;
	int starthourtime = 9;
	int startmintime = 00;
	private String districtid = "";
	private String cityid = "";
	private String strcityid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createshop_layout);
		findView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		new loadGetTokenTask().execute();
		new loadGetTimeNameTask().execute();
	}

	@Override
	public void fillData() {
		super.fillData();
	}

	public void findView() {
		super.findView();
		loading = (LoadingCircleView) findViewById(R.id.loading);
		tvdistrict = (TextView) findViewById(R.id.tvdistrict);
		tvdistrict.setOnClickListener(this);
		tvcitychange = (TextView) findViewById(R.id.tvcitychange);
		tvcitychange.setOnClickListener(this);
		tvsong = (TextView) findViewById(R.id.tvsong);
		tvsong.setOnClickListener(this);
		llsongmoney = (LinearLayout) findViewById(R.id.llsongmoney);
		llcompanyDelivery = (LinearLayout) findViewById(R.id.llcompanyDelivery);

		vi1 = (View) findViewById(R.id.vi1);
		vi2 = (View) findViewById(R.id.vi2);

		// 是否物业送货
		rgISSendwuye = (RadioGroup) findViewById(R.id.rgISSendwuye);
		rbSendwuye1 = (RadioButton) findViewById(R.id.rbSendwuye1);
		rbSendwuye2 = (RadioButton) findViewById(R.id.rbSendwuye2);
		rgISSendwuye.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checid) {
				if (checid == R.id.rbSendwuye1) {
					companyDelivery = true;
				} else if (checid == R.id.rbSend2) {
					companyDelivery = false;
				}
			}
		});

		// 是否送货
		rgISSendcreate = (RadioGroup) findViewById(R.id.rgISSendcreate);
		rbSendcreat1 = (RadioButton) findViewById(R.id.rbSendcreat1);
		rbSendcreat2 = (RadioButton) findViewById(R.id.rbSendcreat2);
		rgISSendcreate
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (checkedId == R.id.rbSendcreat1) {
							deliver = true;
							companyDelivery = true;
							etSendPrice.setText("20");
							llsongmoney.setVisibility(View.VISIBLE);
							llcompanyDelivery.setVisibility(View.VISIBLE);
							vi1.setVisibility(View.VISIBLE);
							vi2.setVisibility(View.VISIBLE);
						} else if (checkedId == R.id.rbSendcreat2) {
							deliver = false;
							companyDelivery = false;
							etSendPrice.setText("");
							vi1.setVisibility(View.GONE);
							vi2.setVisibility(View.GONE);
							llsongmoney.setVisibility(View.GONE);
							llcompanyDelivery.setVisibility(View.GONE);

						}
					}
				});
		// 顶部title
		tvTittle = (TextView) findViewById(R.id.tvTittle);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		tvTittle.setText("创建店铺");
		btnRight.setText("清空");
		btnRight.setOnClickListener(this);
		btnLeft.setOnClickListener(this);

		llHeadImg = (LinearLayout) findViewById(R.id.llHeadImg);
		llHeadImg.setOnClickListener(this);
		ivShopHead = (ImageView) findViewById(R.id.ivShopHead);
		etShopName = (EditText) findViewById(R.id.etShopName);
		etShopPhone = (EditText) findViewById(R.id.etShopPhone);
		etLinkMan = (EditText) findViewById(R.id.etLinkMan);
		etShopAddress = (EditText) findViewById(R.id.etShopAddress);

		tvDetail = (TextView) findViewById(R.id.tvDetail);
		tvDetail.setOnClickListener(this);
		tvpayment = (TextView) findViewById(R.id.tvpayment);
		tvpayment.setOnClickListener(this);
		etStartDate = (EditText) findViewById(R.id.etStartDate);
		etEndDate = (EditText) findViewById(R.id.etEndDate);
		etStartTime = (EditText) findViewById(R.id.etStartTime);
		etEndTime = (EditText) findViewById(R.id.etEndTime);
		etShopContent = (EditText) findViewById(R.id.etShopContent);
		etdescription = (EditText) findViewById(R.id.etdescription);

		btnCreateShop = (Button) findViewById(R.id.btnCreateShop);
		btnCreateShop.setOnClickListener(this);
		etStartDate.setOnClickListener(this);
		etEndDate.setOnClickListener(this);
		etStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 创建一个TimoPickerDialog实例，并把他们显示出来
				new TimePickerDialog(CreateShopActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {
								etStartTime.setText(StringUtil
										.addzero(hourOfDay)
										+ ":"
										+ StringUtil.addzero(minute));
							}
						}
						// 设置开始营业初始时间
						, Integer.parseInt(etStartTime.getText().toString()
								.substring(0, 2)), Integer.parseInt(etStartTime
								.getText().toString().substring(3, 5))
						// true表示采用24小时制
						, true).show();
			}
		});
		etEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 创建一个TimoPickerDialog实例，并把他们显示出来
				new TimePickerDialog(CreateShopActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay,
									int minute) {
								etEndTime.setText(StringUtil.addzero(hourOfDay)
										+ ":" + StringUtil.addzero(minute));
							}
						}
						// 设置结束营业时间
						, Integer.parseInt(etEndTime.getText().toString()
								.substring(0, 2)), Integer.parseInt(etEndTime
								.getText().toString().substring(3, 5))
						// true表示采用24小时制
						, true).show();
			}
		});

		etSendPrice = (EditText) findViewById(R.id.etSendPrice);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvdistrict:
			if (StringUtil.isBlank(Constants.CITYCHANGEID)) {
				Toast.makeText(CreateShopActivity.this, "请先选择城市",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent district = new Intent(CreateShopActivity.this,
						DistrictActivity.class);
				district.putExtra("cityid", Constants.CITYCHANGEID);
				startActivityForResult(district, DISTRICTCHANGE);
			}

			break;
		case R.id.tvcitychange:
			Constants.CITYGPS = true;
			Intent cityIntent = new Intent(CreateShopActivity.this,
					CityChangeActivity.class);
			startActivityForResult(cityIntent, CITYCHANGE);
			break;
		case R.id.btnLeft:
			AlertDialog.Builder backbuilder = new AlertDialog.Builder(
					CreateShopActivity.this);
			backbuilder.setTitle("放弃创建店铺？");
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
		case R.id.tvsong:
			System.out.println(deliver + "是否送货");
			System.out.println(companyDelivery + "是否物业送货");
			break;
		case R.id.tvpayment:
			new AlertDialog.Builder(CreateShopActivity.this)
					.setTitle("请选择支付方式")
					.setMultiChoiceItems(payitem, selected,
							new OnMultiChoiceClickListener() {

								@Override
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
									strpay.deleteCharAt(strpay.length() - 1);
									payment = strpay.toString();

									tvpayment.setText("支付方式已选，点击可切换");
								}

							}).setNegativeButton("取消", null).show();
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
			new AlertDialog.Builder(this)
					.setTitle("结束营业日期")
					.setSingleChoiceItems(date2, defaultItem,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									defaultItem = which;
									etEndDate.setText(date2[which]);
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.btnRight:
			// etShopName.setText("");
			// etShopPhone.setText("");
			// etLinkMan.setText("");
			// etShopAddress.setText("");
			// tvDetail.setText("");
			// etStartDate.setText("");
			// etEndDate.setText("");
			// etStartTime.setText("");
			// etEndTime.setText("");
			// deliver = false;
			// companyDelivery = false;
			// etShopContent.setText("");
			startActivity(new Intent(CreateShopActivity.this,
					CreateShopActivity.class));
			finish();
			Toast.makeText(CreateShopActivity.this, "已恢复初始状态，请重新填写相关信息",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.llHeadImg:
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
								startActivityForResult(intent, PICK_IMAGE_VIEW);
								break;
							}
						}
					});
			builder.setNegativeButton("取消", null);
			builder.show();
			break;

		case R.id.tvDetail:
			// Log.i("定位", "定位");
			if (StringUtil.isBlank(tvcitychange.getText().toString())) {
				Toast.makeText(CreateShopActivity.this, "请先选择所在市",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isBlank(tvdistrict.getText().toString())) {
				Toast.makeText(CreateShopActivity.this, "请先选择所在区县",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent adintent = new Intent(CreateShopActivity.this,
					LocationActivity.class);
			adintent.putExtra("location", etShopAddress.getText().toString()
					.trim());
			tvDetail.setText("正在定位……");
			startActivityForResult(adintent, LOCATION_ACTION);
			break;
		case R.id.btnCreateShop:
			String strShopName = etShopName.getText().toString().trim();
			String strShopPhone = etShopPhone.getText().toString().trim();
			String strLinkMan = etLinkMan.getText().toString().trim();
			String strShopAddress = etShopAddress.getText().toString().trim();
			String strlng = Constants.LNG + "";
			String strlat = Constants.LAT + "";
			String strStartDate = etStartDate.getText().toString().trim();
			String strEndDate = etEndDate.getText().toString().trim();
			String strStartTime = etStartTime.getText().toString().trim();
			String strEndTime = etEndTime.getText().toString().trim();
			String strSendPrice = etSendPrice.getText().toString().trim();
			String strdescription = etdescription.getText().toString().trim();
			if (StringUtil.isBlank(strShopName)) {
				Toast.makeText(CreateShopActivity.this, "店铺名称不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (StringUtil.isBlank(strShopPhone)) {
				Toast.makeText(CreateShopActivity.this, "店铺电话不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (StringUtil.isBlank(strLinkMan)) {
				Toast.makeText(CreateShopActivity.this, "联系人不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(cityid)) {
				Toast.makeText(CreateShopActivity.this, "所在城市不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(districtid)) {
				Toast.makeText(CreateShopActivity.this, "所在区县不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strShopAddress)) {
				Toast.makeText(CreateShopActivity.this, "店铺地址不能为空",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strlng)) {
				Toast.makeText(CreateShopActivity.this, "请获取定位信息",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strlat)) {
				Toast.makeText(CreateShopActivity.this, "请获取定位信息",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strStartTime)) {
				Toast.makeText(CreateShopActivity.this, "请选择营业时间",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(strEndTime)) {
				Toast.makeText(CreateShopActivity.this, "请选择营业时间",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (StringUtil.isBlank(payment)) {
				Toast.makeText(CreateShopActivity.this, "选择支付方式",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (Constants.IMG_SUCCESS) {
				new LoadShopCreatTask(
						SharedPrefUtil.getToken(CreateShopActivity.this),
						strShopName, strShopPhone, strLinkMan, strShopAddress,
						backUri, strlng, strlat, strSendPrice, strStartDate,
						strEndDate, strStartTime, strEndTime, strdescription,
						payment, companyDelivery, deliver, cityid, districtid)
						.execute();
			} else {
				Toast.makeText(CreateShopActivity.this, "请等待图片上传结束",
						Toast.LENGTH_LONG).show();
			}

			break;
		default:
			break;
		}
	}

	private void onTakePhotoFinished(int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			Constants.IMG_SUCCESS = true;
			return;
		} else if (resultCode != RESULT_OK) {
			Constants.IMG_SUCCESS = true;
		} else {
			Constants.IMG_SUCCESS = false;
			doUpload(finalUri);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (resultCode == RESULT_OK) {
		switch (requestCode) {
		case TAKE_WITH_CAMERA:// 拍照取得图片
			try {
				cropImage(finalUri);
				// doUpload(finalUri);
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

		case PICTURE_LOCAL:// 获取裁剪后的图片,上传到七牛
			onTakePhotoFinished(resultCode, data);
			break;
		case LOCATION_ACTION:
			tvDetail.setText("已完成定位");
			break;

		case DISTRICTCHANGE:
			if (!StringUtil.isBlank(Constants.DISTRICTID)) {
				districtid = Constants.DISTRICTID;
				tvdistrict.setText(Constants.DISTRICTNANME);
			}
			break;

		case CITYCHANGE:
			if (!StringUtil.isBlank(Constants.CITYCHANGEID)) {

				cityid = Constants.CITYCHANGEID;
				tvcitychange.setText(Constants.CITYCHANGENANME);
				if (!Constants.CITYCHANGEID.equals(strcityid)) {
					districtid = "";
					tvdistrict.setText("");
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
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
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
			Constants.IMG_SUCCESS = true;
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
		// String key = IO.UNDEFINED_KEY; // 自动生成key
		String key = TimeName; // 自动生成key
		PutExtras extra = new PutExtras();
		extra.params = new HashMap<String, String>();
		// extra.params.put("x:a", TimeName);// 上传成功的资源名，即Key
		// System.out.println("key=" + key + ",uri=" + uri + ",extra=" + extra);
		IO.putFile(this, uptoken, key, uri, extra, new JSONObjectRet() {
			@Override
			public void onProcess(long current, long total) {
				loading(current, total);
				loading.setVisibility(View.VISIBLE);
				ivShopHead.setVisibility(View.GONE);
			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(JSONObject resp) {
				uploading = false;
				String key = resp.optString("key", "");
				String hash = resp.optString("hash", "");
				String value = resp.optString("x:a", "");
				backUri = "http://" + domain + "/" + key;
				System.out.println(backUri);
				Constants.IMG_SUCCESS = true;
				Toast.makeText(CreateShopActivity.this, "图片上传成功",
						Toast.LENGTH_LONG).show();
				Bitmap bitmap = decodeUriAsBitmap(finalUri);
				ivShopHead.setImageBitmap(bitmap);
				loading.setVisibility(View.GONE);
				ivShopHead.setVisibility(View.VISIBLE);
				// Intent intent = new Intent(Intent.ACTION_VIEW,
				// Uri.parse(backUri));
				// startActivity(intent);
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
	 * 商品列表
	 * 
	 * @author Administrator
	 * 
	 */
	class LoadShopCreatTask extends AsyncTask<String, Void, JSONObject> {
		private String accessToken;
		private String name;
		private String phone;
		private String linkman;
		private String address;
		private String img;
		private String lng;
		private String lat;
		private String sendPrice;
		// private String content;
		private String startDate;
		private String endDate;
		private String startTime;
		private String endTime;
		private String description;
		private String payment;
		private Boolean companyDelivery;
		private Boolean deliver;
		private String cityid;
		private String districtid;

		protected LoadShopCreatTask(String accessToken, String name,
				String phone, String linkman, String address, String img,
				String lng, String lat, String sendPrice, String startDate,
				String endDate, String startTime, String endTime,
				String description, String payment, Boolean companyDelivery,
				Boolean deliver, String cityid, String districtid) {
			this.accessToken = accessToken;
			this.name = name;
			this.phone = phone;
			this.linkman = linkman;
			this.address = address;
			this.img = img;
			this.lng = lng;
			this.lat = lat;
			this.sendPrice = sendPrice;
			this.startDate = startDate;
			this.endDate = endDate;
			this.startTime = startTime;
			this.endTime = endTime;
			this.description = description;
			this.payment = payment;
			this.companyDelivery = companyDelivery;
			this.deliver = deliver;
			this.cityid = cityid;
			this.districtid = districtid;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return new ShopHelper().CreatShop(accessToken, name, phone,
						linkman, address, img, lng, lat, sendPrice, startDate,
						endDate, startTime, endTime, description, payment,
						companyDelivery, deliver, cityid, districtid);
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
						Toast.makeText(CreateShopActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();

						finish();
					} else if (result.getInt("status") == Constants.FAIL) {
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CreateShopActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(CreateShopActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}
		}
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
						Toast.makeText(CreateShopActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CreateShopActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(CreateShopActivity.this, "数据加载失败",
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
						Toast.makeText(CreateShopActivity.this,
								result.getString("msg"), Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(CreateShopActivity.this, "数据加载失败",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(CreateShopActivity.this, "数据加载失败",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder backbuilder = new AlertDialog.Builder(
					CreateShopActivity.this);
			backbuilder.setTitle("放弃创建店铺？");
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
