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

import io.jsonwebtoken.Claims;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * JWT常量
 *
 * @author wangliang181230
 */
public interface JwtConstant {

	/**
	 * JWT特殊声明的键值
	 */
	String[] JWT_SPECIAL_CLAIMS_KEYS = new String[]{
			Claims.ID, // jit
			Claims.ISSUED_AT, // iat
			Claims.EXPIRATION, // exp
			Claims.ISSUER, // iss
			Claims.SUBJECT, // sub
			Claims.AUDIENCE, // aud
			Claims.NOT_BEFORE // nbf
	};

	/**
	 * 默认过期时间
	 */
	Date DEFAULT_EXPIRES_AT = null;

	/**
	 * 默认容忍时间：2秒
	 */
	long DEFAULT_LEEWAY = 2L;


	//region 创建JWT

	/**
	 * 创建JWT
	 *
	 * @param jwtId     JWT的ID
	 * @param claims    私有声明
	 * @param issuedAt  签发时间
	 * @param expiresAt 过期时间，不能小于等于签发时间，为空时表示永久有效
	 * @return jwt 返回创建的JWT
	 */
	String create(String jwtId, Map<String, Object> claims, Date issuedAt, Date expiresAt);

	/**
	 * 创建JWT
	 *
	 * @param jwtId       JWT的ID
	 * @param claims      私有声明
	 * @param issuedAt    签发时间
	 * @param expiredTime 过期时间，单位：秒。小于等于0时，永久有效。
	 * @return jwt 返回创建的JWT
	 */
	default String create(String jwtId, Map<String, Object> claims, Date issuedAt, int expiredTime) {
		if (issuedAt == null) {
			issuedAt = new Date();
		}

		Date expiresAt = null;
		if (expiredTime > 0) {
			expiresAt = new Date(issuedAt.getTime() + expiredTime * 1000);
		}

		return create(jwtId, claims, issuedAt, expiresAt);
	}

	/**
	 * 创建JWT
	 *
	 * @param jwtInfo JWT信息
	 * @return jwt 返回创建的JWT
	 */
	default String create(JwtInfo jwtInfo) {
		Assert.notNull(jwtInfo, "'jwtInfo' must be not null");

		if (jwtInfo.getIssuedAt() == null) {
			jwtInfo.setIssuedAt(new Date());
		}

		int expiredTime = 0;
		if (jwtInfo.getExpiresAt() != null) {
			expiredTime = (int)((jwtInfo.getExpiresAt().getTime() - jwtInfo.getIssuedAt().getTime()) / 1000);
		}

		return create(jwtInfo.getJwtId(), jwtInfo.getClaims(), jwtInfo.getIssuedAt(), expiredTime);
	}

	/**
	 * 创建JWT（永久不过期）
	 *
	 * @param jwtId    JWT的ID
	 * @param claims   私有声明
	 * @param issuedAt 签发时间
	 * @return jwt 返回创建的JWT
	 */
	default String create(String jwtId, Map<String, Object> claims, Date issuedAt) {
		return create(jwtId, claims, issuedAt, DEFAULT_EXPIRES_AT);
	}

	/**
	 * 创建JWT（永久不过期）
	 *
	 * @param jwtId       JWT的ID
	 * @param claims      私有声明
	 * @param expiredTime 过期时间，单位：秒
	 * @return jwt 返回创建的JWT
	 */
	default String create(String jwtId, Map<String, Object> claims, int expiredTime) {
		return create(jwtId, claims, new Date(), expiredTime);
	}

	/**
	 * 创建JWT（永久不过期）
	 *
	 * @param jwtId  JWT的ID
	 * @param claims 私有声明
	 * @return jwt 返回创建的JWT
	 */
	default String create(String jwtId, Map<String, Object> claims) {
		return create(jwtId, claims, new Date(), DEFAULT_EXPIRES_AT);
	}

	//endregion

	//region 解析JWT

	/**
	 * 解析JWT
	 *
	 * @param jwtStr JWT串
	 * @param leeway 容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
	 * @return jwtInfo 返回解析出的JWT信息
	 */
	JwtInfo verify(String jwtStr, long leeway);

	/**
	 * 解析JWT，默认容忍空间为2秒
	 *
	 * @param jwtStr JWT串
	 * @return jwt
	 */
	default JwtInfo verify(String jwtStr) {
		return verify(jwtStr, 2000);
	}

	/**
	 * 解析JWT
	 *
	 * @param jwtStr             JWT串
	 * @param leeway             容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
	 * @param currentExpiredTime 当前的过期时间，单位：秒。（作用是怕之前设置的时间过长，导致生成的JWT一直不会过期，使系统存在安全隐患。）
	 * @return jwt
	 */
	default JwtInfo verify(String jwtStr, long leeway, int currentExpiredTime) {
		JwtInfo jwtInfo = verify(jwtStr, leeway);

		// 使用新的过期时间重新校验一次时间
		if (currentExpiredTime > 0) {
			Date issuedAt = jwtInfo.getIssuedAt();
			if (issuedAt.getTime() + currentExpiredTime * 1000 <= System.currentTimeMillis()) {
				throw new RuntimeException("JWT已过期");
			}
		}

		return jwtInfo;
	}

	/**
	 * 移除特殊声明信息
	 *
	 * @param claims 声明
	 */
	static void removeSpecialClaims(Map<String, Object> claims) {
		for (String key : JWT_SPECIAL_CLAIMS_KEYS) {
			claims.remove(key);
		}
	}

	//endregion
}
