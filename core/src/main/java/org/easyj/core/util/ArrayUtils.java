package org.easyj.core.util;

import org.springframework.lang.NonNull;

/**
 * 数组工具类
 *
 * @author wangliang181230
 */
public class ArrayUtils {

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
}
