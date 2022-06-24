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
package icu.easyj.web.cors;

import java.lang.reflect.InvocationTargetException;

import icu.easyj.core.util.ArrayUtils;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域请求过滤器
 *
 * @author wangliang181230
 * @since 0.6.5
 */
public class CorsWebMvcConfigurer implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CorsWebMvcConfigurer.class);


	private final CorsProperties properties;


	public CorsWebMvcConfigurer(CorsProperties properties) {
		this.properties = properties;
	}

	/**
	 * 允许跨域请求
	 *
	 * @param registry 跨域注册器
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if (!properties.isEnabled() || StringUtils.isBlank(properties.getMapping())) {
			return;
		}

		CorsRegistration registration = registry.addMapping(properties.getMapping());

		if (properties.getAllowCredentials() != null) {
			registration.allowCredentials(properties.getAllowCredentials());
		}

		if (ArrayUtils.isNotEmpty(properties.getAllowedOrigins())) {
			registration.allowedOrigins(properties.getAllowedOrigins());
		}
		if (ArrayUtils.isNotEmpty(properties.getAllowedOriginPatterns())) {
			try {
				ReflectionUtils.invokeMethod(registration, "allowedOriginPatterns", new Class[]{String[].class}, properties.getAllowedOriginPatterns());
			} catch (NoSuchMethodException e) {
				LOGGER.warn("Spring版本太低，方法 'CorsRegistration.allowedOriginPatterns(String[])' 不存在，allowedOriginPatterns配置无效。");
			} catch (InvocationTargetException e) {
				throw new RuntimeException("调用 'allowedOriginPatterns' 方法失败", e);
			}
		}

		if (ArrayUtils.isNotEmpty(properties.getAllowedMethods())) {
			registration.allowedMethods(properties.getAllowedMethods());
		}
		if (ArrayUtils.isNotEmpty(properties.getAllowedHeaders())) {
			registration.allowedMethods(properties.getAllowedHeaders());
		}

		if (properties.getMaxAge() > 0) {
			registration.maxAge(properties.getMaxAge());
		}
	}
}
