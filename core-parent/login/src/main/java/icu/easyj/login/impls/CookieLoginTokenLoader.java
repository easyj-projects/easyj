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
package icu.easyj.login.impls;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.login.ILoginProperties;
import icu.easyj.login.ILoginTokenLoader;
import org.springframework.lang.Nullable;

/**
 * 登录token从Cookie中加载
 *
 * @author wangliang181230
 */
@LoadLevel(name = "cookie", order = 20)
public class CookieLoginTokenLoader implements ILoginTokenLoader {

	@Nullable
	@Override
	public String load(HttpServletRequest request, ILoginProperties properties) {
		Cookie[] cookies = request.getCookies();

		for (Cookie cookie : cookies) {
			if (properties.getTokenCookieName().equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}

	@Override
	public boolean isAllow(ILoginProperties properties) {
		return properties.isAllowCookie();
	}
}
