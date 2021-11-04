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
package icu.easyj.spring.boot.util;

import javax.servlet.Filter;

import icu.easyj.core.util.CollectionUtils;
import icu.easyj.web.exception.FilterDisabledException;
import icu.easyj.web.filter.IFilterProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;

/**
 * 过滤器注册工具类
 *
 * @author wangliang181230
 */
public abstract class FilterRegistrationUtils {

	/**
	 * 根据配置进行注册信息补充
	 *
	 * @param filter       过滤器
	 * @param properties   过滤器配置
	 * @param defaultOrder 默认的执行顺序
	 * @param <F>          过滤器类型
	 * @param <P>          配置类型
	 * @return registration 过滤器注册器
	 */
	public static <F extends Filter, P extends IFilterProperties> FilterRegistrationBean<F> register(F filter, P properties, Integer defaultOrder) {
		if (!properties.isEnabled()) {
			throw new FilterDisabledException("当前过滤器已禁用，无法继续注册");
		}

		// 过滤器注册器
		FilterRegistrationBean<F> registration = new FilterRegistrationBean<>(filter);

		// 设置过滤器 名称
		registration.setName(filter.getClass().getSimpleName());

		// 设置过滤器 执行顺序
		if (properties.getOrder() == null) {
			properties.setOrder(defaultOrder != null ? defaultOrder : Ordered.LOWEST_PRECEDENCE);
		}
		registration.setOrder(properties.getOrder());

		// 添加当前过滤器需要过滤的地址列表
		String[] urlPatterns;
		if (CollectionUtils.isEmpty(properties.getUrlPatterns())) {
			urlPatterns = new String[]{"/*"};
		} else {
			urlPatterns = properties.getUrlPatterns().toArray(ArrayUtils.EMPTY_STRING_ARRAY);
		}
		registration.addUrlPatterns(urlPatterns);

		return registration;
	}
}
