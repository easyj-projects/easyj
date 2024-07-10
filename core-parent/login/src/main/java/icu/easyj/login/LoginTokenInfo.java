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
package icu.easyj.login;

import java.io.Serializable;

/**
 * 登录Token信息
 *
 * @author wangliang181230
 */
public class LoginTokenInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 登录凭证
	 */
	private String token;

	/**
	 * 刷新凭证
	 */
	private String refreshToken;


	public LoginTokenInfo() {
	}

	public LoginTokenInfo(String token, String refreshToken) {
		this.token = token;
		this.refreshToken = refreshToken;
	}


	//region Getter、Setter

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	//endregion
}
