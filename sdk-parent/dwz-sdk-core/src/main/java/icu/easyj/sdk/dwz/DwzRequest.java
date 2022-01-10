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
package icu.easyj.sdk.dwz;

import java.util.Map;

import org.springframework.lang.Nullable;

/**
 * 短链接服务 请求参数
 *
 * @author wangliang181230
 */
public class DwzRequest {

	/**
	 * 长链接
	 */
	private String longUrl;

	/**
	 * 可配置参数（主要为了考虑多种实现）
	 */
	private Map<String, Object> configs;


	//region Constructor

	public DwzRequest() {
	}

	public DwzRequest(String longUrl) {
		this.longUrl = longUrl;
	}

	public DwzRequest(String longUrl, Map<String, Object> configs) {
		this.longUrl = longUrl;
		this.configs = configs;
	}

	//endregion


	//region Getter、Setter

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public Map<String, Object> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, Object> configs) {
		this.configs = configs;
	}

	@Nullable
	public <T> T getConfig(String key) {
		if (configs != null) {
			return (T)configs.get(key);
		} else {
			return null;
		}
	}

	//endregion
}
