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
package icu.easyj.core.exception;

import org.springframework.lang.Nullable;

/**
 * 通用异常
 *
 * @author wangliang181230
 */
public class BaseException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 错误码
	 */
	private final String errorCode;


	public BaseException(String message) {
		this(message, (String)null);
	}

	public BaseException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public BaseException(String message, Throwable cause) {
		this(message, null, cause);
	}

	public BaseException(String message, String errorCode, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}


	/**
	 * 获取错误码
	 *
	 * @return 错误码
	 */
	@Nullable
	public String getErrorCode() {
		return errorCode;
	}
}
