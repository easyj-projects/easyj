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
package icu.easyj.spring.boot.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import icu.easyj.core.json.JSONUtils;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.ResourceUtils;
import icu.easyj.test.exception.TestException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestPart;

/**
 * 模拟Request
 *
 * @author wangliang181230
 */
public class MockRequest {

	protected final String urlTemplate;
	protected final Object[] uriVars;

	protected final MockMvc mockMvc;
	protected MockHttpServletRequestBuilder builder;
	protected MockMultipartHttpServletRequestBuilder multipartBuilder;

	private final Map<String, Object> contentMap = new HashMap<>();


	public MockRequest(MockMvc mockMvc, MockHttpServletRequestBuilder builder, String urlTemplate, Object[] uriVars) {
		Assert.notNull(mockMvc, "'mockMvc' must not be null");
		Assert.notNull(builder, "'builder' must not be null");

		this.urlTemplate = urlTemplate;
		this.uriVars = uriVars;

		this.mockMvc = mockMvc;
		this.builder = builder;

		if (builder instanceof MockMultipartHttpServletRequestBuilder) {
			this.multipartBuilder = (MockMultipartHttpServletRequestBuilder)builder;
		} else {
			this.multipartBuilder = null;
		}
	}


	//region Header

	/**
	 * 设置头信息
	 *
	 * @param name   头信息键
	 * @param values 头信息值
	 * @return self
	 */
	public MockRequest header(String name, Object... values) {
		this.builder.header(name, values);
		return this;
	}

	//endregion


	//region Content-Type

	/**
	 * 设置请求内容类型枚举
	 *
	 * @param contentType 内容类型枚举
	 * @return self
	 */
	public MockRequest contentType(MediaType contentType) {
		Assert.notNull(contentType, "'contentType' must not be null");
		this.builder.contentType(contentType);
		return this;
	}

	/**
	 * 设置请求内容类型
	 *
	 * @param contentType 内容类型
	 * @return self
	 */
	public MockRequest contentType(String contentType) {
		Assert.notNull(contentType, "'contentType' must not be null");
		this.builder.contentType(contentType);
		return this;
	}

	//endregion


	//region Character Encoding

	/**
	 * 设置字符编码
	 *
	 * @param charset 字符编码枚举
	 * @return self
	 */
	public MockRequest characterEncoding(Charset charset) {
		Assert.notNull(charset, "'charset' must not be null");
		this.builder.characterEncoding(charset.name());
		return this;
	}

	/**
	 * 设置字符编码
	 *
	 * @param charsetEncoding 字符编码
	 * @return self
	 */
	public MockRequest characterEncoding(String charsetEncoding) {
		Assert.notNull(charsetEncoding, "'encoding' must not be null");
		this.builder.characterEncoding(charsetEncoding);
		return this;
	}

	//endregion


	//region Content（Body）

	/**
	 * 设置内容
	 *
	 * @param content 内容
	 * @return self
	 */
	public MockRequest content(byte[] content) {
		if (!this.contentMap.isEmpty()) {
			throw new TestException("`MockRequest.content(byte[] content)`与`MockRequest.content(String, Object)`两个方法不能混合使用");
		}
		this.builder.content(content);
		return this;
	}

	/**
	 * 设置内容
	 *
	 * @param content 内容
	 * @return self
	 */
	public MockRequest content(String content) {
		if (!this.contentMap.isEmpty()) {
			throw new TestException("`MockRequest.content(String content)`与`MockRequest.content(String, Object)`两个方法不能混合使用");
		}
		this.builder.content(content);
		this.characterEncoding(StandardCharsets.UTF_8);
		return this;
	}

	/**
	 * 设置内容
	 *
	 * @param content 内容对象
	 * @return self
	 */
	public MockRequest content(Object content) {
		if (!this.contentMap.isEmpty()) {
			throw new TestException("`MockRequest.content(Object content)`与`MockRequest.content(String, Object)`两个方法不能混合使用");
		}
		this.content(JSONUtils.toJSONString(content));
		this.contentType(MediaType.APPLICATION_JSON);
		return this;
	}

	/**
	 * 设置完整内容
	 *
	 * @param contentJsonKey   JSON参数键
	 * @param contentJsonValue JSON参数值
	 * @return self
	 */
	public MockRequest content(String contentJsonKey, Object contentJsonValue) {
		try {
			if (ReflectionUtils.getFieldValue(this.builder, "content") != null) {
				throw new TestException("`MockRequest.content(String, Object)`与其他几个重构方法不能混合使用");
			}
		} catch (NoSuchFieldException ignore) {
		}
		this.contentMap.put(contentJsonKey, contentJsonValue);
		return this;
	}

	//region File

	/**
	 * 设置要上传的文件
	 *
	 * @param multipartParamName multipart参数名：对应controller参数注解 {@link RequestPart#name()} 的值
	 * @param filePath           文件路径，可以是相对于`/src/test/resources`目录的路径
	 * @return self
	 */
	@SuppressWarnings("deprecation")
	public MockRequest file(String multipartParamName, String filePath) {
		if (this.multipartBuilder == null) {
			MockMultipartHttpServletRequestBuilder newMultipartBuilder;
			try {
				newMultipartBuilder = MockMvcRequestBuilders.multipart(urlTemplate, uriVars);
			} catch (NoSuchMethodError e) {
				newMultipartBuilder = MockMvcRequestBuilders.fileUpload(urlTemplate, uriVars);
			}
			newMultipartBuilder.merge(this.builder);
			this.multipartBuilder = newMultipartBuilder;
			this.builder = newMultipartBuilder;
		}

		try {
			Resource[] resource = ResourceUtils.getResources(filePath);
			if (ArrayUtils.isEmpty(resource)) {
				throw new TestException("文件不存在：" + filePath);
			}
			this.multipartBuilder.file(new MockMultipartFile(multipartParamName, "", "", resource[0].getInputStream()));
		} catch (IOException e) {
			throw new TestException("设置文件参数时，出现IO异常", e);
		}
		return this;
	}

	//endregion

	//endregion


	//region Query String

	/**
	 * 设置查询参数
	 *
	 * @param name   参数名
	 * @param values 参数值
	 * @return self
	 */
	public MockRequest queryParam(String name, String... values) {
		this.builder.param(name, values);
		return this;
	}

	/**
	 * 批量设置查询参数
	 *
	 * @param params 参数集合
	 * @return self
	 */
	public MockRequest queryParams(MultiValueMap<String, String> params) {
		this.builder.params(params);
		return this;
	}

	//endregion


	//region Send 发送请求

	/**
	 * 发送模拟请求
	 *
	 * @return mockResponse 模拟响应
	 * @throws Exception 异常
	 */
	public MockResponse send() throws Exception {
		if (!this.contentMap.isEmpty()) {
			Map<String, Object> content = new HashMap<>(this.contentMap);
			this.contentMap.clear();
			this.content(content);
		}

		ResultActions resultActions = this.mockMvc.perform(builder);
		return new MockResponse(resultActions);
	}

	//endregion


	//region Getter

	@NonNull
	public MockMvc getOriginMockMvc() {
		return mockMvc;
	}

	@NonNull
	public MockHttpServletRequestBuilder getOriginBuilder() {
		return builder;
	}

	@Nullable
	public MockMultipartHttpServletRequestBuilder getOriginMultipartBuilder() {
		return multipartBuilder;
	}

	//endregion
}
