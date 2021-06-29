package org.easyj.web.cache304;

import java.util.Date;

import org.easyj.core.util.DateUtils;
import org.easyj.web.cache304.config.Cache304Config;

import static org.easyj.core.constant.DateConstants.ONE_DAY_MILL;
import static org.easyj.core.constant.DateConstants.ONE_DAY_SEC;
import static org.easyj.web.cache304.Cache304Constants.CRITICAL_TIME;

/**
 * 缓存秒数计算机
 *
 * @author wangliang181230
 */
class CacheTimeComputer {

	/**
	 * 计算缓存有效时间，单位：毫秒
	 *
	 * @param config 缓存配置
	 * @return cacheTime 缓存毫秒数
	 */
	static long computeTime(Date lastModified, Cache304Config config) {
		if (config.getCacheDays() > 0) {
			// 以天为单位计算缓存有效时间
			return computeCacheTimeByCacheDays(lastModified, config.getCacheDays());
		} else {
			return 1000 * normalizeCacheSeconds(config.getCacheSeconds());
		}
	}

	/**
	 * 计算按天为单位的缓存时间
	 *
	 * @param time      时间基准
	 * @param cacheDays 有效天数，24点失效
	 * @return cacheTime 缓存毫秒
	 */
	private static long computeCacheTimeByCacheDays(Date time, int cacheDays) {
		long endTimeOfToday = DateUtils.getTomorrowDate(time).getTime(); // 今天24点的time值
		long remainingTime = endTimeOfToday - time.getTime(); // 今天剩余毫秒数

		//region #解决临界时间，客户端与服务器端的时间差导致缓存多存了一天
		if (remainingTime < CRITICAL_TIME) { // 刚过0点，不足10分钟
			return CRITICAL_TIME; // 缓存10分钟
		}
		//endregion

		return remainingTime + (cacheDays - 1) * ONE_DAY_MILL;
	}

	/**
	 * 标准化缓存时间，避免无效的时间
	 *
	 * @param cacheSeconds 缓存秒数
	 * @return cacheSeconds 有效的缓存秒数
	 */
	public static long normalizeCacheSeconds(long cacheSeconds) {
		if (cacheSeconds <= 0) {
			return 3 * ONE_DAY_SEC; // 默认缓存3天
		}

		// 最多只允许缓存7天
		return Math.min(cacheSeconds, 7 * ONE_DAY_SEC);
	}
}
