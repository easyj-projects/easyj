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

import icu.easyj.core.util.EnumUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 身份证识别告警
 *
 * @author wangliang181230
 */
public enum IdCardOcrWarn {

	/**
	 * 有效日期不合法（未开始或已过期）
	 */
	INVALID_DATE(-100, "身份证有效日期不合法", 1),

	/**
	 * 证件已失效
	 */
	INVALID_ID(-101, "身份证已失效（挂失过、迁过户口等）", 1),

	/**
	 * 边框不完整
	 */
	BORDER_INCOMPLETE(-200, "身份证边框不完整", 2),

	/**
	 * 框内部分被遮挡
	 */
	IN_FRAME_COVERED(-201, "身份证边框内部分被遮挡", 2),

	/**
	 * 为复印件
	 */
	COPY(-300, "为身份证复印件", 3),

	/**
	 * 为翻拍照
	 */
	RESHOOT(-301, "为身份证翻拍照", 3),

	/**
	 * 为临时身份证
	 */
	TEMP(-400, "为临时身份证", 4),

	/**
	 * 为PS过的身份证
	 */
	PS(-500, "为PS过的身份证", 5),

	/**
	 * 为假的身份证（不确定云服务是否支持）
	 */
	FAKE(-501, "为假的身份证", 5);


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


	IdCardOcrWarn(int code, String msg, int type) {
		this.code = code;
		this.msg = msg;
		this.type = type;
	}


	//region Getter

	public int code() {
		return code;
	}

	public String msg() {
		return msg;
	}

	/**
	 * @return 告警类别：1=已失效；2=不完整的；3=为副本；4=临时的；5=伪造的
	 */
	public int type() {
		return type;
	}

	//endregion


	/**
	 * 根据告警码获取枚举
	 *
	 * @param warnCode 告警码
	 * @return warnEnum 告警枚举
	 */
	@Nullable
	public static IdCardOcrWarn get(int warnCode) {
		for (IdCardOcrWarn warn : IdCardOcrWarn.values()) {
			if (warnCode == warn.code()) {
				return warn;
			}
		}
		return null;
	}

	/**
	 * 根据枚举名获取枚举
	 *
	 * @param name 枚举名
	 * @return warnEnum 告警枚举
	 */
	@NonNull
	public static IdCardOcrWarn get(String name) {
		return EnumUtils.fromName(IdCardOcrWarn.class, name);
	}
}
