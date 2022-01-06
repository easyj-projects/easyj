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
package icu.easyj.sdk.ocr.idcardocr;

import java.util.Map;

import icu.easyj.core.convert.ConvertUtils;
import org.springframework.lang.Nullable;

/**
 * 身份证识别请求信息
 *
 * @author wangliang181230
 */
public class SimpleIdCardOcrRequest {

	/**
	 * 高级功能数组
	 */
	private IdCardOcrAdvanced[] advancedArr;

	/**
	 * 动态传入最终实现类的配置信息
	 * 可以做到动态可配置化
	 */
	@Nullable
	private Map<String, Object> configs;


	//region Constructor

	public SimpleIdCardOcrRequest() {
	}

	public SimpleIdCardOcrRequest(IdCardOcrAdvanced[] advancedArr) {
		this.advancedArr = advancedArr;
	}

	public SimpleIdCardOcrRequest(IdCardOcrAdvanced[] advancedArr, Map<String, Object> configs) {
		this.advancedArr = advancedArr;
		this.configs = configs;
	}

	//endregion


	//region Getter、Setter

	public IdCardOcrAdvanced[] getAdvancedArr() {
		return advancedArr;
	}

	public void setAdvancedArr(IdCardOcrAdvanced[] advancedArr) {
		this.advancedArr = advancedArr;
	}

	@Nullable
	public Map<String, Object> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, Object> configs) {
		this.configs = configs;
	}

	@Nullable
	public <T> T getConfig(String key) {
		if (configs == null) {
			return null;
		} else {
			return (T)configs.get(key);
		}
	}

	@Nullable
	public <T> T getConfig(String key, Class<T> typeClass) {
		if (configs == null) {
			return null;
		} else {
			return ConvertUtils.convert(configs.get(key), typeClass);
		}
	}

	//endregion
}
