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

import icu.easyj.core.code.analysis.CodeAnalysisResult;
import icu.easyj.core.exception.ConfigurationException;
import icu.easyj.core.util.ArrayUtils;
import icu.easyj.core.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类函数式配置工具类
 *
 * @author wangliang181230
 */
public abstract class ClassPropertyUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassPropertyUtils.class);

	public static String getProperty(String name, CodeAnalysisResult result) {
		String property = null;
		try {
			// 执行方法对应的逻辑
			Object[] parameters = result.getParameters();
			switch (result.getMethodName()) {
				case "isExist":
					if (ArrayUtils.isNotEmpty(parameters)) {
						for (Object param : parameters) {
							if (ClassUtils.isNotExist(param.toString())) {
								property = "false";
								return property;
							}
						}
						property = "true";
						return property;
					} else {
						throw new ConfigurationException("'${easyj.class.isExist(String... classNames)}' 函数的类名数组参数不能为空");
					}
				case "getExistingOne":
					if (ArrayUtils.isNotEmpty(parameters)) {
						for (Object param : parameters) {
							if (ClassUtils.isExist(property = param.toString())) {
								// 如果类存在，则返回
								return property;
							}
						}
						property = null;
						throw new ConfigurationException("类函数 '" + name + "' 中，没有一个类存在");
					} else {
						throw new ConfigurationException("'${easyj.class.getExistingOne(String... classNames)}' 函数的类名数组参数不能为空");
					}
				default:
					throw new ConfigurationException("不支持的类函数式配置：${" + name + "}");
			}
		} finally {
			if (property != null) {
				LOGGER.info("函数式配置`${{}}`的值: {}", name, property);
			}
		}
	}
}
