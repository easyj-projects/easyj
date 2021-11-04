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

/**
 * 过滤器无效的异常
 *
 * @author wangliang181230
 */
public class FilterInvalidException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FilterInvalidException(String message) {
		super(message);
	}

	public FilterInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilterInvalidException(Throwable cause) {
		super(cause);
	}

	public FilterInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
