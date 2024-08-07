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

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 集合工具类
 *
 * @author wangliang181230
 */
public abstract class CollectionUtils extends MapUtils {

	/**
	 * 获取列表的第一项，并保证线程安全。
	 *
	 * @param list 列表
	 * @param <T>  列表项的类型
	 * @return lastItem 返回列表的第一项
	 */
	@Nullable
	public static <T> T getFirst(List<T> list) {
		if (isEmpty(list)) {
			return null;
		}

		try {
			return list.get(0);
		} catch (IndexOutOfBoundsException ignore) {
			return null;
		}
	}

	/**
	 * 获取列表的最后一项，并保证线程安全。
	 * <p>
	 * 存在线程安全问题的原因：`list.size()` 和 `list.get(size - 1)` 两个方法不是原子操作。<br>
	 * <p>
	 * 注意：即使 `List` 本身是线程安全的，也会存在该问题。
	 *
	 * @param list 列表
	 * @param <T>  列表项的类型
	 * @return lastItem 返回列表的最后一项
	 */
	@Nullable
	public static <T> T getLast(List<T> list) {
		if (isEmpty(list)) {
			return null;
		}

		int size;
		while (true) {
			size = list.size();
			if (size == 0) {
				return null;
			}

			try {
				return list.get(size - 1);
			} catch (IndexOutOfBoundsException ignore) {
				// 捕获IndexOutOfBoundsException，并继续尝试获取最后一项
			}
		}
	}

	/**
	 * 判断集合是否为空
	 *
	 * @param coll 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * 判断集合是否不为空
	 *
	 * @param coll 集合
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(Collection<?> coll) {
		return !isEmpty(coll);
	}

	/**
	 * 如果为空集合，则取默认值
	 *
	 * @param coll         集合
	 * @param defaultValue 默认值
	 * @param <T>          集合类型
	 * @return 入参集合或默认值
	 */
	public static <T extends Collection<?>> T defaultIfEmpty(T coll, T defaultValue) {
		if (isEmpty(coll)) {
			return defaultValue;
		}

		return coll;
	}

	/**
	 * 如果为空数组，则执行supplier生成新的值
	 *
	 * @param coll                 集合
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  集合类型
	 * @return 入参集合或生成的默认值
	 */
	public static <T extends Collection<?>> T defaultIfEmpty(final T coll, Supplier<T> defaultValueSupplier) {
		if (isEmpty(coll)) {
			return defaultValueSupplier.get();
		}

		return coll;
	}

	/**
	 * Collection To String.
	 *
	 * @param col 集合
	 * @return str 字符串
	 */
	@NonNull
	public static String toString(final Collection<?> col) {
		if (col == null) {
			return "null";
		}
		if (col.isEmpty()) {
			return "[]";
		}

		return CycleDependencyHandler.wrap(col, o -> {
			StringBuilder sb = new StringBuilder(32);
			sb.append("[");
			for (Object obj : col) {
				if (sb.length() > 1) {
					sb.append(", ");
				}
				if (obj == col) {
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
