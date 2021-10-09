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

import java.nio.charset.StandardCharsets;

import icu.easyj.core.loader.EnhancedServiceLoader;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Base64工具类
 *
 * @author wangliang181230
 * @see <a href="https://baike.baidu.com/item/base64">Base64原理_百度百科</a>
 * @see <a href="https://segmentfault.com/a/1190000012654771">Base64原理（这偏文章的图片画的很不错，比较容易理解）</a>
 */
public abstract class Base64Utils {

	/**
	 * Base64服务的实现
	 */
	private static final IBase64Service BASE64_SERVICE = EnhancedServiceLoader.load(IBase64Service.class);


	/**
	 * Base64字符在Assic码
	 */
	public static final byte[] BASE64_CHAR_TABLE = {
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
	 * Base64字符集中，ASSIC码最小的字符：'+'
	 */
	public static final byte MIN_BASE64_BYTE = (byte)'+';

	/**
	 * Base64字符集中，ASSIC码最大的字符：'z'
	 */
	public static final byte MAX_BASE64_BYTE = (byte)'z';

	/**
	 * 补位字符：'='
	 */
	public static final byte PADDING_CHAR = (byte)'=';

	/**
	 * 规范化Base64串.
	 *
	 * @param base64Str Base64字符串
	 * @return 规范化后的Base64串
	 * @throws IllegalArgumentException 编码有误
	 */
	@NonNull
	public static String normalize(@NonNull String base64Str) throws IllegalArgumentException {
		Assert.notNull(base64Str, "'base64Str' must not be null");

		boolean needToChange = false;

		int base64Length = base64Str.length();
		StringBuilder sb = new StringBuilder(base64Length > 500 ? base64Length / 2 : base64Length);

		int i = 0;
		char c;
		byte[] bytes = null;
		int pos;
		String hex;
		int v;
		while (i < base64Length) {
			c = base64Str.charAt(i);
			switch (c) {
				// 空格或'-' -> '+'
				case ' ':
				case '-': // hutool 使用UrlSafe模式生成的Base64串里，该字符表示'+'
					sb.append('+');
					i++;
					needToChange = true;
					break;
				// '_' -> '/'
				case '_': // hutool 使用UrlSafe模式生成的Base64串里，该字符表示'/'
					sb.append('/');
					i++;
					needToChange = true;
					break;
				// 移除回车符、换行符、双引号（部分前端方法经常会生效带双引号的Base64串）
				case '\r':
				case '\n':
				case '\"':
					i++;
					needToChange = true;
					break;
				// URL编码16进制字符转换
				case '%':
					try {
						if (bytes == null) {
							bytes = new byte[(base64Length - i) / 3];
						}

						pos = 0;
						while (c == '%' && i + 2 < base64Length) {
							hex = base64Str.substring(i + 1, i + 3);
							v = Integer.parseInt(hex, 16);
							if (v < 0) {
								throw new IllegalArgumentException("转义（%）模式时存在非法十六进制字符-负值: %" + hex + " -> " + v);
							}
							bytes[pos++] = (byte)v;
							i += 3;
							if (i < base64Length) {
								c = base64Str.charAt(i);
							}
						}

						if (c == '%' && i < base64Length) {
							throw new IllegalArgumentException("不完整的转义（%）模式");
						}

						sb.append(new String(bytes, 0, pos, StandardCharsets.UTF_8));
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("转义（%）模式中的非法十六进制字符 - " + e.getMessage());
					}
					needToChange = true;
					break;
				default:
					sb.append(c);
					i++;
					break;
			}
		}

		return (needToChange ? sb.toString() : base64Str);
	}

	//region 判断是否为Base64

	/**
	 * 判断是否为Base64字符（除'='号外）
	 *
	 * @param c 字符
	 * @return 是否为Base64字符
	 */
	private static boolean isBase64CharInner(char c) {
		return c >= MIN_BASE64_BYTE && c <= MAX_BASE64_BYTE && BASE64_CHAR_TABLE[c] != -1;
	}

	/**
	 * 判断是否为Base64字节（除'='号外）
	 *
	 * @param b 字节
	 * @return 是否为Base64字节
	 */
	private static boolean isBase64ByteInner(byte b) {
		return b >= MIN_BASE64_BYTE && b <= MAX_BASE64_BYTE && BASE64_CHAR_TABLE[b] != -1;
	}

	/**
	 * 判断是否为Base64字符
	 *
	 * @param c 字符
	 * @return 是否为Base64字符
	 */
	public static boolean isBase64Char(char c) {
		return c == PADDING_CHAR || isBase64CharInner(c);
	}

	/**
	 * 判断是否为Base64字节
	 *
	 * @param b 字节
	 * @return 是否为Base64字节
	 */
	public static boolean isBase64Byte(byte b) {
		return b == PADDING_CHAR || isBase64ByteInner(b);
	}

	/**
	 * 判断是否为Base64字符串
	 * <p>
	 * 注：当前方法不考虑换行符！
	 *
	 * @param cs 字符串
	 * @return 是否为Base64字符串
	 */
	public static boolean isBase64(@Nullable final CharSequence cs) {
		if (cs == null) {
			return false;
		}

		return BASE64_SERVICE.isBase64(cs);
	}

	/**
	 * 判断是否为Base64字符数组
	 * <p>
	 * 注：当前方法不考虑换行符！
	 *
	 * @param chars 字符数组
	 * @return 是否为Base64字符数组
	 */
	public static boolean isBase64Chars(@Nullable final char[] chars) {
		int length;
		if (chars == null || (length = chars.length) < 2) {
			return false;
		}

		// 计算需校验字符的长度，减掉末尾补位字符数量
		char c = chars[length - 1];
		if (c == PADDING_CHAR) {
			// 存在补位字符时，长度必须为4的倍数
			if (length % 4 != 0) {
				return false;
			}
			--length;

			// 最多末尾两个'='
			c = chars[length - 1];
			if (c != PADDING_CHAR && !isBase64CharInner(c)) {
				return false;
			}
		} else {
			// 不存在补位字符时，长度除4的余数不可能为1
			if (length % 4 == 1) {
				return false;
			}
			if (!isBase64CharInner(c)) {
				return false;
			}
		}
		--length;

		// 校验除最后两位的字符
		for (int i = 0; i < length; ++i) {
			if (!isBase64CharInner(chars[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断是否为Base64字节数组
	 * <p>
	 * 注：当前方法不考虑换行符！
	 *
	 * @param bytes 字节数组
	 * @return 是否为Base64字节数组
	 */
	public static boolean isBase64Bytes(@Nullable final byte[] bytes) {
		int length;
		if (bytes == null || (length = bytes.length) < 2) {
			return false;
		}

		// 计算需校验字符的长度，减掉末尾补位字符数量
		byte b = bytes[length - 1];
		if (b == PADDING_CHAR) {
			// 存在补位字符时，长度必须为4的倍数
			if (length % 4 != 0) {
				return false;
			}
			--length;

			// 最多末尾两个'='
			b = bytes[length - 1];
			if (b != PADDING_CHAR && !isBase64ByteInner(b)) {
				return false;
			}
		} else {
			// 不存在补位字符时，长度除4的余数不能为1
			if (length % 4 == 1) {
				return false;
			}
			if (!isBase64ByteInner(b)) {
				return false;
			}
		}
		--length;

		// 校验除最后两位的字符
		for (int i = 0; i < length; ++i) {
			if (!isBase64ByteInner(bytes[i])) {
				return false;
			}
		}

		return true;
	}

	//endregion
}
