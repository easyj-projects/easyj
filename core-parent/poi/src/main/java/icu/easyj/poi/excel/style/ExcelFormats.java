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
package icu.easyj.poi.excel.style;

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
