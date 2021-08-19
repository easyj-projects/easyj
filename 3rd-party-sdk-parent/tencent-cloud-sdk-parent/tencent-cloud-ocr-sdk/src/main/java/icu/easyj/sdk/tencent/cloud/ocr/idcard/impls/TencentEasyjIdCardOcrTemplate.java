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
package icu.easyj.sdk.tencent.cloud.ocr.idcard.impls;

import java.text.ParseException;
import java.util.Date;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import icu.easyj.core.util.DateUtils;
import icu.easyj.sdk.ocr.CardSide;
import icu.easyj.sdk.ocr.idcard.IIdCardOcrTemplate;
import icu.easyj.sdk.ocr.idcard.IdCardOcrAdvanced;
import icu.easyj.sdk.ocr.idcard.IdCardOcrResponse;
import icu.easyj.sdk.ocr.idcard.IdCardOcrSdkException;
import icu.easyj.sdk.ocr.idcard.IdCardOcrWarn;
import icu.easyj.sdk.tencent.cloud.ocr.build.OcrRequestBuilder;
import icu.easyj.sdk.tencent.cloud.ocr.idcard.ITencentCloudIdCardOcrTemplate;
import icu.easyj.sdk.tencent.cloud.ocr.idcard.IdCardOcrAdvancedInfo;
import icu.easyj.sdk.tencent.cloud.ocr.idcard.IdCardOcrRequestBuilder;
import icu.easyj.sdk.tencent.cloud.ocr.idcard.TencentIdCardOcrWarn;
import icu.easyj.sdk.tencent.cloud.util.TencentCloudSDKExceptionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 腾讯云IdCardOCR实现的 {@link icu.easyj.sdk.ocr.idcard.IIdCardOcrTemplate} 接口
 *
 * @author wangliang181230
 */
