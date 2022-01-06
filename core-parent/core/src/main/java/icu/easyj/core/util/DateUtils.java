/*
 * Copyright 2021-2022 the original author or authors.
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
	@NonNull
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
		SimpleDateFormat sdf = DateFormatFactory.get(format);
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
		SimpleDateFormat sdf = DateFormatFactory.get(format);
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
	public static Date parseMonth3(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.MM3);
	}

	@NonNull
	public static Date parseDate3(@NonNull String str) throws ParseException {
		return parse(str, DateFormatType.DD3);
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
		Assert.notNull(timeStr, "'timeStr' must not be null");

		RuntimeException e = null;
		if (timeStr.contains("-")) {
			timeStr = timeStr.replace('-', '/');
			try {
				return new Date(timeStr);
			} catch (RuntimeException re) {
				e = re;
			}
		}

		try {
			switch (timeStr.length()) {
				case 7:
					if (timeStr.contains("/")) {
						return parseMonth2(timeStr);
					} else {
						return parseMonth3(timeStr);
					}
				case 10:
					if (timeStr.contains("/")) {
						return parseDate2(timeStr);
					} else {
						return parseDate3(timeStr);
					}
				case 16:
					return parseMinutes2(timeStr);
				case 19:
					return parseSeconds2(timeStr);
				case 23:
					return parseMillisecond2(timeStr);

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

		if (e != null) {
			throw e;
		}

		// 如果上面的方式不匹配或解析失败了，则使用下面的方式直接构建实例
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
		Assert.notNull(dateFormat, "'dateFormat' must not be null");
		Assert.notNull(date, "'date' must not be null");

		return DateFormatFactory.get(dateFormat).format(date);
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
		Assert.notNull(dateFormat, "'dateFormat' must not be null");
		Assert.notNull(date, "'date' must not be null");

		return DateFormatFactory.get(dateFormat).format(date);
	}


	////region 格式化1

	/**
	 * 格式化为：yyyy-MM
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMonth(Date date) {
		return format(DateFormatType.MM, date);
	}

	/**
	 * 格式化为：yyyy-MM-dd
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toDate(Date date) {
		return format(DateFormatType.DD, date);
	}

	/**
	 * 格式化为：yyyy-MM-dd HH:mm
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMinute(Date date) {
		return format(DateFormatType.MI, date);
	}

	/**
	 * 格式化为：yyyy-MM-dd HH:mm:ss
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toSeconds(Date date) {
		return format(DateFormatType.SS, date);
	}

	/**
	 * 格式化为：yyyy-MM-dd HH:mm:ss.SSS
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMilliseconds(Date date) {
		return format(DateFormatType.SSS, date);
	}

	////endregion 格式化1 end


	////region 格式化2

	/**
	 * 格式化为：yyyy/MM
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMonth2(Date date) {
		return format(DateFormatType.MM2, date);
	}

	/**
	 * 格式化为：yyyy/MM/dd
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toDate2(Date date) {
		return format(DateFormatType.DD2, date);
	}

	/**
	 * 格式化为：yyyy/MM/dd HH:mm
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMinute2(Date date) {
		return format(DateFormatType.MI2, date);
	}

	/**
	 * 格式化为：yyyy/MM/dd HH:mm:ss
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toSeconds2(Date date) {
		return format(DateFormatType.SS2, date);
	}

	/**
	 * 格式化为：yyyy/MM/dd HH:mm:ss.SSS
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMilliseconds2(Date date) {
		return format(DateFormatType.SSS2, date);
	}

	////endregion 格式化2 end


	////region 格式化3

	/**
	 * 格式化为：yyyy.MM
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMonth3(Date date) {
		return format(DateFormatType.MM3, date);
	}

	/**
	 * 格式化为：yyyy.MM.dd
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toDate3(Date date) {
		return format(DateFormatType.DD3, date);
	}

	////endregion 格式化3 end


	////region 格式化-UNSIGNED

	/**
	 * 格式化为：yyyyMM
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMonthUnsigned(Date date) {
		return format(DateFormatType.MM_UNSIGNED, date);
	}

	/**
	 * 格式化为：yyyyMMdd
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toDateUnsigned(Date date) {
		return format(DateFormatType.DD_UNSIGNED, date);
	}

	/**
	 * 格式化为：yyyyMMddHHmm
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMinuteUnsigned(Date date) {
		return format(DateFormatType.MI_UNSIGNED, date);
	}

	/**
	 * 格式化为：yyyyMMddHHmmss
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toSecondsUnsigned(Date date) {
		return format(DateFormatType.SS_UNSIGNED, date);
	}

	/**
	 * 格式化为：yyyyMMddHHmmssSSS
	 *
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String toMillisecondsUnsigned(Date date) {
		return format(DateFormatType.SSS_UNSIGNED, date);
	}

	////endregion 格式化-UNSIGNED end

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
