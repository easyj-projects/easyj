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
 * Wrapper Exception.<br>
 * 包装异常，该异常类必须在一定范围内捕获并抛出cause。由于是比较特殊的异常类，请谨慎使用。
 *
 * @author wangliang181230
 */
public class WrapperException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public WrapperException(String message, Throwable cause) {
		super(message, cause);
		Assert.notNull(cause, "'cause' must be not null");
	}

	public WrapperException(Throwable cause) {
		super(cause);
		Assert.notNull(cause, "'cause' must be not null");
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		// do nothing
		return null;
	}
}