public class TencentEasyjIdCardOcrTemplate implements IIdCardOcrTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(TencentEasyjIdCardOcrTemplate.class);


	private final ITencentCloudIdCardOcrTemplate idCardOcrTemplate;


	public TencentEasyjIdCardOcrTemplate(ITencentCloudIdCardOcrTemplate idCardOcrTemplate) {
		Assert.notNull(idCardOcrTemplate, "'idCardOcrTemplate' must be not null");
		this.idCardOcrTemplate = idCardOcrTemplate;
	}


	@Override
	public IdCardOcrResponse idCardOcr(@NonNull String image, @Nullable CardSide cardSide, IdCardOcrAdvanced... advancedArr) throws IdCardOcrSdkException {
		Assert.notNull(image, "'image' must be not null");

		// 为两面时，
		if (CardSide.DOUBLE == cardSide) {
			cardSide = null;
		}

		// 创建request builder
		IdCardOcrRequestBuilder builder = OcrRequestBuilder.idCardOcrRequestBuilder()
				.image(image) // 图片
				.cardSide(cardSide); // 卡片正反面，可以为空
		this.setAdvanced(builder, advancedArr);

		// 构建request
		IDCardOCRRequest request = builder.build();

		// 发送请求，返回响应
		IDCardOCRResponse re;
		try {
			re = idCardOcrTemplate.doIdCardOcr(request);
		} catch (TencentCloudSDKException e) {
			String errorCode = TencentCloudSDKExceptionUtils.getErrorCode(e);
			String errorMsg = "身份证识别失败" + (StringUtils.hasText(errorCode) ? "：" + errorCode : "");
			throw new IdCardOcrSdkException(errorMsg, errorCode, e);
		} catch (RuntimeException e) {
			throw new IdCardOcrSdkException("身份证识别失败", e);
		}


		//region 读取响应信息

		// 转换为当前接口的响应类型
		IdCardOcrResponse response = new IdCardOcrResponse();

		// 设置正反面枚举
		if (StringUtils.hasText(re.getName())) {
			response.setCardSide(CardSide.FRONT);
		} else if (StringUtils.hasText(re.getAuthority())) {
			response.setCardSide(CardSide.BACK);
		} else {
			throw new IdCardOcrSdkException("未知的身份证正反面信息", "UNKNOWN_CARD_SIDE");
		}
		if (cardSide != null && cardSide != response.getCardSide()) {
			throw new IdCardOcrSdkException("当前身份证图片不是" + cardSide.sideName() + "照", "WRONG_CARD_SIDE");
		}

		// 设置正面信息
		if (CardSide.FRONT == response.getCardSide()) {
			response.setName(re.getName());
			response.setSex(re.getSex());
			response.setNation(re.getNation());
			this.setBirthday(response, re.getBirth());
			response.setAddress(re.getAddress());
			response.setIdNum(re.getIdNum());
		}

		// 设置反面信息
		if (CardSide.BACK == response.getCardSide()) {
			response.setAuthority(re.getAuthority());
			this.setValidDate(response, re.getValidDate());
		}

		// 设置高级功能信息
		this.setResponseAdvancedInfo(response, re.getAdvancedInfo());

		//endregion

		// 返回响应
		return response;
	}

	//region Private

	/**
	 * 启用高级功能
	 *
	 * @param builder     请求构建者
	 * @param advancedArr 高级功级数组
	 */
	private void setAdvanced(IdCardOcrRequestBuilder builder, IdCardOcrAdvanced[] advancedArr) {
		// 部分高级功能默认启用
		builder.enableMultiCardDetect(); // 多卡检测

		if (ArrayUtils.isNotEmpty(advancedArr)) {
			boolean isAll = false;
			for (IdCardOcrAdvanced advanced : advancedArr) {
				switch (advanced) {
					case ALL:
						builder.enableAllAdvanced(); // 全部
						isAll = true;
						break;
					case CROP_ID_CARD:
						builder.enableCropIdCard();
						break;
					case CROP_PORTRAIT:
						builder.enableCropPortrait();
						break;
					case DETECT_INVALID:
						builder.enableInvalidDateWarn();
						break;
					case DETECT_INCOMPLETE:
						builder.enableBorderCheckWarn();
						break;
					case DETECT_COPY:
						builder.enableCopyWarn() // 复印件
								.enableReshootWarn(); // 翻拍照片
						break;
					case DETECT_TEMP:
						builder.enableTempIdWarn();
						break;
					case DETECT_FAKE:
						builder.enableDetectPsWarn();
						break;
					default:
						break;
				}
				if (isAll) {
					break;
				}
			}

			// 防止高级功能未使用，做如下测试
			if (LOGGER.isWarnEnabled() && IdCardOcrAdvanced.values().length > 8) {
				LOGGER.warn("当前`switch`代码段未包含{}枚举的所有值，请联系开发人员完善代码！", IdCardOcrAdvanced.class.getSimpleName());
			}
		}
	}

	/**
	 * 设置出生日期
	 *
	 * @param response 响应
	 * @param birthday 出生日期，格式如：yyyy/m/d
	 * @throws IdCardOcrSdkException 解析出生日期异常
	 */
	private void setBirthday(IdCardOcrResponse response, String birthday) throws IdCardOcrSdkException {
		try {
			response.setBirthday(DateUtils.parseDate2(birthday));
		} catch (ParseException e) {
			throw new IdCardOcrSdkException("出生日期解析失败：" + birthday, "PARSE_BIRTHDAY_FAILED", e);
		}
	}

	/**
	 * 设置有效期限
	 *
	 * @param response  响应
	 * @param validDate 有效期限字符串，格式有两种可能：`yyyy.mm.dd-yyyy.mm.dd` 或 `yyyy.mm.dd-长期`
	 * @throws IdCardOcrSdkException 解析有效期限异常
	 */
	private void setValidDate(IdCardOcrResponse response, String validDate) throws IdCardOcrSdkException {
		String[] validDateArr = validDate.split(StrPool.DASHED);

		try {
			String validDateStartStr = validDateArr[0];
			String validDateEndStr = validDateArr[1];

			Date validDateStart = DateUtils.parseDate3(validDateStartStr);
			Date validDateEnd = "长期".equals(validDateEndStr) ? null : DateUtils.parseDate3(validDateEndStr);

			response.setValidDateStart(validDateStart);
			response.setValidDateEnd(validDateEnd);
		} catch (Exception e) {
			throw new IdCardOcrSdkException("身份证有效期限解析失败：" + validDate, "PARSE_VALID_DATE_FAILED", e);
		}
	}

	/**
	 * 设置高级功能信息
	 *
	 * @param response         响应
	 * @param advancedInfoJson 高级功能信息JSON串
	 */
	private void setResponseAdvancedInfo(IdCardOcrResponse response, String advancedInfoJson) {
		IdCardOcrAdvancedInfo advancedInfo = IdCardOcrAdvancedInfo.fromJsonString(advancedInfoJson);
		if (advancedInfo == null) {
			return;
		}

		if (StringUtils.hasText(advancedInfo.getIdCardBase64())) {
			response.setIdCardBase64(advancedInfo.getIdCardBase64());
		}
		if (StringUtils.hasText(advancedInfo.getPortraitBase64())) {
			response.setPortraitBase64(advancedInfo.getPortraitBase64());
		}

		if (!CollectionUtils.isEmpty(advancedInfo.getWarnInfos())) {
			TencentIdCardOcrWarn warn;
			for (Integer warnCode : advancedInfo.getWarnInfos()) {
				warn = TencentIdCardOcrWarn.get(warnCode);
				if (warn != null) {
					// 两个枚举的名称一致，可以直接通过名称转换
					response.addWarns(IdCardOcrWarn.get(warn.name()));
				} else {
					if (LOGGER.isWarnEnabled()) {
						LOGGER.warn("身份证识别接口返回了未知的告警码：{}", warnCode);
					}
				}
			}
		}
	}

	//endregion
}
