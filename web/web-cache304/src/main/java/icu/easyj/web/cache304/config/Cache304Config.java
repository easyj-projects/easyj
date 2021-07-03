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
package icu.easyj.web.cache304.config;

import icu.easyj.web.cache304.Cache304Constants;

/**
 * Cache304配置类
 *
 * @author wangliang181230
 */
public class Cache304Config {

	/**
	 * 缓存秒数
	 */
	private long cacheSeconds = Cache304Constants.DEFAULT_CACHE_SECONDS;

	/**
	 * 缓存天数
	 */
	private int cacheDays = Cache304Constants.DEFAULT_CACHE_DAYS;

	/**
	 * 是否使用`Cache-Control:max-age`响应头
	 */
	private boolean useMaxAge = Cache304Constants.DEFAULT_USE_MAX_AGE;


	public long getCacheSeconds() {
		return cacheSeconds;
	}

	public void setCacheSeconds(long cacheSeconds) {
		this.cacheSeconds = cacheSeconds;
	}

	public int getCacheDays() {
		return cacheDays;
	}

	public void setCacheDays(int cacheDays) {
		this.cacheDays = cacheDays;
	}

	public boolean isUseMaxAge() {
		return useMaxAge;
	}

	public void setUseMaxAge(boolean useMaxAge) {
		this.useMaxAge = useMaxAge;
	}
}
