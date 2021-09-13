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
package icu.easyj.web.util;

import java.util.HashMap;
import java.util.Map;

import icu.easyj.core.util.UrlUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 查询参数串工具类
 *
 * @author wangliang181230
 */
public abstract class QueryStringUtils {

	/**
	 * 解析查询参数串
	 *
	 * @param queryString 查询参数串
	 * @return parameterMap 查询参数Map
	 */
	@NonNull
	public static Map<String, String[]> parse(@Nullable String queryString) {
		// 如果为空，则返回空的Map
		if (!StringUtils.hasLength(queryString)) {
			return new HashMap<>(0);
		}

		String[] paramArr = queryString.split("&");
		Map<String, String[]> parameterMap = new HashMap<>(paramArr.length);

		int idx;
		String key, value;
		String[] values;
		for (String param : paramArr) {
			if (!StringUtils.hasText(param)) {
				continue;
			}

			idx = param.indexOf("=");
			if (idx >= 0) {
				key = idx == 0 ? "" : param.substring(0, idx);
				value = param.substring(idx + 1);

				// 进行URL解码
				value = UrlUtils.decode(value);

				if (parameterMap.containsKey(key)) {
					values = ArrayUtils.addAll(parameterMap.get(key), value);
				} else {
					values = new String[]{value};
				}
			} else {
				key = param;
				values = new String[]{""};
			}

			parameterMap.put(key, values);
		}

		return parameterMap;
	}

}
