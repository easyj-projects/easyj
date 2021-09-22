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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 对象工具类
 *
 * @author wangliang181230
 */
public abstract class ObjectUtils {

	/**
	 * 如果为null，则返回默认值
	 *
	 * @param obj          对象
	 * @param defaultValue 默认值
	 * @param <T>          对象类型
	 * @return 入参对象或默认值
	 */
	public static <T> T defaultIfNull(T obj, T defaultValue) {
		if (obj == null) {
			return defaultValue;
		}

		return obj;
	}

	/**
	 * 如果为null，则执行supplier生成新的值
	 *
	 * @param obj                  对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 入参对象或生成的默认值
	 */
	public static <T> T defaultIfNull(T obj, Supplier<T> defaultValueSupplier) {
		if (obj == null) {
			return defaultValueSupplier.get();
		}

		return obj;
	}

	/**
	 * 如果为空，则返回默认值
	 *
	 * @param obj          对象
	 * @param defaultValue 默认值
	 * @param <T>          对象类型
	 * @return 入参对象或默认值
	 */
	public static <T> T defaultIfEmpty(T obj, T defaultValue) {
		if (obj == null) {
			return defaultValue;
		} else if (obj instanceof CharSequence) {
			return (T)StringUtils.defaultIfEmpty((CharSequence)obj, (CharSequence)defaultValue);
		} else if (obj instanceof Collection) {
			return (T)CollectionUtils.defaultIfEmpty((Collection)obj, (Collection)defaultValue);
		} else if (obj instanceof Map) {
			return (T)MapUtils.defaultIfEmpty((Map<?, ?>)obj, (Map<?, ?>)defaultValue);
		} else if (obj.getClass().isArray()) {
			if (Array.getLength(obj) == 0) {
				return defaultValue;
			}
		}

		return obj;
	}

	/**
	 * 如果为空，则执行supplier生成新的值
	 *
	 * @param obj                  对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 入参对象或生成的默认值
	 */
	public static <T> T defaultIfEmpty(T obj, Supplier<T> defaultValueSupplier) {
		if (obj == null
				|| (obj instanceof CharSequence && StringUtils.isEmpty((CharSequence)obj))
				|| (obj instanceof Collection && CollectionUtils.isEmpty((Collection)obj))
				|| (obj instanceof Map && MapUtils.isEmpty((Map)obj))
				|| (obj.getClass().isArray() && Array.getLength(obj) == 0)) {
			return defaultValueSupplier.get();
		}

		return obj;
	}
}
