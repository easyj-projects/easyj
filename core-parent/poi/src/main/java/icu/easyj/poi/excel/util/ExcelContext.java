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
package icu.easyj.poi.excel.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Excel所需的上下文
 *
 * @author wangliang181230
 */
public abstract class ExcelContext {

	private static final ThreadLocal<Map<Object, Object>> CONTEXT = ThreadLocal.withInitial(HashMap::new);


	/**
	 * 设置数据
	 *
	 * @param key   键
	 * @param value 值
	 * @return previousValue 返回原有的值或null
	 */
	@Nullable
	public static Object put(Object key, Object value) {
		return CONTEXT.get().put(key, value);
	}

	/**
	 * 获取数据
	 *
	 * @param key 键
	 * @param <T> 值类型
	 * @return 值或null
	 */
	@Nullable
	@SuppressWarnings("cast")
	public static <T> T get(Object key) {
		return (T) CONTEXT.get().get(key);
	}

	/**
	 * 获取所有数据
	 *
	 * @return 所有数据
	 */
	@NonNull
	public static Map<Object, Object> get() {
		return CONTEXT.get();
	}

	/**
	 * 清除数据
	 *
	 * @param key 键
	 */
	public static void remove(Object key) {
		CONTEXT.get().remove(key);
	}

	/**
	 * 清除数据
	 */
	public static void remove() {
		CONTEXT.remove();
	}
}
