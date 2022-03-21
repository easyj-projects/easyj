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

import icu.easyj.core.loader.EnhancedServiceLoader;

/**
 * 上下文工具类
 *
 * @author wangliang181230
 */
public abstract class ContextUtils {

	//region 上下文单例持有者（设计模式-创建型模式-单例模式：枚举实现单例）

	private enum ContextSingletonHolder {
		// 单例
		INSTANCE;

		private final Context instances = EnhancedServiceLoader.load(Context.class);

		public Context getInstance() {
			return INSTANCE.instances;
		}
	}

	/**
	 * 获取上下文服务
	 *
	 * @return 上下文服务
	 */
	static Context getInstance() {
		return ContextSingletonHolder.INSTANCE.getInstance();
	}

	//endregion


	/**
	 * 设置值
	 *
	 * @param key   键
	 * @param value 值
	 * @param <V>   上下文数据类型
	 * @return previousValue 返回以前的值 或 null
	 */
	public static <V> V put(String key, V value) {
		return getInstance().put(key, value);
	}

	/**
	 * 获取值
	 *
	 * @param key 键
	 * @param <V> 值类型
	 * @return value 值
	 */
	public static <V> V get(String key) {
		return getInstance().get(key);
	}

	/**
	 * 移除值
	 *
	 * @param key 键
	 * @param <V> 值类型
	 * @return removedValue 返回被移除的值 或 null
	 */
	public static <V> V remove(String key) {
		return getInstance().remove(key);

	}

	/**
	 * 移除值，如果与value相等的话
	 *
	 * @param key   键
	 * @param value 目标值
	 * @return true=原有值与目标值相等 | false=原有值与目标值不相等
	 */
	public static boolean remove(String key, Object value) {
		return getInstance().remove(key, value);
	}

	/**
	 * 是否含有键
	 *
	 * @param key 键
	 * @return true=包含 | false=不包含
	 */
	public static boolean containsKey(String key) {
		return getInstance().containsKey(key);
	}

	/**
	 * 清空上下文
	 */
	public static void clear() {
		getInstance().clear();
	}
}
