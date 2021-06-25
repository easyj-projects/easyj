package org.easyj.web.cache304.config;

import org.easyj.web.cache304.Cache304Constant;

/**
 * Cache304配置类
 *
 * @author wangliang181230
 */
public class Cache304Config {

	/**
	 * 缓存秒数
	 */
	private long cacheSeconds = Cache304Constant.DEFAULT_CACHE_SECONDS;

	/**
	 * 缓存天数
	 */
	private int cacheDays = Cache304Constant.DEFAULT_CACHE_DAYS;

	/**
	 * 是否使用`Cache-Control:max-age`响应头
	 */
	private boolean useMaxAge = Cache304Constant.DEFAULT_USE_MAX_AGE;


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
