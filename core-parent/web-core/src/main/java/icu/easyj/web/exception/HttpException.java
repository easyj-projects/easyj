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
package icu.easyj.web.exception;

import icu.easyj.core.exception.BaseRuntimeException;

/**
 * HTTP请求相关异常
 *
 * @author wangliang181230
 */
public class HttpException extends BaseRuntimeException {

	private int status;

	private String content;


	public HttpException(int status, String message) {
		super(message);
		this.status = status;
	}

	public HttpException(int status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
	}

	public HttpException(int status, String content, String message) {
		super(message);
		this.status = status;
		this.content = content;
	}

	public HttpException(int status, String content, String message, String errorCode) {
		super(message, errorCode);
		this.status = status;
		this.content = content;
	}

	public HttpException(int status, String content, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
		this.content = content;
	}

	public HttpException(int status, String content, String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
		this.status = status;
		this.content = content;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
