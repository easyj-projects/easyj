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
package icu.easyj.core.loader;

/**
 * The type Enhanced service not found exception.
 * 注：从阿里的seata项目中复制过来的。
 *
 * @author slievrly
 */
public class EnhancedServiceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new Enhanced service not found exception.
	 *
	 * @param message the message
	 */
	public EnhancedServiceNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Enhanced service not found exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public EnhancedServiceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new Enhanced service not found exception.
	 *
	 * @param cause the cause
	 */
	public EnhancedServiceNotFoundException(Throwable cause) {
		super(cause);
	}
}
