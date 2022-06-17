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

/**
 * 跨域请求相关配置
 *
 * @author wangliang181230
 * @since 0.6.5
 */
public class CorsProperties {

	/**
	 * 是否启用跨域功能.
	 */
	private boolean enabled;

	/**
	 * 地址匹配，默认匹配所有.
	 */
	private String mapping;

	/**
	 * 是否允许携带凭证.
	 */
	private Boolean allowCredentials;

	/**
	 * 允许的请求来源.
	 */
	private String[] allowedOrigins;

	/**
	 * 允许的请求来源匹配串.
	 */
	private String[] allowedOriginPatterns;

	/**
	 * 允许的请求类型，默认：GET、POST（spring-boot中默认值多一个HEAD）.
	 */
	private String[] allowedMethods = {"GET", "POST"};

	/**
	 * 允许的头信息.
	 */
	private String[] allowedHeaders;

	/**
	 * OPTIONS请求缓存时间，小于等于0时，不设置该响应头
	 */
	private long maxAge = 1800;


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public Boolean getAllowCredentials() {
		return allowCredentials;
	}

	public void setAllowCredentials(Boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
	}

	public String[] getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(String[] allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public String[] getAllowedOriginPatterns() {
		return allowedOriginPatterns;
	}

	public void setAllowedOriginPatterns(String[] allowedOriginPatterns) {
		this.allowedOriginPatterns = allowedOriginPatterns;
	}

	public String[] getAllowedMethods() {
		return allowedMethods;
	}

	public void setAllowedMethods(String[] allowedMethods) {
		this.allowedMethods = allowedMethods;
	}

	public String[] getAllowedHeaders() {
		return allowedHeaders;
	}

	public void setAllowedHeaders(String[] allowedHeaders) {
		this.allowedHeaders = allowedHeaders;
	}

	public long getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(long maxAge) {
		this.maxAge = maxAge;
	}
}
