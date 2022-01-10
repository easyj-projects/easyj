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
package icu.easyj.core.env;

import icu.easyj.core.util.EnumUtils;

/**
 * 环境类型枚举
 *
 * @author wangliang181230
 */
public enum EnvironmentType {

	/**
	 * 生产环境
	 */
	PROD,

	/**
	 * 沙箱环境
	 * <p>
	 * 一般用于提供用户体验、第三方对接等的环境。
	 */
	SANDBOX,

	/**
	 * 测试环境
	 * <p>
	 * 一般用于预发布环境、联调测试环境等。
	 */
	TEST,

	/**
	 * 开发环境
	 */
	DEV;


	/**
	 * 根据枚举名获取枚举
	 *
	 * @param name 枚举名
	 * @return enum 枚举
	 */
	public static EnvironmentType get(String name) {
		return EnumUtils.fromName(EnvironmentType.class, name);
	}
}
