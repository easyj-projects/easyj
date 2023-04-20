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
package icu.easyj.core.util;

import java.io.CharArrayWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Map;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.URLUtil;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import static icu.easyj.core.constant.UrlConstants.HTTPS_PRE;
import static icu.easyj.core.constant.UrlConstants.HTTP_PRE;

/**
 * URL工具类
 *
 * @author wangliang181230
 */
public abstract class UrlUtils {

	//region 这段代码从UrlEncoder中复制过来的，用于encode方法。

	/**
	 * 无需进行URL编码转义的字符集合.
	 * 参照：RFC 2396
	 */
	static final BitSet DONT_NEED_URL_ENCODE;

	static {
		DONT_NEED_URL_ENCODE = new BitSet(128);
		int i;
		for (i = 'a'; i <= 'z'; i++) {
			DONT_NEED_URL_ENCODE.set(i);
		}
		for (i = 'A'; i <= 'Z'; i++) {
			DONT_NEED_URL_ENCODE.set(i);
		}
		for (i = '0'; i <= '9'; i++) {
			DONT_NEED_URL_ENCODE.set(i);
		}
		DONT_NEED_URL_ENCODE.set(' ');
		DONT_NEED_URL_ENCODE.set('-');
		DONT_NEED_URL_ENCODE.set('_');
		DONT_NEED_URL_ENCODE.set('.');
		DONT_NEED_URL_ENCODE.set('*');
	}

	//endregion


	//region 标准化

	/**
	 * 标准化路径
	 * <ol>
	 *     <li>"\"替换为"/"</li>
	 *     <li>为URL时，取路径</li>
	 *     <li>连续的'/'和\s，替换为单个'/'</li>
	 *     <li>移除最后一位'/'</li>
	 *     <li>前面补齐’/‘</li>
	 * </ol>
	 *
	 * @param path 路径
	 * @return path 标准化后的路径
	 */
	@NonNull
	public static String normalizePath(String path) {
		Assert.notNull(path, "'path' must not be null");

		path = path.trim();

		if (path.isEmpty()) {
			return StrPool.SLASH;
		}

		// "\"替换为"/"
		path = path.replace(CharPool.BACKSLASH, CharPool.SLASH);

		// 为URL时，取路径
		if (path.startsWith(HTTP_PRE) || path.startsWith(HTTPS_PRE)) {
			path = URLUtil.getPath(path);
		}

		// 连续的'/'和\s，替换为单个'/'
		path = path.replaceAll("[/\\s]+", StrPool.SLASH);

		// 移除最后一位'/'
		if (path.length() > 1 && CharPool.SLASH == path.charAt(path.length() - 1)) {
			path = path.substring(0, path.length() - 1);
		}

		// 前面补齐’/‘
		if (CharPool.SLASH != path.charAt(0)) {
			path = StrPool.SLASH + path;
		}

		return path;
	}

	//endregion


	//region URL参数编码解码

	////region encode

	/**
	 * 字符串进行URL编码。<br>
	 * 代码是从 OpenJDK8 {@link java.net.URLEncoder#encode(String, String)} 中复制过来，并进行了优化：
	 * - 1、编码入参也由String直接变成了Charset；
	 * - 2、StringBuffer变为StringBuilder
	 *
	 * @param s       字符串
	 * @param charset 字符集
	 * @return 编码后的字符串
	 * @throws IllegalArgumentException s或charset为空时，将抛出该异常
	 */
	public static String encode(String s, Charset charset) {
		Assert.notNull(s, "'s' must not be null");
		Assert.notNull(charset, "'charset' must not be null");

		boolean needToChange = false;
		final StringBuilder sb = new StringBuilder(s.length());
		try (final CharArrayWriter charArrayWriter = new CharArrayWriter()) {
			int c;
			for (int i = 0; i < s.length(); ) {
				c = s.charAt(i);
				if (DONT_NEED_URL_ENCODE.get(c)) {
					if (c == ' ') {
						c = '+';
						needToChange = true;
					}
					sb.append((char)c);
					i++;
				} else {
					do {
						charArrayWriter.write(c);
						if (c >= 0xD800 && c <= 0xDBFF) {
							if ((i + 1) < s.length()) {
								int d = s.charAt(i + 1);
								if (d >= 0xDC00 && d <= 0xDFFF) {
									charArrayWriter.write(d);
									i++;
								}
							}
						}
						i++;
					} while (i < s.length() && !DONT_NEED_URL_ENCODE.get((c = s.charAt(i))));

					charArrayWriter.flush();
					String str = charArrayWriter.toString();
					byte[] ba = str.getBytes(charset);
					for (byte b : ba) {
						sb.append('%');
						char ch = Character.forDigit((b >> 4) & 0xF, 16);
						if (Character.isLetter(ch)) {
							ch -= StringUtils.CASE_DIFF;
						}
						sb.append(ch);
						ch = Character.forDigit(b & 0xF, 16);
						if (Character.isLetter(ch)) {
							ch -= StringUtils.CASE_DIFF;
						}
						sb.append(ch);
					}
					charArrayWriter.reset();
					needToChange = true;
				}
			}
		}

		return (needToChange ? sb.toString() : s);
	}

	/**
	 * 字符串进行URL编码，编码方式：UTF-8
	 *
	 * @param s 字符串
	 * @return 编码后的字符串
	 */
	public static String encode(String s) {
		return encode(s, StandardCharsets.UTF_8);
	}

	////endregion


	////region decode

