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

import java.util.Map;
import java.util.function.Function;

/**
 * Map工具类
 *
 * @author wangliang181230
 */
public abstract class MapUtils {

	/**
	 * Map to string.
	 *
	 * @param map the map
	 * @return the string
	 */
	public static String toString(final Map<?, ?> map) {
		if (map == null) {
			return "null";
		}
		if (map.isEmpty()) {
			return "{}";
		}

		return CycleDependencyHandler.wrap(map, o -> {
			StringBuilder sb = new StringBuilder(32);
			sb.append("{");
			map.forEach((key, value) -> {
				if (sb.length() > 1) {
					sb.append(", ");
				}
				if (key == map) {
					sb.append("(this ").append(map.getClass().getSimpleName()).append(")");
				} else {
					sb.append(StringUtils.toString(key));
				}
				sb.append(" -> ");
				if (value == map) {
					sb.append("(this ").append(map.getClass().getSimpleName()).append(")");
				} else {
					sb.append(StringUtils.toString(value));
				}
			});
			sb.append("}");
			return sb.toString();
		});
	}

	/**
	 * A temporary workaround for Java 8 specific performance issue JDK-8161372 .<br>
	 * This class should be removed once we drop Java 8 support.
	 *
	 * @param map             the map
	 * @param key             the key
	 * @param mappingFunction the mapping function
	 * @param <K>             the type of key
	 * @param <V>             the type of value
	 * @return the value
	 * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
	 */
	public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<? super K, ? extends V> mappingFunction) {
		V value = map.get(key);
		if (value != null) {
			return value;
		}
		return map.computeIfAbsent(key, mappingFunction);
	}
}
