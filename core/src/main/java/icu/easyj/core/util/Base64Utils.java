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
	 * 规范化Base64串
	 *
	 * @param base64Str Base64字符串
	 * @return 规范化后的Base64串
	 */
	public static String normalize(String base64Str) {
		if (base64Str.contains(" ")) {
			base64Str = base64Str.replace(" ", "+");
		}
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
		if (base64Str.contains("\n") || base64Str.contains("\r")) {
			base64Str = P_CRLF.matcher(base64Str).replaceAll("");
		}
		return base64Str;
	}
}
