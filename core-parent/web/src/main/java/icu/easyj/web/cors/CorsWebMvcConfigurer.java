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

import icu.easyj.core.util.ArrayUtils;
import icu.easyj.core.util.StringUtils;
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
			registration.allowedOriginPatterns(properties.getAllowedOriginPatterns());
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
