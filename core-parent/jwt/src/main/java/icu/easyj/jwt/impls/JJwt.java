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
package icu.easyj.jwt.impls;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.MapUtils;
import icu.easyj.jwt.IJwt;
import icu.easyj.jwt.JwtInfo;
import icu.easyj.jwt.JwtUtils;
import icu.easyj.jwt.SecretKeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.Assert;

/**
 * 基于jjwt实现的JWT生成器
 *
 * @author wangliang181230
 */
@LoadLevel(name = "jjwt", order = 0)
public class JJwt implements IJwt {


	private final SignatureAlgorithm signatureAlgorithm;
	private final Key secretKey;


	public JJwt(SignatureAlgorithm signatureAlgorithm, Key secretKey) {
		Assert.notNull(signatureAlgorithm, "'signatureAlgorithm' must be not null");
		Assert.notNull(secretKey, "'secretKey' must be not null");

		this.signatureAlgorithm = signatureAlgorithm;
		this.secretKey = secretKey;
	}

	public JJwt(SignatureAlgorithm signatureAlgorithm, String secretKeyStr, String secretKeyAlgorithm) {
		Assert.notNull(signatureAlgorithm, "'signatureAlgorithm' must be not null");
		Assert.notNull(secretKeyStr, "'secretKeyStr' must be not null");
		Assert.notNull(secretKeyAlgorithm, "'secretKeyAlgorithm' must be not null");

		this.signatureAlgorithm = signatureAlgorithm;
		this.secretKey = SecretKeyUtils.generate(secretKeyStr, secretKeyAlgorithm);
	}

	public JJwt(String signatureAlgorithmStr, String secretKeyStr, String secretKeyAlgorithm) {
		Assert.notNull(signatureAlgorithmStr, "'signatureAlgorithmStr' must be not null");
		Assert.notNull(secretKeyStr, "'secretKeyStr' must be not null");
		Assert.notNull(secretKeyAlgorithm, "'secretKeyAlgorithm' must be not null");

		this.signatureAlgorithm = SignatureAlgorithm.valueOf(signatureAlgorithmStr);
		this.secretKey = SecretKeyUtils.generate(secretKeyStr, secretKeyAlgorithm);
	}


	@Override
	public String create(String jwtId, Map<String, Object> claims, Date issuedAt, Date expiresAt) {
		JwtBuilder builder = Jwts.builder();


		// 设置ID
		if (jwtId != null) {
			builder.setId(jwtId);
		}

		// 设置私有声明
		if (MapUtils.isNotEmpty(claims)) {
			builder.addClaims(claims);
		}

		// 设置签发时间
		if (issuedAt == null) {
			issuedAt = new Date();
		}
		builder.setIssuedAt(issuedAt);

		// 设置过期时间
		if (expiresAt != null) {
			builder.setExpiration(expiresAt);
		}


		// 设置签名参数
		builder.signWith(this.signatureAlgorithm, this.secretKey);

		// 返回JWT
		return builder.compact();
	}

	@Override
	public JwtInfo parse(String jwtStr, long leeway) {
		Assert.notNull(jwtStr, "'jwtStr' must be not null");

		Claims claims = Jwts.parser()                                    // 得到DefaultJwtParser
				.setAllowedClockSkewSeconds(leeway >= 0 ? leeway : 0)    // 允许的时钟偏差秒数
				.setSigningKey(this.secretKey)                           // 设置签名的秘钥
				//.setClock(this.clock)                                  // 设置时钟
				.parseClaimsJws(jwtStr)                                  // 解析AccessToken
				.getBody();                                              // 获取声明信息

		String jwtId = claims.getId();
		Date issuedAt = claims.getIssuedAt();
		Date expiresAt = claims.getExpiration();

		// 移除特殊声明信息
		JwtUtils.removeSpecialClaims(claims);

		return new JwtInfo(jwtId, claims, issuedAt, expiresAt);
	}
}
