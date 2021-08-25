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
package icu.easyj.sdk.ocr.idcardocr;

import java.util.Map;

/**
 * 身份证识别请求信息
 *
 * @author wangliang181230
 */
public class SimpleIdCardOcrRequest {

	/**
	 * 最小身份证图片质量分数
	 */
	private Integer minQuality;

	/**
	 * 高级功能数组
	 */
	private IdCardOcrAdvanced[] advancedArr;

	/**
	 * 动态传入最终实现类的配置信息
	 * 可以做到动态可配置化
	 */
	private Map<String, String> config;


	//region Constructor

	public SimpleIdCardOcrRequest() {
	}

	public SimpleIdCardOcrRequest(IdCardOcrAdvanced[] advancedArr) {
		this.advancedArr = advancedArr;
	}

	public SimpleIdCardOcrRequest(Integer minQuality, IdCardOcrAdvanced[] advancedArr, Map<String, String> config) {
		this.minQuality = minQuality;
		this.advancedArr = advancedArr;
		this.config = config;
	}

	//endregion


	//region Getter、Setter

	public Integer getMinQuality() {
		return minQuality;
	}

	public void setMinQuality(Integer minQuality) {
		this.minQuality = minQuality;
	}

	public IdCardOcrAdvanced[] getAdvancedArr() {
		return advancedArr;
	}

	public void setAdvancedArr(IdCardOcrAdvanced[] advancedArr) {
		this.advancedArr = advancedArr;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	//endregion
}
