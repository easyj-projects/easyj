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
package icu.easyj.spring.boot.autoconfigure.sdk.ocr;

import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import icu.easyj.sdk.ocr.IOcrTemplate;
import icu.easyj.sdk.ocr.WrapperOcrTemplate;
import icu.easyj.sdk.ocr.idcard.IIdCardOcrTemplate;
import icu.easyj.sdk.tencent.cloud.ocr.TencentCloudConfig;
import icu.easyj.sdk.tencent.cloud.ocr.idcard.ITencentCloudIdCardOcrTemplate;
import icu.easyj.sdk.tencent.cloud.ocr.idcard.impls.DefaultTencentCloudIdCardOcrTemplate;
import icu.easyj.sdk.tencent.cloud.ocr.idcard.impls.TencentEasyjIdCardOcrTemplate;
import icu.easyj.spring.boot.autoconfigure.sdk.ocr.idcardocr.IdCardOcrProperties;
import icu.easyj.spring.boot.autoconfigure.sdk.ocr.idcardocr.tencent.TencentCloudIdCardOcrProperties;
import icu.easyj.spring.boot.autoconfigure.sdk.ocr.idcardocr.tencent.TencentCloudProperties;
import icu.easyj.spring.boot.autoconfigure.sdk.ocr.idcardocr.tencent.WrapperTencentCloudConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文字识别（OCR）自动装配
 *
 * @author wangliang181230
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(IOcrTemplate.class)
@EnableConfigurationProperties(IdCardOcrProperties.class)
public class OcrTemplateAutoConfiguration {

	//region 腾讯云的实现 start

	/**
	 * 自动装配腾讯云OCR相关的Bean
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ITencentCloudIdCardOcrTemplate.class, IDCardOCRRequest.class})
	@ConditionalOnProperty(value = "easyj.sdk.ocr.idcard-ocr.type", havingValue = "tencent", matchIfMissing = true)
	@EnableConfigurationProperties({TencentCloudProperties.class, TencentCloudIdCardOcrProperties.class})
	public static class TencentOcrTemplateConfiguration {

		/**
		 * 腾讯云身份证识别（IDCardOCR）接口封装的Bean
		 *
		 * @param cloudProperties     腾讯云通用配置
		 * @param idCardOcrProperties 腾讯云身份证识别相关配置
		 * @return 腾讯云身份证识别接口的Bean
		 */
		@Bean
		@ConditionalOnMissingBean
		public ITencentCloudIdCardOcrTemplate defaultTencentCloudIdCardOcrTemplate(TencentCloudProperties cloudProperties,
																				   TencentCloudIdCardOcrProperties idCardOcrProperties) {
			TencentCloudConfig config = new WrapperTencentCloudConfig(cloudProperties, idCardOcrProperties);
			return new DefaultTencentCloudIdCardOcrTemplate(config);
		}

		/**
		 * 基于腾讯云实现的 {@link IIdCardOcrTemplate} 接口
		 *
		 * @param template 腾讯云身份证识别（IDCardOCR）接口封装的Bean
		 * @return 身份证识别接口的Bean
		 */
		@Bean
		public IIdCardOcrTemplate tencentIdCardOcrTemplate(ITencentCloudIdCardOcrTemplate template) {
			return new TencentEasyjIdCardOcrTemplate(template);
		}
	}

	//endregion 腾讯云的实现 end


	/**
	 * 包装的文字识别Bean，由各种识别接口拼接而成
	 *
	 * @param idCardOcrTemplate 身份证识别接口
	 * @return 包装的文字识别Bean
	 * @see IOcrTemplate
	 * @see WrapperOcrTemplate
	 * @see IIdCardOcrTemplate
	 */
	@Bean
	@ConditionalOnMissingBean
	public IOcrTemplate wrapperOcrTemplate(IIdCardOcrTemplate idCardOcrTemplate) {
		return new WrapperOcrTemplate(idCardOcrTemplate);
	}
}
