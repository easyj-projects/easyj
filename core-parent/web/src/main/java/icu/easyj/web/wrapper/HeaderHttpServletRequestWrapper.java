/*
 * Copyright 2021-2022 the original author or authors.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import icu.easyj.core.util.DateUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * 头信息 HttpServletRequest包装类
 *
 * @author wangliang181230
 */
public class HeaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 头信息
	 */
	private final LinkedCaseInsensitiveMap<Object> headers;

	//region 头信息解析缓存

	private Enumeration<String> headerNames;

	//endregion


	/**
	 * 构造函数
	 *
	 * @param request 请求
	 * @param headers 头信息
	 */
	public HeaderHttpServletRequestWrapper(HttpServletRequest request, LinkedCaseInsensitiveMap<Object> headers) {
		super(request);
		this.headers = headers;
	}


	//region Override

	@Override
	public Enumeration<String> getHeaderNames() {
		if (this.headerNames == null) {
			// 读取当前包装类中的头信息
			Set<String> headerNames = this.headers.keySet();

			// 读取父类中的头信息
			Enumeration<String> enumeration = super.getHeaderNames();
			if (enumeration != null) {
				while (enumeration.hasMoreElements()) {
					headerNames.add(enumeration.nextElement());
				}
			}

			this.headerNames = Collections.enumeration(headerNames);
		}
		return this.headerNames;
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		if (this.headers.containsKey(name)) {
			Object header = this.headers.get(name);
			if (header != null) {
				String headerStr = String.valueOf(header);

				List<String> headers = new ArrayList<>();
				headers.add(headerStr);

				Enumeration<String> enumeration = super.getHeaders(name);
				if (enumeration != null) {
					while (enumeration.hasMoreElements()) {
						headers.add(enumeration.nextElement());
					}
				}

				return Collections.enumeration(headers);
			}
		}
		return super.getHeaders(name);
	}

	@Override
	public String getHeader(String name) {
		if (this.headers.containsKey(name)) {
			Object header = this.headers.get(name);
			if (header != null) {
				return String.valueOf(header);
			}
		}
		return super.getHeader(name);
	}

	@Override
	public long getDateHeader(String name) {
		if (this.headers.containsKey(name)) {
			Object value = this.headers.get(name);
			if (value != null) {
				if (value instanceof Date) {
					return ((Date)value).getTime();
				} else if (value instanceof Number) {
					return ((Number)value).longValue();
				} else if (value instanceof String) {
					String str = (String)value;
					try {
						return Long.parseLong(str);
					} catch (NumberFormatException e) {
						return DateUtils.parseAll(str).getTime();
					}
				} else {
					return -1L;
				}
			}
		}
		return super.getDateHeader(name);
	}

	@Override
	public int getIntHeader(String name) {
		if (this.headers.containsKey(name)) {
			Object value = this.headers.get(name);
			if (value != null) {
				if (value instanceof Number) {
					return ((Number)value).intValue();
				} else if (value instanceof String) {
					return Integer.parseInt((String)value);
				} else {
					return -1;
				}
			}
		}
		return super.getIntHeader(name);
	}

	//endregion
}
