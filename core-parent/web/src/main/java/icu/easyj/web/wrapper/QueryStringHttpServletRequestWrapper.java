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
package icu.easyj.web.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import icu.easyj.core.util.StringUtils;
import icu.easyj.web.util.QueryStringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 查询参数串 HttpServletRequest包装类
 *
 * @author wangliang181230
 */
public class QueryStringHttpServletRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 查询参数串
	 */
	private final String queryString;

	/**
	 * 解析后的查询参数缓存
	 */
	private Map<String, String[]> parameterMap;

	/**
	 * 构造函数
	 *
	 * @param request     请求
	 * @param queryString 查询参数串
	 */
	public QueryStringHttpServletRequestWrapper(HttpServletRequest request, String queryString) {
		super(request);
		this.queryString = queryString == null ? "" : queryString;
	}


	//region Override

	@NonNull
	@Override
	public String getQueryString() {
		return queryString;
	}

	@NonNull
	@Override
	public Map<String, String[]> getParameterMap() {
		if (this.parameterMap == null) {
			this.parameterMap = QueryStringUtils.parse(this.queryString);
		}
		return this.parameterMap;
	}

	@NonNull
	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	}

	@Nullable
	@Override
	public String[] getParameterValues(String name) {
		return getParameterMap().get(name);
	}

	@Nullable
	@Override
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		if (values == null) {
			return null;
		} else if (values.length == 0) {
			return "";
		} else if (values.length == 1) {
			return values[0];
		} else {
			for (String value : values) {
				if (StringUtils.isNotBlank(value)) {
					return value;
				}
			}
			return values[0];
		}
	}

	//endregion
}
