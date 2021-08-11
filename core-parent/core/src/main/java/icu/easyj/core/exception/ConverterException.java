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

/**
 * 转换失败的异常
 *
 * @author wangliang181230
 */
public class ConverterException extends RuntimeException {

	public ConverterException() {
		super();
	}

	public ConverterException(String message) {
		super(message);
	}

	public ConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConverterException(Throwable cause) {
		super(cause);
	}

	public ConverterException(String fromType, String toType) {
		super("`" + fromType + "`转换为`" + toType + "`失败");
	}

	public ConverterException(Class<?> fromType, Class<?> toType) {
		this(fromType.getName(), toType.getName());
	}
}
