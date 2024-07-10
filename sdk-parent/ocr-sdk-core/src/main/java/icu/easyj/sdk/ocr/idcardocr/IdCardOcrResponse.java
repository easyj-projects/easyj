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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import icu.easyj.core.util.StringUtils;
import icu.easyj.sdk.ocr.CardSide;
import org.springframework.lang.NonNull;

/**
 * 身份证识别响应内容
 *
 * @author wangliang181230
 */
public class IdCardOcrResponse {

	/**
	 * 身份证图片正反面枚举
	 */
	private CardSide cardSide;


	//region 正面信息（人像面）

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private String sex;

	/**
	 * 民族
	 */
	private String nation;

	/**
	 * 出生日期
	 */
	private Date birthday;

	/**
	 * 住址（户籍地址）
	 */
	private String address;

	/**
	 * 身份证号
	 */
	private String idNum;

	//endregion


	//region 反面信息（国徽面）

	/**
	 * 签发机关
	 * 如：宁波市公安局
	 */
	private String authority;

	/**
	 * 有效期限-起始
	 */
	private Date validDateStart;

	/**
	 * 有效期限-截止，截止到该日期的24点
	 * 注：为空时，表达长期有效
	 */
	private Date validDateEnd;

	//endregion


	//region 高级功能返回的信息（需useAdvanced参数为true）（注意：不同的接口实现，可能会有不同的结果，需要注意以下信息的使用）

	/**
	 * 裁剪出的身份证图片Base64串（去掉身份证外的内容，并自动矫正拍摄角度后的图片）
	 * 各服务支持情况初步调查结果（如果以下结果错误，请修正，谢谢）：
	 * 腾讯云：支持
	 * 阿里云：不支持
	 */
	private String idCardBase64;

	/**
	 * 备用属性，同时解析正反面两张图片，并且启用了CROP_ID_CARD功能时，此属性才会有值。
	 * 注：此属性的get方法特殊处理过，可以放心获取此属性的有效值：{@link this#getBackIdCardBase64()}
	 */
	private String backIdCardBase64;

	/**
	 * 裁剪出的人像图片Base64串（自动矫正拍摄角度后的图片）
	 * 各服务支持情况初步调查结果（如果以下结果错误，请修正，谢谢）：
	 * 腾讯云：支持
	 * 阿里云：支持，但返回的是人像的坐标和宽高，需要接口实现中自行处理
	 */
	private String portraitBase64;

	/**
	 * 告警信息
	 * 部分告警功能不需要传高级功能参数，也会返回告警枚举
	 */
	@NonNull
	private final Set<IdCardOcrWarn> warns = new HashSet<>();

	//endregion


	//region Getter、Setter

	public CardSide getCardSide() {
		return cardSide;
	}

	public void setCardSide(CardSide cardSide) {
		this.cardSide = cardSide;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Date getValidDateStart() {
		return validDateStart;
	}

	public void setValidDateStart(Date validDateStart) {
		this.validDateStart = validDateStart;
	}

	public Date getValidDateEnd() {
		return validDateEnd;
	}

	public void setValidDateEnd(Date validDateEnd) {
		this.validDateEnd = validDateEnd;
	}

	public String getIdCardBase64() {
		return idCardBase64;
	}

	public void setIdCardBase64(String idCardBase64) {
		this.idCardBase64 = idCardBase64;
	}

	public String getBackIdCardBase64() {
		if (StringUtils.isEmpty(backIdCardBase64) && cardSide == CardSide.BACK) {
			return idCardBase64;
		}
		return backIdCardBase64;
	}

	public void setBackIdCardBase64(String backIdCardBase64) {
		this.backIdCardBase64 = backIdCardBase64;
	}

	public String getPortraitBase64() {
		return portraitBase64;
	}

	public void setPortraitBase64(String portraitBase64) {
		this.portraitBase64 = portraitBase64;
	}

	@NonNull
	public Set<IdCardOcrWarn> getWarns() {
		return warns;
	}

	public void addWarns(IdCardOcrWarn warn) {
		this.warns.add(warn);
	}

	//endregion
}
