/*
 * Copyright 2021 the original author or authors.
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
 * IO工具类
 *
 * @author wangliang181230
 */
public abstract class IOUtils {

	/**
	 * 关闭IO
	 *
	 * @param closeable 可关闭的对象
	 */
	public static void close(AutoCloseable closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		} catch (Exception ignore) {
			// do nothing
		}
	}

	/**
	 * 关闭IO
	 *
	 * @param closeableArr 可关闭的对象数组
	 */
	public static void close(AutoCloseable... closeableArr) {
		if (closeableArr == null) {
			return;
		}
		for (AutoCloseable closeable : closeableArr) {
			close(closeable);
		}
	}
}
