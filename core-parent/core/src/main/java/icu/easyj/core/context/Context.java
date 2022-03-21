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
package icu.easyj.core.context;

import java.util.Map;
import java.util.Objects;

/**
 * 上下文接口
 *
 * @author wangliang181230
 */
public interface Context {

	/**
	 * 设置值
	 *
	 * @param key   键
	 * @param value 值
	 * @param <V>   上下文数据类型
	 * @return previousValue 返回以前的值 或 null
	 */
	<V> V put(String key, V value);

	/**
	 * 获取值
	 *
	 * @param key 键
	 * @param <V> 值类型
	 * @return value 值
	 */
	<V> V get(String key);

	/**
	 * 移除值
	 *
	 * @param key 键
	 * @param <V> 值类型
	 * @return removedValue 返回被移除的值 或 null
	 */
	<V> V remove(String key);

	/**
	 * 移除值，如果与value相等的话
	 *
	 * @param key   键
	 * @param value 目标值
	 * @return true=原有值与目标值相等 | false=原有值与目标值不相等
	 */
	default boolean remove(String key, Object value) {
		Object curValue = get(key);
		if (!Objects.equals(curValue, value) ||
				(curValue == null && !containsKey(key))) {
			return false;
		}
		remove(key);
		return true;
	}

	/**
	 * 是否含有键
	 *
	 * @param key 键
	 * @return true=包含 | false=不包含
	 */
	boolean containsKey(String key);

	/**
	 * 获取所有上下文
	 *
	 * @return 所有上下文
	 */
	Map<String, Object> entries();

	/**
	 * 清空上下文
	 */
	void clear();
}
