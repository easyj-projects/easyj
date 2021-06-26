package org.easyj.office.excel.style;

/**
 * Excel中常用的格式
 *
 * @author wangliang181230
 */
public interface ExcelFormats {
	// 日期格式化
	String YYYY = "yyyy"; // 年
	String YYYYMM = "yyyy-MM"; // 年月
	String YYYYMMDD = "yyyy-MM-dd"; // 年月日
	String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm"; // 年月日时分
	String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss"; // 年月日时分秒（默认）


	String INT = "0_ "; // 整形数据格式化
	String FLOAT_1 = "0.0_ "; // 浮点数据格式化，保留小数1位
	String FLOAT_2 = "0.00_ "; // 浮点数据格式化，保留小数2位
	String FLOAT_3 = "0.000_ "; // 浮点数据格式化，保留小数3位
	String FLOAT_4 = "0.0000_ "; // 浮点数据格式化，保留小数4位
	String FLOAT_5 = "0.00000_ "; // 浮点数据格式化，保留小数5位
	String FLOAT_6 = "0.000000_ "; // 浮点数据格式化，保留小数6位

	String PERCENT = "0%"; // 百分比，不保留小数
	String PERCENT_1 = "0.0%"; // 百分比，保留小数1位
	String PERCENT_2 = "0.00%"; // 百分比，保留小数2位
	String PERCENT_3 = "0.000%"; // 百分比，保留小数3位
	String PERCENT_4 = "0.0000%"; // 百分比，保留小数4位
}
