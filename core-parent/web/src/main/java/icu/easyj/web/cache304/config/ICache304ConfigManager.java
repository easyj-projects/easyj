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
package icu.easyj.web.cache304.config;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import icu.easyj.core.util.MapUtils;
import org.springframework.util.Assert;

/**
 * Cache304配置缓存管理器接口
 *
 * @author wangliang181230
 */
public interface ICache304ConfigManager {

	/**
	 * 添加配置
	 *
	 * @param path   请求路径
	 * @param config 缓存配置
	 */
	void putConfig(String path, Cache304Config config);

	/**
	 * 获取配置
	 *
	 * @param path 请求路径
	 * @return config Cache304配置
	 */
	Cache304Config getConfig(String path);


	//region Default Methods

	/**
	 * 添加配置
	 *
	 * @param request 请求实例
	 * @param config  缓存配置
	 */
	default void putConfig(HttpServletRequest request, Cache304Config config) {
		Assert.notNull(request, "'request' must not be null");
		this.putConfig(request.getRequestURI(), config);
	}

	/**
	 * 批量添加配置
	 *
	 * @param configMap 配置集合
	 */
	default void putConfig(Map<String, Cache304Config> configMap) {
		if (!MapUtils.isEmpty(configMap)) {
			configMap.forEach(this::putConfig);
		}
	}

	/**
	 * 获取Cache304配置
	 *
	 * @param request 请求实例
	 * @return config Cache304配置
	 */
	default Cache304Config getConfig(HttpServletRequest request) {
		Assert.notNull(request, "'request' must not be null");
		return this.getConfig(request.getRequestURI());
	}

	//endregion
}
