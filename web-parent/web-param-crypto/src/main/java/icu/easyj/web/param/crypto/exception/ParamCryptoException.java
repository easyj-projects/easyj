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
package icu.easyj.web.param.crypto.exception;

/**
 * 参数加密解密的异常
 *
 * @author wangliang181230
 */
public class ParamCryptoException extends RuntimeException {

	public ParamCryptoException() {
		super();
	}

	public ParamCryptoException(String message) {
		super(message);
	}

	public ParamCryptoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParamCryptoException(Throwable cause) {
		super(cause);
	}
}