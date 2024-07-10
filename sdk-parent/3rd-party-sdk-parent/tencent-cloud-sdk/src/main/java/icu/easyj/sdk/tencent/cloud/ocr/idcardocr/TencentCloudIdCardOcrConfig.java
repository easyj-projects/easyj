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
package icu.easyj.sdk.tencent.cloud.ocr.idcardocr;

import icu.easyj.sdk.ocr.idcardocr.IdCardOcrWarn;
import icu.easyj.sdk.tencent.cloud.common.config.TencentCloudCommonConfig;

/**
 * 腾讯云身份证识别（IDCardOCR）相关配置
 *
 * @author wangliang181230
 */
public class TencentCloudIdCardOcrConfig extends TencentCloudCommonConfig {

	/**
	 * 最小身份证图片质量分数
	 * 低于此清晰度时，将报出 {@link IdCardOcrWarn#VAGUE} 告警
	 */
	private Integer minQuality;


	//region Getter、Setter

	public Integer getMinQuality() {
		return minQuality;
	}

	public void setMinQuality(Integer minQuality) {
		this.minQuality = minQuality;
	}

	//endregion
}
