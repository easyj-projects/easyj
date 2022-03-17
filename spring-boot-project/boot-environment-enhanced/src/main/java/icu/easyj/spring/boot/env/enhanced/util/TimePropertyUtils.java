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
package icu.easyj.spring.boot.env.enhanced.util;

import java.util.Date;

import icu.easyj.core.code.analysis.CodeAnalysisResult;
import icu.easyj.core.exception.ConfigurationException;
import icu.easyj.core.util.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间配置工具类
 *
 * @author wangliang181230
 */
public abstract class TimePropertyUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimePropertyUtils.class);

	/**
	 * 根据配置命令解析结果获取配置值
	 *
	 * @param name   配置信息
	 * @param result 配置命令解析结果
	 * @return value 配置值
	 */
	public static Object getProperty(String name, CodeAnalysisResult result) {
		Object value = null;

		try {
			// 执行方法对应的逻辑
			Object[] parameters = result.getParameters();
			switch (result.getMethodName()) {
				// 当前时间
				case "now":
					String format = getFormat(parameters);
					value = DateUtils.format(format, new Date());
					break;

				// 未知
				default:
					throw new ConfigurationException("不支持当前时间内容的生成，当前的配置：${" + name + "}");
			}

			return value;
		} finally {
			// 随机数比较特殊，在这里打印一下信息
			if (value != null) {
				LOGGER.info("函数式配置`${{}}`的值: {}", name, value);
			}
		}
	}

	private static String getFormat(Object[] parameters) {
		if (ArrayUtils.isEmpty(parameters) || parameters[0] == null) {
			return "yyyy-MM-dd HH:mm:ss.SSS";
		}

		return String.valueOf(parameters[0]);
	}
}
