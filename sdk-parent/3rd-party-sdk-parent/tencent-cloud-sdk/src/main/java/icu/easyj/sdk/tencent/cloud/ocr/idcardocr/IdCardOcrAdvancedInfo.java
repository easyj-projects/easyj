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
package icu.easyj.sdk.tencent.cloud.ocr.idcardocr;

import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.StringUtils;
import org.springframework.lang.Nullable;

/**
 * 身份证识别（IDCardOCR）高级响应信息
 *
 * @author wangliang181230
 * @see IDCardOCRResponse#getAdvancedInfo()
 */
public class IdCardOcrAdvancedInfo {

	/**
	 * 裁剪后的身份证照片Base64串（注：去掉证件外多余的边缘、自动矫正拍摄角度）<br>
	 * `CropIdCard=true`时，才会返回。
	 */
	@SerializedName("IdCard")
	@Expose
	private String idCardBase64;

	/**
	 * 裁剪出的人像照片Base64串（抠取身份证头像区域）<br>
	 * `CropPortrait=true`时，才会返回。
	 */
	@SerializedName("Portrait")
	@Expose
	private String portraitBase64;

	/**
	 * 图片质量分数，请求 Config.Quality 时返回（取值范围：0~100，分数越低越模糊，建议阈值≥50）;<br>
	 * `Quality=true`时，才会返回。
	 */
	@SerializedName("Quality")
	@Expose
	private Integer quality;

	/**
	 * 身份证边框不完整告警阈值分数，请求 Config.BorderCheckWarn时返回（取值范围：0~100，分数越低边框遮挡可能性越低，建议阈值≥50）;<br>
	 * `BorderCheckWarn=true`时，才会返回。
	 */
	@SerializedName("BorderCodeValue")
	@Expose
	private Integer borderCodeValue;

	/**
	 * 告警信息，Code 告警码列表和释义如下：
	 * -9100	身份证有效日期不合法告警；
	 * -9101	身份证边框不完整告警；
	 * -9102	身份证复印件告警；
	 * -9103	身份证翻拍告警；
	 * -9104	临时身份证告警；
	 * -9105	身份证框内遮挡告警；
	 * -9106	身份证 PS 告警。
	 */
	@SerializedName("WarnInfos")
	@Expose
	private Set<Integer> warnInfos;


	//region 判断告警信息

	/**
	 * 判断是否不含任意告警
	 *
	 * @return 是否不含任意告警
	 */
	public boolean hasNoWarn() {
		return CollectionUtils.isEmpty(warnInfos);
	}

	/**
	 * 判断是否包含任意告警
	 *
	 * @return 是否包含任意告警
	 */
	public boolean hasAnyWarn() {
		return !hasNoWarn();
	}

	/**
	 * 判断是否包含指定告警
	 *
	 * @param warnCode 告警码
	 * @return 是否包含指定告警
	 */
	private boolean hasWarn(int warnCode) {
		if (this.hasNoWarn()) {
			return false;
		}
		return warnInfos.contains(warnCode);
	}

	/**
	 * 判断是否包含有效日期不合法告警
	 *
	 * @return 是否包含有效日期不合法告警
	 */
	public boolean hasInvalidDateWarn() {
		return this.hasWarn(-9100);
	}

	/**
	 * 判断是否包含身份证边框不完整告警
	 *
	 * @return 是否包含身份证边框不完整告警
	 */
	public boolean hasBorderIncompleteWarn() {
		return this.hasWarn(-9101);
	}

	/**
	 * 判断是否包含身份证复印件告警
	 *
	 * @return 是否包含身份证复印件告警
	 */
	public boolean hasCopyWarn() {
		return this.hasWarn(-9102);
	}

	/**
	 * 判断是否包含身份证翻拍告警
	 *
	 * @return 是否包含身份证翻拍告警
	 */
	public boolean hasReshootWarn() {
		return this.hasWarn(-9103);
	}

	/**
	 * 判断是否包含临时身份证告警
	 *
	 * @return 是否包含临时身份证告警
	 */
	public boolean hasTempIdWarn() {
		return this.hasWarn(-9104);
	}

	/**
	 * 判断是否包含身份证框内遮挡告警
	 *
	 * @return 是否包含身份证框内遮挡告警
	 */
	public boolean hasInFrameCoveredWarn() {
		return this.hasWarn(-9105);
	}

	/**
	 * 判断是否包含PS告警
	 *
	 * @return 是否包含PS告警
	 */
	public boolean hasDetectPsWarn() {
		return this.hasWarn(-9106);
	}

	//endregion


	//region Converter（转换）

	/**
	 * 转换身份证识别（IDCardOCR）的扩展信息JSON串为对象
	 *
	 * @param advanceInfoJson 扩展信息JSON串
	 * @return advancedInfo 扩展信息
	 */
	@Nullable
	public static IdCardOcrAdvancedInfo fromJsonString(String advanceInfoJson) {
		if (StringUtils.isBlank(advanceInfoJson) || advanceInfoJson.length() == 2) { // length=2，即{}
			return null;
		}
		return IDCardOCRResponse.fromJsonString(advanceInfoJson, IdCardOcrAdvancedInfo.class);
	}

	//endregion


	//region Getter、Setter

	public String getIdCardBase64() {
		return idCardBase64;
	}

	public void setIdCardBase64(String idCardBase64) {
		this.idCardBase64 = idCardBase64;
	}

	public String getPortraitBase64() {
		return portraitBase64;
	}

	public void setPortraitBase64(String portraitBase64) {
		this.portraitBase64 = portraitBase64;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getBorderCodeValue() {
		return borderCodeValue;
	}

	public void setBorderCodeValue(Integer borderCodeValue) {
		this.borderCodeValue = borderCodeValue;
	}

	public Set<Integer> getWarnInfos() {
		return warnInfos;
	}

	public void setWarnInfos(Set<Integer> warnInfos) {
		this.warnInfos = warnInfos;
	}

	//endregion
}
