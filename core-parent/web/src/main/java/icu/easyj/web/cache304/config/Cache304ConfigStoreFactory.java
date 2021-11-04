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
package icu.easyj.web.cache304.config;

import icu.easyj.core.loader.EnhancedServiceLoader;

/**
 * Cache304配置存储器 工厂类
 *
 * @author wangliang181230
 */
public abstract class Cache304ConfigStoreFactory {

	//region 配置存储器单例持有者（设计模式-创建型模式-单例模式-枚举实现单例）

	private enum Cache304ConfigStoreSingletonHolder {
		// 单例
		INSTANCE;

		private final ICache304ConfigStore instance = EnhancedServiceLoader.load(ICache304ConfigStore.class);

		public ICache304ConfigStore getInstance() {
			return INSTANCE.instance;
		}
	}

	/**
	 * 获取加密算法生成器
	 *
	 * @return 加密算法生成器
	 */
	public static ICache304ConfigStore getStore() {
		return Cache304ConfigStoreSingletonHolder.INSTANCE.getInstance();
	}

	//endregion
}
