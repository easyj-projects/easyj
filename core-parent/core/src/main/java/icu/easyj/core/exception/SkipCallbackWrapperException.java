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
package icu.easyj.core.exception;

import org.springframework.util.Assert;

/**
 * Skip Callback Wrapper Exception.
 * 仅仅是为了跳过函数的异常类型限制而使用，必须在函数外捕获该异常并抛出cause
 *
 * @author wangliang181230
 */
public class SkipCallbackWrapperException extends WrapperException {

	public SkipCallbackWrapperException(String message, Throwable cause) {
		super(message, cause);
		Assert.notNull(cause, "'cause' must be not null");
	}

	public SkipCallbackWrapperException(Throwable cause) {
		super(cause);
		Assert.notNull(cause, "'cause' must be not null");
	}
}
