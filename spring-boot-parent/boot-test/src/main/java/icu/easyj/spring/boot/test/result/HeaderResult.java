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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.PatternMatchUtils;

/**
 * 响应头结果
 *
 * @author wangliang181230
 */
public class HeaderResult extends BaseResult {

	private final MockHttpServletResponse response;

	public HeaderResult(MockResponse mockResponse, MockHttpServletResponse response) {
		super(mockResponse);
		this.response = response;
	}


	//region 校验方法

	/**
	 * 判断响应头的值
	 *
	 * @param headerName          响应头键
	 * @param expectedHeaderValue 响应头预期值
	 * @return self
	 */
	public HeaderResult is(String headerName, Object expectedHeaderValue) {
		Object headerValue = response.getHeaderValue(headerName);
		Assertions.assertEquals(expectedHeaderValue, headerValue);
		return this;
	}

	/**
	 * 根据匹配串判断响应头的值的格式
	 *
	 * @param headerName                 响应头键
	 * @param expectedHeaderValuePattern 响应头预期格式串，可以是正则，也可以是简易匹配串
	 * @return self
	 */
	public HeaderResult isMatch(String headerName, String expectedHeaderValuePattern) {
		Object headerValue = response.getHeaderValue(headerName);
		Assertions.assertNotNull(headerValue);
		if (expectedHeaderValuePattern.startsWith("^")) {
			Assertions.assertTrue(PatternUtils.validate(expectedHeaderValuePattern, headerValue.toString()));
		} else {
			Assertions.assertTrue(PatternMatchUtils.simpleMatch(expectedHeaderValuePattern, headerValue.toString()));
		}

		return this;
	}

	/**
	 * 根据正则判断响应头的值的格式
	 *
	 * @param headerName                 响应头键
	 * @param expectedHeaderValuePattern 响应头预期格式正则
	 * @return self
	 */
	public HeaderResult isMatch(String headerName, Pattern expectedHeaderValuePattern) {
		Object headerValue = response.getHeaderValue(headerName);
		Assertions.assertNotNull(headerValue);
		Assertions.assertTrue(PatternUtils.validate(expectedHeaderValuePattern, headerValue.toString()));
		return this;
	}

	/**
	 * 判断响应头是否存在
	 *
	 * @param headerName 响应头键
	 * @return self
	 */
	public HeaderResult contains(String headerName) {
		Assertions.assertTrue(response.containsHeader(headerName));
		return this;
	}

	/**
	 * 判断响应头是否不存在
	 *
	 * @param headerName 响应头键
	 * @return self
	 */
	public HeaderResult notContains(String headerName) {
		Assertions.assertFalse(response.containsHeader(headerName));
		return this;
	}

	//endregion
}
