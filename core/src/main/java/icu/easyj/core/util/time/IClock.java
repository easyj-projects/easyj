package icu.easyj.core.util.time;

import java.util.Date;

/**
 * 时钟接口
 *
 * @author wangliang181230
 */
public interface IClock {

	/**
	 * 当前时间
	 *
	 * @return now 当前时间
	 */
	default Date now() {
		return new Date(currentTimeMillis());
	}

	/**
	 * 当前毫秒数
	 *
	 * @return timeMillis 毫秒数
	 */
	default long currentTimeMillis() {
		return currentTimeNanos() / 1000000;
	}

	/**
	 * 当前微秒数
	 *
	 * @return timeMicros 微秒数
	 */
	default long currentTimeMicros() {
		return currentTimeNanos() / 1000;
	}

	/**
	 * 当前纳秒数
	 * 注意：值格式与`System.nanoTime()`并不相同
	 *
	 * @return timeNanos 纳秒数
	 */
	long currentTimeNanos();
}
