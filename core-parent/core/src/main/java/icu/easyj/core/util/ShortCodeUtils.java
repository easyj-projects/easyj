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

import org.springframework.lang.NonNull;

/**
 * 短字符串工具类
 *
 * @author wangliang181230
 */
public abstract class ShortCodeUtils {

	/**
	 * 默认的自定义进制（0、1没有加入，容易与O、L、I混淆）
	 */
	private static final char[] DEFAULT_CHARS_TABLE = new char[]{
			'Q', 'W', 'E', '8', 'A', 'S', '2', 'D', 'Z', 'X',
			'9', 'C', '7', 'P', '5', 'I', 'K', '3', 'M', 'J',
			'U', 'F', 'R', '4', 'V', 'Y', 'L', 'T', 'N', '6',
			'B', 'G', 'H',
	};

	/**
	 * 默认的分隔字符（不能与自定义进制中的字符重复）
	 */
	private static final char DEFAULT_SPLIT_CHAR = 'O';

	/**
	 * 默认的短字符串最小长度
	 */
	private static final int DEFAULT_MIN_LENGTH = 5;


	//region toShortCode

	/**
	 * 根据ID生成短字符串
	 *
	 * @param id         ID（必须大于等于0）
	 * @param charsTable 自定义进制字符表
	 * @param splitChar  正式字符与补位字符之间的分隔符
	 * @param minLength  最小字符长度
	 * @return 短字符串
	 */
	public static String toCode(long id, @NonNull char[] charsTable, char splitChar, int minLength) {
		int tableLength = charsTable.length;

		String str;
		if (id > 0) {
			double power = Math.log(id) / Math.log(tableLength); // Math.pow(tableLength, power) == id
			int charPos = (int)power + 1;

			// 生成字符数组
			char[] buf = new char[charPos];
			while (id > 0) {
				int index = (int)(id % tableLength);
				buf[--charPos] = charsTable[index];
				id /= tableLength;
			}

			str = new String(buf);
		} else {
			str = "";
		}

		// 不够长度的自动随机补全
		if (str.length() < minLength) {
			StringBuilder sb = new StringBuilder();
			sb.append(splitChar);
			Random rnd = new Random();
			for (int i = 1; i < minLength - str.length(); i++) {
				sb.append(charsTable[rnd.nextInt(tableLength)]);
			}
			str += sb.toString();
		}
		return str;
	}

	/**
	 * 根据ID生成短字符串（最小长度6位）
	 *
	 * @param id        ID
	 * @param minLength 最小字符长度
	 * @return 短字符串
	 */
	public static String toCode(long id, int minLength) {
		return toCode(id, DEFAULT_CHARS_TABLE, DEFAULT_SPLIT_CHAR, minLength);
	}

	/**
	 * 根据ID生成短字符串（最小长度6位）
	 *
	 * @param id ID
	 * @return 短字符串
	 */
	public static String toCode(long id) {
		return toCode(id, DEFAULT_CHARS_TABLE, DEFAULT_SPLIT_CHAR, DEFAULT_MIN_LENGTH);
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
	 */
	public static long toId(String code, char[] charsTable, char splitChar) {
		int tableLength = charsTable.length;

		char[] chars = code.toCharArray();
		long res = 0L;
		for (int i = 0; i < chars.length; i++) {
			int ind = 0;
			for (int j = 0; j < tableLength; j++) {
				if (chars[i] == charsTable[j]) {
					ind = j;
					break;
				}
			}
			if (chars[i] == splitChar) {
				break;
			}
			if (i > 0) {
				res = res * tableLength + ind;
			} else {
				res = ind;
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
		return toId(code, DEFAULT_CHARS_TABLE, DEFAULT_SPLIT_CHAR);
	}

	//endregion
}
