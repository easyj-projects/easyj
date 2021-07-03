/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.web.cache304.annotation;

import icu.easyj.web.cache304.config.Cache304Config;

/**
 * Cache304注解 解析器
 *
 * @author wangliang181230
 */
public class Cache304AnnotationParser {

	/**
	 * 解析{@link Cache304}注解
	 *
	 * @param annotation 注解
	 * @return config 缓存配置
	 */
	public static Cache304Config parse(Cache304 annotation) {
		Cache304Config config = new Cache304Config();

		config.setCacheSeconds(getCacheSeconds(annotation));
		config.setCacheDays(annotation.cacheDays());
		config.setUseMaxAge(annotation.useMaxAge());

		return config;
	}

	/**
	 * 从注解中获取缓存秒数
	 *
	 * @param annotation 注解
	 * @return cacheSeconds 缓存秒数
	 */
	private static long getCacheSeconds(Cache304 annotation) {
		if (annotation.cacheSeconds() > 0) {
			return annotation.cacheSeconds();
		}
		return annotation.value();
	}
}
