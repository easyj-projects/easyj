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
package icu.easyj.jwt;

import java.util.Map;

import static icu.easyj.jwt.JwtConstant.JWT_SPECIAL_CLAIMS_KEYS;

/**
 * JWT工具类
 *
 * @author wangliang181230
 */
public abstract class JwtUtils {

	/**
	 * 移除特殊声明信息
	 *
	 * @param claims 声明
	 */
	public static void removeSpecialClaims(Map<String, Object> claims) {
		for (String key : JWT_SPECIAL_CLAIMS_KEYS) {
			claims.remove(key);
		}
	}
}
