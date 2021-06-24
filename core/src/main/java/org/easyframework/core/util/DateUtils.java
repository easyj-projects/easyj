package org.easyframework.core.util;

import java.util.Date;

import static org.easyframework.core.constant.DateConstant.ONE_DAY_MILL;

/**
 * Date工具类
 *
 * @author wangliang181230
 */
public class DateUtils {

	/**
	 * 获取指定时间的日期
	 *
	 * @param time 时间
	 * @return date 日期
	 */
	@SuppressWarnings("deprecation")
	public static Date getDate(Date time) {
		return new Date(time.getYear(), time.getMonth(), time.getDate());
	}

	/**
	 * 获取指定时间的明天的日期
	 *
	 * @param time 时间
	 * @return tomorrowDate 明天的日期
	 */
	public static Date getTomorrowDate(Date time) {
		return getDate(new Date(time.getTime() + ONE_DAY_MILL));
	}
}
