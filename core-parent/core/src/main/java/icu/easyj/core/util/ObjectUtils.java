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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import cn.hutool.core.clone.CloneRuntimeException;
import cn.hutool.core.clone.CloneSupport;
import icu.easyj.core.convert.ConvertUtils;
import icu.easyj.core.exception.ConvertException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 对象工具类
 *
 * @author wangliang181230
 */
public abstract class ObjectUtils {

	//region ----------------- 合并数据

	/**
	 * 克隆模式
	 * 只有不克隆时，目标对象才不需要继承 {@link cn.hutool.core.clone.CloneSupport} 类
	 */
	public enum CloneMode {
		/**
		 * 不克隆
		 */
		NOT_CLONE,

		/**
		 * 数据不为空时才克隆
		 */
		CLONE_ONLY_DATA_NOT_EMPTY,

		/**
		 * 总是克隆
		 */
		ALWAYS_CLONE
	}

	/**
	 * 合并数据到目标对象中
	 *
	 * @param target    目标对象
	 * @param data      数据
	 * @param cloneMode 克隆模式
	 * @param <T>       目标对象类
	 * @return 返回目标对象或克隆对象
	 * @throws CloneRuntimeException 目标对象不支持克隆时，将抛出该异常
	 */
	public static <T> T mergeData(@NonNull T target, @Nullable Map<String, Object> data, CloneMode cloneMode) {
		if (MapUtils.isEmpty(data)) {
			return cloneMode == CloneMode.ALWAYS_CLONE ? ((CloneSupport<T>)target).clone() : target;
		}

		// 新的变量
		T result = (cloneMode != CloneMode.NOT_CLONE ? ((CloneSupport<T>)target).clone() : target);

		// 反射获取所有字段
		Field[] fields = ReflectionUtils.getAllFields(result.getClass());
		Object value;
		for (Field field : fields) {
			// 跳过final字段
			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}

			// 获取值
			value = data.get(field.getName());
			if (value == null) {
				// 值为空时，忽略该数据
				continue;
			}

			// 转换值类型
			if (!value.getClass().equals(field.getType())) {
				try {
					value = ConvertUtils.convert(value, field.getType());
				} catch (ConvertException ignore) {
					// 转换失败时，忽略该字段
					continue;
				}
			}

			// 设置值
			try {
				field.set(result, value);
			} catch (IllegalAccessException ignore) {
				// 设置失败时，忽略该字段
			}
		}

		return result;
	}

	/**
	 * 合并数据到目标对象中
	 *
	 * @param target 目标对象
	 * @param data   数据
	 * @param <T>    目标对象类
	 * @return 返回目标对象或克隆对象（当数据不为空时，才会克隆一份对象）
	 * @throws CloneRuntimeException 目标对象不支持克隆时，将抛出该异常
	 */
	public static <T> T mergeData(@NonNull T target, @Nullable Map<String, Object> data) {
		return mergeData(target, data, CloneMode.CLONE_ONLY_DATA_NOT_EMPTY);
	}

	//endregion

	/**
	 * 判断对象是否与数组中的某个元素相等
	 *
	 * @param obj   对象
	 * @param array 对象数组
	 * @param <T>   对象类型
	 * @return 是否存在相等的元素
	 */
	@SafeVarargs
	public static <T> boolean in(T obj, T... array) {
		for (T t : array) {
			if (Objects.equals(obj, t)) {
				return true;
			}
		}
		return false;
	}


	//region ----------------- defaultIfNull、defaultIfEmpty

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
			return (T)CollectionUtils.defaultIfEmpty((Collection<?>)obj, (Collection<?>)defaultValue);
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
				|| (obj instanceof Collection && CollectionUtils.isEmpty((Collection<?>)obj))
				|| (obj instanceof Map && MapUtils.isEmpty((Map<?, ?>)obj))
				|| (obj.getClass().isArray() && Array.getLength(obj) == 0)) {
			return defaultValueSupplier.get();
		}

		return obj;
	}

	//endregion
}
