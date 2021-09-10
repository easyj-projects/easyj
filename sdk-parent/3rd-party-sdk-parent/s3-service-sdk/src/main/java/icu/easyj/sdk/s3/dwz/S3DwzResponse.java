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
package icu.easyj.sdk.s3.dwz;

import org.springframework.lang.Nullable;

/**
 * @author wangliang181230
 */
public class S3DwzResponse {

	private String code;

	private String message;

	private S3DwzResponseData data;


	/**
	 * @return 是否请求成功
	 */
	public boolean isSuccess() {
		return "0".equals(code);
	}

	/**
	 * @return 错误类型枚举
	 */
	@Nullable
	public S3DwzErrorType getErrorType() {
		return S3DwzErrorType.getByCode(this.code);
	}

	/**
	 * @return 错误信息
	 */
	public String getErrorMessage() {
		S3DwzErrorType errorType = getErrorType();
		return getErrorMessage(errorType);
	}

	/**
	 * 获取错误信息
	 *
	 * @param errorType 错误类型枚举
	 * @return 错误信息
	 */
	public String getErrorMessage(@Nullable S3DwzErrorType errorType) {
		if (errorType != null) {
			return errorType.getDesc();
		}

		return message;
	}


	//region Getter、Setter

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public S3DwzResponseData getData() {
		return data;
	}

	public void setData(S3DwzResponseData data) {
		this.data = data;
	}

	//endregion
}
