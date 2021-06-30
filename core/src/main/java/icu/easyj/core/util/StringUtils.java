package icu.easyj.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.lang.NonNull;

/**
 * 字符串工具类
 *
 * @author wangliang181230
 */
public abstract class StringUtils {

	//region 字符串长度相关方法

	/**
	 * 计算字符串长度，中文计2个字符
	 *
	 * @param str 字符串
	 * @return strLength 字符串
	 */
	public static int getStrLength(String str) {
		if (str == null || str.isEmpty()) {
			return 0;
		}

		int valueLength = str.length();
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < str.length(); i++) {
			String temp = str.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength++;
			}
		}
		return valueLength;
	}

	//endregion


	//region toString

	/**
	 * 将对象转换为字符串
	 *
	 * @param obj 任意类型的对象
	 * @return str 转换后的字符串
	 */
	@SuppressWarnings("deprecation")
	public static String toString(final Object obj) {
		if (obj == null) {
			return "null";
		}

		//region Convert simple types to String directly

		if (obj instanceof CharSequence || obj instanceof Number || obj instanceof Boolean || obj instanceof Character) {
			return obj.toString();
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
		if (obj instanceof Map) {
			return MapUtils.toString((Map<?, ?>)obj);
		}
		if (obj.getClass().isArray()) {
			return ArrayUtils.toString((Object[])obj);
		}

		//endregion

		// 未知类型的对象转换为字符串
		return unknownTypeObjectToString(obj);
	}

	/**
	 * 未知类型的对象转换为字符串
	 *
	 * @param obj 未知类型的对象
	 * @return str 转换后的字符串
	 */
	private static String unknownTypeObjectToString(@NonNull Object obj) {
		return CycleDependencyHandler.wrap(obj, o -> {
			StringBuilder sb = new StringBuilder(32);
			sb.append(obj.getClass().getSimpleName()).append("(");
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
				} catch (Exception ignore) {
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