/*
 * Copyright 2021-2024 the original author or authors.
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

/**
 * Skip Callback Wrapper Exception.<br>
 * 跳过回调函数的包装异常。<br>
 * 注意：仅仅是为了跳过函数的异常类型限制而使用，必须在函数外捕获该异常并抛出cause
 *
 * @author wangliang181230
 */
public class SkipCallbackWrapperException extends WrapperException {
	private static final long serialVersionUID = 1L;

	public SkipCallbackWrapperException(String message, Throwable cause) {
		super(message, cause);
	}

	public SkipCallbackWrapperException(Throwable cause) {
		super(cause);
	}
}
