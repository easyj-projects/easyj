package icu.easyj.web.cache304;

/**
 * Cache304相关常量
 *
 * @author wangliang181230
 */
public interface Cache304Constants {

	long DEFAULT_CACHE_SECONDS = -1L;

	int DEFAULT_CACHE_DAYS = -1;

	boolean DEFAULT_USE_MAX_AGE = true;

	/**
	 * 临界误差时间：10分钟
	 * 用于解决：以天来计算缓存秒数时，避免客户端与服务器端的时间差导致缓存多存了一天
	 */
	long CRITICAL_TIME = 10 * 60 * 1000L;
}
