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

import java.util.Random;

import cn.hutool.core.util.StrUtil;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 短字符串工具类
 *
 * @author wangliang181230
 */
public abstract class ShortCodeUtils {

	/**
	 * 默认的自定义进制
	 * 不含分隔字符，见常量：{@link #DEFAULT_SPLIT_CHAR}
	 */
	private static final char[] DEFAULT_CHAR_TABLE = new char[]{
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			/*'0',*/ '1', '2', '3', '4', '5', '6', '7', '8', '9'
	};

	/**
	 * 默认的分隔字符（不能与自定义进制中的字符重复）
	 */
	private static final char DEFAULT_SPLIT_CHAR = '0';

	/**
	 * 默认的短字符串最小长度
	 */
	private static final int DEFAULT_MIN_LENGTH = 5;


	//region toShortCode

	/**
	 * 根据ID生成短字符串
	 *
	 * @param id        ID（必须大于等于0）
	 * @param charTable 自定义进制字符集
	 * @param splitChar 正式字符与补位字符之间的分隔符（该字符不与chars参数中的任意字符相同）
	 * @param minLength 最小字符长度
	 * @return 短字符串
	 * @throws IllegalArgumentException ID小于0 或 charTable为null
	 */
	public static String toCode(long id, @NonNull char[] charTable, char splitChar, int minLength) {
		Assert.isTrue(id >= 0, "ID必须大于等于0");
		Assert.notNull(charTable, "'charTable' must not be null");

		// 自定义进制字符集长度
		int charTableLength = charTable.length;

		String str;
		if (id > 0) {
			double power = Math.log(id) / Math.log(charTableLength); // Math.pow(charTableLength, power) == id
			int charPos = (int)(power) + 2;

			// 生成字符数组
			char[] buf = new char[charPos];
			while (id > 0) {
				int index = (int)(id % charTableLength);
				buf[--charPos] = charTable[index];
				id /= charTableLength;
			}

			if (charPos == 0) {
				str = new String(buf);
			} else {
				str = new String(buf, charPos, (buf.length - charPos));
			}
		} else {
			str = StrUtil.EMPTY;
		}

		// 不够长度的自动随机补全
		if (str.length() < minLength) {
			StringBuilder sb = new StringBuilder();
			sb.append(splitChar);
			Random rnd = new Random();
			for (int i = 1; i < minLength - str.length(); i++) {
				sb.append(charTable[rnd.nextInt(charTableLength)]);
			}
			str += sb.toString();
		}
		return str;
	}

	/**
	 * 根据ID生成短字符串（最小长度5）
	 *
	 * @param id        ID（必须大于等于0）
	 * @param charTable 自定义进制字符集
	 * @param splitChar 正式字符与补位字符之间的分隔符（该字符不与chars参数中的任意字符相同）
	 * @return 短字符串
	 * @throws IllegalArgumentException ID小于0 或 charTable为null
	 */
	public static String toCode(long id, @NonNull char[] charTable, char splitChar) {
		return toCode(id, charTable, splitChar, DEFAULT_MIN_LENGTH);
	}

	/**
	 * 根据ID生成短字符串（最小长度6位）
	 *
	 * @param id        ID
	 * @param minLength 最小字符长度
	 * @return 短字符串
	 * @throws IllegalArgumentException ID小于0
	 */
	public static String toCode(long id, int minLength) {
		return toCode(id, DEFAULT_CHAR_TABLE, DEFAULT_SPLIT_CHAR, minLength);
	}

	/**
	 * 根据ID生成短字符串（最小长度6位）
	 *
	 * @param id ID
	 * @return 短字符串
	 * @throws IllegalArgumentException ID小于0
	 */
	public static String toCode(long id) {
		return toCode(id, DEFAULT_CHAR_TABLE, DEFAULT_SPLIT_CHAR, DEFAULT_MIN_LENGTH);
	}

	//endregion


	//region toId

	/**
	 * 短字符串转为64位长整形ID
	 *
	 * @param code       短字符串
	 * @param charsTable 自定义进制
	 * @param splitChar  分隔字符
	 * @return 原ID
	 * @throws IllegalArgumentException charTable为null
	 */
	public static long toId(String code, @NonNull char[] charsTable, char splitChar) {
		if (StringUtils.isEmpty(code)) {
			return 0L;
		}

		int tableLength = charsTable.length;

		char[] chars = code.toCharArray();
		long res = 0L;
		for (int i = 0; i < chars.length; i++) {
			int index = 0;
			for (int j = 0; j < tableLength; j++) {
				if (chars[i] == charsTable[j]) {
					index = j;
					break;
				}
			}
			if (chars[i] == splitChar) {
				break;
			}
			if (i > 0) {
				res = res * tableLength + index;
			} else {
				res = index;
			}
		}
		return res;
	}

	/**
	 * 短字符串转为64位长整形ID
	 *
	 * @param code 短字符串
	 * @return 原ID
	 */
	public static long toId(String code) {
		return toId(code, DEFAULT_CHAR_TABLE, DEFAULT_SPLIT_CHAR);
	}

	//endregion
}
