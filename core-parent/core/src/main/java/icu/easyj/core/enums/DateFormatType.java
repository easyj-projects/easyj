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
package icu.easyj.core.enums;

import icu.easyj.core.constant.DateFormatConstants;

/**
 * 常用时间格式枚举
 *
 * @author wangliang181230
 */
@SuppressWarnings("all")
public enum DateFormatType {

	MM(DateFormatConstants.MM), // 精确到月，长度：7
	DD(DateFormatConstants.DD), // 精确到日，长度：10
	MI(DateFormatConstants.MI), // 精确到分，长度：16
	SS(DateFormatConstants.SS), // 精确到秒，长度：19
	SSS(DateFormatConstants.SSS), // 完整格式，精确到毫秒，长度：23

	MM2(DateFormatConstants.MM2), // 精确到月，长度：7
	DD2(DateFormatConstants.DD2), // 精确到日，长度：10
	MI2(DateFormatConstants.MI2), // 精确到分，长度：16
	SS2(DateFormatConstants.SS2), // 精确到秒，长度：19
	SSS2(DateFormatConstants.SSS2), // 完整格式，精确到毫秒，长度：23

	MM3(DateFormatConstants.MM3), // 精确到月，长度：7
	DD3(DateFormatConstants.DD3), // 精确到日，长度：10

	MM_UNSIGNED(DateFormatConstants.MM_UNSIGNED), // 精确到月，长度：6
	DD_UNSIGNED(DateFormatConstants.DD_UNSIGNED), // 精确到日，长度：8
	MI_UNSIGNED(DateFormatConstants.MI_UNSIGNED), // 精确到分，长度：12
	SS_UNSIGNED(DateFormatConstants.SS_UNSIGNED), // 精确到秒，长度：14
	SSS_UNSIGNED(DateFormatConstants.SSS_UNSIGNED), // 完整格式，精确到毫秒，长度：17

	TIME_MM(DateFormatConstants.TIME_MM), // 无日期，精确到分钟，长度：5
	TIME_SS(DateFormatConstants.TIME_SS); // 无日期，精确到秒，长度：8

	/**
	 * 时间格式
	 */
	private final String format;

	/**
	 * 构造函数
	 *
	 * @param format 时间格式
	 */
	DateFormatType(String format) {
		this.format = format;
	}

	/**
	 * @return 时间格式
	 */
	public String getFormat() {
		return this.format;
	}
}
