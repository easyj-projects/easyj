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
package icu.easyj.web.cache304.config;

import java.io.Serializable;

import icu.easyj.web.cache304.Cache304Constants;
import icu.easyj.web.cache304.annotation.Cache304;

/**
 * Cache304配置类
 *
 * @author wangliang181230
 * @see Cache304
 * @see icu.easyj.web.cache304.annotation.Cache304AnnotationParser#parse(Cache304)
 */
public class Cache304Config implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 缓存秒数
	 *
	 * @see Cache304#value()
	 * @see Cache304#cacheSeconds()
	 */
	private long cacheSeconds = Cache304Constants.DEFAULT_CACHE_SECONDS;

	/**
	 * 缓存天数
	 *
	 * @see Cache304#cacheDays()
	 */
	private int cacheDays = Cache304Constants.DEFAULT_CACHE_DAYS;

	/**
	 * 是否使用`Cache-Control:max-age=xxx`响应头
	 *
	 * @see Cache304#useMaxAge()
	 */
	private boolean useMaxAge = Cache304Constants.DEFAULT_USE_MAX_AGE;

	/**
	 * `Cache-Control:max-age=xxx`中max-age的上限值
	 *
	 * @see Cache304#limitMaxAge()
	 */
	private long limitMaxAge = Cache304Constants.DEFAULT_LIMIT_MAX_AGE;

	/**
	 * 如果controller出现异常，但客户端存在缓存，则响应304使客户端继续使用缓存
	 *
	 * @see Cache304#useCacheIfException()
	 */
	private boolean useCacheIfException = Cache304Constants.DEFAULT_USE_CACHE_IF_EXCEPTION;


	//region Getter、Setter

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

	public long getLimitMaxAge() {
		return limitMaxAge;
	}

	public void setLimitMaxAge(long limitMaxAge) {
		this.limitMaxAge = limitMaxAge;
	}

	public boolean isUseCacheIfException() {
		return useCacheIfException;
	}

	public void setUseCacheIfException(boolean useCacheIfException) {
		this.useCacheIfException = useCacheIfException;
	}

	//endregion
}
