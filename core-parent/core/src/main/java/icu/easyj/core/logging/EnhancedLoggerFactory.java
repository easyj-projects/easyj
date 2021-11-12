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
package icu.easyj.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 增强日志记录器工厂
 *
 * @author wangliang181230
 */
public abstract class EnhancedLoggerFactory {

	/**
	 * 获取增强后的日志记录器
	 *
	 * @param clazz 类（返回的记录器将以类名来命名）
	 * @return 增强后的日志记录器
	 */
	public static Logger getLogger(Class<?> clazz) {
		return new EnhancedLogger(LoggerFactory.getLogger(clazz));
	}

	/**
	 * 获取增强后的日志记录器
	 *
	 * @param name 日志名称
	 * @return 增强后的日志记录器
	 */
	public static Logger getLogger(String name) {
		return new EnhancedLogger(LoggerFactory.getLogger(name));
	}
}
