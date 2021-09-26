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
package icu.easyj.sdk.ocr.idcardocr;

import icu.easyj.sdk.ocr.OcrSdkServerException;

/**
 * 身份证识别（IdCardOCR）SDK服务端异常
 *
 * @author wangliang181230
 */
public class IdCardOcrSdkServerException extends OcrSdkServerException {
	private static final long serialVersionUID = 1L;

	public IdCardOcrSdkServerException(String message) {
		super(message);
	}

	public IdCardOcrSdkServerException(String message, String errorCode) {
		super(message, errorCode);
	}

	public IdCardOcrSdkServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public IdCardOcrSdkServerException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}
