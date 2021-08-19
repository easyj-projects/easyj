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

import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 腾讯云身份证识别相关配置
 *
 * @author wangliang181230
 */
@ConfigurationProperties("easyj.sdk.tencent.ocr.idcard-ocr")
public class TencentCloudIdCardOcrProperties {

	//region 地域配置

	/**
	 * 地域代码
	 * <p>
	 * 不同云服务的可选地域可能不同
	 */
	private String region;

	//endregion


	//region 接口相关配置

	/**
	 * Connect timeout in seconds
	 */
	private Integer connTimeout = HttpProfile.TM_MINUTE;

	/**
	 * Write timeout in seconds
	 */
	private Integer writeTimeout;

	/**
	 * Read timeout in seconds.
	 */
	private Integer readTimeout;

	/**
	 * 语言，默认：简体中文
	 */
	private Language language = Language.ZH_CN;

	/**
	 * 是否调试
	 */
	private Boolean debug = false;

	//endregion


	//region Getter、Setter

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getConnTimeout() {
		return connTimeout;
	}

	public void setConnTimeout(Integer connTimeout) {
		this.connTimeout = connTimeout;
	}

	public Integer getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(Integer writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	//endregion

}
