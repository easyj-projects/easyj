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

import java.util.regex.Pattern;

/**
 * Base64工具类
 *
 * @author wangliang181230
 */
public abstract class Base64Utils {

	public static final String REGEX_CRLF = "[\\r\\n]";
	public static final Pattern P_CRLF = Pattern.compile(REGEX_CRLF + "+");

	/**
	 * Base64字符在Assic码
	 */
	private static final byte[] BASE64_CHAR_TABLE = {
			// 0 1 2 3 4 5 6 7 8 9 A B C D E F
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 00-0F
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 10-1F
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, -1, -1, 47, // 20-2F（含：+ /）
			48, 49, 50, 51, 52, 53, 54, 55, 56, 57, -1, -1, -1, -1, -1, -1, // 30-3F（含：0-9）
			-1, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, // 40-4F（含：大写A~大写O）
			80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, -1, -1, -1, -1, -1, // 50-5F（含：大写P~大写Z）
			-1, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, // 60-6F（含：小写a~小写o）
			112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 // 70-7A（含：小写p-小写z）
	};

	/**
	 * 补位字符
	 */
	public static final char PADDING_CHAR = '=';

	/**
	 * 规范化Base64串
	 *
	 * @param base64Str Base64字符串
	 * @return 规范化后的Base64串
	 */
	public static String normalize(String base64Str) {
		// 处理空白符
		if (base64Str.contains(" ")) {
			base64Str = base64Str.replace(" ", "+");
		}

		// 处理转义符
		if (base64Str.contains("%")) {
			if (base64Str.contains("%25")) {
				base64Str = base64Str.replaceAll("%25", "%");
			}
			if (base64Str.contains("%2B")) {
				base64Str = base64Str.replace("%2B", "+");
			}
			if (base64Str.contains("%2F")) {
				base64Str = base64Str.replace("%2F", "/");
			}
			if (base64Str.endsWith("%3D")) {
				base64Str = base64Str.replace("%3D", "=");
			}
		}

		// 处理换行符
		if (base64Str.contains("\n") || base64Str.contains("\r")) {
			base64Str = P_CRLF.matcher(base64Str).replaceAll("");
		}

		return base64Str;
	}

	/**
	 * 判断是否为Base64字符
	 *
	 * @param c 字符
	 * @return 是否为Base64字符
	 */
	public static boolean isBase64Char(char c) {
//		return (c >= 'a' && c <= 'z')
//				|| (c >= 'A' && c <= 'Z')
//				|| (c >= '0' && c <= '9')
//				|| c == '/' || c == '+';
		return c < BASE64_CHAR_TABLE.length && BASE64_CHAR_TABLE[c] != -1;
	}

	/**
	 * 判断是否为Base64字符串
	 *
	 * @param str 字符串
	 * @return 是否为Base64字符串
	 */
	public static boolean isBase64(CharSequence str) {
		if (str == null || str.length() < 2) {
			return false;
		}

		char[] charArr = str.toString().toCharArray();

		// 计算需校验字符的长度
		int length = charArr.length;
		if (charArr[length - 1] == PADDING_CHAR) {
			if (str.length() % 4 != 0) {
				return false;
			}

			--length;
			// 最多末尾两个'='
			if (charArr[length - 1] == PADDING_CHAR) {
				--length;
			}
		}

		// 校验除最后两位的字符
		char c;
		for (int i = 0; i < length; ++i) {
			c = charArr[i];
			if (!isBase64Char(c)) {
				return false;
			}
		}

		return true;
	}
}