package icu.easyj.core.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 异常工具类
 *
 * @author wangliang181230
 */
public abstract class ThrowableUtils {

	/**
	 * 从异常信息中查找对应的异常
	 *
	 * @param t          异常信息
	 * @param causeClass 要查找的目标异常类型
	 * @return cause 目标异常
	 */
	@Nullable
	public static <T extends Throwable> T findCause(@NonNull Throwable t, @NonNull Class<T> causeClass) {
		Assert.notNull(t, "'t' must be not null");
		Assert.notNull(causeClass, "'causeClass' must be not null");

		while (t != null) {
			if (causeClass.isAssignableFrom(t.getClass())) {
				return (T)t;
			}
			t = t.getCause();
		}
		return null;
	}

	/**
	 * 判断异常信息中是否包含指定的异常类型
	 *
	 * @param t          异常信息
	 * @param causeClass 要查找的目标异常类型
	 * @return isContains 返回是否包含
	 */
	public static boolean containsCause(@NonNull Throwable t, @NonNull Class<? extends Throwable> causeClass) {
		return findCause(t, causeClass) != null;
	}
}
