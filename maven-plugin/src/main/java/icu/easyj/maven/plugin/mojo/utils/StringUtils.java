package icu.easyj.maven.plugin.mojo.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Nonnull;

/**
 * 字符串工具类
 *
 * @author wangliang181230
 * @since 0.7.2
 */
public abstract class StringUtils {

	public static boolean isEmpty(final String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isNotEmpty(final String str) {
		return !isEmpty(str);
	}

	public static String padLeft(Object obj, int length) {
		String str = obj.toString();

		if (str.length() >= length) {
			return str;
		}

		StringBuilder sb = new StringBuilder(length);
		length -= str.length();
		while (length-- > 0) {
			sb.append(" ");
		}
		sb.append(str);
		return sb.toString();
	}

	//region toSet

	public static Set<String> addToSet(final Set<String> set, final String str) {
		assert set != null;

		if (isNotEmpty(str)) {
			String[] strArr = str.split(",");

			for (String s : strArr) {
				s = s.trim();
				if (s.isEmpty()) {
					continue;
				}

				set.add(s);
			}
		}

		return set;
	}

	@Nonnull
	public static Set<String> toSet(final String str) {
		Set<String> result = new HashSet<>();
		return addToSet(result, str);
	}

	@Nonnull
	public static Set<String> toTreeSet(final String str) {
		Set<String> result = new TreeSet<>(String::compareTo);
		return addToSet(result, str);
	}

	//endregion
}
