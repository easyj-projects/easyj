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
import org.junit.jupiter.api.Assertions;

/**
 * 响应头结果
 *
 * @author wangliang181230
 */
public class ContentResult extends GenericContentResult<String, ContentResult> {

	public ContentResult(MockResponse mockResponse, String content) {
		super(mockResponse, content);
	}


	//region 校验方法

	/**
	 * 校验响应内容
	 *
	 * @param expectedContent 预期响应内容
	 * @return self
	 */
	public ContentResult is(String expectedContent) {
		Assertions.assertEquals(expectedContent, super.content);
		return this;
	}

	/**
	 * 可自定义验证方法
	 *
	 * @param expectedValidateFun 预期值的验证函数
	 * @return self
	 */
	@Override
	public ContentResult is(Consumer<String> expectedValidateFun) {
		expectedValidateFun.accept(super.content);
		return this;
	}

	//endregion
}
