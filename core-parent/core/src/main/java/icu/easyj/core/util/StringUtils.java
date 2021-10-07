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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 字符串工具类
 *
 * @author wangliang181230
 */
public abstract class StringUtils {

	/**
	 * 大小写字符的差值
	 */
	public static final byte CASE_DIFF = ('a' - 'A');


	//region 获取字符串的value和coder属性值

	/**
	 * 字符串的value属性（当Java版本为16及以上时，此常量才为null）
	 */
	@Nullable
	private static final Field STRING_VALUE_FIELD;

	/**
	 * 字符串的coder()方法
	 * <p>
	 * Java9及以上版本，才有此方法。
	 * <p>
	 * Java9~15版本，此常量不为null，其他版本均为null；<br>
	 * 原因如下：<br>
	 * - Java8及以下版本，没有此方法；<br>
	 * - Java16及以上版本，禁止了非常多的非法访问，包括对java.lang下的类的反射操作，所以此常量为null。
	 */
	@Nullable // java9以下时，为空
	private static final Method GET_STRING_CODER_METHOD;

	static {
		//region 获取Field: String.value

		Field field;
		try {
			field = String.class.getDeclaredField("value");
			field.setAccessible(true); // JDK9~15时，控制台会出现WARNING，JDK16及以上，此方法将抛出异常
		} catch (NoSuchFieldException | RuntimeException ignore) {
			field = null;
		}
		STRING_VALUE_FIELD = field;

		//endregion


		//region 获取Method: String.coder()

		Method method;
		try {
			method = String.class.getDeclaredMethod("coder");
			method.setAccessible(true);
		} catch (NoSuchMethodException | RuntimeException ignore) {
			method = null;
		}
		GET_STRING_CODER_METHOD = method;

		//endregion
	}


	/**
	 * 获取String的value属性值
	 * <p>
	 * 部分场景下，我们获取字符串的char数组，只是为了校验字符串，并没有任何修改、删除操作。<br>
	 * 但由于 {@link String#toCharArray()} 方法会复制一次字符数组，导致无谓的性能损耗。<br>
	 * 所以，开发了此方法用于提升性能。
	 *
	 * @param str 字符串
	 * @return java8返回char[]、java9及以上返回byte[]
	 * @throws IllegalArgumentException str为空时，抛出该异常
	 * @see String#toCharArray()
	 */
	public static Object getValue(@NonNull CharSequence str) {
		Assert.notNull(str, "'str' must not be null");

		if (STRING_VALUE_FIELD == null) {
			return str.toString().getBytes(StandardCharsets.UTF_8);
		}

		try {
			return STRING_VALUE_FIELD.get(str.toString());
		} catch (IllegalAccessException e) {
			throw new RuntimeException("获取字符串的value失败", e);
		}
	}

	/**
	 * 获取String的coder属性值
	 *
	 * @param str 字符串
	 * @return 字符编码的标识符（值域：0=LATIN1 | 1=UTF16）
	 * @throws IllegalArgumentException str为空时，抛出该异常
	 */
	public static byte getCoder(@NonNull CharSequence str) {
		Assert.notNull(str, "'str' must not be null");

		if (GET_STRING_CODER_METHOD == null) {
			//throw new NullPointerException("当前JDK版本中的String类，没有coder属性!");
			return (byte)0;
		}

		try {
			return (byte)GET_STRING_CODER_METHOD.invoke(str.toString());
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException("获取字符串的coder失败", e);
		}
	}

	//endregion


	//region 判断方法

