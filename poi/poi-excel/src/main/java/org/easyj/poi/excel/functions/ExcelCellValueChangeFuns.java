package org.easyj.poi.excel.functions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.easyj.core.util.DateUtils;

/**
 * 值类型相互转换 工具类
 *
 * @author wangliang181230
 */
@SuppressWarnings("deprecation")
public class ExcelCellValueChangeFuns {

	// 测试
	public static void main(String[] args) {
		System.out.println("*************** String *****************");
		String s = "1.111";
		System.out.println(StringToCharacter(s));
		System.out.println(StringToBigDecimal(s));
		System.out.println(StringToBigInteger(s));
		System.out.println(StringToDouble(s));
		System.out.println(StringToFloat(s));
		System.out.println(StringToLong(s));
		System.out.println(StringToInteger(s));
		System.out.println(StringToShort(s));
		System.out.println(StringToBoolean(s));
		System.out.println(StringToByte("1"));
		System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(StringToDate("201801")));
		System.out.println(StringToSqlDate("201801"));
		System.out.println(StringToSqlTime("201801"));
		System.out.println(StringToSqlTimestamp("201801"));

		System.out.println("*************** double 1 *****************");
		Double d1 = 1.1D;
		System.out.println(doubleToString(d1));
		System.out.println(doubleToBigDecimal(d1));
		System.out.println(doubleToBigInteger(d1));
		System.out.println(doubleToFloat(d1));
		System.out.println(doubleToLong(d1));
		System.out.println(doubleToInteger(d1));
		System.out.println(doubleToShort(d1));
		System.out.println(doubleToBoolean(d1));
		System.out.println(doubleToByte(d1));
		System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(doubleToDate(d1)));
		System.out.println("*************** double 0 *****************");
		Double d0 = 0D;
		System.out.println(doubleToString(d0));
		System.out.println(doubleToBigDecimal(d0));
		System.out.println(doubleToBigInteger(d0));
		System.out.println(doubleToFloat(d0));
		System.out.println(doubleToLong(d0));
		System.out.println(doubleToInteger(d0));
		System.out.println(doubleToShort(d0));
		System.out.println(doubleToBoolean(d0));
		System.out.println(doubleToByte(d0));
		System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(doubleToDate(d0)));

		Boolean b1 = true;
		System.out.println("*************** double 1 *****************");
		System.out.println(booleanToString(b1));
		System.out.println(booleanToDouble(b1));
		System.out.println(booleanToFloat(b1));
		System.out.println(booleanToLong(b1));
		System.out.println(booleanToInteger(b1));
		System.out.println(booleanToShort(b1));
		System.out.println(booleanToByte(b1));
		Boolean b0 = false;
		System.out.println("*************** double 0 *****************");
		System.out.println(booleanToString(b0));
		System.out.println(booleanToDouble(b0));
		System.out.println(booleanToFloat(b0));
		System.out.println(booleanToLong(b0));
		System.out.println(booleanToInteger(b0));
		System.out.println(booleanToShort(b0));
		System.out.println(booleanToByte(b0));
	}

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
