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

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.trace.TraceUtils;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.web.constant.FilterOrderConstants;
import icu.easyj.web.filter.FilterAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

/**
 * 登录过滤器
 *
 * @author wangliang181230
 */
@Order(FilterOrderConstants.LOGIN)
public class LoginFilter implements FilterAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

	private static final List<ILoginTokenLoader> LOGIN_TOKEN_LOADERS = EnhancedServiceLoader.loadAll(ILoginTokenLoader.class);

	private static final String TRACE_PRE_KEY = "LOGIN_";

	private final ILoginTokenBuilder loginTokenBuilder;
	private final ILoginProperties properties;


	public LoginFilter(ILoginTokenBuilder loginTokenBuilder, ILoginProperties properties) {
		Assert.notNull(loginTokenBuilder, "'loginTokenBuilder' must be not null");
		Assert.notNull(properties, "'properties' must be not null");
		this.loginTokenBuilder = loginTokenBuilder;
		this.properties = properties;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 直接强转类型
		HttpServletRequest httpRequest = (HttpServletRequest)request;

		Set<String> traceKeys = new HashSet<>(properties.getTraceLoginInfos().length);
		try {
			String token;
			for (ILoginTokenLoader loader : LOGIN_TOKEN_LOADERS) {
				token = loader.load(httpRequest, properties);

				// 如果token存在，则设置
				if (StringUtils.isNotBlank(token)) {
					LoginInfo<?, ?> loginInfo = loginTokenBuilder.parse(token);
					LoginUtils.setLoginInfo(loginInfo);

					// 是否需要追踪登录信息
					if (properties.isNeedTraceLoginInfo()) {
						for (String traceLoginInfo : properties.getTraceLoginInfos()) {
							try {
								Object loginInfoFieldValue = ReflectionUtils.getFieldValue(loginInfo, traceLoginInfo);

								String traceKey = TRACE_PRE_KEY + traceLoginInfo;
								traceKeys.add(traceKey);
								TraceUtils.put(traceKey, String.valueOf(loginInfoFieldValue));
							} catch (NoSuchFieldException e) {
								LOGGER.warn("不存在登录信息 '{}'，无法添加追踪信息", traceLoginInfo);
							}
						}
					}

					break;
				}
			}

			// 继续执行
			chain.doFilter(request, response);
		} finally {
			// 清理上下文
			LoginUtils.clear();
			TraceUtils.remove(traceKeys);
		}
	}
}
