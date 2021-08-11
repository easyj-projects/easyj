/*
 * Copyright 2021 the original author or authors.
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
package icu.easyj.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import icu.easyj.core.enums.DateFormatType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import static icu.easyj.core.constant.DateConstants.ONE_DAY_MILL;

/**
 * Date工具类
 *
 * @author wangliang181230
 */
@SuppressWarnings("deprecation")
public abstract class DateUtils {

	//region 获取特殊日期的方法

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
	@NonNull
	public static Date getTomorrowDate(@NonNull Date time) {
		return getDate(new Date(time.getTime() + ONE_DAY_MILL));
	}

	//endregion


	//region 解析时间字符串的方法

	/**
	 * 解析常用时间
	 *
	 * @param str    时间字符串
	 * @param format 常用时间格式
	 * @return time 时间
	 * @throws ParseException 解析异常
	 */
	@NonNull
	public static Date parse(@NonNull String str, @NonNull DateFormatType format) throws ParseException {
		SimpleDateFormat sdf = LocalDateFormatHolder.get(format);
		return sdf.parse(str);
	}

	/**
	 * 解析时间
	 *
	 * @param str    时间字符串
	 * @param format 格式化串
	 * @return time
	 * @throws ParseException 解析异常
	 */
	@NonNull
	public static Date parse(@NonNull String str, @NonNull String format) throws ParseException {
		SimpleDateFormat sdf = LocalDateFormatHolder.get(format);
		return sdf.parse(str);
	}

	@NonNull
	public static Date parseMonth(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.MM);
	}

	@NonNull
	public static Date parseDate(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.DD);
	}

	@NonNull
	public static Date parseMinutes(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.MI);
	}

	@NonNull
	public static Date parseSeconds(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.SS);
	}

	@NonNull
	public static Date parseMillisecond(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.SSS);
	}


	@NonNull
	public static Date parseMonth2(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.MM2);
	}

	@NonNull
	public static Date parseDate2(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.DD2);
	}

	public static Date parseMinutes2(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.MI2);
	}

	@NonNull
	public static Date parseSeconds2(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.SS2);
	}

	@NonNull
	public static Date parseMillisecond2(String str) throws ParseException {
		return parse(str, DateFormatType.SSS2);
	}


	@NonNull
	public static Date parseMonthUnsigned(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.MM_UNSIGNED);
	}

	@NonNull
	public static Date parseDateUnsigned(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.DD_UNSIGNED);
	}

	@NonNull
	public static Date parseMinutesUnsigned(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.MI_UNSIGNED);
	}

	@NonNull
	public static Date parseSecondsUnsigned(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.SS_UNSIGNED);
	}

	@NonNull
	public static Date parseMillisecondUnsigned(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.SSS_UNSIGNED);
	}

	/**
	 * 解析时间字符串
	 *
	 * @param timeStr 时间字符串
	 * @return time 时间
	 */
	@NonNull
	public static Date parseAll(@NonNull String timeStr) {
		Assert.notNull(timeStr, "timeStr must be not null");

		try {
			switch (timeStr.length()) {
				case 7:
					if (timeStr.contains("-")) {
						return parseMonth(timeStr);
					} else {
						return parseMonth2(timeStr);
					}
				case 10:
					if (timeStr.contains("-")) {
						return parseDate(timeStr);
					} else {
						return parseDate2(timeStr);
					}
				case 16:
					if (timeStr.contains("-")) {
						return parseMinutes(timeStr);
					} else {
						return parseSeconds2(timeStr);
					}
				case 19:
					if (timeStr.contains("-")) {
						return parseSeconds(timeStr);
					} else {
						return parseMinutes2(timeStr);
					}
				case 23:
					if (timeStr.contains("-")) {
						return parseMillisecond(timeStr);
					} else {
						return parseMillisecond2(timeStr);
					}

				case 6:
					return parseMonthUnsigned(timeStr);
				case 8:
					return parseDateUnsigned(timeStr);
				case 12:
					return parseMinutesUnsigned(timeStr);
				case 14:
					return parseSecondsUnsigned(timeStr);
				case 17:
					return parseMillisecondUnsigned(timeStr);

				default:
					break;
			}
		} catch (ParseException ignore) {
		}

		// 如果上面的方式都失败，则直接使用 new Date(String timeStr); 的方式转换
		return new Date(timeStr);
	}

	//endregion


	//region 格式化输出日期

	/**
	 * 时间格式化
	 *
	 * @param dateFormat 时间格式化
	 * @param date       时间
	 * @return dateStr 格式化的时间字符串
	 */
	@NonNull
	public static String format(@NonNull String dateFormat, @NonNull Date date) {
		Assert.notNull(dateFormat, "dateFormat must be not null");
		Assert.notNull(date, "date must be not null");

		return LocalDateFormatHolder.get(dateFormat).format(date);
	}

	/**
	 * 时间格式化
	 *
	 * @param dateFormat 时间格式化
	 * @param date       时间
	 * @return dateStr 格式化的时间字符串
	 */
	@NonNull
	public static String format(@NonNull DateFormatType dateFormat, @NonNull Date date) {
		Assert.notNull(dateFormat, "dateFormat must be not null");
		Assert.notNull(date, "date must be not null");

		return LocalDateFormatHolder.get(dateFormat).format(date);
	}

	//endregion


	//region toString

	/**
	 * 将时间对象转换为字符串
	 *
	 * @param date 时间
	 * @return str 转换后的字符串
	 */
	@NonNull
	public static String toString(@Nullable Date date) {
		if (date == null) {
			return "null";
		}

		long time = date.getTime();

		DateFormatType dateFormat;
		if (date.getHours() == 0 && date.getMinutes() == 0 && date.getSeconds() == 0 && time % 1000 == 0) {
			dateFormat = DateFormatType.DD;
		} else if (time % (60 * 1000) == 0) {
			dateFormat = DateFormatType.MI;
		} else if (time % 1000 == 0) {
			dateFormat = DateFormatType.SS;
		} else {
			dateFormat = DateFormatType.SSS;
		}

		return DateUtils.format(dateFormat, date);
	}

	//endregion
}
