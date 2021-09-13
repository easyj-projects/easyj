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

import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import icu.easyj.sdk.ocr.idcardocr.IIdCardOcrTemplate;
import icu.easyj.sdk.tencent.cloud.common.config.TencentCloudCommonConfigUtils;
import icu.easyj.sdk.tencent.cloud.ocr.idcardocr.ITencentCloudIdCardOcrTemplate;
import icu.easyj.sdk.tencent.cloud.ocr.idcardocr.TencentCloudIdCardOcrConfig;
import icu.easyj.sdk.tencent.cloud.ocr.idcardocr.impls.DefaultTencentCloudIdCardOcrTemplate;
import icu.easyj.sdk.tencent.cloud.ocr.idcardocr.impls.TencentEasyjIdCardOcrTemplateImpl;
import icu.easyj.spring.boot.autoconfigure.sdk.ocr.EasyjOcrTemplateAutoConfiguration;
import icu.easyj.spring.boot.autoconfigure.sdk.tencent.cloud.EasyjTencentCloudAutoConfiguration;
import icu.easyj.spring.boot.autoconfigure.sdk.tencent.cloud.TencentCloudCommonProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 基于腾讯云的身份证识别（IDCardOCR）自动装配
 *
 * @author wangliang181230
 */
@ConditionalOnClass({ITencentCloudIdCardOcrTemplate.class, IDCardOCRRequest.class})
@ConditionalOnProperty(value = "easyj.sdk.ocr.idcard-ocr.type", havingValue = "tencent", matchIfMissing = true)
@AutoConfigureAfter(EasyjTencentCloudAutoConfiguration.class)
@AutoConfigureBefore(EasyjOcrTemplateAutoConfiguration.class)
public class EasyjTencentCloudIdCardOcrAutoConfiguration {

	@Bean
	@ConfigurationProperties("easyj.sdk.ocr.idcard-ocr.tencent")
	public TencentCloudIdCardOcrConfig tencentCloudIdCardOcrConfig() {
		return new TencentCloudIdCardOcrConfig();
	}

	/**
	 * 腾讯云身份证识别（IDCardOCR）接口封装的Bean
	 *
	 * @param commonProperties    腾讯云通用配置（所有SDK可以共享该配置）
	 * @param idCardOcrProperties 腾讯云身份证识别（IDCardOCR）配置
	 * @return 腾讯云身份证识别接口的Bean
	 */
	@Bean
	@ConditionalOnMissingBean
	public ITencentCloudIdCardOcrTemplate defaultTencentCloudIdCardOcrTemplate(
			TencentCloudCommonProperties commonProperties, TencentCloudIdCardOcrConfig idCardOcrProperties) {
		TencentCloudCommonConfigUtils.merge(idCardOcrProperties, commonProperties);
		return new DefaultTencentCloudIdCardOcrTemplate(idCardOcrProperties);
	}

	/**
	 * 基于腾讯云实现的 {@link IIdCardOcrTemplate} 接口
	 *
	 * @param template 腾讯云身份证识别（IDCardOCR）接口封装的Bean
	 * @return 身份证识别接口的Bean
	 */
	@Bean
	@ConditionalOnMissingBean
	public IIdCardOcrTemplate tencentIdCardOcrTemplate(ITencentCloudIdCardOcrTemplate template) {
		return new TencentEasyjIdCardOcrTemplateImpl(template);
	}
}
