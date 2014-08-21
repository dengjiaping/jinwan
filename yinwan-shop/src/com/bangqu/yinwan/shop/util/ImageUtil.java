package com.bangqu.yinwan.shop.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * 图片处理工具类
 * 
 * @author Aizhimin
 * 
 */
public class ImageUtil {

	/**
	 * drawable 转换成 bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取 drawable 的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
																	// bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把 drawable 内容画到画布中
		return bitmap;
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 图片加上圆角效果
	 * 
	 * @param drawable
	 *            需要处理的图片
	 * @param percent
	 *            圆角比例大小
	 * @return
	 */
	public static Bitmap getRoundCornerBitmapWithPic(Drawable drawable,
			float percent) {
		Bitmap bitmap = drawableToBitmap(drawable);
		return getRoundedCornerBitmapWithPic(bitmap, percent);
	}

	/**
	 * 图片加上圆角效果
	 * 
	 * @param bitmap
	 *            要处理的位图
	 * @param roundPx
	 *            圆角大小
	 * @return 返回处理后的位图
	 */
	public static Bitmap getRoundedCornerBitmapWithPic(Bitmap bitmap,
			float percent) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, bitmap.getWidth() * percent,
				bitmap.getHeight() * percent, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获得某个图片资源的宽高
	 * 
	 * @param context
	 * @param drawableId
	 * @return
	 */
	public static int[] getImageWidthHeight(Context context, int drawableId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), drawableId, opts);
		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				drawableId, opts);
		return new int[] { bitmap.getWidth(), bitmap.getHeight() };
	}

	/**
	 * 设置iamgeview 显示宽高
	 * 
	 * @param iv
	 * @param photoWidth
	 * @param oldwidth
	 * @param oldheight
	 */
	public static void setImageViewParams(ImageView iv, int photoWidth,
			int oldwidth, int oldheight) {
		LayoutParams lp = iv.getLayoutParams();
		lp.width = photoWidth;
		lp.height = (oldheight * photoWidth) / oldwidth;
		iv.setLayoutParams(lp);
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HHmmsss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 头像文件名
	 * 
	 * @param sid
	 * @return
	 */
	public static String createAvatarFileName(String sid) {
		return "avatar_" + sid + ".jpg";
	}

	/**
	 * 根据远程服务器id生成图片文件名
	 * 
	 * @param remoteId
	 * @return
	 */
	public static String createPhotoFileName(String remoteId) {
		return "photo_" + remoteId + ".jpg";
	}

	// public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
	//
	// Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	// bitmap.getHeight(), Config.ARGB_8888);
	// Canvas canvas = new Canvas(output);
	//
	// final int color = 0xff424242;
	// final Paint paint = new Paint();
	// final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	// final RectF rectF = new RectF(rect);
	// final float roundPx = pixels;
	//
	// paint.setAntiAlias(true);
	// canvas.drawARGB(0, 0, 0, 0);
	// paint.setColor(color);
	// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	//
	// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	// canvas.drawBitmap(bitmap, rect, rect, paint);
	//
	// return output;
	// }

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 创建一个缩放的图片
	 * 
	 * @param path
	 *            图片地址
	 * @param w
	 *            图片宽度
	 * @param h
	 *            图片高度
	 * @return 缩放后的图片
	 */
	public static Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 保存图片到本地(JPG)
	 * 
	 * @param bm
	 *            保存的图片
	 * @return 图片路径
	 */
	public static String saveToLocal(Bitmap bm) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		File file = new File("/sdcard/wuliaotu/publish/");
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileName = UUID.randomUUID().toString() + ".jpg";
		String filePath = "/sdcard/wuliaotu/publish/" + fileName;
		File f = new File(filePath);
		if (!f.exists()) {
			try {
				f.createNewFile();
				fileOutputStream = new FileOutputStream(filePath);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			} catch (IOException e) {
				return null;
			} finally {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		return filePath;
	}

}
