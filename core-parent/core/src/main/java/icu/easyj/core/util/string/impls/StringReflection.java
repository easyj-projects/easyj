/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.core.util.string.impls;

import java.lang.annotation.Native;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * {@link String} 反射信息
 *
 * @author wangliang181230
 */
class StringReflection {

	@Native
	static final byte LATIN1 = 0;
	@Native
	static final byte UTF16 = 1;

	/**
	 * 字符串的value属性（当Java版本为16及以上时，此常量才为null）
	 */
	//@Nullable
	static final Field STRING_VALUE_FIELD;

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
	//@Nullable // java8及以下时，为空
	static final Method GET_STRING_CODER_METHOD;

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
}
