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
package icu.easyj.sdk.s3.dwz;

import icu.easyj.core.util.EnumUtils;
import org.springframework.lang.Nullable;

/**
 * S-3的短链接服务错误枚举
 *
 * @author wangliang181230
 */
public enum S3DwzErrorType {

	/**
	 * 参数错误
	 */
	PARAM_ERROR("40001", "参数错误"), // 具体msg见返回信息

	/**
	 * 密钥错误
	 */
	SIGNATURE_ERROR("40002", "不合法的client_secret"),

	/**
	 * 不合法的client_id
	 */
	INVALID_CLIENT_ID("40003", "不合法的client_id"),

	/**
	 * 域名权限异常
	 */
	DOMAIN_AUTH_ERROR("40005", "域名权限错误，不在白名单中"), // 操作失败,请检查接口权限,如域名白名单等

	/**
	 * 参数错误
	 */
	SYS_ERROR("50000", "系统错误"),

	;

	/**
	 * 错误代码
	 */
	private final String code;

	/**
	 * 错误描述
	 */
	private final String desc;

	S3DwzErrorType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}


	//region Getter

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	//endregion

	/**
	 * 根据code获取枚举
	 *
	 * @param code 错误代码
	 * @return 枚举
	 */
	@Nullable
	public static S3DwzErrorType getByCode(String code) {
		return EnumUtils.match(S3DwzErrorType.class, e -> e.getCode().equalsIgnoreCase(code));
	}
}
