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
package icu.easyj.jwt.impls;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.MapUtils;
import icu.easyj.jwt.IJwt;
import icu.easyj.jwt.JwtInfo;
import icu.easyj.jwt.JwtUtils;
import icu.easyj.jwt.SecretKeyUtils;
import org.springframework.util.Assert;

/**
 * 基于jjwt实现的JWT生成器
 *
 * @author wangliang181230
 */
@LoadLevel(name = "hutool", order = 100)
public class HutoolJwt implements IJwt {

	private final JWTSigner signer;


	public HutoolJwt(JWTSigner signer) {
		Assert.notNull(signer, "'signer' must be not null");
		this.signer = signer;
	}

	public HutoolJwt(String algorithmId, Key secretKey) {
		Assert.notNull(algorithmId, "'algorithmId' must be not null");
		Assert.notNull(secretKey, "'secretKey' must be not null");
		this.signer = JWTSignerUtil.createSigner(algorithmId, secretKey);
	}

	public HutoolJwt(String algorithmId, String secretKeyStr, String secretKeyAlgorithm) {
		Assert.notNull(algorithmId, "'algorithmId' must be not null");
		Assert.notNull(secretKeyStr, "'secretKeyStr' must be not null");
		Assert.notNull(secretKeyAlgorithm, "'secretKeyAlgorithm' must be not null");

		Key secretKey = SecretKeyUtils.generate(secretKeyStr, secretKeyAlgorithm);
		this.signer = JWTSignerUtil.createSigner(algorithmId, secretKey);
	}


	@Override
	public String create(String jwtId, Map<String, Object> claims, Date issuedAt, Date expiresAt) {
		JWT jwt = JWT.create();

		// 设置ID
		if (jwtId != null) {
			jwt.setJWTId(jwtId);
		}

		// 设置私有声明
		if (MapUtils.isNotEmpty(claims)) {
			jwt.addPayloads(claims);
		}

		// 设置签发时间
		if (issuedAt == null) {
			issuedAt = new Date();
		}
		jwt.setIssuedAt(issuedAt);

		// 设置过期时间
		if (expiresAt != null) {
			if (expiresAt.compareTo(issuedAt) <= 0) {
				throw new IllegalArgumentException("过期时间 必须大于等于 签发时间：");
			}
			jwt.setExpiresAt(expiresAt);
		}

		// 返回JWT
		return jwt.sign(this.signer);
	}

	@Override
	public JwtInfo parse(String jwtStr, long leeway) {
		JWT jwt = JWT.create().parse(jwtStr);
		jwt.setSigner(this.signer);
		if (!jwt.verify()) {
			throw new RuntimeException("JWT签名无效");
		}

		JWTValidator.of(jwt).validateDate(DateUtil.date(), leeway);

		JSONObject claims = jwt.getPayloads();
		String jwtId = claims.get("jti", String.class);
		Date issuedAt = new Date(claims.getDate("iat").getTime());
		Date expiresAt = new Date(claims.getDate("exp").getTime());

		// 移除特殊声明信息
		JwtUtils.removeSpecialClaims(claims);

		return new JwtInfo(jwtId, claims, issuedAt, expiresAt);
	}
}
