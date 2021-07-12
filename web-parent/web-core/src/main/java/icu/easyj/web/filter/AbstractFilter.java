/*
 * Copyright 2021 the original author or authors.
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
package icu.easyj.web.filter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import icu.easyj.web.util.HttpUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import static icu.easyj.web.constant.FilterConstants.DEFAULT_EXCLUSIONS;
import static icu.easyj.web.constant.FilterConstants.EXCLUSIONS_PARAMETER_NAME;

/**
 * Filter类的基类
 * <p>
 * 封装如下功能：
 * <ol>
 *     <li>1. exclusions 不需要拦截的地址</li>
 * </ol>
 *
 * @author wangliang181230
 */
public abstract class AbstractFilter implements Filter {

	//region Fields

	/**
	 * 过滤器名称
	 */
	protected FilterConfig filterConfig;

	/**
	 * 站点二级目录
	 */
	protected String contextPath;

	/**
	 * 需排除的请求地址
	 */
	protected Map<String, List<String>> exclusions;

	/**
	 * 匹配结果缓存
	 */
	protected final Map<String, Boolean> needDoFilterCaches = new ConcurrentHashMap<>();

	//endregion


	//region Override

	/**
	 * 初始化过滤器的方法
	 *
	 * @param filterConfig 过滤器配置
	 * @throws ServletException Servlet异常
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化过滤器配置
		this.filterConfig = filterConfig;

		// 初始化二级目录
		this.contextPath = HttpUtils.getContextPath(filterConfig.getServletContext());

		// 初始化需要排除的请求地址
		String exclusionsStr = filterConfig.getInitParameter(EXCLUSIONS_PARAMETER_NAME);
		if (StringUtils.hasText(exclusionsStr)) {
			exclusionsStr = exclusionsStr.trim() + "," + DEFAULT_EXCLUSIONS;
		} else {
			exclusionsStr = DEFAULT_EXCLUSIONS;
		}
		this.exclusions = FilterExclusion.convert(exclusionsStr.split(","));
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
		this.contextPath = null;
		if (this.exclusions != null) {
			this.exclusions.clear();
			this.exclusions = null;
		}
		this.needDoFilterCaches.clear();
	}

	//endregion


	//region Protected

	/**
	 * 判断`当前请求`是否需要执行`当前过滤器`
	 *
	 * @param request 当前请求
	 * @return isNeedDoFilter true=需要，false=不需要
	 */
	protected boolean isNeedDoFilter(HttpServletRequest request) {
		// 跳过OPTIONS请求，不执行当前过滤器
		if (HttpUtils.isOptionsRequest(request)) {
			return false;
		}

		if (CollectionUtils.isEmpty(exclusions)) {
			// 未配置需排除请求，继续执行过滤器
			return true;
		}

		// 请求方法
		String method = request.getMethod();
		// 将请求路径移除掉contextPath
		String uri = HttpUtils.getNoContextPathUri(request.getRequestURI(), contextPath);

		// 先从缓存中获取判断结果
		String cacheKey = method + ":" + uri;
		Boolean isNeedDoFilter = needDoFilterCaches.get(cacheKey);
		if (isNeedDoFilter != null) {
			// 存在缓存，直接返回结果
			return isNeedDoFilter;
		}

		try {
			String[] methods = new String[]{method, "*"};
			List<String> patterns;
			for (String m : methods) {
				patterns = exclusions.get(m);
				if (!CollectionUtils.isEmpty(patterns)) {
					// 在排除列表中匹配，如果匹配到了，说明需要排除监控
					for (String pattern : patterns) {
						if (PatternMatchUtils.simpleMatch(pattern, uri)) {
							// 匹配到了，不执行当前过滤器
							isNeedDoFilter = false;
							return false;
						}
					}
				}
			}

			// 未匹配到，执行当前过滤器
			isNeedDoFilter = true;
			return true;
		} finally {
			if (isNeedDoFilter != null) {
				needDoFilterCaches.put(cacheKey, isNeedDoFilter);
			}
		}
	}

	//endregion


	//region Getter

	/**
	 * @return 过滤器名称
	 */
	@Nullable
	public String getFilterName() {
		return filterConfig != null ? filterConfig.getFilterName() : null;
	}

	/**
	 * @return 过滤器配置
	 */
	@Nullable
	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	//endregion
}