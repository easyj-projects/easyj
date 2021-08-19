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
package icu.easyj.sdk.ocr.idcard;

import icu.easyj.sdk.ocr.CardSide;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 身份证识别（IDCardOCR）接口
 *
 * @author wangliang181230
 */
public interface IIdCardOcrTemplate {

	/**
	 * 身份证识别
	 *
	 * @param image       身份证图片的Base64串或URL地址
	 * @param cardSide    正反面枚举（为空时，将自动解析正反而；不为空时，如果传入图片与该参数不符，将抛出异常）
	 * @param advancedArr 高级功能数组
	 * @return response 响应
	 * @throws IdCardOcrSdkException 身份证识别异常
	 */
	@NonNull
	IdCardOcrResponse idCardOcr(@NonNull String image, @Nullable CardSide cardSide,
								IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException;

	/**
	 * 重载方法：身份证识别（自动识别正反面）
	 *
	 * @param image       身份证图片的Base64串或URL地址
	 * @param advancedArr 高级功能数组
	 * @return response 响应
	 * @throws IdCardOcrSdkException 身份证识别异常
	 */
	@NonNull
	default IdCardOcrResponse idCardOcr(@NonNull String image,
										IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		return this.idCardOcr(image, (CardSide)null, advancedArr);
	}

	/**
	 * 重载方法：身份证识别（两面一起发送识别）
	 *
	 * @param image1          身份证正面或反面图片的Base64串或URL地址，两张图片入参顺序不做要求，但必须是不同的两面图片
	 * @param image2          身份证正面或反面图片的Base64串或URL地址，两张图片入参顺序不做要求，但必须是不同的两面图片
	 * @param returnIfHasWarn 如果第一张图片存在告警信息，则不再继续识别第二张图片了
	 * @param advancedArr     高级功能数组
	 * @return response 响应
	 * @throws IdCardOcrSdkException 身份证识别异常
	 */
	@NonNull
	default IdCardOcrResponse idCardOcr(@NonNull String image1, @NonNull String image2, boolean returnIfHasWarn,
										IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		// 请求识别第一张图片
		IdCardOcrResponse response = this.idCardOcr(image1, advancedArr);

		// 如果`returnIfHasWarn=true`，且存在告警信息，则直接返回
		if (returnIfHasWarn && !response.getWarns().isEmpty()) {
			return response;
		}

		// 根据第一张图片的解析结果，设置第二张图片的卡片正反面参数
		CardSide cardSide2 = CardSide.FRONT;
		if (response.getCardSide() == CardSide.FRONT) {
			cardSide2 = CardSide.BACK;
		}

		// 请求识别第二张图片
		IdCardOcrResponse response2 = this.idCardOcr(image2, cardSide2, advancedArr);
		if (response2.getCardSide() == response.getCardSide()) {
			throw new IdCardOcrSdkException("两张图片的正反面属性相同", "SAME_CARD_SIDE");
		}

		// 确定正反面响应
		IdCardOcrResponse doubleResponse, backResponse;
		if (response.getCardSide() == CardSide.FRONT) {
			doubleResponse = response;
			backResponse = response2;
		} else {
			doubleResponse = response2;
			backResponse = response;
		}

		//region 合并两个响应的内容

		// 设置为两面
		doubleResponse.setCardSide(CardSide.DOUBLE);

		// 反面信息
		doubleResponse.setAuthority(backResponse.getAuthority());
		doubleResponse.setValidDateStart(backResponse.getValidDateStart());
		doubleResponse.setValidDateEnd(backResponse.getValidDateEnd());
		// 将反面信息的`idCardBase64`属性，设置到`backIdCardBase64`属性中
		doubleResponse.setBackIdCardBase64(backResponse.getIdCardBase64());
		// 告警信息
		doubleResponse.getWarns().addAll(backResponse.getWarns());

		//endregion

		return doubleResponse;
	}

	/**
	 * 重载方法：身份证识别（两面一起发送识别）
	 *
	 * @param image1      身份证正面或反面图片的Base64串或URL地址，两张图片入参顺序不做要求，但必须是不同的两面图片
	 * @param image2      身份证正面或反面图片的Base64串或URL地址，两张图片入参顺序不做要求，但必须是不同的两面图片
	 * @param advancedArr 高级功能数组
	 * @return response 响应
	 * @throws IdCardOcrSdkException 身份证识别异常
	 */
	default IdCardOcrResponse idCardOcr(@NonNull String image1, @NonNull String image2,
										IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		return idCardOcr(image1, image2, false, advancedArr);
	}
}
