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

import icu.easyj.sdk.ocr.CardSide;

/**
 * 身份证识别（IDCardOCR）接口
 *
 * @author wangliang181230
 */
public interface IIdCardOcrTemplate {

	//region 单面识别

	/**
	 * 身份证识别
	 *
	 * @param request 身份证识别请求信息
	 * @return response 响应
	 * @throws IdCardOcrSdkException 身份证识别异常
	 */
	IdCardOcrResponse idCardOcr(IdCardOcrRequest request) throws IdCardOcrSdkException;

	/**
	 * 重载方法：身份证识别
	 *
	 * @param image       身份证图片的Base64串或URL地址
	 * @param cardSide    正反面枚举（为空时，将自动解析正反而；不为空时，如果传入图片与该参数不符，将抛出异常）
	 * @param advancedArr 高级功能数组
	 * @return response 响应
	 * @throws IdCardOcrSdkException 身份证识别异常
	 */
	default IdCardOcrResponse idCardOcr(String image,
										CardSide cardSide,
										IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		return idCardOcr(new IdCardOcrRequest(image, cardSide, advancedArr, null));
	}

	/**
	 * 重载方法：身份证识别（自动识别正反面）
	 *
	 * @param image       身份证图片的Base64串或URL地址
	 * @param advancedArr 高级功能数组
	 * @return response 响应
	 * @throws IdCardOcrSdkException 身份证识别异常
	 */
	default IdCardOcrResponse idCardOcr(String image,
										IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		return this.idCardOcr(image, (CardSide)null, advancedArr);
	}

	//endregion


	//region 正反两面一起识别，拿到一个完整的响应信息

	/**
	 * 身份证识别（两面一起发送识别）
	 *
	 * @param image1          身份证正面或反面图片的Base64串或URL地址，两张图片入参顺序不做要求，但必须是不同的两面图片
	 * @param image2          身份证正面或反面图片的Base64串或URL地址，两张图片入参顺序不做要求，但必须是不同的两面图片
	 * @param returnIfHasWarn 如果第一张图片存在告警信息，则不再继续识别第二张图片了
	 * @param simpleRequest   少量的身份证识别请求信息
	 * @return response 响应
	 * @throws IdCardOcrSdkException 身份证识别异常
	 */
	default IdCardOcrResponse idCardOcr(String image1, String image2, boolean returnIfHasWarn,
										SimpleIdCardOcrRequest simpleRequest) throws IdCardOcrSdkException {
		IdCardOcrRequest request = new IdCardOcrRequest(simpleRequest);


		//region 请求识别第一张图片，获取response1

		request.setImage(image1);
		IdCardOcrResponse response1 = this.idCardOcr(request);

		// 如果`returnIfHasWarn=true`，且存在告警信息，则直接返回
		if (returnIfHasWarn && !response1.getWarns().isEmpty()) {
			return response1;
		}

		//endregion 请求第一张 end


		//region 请求识别第二张图片，获取response2

		// 根据第一张图片的解析结果，设置第二张图片的卡片正反面参数
		CardSide cardSide2 = CardSide.FRONT;
		if (CardSide.FRONT == response1.getCardSide()) {
			cardSide2 = CardSide.BACK;
		}

		// 请求识别第二张图片
		request.setImage(image2); // 覆盖图片参数
		request.setCardSide(cardSide2); // 设置正反面参数
		IdCardOcrResponse response2 = this.idCardOcr(request);

		// 如果两张图片属于同一面，则抛出异常
		if (response2.getCardSide() == response1.getCardSide()) {
			throw new IdCardOcrSdkException("两张图片的正反面属性相同", "SAME_CARD_SIDE");
		}

		//endregion 请求第二张 end


		//region 合并两张图片的响应内容，使响应信息包含身份证正反两面的信息

		// 先确定正反面响应
		IdCardOcrResponse doubleResponse; // 双面响应，先将正面赋值给它
		IdCardOcrResponse backResponse; // 反面响应
		if (CardSide.FRONT == response1.getCardSide()) {
			doubleResponse = response1;
			backResponse = response2;
		} else {
			doubleResponse = response2;
			backResponse = response1;
		}
		// 设置为正反面都有
		doubleResponse.setCardSide(CardSide.BOTH);

		//region 将反面信息合并到双面响应中
		doubleResponse.setAuthority(backResponse.getAuthority());
		doubleResponse.setValidDateStart(backResponse.getValidDateStart());
		doubleResponse.setValidDateEnd(backResponse.getValidDateEnd());
		// 将反面信息的`idCardBase64`属性，设置到`backIdCardBase64`属性中
		doubleResponse.setBackIdCardBase64(backResponse.getIdCardBase64());
		// 告警信息
		doubleResponse.getWarns().addAll(backResponse.getWarns());
		//endregion

		//endregion 合并 end

		return doubleResponse;
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
	default IdCardOcrResponse idCardOcr(String image1, String image2, boolean returnIfHasWarn,
										IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		return this.idCardOcr(image1, image2, returnIfHasWarn, new SimpleIdCardOcrRequest(advancedArr));
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
	default IdCardOcrResponse idCardOcr(String image1, String image2,
										IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		return idCardOcr(image1, image2, false, advancedArr);
	}

	//endregion
}
