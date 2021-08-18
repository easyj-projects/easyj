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
package icu.easyj.sdk.tencent.cloud.ocr.impls;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import icu.easyj.sdk.tencent.cloud.ocr.ITencentCloudOcrTemplate;
import icu.easyj.sdk.tencent.cloud.ocr.TencentCloudConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 腾讯云 文字识别（OCR） 服务接口 默认实现
 *
 * @author wangliang181230
 */
public class DefaultTencentCloudOcrTemplate implements ITencentCloudOcrTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTencentCloudOcrTemplate.class);

	private static final String ENDPOINT = "ocr.tencentcloudapi.com";

	/**
	 * 全局配置
	 */
	@Nullable
	private final TencentCloudConfig globalConfig;

	@Nullable
	private final OcrClient globalClient;


	public DefaultTencentCloudOcrTemplate() {
		this.globalConfig = null;
		this.globalClient = null;
	}

	public DefaultTencentCloudOcrTemplate(@NonNull TencentCloudConfig globalConfig) {
		Assert.notNull(globalConfig, "'globalConfig' must be not null");

		this.globalConfig = globalConfig;
		this.globalClient = this.newOcrClient(globalConfig);
	}


	//region Override start

	//region 身份证识别 start

	/**
	 * 身份证识别
	 *
	 * @param request 请求
	 * @param config  请求配置
	 * @return response 响应
	 * @see <a href="https://cloud.tencent.com/document/api/866/33524">API文档</a>
	 * @see <a href="https://console.cloud.tencent.com/api/explorer?Product=ocr&Version=2018-11-19&Action=IDCardOCR">调试页面</a>
	 */
	@Override
	public IDCardOCRResponse doIdCardOcr(IDCardOCRRequest request, @Nullable TencentCloudConfig config) throws TencentCloudSDKException {
		if (config == null) {
			Assert.notNull(this.globalConfig, "'this.globalConfig' must be not null");
			config = this.globalConfig;
		}

		// 合并全局配置
		this.mergeGlobalConfig(config);

		Assert.notNull(config.getSecretId(), "'secretId' must be not null");
		Assert.notNull(config.getSecretKey(), "'secretKey' must be not null");
		Assert.notNull(config.getRegion(), "'region' must be not null");

		long startTime = System.nanoTime();
		try {
			// 实例化要请求产品的client对象,clientProfile是可选的
			OcrClient client;
			if (config == this.globalConfig) {
				Assert.notNull(this.globalClient, "未设置全局配置，没有生成过全局客户端实例。");
				client = this.globalClient;
			} else {
				client = this.newOcrClient(config);
			}

			// 发送请求，并返回一个IDCardOCRResponse的实例，与请求对象对应
			IDCardOCRResponse response = client.IDCardOCR(request);

			// 记录日志
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("IDCardOCR 请求成功！\r\n Request: {}\r\nResponse: {}\r\n  Config: {}\r\n   Cost: {} ms",
						icu.easyj.core.util.StringUtils.toString(request),
						icu.easyj.core.util.StringUtils.toString(response),
						icu.easyj.core.util.StringUtils.toString(config),
						(System.nanoTime() - startTime) / 1000000);
			}

			return response;
		} catch (TencentCloudSDKException | RuntimeException e) {
			LOGGER.error("IDCardOCR 请求失败：{}\r\nRequest: {}\r\n Config: {}\r\n  Cost: {}",
					e,
					icu.easyj.core.util.StringUtils.toString(request),
					icu.easyj.core.util.StringUtils.toString(config),
					(System.nanoTime() - startTime) / 1000000);
			if (e instanceof TencentCloudSDKException) {
				throw (TencentCloudSDKException)e;
			} else {
				throw new RuntimeException("IDCardOCR 请求失败", e);
			}
		}
	}

	//endregion 身份证识别 end

	//endregion Override end


	//region Private

	private void mergeGlobalConfig(@NonNull TencentCloudConfig config) {
		if (config != this.globalConfig && this.globalConfig != null) {
			if (StringUtils.isBlank(config.getSecretId())) {
				config.setSecretId(this.globalConfig.getSecretId());
			}
			if (StringUtils.isBlank(config.getSecretKey())) {
				config.setSecretKey(this.globalConfig.getSecretKey());
			}
			if (StringUtils.isBlank(config.getRegion())) {
				config.setRegion(this.globalConfig.getRegion());
			}
			if (config.getConnTimeout() == null) {
				config.setConnTimeout(this.globalConfig.getConnTimeout());
			}
			if (config.getWriteTimeout() == null) {
				config.setWriteTimeout(this.globalConfig.getWriteTimeout());
			}
			if (config.getReadTimeout() == null) {
				config.setReadTimeout(this.globalConfig.getReadTimeout());
			}
			if (config.getLanguage() == null) {
				config.setLanguage(this.globalConfig.getLanguage());
			}
			if (config.getDebug() == null) {
				config.setDebug(this.globalConfig.getDebug());
			}
		}
	}

	@Nullable
	private Credential newCredential(TencentCloudConfig config) {
		if (StringUtils.isNotBlank(config.getSecretId()) && StringUtils.isNotBlank(config.getSecretKey())) {
			return new Credential(config.getSecretId(), config.getSecretKey());
		} else {
			return null;
		}
	}

	@NonNull
	private HttpProfile newHttpProfile(TencentCloudConfig config) {
		HttpProfile httpProfile = new HttpProfile();
		httpProfile.setEndpoint(ENDPOINT);
		if (config.getConnTimeout() != null && config.getConnTimeout() > 0) {
			httpProfile.setConnTimeout(config.getConnTimeout());
		}
		if (config.getWriteTimeout() != null && config.getWriteTimeout() > 0) {
			httpProfile.setWriteTimeout(config.getWriteTimeout());
		}
		if (config.getReadTimeout() != null && config.getReadTimeout() > 0) {
			httpProfile.setReadTimeout(config.getReadTimeout());
		}
		return httpProfile;
	}

	private ClientProfile newClientProfile(HttpProfile httpProfile, TencentCloudConfig config) {
		ClientProfile clientProfile = new ClientProfile();
		clientProfile.setHttpProfile(httpProfile);
		clientProfile.setLanguage(config.getLanguage() == null ? Language.ZH_CN : config.getLanguage());
		clientProfile.setDebug(config.getDebug() != null && config.getDebug());
		return clientProfile;
	}

	private OcrClient newOcrClient(TencentCloudConfig config) {
		// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
		// 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
		Credential cred = this.newCredential(config);

		// 实例化一个HTTP选项，可选的，没有特殊需求可以跳过
		HttpProfile httpProfile = this.newHttpProfile(config);

		// 实例化一个client选项，可选的，没有特殊需求可以跳过
		ClientProfile clientProfile = this.newClientProfile(httpProfile, config);

		// 实例化要请求产品的client对象,clientProfile是可选的
		return new OcrClient(cred, config.getRegion(), clientProfile);
	}

	//endregion


	//region Getter

	@Nullable
	public TencentCloudConfig getGlobalConfig() {
		return globalConfig;
	}

	//endregion
}
