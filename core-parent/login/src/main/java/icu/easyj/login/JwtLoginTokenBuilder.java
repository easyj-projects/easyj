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
package icu.easyj.login;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import icu.easyj.core.util.StringUtils;
import icu.easyj.jwt.IJwt;
import icu.easyj.jwt.JwtInfo;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 基于JWT构建登录token的构建器
 *
 * @author wangliang181230
 */
public class JwtLoginTokenBuilder implements ILoginTokenBuilder {

	private final IJwt jwt;

	private final ILoginProperties properties;


	public JwtLoginTokenBuilder(IJwt jwt, ILoginProperties properties) {
		Assert.notNull(jwt, "'jwt' must be not null");
		Assert.notNull(properties, "'properties' must be not null");
		this.jwt = jwt;
		this.properties = properties;
	}


	@NonNull
	@Override
	public LoginTokenInfo create(String loginId, Map<String, Object> loginData, Date loginTime) {
		String token = jwt.create(loginId, loginData, loginTime, properties.getTokenExpiredTime());
		String refreshToken = jwt.create(loginId, null, new Date(), properties.getRefreshTokenExpiredTime());
		return new LoginTokenInfo(token, refreshToken);
	}

	@NonNull
	@Override
	public LoginInfo<?, ?> parse(String token) {
		JwtInfo jwtInfo = jwt.parse(token);

		LoginInfo<?, ?> loginInfo = new LoginInfo<>();
		loginInfo.setToken(token);
		loginInfo.setLoginId(jwtInfo.getJwtId());
		loginInfo.setAccountId(jwtInfo.getClaims("accountId", Serializable.class));
		loginInfo.setUserId(jwtInfo.getClaims("userId", Serializable.class));
		loginInfo.setUserName(jwtInfo.getClaims("userName", String.class));
		loginInfo.setLoginClient(jwtInfo.getClaims("loginClient", String.class));
		loginInfo.setMainRole(jwtInfo.getClaims("mainRole", String.class));
		loginInfo.setRoles(this.getRoles(jwtInfo.getClaims("roles", String.class)));
		loginInfo.setLoginTime(jwtInfo.getClaims("loginTime", Date.class));
		if (loginInfo.getLoginTime() == null) {
			loginInfo.setLoginTime(jwtInfo.getIssuedAt());
		}
		loginInfo.setLoginExpiredTime(jwtInfo.getExpiresAt());
		loginInfo.setData(jwtInfo.getClaims());

		return loginInfo;
	}

	@NonNull
	@Override
	public LoginTokenInfo refresh(String token, String refreshToken) {
		JwtInfo jwtInfo = jwt.parse(token);
		JwtInfo refreshJwtInfo = jwt.parse(refreshToken);

		if (!jwtInfo.getJwtId().equals(refreshJwtInfo.getJwtId())) {
			throw new IllegalArgumentException("当前refreshToken与token不匹配");
		}

		// 从token生成到现在，经过的秒数。生成新的token和refreshToken时，增加到过期时间中
		int passSeconds = (int)(System.currentTimeMillis() - jwtInfo.getIssuedAt().getTime()) / 1000;

		// 生成新的token和refreshToken
		String newToken = jwt.create(jwtInfo.getJwtId(), jwtInfo.getClaims(), jwtInfo.getIssuedAt(), passSeconds + properties.getTokenExpiredTime());
		String newRefreshToken = jwt.create(jwtInfo.getJwtId(), null, refreshJwtInfo.getIssuedAt(), passSeconds + properties.getRefreshTokenExpiredTime());
		return new LoginTokenInfo(newToken, newRefreshToken);
	}


	@Nullable
	private List<String> getRoles(String rolesStr) {
		if (StringUtils.isEmpty(rolesStr)) {
			return null;
		}

		String[] roles = rolesStr.split(",");
		return Arrays.asList(roles);
	}
}
