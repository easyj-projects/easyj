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
package icu.easyj.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 正则匹配工具类
 *
 * @author wangliang181230
 */
public abstract class PatternUtils {

	//region 正则

	// BASE64串（严谨）
	public static final String REGEX_BASE64 = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
	public static final Pattern P_BASE64 = Pattern.compile(REGEX_BASE64);

	// BASE64串（不严谨）
	public static final String REGEX_BASE64_2 = "^[A-Za-z0-9+/]+={0,2}$";
	public static final Pattern P_BASE64_2 = Pattern.compile(REGEX_BASE64_2);

	//endregion


	//region 通用正则匹配方法

	/**
	 * 正则匹配
	 *
	 * @param pattern 正则匹配
	 * @param str     字符串
	 * @return isMatch 是否匹配
	 */
	public static boolean validate(@NonNull Pattern pattern, @Nullable CharSequence str) {
		if (str == null) {
			return false;
		}
		Assert.notNull(pattern, "'pattern' must be not null");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 正则匹配
	 *
	 * @param regex 正则匹配
	 * @param str   字符串
	 * @return isMatch 是否匹配
	 */
	public static boolean validate(@NonNull String regex, @Nullable CharSequence str) {
		if (str == null) {
			return false;
		}
		Assert.notNull(regex, "'regex' must be not null");
		Pattern p = Pattern.compile(regex);
		return validate(p, str);
	}

	//endregion


	//region base64串

	/**
	 * 判断是否为Base64的有效字符
	 *
	 * @param c 字符
	 * @return isMatch 是否匹配
	 */
	public static boolean isBase64Char(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || '=' == c || '/' == c || '+' == c;
	}

	/**
	 * 验证：base64串（严格匹配）
	 *
	 * @param str 字符串
	 * @return isMatch 是否匹配
	 */
	public static boolean isBase64Str(CharSequence str) {
		if (str == null || str.length() < 4) {
			return false;
		}

		String s = str.toString();

		// 先对最后一个字符判断一遍，防止性能损耗过大
		char c = s.charAt(s.length() - 1);
		if (!isBase64Char(c)) {
			return false;
		}

		return validate(P_BASE64, s);
	}

	/**
	 * 验证：base64串（不严格匹配）
	 *
	 * @param str 字符串
	 * @return isMatch 是否匹配
	 */
	public static boolean isBase64Str2(CharSequence str) {
		if (str == null || str.length() < 4) {
			return false;
		}

		String s = str.toString();

		// 先对最后一个字符判断一遍，防止性能损耗过大
		char c = s.charAt(s.length() - 1);
		if (!isBase64Char(c)) {
			return false;
		}

		return validate(P_BASE64_2, s);
	}

	//endregion
}
