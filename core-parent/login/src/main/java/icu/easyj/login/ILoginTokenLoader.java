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

import javax.servlet.http.HttpServletRequest;

import org.springframework.lang.Nullable;

/**
 * 登录token加载器
 *
 * @author wangliang181230
 */
public interface ILoginTokenLoader {

	/**
	 * 从请求中加载登录token
	 *
	 * @param request 请求
	 * @return token 返回登录token
	 */
	@Nullable
	String load(HttpServletRequest request, ILoginProperties properties);

	/**
	 * 是否允许
	 *
	 * @param properties 登录配置
	 * @return 是否允许
	 */
	boolean isAllow(ILoginProperties properties);
}
