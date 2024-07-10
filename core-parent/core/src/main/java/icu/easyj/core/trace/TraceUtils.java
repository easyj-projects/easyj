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
package icu.easyj.core.trace;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.ArrayUtils;
import icu.easyj.core.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 内容追踪工具类
 *
 * @author wangliang181230
 */
public abstract class TraceUtils {

	//region 追踪服务单例持有者（设计模式-创建型模式-单例模式：枚举实现单例）

	private enum TraceServiceSingletonHolder {
		// 单例
		INSTANCE;

		private final List<TraceService> instances = EnhancedServiceLoader.loadAll(TraceService.class);

		public List<TraceService> getInstances() {
			return INSTANCE.instances;
		}
	}

	static List<TraceService> getInstances() {
		return TraceServiceSingletonHolder.INSTANCE.getInstances();
	}

	/**
	 * 循环执行每个追踪服务
	 */
	private static void execute(Consumer<TraceService> consumer) {
		getInstances().forEach(consumer);
	}

	//endregion


	/**
	 * 判断当前是否可以执行追踪
	 *
	 * @return true=可以 | false=不可以
	 */
	public static boolean canTrace() {
		List<TraceService> tsList = getInstances();
		if (CollectionUtils.isEmpty(tsList)) {
			return false;
		}

		for (TraceService ts : tsList) {
			if (ts.canTrace()) {
				// 只要存在一个追踪服务可以追踪，那么就可以执行追踪
				return true;
			}
		}

		// 全部追踪服务都无法追踪
		return false;
	}

	/**
	 * 判断当前是否不可以执行追踪
	 *
	 * @return true=可以 | false=不可以
	 */
	public static boolean canNotTrace() {
		return !canTrace();
	}


	/**
	 * 设置追踪内容
	 *
	 * @param key   键
	 * @param value 值
	 */
	public static void put(String key, String value) {
		if (canNotTrace()) {
			return;
		}

		execute(ts -> ts.put(key, value));
	}

	/**
	 * 批量设置追踪内容
	 *
	 * @param map 键值对
	 */
	public static void put(Map<String, String> map) {
		if (CollectionUtils.isEmpty(map) || canNotTrace()) {
			return;
		}
		execute(ts -> ts.put(map));
	}

	/**
	 * 移除追踪内容
	 *
	 * @param key 键
	 */
	public static void remove(String key) {
		if (canNotTrace()) {
			return;
		}
		execute(ts -> ts.remove(key));
	}

	/**
	 * 移除追踪内容
	 *
	 * @param keys 键
	 */
	public static void remove(String... keys) {
		if (ArrayUtils.isEmpty(keys) || canNotTrace()) {
			return;
		}
		execute(ts -> ts.remove(keys));
	}

	/**
	 * 移除追踪内容
	 *
	 * @param keys 键
	 */
	public static void remove(Collection<String> keys) {
		if (CollectionUtils.isEmpty(keys) || canNotTrace()) {
			return;
		}
		execute(ts -> ts.remove(keys));
	}

	/**
	 * 清空追踪内容
	 */
	public static void clear() {
		execute(TraceService::clear);
	}
}
