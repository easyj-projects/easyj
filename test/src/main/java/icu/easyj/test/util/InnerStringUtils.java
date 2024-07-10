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
package icu.easyj.test.util;

/**
 * Test内部使用 StringUtils
 *
 * @author wangliang181230
 */
class InnerStringUtils {

	//region 中文相关方法

	/**
	 * 判断是否为中文字符
	 *
	 * @param c 字符
	 * @return 是否为中文字符
	 */
	public static boolean isChinese(final char c) {
		return c >= 0x4E00 && c <= 0x9FA5;
	}

	/**
	 * 计算字符串长度，中文计2个字符
	 *
	 * @param cs 字符串
	 * @return strLength 字符串
	 */
	public static int chineseLength(final CharSequence cs) {
		if (isEmpty(cs)) {
			return 0;
		}

		final int length = cs.length();
		int resultLength = length; // 返回长度
		for (int i = 0; i < length; ++i) {
			if (isChinese(cs.charAt(i))) {
				++resultLength;
			}
		}

		return resultLength;
	}

	//endregion


	//region 判断方法

	/**
	 * 字符串是否为空
	 *
	 * @param cs 字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	//endregion
}
