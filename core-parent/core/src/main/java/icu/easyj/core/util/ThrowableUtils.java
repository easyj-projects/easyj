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

import icu.easyj.core.exception.WrapperException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 异常工具类
 *
 * @author wangliang181230
 */
public abstract class ThrowableUtils {

	public static final String WRAPPER_EXCEPTION_SUFFIX = "WrapperException";

	/**
	 * 从异常信息中查找对应的异常
	 *
	 * @param t          异常信息
	 * @param causeClass 要查找的目标异常类
	 * @param <T>        异常类型
	 * @return cause 目标异常
	 */
	@Nullable
	public static <T extends Throwable> T findCause(@NonNull Throwable t,
													@NonNull final Class<T> causeClass) {
		Assert.notNull(t, "'t' must not be null");
		Assert.notNull(causeClass, "'causeClass' must not be null");

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
	 * @param causeClass 要查找的目标异常类
	 * @return isContains 返回是否包含
	 */
	public static boolean containsCause(@NonNull Throwable t, @NonNull Class<? extends Throwable> causeClass) {
		return findCause(t, causeClass) != null;
	}

	/**
	 * 拆包异常，如果是包装异常的话。
	 *
	 * @param t 异常
	 * @return 拆包后的异常
	 */
	public static Throwable unwrap(@Nullable Throwable t) {
		if (t == null) {
			return null;
		}

		if (t instanceof WrapperException) {
			return t.getCause();
		}
		if (t.getClass().getName().endsWith(WRAPPER_EXCEPTION_SUFFIX) && t.getCause() != null) {
			return t.getCause();
		}

		return t;
	}

	/**
	 * 拆包异常
	 *
	 * @param t 包装异常
	 * @return 拆包后的异常（只要入参不为空，则出参也不会为空）
	 */
	public static Throwable unwrap(WrapperException t) {
		if (t == null) {
			return null;
		}

		return t.getCause();
	}
}
