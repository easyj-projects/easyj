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

import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.lang.Assert;
import icu.easyj.core.enums.DateFormatType;
import org.springframework.lang.NonNull;

/**
 * 日期格式化实例持有者
 *
 * @author wangliang181230
 */
public abstract class LocalDateFormatHolder {

	//region 非 常用日期格式

	public static final ThreadLocal<Map<String, SimpleDateFormat>> DATE_FORMAT = ThreadLocal.withInitial(HashMap::new);

	/**
	 * 获取当前线程的时间格式化实例
	 *
	 * @param dateFormat 时间格式
	 * @return dateFormat 时间格式化实例
	 */
	@NonNull
	public static SimpleDateFormat get(@NonNull String dateFormat) {
		Assert.notNull(dateFormat, "'dateFormat' must be not null");
		Map<String, SimpleDateFormat> dateFormatMap = DATE_FORMAT.get();
		return MapUtils.computeIfAbsent(dateFormatMap, dateFormat, f -> new SimpleDateFormat(dateFormat));
	}

	//endregion


	//region 常用日期格式

	public static final ThreadLocal<EnumMap<DateFormatType, SimpleDateFormat>> FREQUENTLY_USED_DATE_FORMAT
			= ThreadLocal.withInitial(() -> new EnumMap<>(DateFormatType.class));

	/**
	 * 获取当前线程的常用时间格式化实例（时间格式）
	 *
	 * @param dateFormat 常用时间格式
	 * @return dateFormat 常用时间格式化实例
	 */
	@NonNull
	public static SimpleDateFormat get(@NonNull DateFormatType dateFormat) {
		Assert.notNull(dateFormat, "'dateFormat' must be not null");
		Map<DateFormatType, SimpleDateFormat> dateFormatMap = FREQUENTLY_USED_DATE_FORMAT.get();
		return MapUtils.computeIfAbsent(dateFormatMap, dateFormat, f -> new SimpleDateFormat(dateFormat.getFormat()));
	}

	//endregion
}
