/*
 * Copyright 2021-2024 the original author or authors.
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

import java.util.Date;

/**
 * JWT解析器
 *
 * @author wangliang181230
 */
public interface IJwtParser {

	/**
	 * 解析JWT
	 *
	 * @param jwtStr JWT串
	 * @param leeway 容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
	 * @return jwtInfo 返回解析出的JWT信息
	 */
	JwtInfo parse(String jwtStr, long leeway);

	/**
	 * 解析JWT，默认容忍空间为2秒
	 *
	 * @param jwtStr JWT串
	 * @return jwt
	 */
	default JwtInfo parse(String jwtStr) {
		return parse(jwtStr, 2000);
	}

	/**
	 * 解析JWT
	 *
	 * @param jwtStr             JWT串
	 * @param leeway             容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
	 * @param currentExpiredTime 当前的过期时间，单位：秒。（作用是怕之前设置的时间过长，导致生成的JWT一直不会过期，使系统存在安全隐患。）
	 * @return jwt
	 */
	default JwtInfo parse(String jwtStr, long leeway, int currentExpiredTime) {
		JwtInfo jwtInfo = parse(jwtStr, leeway);

		// 使用新的过期时间重新校验一次时间
		if (currentExpiredTime > 0) {
			Date issuedAt = jwtInfo.getIssuedAt();
			if (issuedAt.getTime() + currentExpiredTime * 1000 <= System.currentTimeMillis()) {
				throw new RuntimeException("JWT已过期");
			}
		}

		return jwtInfo;
	}
}
