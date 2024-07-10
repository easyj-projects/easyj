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
package icu.easyj.sdk.ocr.idcardocr;

import java.util.Map;

import icu.easyj.sdk.ocr.CardSide;

/**
 * 身份证识别请求信息
 *
 * @author wangliang181230
 */
public class IdCardOcrRequest extends SimpleIdCardOcrRequest {

	/**
	 * 身份证图片的Base64串或URL地址
	 */
	private String image;

	/**
	 * 身份证图片正反面枚举
	 */
	private CardSide cardSide;


	//region Constructor

	public IdCardOcrRequest() {
	}

	public IdCardOcrRequest(String image, CardSide cardSide, IdCardOcrAdvanced[] advancedArr) {
		super(advancedArr);
		this.image = image;
		this.cardSide = cardSide;
	}

	public IdCardOcrRequest(String image, CardSide cardSide, IdCardOcrAdvanced[] advancedArr, Map<String, Object> configs) {
		super(advancedArr, configs);
		this.image = image;
		this.cardSide = cardSide;
	}

	public IdCardOcrRequest(SimpleIdCardOcrRequest simpleRequest) {
		if (simpleRequest != null) {
			super.setAdvancedArr(simpleRequest.getAdvancedArr());
			super.setConfigs(simpleRequest.getConfigs());
		}
	}

	//endregion


	//region Getter、Setter

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public CardSide getCardSide() {
		return cardSide;
	}

	public void setCardSide(CardSide cardSide) {
		this.cardSide = cardSide;
	}

	//endregion
}
