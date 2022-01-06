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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import icu.easyj.core.util.StringUtils;
import icu.easyj.web.servlet.BodyServletInputStream;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Body HttpServletRequest包装类
 *
 * @author wangliang181230
 */
public class BodyHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private String body;

	@Nullable
	private ServletInputStream inputStream;

	@Nullable
	private BufferedReader reader;

	/**
	 * 构造函数
	 *
	 * @param request 请求
	 * @param body    body参数字符串
	 */
	public BodyHttpServletRequestWrapper(HttpServletRequest request, String body) {
		super(request);
		Assert.isTrue(StringUtils.isNotBlank(body), "'body' must not be null");
		this.body = body;
	}


	//region Override

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (this.inputStream == null) {
			// 生成
			this.inputStream = new BodyServletInputStream(this.body.getBytes(DEFAULT_CHARSET));

			// body已不再需要，设为null，方便GC回收
			this.body = null;
		}
		return this.inputStream;
	}

	@Override
	public String getCharacterEncoding() {
		return DEFAULT_CHARSET.name();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		if (this.reader == null) {
			this.reader = new BufferedReader(new InputStreamReader(this.getInputStream(), DEFAULT_CHARSET));
		}
		return this.reader;
	}

	//endregion
}
