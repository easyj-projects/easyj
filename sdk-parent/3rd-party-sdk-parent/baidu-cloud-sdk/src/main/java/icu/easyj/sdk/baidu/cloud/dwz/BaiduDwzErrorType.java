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
package icu.easyj.sdk.baidu.cloud.dwz;

import icu.easyj.core.util.EnumUtils;
import org.springframework.lang.Nullable;

/**
 * 百度云DWZ服务错误类型枚举
 *
 * @author wangliang181230
 */
public enum BaiduDwzErrorType {

	/**
	 * 枚举
	 */
	INVALID_LONG_URL(-10, "无效长网址"),
	UN_SUPPORTED(-11, "长网址不支持缩短"),
	HAS_SAFETY_RISKS(-13, "长网址可能存在安全风险，可申请 安全审核 解决"),
	EXCESSIVE_LONG_URL(-14, "长网址数量过多"),
	INVALID_TERM_OF_VALIDITY(-20, "有效期设置错误"),
	LONG_TERM_LIMIT(-21, "长期有效额度不足"),
	NO_POINTS(-30, "可用点数不足"),
	INVALID_DOMAIN(-91, "域名设置错误"),
	PART_LONG_URL_ERROR(-99, "部分长网址缩短失败"),
	INVALID_TOKEN(-100, "Token验证失败"),
	NEED_ENTERPRISE_AUTH(-114, "需完成企业实名认证"),
	;


	private final int code;
	private final String desc;

	BaiduDwzErrorType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}


	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}


	/**
	 * 根据code获取枚举
	 *
	 * @param code 错误代码
	 * @return 枚举
	 */
	@Nullable
	public static BaiduDwzErrorType getByCode(Integer code) {
		if (code == null) {
			return null;
		}
		return EnumUtils.match(BaiduDwzErrorType.class, e -> e.getCode() == code);
	}
}