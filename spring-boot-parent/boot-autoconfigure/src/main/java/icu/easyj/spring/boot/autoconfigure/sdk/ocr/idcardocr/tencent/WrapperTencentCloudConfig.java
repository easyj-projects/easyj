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

import com.tencentcloudapi.common.profile.Language;
import icu.easyj.sdk.ocr.tencent.cloud.TencentCloudConfig;
import icu.easyj.sdk.tencent.cloud.core.TencentCloudSecretConfig;
import org.springframework.util.Assert;

/**
 * @author wangliang181230
 */
public class WrapperTencentCloudConfig extends TencentCloudConfig {

	private final TencentCloudSecretConfig secretConfig;
	private final TencentCloudIdCardOcrProperties idCardOcrProperties;

	public WrapperTencentCloudConfig(TencentCloudSecretConfig secretConfig,
									 TencentCloudIdCardOcrProperties idCardOcrProperties) {
		Assert.notNull(secretConfig, "'secretConfig' must be not null");
		Assert.notNull(idCardOcrProperties, "'idCardOcrProperties' must be not null");

		this.secretConfig = secretConfig;
		this.idCardOcrProperties = idCardOcrProperties;
	}

	@Override
	public String getSecretId() {
		return secretConfig.getSecretId();
	}

	@Override
	public void setSecretId(String secretId) {
		secretConfig.setSecretId(secretId);
	}

	@Override
	public String getSecretKey() {
		return secretConfig.getSecretKey();
	}

	@Override
	public void setSecretKey(String secretKey) {
		secretConfig.setSecretKey(secretKey);
	}

	@Override
	public String getRegion() {
		return idCardOcrProperties.getRegion();
	}

	@Override
	public void setRegion(String region) {
		idCardOcrProperties.setRegion(region);
	}

	@Override
	public Integer getConnTimeout() {
		return idCardOcrProperties.getConnTimeout();
	}

	@Override
	public void setConnTimeout(Integer connTimeout) {
		idCardOcrProperties.setConnTimeout(connTimeout);
	}

	@Override
	public Integer getWriteTimeout() {
		return idCardOcrProperties.getWriteTimeout();
	}

	@Override
	public void setWriteTimeout(Integer writeTimeout) {
		idCardOcrProperties.setWriteTimeout(writeTimeout);
	}

	@Override
	public Integer getReadTimeout() {
		return idCardOcrProperties.getReadTimeout();
	}

	@Override
	public void setReadTimeout(Integer readTimeout) {
		idCardOcrProperties.setReadTimeout(readTimeout);
	}

	@Override
	public Language getLanguage() {
		return idCardOcrProperties.getLanguage();
	}

	@Override
	public void setLanguage(Language language) {
		idCardOcrProperties.setLanguage(language);
	}

	@Override
	public Boolean getDebug() {
		return idCardOcrProperties.getDebug();
	}

	@Override
	public void setDebug(Boolean debug) {
		idCardOcrProperties.setDebug(debug);
	}
}
