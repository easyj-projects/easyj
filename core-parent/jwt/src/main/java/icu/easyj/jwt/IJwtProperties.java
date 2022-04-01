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

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT相关配置的接口
 *
 * @author wangliang181230
 */
public interface IJwtProperties {

	/**
	 * 获取token参数名
	 *
	 * @return token参数名
	 */
	SignatureAlgorithm getSignatureAlgorithm();

	void setTokenName(String tokenName);

	/**
	 * 是否允许cookie传入token
	 *
	 * @return true=允许 | false=不允许
	 */
	boolean isAllowCookie();

	void setAllowCookie(boolean allowCookie);

	/**
	 * 是否允许头信息传入token
	 *
	 * @return true=允许 | false=不允许
	 */
	boolean isAllowHeader();

	void setAllowHeader(boolean allowHeader);

	/**
	 * 是否允许GET参数传入token
	 *
	 * @return true=允许 | false=不允许
	 */
	boolean isAllowQueryString();

	void setAllowQueryString(boolean allowQueryString);

	/**
	 * GET参数传入token时，使用的参数名.
	 *
	 * @return tokenNameForQueryString
	 */
	String getTokenNameForQueryString();

	void setTokenNameForQueryString(String tokenNameForQueryString);

	/**
	 * @return
	 */
	int getTokenExpiredTime();

	void setTokenExpiredTime(int tokenExpiredTime);

	/**
	 * RefreshToken过期时间，单位: 秒.
	 *
	 * @return RefreshToken过期时间
	 */
	int getRefreshTokenExpiredTime();

	void setRefreshTokenExpiredTime(int refreshTokenExpiredTime);
}
