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

import org.springframework.lang.NonNull;

/**
 * 数组工具类
 *
 * @author wangliang181230
 */
public abstract class ArrayUtils {

	/**
	 * Array To String.
	 *
	 * @param objectArray 对象数组
	 * @return str 字符串
	 */
	@NonNull
	public static String toString(final Object[] objectArray) {
		if (objectArray == null) {
			return "null";
		}
		if (objectArray.length == 0) {
			return "[]";
		}

		return CycleDependencyHandler.wrap(objectArray, o -> {
			StringBuilder sb = new StringBuilder(32);
			sb.append("[");
			for (Object obj : objectArray) {
				if (sb.length() > 1) {
					sb.append(", ");
				}
				if (obj == objectArray) {
					sb.append("(this ").append(obj.getClass().getSimpleName()).append(")");
				} else {
					sb.append(StringUtils.toString(obj));
				}
			}
			sb.append("]");
			return sb.toString();
		});
	}
}
