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
package icu.easyj.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import icu.easyj.core.constant.DateFormatConstants;
import org.springframework.lang.NonNull;

import static icu.easyj.core.constant.DateConstants.ONE_DAY_MILL;

/**
 * Date工具类
 *
 * @author wangliang181230
 */
@SuppressWarnings("deprecation")
public abstract class DateUtils {

	//region 获取日期

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

	//endregion


	//region 解析时间字符串的方法

	/**
	 * 解析时间
	 *
	 * @param str    时间字符串
	 * @param format 格式化串
	 * @return time
	 * @throws ParseException 解析异常
	 */
	@NonNull
	public static Date parse(String str, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str);
	}

	@NonNull
	public static Date parseMonth(String str) throws ParseException {
		return parse(str, DateFormatConstants.MM);
	}

	@NonNull
	public static Date parseDate(String str) throws ParseException {
		return parse(str, DateFormatConstants.DD);
	}

	@NonNull
	public static Date parseMinutes(String str) throws ParseException {
		return parse(str, DateFormatConstants.MI);
	}

	@NonNull
	public static Date parseSeconds(String str) throws ParseException {
		return parse(str, DateFormatConstants.SS);
	}

	@NonNull
	public static Date parseMillisecond(String str) throws ParseException {
		return parse(str, DateFormatConstants.SSS);
	}


	@NonNull
	public static Date parseMonth2(String str) throws ParseException {
		return parse(str, DateFormatConstants.MM2);
	}

	@NonNull
	public static Date parseDate2(String str) throws ParseException {
		return parse(str, DateFormatConstants.DD2);
	}

	public static Date parseMinutes2(String str) throws ParseException {
		return parse(str, DateFormatConstants.MI2);
	}

	@NonNull
	public static Date parseSeconds2(String str) throws ParseException {
		return parse(str, DateFormatConstants.SS2);
	}

	@NonNull
	public static Date parseMillisecond2(String str) throws ParseException {
		return parse(str, DateFormatConstants.SSS2);
	}


	@NonNull
	public static Date parseMonthUnsigned(String str) throws ParseException {
		return parse(str, DateFormatConstants.MM_UNSIGNED);
	}

	@NonNull
	public static Date parseDateUnsigned(String str) throws ParseException {
		return parse(str, DateFormatConstants.DD_UNSIGNED);
	}

	@NonNull
	public static Date parseMinutesUnsigned(String str) throws ParseException {
		return parse(str, DateFormatConstants.MI_UNSIGNED);
	}

	@NonNull
	public static Date parseSecondsUnsigned(String str) throws ParseException {
		return parse(str, DateFormatConstants.SS_UNSIGNED);
	}

	@NonNull
	public static Date parseMillisecondUnsigned(String str) throws ParseException {
		return parse(str, DateFormatConstants.SSS_UNSIGNED);
	}

	/**
	 * 解析时间字符串
	 *
	 * @param str 时间字符串
	 * @return time 时间
	 */
	@NonNull
	public static Date parseAll(String str) {
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
					return parseMonthUnsigned(str);
				case 8:
					return parseDateUnsigned(str);
				case 12:
					return parseMinutesUnsigned(str);
				case 14:
					return parseSecondsUnsigned(str);
				case 17:
					return parseMillisecondUnsigned(str);

				default:
					break;
			}
		} catch (ParseException ignore) {
		}

		// 如果上面的方式都失败，则直接使用 new Date(String s); 的方式转换
		return new Date(str);
	}

	//endregion


	//region toString

	/**
	 * 将时间对象转换为字符串
	 *
	 * @param date 时间对象
	 * @return str 转换后的字符串
	 */
	public static String toString(Date date) {
		if (date == null) {
			return "null";
		}

		long time = date.getTime();
		String dateFormat;
		if (date.getHours() == 0 && date.getMinutes() == 0 && date.getSeconds() == 0 && time % 1000 == 0) {
			dateFormat = "yyyy-MM-dd";
		} else if (time % (60 * 1000) == 0) {
			dateFormat = "yyyy-MM-dd HH:mm";
		} else if (time % 1000 == 0) {
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		} else {
			dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		return new SimpleDateFormat(dateFormat).format(date);
	}

	//endregion
}
