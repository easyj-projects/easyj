/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.sdk.tencent.cloud.ocr.idcardocr.impls;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.TimeMeter;
import icu.easyj.sdk.tencent.cloud.ocr.idcardocr.ITencentCloudIdCardOcrService;
import icu.easyj.sdk.tencent.cloud.ocr.idcardocr.TencentCloudIdCardOcrConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 腾讯云 身份证识别（IdCardOCR） 服务接口 默认实现
 *
 * @author wangliang181230
 */
public class DefaultTencentCloudIdCardOcrServiceImpl implements ITencentCloudIdCardOcrService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTencentCloudIdCardOcrServiceImpl.class);

	private static final String ENDPOINT = "ocr.tencentcloudapi.com";

	/**
	 * 全局的身份证识别配置
	 */
	@NonNull
	private final TencentCloudIdCardOcrConfig globalConfig;

	@NonNull
	private final OcrClient globalClient;


	public DefaultTencentCloudIdCardOcrServiceImpl(@NonNull TencentCloudIdCardOcrConfig config) {
		Assert.notNull(config, "'config' must not be null");

		this.globalConfig = config;
		this.globalClient = this.newOcrClient(config);
	}


	//region Override start

	////region 身份证识别 start

	/**
	 * 身份证识别
	 *
	 * @param request 请求
	 * @param config  当前请求的个性配置
	 * @return response 响应
	 * @see <a href="https://cloud.tencent.com/document/api/866/33524">API文档</a>
	 * @see <a href="https://console.cloud.tencent.com/api/explorer?Product=ocr&Version=2018-11-19&Action=IDCardOCR">调试页面</a>
	 */
	@Override
	public IDCardOCRResponse doIdCardOcr(IDCardOCRRequest request, @Nullable TencentCloudIdCardOcrConfig config) throws TencentCloudSDKException {
		if (config == null) {
			config = this.globalConfig;
		}

		Assert.notNull(config.getSecretId(), "'secretId' must not be null");
		Assert.notNull(config.getSecretKey(), "'secretKey' must not be null");
		Assert.notNull(config.getRegion(), "'region' must not be null");

		TimeMeter tm = TimeMeter.create();
		try {
			// 实例化要请求产品的client对象,clientProfile是可选的
			OcrClient client;
			if (config == this.globalConfig) {
				client = this.globalClient;
			} else {
				client = this.newOcrClient(config);
			}

			// 发送请求，并返回一个IDCardOCRResponse的实例，与请求对象对应
			IDCardOCRResponse response = client.IDCardOCR(request);

			// 记录日志
			if (LOGGER.isInfoEnabled()) {
				// base64不打印在日志中
				String imageBase64Bak = request.getImageBase64();
				String advancedInfoBak = response.getAdvancedInfo();
				request.setImageBase64(null);
				response.setAdvancedInfo(null);
				try {
					LOGGER.info("IDCardOCR 请求成功！\r\n==>\r\n -  Request: {}\r\n - Response: {}\r\n -   Config: {}\r\n -     Cost: {} ms\r\n<==\r\n",
							icu.easyj.core.util.StringUtils.toString(request),
							icu.easyj.core.util.StringUtils.toString(response),
							icu.easyj.core.util.StringUtils.toString(config),
							tm.spendMilliSeconds());
				} finally {
					request.setImageBase64(imageBase64Bak);
					response.setAdvancedInfo(advancedInfoBak);
				}
			}

			return response;
		} catch (TencentCloudSDKException | RuntimeException e) {
			String imageBase64Bak = request.getImageBase64();
			request.setImageBase64(null); // base64不打印在日志中
			try {
				LOGGER.error("身份证识别服务请求失败：{}\r\n==>\r\n - Request: {}\r\n -  Config: {}\r\n -    Cost: {} ms\r\n<==\r\n",
						e.getMessage(),
						icu.easyj.core.util.StringUtils.toString(request),
						icu.easyj.core.util.StringUtils.toString(config),
						tm.spendMilliSeconds());
			} finally {
				request.setImageBase64(imageBase64Bak);
			}
			throw e;
		}
	}

	////endregion 身份证识别 end

	//endregion Override end


	//region Private

	@Nullable
	private Credential newCredential(TencentCloudIdCardOcrConfig config) {
		if (StringUtils.isNotBlank(config.getSecretId()) && StringUtils.isNotBlank(config.getSecretKey())) {
			return new Credential(config.getSecretId(), config.getSecretKey());
		} else {
			return null;
		}
	}

	@NonNull
	private HttpProfile newHttpProfile(TencentCloudIdCardOcrConfig config) {
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

	private ClientProfile newClientProfile(HttpProfile httpProfile, TencentCloudIdCardOcrConfig config) {
		ClientProfile clientProfile = new ClientProfile();
		clientProfile.setHttpProfile(httpProfile);
		clientProfile.setLanguage(config.getLanguage() == null ? Language.ZH_CN : config.getLanguage());
		clientProfile.setDebug(config.getDebug() != null && config.getDebug());
		return clientProfile;
	}

	private OcrClient newOcrClient(TencentCloudIdCardOcrConfig config) {
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

	@NonNull
	@Override
	public TencentCloudIdCardOcrConfig getGlobalConfig() {
		return globalConfig;
	}

	//endregion
}
