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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.StrPool;
import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.MapUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.web.util.HttpConfigs;
import icu.easyj.web.util.HttpUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;

import static icu.easyj.web.constant.FilterConstants.GLOBAL_EXCLUSIONS;

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
public abstract class AbstractFilter<P extends IFilterProperties> implements Filter {

	/**
	 * 全局过滤器需排除列表
	 */
	private static final List<String> GLOBAL_EXCLUSIONS_LIST = ListUtil.toList(GLOBAL_EXCLUSIONS.split(StrPool.COMMA));


	//region Fields

	/**
	 * 过滤器配置
	 */
	protected final P filterProperties;

	/**
	 * 过滤器名称
	 */
	protected String filterName;

	/**
	 * 需排除的请求地址
	 */
	protected final Map<String, List<String>> exclusions;

	/**
	 * 匹配结果缓存
	 */
	protected final Map<String, Boolean> needDoFilterCaches = new ConcurrentHashMap<>();

	//endregion


	//region Constructor

	/**
	 * 构造函数
	 *
	 * @param filterProperties 过滤器配置
	 */
	protected AbstractFilter(@NonNull P filterProperties) {
		Assert.notNull(filterProperties, "'filterProperties' must not be null");

		// 设置过滤器配置
		this.filterProperties = filterProperties;

		// 初始化需要排除的请求地址
		List<String> exclusionsList = this.filterProperties.getExclusions();
		if (exclusionsList == null) {
			exclusionsList = new ArrayList<>(GLOBAL_EXCLUSIONS_LIST);
		} else {
			exclusionsList.addAll(GLOBAL_EXCLUSIONS_LIST);
		}
		this.exclusions = FilterExclusion.convert(exclusionsList);
	}

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
		// 初始化过滤器名称
		String filterName = filterConfig.getFilterName();
		if (StringUtils.isBlank(filterName)) {
			filterName = this.getClass().getSimpleName();
		}
		this.filterName = filterName;
	}

	@Override
	public void destroy() {
		if (this.exclusions != null) {
			this.exclusions.clear();
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
	public boolean isNeedDoFilter(HttpServletRequest request) {
		// 过滤器已禁用，不执行当前过滤器
		if (this.filterProperties.isDisabled()) {
			return false;
		}

		// 跳过OPTIONS请求，不执行当前过滤器
		if (HttpUtils.isOptionsRequest(request)) {
			return false;
		}

		if (MapUtils.isEmpty(exclusions)) {
			// 未配置需排除请求，继续执行过滤器
			return true;
		}

		// 请求方法
		String method = request.getMethod();
		// 将请求路径移除掉contextPath
		String uri = HttpUtils.getNoContextPathUri(request.getRequestURI(), HttpConfigs.getContextPath());

		// 缓存键
		String cacheKey = method + ":" + uri;
		// 如果缓存存在，直接返回，否则执行校验
		return MapUtils.computeIfAbsent(needDoFilterCaches, cacheKey, key -> {
			String[] methods = new String[]{method, "*"};
			List<String> patterns;
			for (String m : methods) {
				patterns = exclusions.get(m);
				if (!CollectionUtils.isEmpty(patterns)) {
					// 在排除列表中匹配，如果匹配到了，说明需要排除监控
					for (String pattern : patterns) {
						if (PatternMatchUtils.simpleMatch(pattern, uri)) {
							// 匹配到了，不执行当前过滤器
							return false;
						}
					}
				}
			}

			// 设为null，方便GC回收
			methods = null;
			patterns = null;

			// 未匹配到，执行当前过滤器
			return true;
		});
	}

	//endregion


	//region Getter

	/**
	 * 获取过滤器配置
	 *
	 * @return 过滤器配置
	 */
	public P getFilterProperties() {
		return filterProperties;
	}

	/**
	 * 获取过滤器名称
	 *
	 * @return 过滤器名称
	 */
	@Nullable
	public String getFilterName() {
		return filterName;
	}

	//endregion
}
