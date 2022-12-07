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
package icu.easyj.web.context;

import icu.easyj.core.context.ContextCleanerUtils;
import icu.easyj.web.constant.FilterOrderConstants;
import icu.easyj.web.filter.FilterAdapter;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * 所有上下文过滤器
 *
 * @author wangliang181230
 */
@Order(FilterOrderConstants.CONTEXT_CLEANER)
public class ContextCleanerFilter implements FilterAdapter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} finally {
			ContextCleanerUtils.clear();
		}
	}
}
