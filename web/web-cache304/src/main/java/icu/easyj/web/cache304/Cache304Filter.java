/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.web.cache304;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import icu.easyj.core.exception.SkipCallbackWrapperException;
import icu.easyj.web.cache304.config.Cache304Config;
import icu.easyj.web.cache304.config.Cache304ConfigStorageFactory;
import icu.easyj.web.util.HttpUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Cache304过滤器
 *
 * @author wangliang181230
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Cache304Filter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 直接强转类型
		HttpServletRequest httpRequest = (HttpServletRequest)request;

		// 非GET请求，不使用Cache304
		if (HttpUtils.isNotGetRequest(httpRequest)) {
			chain.doFilter(request, response);
			return;
		}

		// 获取当前请求的配置
		Cache304Config config = Cache304ConfigStorageFactory.getStorage().getConfig(httpRequest);
		if (config != null) {
			Cache304Aspect.disable();
		}

		// 执行Cache304逻辑
		try {
			HttpServletResponse httpResponse = (HttpServletResponse)response;
			Cache304Utils.doCache(httpRequest, httpResponse, config, () -> {
				try {
					chain.doFilter(request, response);
				} catch (IOException | ServletException e) {
					throw new SkipCallbackWrapperException(e);
				}
			});
		} catch (SkipCallbackWrapperException ex) {
			Throwable e = ex.getCause();
			if (e instanceof IOException) {
				throw (IOException)e;
			} else {
				throw (ServletException)e;
			}
		} finally {
			if (config != null) {
				Cache304Aspect.enable();
			}
		}
	}
}
