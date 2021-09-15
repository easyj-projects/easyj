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

import org.springframework.lang.Nullable;

/**
 * 转换器未找到的异常
 *
 * @author wangliang181230
 */
public class ConverterNotFoundException extends BaseRuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * 源类型
	 */
	private Class<?> sourceType;

	/**
	 * 目标类型
	 */
	private Class<?> targetType;


	public ConverterNotFoundException(String message) {
		super(message, "CONVERTER_NOT_FOUND");
	}

	public ConverterNotFoundException(String message, String errorCode) {
		super(message, errorCode);
	}

	public ConverterNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConverterNotFoundException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}

	public ConverterNotFoundException(String message, Class<?> sourceType, Class<?> targetType) {
		this(message);
		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	public ConverterNotFoundException(Class<?> sourceType, Class<?> targetType) {
		this("未找到 `" + sourceType.getName() + "` 转换为 `" + targetType.getName() + "` 的转换器", sourceType, targetType);
	}


	//region Getter

	@Nullable
	public Class<?> getSourceType() {
		return sourceType;
	}

	@Nullable
	public Class<?> getTargetType() {
		return targetType;
	}

	//endregion
}
