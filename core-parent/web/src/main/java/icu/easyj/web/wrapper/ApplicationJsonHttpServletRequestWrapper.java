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

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;

/**
 * 将当前请求，强制替换为 application/json请求 的HttpServletRequest包装类
 *
 * @author wangliang181230
 * @since 0.7.7
 */
public class ApplicationJsonHttpServletRequestWrapper extends BodyHttpServletRequestWrapper {

	private final static Enumeration<String> CONTENT_TYPE_ENUMERATION = Collections.enumeration(Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));


	public ApplicationJsonHttpServletRequestWrapper(HttpServletRequest request, String jsonData) {
		super(request, jsonData);
	}


	@Override
	public String getContentType() {
		return MediaType.APPLICATION_JSON_VALUE;
	}

	@Override
	public String getHeader(String name) {
		if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name)) {
			return MediaType.APPLICATION_JSON_VALUE;
		}

		return super.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name)) {
			return CONTENT_TYPE_ENUMERATION;
		}

		return super.getHeaders(name);
	}
}
