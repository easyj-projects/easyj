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
package icu.easyj.sdk.ocr.tencent.cloud.idcardocr;

import org.springframework.lang.Nullable;

/**
 * 身份证识别告警
 *
 * @author wangliang181230
 */
public enum TencentIdCardOcrWarn {

	/**
	 * 有效日期不合法
	 */
	INVALID_DATE(-9100, "身份证有效日期不合法", 1),

	/**
	 * 边框不完整
	 */
	BORDER_INCOMPLETE(-9101, "身份证边框不完整", 2),

	/**
	 * 框内部分被遮挡
	 */
	IN_FRAME_COVERED(-9105, "身份证边框内部分被遮挡", 2),

	/**
	 * 为复印件
	 */
	COPY(-9102, "为身份证复印件", 3),

	/**
	 * 为翻拍照
	 */
	RESHOOT(-9103, "为身份证翻拍照", 3),

	/**
	 * 为临时身份证
	 */
	TEMP(-9104, "为临时身份证", 4),

	/**
	 * 为PS过的身份证
	 */
	PS(-9106, "为PS过的身份证", 5);

	/**
	 * 告警码
	 */
	private final int code;

	/**
	 * 告警信息
	 */
	private final String msg;

	/**
	 * 告警类别：1=已失效；2=不完整的；3=为副本；4=临时的；5=伪造的
	 */
	private final int type;

	TencentIdCardOcrWarn(int code, String msg, int type) {
		this.code = code;
		this.msg = msg;
		this.type = type;
	}

	public int code() {
		return code;
	}

	public String msg() {
		return msg;
	}

	public int type() {
		return type;
	}

	/**
	 * 根据告警码获取枚举
	 *
	 * @param warnCode 告警码
	 * @return warnEnum 告警枚举
	 */
	@Nullable
	public static TencentIdCardOcrWarn get(int warnCode) {
		for (TencentIdCardOcrWarn warn : TencentIdCardOcrWarn.values()) {
			if (warnCode == warn.code()) {
				return warn;
			}
		}
		return null;
	}
}
