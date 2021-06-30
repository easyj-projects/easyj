package icu.easyj.core.constant;

/**
 * Date相关常量
 *
 * @author wangliang181230
 */
public interface DateConstants {

	/**
	 * 一天的秒数
	 */
	long ONE_DAY_SEC = 24 * 60 * 60L;

	/**
	 * 半天的秒数
	 */
	long HALF_DAY_SEC = ONE_DAY_SEC / 2;

	/**
	 * 一天的毫秒数
	 */
	long ONE_DAY_MILL = ONE_DAY_SEC * 1000;

	/**
	 * 半天的毫秒数
	 */
	long HALF_DAY_MILL = ONE_DAY_MILL / 2;
}
