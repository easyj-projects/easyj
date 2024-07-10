/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.spring.boot.test.result;

import icu.easyj.spring.boot.test.MockResponse;

/**
 * 响应结果基类
 *
 * @author wangliang181230
 */
public class BaseResult {

	protected final MockResponse mockResponse;

	/**
	 * 构造函数
	 *
	 * @param mockResponse 模拟响应
	 */
	public BaseResult(MockResponse mockResponse) {
		this.mockResponse = mockResponse;
	}


	/**
	 * 结束对当前响应结果的校验，可继续对下一个响应结果的校验，也可直接结束不校验了。
	 * 部分类型的响应结果，为一次性校验，无需主动调用此方法。
	 *
	 * @return mockResponse
	 */
	public MockResponse end() {
		return this.mockResponse;
	}
}
