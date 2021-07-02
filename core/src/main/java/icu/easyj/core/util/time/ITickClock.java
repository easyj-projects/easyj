package icu.easyj.core.util.time;

/**
 * 记号时钟接口
 *
 * @author wangliang181230
 */
public interface ITickClock extends IClock {

	/**
	 * 获取基准微秒数
	 *
	 * @return baseEpochMicros 基准微秒数
	 */
	long getBaseEpochMicros();

	/**
	 * 获取基准记号纳秒数
	 *
	 * @return baseTickNanos 基准记号纳秒数
	 */
	long getBaseTickNanos();

	/**
	 * 获取已经过的纳秒数
	 * 说明：以baseTickNanos为基准，经过的纳秒数
	 *
	 * @return passedNanos 已经过的纳秒数
	 */
	default long getPassedNanos() {
		return System.nanoTime() - getBaseTickNanos();
	}
}
