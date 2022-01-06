/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.spring.boot.test.util;

import icu.easyj.core.util.StringUtils;

/**
 * 断言工具类
 *
 * @author wangliang181230
 */
public abstract class AssertionUtils {

	/**
	 * 生成AssertionError对象
	 *
	 * @param message  错误提示信息
	 * @param expected 预期值
	 * @param actual   实际值
	 * @return error 错误
	 */
	public static AssertionError error(String message, Object expected, Object actual) {
		return new AssertionError(message
				+ "\r\nExpected: " + StringUtils.toString(expected)
				+ "\r\n  Actual: " + StringUtils.toString(actual));
	}
}
