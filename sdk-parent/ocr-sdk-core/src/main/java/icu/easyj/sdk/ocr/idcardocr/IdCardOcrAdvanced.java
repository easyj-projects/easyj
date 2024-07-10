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

/**
 * 高级功能枚举
 *
 * @author wangliang181230
 * @see IdCardOcrWarn
 */
public enum IdCardOcrAdvanced {

	/**
	 * 裁剪身份证图片，去掉边框外的内容，并自动矫正拍摄角度
	 */
	CROP_ID_CARD,

	/**
	 * 裁剪人像，自动矫正拍摄角度
	 */
	CROP_PORTRAIT,

	/**
	 * 检查是否已过期（有效期限未开始或已过期）
	 *
	 * @see IdCardOcrWarn#INVALID_DATE
	 * @see IdCardOcrWarn#INVALID_ID
	 */
	DETECT_INVALID,

	/**
	 * 检查是否为不完整图片（如：边框或框内有遮挡等）
	 *
	 * @see IdCardOcrWarn#BORDER_INCOMPLETE
	 * @see IdCardOcrWarn#IN_FRAME_COVERED
	 */
	DETECT_INCOMPLETE,

	/**
	 * 检查图片是否清晰（如：模糊的、有反光的）
	 *
	 * @see IdCardOcrWarn#VAGUE
	 * @see IdCardOcrWarn#REFLECT
	 */
	DETECT_DEFINITION,

	/**
	 * 检查是否为身份证的副本（如：复印件、翻拍照片等）
	 *
	 * @see IdCardOcrWarn#COPY
	 * @see IdCardOcrWarn#RESHOOT
	 */
	DETECT_COPY,

	/**
	 * 检查是否临时身份证
	 *
	 * @see IdCardOcrWarn#TEMP
	 */
	DETECT_TEMP,

	/**
	 * 检查是否伪造的（如：PS过的、假证等等）
	 *
	 * @see IdCardOcrWarn#PS
	 * @see IdCardOcrWarn#FAKE
	 */
	DETECT_FAKE;


	/**
	 * 不含任何枚举的数组
	 */
	public final static IdCardOcrAdvanced[] EMPTY_ARRAY = new IdCardOcrAdvanced[0];

	/**
	 * 包含所有枚举的数组
	 */
	public final static IdCardOcrAdvanced[] ALL_ARRAY = IdCardOcrAdvanced.values();

	/**
	 * 包含所有裁剪的枚举的数组
	 */
	public final static IdCardOcrAdvanced[] ALL_CROP_ARRAY = new IdCardOcrAdvanced[]{
			CROP_ID_CARD,
			CROP_PORTRAIT};

	/**
	 * 包含所有检查枚举的数组
	 */
	public final static IdCardOcrAdvanced[] ALL_DETECT_ARRAY = new IdCardOcrAdvanced[]{
			DETECT_INVALID,
			DETECT_INCOMPLETE,
			DETECT_DEFINITION,
			DETECT_COPY,
			DETECT_TEMP,
			DETECT_FAKE};
}
