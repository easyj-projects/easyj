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

/**
 * 登录相关配置的接口
 *
 * @author wangliang181230
 */
public interface ILoginProperties {

	//region 几种token获取渠道及name

	/**
	 * 是否允许头信息传入token
	 *
	 * @return true=允许 | false=不允许
	 */
	boolean isAllowHeader();

	void setAllowHeader(boolean allowHeader);

	/**
	 * 获取token的头信息键名
	 *
	 * @return 返回token的头信息键名
	 */
	String getTokenHeaderName();

	void setTokenHeaderName(String tokenHeaderName);

	/**
	 * 是否允许cookie传入token
	 *
	 * @return true=允许 | false=不允许
	 */
	boolean isAllowCookie();

	void setAllowCookie(boolean allowCookie);

	/**
	 * 获取token的Cookie键名
	 *
	 * @return 返回token的Cookie键名
	 */
	String getTokenCookieName();

	void setTokenCookieName(String tokenCookieName);

	/**
	 * 是否允许GET参数传入token
	 *
	 * @return true=允许 | false=不允许
	 */
	boolean isAllowParam();

	void setAllowParam(boolean allowParam);

	/**
	 * 获取token的参数名
	 *
	 * @return tokenParamName
	 */
	String getTokenParamName();

	void setTokenParamName(String tokenParamName);

	//endregion

	//region token相关属性

	/**
	 * token类型，如：JWT.
	 *
	 * @return token类型
	 */
	String getTokenType();

	void setTokenType(String tokenType);

	/**
	 * token过期时间，单位: 秒.
	 *
	 * @return token过期时间
	 */
	int getTokenExpiredTime();

	void setTokenExpiredTime(int tokenExpiredTime);

	/**
	 * refreshToken过期时间，单位: 秒.
	 *
	 * @return refreshToken过期时间
	 */
	int getRefreshTokenExpiredTime();

	void setRefreshTokenExpiredTime(int refreshTokenExpiredTime);

	//endregion

	/**
	 * 是否需要追踪登录信息
	 *
	 * @return 返回是否需要追踪登录信息
	 */
	boolean isNeedTraceLoginInfo();

	void setNeedTraceLoginInfo(boolean needTraceLoginInfo);

	/**
	 * 需要追踪的信息
	 *
	 * @return 返回需要追踪的信息
	 */
	String[] getTraceLoginInfos();

	void setTraceLoginInfos(String[] traceLoginInfos);
}
