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
package icu.easyj.crypto;

import icu.easyj.core.util.EnumUtils;

/**
 * 加密算法类型
 *
 * @author wangliang181230
 */
public enum CryptoType {

	/**
	 * 对称加密算法
	 */
	Symmetric,

	/**
	 * 非对称加密算法
	 */
	Asymmetric;


	/**
	 * 根据枚举名获取枚举
	 *
	 * @param name 枚举名
	 * @return enum 枚举
	 */
	public static CryptoType get(String name) {
		return EnumUtils.fromName(CryptoType.class, name);
	}
}
