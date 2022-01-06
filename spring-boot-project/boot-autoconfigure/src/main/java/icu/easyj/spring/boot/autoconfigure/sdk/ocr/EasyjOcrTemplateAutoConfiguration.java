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
package icu.easyj.spring.boot.autoconfigure.sdk.ocr;

import icu.easyj.sdk.ocr.IOcrTemplate;
import icu.easyj.sdk.ocr.WrapperOcrTemplate;
import icu.easyj.sdk.ocr.idcardocr.IIdCardOcrTemplate;
import icu.easyj.spring.boot.autoconfigure.sdk.ocr.idcardocr.IdCardOcrProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 文字识别（OCR）自动装配
 *
 * @author wangliang181230
 */
@ConditionalOnClass(IOcrTemplate.class)
@EnableConfigurationProperties(IdCardOcrProperties.class)
public class EasyjOcrTemplateAutoConfiguration {

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
