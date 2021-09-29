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
package icu.easyj.sdk.dwz;

import icu.easyj.core.exception.ISdkClientException;

/**
 * 短链接服务（DWZ）SDK客户端异常
 *
 * @author wangliang181230
 */
public class DwzSdkClientException extends DwzSdkException implements ISdkClientException {
	private static final long serialVersionUID = 1L;

	public DwzSdkClientException(String message) {
		super(message);
	}

	public DwzSdkClientException(String message, String errorCode) {
		super(message, errorCode);
	}

	public DwzSdkClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public DwzSdkClientException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}