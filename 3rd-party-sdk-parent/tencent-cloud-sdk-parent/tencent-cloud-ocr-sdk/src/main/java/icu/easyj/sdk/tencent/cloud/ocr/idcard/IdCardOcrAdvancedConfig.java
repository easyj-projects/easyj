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

import org.springframework.lang.Nullable;

/**
 * 身份证识别（IDCardOCR）接口高级功能配置
 *
 * @author wangliang181230
 */
public class IdCardOcrAdvancedConfig {

	/**
	 * 身份证照片裁剪（去掉证件外多余的边缘、自动矫正拍摄角度）
	 * 扩展响应：IdCard
	 */
	private Boolean cropIdCard;

	/**
	 * 人像照片裁剪（自动抠取身份证头像区域）
	 * 扩展响应：Portrait
	 */
	private Boolean cropPortrait;

	/**
	 * 图片质量分数（评价图片的模糊程度）
	 * 扩展响应：Quality
	 */
	private Boolean quality;

	/**
	 * 是否开启多卡证检测
	 * 检验出多张卡时，会抛出 {@link com.tencentcloudapi.common.exception.TencentCloudSDKException} 异常
	 */
	private Boolean multiCardDetect;

	/**
	 * 身份证有效日期不合法告警
	 * 警告码：-9100
	 */
	private Boolean invalidDateWarn;

	/**
	 * 边框和框内遮挡告警
	 * 警告码：-9101：边框不完整，或边框有遮挡
	 * 警告码：-9105：框内有遮挡
	 */
	private Boolean borderCheckWarn;

	/**
	 * 复印件告警
	 * 警告码：-9102
	 */
	private Boolean copyWarn;

	/**
	 * 翻拍告警
	 * 警告码：-9103
	 */
	private Boolean reshootWarn;

	/**
	 * 临时身份证告警
	 * 警告码：-9105
	 */
	private Boolean tempIdWarn;

	/**
	 * PS检测告警
	 * 警告码：-9106
	 */
	private Boolean detectPsWarn;


	/**
	 * 转换为接口所需的JSON格式
	 *
	 * @return JSON数据
	 */
	@Nullable
	public String toJson() {
		StringBuilder sb = new StringBuilder("{");
		if (Boolean.TRUE.equals(cropIdCard)) {
			sb.append("\"CropIdCard\":true,");
		}
		if (Boolean.TRUE.equals(cropPortrait)) {
			sb.append("\"CropPortrait\":true,");
		}
		if (Boolean.TRUE.equals(quality)) {
			sb.append("\"Quality\":true,");
		}
		if (Boolean.TRUE.equals(multiCardDetect)) {
			sb.append("\"MultiCardDetect\":true,");
		}
		if (Boolean.TRUE.equals(invalidDateWarn)) {
			sb.append("\"InvalidDateWarn\":true,");
		}
		if (Boolean.TRUE.equals(borderCheckWarn)) {
			sb.append("\"BorderCheckWarn\":true,");
		}
		if (Boolean.TRUE.equals(copyWarn)) {
			sb.append("\"CopyWarn\":true,");
		}
		if (Boolean.TRUE.equals(reshootWarn)) {
			sb.append("\"ReshootWarn\":true,");
		}
		if (Boolean.TRUE.equals(tempIdWarn)) {
			sb.append("\"TempIdWarn\":true,");
		}
		if (Boolean.TRUE.equals(detectPsWarn)) {
			sb.append("\"DetectPsWarn\":true,");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
			return sb.toString();
		} else {
			return null;
		}
	}


	//region Getter、Setter

	public Boolean getCropIdCard() {
		return cropIdCard;
	}

	public void setCropIdCard(Boolean cropIdCard) {
		this.cropIdCard = cropIdCard;
	}

	public Boolean getCropPortrait() {
		return cropPortrait;
	}

	public void setCropPortrait(Boolean cropPortrait) {
		this.cropPortrait = cropPortrait;
	}

	public Boolean getQuality() {
		return quality;
	}

	public void setQuality(Boolean quality) {
		this.quality = quality;
	}

	public Boolean getMultiCardDetect() {
		return multiCardDetect;
	}

	public void setMultiCardDetect(Boolean multiCardDetect) {
		this.multiCardDetect = multiCardDetect;
	}

	public Boolean getInvalidDateWarn() {
		return invalidDateWarn;
	}

	public void setInvalidDateWarn(Boolean invalidDateWarn) {
		this.invalidDateWarn = invalidDateWarn;
	}

	public Boolean getBorderCheckWarn() {
		return borderCheckWarn;
	}

	public void setBorderCheckWarn(Boolean borderCheckWarn) {
		this.borderCheckWarn = borderCheckWarn;
	}

	public Boolean getCopyWarn() {
		return copyWarn;
	}

	public void setCopyWarn(Boolean copyWarn) {
		this.copyWarn = copyWarn;
	}

	public Boolean getReshootWarn() {
		return reshootWarn;
	}

	public void setReshootWarn(Boolean reshootWarn) {
		this.reshootWarn = reshootWarn;
	}

	public Boolean getTempIdWarn() {
		return tempIdWarn;
	}

	public void setTempIdWarn(Boolean tempIdWarn) {
		this.tempIdWarn = tempIdWarn;
	}

	public Boolean getDetectPsWarn() {
		return detectPsWarn;
	}

	public void setDetectPsWarn(Boolean detectPsWarn) {
		this.detectPsWarn = detectPsWarn;
	}

	//endregion
}
