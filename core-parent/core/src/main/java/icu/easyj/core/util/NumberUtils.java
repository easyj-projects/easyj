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
package icu.easyj.core.util;

/**
 * 正则匹配工具类
 *
 * @author wangliang181230
 * @since 0.7.7
 */
public abstract class NumberUtils {

	/**
	 * 将科学记数法字符串
	 *
	 * @param scientificNumberStr 科学计数法的数字字符串
	 * @return normalNumberStr 普通数字字符串
	 */
	public static String convertScientificToNormal(String scientificNumberStr) {
		int indexE = scientificNumberStr.toUpperCase().indexOf("E");

		int exponential = Integer.parseInt(scientificNumberStr.substring(indexE + 1));

		scientificNumberStr = scientificNumberStr.substring(0, indexE);

		StringBuilder sb = new StringBuilder();


		boolean afterDOT = false;
		for (char c : scientificNumberStr.toCharArray()) {
			if (c == '.') {
				afterDOT = true;
				continue;
			}

			if (afterDOT && exponential >= 0) {
				if (exponential == 0) {
					sb.append(".");
				}
				exponential--;
			}

			sb.append(c);
		}

		while (exponential > 0) {
			sb.append("0");
			exponential--;
		}

		return sb.toString();
	}

	public static String doubleToString(Double d) {
		return convertScientificToNormal(d.toString());
	}

}
