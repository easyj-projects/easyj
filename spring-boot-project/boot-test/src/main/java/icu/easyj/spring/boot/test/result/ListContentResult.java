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

import java.util.List;

import icu.easyj.core.util.StringUtils;
import icu.easyj.spring.boot.test.MockResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.ResultActions;

/**
 * 列表响应内容结果
 *
 * @author wangliang181230
 */
public class ListContentResult<T> extends GenericContentResult<List<T>> {

	/**
	 * 构造函数
	 *
	 * @param mockResponse  模拟响应
	 * @param resultActions 模拟返回操作
	 * @param list          响应内容
	 */
	public ListContentResult(MockResponse mockResponse, ResultActions resultActions, List<T> list) {
		super(mockResponse, resultActions, list);
	}


	//region 自定义校验方法

	/**
	 * 校验列表数据量
	 *
	 * @param expectedSize 预期的数据量
	 * @return self
	 */
	public ListContentResult<T> is(int expectedSize) {
		Assertions.assertEquals(expectedSize, content.size());
		return this;
	}

	/**
	 * 校验数据
	 *
	 * @param expectedContent 预期的内容
	 * @return self
	 */
	public ListContentResult<T> is(String expectedContent) {
		Assertions.assertEquals(expectedContent, StringUtils.toString(this.content));
		return this;
	}

	//endregion
}