	/**
	 * 字符串是否为空
	 *
	 * @param cs 字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * 字符串是否不为空
	 *
	 * @param cs 字符串
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}

	/**
	 * 字符串是否为空白
	 *
	 * @param cs 字符串
	 * @return 是否为空白
	 */
	public static boolean isBlank(final CharSequence cs) {
		if (cs == null) {
			return true;
		}

		final int length = cs.length();
		if (length > 0) {
			for (int i = 0; i < length; ++i) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 字符串是否不为空白
	 *
	 * @param cs 字符串
	 * @return 是否不为空白
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * 判断字符串是否相等
	 *
	 * @param cs1 字符串1
	 * @param cs2 字符串2
	 * @return 是否相等
	 */
	public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
		if (cs1 == cs2) {
			return true;
		}
		if (cs1 == null || cs2 == null) {
			return false;
		}
		if (cs1.length() != cs2.length()) {
			return false;
		}
		if (cs1 instanceof String && cs2 instanceof String) {
			return cs1.equals(cs2);
		}

		final int length = cs1.length();
		for (int i = 0; i < length; i++) {
			if (cs1.charAt(i) != cs2.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	//endregion


	//region 如果为空则取默认值的方法

	/**
	 * 如果为空字符串，则取默认值
	 *
	 * @param cs           字符串
	 * @param defaultValue 默认值
	 * @param <T>          字符串类型
	 * @return 字符串或默认值
	 */
	public static <T extends CharSequence> T defaultIfEmpty(T cs, T defaultValue) {
		if (isEmpty(cs)) {
			return defaultValue;
		}

		return cs;
	}

	/**
	 * 如果为空字符串，则执行supplier生成新的值
	 *
	 * @param cs                   字符串
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  字符串类型
	 * @return 入参字符串或生成的默认值
	 */
	public static <T extends CharSequence> T defaultIfEmpty(final T cs, Supplier<T> defaultValueSupplier) {
		if (isEmpty(cs)) {
			return defaultValueSupplier.get();
		}

		return cs;
	}

	/**
	 * 如果为空白字符串，则取默认值
	 *
	 * @param cs           字符串
	 * @param defaultValue 默认值
	 * @param <T>          字符串类型
	 * @return 字符串或默认值
	 */
	public static <T extends CharSequence> T defaultIfBlank(T cs, T defaultValue) {
		if (isBlank(cs)) {
			return defaultValue;
		}

		return cs;
	}

	/**
	 * 如果为空白字符串，则执行supplier生成新的值
	 *
	 * @param cs                   字符串
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  字符串类型
	 * @return 入参字符串或生成的默认值
	 */
	public static <T extends CharSequence> T defaultIfBlank(final T cs, Supplier<T> defaultValueSupplier) {
		if (isBlank(cs)) {
			return defaultValueSupplier.get();
		}

		return cs;
	}

	//endregion


	//region 中文相关方法

	/**
	 * 判断是否为中文字符
	 *
	 * @param c 字符
	 * @return 是否为中文字符
	 */
	public static boolean isChinese(final char c) {
		return c >= 0x4E00 && c <= 0x9FA5;
	}

	/**
	 * 计算字符串长度，中文计2个字符
	 *
	 * @param cs 字符串
	 * @return strLength 字符串
	 */
	public static int chineseLength(final CharSequence cs) {
		if (isEmpty(cs)) {
			return 0;
		}

		final int length = cs.length();
		int resultLength = length; // 返回长度
		for (int i = 0; i < length; ++i) {
			if (isChinese(cs.charAt(i))) {
				++resultLength;
			}
		}

		return resultLength;
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
	public static String find(final String[] strArr, final Predicate<String> matcher) {
		for (final String str : strArr) {
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
	public static String findNotEmptyOne(final String... strArr) {
		return find(strArr, StringUtils::isNotEmpty);
	}

	/**
	 * 查找一个不为null或空白字符串的字符串
	 *
	 * @param strArr 字符串数组
	 * @return 返回找到的字符串 或 {@code null}
	 */
	@Nullable
	public static String findNotBlankOne(final String... strArr) {
		return find(strArr, StringUtils::isNotBlank);
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
			return obj.getClass().getSimpleName() + "." + ((Enum<?>)obj).name();
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
	private static String unknownTypeObjectToString(@NonNull final Object obj) {
		return CycleDependencyHandler.wrap(obj, o -> {
			final StringBuilder sb = new StringBuilder(32);

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

			// the initial length
			final int initialLength = sb.length();

			// Gets all fields, excluding static or synthetic fields
			final Field[] fields = ReflectionUtils.getAllFields(obj.getClass());
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
