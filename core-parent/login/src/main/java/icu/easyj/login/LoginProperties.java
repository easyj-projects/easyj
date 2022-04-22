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
 * 登录相关配置
 *
 * @author wangliang181230
 */
public class LoginProperties implements ILoginProperties {

	/**
	 * 是否启用登录校验功能.
	 */
	private boolean enabled = false;


	//region 三种 token获取渠道及键名 相关的属性

	/**
	 * 是否允许头信息传入token.
	 */
	private boolean allowHeader = true;

	/**
	 * token的头信息键名.
	 */
	private String tokenHeaderName = "accessToken";

	/**
	 * 是否允许cookie传入token.
	 */
	private boolean allowCookie = true;

	/**
	 * token的Cookie键名.
	 */
	private String tokenCookieName = "accessToken";

	/**
	 * 是否允许参数传入token.
	 */
	private boolean allowParam = true;

	/**
	 * 获取token的参数名.
	 */
	private String tokenParamName = "accessToken";

	//endregion

	//region token过期相关属性

	/**
	 * token类型
	 */
	private String tokenType = "JWT";

	/**
	 * token过期时间，单位: 秒.<br>
	 * 说明：如果该值小于等于0，则表示永久有效.
	 */
	private int tokenExpiredTime = 3600; // 默认：1小时

	/**
	 * refreshToken过期时间，单位: 秒.
	 */
	private int refreshTokenExpiredTime = 7200; // 默认：2小时

	//endregion

	/**
	 * 是否需要追踪登录信息.
	 */
	private boolean needTraceLoginInfo = false;

	/**
	 * 需要追踪的信息，追踪信息为该 {@link LoginInfo} 类的属性名
	 *
	 * @see LoginInfo
	 */
	private String[] traceLoginInfos = new String[]{
			"loginId",
			"accountId",
			"userId"
	};


	//region Getter、Setter

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isAllowHeader() {
		return allowHeader;
	}

	@Override
	public void setAllowHeader(boolean allowHeader) {
		this.allowHeader = allowHeader;
	}

	@Override
	public String getTokenHeaderName() {
		return tokenHeaderName;
	}

	@Override
	public void setTokenHeaderName(String tokenHeaderName) {
		this.tokenHeaderName = tokenHeaderName;
	}

	@Override
	public boolean isAllowCookie() {
		return allowCookie;
	}

	@Override
	public void setAllowCookie(boolean allowCookie) {
		this.allowCookie = allowCookie;
	}

	@Override
	public String getTokenCookieName() {
		return tokenCookieName;
	}

	@Override
	public void setTokenCookieName(String tokenCookieName) {
		this.tokenCookieName = tokenCookieName;
	}

	@Override
	public boolean isAllowParam() {
		return allowParam;
	}

	@Override
	public void setAllowParam(boolean allowParam) {
		this.allowParam = allowParam;
	}

	@Override
	public String getTokenParamName() {
		return tokenParamName;
	}

	@Override
	public void setTokenParamName(String tokenParamName) {
		this.tokenParamName = tokenParamName;
	}

	@Override
	public String getTokenType() {
		return tokenType;
	}

	@Override
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@Override
	public int getTokenExpiredTime() {
		return tokenExpiredTime;
	}

	@Override
	public void setTokenExpiredTime(int tokenExpiredTime) {
		this.tokenExpiredTime = tokenExpiredTime;
	}

	@Override
	public int getRefreshTokenExpiredTime() {
		return refreshTokenExpiredTime;
	}

	@Override
	public void setRefreshTokenExpiredTime(int refreshTokenExpiredTime) {
		this.refreshTokenExpiredTime = refreshTokenExpiredTime;
	}

	@Override
	public boolean isNeedTraceLoginInfo() {
		return needTraceLoginInfo;
	}

	@Override
	public void setNeedTraceLoginInfo(boolean needTraceLoginInfo) {
		this.needTraceLoginInfo = needTraceLoginInfo;
	}

	@Override
	public String[] getTraceLoginInfos() {
		return traceLoginInfos;
	}

	@Override
	public void setTraceLoginInfos(String[] traceLoginInfos) {
		this.traceLoginInfos = traceLoginInfos;
	}

	//endregion
}
