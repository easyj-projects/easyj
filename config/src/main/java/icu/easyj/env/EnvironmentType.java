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
	 */
	SANDBOX,

	/**
	 * 测试环境
	 */
	TEST,

	/**
	 * 开发环境
	 */
	DEV;

	/**
	 * 根据 `环境名` 获取 `环境类型枚举`
	 *
	 * @param name 环境名
	 * @return enum 环境类型枚举
	 */
	public static EnvironmentType get(String name) {
		return EnumUtils.fromName(EnvironmentType.class, name);
	}
}
