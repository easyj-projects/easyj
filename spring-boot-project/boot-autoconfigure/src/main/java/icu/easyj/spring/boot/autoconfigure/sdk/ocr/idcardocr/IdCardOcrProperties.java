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
package icu.easyj.spring.boot.autoconfigure.sdk.ocr.idcardocr;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 身份证识别相关配置
 *
 * @author wangliang181230
 */
@ConfigurationProperties("easyj.sdk.ocr.idcard-ocr")
public class IdCardOcrProperties {

	/**
	 * 身份证识别接口实现类型，可选值：tencent(默认)。后续有需要再扩展百度云或阿里云等
	 */
	private String type = "tencent";


	//region Getter、Setter

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//endregion
}
