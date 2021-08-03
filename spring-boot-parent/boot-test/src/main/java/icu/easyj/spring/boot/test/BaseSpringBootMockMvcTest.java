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

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

/**
 * SpringBoot的模拟MVC接口测试 基类
 *
 * @author wangliang181230
 */
public class BaseSpringBootMockMvcTest {

	@Autowired
	protected WebApplicationContext wac;

	protected MockMvc mockMvc;

	/**
	 * 用于当前测试类中所有方法的过滤器
	 */
	private final Filter[] filters;

	/**
	 * 构造函数
	 *
	 * @param filters 过滤器数组
	 */
	public BaseSpringBootMockMvcTest(Filter... filters) {
		this.filters = filters;
	}

	@BeforeEach
	public void beforeEach() {
		this.initMockMvc(this.filters);
	}

	/**
	 * 初始化模拟MVC
	 *
	 * @param filters 过滤器数组
	 */
	protected void initMockMvc(Filter... filters) {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(wac);

		// 添加构造函数中的过滤器（如果与入参不同的话）
		if (this.filters != null && this.filters.length > 0 && this.filters != filters) {
			// 先添加构造函数中的过滤器
			builder.addFilters(this.filters);

			// 再添加入参中的过滤器（不与构造函数中的过滤器重复添加）
			if (filters != null && filters.length > 0) {
				for (Filter f : filters) {
					Assert.notNull(f, "filters cannot contain null values");

					// 判断是否已添加过过滤器
					boolean isAdded = false;
					for (Filter f0 : this.filters) {
						if (f0 == f) {
							isAdded = true;
							break;
						}
					}
					if (isAdded) {
						continue;
					}

					builder.addFilter(f);
				}
			}
		} else if (filters != null && filters.length > 0) {
			builder.addFilters(filters);
		}

		mockMvc = builder.build();
	}

	/**
	 * 创建模拟GET请求
	 *
	 * @param urlTemplate 地址模板
	 * @param uriVars     参数
	 * @return mockRequest 模拟GET请求
	 */
	protected MockRequest mockGet(String urlTemplate, Object... uriVars) {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(urlTemplate, uriVars);
		return new MockRequest(this.mockMvc, builder);
	}

	/**
	 * 创建模拟POST请求
	 *
	 * @param urlTemplate 地址模板
	 * @param uriVars     参数
	 * @return mockRequest 模拟POST请求
	 */
	protected MockRequest mockPost(String urlTemplate, Object... uriVars) {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(urlTemplate, uriVars);
		return new MockRequest(this.mockMvc, builder);
	}

	/**
	 * 创建模拟POST请求，上传文件接口专用
	 *
	 * @param urlTemplate 地址模板
	 * @param uriVars     参数
	 * @return mockRequest 模拟DELETE请求
	 */
	protected MockRequest mockMultipart(String urlTemplate, Object... uriVars) {
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(urlTemplate, uriVars);
		return new MockRequest(this.mockMvc, builder);
	}

	/**
	 * 创建模拟PUT请求
	 *
	 * @param urlTemplate 地址模板
	 * @param uriVars     参数
	 * @return mockRequest 模拟PUT请求
	 */
	protected MockRequest mockPut(String urlTemplate, Object... uriVars) {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(urlTemplate, uriVars);
		return new MockRequest(this.mockMvc, builder);
	}

	/**
	 * 创建模拟PATCH请求
	 *
	 * @param urlTemplate 地址模板
	 * @param uriVars     参数
	 * @return mockRequest 模拟PATCH请求
	 */
	protected MockRequest mockPatch(String urlTemplate, Object... uriVars) {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch(urlTemplate, uriVars);
		return new MockRequest(this.mockMvc, builder);
	}

	/**
	 * 创建模拟DELETE请求
	 *
	 * @param urlTemplate 地址模板
	 * @param uriVars     参数
	 * @return mockRequest 模拟DELETE请求
	 */
	protected MockRequest mockDelete(String urlTemplate, Object... uriVars) {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(urlTemplate, uriVars);
		return new MockRequest(this.mockMvc, builder);
	}
}
