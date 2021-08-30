/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.core.util;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.URLUtil;
import org.springframework.util.Assert;

import static icu.easyj.core.constant.UrlConstants.HTTPS_PRE;
import static icu.easyj.core.constant.UrlConstants.HTTP_PRE;

/**
 * URL工具类
 *
 * @author wangliang181230
 */
public abstract class UrlUtils {

	/**
	 * 标准化路径
	 * <ol>
	 *     <li>"\"替换为"/"</li>
	 *     <li>为URL时，取路径</li>
	 *     <li>连续的'/'和\s，替换为单个'/'</li>
	 *     <li>移除最后一位'/'</li>
	 *     <li>前面补齐’/‘</li>
	 * </ol>
	 *
	 * @param path 路径
	 * @return path 标准化后的路径
	 */
	public static String normalizePath(String path) {
		Assert.notNull(path, "'path' must be not null");

		path = path.trim();

		if (path.isEmpty()) {
			return StrPool.SLASH;
		}

		// "\"替换为"/"
		path = path.replace(CharPool.BACKSLASH, CharPool.SLASH);

		// 为URL时，取路径
		if (path.startsWith(HTTP_PRE) || path.startsWith(HTTPS_PRE)) {
			path = URLUtil.getPath(path);
		}

		// 连续的'/'和\s，替换为单个'/'
		path = path.replaceAll("[/\\s]+", StrPool.SLASH);

		// 移除最后一位'/'
		if (path.length() > 1 && CharPool.SLASH == path.charAt(path.length() - 1)) {
			path = path.substring(0, path.length() - 1);
		}

		// 前面补齐’/‘
		if (CharPool.SLASH != path.charAt(0)) {
			path = StrPool.SLASH + path;
		}

		return path;
	}
}
