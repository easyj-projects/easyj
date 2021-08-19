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
package icu.easyj.spring.boot.autoconfigure.sdk.ocr.idcardocr.tencent;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 腾讯云通用配置
 *
 * @author wangliang181230
 */
@ConfigurationProperties("easyj.sdk.tencent")
public class TencentCloudProperties {

	/**
	 * API密钥对中的ID
	 */
	private String secretId;

	/**
	 * API密钥对中的Key
	 */
	private String secretKey;


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
