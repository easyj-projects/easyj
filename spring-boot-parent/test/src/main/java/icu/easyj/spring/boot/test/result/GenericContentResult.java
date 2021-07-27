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
package icu.easyj.spring.boot.test.result;

import java.util.function.Consumer;

import icu.easyj.spring.boot.test.MockResponse;

/**
 * 泛型响应内容结果
 *
 * @author wangliang181230
 */
public class GenericContentResult<T, R extends GenericContentResult<T, R>> extends BaseResult {

	protected final T content;

	/**
	 * 构造函数
	 *
	 * @param mockResponse 模拟响应
	 * @param content      响应内容
	 */
	public GenericContentResult(MockResponse mockResponse, T content) {
		super(mockResponse);
		this.content = content;
	}


	//region 自定义校验方法

	/**
	 * 可自定义验证方法
	 *
	 * @param expectedValidateFun 预期值的验证函数
	 * @return self
	 */
	public GenericContentResult<T, R> is(Consumer<T> expectedValidateFun) {
		expectedValidateFun.accept(content);
		return this;
	}

	//endregion
}
