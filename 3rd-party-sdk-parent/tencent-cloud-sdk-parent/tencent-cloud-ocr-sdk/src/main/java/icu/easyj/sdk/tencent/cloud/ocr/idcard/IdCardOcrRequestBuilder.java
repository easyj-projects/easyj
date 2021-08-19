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
package icu.easyj.sdk.tencent.cloud.ocr.idcard;

import java.io.InputStream;

import cn.hutool.core.codec.Base64;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import icu.easyj.core.constant.UrlConstants;
import icu.easyj.sdk.ocr.CardSide;

/**
 * 身份证识别请求构建者
 *
 * @author wangliang181230
 */
public class IdCardOcrRequestBuilder {

	/**
	 * 请求实例
	 */
	private final IDCardOCRRequest request = new IDCardOCRRequest();

	/**
	 * 请求参数配置
	 */
	private IdCardOcrAdvancedConfig config;


	/**
	 * 设置图片Base64串或Url地址
	 *
	 * @param image 图片Base64串或Url地址
	 * @return self
	 */
	public IdCardOcrRequestBuilder image(String image) {
		if (image.startsWith(UrlConstants.HTTP_PRE) || image.startsWith(UrlConstants.HTTPS_PRE)) {
			request.setImageUrl(image);
		} else {
			request.setImageBase64(image);
		}
		return this;
	}

	/**
	 * 设置图片输入流
	 *
	 * @param imageInputStream 图片输入流
	 * @return self
	 */
	public IdCardOcrRequestBuilder image(InputStream imageInputStream) {
		request.setImageBase64(Base64.encode(imageInputStream));
		return this;
	}

	/**
	 * 设置卡证正反面
	 *
	 * @param cardSide 卡证正反面
	 * @return self
	 */
	public IdCardOcrRequestBuilder cardSide(String cardSide) {
		request.setCardSide(cardSide);
		return this;
	}

	/**
	 * 设置卡证正反面枚举
	 *
	 * @param cardSide 卡证正反面枚举
	 * @return self
	 */
	public IdCardOcrRequestBuilder cardSide(CardSide cardSide) {
		if (cardSide == null) {
			// 为空时，不设置
			return this;
		}
		return cardSide(cardSide.name());
	}

	/**
	 * 设置为正面
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder frontCardSide() {
		return this.cardSide(CardSide.FRONT);
	}

	/**
	 * 设置为反面
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder backCardSide() {
		return this.cardSide(CardSide.BACK);
	}

	/**
	 * 高级功能：开启 身份证照片裁剪（去掉证件外多余的边缘、自动矫正拍摄角度），获取方式：response.getAdvancedInfo().getIdCard()
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableCropIdCard() {
		this.getConfig().setCropIdCard(true);
		return this;
	}

	/**
	 * 高级功能：开启 人像照片裁剪（自动抠取身份证头像区域），获取方式：response.getAdvancedInfo().getPortrait()
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableCropPortrait() {
		this.getConfig().setCropPortrait(true);
		return this;
	}

	/**
	 * 高级功能：开启 解析图片质量分数（评价图片的模糊程度）；值域：0~100，获取方式：response.getAdvancedInfo().getQuality()
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableQuality() {
		this.getConfig().setQuality(true);
		return this;
	}

	/**
	 * 高级功能：开启 多卡证检测
	 * 存在多张卡时，{@link ITencentCloudIdCardOcrTemplate#doIdCardOcr} 方法将直接抛出异常
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableMultiCardDetect() {
		this.getConfig().setMultiCardDetect(true);
		return this;
	}

	/**
	 * 高级功能：开启 身份证有效日期不合法告警，警告码：-9100，获取方式：response.getAdvancedInfo().getWarnInfos()数组中遍历判断
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableInvalidDateWarn() {
		this.getConfig().setInvalidDateWarn(true);
		return this;
	}

	/**
	 * 高级功能：开启 边框和框内遮挡告警，警告码：-9101 身份证边框不完整告警；-9105 身份证框内遮挡告警。
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableBorderCheckWarn() {
		this.getConfig().setBorderCheckWarn(true);
		return this;
	}

	/**
	 * 高级功能：开启 复印件告警，警告码：-9102
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableCopyWarn() {
		this.getConfig().setCopyWarn(true);
		return this;
	}

	/**
	 * 高级功能：开启 翻拍告警，警告码：-9103
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableReshootWarn() {
		this.getConfig().setReshootWarn(true);
		return this;
	}

	/**
	 * 高级功能：开启 临时身份证告警，警告码：-9104
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableTempIdWarn() {
		this.getConfig().setTempIdWarn(true);
		return this;
	}

	/**
	 * 高级功能：开启 PS检测告警，警告码：-9106
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableDetectPsWarn() {
		this.getConfig().setDetectPsWarn(true);
		return this;
	}

	/**
	 * 开启 所有高级功能
	 *
	 * @return self
	 */
	public IdCardOcrRequestBuilder enableAllAdvanced() {
		// 裁剪功能
		this.enableCropIdCard(); // 开启 身份证照片裁剪（去掉证件外多余的边缘、自动矫正拍摄角度）
		this.enableCropPortrait(); // 开启 人像照片裁剪（自动抠取身份证头像区域）

		// 图片质量解析功能
		this.enableQuality(); // 开启 解析图片质量分数（评价图片的模糊程度），值域：1~100

		// 检测功能
		this.enableMultiCardDetect(); // 开启 多卡证检测

		// 各种告警功能
		this.enableInvalidDateWarn(); // 开启 身份证有效日期不合法告警
		this.enableBorderCheckWarn(); // 开启 边框和框内遮挡告警
		this.enableCopyWarn(); // 开启 复印件告警
		this.enableReshootWarn(); // 开启 翻拍告警
		this.enableTempIdWarn(); // 开启 临时身份证告警
		this.enableDetectPsWarn(); // 开启 PS检测告警
		return this;
	}

	/**
	 * 构建请求对象
	 *
	 * @return request 请求对象
	 */
	public IDCardOCRRequest build() {
		if (this.config != null) {
			this.optimizeConfigBeforeBuild();
			this.request.setConfig(this.config.toJson());
		}
		return this.request;
	}

	/**
	 * 优化配置，避免不必要的性能损耗
	 */
	private void optimizeConfigBeforeBuild() {
		// 反面没有人像照片，无需启用CropPortrait
		if (this.config.getCropPortrait() != null && CardSide.BACK.name().equals(this.request.getCardSide())) {
			this.config.setCropPortrait(null);
		}
	}

	private IdCardOcrAdvancedConfig getConfig() {
		if (this.config == null) {
			this.config = new IdCardOcrAdvancedConfig();
		}
		return this.config;
	}
}