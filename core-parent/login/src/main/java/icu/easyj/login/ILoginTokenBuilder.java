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

import java.util.Date;
import java.util.Map;

import org.springframework.lang.NonNull;

/**
 * 登录相关配置的接口
 *
 * @author wangliang181230
 */
public interface ILoginTokenBuilder {

	/**
	 * 创建token
	 *
	 * @param loginId   当前登录唯一标识
	 * @param loginData 登录数据
	 * @param loginTime 登录时间
	 * @return tokenInfo 返回新的token信息
	 */
	@NonNull
	LoginTokenInfo create(String loginId, Map<String, Object> loginData, Date loginTime);

	/**
	 * 解析token
	 *
	 * @param token token
	 * @return jwtInfo 返回解析出的JWT信息
	 */
	@NonNull
	LoginInfo<?, ?> parse(String token);

	/**
	 * 刷新token
	 *
	 * @param token        token
	 * @param refreshToken 刷新token
	 * @return tokenInfo 返回新的token信息
	 */
	@NonNull
	LoginTokenInfo refresh(String token, String refreshToken);
}
