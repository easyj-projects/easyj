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
package icu.easyj.spring.boot.autoconfigure.util;

import javax.servlet.Filter;

import icu.easyj.core.exception.ConfigurationException;
import icu.easyj.web.filter.IFilterProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

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
	 * @param <P>          配置类型
	 * @return registration 过滤器注册器
	 */
	public static <P extends IFilterProperties> FilterRegistrationBean register(Filter filter, P properties, Integer defaultOrder) {
		if (!properties.isEnabled()) {
			throw new ConfigurationException("当前过滤器已禁用，无法继续注册");
		}

		// 过滤器注册器
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);

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
			urlPatterns = properties.getUrlPatterns().toArray(new String[0]);
		}
		registration.addUrlPatterns(urlPatterns);

		return registration;
	}
}