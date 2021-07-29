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

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

/**
 * 模拟 GET Request
 *
 * @author wangliang181230
 */
public class MockGetRequest {

	protected final MockMvc mockMvc;

	protected final MockHttpServletRequestBuilder builder;


	public MockGetRequest(MockMvc mockMvc, MockHttpServletRequestBuilder builder) {
		Assert.notNull(mockMvc, "'mockMvc' must be not null");
		Assert.notNull(builder, "'builder' must be not null");

		this.mockMvc = mockMvc;
		this.builder = builder;
	}


	//region Query String

	/**
	 * 设置查询参数
	 *
	 * @param name   参数名
	 * @param values 参数值
	 * @return self
	 */
	public MockGetRequest queryParam(String name, String... values) {
		this.builder.queryParam(name, values);
		return this;
	}

	/**
	 * 批量设置查询参数
	 *
	 * @param params 参数集合
	 * @return self
	 */
	public MockGetRequest queryParams(MultiValueMap<String, String> params) {
		this.builder.queryParams(params);
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
		ResultActions resultActions = this.mockMvc.perform(builder);
		return new MockResponse(resultActions);
	}

	//endregion
}
