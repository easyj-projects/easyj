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
package icu.easyj.core.util;

/**
 * 类型工具类
 *
 * @author wangliang181230
 */
public abstract class ClassUtils {

	/**
	 * 判断类是否存在
	 *
	 * @param className 类名
	 * @param loader    类加载器
	 * @return 是否存在
	 */
	public static boolean isExist(String className, ClassLoader loader) {
		try {
			Class.forName(className, false, loader);
			return true;
		} catch (ClassNotFoundException ignore) {
			return false;
		}
	}

	/**
	 * 判断类是否存在
	 *
	 * @param className 类名
	 * @return 是否存在
	 */
	public static boolean isExist(String className) {
		return isExist(className, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * 判断类是否不存在
	 *
	 * @param className 类名
	 * @param loader    类加载器
	 * @return 是否不存在
	 */
	public static boolean isNotExist(String className, ClassLoader loader) {
		return !isExist(className, loader);
	}

	/**
	 * 判断类是否不存在
	 *
	 * @param className 类名
	 * @return 是否不存在
	 */
	public static boolean isNotExist(String className) {
		return isNotExist(className, Thread.currentThread().getContextClassLoader());
	}
}
