package org.easyframework.core.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

import static org.easyframework.core.date.DateConstant.ONE_DAY_MILL;

/**
 * Date工具类
 *
 * @author wangliang181230
 */
@SuppressWarnings("deprecation")
public class DateUtils {

	/**
	 * 获取指定时间的日期
	 *
	 * @param time 时间
	 * @return date 日期
	 */
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

	//region 解析时间字符串的方法

	/**
	 * 解析时间
	 *
	 * @param str    时间字符串
	 * @param format 格式化串
	 * @return time
	 * @throws ParseException 解析异常
	 */
	public static Date parse(String str, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str);
	}

	public static Date parseMonth(String str) throws ParseException {
		return parse(str, DateFormatConstant.MM);
	}

	public static Date parseDate(String str) throws ParseException {
		return parse(str, DateFormatConstant.DD);
	}

	public static Date parseMinutes(String str) throws ParseException {
		return parse(str, DateFormatConstant.MI);
	}

	public static Date parseSeconds(String str) throws ParseException {
		return parse(str, DateFormatConstant.SS);
	}

	public static Date parseMillisecond(String str) throws ParseException {
		return parse(str, DateFormatConstant.SSS);
	}


	public static Date parseMonth2(String str) throws ParseException {
		return parse(str, DateFormatConstant.MM2);
	}

	public static Date parseDate2(String str) throws ParseException {
		return parse(str, DateFormatConstant.DD2);
	}

	public static Date parseMinutes2(String str) throws ParseException {
		return parse(str, DateFormatConstant.MI2);
	}

	public static Date parseSeconds2(String str) throws ParseException {
		return parse(str, DateFormatConstant.SS2);
	}

	public static Date parseMillisecond2(String str) throws ParseException {
		return parse(str, DateFormatConstant.SSS2);
	}


	public static Date parseMonthUnsign(String str) throws ParseException {
		return parse(str, DateFormatConstant.MM_UNSIGN);
	}

	public static Date parseDateUnsign(String str) throws ParseException {
		return parse(str, DateFormatConstant.DD_UNSIGN);
	}

	public static Date parseMinutesUnsign(String str) throws ParseException {
		return parse(str, DateFormatConstant.MI_UNSIGN);
	}

	public static Date parseSecondsUnsign(String str) throws ParseException {
		return parse(str, DateFormatConstant.SS_UNSIGN);
	}

	public static Date parseMillisecondUnsign(String str) throws ParseException {
		return parse(str, DateFormatConstant.SSS_UNSIGN);
	}

	/**
	 * 解析时间字符串
	 *
	 * @param str 时间字符串
	 * @return time 时间
	 */
	public static Date parseAll(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}

		try {
			switch (str.length()) {
				case 7:
					if (str.contains("-")) {
						return parseMonth(str);
					} else {
						return parseMonth2(str);
					}
				case 10:
					if (str.contains("-")) {
						return parseDate(str);
					} else {
						return parseDate2(str);
					}
				case 16:
					if (str.contains("-")) {
						return parseMinutes(str);
					} else {
						return parseSeconds2(str);
					}
				case 19:
					if (str.contains("-")) {
						return parseSeconds(str);
					} else {
						return parseMinutes2(str);
					}
				case 23:
					if (str.contains("-")) {
						return parseMillisecond(str);
					} else {
						return parseMillisecond2(str);
					}

				case 6:
					return parseMonthUnsign(str);
				case 8:
					return parseDateUnsign(str);
				case 12:
					return parseMinutesUnsign(str);
				case 14:
					return parseSecondsUnsign(str);
				case 17:
					return parseMillisecondUnsign(str);

				default:
					break;
			}
		} catch (ParseException ignore) {
		}

		// 如果上面的方式都失败，则直接使用 new Date(String s); 的方式转换
		return new Date(str);
	}

	//endregion
}
