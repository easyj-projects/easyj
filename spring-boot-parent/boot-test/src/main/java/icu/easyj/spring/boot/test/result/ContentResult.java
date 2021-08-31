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

import java.util.regex.Pattern;

import icu.easyj.core.util.PatternUtils;
import icu.easyj.spring.boot.test.MockResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.PatternMatchUtils;

/**
 * 响应内容结果
 *
 * @author wangliang181230
 */
public class ContentResult extends GenericContentResult<String> {

	public ContentResult(MockResponse mockResponse, ResultActions resultActions, String content) {
		super(mockResponse, resultActions, content);
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
	 * 校验表达式对应的值
	 *
	 * @param jsonPathExpression JSON路径表达式，详情见开源项目：<a href="https://github.com/jayway/JsonPath">JsonPath</a>
	 * @param expectedValue      预期值
	 * @return self
	 */
	public ContentResult is(String jsonPathExpression, Object expectedValue) {
		try {
			resultActions.andExpect(MockMvcResultMatchers.jsonPath(jsonPathExpression).value(expectedValue));
			return this;
		} catch (Exception e) {
			throw new AssertionError("校验不通过！", e);
		}
	}

	/**
	 * 如果响应内容为列表数据，则校验数据量
	 *
	 * @param expectedSize 预期的列表数据数量
	 * @return self
	 */
	public ContentResult is(int expectedSize) {
		return is("$.length()", String.valueOf(expectedSize));
	}

	/**
	 * 根据匹配串，校验响应内容格式
	 *
	 * @param expectedContentPattern 预期响应内容格式，可以是正则，也可以是简易匹配串
	 * @return self
	 */
	public ContentResult isMatch(String expectedContentPattern) {
		if (expectedContentPattern.startsWith("^")) {
			Assertions.assertTrue(PatternUtils.validate(expectedContentPattern, super.content));
		} else {
			Assertions.assertTrue(PatternMatchUtils.simpleMatch(expectedContentPattern, super.content));
		}
		return this;
	}

	/**
	 * 根据正则，校验响应内容格式
	 *
	 * @param expectedContentPattern 预期响应内容格式正则
	 * @return self
	 */
	public ContentResult isMatch(Pattern expectedContentPattern) {
		Assertions.assertTrue(PatternUtils.validate(expectedContentPattern, super.content));
		return this;
	}

	//endregion
}
