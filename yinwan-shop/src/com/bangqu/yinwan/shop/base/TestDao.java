package com.bangqu.yinwan.shop.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

import com.bangqu.yinwan.shop.util.HttpUtils;

import android.content.Context;
import android.graphics.Bitmap;

public class TestDao {
	// 上传头像(用户ID，头像，文件名)
	public static int uploadHead(int userId, Bitmap photoPath, Context context,
			String fileName) {
		int resultId = 0;
		String urlStr = "servlet/UserServlet?command=uploadHead&uId=" + userId
				+ "&photoPath=" + fileName;
		String end = "\r\n";// 换行符
		String twoHyphens = "--";// 连字符
		String boundary = "*****";// 界限
		HttpURLConnection uc = HttpUtils.getUC(context, urlStr);
		// 允许Input、Output，不使用Cache
		uc.setDoInput(true);
		uc.setDoOutput(true);
		uc.setUseCaches(false);
		// boolean isWrite = true;
		String newName = fileName;
		// if (photoPath.lastIndexOf("/") >= 0) {
		// newName = photoPath.substring(photoPath.lastIndexOf("/"));
		// } else {
		// isWrite = false;
		// }
		try {
			uc.setRequestMethod("POST");
			uc.setRequestProperty("Connection", "Keep-Alive");
			uc.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			uc.setRequestProperty("Charset", "UTF-8");
			// if (isWrite) {
			DataOutputStream ds = new DataOutputStream(uc.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			// FileInputStream fStream = new FileInputStream(photoPath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 得到输出流
			photoPath.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			// 转输入流
			InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			// 从文件读取数据至缓冲区
			while ((length = isBm.read(buffer)) > 0) {
				// 将资料写入DataOutputStream中
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			isBm.close();
			ds.flush();
			DataInputStream dis = new DataInputStream(uc.getInputStream());
			resultId = dis.readInt();
			dis.close();
			// }
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return resultId;
	}
}
