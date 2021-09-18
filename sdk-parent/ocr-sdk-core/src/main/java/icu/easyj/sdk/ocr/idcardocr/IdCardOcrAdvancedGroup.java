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

/**
 * {@link IdCardOcrAdvanced} 枚举分组
 *
 * @author wangliang181230
 * @see IdCardOcrWarn
 */
public enum IdCardOcrAdvancedGroup {

	/**
	 * 不包含任何枚举的数组
	 */
	EMPTY(IdCardOcrAdvanced.EMPTY_ARRAY),

	/**
	 * 包含所有枚举的数组
	 */
	ALL(IdCardOcrAdvanced.ALL_ARRAY),

	/**
	 * 包含所有裁剪类枚举的数组
	 */
	ALL_COPE(IdCardOcrAdvanced.ALL_CROP_ARRAY),

	/**
	 * 包含所有检查类枚举的数组
	 */
	ALL_DETECT(IdCardOcrAdvanced.ALL_DETECT_ARRAY);


	private final IdCardOcrAdvanced[] advancedArr;


	IdCardOcrAdvancedGroup(IdCardOcrAdvanced[] advancedArr) {
		this.advancedArr = advancedArr;
	}


	public IdCardOcrAdvanced[] getAdvancedArr() {
		return this.advancedArr;
	}
}
