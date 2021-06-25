package org.easyframework.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 字符串工具类
 *
 * @author wangliang181230
 */
public class StringUtils {

	/**
	 * 将对象转换为字符串
	 *
	 * @param obj 任意类型的对象
	 * @return str 解析后的字符串
	 */
	@SuppressWarnings("all")
	public static String toString(final Object obj) {
		if (obj == null) {
			return "null";
		}

		//region Convert simple types to String directly

		if (obj instanceof CharSequence || obj instanceof Number || obj instanceof Boolean || obj instanceof Character) {
			return obj.toString();
		}
		if (obj instanceof Date) {
			Date date = (Date)obj;
			long time = date.getTime();
			String dateFormat;
			if (date.getHours() == 0 && date.getMinutes() == 0 && date.getSeconds() == 0 && time % 1000 == 0) {
				dateFormat = "yyyy-MM-dd";
			} else if (time % (60 * 1000) == 0) {
				dateFormat = "yyyy-MM-dd HH:mm";
			} else if (time % 1000 == 0) {
				dateFormat = "yyyy-MM-dd HH:mm:ss";
			} else {
				dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
			}
			return new SimpleDateFormat(dateFormat).format(obj);
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

		return CycleDependencyHandler.wrap(obj, o -> {
			StringBuilder sb = new StringBuilder(32);
			sb.append(obj.getClass().getSimpleName()).append("(");
			final int initialLength = sb.length();

			// Gets all fields, excluding static or synthetic fields
			Field[] fields = ReflectionUtils.getAllFields(obj.getClass());
			for (Field field : fields) {
				field.setAccessible(true);

				if (sb.length() > initialLength) {
					sb.append(", ");
				}
				sb.append(field.getName());
				sb.append("=");
				try {
					Object f = field.get(obj);
					if (f == obj) {
						sb.append("(this ").append(f.getClass().getSimpleName()).append(")");
					} else {
						sb.append(toString(f));
					}
				} catch (Exception ignore) {
				}
			}

			sb.append(")");
			return sb.toString();
		});
	}
}
