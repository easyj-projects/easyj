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
package icu.easyj.core.trace;

import icu.easyj.core.util.ArrayUtils;
import icu.easyj.core.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 追踪接口
 *
 * @author wangliang181230
 */
public interface TraceService {

	/**
	 * 判断当前是否可以执行追踪
	 *
	 * @return true=可以 | false=不可以
	 */
	boolean canTrace();

	/**
	 * 设置追踪内容
	 *
	 * @param key   键
	 * @param value 值
	 */
	void put(String key, String value);

	/**
	 * 批量设置追踪内容
	 *
	 * @param map 键值对
	 */
	default void put(Map<String, String> map) {
		if (CollectionUtils.isNotEmpty(map)) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				this.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 移除追踪内容
	 *
	 * @param key 键
	 */
	void remove(String key);

	/**
	 * 移除追踪内容
	 *
	 * @param keys 键
	 */
	default void remove(String... keys) {
		if (ArrayUtils.isNotEmpty(keys)) {
			for (String key : keys) {
				this.remove(key);
			}
		}
	}

	/**
	 * 移除追踪内容
	 *
	 * @param keys 键
	 */
	default void remove(Collection<String> keys) {
		if (CollectionUtils.isNotEmpty(keys)) {
			for (String key : keys) {
				this.remove(key);
			}
		}
	}

	/**
	 * 清空追踪内容
	 */
	void clear();
}
