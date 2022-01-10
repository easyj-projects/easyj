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
package icu.easyj.db.util;

import icu.easyj.core.util.StringUtils;
import org.springframework.util.Assert;

/**
 * SQL语句工具类
 *
 * @author wangliang181230
 */
public abstract class SqlUtils {

	/**
	 * 移除危险字符
	 * <p>
	 * 只保留数字、字母、下划线。空格也不能包含。
	 *
	 * @param variable 变量值
	 * @return 返回安全的变量值
	 */
	public static String removeDangerousChars(String variable) {
		Assert.notNull(variable, "'variable' must be not null");

		boolean needToChange = false;
		StringBuilder sb = new StringBuilder();

		char[] chars = StringUtils.toCharArray(variable);
		for (char c : chars) {
			if (Character.isLetterOrDigit(c) || c == '_') {
				sb.append(c);
				needToChange = true;
			}
		}

		return needToChange ? sb.toString() : variable;
	}

	/**
	 * 移除危险字符（用于序列名）
	 * <p>
	 * 只保留数字、字母、下划线。空格也不能包含。
	 *
	 * @param seqName 变量值
	 * @return 返回安全的变量值
	 */
	public static String removeDangerousCharsForSeqName(String seqName) {
		Assert.notNull(seqName, "'variable' must be not null");

		boolean needToChange = false;
		StringBuilder sb = new StringBuilder();

		char[] chars = StringUtils.toCharArray(seqName);
		for (char c : chars) {
			if (Character.isLetterOrDigit(c) || c == '_' || c == '`' || c == '.') {
				sb.append(c);
				needToChange = true;
			}
		}

		return needToChange ? sb.toString() : seqName;
	}
}
