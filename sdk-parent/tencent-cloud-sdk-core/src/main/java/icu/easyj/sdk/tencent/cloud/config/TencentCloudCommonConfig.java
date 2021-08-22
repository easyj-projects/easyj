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
package icu.easyj.sdk.tencent.cloud.config;

import com.tencentcloudapi.common.profile.Language;

/**
 * 腾讯云通用配置
 *
 * @author wangliang181230
 */
public class TencentCloudCommonConfig {

	//region 密钥对

	/**
	 * 密钥对中的ID
	 *
	 * @see <a href="https://console.cloud.tencent.com/cam/capi">腾讯云密钥对管理页面</a>
	 */
	protected String secretId;

	/**
	 * 密钥对中的Key
	 *
	 * @see <a href="https://console.cloud.tencent.com/cam/capi">腾讯云密钥对管理页面</a>
	 */
	protected String secretKey;

	//endregion


	//region 地域配置

	/**
	 * 地域代码
	 * 注意：不同接口支持的地域不同，如果通用配置的值某个接口不支持，请在接口指定的配置项中进行个性配置。
	 */
	private String region;

	//endregion


	//region 接口相关配置

	/**
	 * Connect timeout in seconds
	 */
	private Integer connTimeout;

	/**
	 * Write timeout in seconds
	 */
	private Integer writeTimeout;

	/**
	 * Read timeout in seconds.
	 */
	private Integer readTimeout;

	/**
	 * 语言，默认：简体中文
	 */
	private Language language;

	/**
	 * 是否调试
	 */
	private Boolean debug;

	//endregion


	//region Getter、Setter

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getConnTimeout() {
		return connTimeout;
	}

	public void setConnTimeout(Integer connTimeout) {
		this.connTimeout = connTimeout;
	}

	public Integer getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(Integer writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	//endregion
}