	/**
	 * 字符串进行URL解码.<br>
	 * 代码是从 OpenJDK8 {@link java.net.URLDecoder#decode(String, String)} 中复制过来，并进行了优化：
	 * - 1、编码入参也由String直接变成了Charset；
	 * - 2、StringBuffer变为StringBuilderc
	 *
	 * @param s                  字符串
	 * @param charset            字符集
	 * @param needHandlePlusChar 是否需要处理 '+' 字符
	 * @return 解码后的字符串
	 * @throws IllegalArgumentException s或charset为空时，将抛出该异常
	 */
	private static String decode(String s, Charset charset, boolean needHandlePlusChar) {
		Assert.notNull(s, "'s' must not be null");
		Assert.notNull(charset, "'charset' must not be null");

		final int numChars = s.length();
		final StringBuilder sb = new StringBuilder(numChars > 500 ? numChars / 2 : numChars);

		boolean needToChange = false;
		char c;
		byte[] bytes = null;
		int i = 0;
		while (i < numChars) {
			c = s.charAt(i);
			switch (c) {
				case '+':
					if (needHandlePlusChar) {
						sb.append(' ');
						i++;
						needToChange = true;
						break;
					} else {
						sb.append(c);
						i++;
						break;
					}
				case '%':
					try {
						if (bytes == null) {
							bytes = new byte[(numChars - i) / 3];
						}
						int pos = 0;

						while ((i + 2) < numChars && c == '%') {
							int v = Integer.parseInt(s.substring(i + 1, i + 3), 16);
							if (v < 0) {
								throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
							}
							bytes[pos++] = (byte)v;
							i += 3;
							if (i < numChars) {
								c = s.charAt(i);
							}
						}

						if (i < numChars && c == '%') {
							throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
						}

						sb.append(new String(bytes, 0, pos, charset));
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - " + e.getMessage());
					}
					needToChange = true;
					break;
				default:
					sb.append(c);
					i++;
					break;
			}
		}

		return (needToChange ? sb.toString() : s);
	}

	/**
	 * 字符串进行URL解码
	 *
	 * @param s                  字符串
	 * @param needHandlePlusChar 是否需要处理 '+' 字符
	 * @return 解码后的字符串
	 */
	private static String decode(String s, boolean needHandlePlusChar) {
		return decode(s, StandardCharsets.UTF_8, needHandlePlusChar);
	}

	/**
	 * 字符串进行URL解码.<br>
	 * 代码是从 OpenJDK8 {@link java.net.URLDecoder#decode(String, String)} 中复制过来，并进行了优化：
	 * - 1、编码入参也由String直接变成了Charset；
	 * - 2、StringBuffer变为StringBuilderc
	 *
	 * @param s       字符串
	 * @param charset 字符集
	 * @return 解码后的字符串
	 * @throws IllegalArgumentException s或charset为空时，将抛出该异常
	 */
	public static String decode(String s, Charset charset) {
		return decode(s, charset, true);
	}

	/**
	 * 字符串进行URL解码
	 *
	 * @param s 字符串
	 * @return 解码后的字符串
	 */
	public static String decode(String s) {
		return decode(s, StandardCharsets.UTF_8, true);
	}

	/**
	 * 字符串进行URL解码（用于反编译Base64串）.<br>
	 * 代码是从 OpenJDK8 {@link java.net.URLDecoder#decode(String, String)} 中复制过来，并进行了优化：
	 * - 1、编码入参也由String直接变成了Charset；
	 * - 2、StringBuffer变为StringBuilderc
	 *
	 * @param s       字符串
	 * @param charset 字符集
	 * @return 解码后的字符串
	 * @throws IllegalArgumentException s或charset为空时，将抛出该异常
	 */
	public static String decodeForBase64(String s, Charset charset) {
		return decode(s, charset, false);
	}

	/**
	 * 字符串进行URL解码（用于反编译Base64串）.
	 *
	 * @param s 字符串
	 * @return 解码后的字符串
	 */
	public static String decodeForBase64(String s) {
		return decode(s, StandardCharsets.UTF_8, false);
	}

	////endregion

	//endregion


	//region 参数处理

	/**
	 * 拼接QueryString参数
	 *
	 * @param urlOrPath      原URL或Path
	 * @param queryStringMap 参数Map（键和值都未encode过）
	 * @return 拼接好参数后的完整URL或Path
	 */
	@NonNull
	public static String joinQueryString(@NonNull String urlOrPath, Map<String, String> queryStringMap) {
		Assert.notNull(urlOrPath, "'urlOrPath' must not be null");

		if (MapUtils.isEmpty(queryStringMap)) {
			return urlOrPath;
		}

		String hashParam;
		int idx = urlOrPath.indexOf('#');
		if (idx == 0) {
			hashParam = urlOrPath;
		} else if (idx > 0) {
			hashParam = urlOrPath.substring(idx);
			urlOrPath = urlOrPath.substring(0, idx);
		} else {
			hashParam = null;
		}

		boolean isFirstParam = true;
		char firstParamSplicerChar = (StringUtils.contains(urlOrPath, '?') ? '&' : '?');
		StringBuilder sb = new StringBuilder(urlOrPath);
		urlOrPath = null; // 设为null，方便GC回收
		for (Map.Entry<String, String> kv : queryStringMap.entrySet()) {
			if (isFirstParam) {
				sb.append(firstParamSplicerChar);
				isFirstParam = false;
			} else {
				sb.append('&');
			}

			sb.append(UrlUtils.encode(kv.getKey())).append('=');
			if (kv.getValue() != null) {
				sb.append(UrlUtils.encode(kv.getValue()));
			}
		}

		if (hashParam != null) {
			sb.append(hashParam);
		}

		return sb.toString();
	}

	//endregion
}
