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
package icu.easyj.env;

import icu.easyj.core.util.EnumUtils;

/**
 * 运行模式枚举
 *
 * @author wangliang181230
 */
public enum RunMode {

	/**
	 * 发行模式
	 */
	RELEASE,

	/**
	 * 调试模式
	 */
	DEBUG;


	/**
	 * 根据 `模式名` 获取 `运行模式枚举`
	 *
	 * @param mode 模式名
	 * @return enum 运行模式枚举
	 */
	public static RunMode get(String mode) {
		return EnumUtils.fromName(RunMode.class, mode);
	}
}
