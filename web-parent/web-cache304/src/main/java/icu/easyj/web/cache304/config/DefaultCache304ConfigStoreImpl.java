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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.UrlUtils;
import org.springframework.util.Assert;

/**
 * Cache304配置存储器 默认实现类
 *
 * @author wangliang181230
 */
@LoadLevel(name = "memory-map", order = 100)
public class DefaultCache304ConfigStoreImpl implements ICache304ConfigStore {

	/**
	 * path → config
	 */
	private final Map<String, Cache304Config> cache304ConfigMap;


	//region Constructor

	/**
	 * 默认构造函数
	 */
	public DefaultCache304ConfigStoreImpl() {
		cache304ConfigMap = new ConcurrentHashMap<>(4);
	}

	/**
	 * 带参构造函数
	 *
	 * @param cache304ConfigMap 保存配置的map
	 */
	public DefaultCache304ConfigStoreImpl(Map<String, Cache304Config> cache304ConfigMap) {
		this.cache304ConfigMap = cache304ConfigMap;
	}

	//endregion


	//region Override

	@Override
	public void putConfig(String path, Cache304Config config) {
		Assert.notNull(path, "'path' must be not null");
		Assert.notNull(config, "'config' must be not null");

		// 标准化路径
		path = UrlUtils.normalizePath(path);

		// 添加配置
		this.cache304ConfigMap.put(path, config);
	}

	@Override
	public Cache304Config getConfig(String path) {
		Assert.notNull(path, "'path' must be not null");

		// 标准化路径
		path = UrlUtils.normalizePath(path);

		// 获取参数
		return this.cache304ConfigMap.get(path.trim());
	}

	//endregion
}
