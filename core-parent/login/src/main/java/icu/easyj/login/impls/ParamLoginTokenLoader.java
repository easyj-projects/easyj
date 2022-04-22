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

import javax.servlet.http.HttpServletRequest;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.login.ILoginProperties;
import icu.easyj.login.ILoginTokenLoader;
import org.springframework.lang.Nullable;

/**
 * 登录token从参数中加载
 *
 * @author wangliang181230
 */
@LoadLevel(name = "param", order = 0)
public class ParamLoginTokenLoader implements ILoginTokenLoader {

	@Nullable
	@Override
	public String load(HttpServletRequest request, ILoginProperties properties) {
		return request.getParameter(properties.getTokenParamName());
	}

	@Override
	public boolean isAllow(ILoginProperties properties) {
		return properties.isAllowParam();
	}
}
