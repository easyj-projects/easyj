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
package icu.easyj.sdk.baidu.cloud.dwz;

/**
 * 百度的DWZ服务配置
 *
 * @author wangliang181230
 */
public class BaiduDwzConfig {

	/**
	 * 服务地址
	 */
	private String serviceUrl = "https://dwz.cn/api/v3/short-urls";

	/**
	 * 接口token
	 */
	private String token;

	/**
	 * 默认的短网址有效期<br>
	 * 值域：
	 * <ul>
	 *     <li>long-term：长期（有限额）</li>
	 *     <li>1-year：1年</li>
	 * </ul>
	 */
	private String defaultTermOfValidity = "1-year";

	/**
	 * 返回错误语言
	 */
	private String responseLanguage = "zh";


	public BaiduDwzConfig() {
	}

	public BaiduDwzConfig(String token) {
		this.token = token;
	}


	//region Getter、Setter

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDefaultTermOfValidity() {
		return defaultTermOfValidity;
	}

	public void setDefaultTermOfValidity(String defaultTermOfValidity) {
		this.defaultTermOfValidity = defaultTermOfValidity;
	}

	public String getResponseLanguage() {
		return responseLanguage;
	}

	public void setResponseLanguage(String responseLanguage) {
		this.responseLanguage = responseLanguage;
	}

	//endregion
}
