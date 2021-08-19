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
package icu.easyj.sdk.ocr;

import icu.easyj.sdk.ocr.idcard.IIdCardOcrTemplate;
import icu.easyj.sdk.ocr.idcard.IdCardOcrAdvanced;
import icu.easyj.sdk.ocr.idcard.IdCardOcrResponse;
import icu.easyj.sdk.ocr.idcard.IdCardOcrSdkException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 文字识别（OCR）接口包装实现
 *
 * @author wangliang181230
 */
public class WrapperOcrTemplate implements IOcrTemplate {

	private final IIdCardOcrTemplate idCardOcrTemplate;


	public WrapperOcrTemplate(IIdCardOcrTemplate idCardOcrTemplate) {
		this.idCardOcrTemplate = idCardOcrTemplate;
	}


	//region Override 身份证识别

	@Override
	public IdCardOcrResponse idCardOcr(@NonNull String image, @Nullable CardSide cardSide,
									   IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		return idCardOcrTemplate.idCardOcr(image, cardSide, advancedArr);
	}

	//endregion
}
