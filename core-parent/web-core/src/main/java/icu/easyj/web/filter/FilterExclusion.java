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
package icu.easyj.web.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrPool;
import icu.easyj.core.util.MapUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 过滤器排除项信息类
 *
 * @author wangliang181230
 */
public class FilterExclusion {

	/**
	 * 请求方法：为空表示排除所有类型的请求
	 */
	private final String method;

	/**
	 * 排除的URI匹配串
	 */
	private final String uriPattern;

	/**
	 * 构造函数
	 *
	 * @param configStr 配置串，格式如：*:/data/get、POST:/data/save
	 */
	public FilterExclusion(@NonNull String configStr) {
		Assert.notNull(configStr, "'configStr' must be not null");

		if (configStr.contains(StrPool.COLON)) {
			String[] arr = configStr.split(StrPool.COLON);
			if (arr.length == 1) {
				this.method = null;
				this.uriPattern = arr[0].trim();
			} else {
				this.method = arr[0].trim().toUpperCase();
				this.uriPattern = arr[1].trim();
			}
		} else {
			this.method = null;
			this.uriPattern = configStr.trim();
		}
	}

	//region 静态方法

	/**
	 * 转换类型
	 *
	 * @param configList 配置列表
	 * @return 配置集合
	 */
	public static Map<String, List<String>> convert(List<String> configList) {
		if (CollectionUtils.isEmpty(configList)) {
			return null;
		}

		Map<String, List<String>> listMap = new HashMap<>(4);

		FilterExclusion info;
		String method, pattern;
		for (String configStr : configList) {
			if (!StringUtils.hasText(configStr)) {
				continue;
			}
			info = new FilterExclusion(configStr);
			method = info.getMethod();
			pattern = info.getUriPattern();
			addPatternToListMap(listMap, method, pattern);
		}

		return listMap;
	}

	// 重载方法
	public static Map<String, List<String>> convert(String[] configArr) {
		return convert(CollectionUtil.toList(configArr));
	}

	// 重载方法
	public static Map<String, List<String>> convert(String configStr) {
		return convert(configStr.split(StrPool.COMMA));
	}

	/**
	 * 将一个配置项添加到过滤器排除列表过滤器中
	 *
	 * @param listMap 排除列表
	 * @param method  请求方法
	 * @param pattern 请求地址匹配串
	 */
	private static void addPatternToListMap(Map<String, List<String>> listMap, String method, String pattern) {
		if (!StringUtils.hasText(pattern)) {
			return;
		}
		MapUtils.computeIfAbsent(listMap, method, k -> new ArrayList<>())
				.add(pattern);
	}

	//endregion


	//region Getter

	public String getMethod() {
		return method;
	}

	public String getUriPattern() {
		return uriPattern;
	}

	//endregion
}