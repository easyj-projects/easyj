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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import cn.hutool.core.util.ArrayUtil;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.string.IStringService;
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
	 * String服务，用于不同版本的JDK的性能最佳实现
	 */
	private static final IStringService STRING_SERVICE = EnhancedServiceLoader.load(IStringService.class);

	/**
	 * 大小写字符的差值
	 */
	public static final byte CASE_DIFF = ('a' - 'A');


	//region 获取字符串的value和coder属性值

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
		return STRING_SERVICE.getValue(str);
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
		return STRING_SERVICE.getCoder(str);
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
	 * 判断是否全部由指定字符组成的字符串
	 *
	 * @param str        字符串
	 * @param targetChar 指定字符
	 * @return true=全为0、false=为null或不全为0
	 */
	public static boolean isAll(String str, char targetChar) {
		if (str == null) {
			return false;
		}

		char[] chars = toCharArray(str);
		for (char c : chars) {
			if (c != targetChar) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断是否全部由数字 '0' 组成的字符串
	 *
	 * @param str 字符串
	 * @return true=全为0、false=为null或不全为0
	 */
	public static boolean isAllZero(String str) {
		return isAll(str, '0');
	}

	/**
	 * 判断字符串是否包含目标字符
	 *
	 * @param str        字符串
	 * @param targetChar 目标字符
	 * @return true=包含 | false=不包含
	 */
	public static boolean contains(CharSequence str, char targetChar) {
		if (isEmpty(str)) {
			return false;
		}

		char[] chars = toCharArray(str);
		return contains(chars, targetChar);
	}

	/**
	 * 判断字符数组是否包含目标字符
	 *
	 * @param chars      字符数组
	 * @param targetChar 目标字符
	 * @return true=包含 | false=不包含
	 */
	public static boolean contains(char[] chars, char targetChar) {
		if (ArrayUtil.isEmpty(chars)) {
			return false;
		}

		for (char c : chars) {
			if (c == targetChar) {
				return true;
			}
		}

		return false;
	}

	//endregion


	//region 比较方法

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

		char[] chars = toCharArray(cs);

		int length = chars.length;
		for (char c : chars) {
			if (isChinese(c)) {
				length++;
			}
		}

		return length;
	}

	//endregion


	//region 查找数据

	/**
	 * 查找一个不为null或空字符串的字符串
	 *
	 * @param strArr 字符串数组
	 * @return 返回找到的字符串 或 {@code null}
	 */
	@Nullable
	public static String findNotEmptyOne(final String... strArr) {
		return ObjectUtils.find(strArr, StringUtils::isNotEmpty);
	}

	/**
	 * 查找一个不为null或空白字符串的字符串
	 *
	 * @param strArr 字符串数组
	 * @return 返回找到的字符串 或 {@code null}
	 */
	@Nullable
	public static String findNotBlankOne(final String... strArr) {
		return ObjectUtils.find(strArr, StringUtils::isNotBlank);
	}

	/**
	 * 统计字符串中包含的目标字符数量
	 *
	 * @param str        字符串
	 * @param targetChar 需统计的字符
	 * @return 字符数量
	 */
	public static int count(@NonNull String str, char targetChar) {
		Assert.notNull(str, "'str' must be not null");

		char[] chars = toCharArray(str);
		return count(chars, targetChar);
	}

	/**
	 * 统计字符数组中包含的目标字符数量
	 *
	 * @param chars      字符数组
	 * @param targetChar 需统计的字符
	 * @return 字符数量
	 */
	public static int count(@NonNull char[] chars, char targetChar) {
		Assert.notNull(chars, "'chars' must be not null");

		int count = 0;
		for (char c : chars) {
			if (c == targetChar) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 查找第n个目标字符在字符串的索引值
	 *
	 * @param str        字符串
	 * @param targetChar 目标字符
	 * @param n          第 n 个目标字符，n必须大于0
	 * @return index 字符索引，返回-1表示字符串中没有目标字符，返回-2表示字符串中包含的目标字符少于n个
	 */
	public static int indexOf(String str, char targetChar, int n) {
		Assert.isTrue(n > 0, "n必须大于0");

		int count = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == targetChar) {
				count++;
				if (count == n) {
					return i;
				}
			}
		}

		return count == 0 ? -1 : -2;
	}

	//endregion


	//region 字符串处理

	/**
	 * 从字符串中移除对应的字符
	 *
	 * @param str             字符串
	 * @param toBeRemovedChar 需移除字符
	 * @return 新的字符串
	 */
	public static String remove(@NonNull String str, char toBeRemovedChar) {
		Assert.notNull(str, "'s' must not be null");

		final char[] result = new char[str.length()];
		int n = 0;

		char[] chars = toCharArray(str);
		for (char c : chars) {
			if (c != toBeRemovedChar) {
				result[n++] = c;
			}
		}

		return (n > 0 ? new String(result, 0, n) : str);
	}

	/**
	 * 裁剪掉两边的某个字符或空字符
	 *
	 * @param str     字符串
	 * @param cutChar 需裁剪掉的字符
	 * @return 裁剪后的字符串
	 */
	public static String trim(@NonNull String str, char cutChar) {
		Assert.notNull(str, "'str' must not be null");

		int start = 0;
		int end = str.length();

		char[] chars = toCharArray(str);

		while (start < end && isCutCharOrBlank(chars[start], cutChar)) {
			start++;
		}
		if (start == end) {
			return "";
		}

		while (end > start && isCutCharOrBlank(chars[end - 1], cutChar)) {
			end--;
		}

		if (start > 0 || end < str.length()) {
			return str.substring(start, end);
		} else {
			return str;
		}
	}

	/**
	 * 裁剪掉两边的某个字符或空字符
	 *
	 * @param str     字符串
	 * @param cutChar 需裁剪掉的字符
	 * @return 裁剪后的字符串
	 */
	public static String trimStart(@NonNull String str, char cutChar) {
		Assert.notNull(str, "'str' must not be null");

		int start = 0;
		int end = str.length();

		char[] chars = toCharArray(str);

		while (start < end && isCutCharOrBlank(chars[start], cutChar)) {
			start++;
		}
		if (start == end) {
			return "";
		} else if (start > 0) {
			return str.substring(start, end);
		} else {
			return str;
		}
	}

	/**
	 * 裁剪掉两边的某个字符或空字符
	 *
	 * @param str     字符串
	 * @param cutChar 需裁剪掉的字符
	 * @return 裁剪后的字符串
	 */
	public static String trimEnd(@NonNull String str, char cutChar) {
		Assert.notNull(str, "'str' must not be null");

		int start = 0;
		int end = str.length();

		char[] chars = toCharArray(str);

		while (end > start && isCutCharOrBlank(chars[end - 1], cutChar)) {
			end--;
		}

		if (start == end) {
			return "";
		} else if (end < str.length()) {
			return str.substring(start, end);
		} else {
			return str;
		}
	}

	private static boolean isCutCharOrBlank(char c, char cutChar) {
		return c == cutChar || Character.isWhitespace(c);
	}

	//endregion


	//region 快速生成字符串

	/**
	 * 生成 N个源字符 由 分隔符隔开而组成的字符串
	 * <p>
	 * 格式如：?,?,?,?（不带空格）
	 *
	 * @param c         源字符
	 * @param separator 分隔符
	 * @param n         源字符数量
	 * @return 生成的字符串
	 * @throws IllegalArgumentException n小于0
	 * @see #joinWithSpace(char, char, int) 带空格的方法
	 */
	public static String join(char c, char separator, int n) {
		Assert.isTrue(n >= 0, "n必须大于等于0");

		if (n == 0) {
			return "";
		}
		if (n == 1) {
			return String.valueOf(c);
		}

		char[] chars = new char[2 * n - 1];

		chars[0] = c;

		for (int i = 1; i < chars.length; ) {
			chars[i++] = separator;
			chars[i++] = c;
		}

		return new String(chars);
	}

	/**
	 * 生成 N个源字符 由 分隔符隔开而组成的字符串
	 * <p>
	 * 格式如：?, ?, ?, ?（带空格）
	 *
	 * @param c         源字符
	 * @param separator 分隔符
	 * @param n         源字符数量
	 * @return 生成的字符串
	 * @throws IllegalArgumentException n小于0
	 * @see #join(char, char, int) 不带空格的方法
	 */
	public static String joinWithSpace(char c, char separator, int n) {
		Assert.isTrue(n >= 0, "n必须大于等于0");

		if (n == 0) {
			return "";
		}
		if (n == 1) {
			return String.valueOf(c);
		}

		char[] chars = new char[3 * n - 2];

		chars[0] = c;

		for (int i = 1; i < chars.length; ) {
			chars[i++] = separator;
			chars[i++] = ' ';
			chars[i++] = c;
		}

		return new String(chars);
	}

	//endregion


	//region 转换

	/**
	 * 获取字符数组
	 *
	 * @param str 字符串
	 * @return 字符数组
	 */
	public static char[] toCharArray(@NonNull CharSequence str) {
		Assert.notNull(str, "'str' must not be null");
		return STRING_SERVICE.toCharArray(str);
	}

	////region toString

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
			return ArrayUtils.toString(obj);
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

	////endregion

	//endregion
}
