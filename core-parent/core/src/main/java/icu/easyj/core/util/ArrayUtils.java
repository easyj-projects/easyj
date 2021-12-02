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
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Supplier;

import org.springframework.lang.NonNull;

/**
 * 数组工具类
 *
 * @author wangliang181230
 */
public abstract class ArrayUtils {

	//region 由于apache的ArrayUtils中，较新版本才有以下常量。为了兼容低版本，将这些常量也添加在这里

	public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

	public static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

	public static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];

	public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

	//endregion


	/**
	 * 是否为空数组
	 *
	 * @param array 数组
	 * @param <T>   数组数据类型
	 * @return 是否为空数组
	 */
	public static <T> boolean isEmpty(final T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 是否不为空数组
	 *
	 * @param array 数组
	 * @param <T>   数组数据类型
	 * @return 是否不为空数组
	 */
	public static <T> boolean isNotEmpty(final T[] array) {
		return !isEmpty(array);
	}

	/**
	 * 如果为空，则返回默认值
	 *
	 * @param array        数组
	 * @param defaultValue 默认值
	 * @param <T>          数组数据类型
	 * @return 入参数组或默认值
	 */
	public static <T> T[] defaultIfEmpty(final T[] array, final T[] defaultValue) {
		if (isEmpty(array)) {
			return defaultValue;
		}

		return array;
	}

	/**
	 * 如果为空数组，则执行supplier生成新的值
	 *
	 * @param array                数组
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  数组数据类型
	 * @return 入参数组或生成的默认值
	 */
	public static <T> T[] defaultIfEmpty(final T[] array, Supplier<T[]> defaultValueSupplier) {
		if (isEmpty(array)) {
			return defaultValueSupplier.get();
		}

		return array;
	}

	/**
	 * 将未知类型的数组对象转换为 Object[]
	 *
	 * @param arrayObj 数组对象
	 * @return array 数组
	 */
	public static Object[] toArray(Object arrayObj) {
		if (arrayObj == null) {
			return null;
		}

		if (!arrayObj.getClass().isArray()) {
			throw new ClassCastException("'arrayObj' is not an array, can't cast to Object[]");
		}

		int length = Array.getLength(arrayObj);
		Object[] array = new Object[length];
		if (length > 0) {
			for (int i = 0; i < length; ++i) {
				array[i] = Array.get(arrayObj, i);
			}
		}
		return array;
	}

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

	/**
	 * Array To String.
	 *
	 * @param arrayObj 数组对象
	 * @return str 字符串
	 */
	@NonNull
	public static String toString(final Object arrayObj) {
		if (arrayObj == null) {
			return "null";
		}
		if (!arrayObj.getClass().isArray()) {
			return StringUtils.toString(arrayObj);
		}

		if (Array.getLength(arrayObj) == 0) {
			return "[]";
		}

		if (arrayObj.getClass().getComponentType().isPrimitive()) {
			return toString(toArray(arrayObj));
		} else {
			return toString((Object[])arrayObj);
		}
	}
}
