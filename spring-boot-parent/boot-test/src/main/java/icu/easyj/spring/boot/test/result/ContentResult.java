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
import org.springframework.util.PatternMatchUtils;

/**
 * 响应头结果
 *
 * @author wangliang181230
 */
public class ContentResult extends GenericContentResult<String> {

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
