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
package icu.easyj.sdk.tencent.cloud.ocr.idcardocr;

import java.util.Map;

import com.tencentcloudapi.common.profile.Language;
import icu.easyj.core.util.StringUtils;
import icu.easyj.sdk.ocr.idcardocr.IdCardOcrWarn;
import icu.easyj.sdk.tencent.cloud.common.config.TencentCloudCommonConfig;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.lang.Nullable;

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


	//region Static

	@Nullable
	public static TencentCloudIdCardOcrConfig fromMap(@Nullable Map<String, String> configMap) {
		if (configMap == null || configMap.isEmpty()) {
			return null;
		}

		TencentCloudIdCardOcrConfig config = new TencentCloudIdCardOcrConfig();

		config.setSecretId(configMap.get("secretId"));
		config.setSecretKey(configMap.get("secretKey"));
		config.setRegion(configMap.get("region"));
		if (StringUtils.isNotBlank(configMap.get("connTimeout"))) {
			config.setConnTimeout(Integer.parseInt(configMap.get("connTimeout")));
		}
		if (StringUtils.isNotBlank(configMap.get("writeTimeout"))) {
			config.setWriteTimeout(Integer.parseInt(configMap.get("writeTimeout")));
		}
		if (StringUtils.isNotBlank(configMap.get("readTimeout"))) {
			config.setReadTimeout(Integer.parseInt(configMap.get("readTimeout")));
		}
		config.setLanguage(EnumUtils.getEnum(Language.class, configMap.get("language"), Language.ZH_CN));
		config.setDebug(configMap.get("debug") == null ? null : "true".equalsIgnoreCase(configMap.get("debug")));
		if (StringUtils.isNotBlank(configMap.get("minQuality"))) {
			config.setMinQuality(Integer.parseInt(configMap.get("minQuality")));
		}

		return config;
	}

	//endregion
}
