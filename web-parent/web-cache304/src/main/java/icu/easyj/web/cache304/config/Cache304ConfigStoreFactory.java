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

/**
 * Cache304配置存储器 工厂类
 *
 * @author wangliang181230
 */
public class Cache304ConfigStoreFactory {

	/**
	 * 配置存储器
	 */
	private static ICache304ConfigStore configStore;

	/**
	 * 获取配置存储器
	 *
	 * @return store 配置存储器
	 */
	public static ICache304ConfigStore getStore() {
		if (configStore == null) {
			synchronized (Cache304ConfigStoreFactory.class) {
				if (configStore == null) {
					configStore = new DefaultCache304ConfigStoreImpl();
				}
			}
		}
		return configStore;
	}

	/**
	 * 设置配置存储器
	 *
	 * @param store 配置存储器
	 */
	public static void setStore(ICache304ConfigStore store) {
		Cache304ConfigStoreFactory.configStore = store;
	}
}
