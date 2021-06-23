package org.wangliang181230.core.util;

/**
 * String工具类
 *
 * @author 王良
 */
public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isBlank(String str) {
		if (isEmpty(str)) {
			return true;
		}

		for (int i = 0; i < str.length(); ++i) {
			if (Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

}
