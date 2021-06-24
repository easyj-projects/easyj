package org.easyframework.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.URLUtil;

/**
 * URL工具类
 *
 * @author wangliang181230
 */
public class UrlUtils {

	/**
	 * http前缀
	 */
	public static final String HTTP_PRE = "http://";

	/**
	 * https前缀
	 */
	public static final String HTTPS_PRE = "https://";

	/**
	 * 标准化路径
	 * <ol>
	 *     <li>"\"替换为"/"</li>
	 *     <li>完整URL，取路径</li>
	 *     <li>连续的"/"替换为单个"/"</li>
	 *     <li>移除最后一位"/"</li>
	 *     <li>前面补齐”/“</li>
	 * </ol>
	 *
	 * @param path 路径
	 * @return path 标准化后的路径
	 */
	public static String normalizePath(String path) {
		Assert.notNull(path, "path must be not null");

		if (path.isEmpty()) {
			return StrPool.SLASH;
		}

		// "\"替换为"/"
		path = path.replaceAll("\\\\", StrPool.SLASH);

		// 完整URL，取路径
		if (path.startsWith(HTTP_PRE) || path.startsWith(HTTPS_PRE)) {
			path = URLUtil.getPath(path);
		}

		// 连续的"/"替换为单个"/"
		path = path.replaceAll("/{2,}", StrPool.SLASH);

		// 移除最后一个"/"
		while (path.length() > 1 && CharPool.SLASH == path.charAt(path.length() - 1)) {
			path = path.substring(0, path.length() - 1);
		}

		// 第一个字符为”/“
		if (CharPool.SLASH != path.charAt(0)) {
			path = StrPool.SLASH + path;
		}

		return path;
	}
}
