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
import java.util.function.Supplier;

/**
 * Map工具类
 *
 * @author wangliang181230
 */
public abstract class MapUtils {

	/**
	 * 判断集合是否为空
	 *
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 判断集合是否不为空
	 *
	 * @param map 集合
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * 如果为空集合，则取默认值
	 *
	 * @param map          集合
	 * @param defaultValue 默认值
	 * @param <T>          集合类型
	 * @return 入参集合或默认值
	 */
	public static <T extends Map<?, ?>> T defaultIfEmpty(T map, T defaultValue) {
		if (isEmpty(map)) {
			return defaultValue;
		}

		return map;
	}

	/**
	 * 如果为空数组，则执行supplier生成新的值
	 *
	 * @param map                  集合
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  集合类型
	 * @return 入参集合或生成的默认值
	 */
	public static <T extends Map<?, ?>> T defaultIfEmpty(final T map, Supplier<T> defaultValueSupplier) {
		if (isEmpty(map)) {
			return defaultValueSupplier.get();
		}

		return map;
	}

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
