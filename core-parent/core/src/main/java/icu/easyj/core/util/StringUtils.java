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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 字符串工具类
 *
 * @author wangliang181230
 */
public abstract class StringUtils {

	/**
	 * 大小写字符的差值
	 */
	public static final int CASE_DIFF = ('a' - 'A');

	//region 中文相关方法

	/**
	 * 判断是否为中文字符
	 *
	 * @param c 字符
	 * @return 是否为中文字符
	 */
	public static boolean isChinese(char c) {
		return c >= 0x4E00 && c <= 0x9FA5;
	}

	/**
	 * 计算字符串长度，中文计2个字符
	 *
	 * @param str 字符串
	 * @return strLength 字符串
	 */
	public static int chineseLength(String str) {
		if (str == null || str.isEmpty()) {
			return 0;
		}

		int length = str.length();
		for (char c : str.toCharArray()) {
			if (isChinese(c)) {
				++length;
			}
		}
		return length;
	}

	//endregion


	//region 查找数据

	/**
	 * 根据匹配函数，查找数据
	 *
	 * @param strArr  字符串数组
	 * @param matcher 匹配函数
	 * @return 返回找到的字符串 或 {@code null}
	 */
	@Nullable
	public static String find(String[] strArr, Predicate<String> matcher) {
		for (String str : strArr) {
			if (matcher.test(str)) {
				return str;
			}
		}
		return null;
	}

	//region 查找第一个不为空的字符串

	/**
	 * 查找一个不为null或空字符串的字符串
	 *
	 * @param strArr 字符串数组
	 * @return 返回找到的字符串 或 {@code null}
	 */
	@Nullable
	public static String findNotEmptyOne(String... strArr) {
		return find(strArr, org.springframework.util.StringUtils::hasLength);
	}

	/**
	 * 查找一个不为null或空白字符串的字符串
	 *
	 * @param strArr 字符串数组
	 * @return 返回找到的字符串 或 {@code null}
	 */
	@Nullable
	public static String findNotBlankOne(String... strArr) {
		return find(strArr, org.springframework.util.StringUtils::hasText);
	}

	//endregion

	//endregion


	//region toString

	/**
	 * 将对象转换为字符串
	 *
	 * @param obj 任意类型的对象
	 * @return str 转换后的字符串
	 */
	@NonNull
	public static String toString(final Object obj) {
		if (obj == null) {
			return "null";
		}

		//region Convert simple types to String directly

		if (obj instanceof CharSequence) {
			return "\"" + obj + "\"";
		}
		if (obj instanceof Character) {
			return "'" + obj + "'";
		}
		if (obj instanceof Long) {
			return obj + "L";
		}
		if (obj instanceof Date) {
			return DateUtils.toString((Date)obj);
		}
		if (obj instanceof Enum) {
			return obj.getClass().getSimpleName() + "." + ((Enum)obj).name();
		}
		if (obj instanceof Class) {
			return ReflectionUtils.classToString((Class<?>)obj);
		}
		if (obj instanceof Field) {
			return ReflectionUtils.fieldToString((Field)obj);
		}
		if (obj instanceof Method) {
			return ReflectionUtils.methodToString((Method)obj);
		}
		if (obj instanceof Annotation) {
			return ReflectionUtils.annotationToString((Annotation)obj);
		}

		//endregion

		//region Convert the Collection and Map

		if (obj instanceof Collection) {
			return CollectionUtils.toString((Collection<?>)obj);
		}
		if (obj.getClass().isArray()) {
			return ArrayUtils.toString((Object[])obj);
		}
		if (obj instanceof Map) {
			return MapUtils.toString((Map<?, ?>)obj);
		}

		//endregion

		//the jdk classes
		if (obj.getClass().getClassLoader() == null) {
			return obj.toString();
		}

		// 未知类型的对象转换为字符串
		return unknownTypeObjectToString(obj);
	}

	/**
	 * 未知类型的对象转换为字符串
	 *
	 * @param obj 未知类型的对象
	 * @return str 转换后的字符串
	 */
	@NonNull
	private static String unknownTypeObjectToString(@NonNull Object obj) {
		return CycleDependencyHandler.wrap(obj, o -> {
			StringBuilder sb = new StringBuilder(32);

			// handle the anonymous class
			String classSimpleName;
			if (obj.getClass().isAnonymousClass()) {
				if (!obj.getClass().getSuperclass().equals(Object.class)) {
					classSimpleName = obj.getClass().getSuperclass().getSimpleName();
				} else {
					classSimpleName = obj.getClass().getInterfaces()[0].getSimpleName();
				}
				// Connect a '$', different from ordinary class
				classSimpleName += "$";
			} else {
				classSimpleName = obj.getClass().getSimpleName();
			}

			sb.append(classSimpleName).append("(");
			final int initialLength = sb.length();

			// Gets all fields, excluding static or synthetic fields
			Field[] fields = ReflectionUtils.getAllFields(obj.getClass());
			Object fieldValue;
			for (Field field : fields) {
				if (sb.length() > initialLength) {
					sb.append(", ");
				}
				sb.append(field.getName());
				sb.append("=");

				try {
					fieldValue = ReflectionUtils.getFieldValue(obj, field);
				} catch (RuntimeException ignore) {
					continue;
				}

				if (fieldValue == obj) {
					sb.append("(this ").append(fieldValue.getClass().getSimpleName()).append(")");
				} else {
					sb.append(toString(fieldValue));
				}
			}

			sb.append(")");
			return sb.toString();
		});
	}

	//endregion
}
