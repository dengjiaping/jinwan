package com.bangqu.yinwan.shop.helper;

/**
 * 分类相关
 */

import org.json.JSONObject;

import com.bangqu.yinwan.shop.internet.PostParameter;
import com.bangqu.yinwan.shop.internet.SystemException;

public class FenLeiHelper extends BusinessHelper {

	/**
	 * 社区和服务
	 * 
	 */
	public JSONObject list(int level, int parentId, String version)
			throws SystemException {
		return httpClient.post(
				BASE_URL + "category/list.json",
				new PostParameter[] { new PostParameter("query.level", level),
						new PostParameter("query.parentId", parentId),
						new PostParameter("query.version", version),
						new PostParameter("from", "android-shop") })
				.asJSONObject();
	}

}
