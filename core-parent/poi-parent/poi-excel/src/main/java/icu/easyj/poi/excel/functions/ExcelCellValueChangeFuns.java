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
package icu.easyj.poi.excel.functions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import icu.easyj.core.util.DateUtils;

/**
 * 值类型相互转换 工具类
 *
 * @author wangliang181230
 */
@SuppressWarnings("deprecation")
public class ExcelCellValueChangeFuns {

	//****************************************** String to All **********************************************/

	public static Character StringToCharacter(Object value) {
		return value.toString().charAt(0);
	}

	public static BigDecimal StringToBigDecimal(Object value) {
		return BigDecimal.valueOf(Double.valueOf(value.toString()));
	}

	public static BigInteger StringToBigInteger(Object value) {
		return BigInteger.valueOf(StringToLong(value));
	}

	public static Double StringToDouble(Object value) {
		return Double.valueOf(value.toString());
	}

	public static Float StringToFloat(Object value) {
		return Float.valueOf(value.toString());
	}

	public static Long StringToLong(Object value) {
		return (long)(double)StringToDouble(value);
	}

	public static Integer StringToInteger(Object value) {
		return (int)(double)StringToDouble(value);
	}

	public static Short StringToShort(Object value) {
		return (short)(double)StringToDouble(value);
	}

	public static Boolean StringToBoolean(Object value) {
		return Boolean.valueOf(value.toString());
	}

	public static Byte StringToByte(Object value) {
		return Byte.valueOf(value.toString());
	}

	public static Date StringToDate(Object value) {
		return DateUtils.parseAll(value.toString());
	}

	public static java.sql.Date StringToSqlDate(Object value) {
		Date date = StringToDate(value);
		if (date == null) return java.sql.Date.valueOf(value.toString());
		return new java.sql.Date(date.getTime());
	}

	public static java.sql.Time StringToSqlTime(Object value) {
		Date date = StringToDate(value);
		if (date == null) return java.sql.Time.valueOf(value.toString());
		return new java.sql.Time(date.getTime());
	}

	public static java.sql.Timestamp StringToSqlTimestamp(Object value) {
		Date date = StringToDate(value);
		if (date == null) return java.sql.Timestamp.valueOf(value.toString());
		return new java.sql.Timestamp(date.getTime());
	}

	//****************************************** double to All **********************************************/

	public static String doubleToString(Object value) {
		String str = value.toString();
		if ((double)value == Math.round((double)value)) return str.substring(0, str.indexOf("."));
		else return str;
	}

	public static BigDecimal doubleToBigDecimal(Object value) {
		return BigDecimal.valueOf((double)value);
	}

	public static BigInteger doubleToBigInteger(Object value) {
		return BigInteger.valueOf(doubleToLong(value));
	}

	public static Float doubleToFloat(Object value) {
		return (float)(double)value;
	}

	public static Long doubleToLong(Object value) {
		return (long)(double)value;
	}

	public static Integer doubleToInteger(Object value) {
		return (int)(double)value;
	}

	public static Short doubleToShort(Object value) {
		return (short)(double)value;
	}

	public static Boolean doubleToBoolean(Object value) {
		return (double)value > 0;
	}

	public static Byte doubleToByte(Object value) {
		return (double)value > 0 ? (byte)1 : (byte)0;
	}

	public static Date doubleToDate(Object value) {
		// excel中的日期，获取出来的值是一个double型数字，其值为该日期距离1900年1月0日的天数，如不是0点则会含小数。
		Date time = new Date(Math.round((Double)value * 24 * 60 * 60 * 1000));
		// java中的日期，开始于1970年1月1日08:00，所以需要减掉70年1天8小时，即70年32小时
		time.setYear(time.getYear() - 70); // 减年70年（不能通过70*365*24*60*60*1000来减time值，因为部分年份为366天）
		time.setTime(time.getTime() - 32 * 60 * 60 * 1000);// 减掉32小时
		return time;
	}

	//****************************************** boolean to All **********************************************/

	public static String booleanToString(Object value) {
		return value.toString();
	}

	public static Double booleanToDouble(Object value) {
		return (boolean)value ? 1D : 0;
	}

	public static Float booleanToFloat(Object value) {
		return (boolean)value ? 1F : 0;
	}

	public static Long booleanToLong(Object value) {
		return (boolean)value ? 1L : 0;
	}

	public static Integer booleanToInteger(Object value) {
		return (boolean)value ? 1 : 0;
	}

	public static Short booleanToShort(Object value) {
		return (boolean)value ? (short)1 : 0;
	}

	public static Byte booleanToByte(Object value) {
		return (boolean)value ? (byte)1 : 0;
	}
}
