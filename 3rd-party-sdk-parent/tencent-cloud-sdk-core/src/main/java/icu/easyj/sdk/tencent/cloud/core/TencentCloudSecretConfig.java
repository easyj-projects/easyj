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
package icu.easyj.sdk.tencent.cloud.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 腾讯云安全相关配置
 *
 * @author wangliang181230
 * @see <a href="https://console.cloud.tencent.com/cam/capi">腾讯云密钥对管理页面</a>
 */
public class TencentCloudSecretConfig {

	/**
	 * 密钥对中的ID
	 */
	@SerializedName("SecretId")
	@Expose
	protected String secretId;

	/**
	 * 密钥对中的Key
	 */
	@SerializedName("SecretKey")
	@Expose
	protected String secretKey;


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

	//endregion
}
