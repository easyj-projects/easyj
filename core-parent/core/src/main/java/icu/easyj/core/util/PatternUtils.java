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

	/**
	 * 中文字符正则
	 */
	public final static String REGEX_CHINESE_CHAR = "[\u4E00-\u9FA5]";

	//region 代码匹配的正则

	// 各类型数据值的匹配
	public static final String REGEX_CODE_STRING1 = "'((?<=\\\\)'|[^'])*'"; // 单引号字符串
	public static final String REGEX_CODE_STRING2 = "\"((?<=\\\\)\"|[^\"])*\""; // 双引号字符串
	public static final String REGEX_CODE_STRING = "(" + REGEX_CODE_STRING1 + "|" + REGEX_CODE_STRING2 + ")"; // 单引号或双引号字符串
	public static final String REGEX_CODE_NUMBER = "-?(\\d+((\\.\\d*[fFdD]?)|[lL])?)"; // 数字，F、D、L是明确类型时使用
	public static final String REGEX_CODE_BOOLEAN = "(true|false)"; // 布尔类型
	public static final String REGEX_CODE_NULL = "null"; // null
	// 所有类型的数据值匹配
	public static final String REGEX_CODE_VALUE = "(" + REGEX_CODE_STRING + "|" + REGEX_CODE_NUMBER + "|" + REGEX_CODE_BOOLEAN + "|" + REGEX_CODE_NULL + ")"; // 字符串（含单引号及双引号）、数字（含浮点数字）、布尔类型、null
	public static final Pattern P_CODE_DATA_VALUE = Pattern.compile(REGEX_CODE_VALUE);

	// 单行执行代码正则，不允许存在空格
	public static final String REGEX_CODE_LINE = "^(\\w+)(\\.(\\w+)(\\((" + REGEX_CODE_VALUE + "(,\\s{0,1}" + REGEX_CODE_VALUE + ")*)?\\)(?=(;|$)))?)?;?$";
	public static final Pattern P_CODE_LINE = Pattern.compile(REGEX_CODE_LINE);

	// 单行执行代码正则，允许存在空格
	public static final String REGEX_CODE_LINE2 = "^(\\w+)\\s*(\\.\\s*(\\w+)\\s*(\\(\\s*(" + REGEX_CODE_VALUE + "\\s*(,\\s*" + REGEX_CODE_VALUE + "\\s*)*)?\\)(?=(\\s|;|$)))?)?\\s*;?$";
	public static final Pattern P_CODE_LINE2 = Pattern.compile(REGEX_CODE_LINE2);

	//endregion

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
}
