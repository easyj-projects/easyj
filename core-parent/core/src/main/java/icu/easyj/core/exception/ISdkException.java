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

import java.io.Serializable;

import org.springframework.lang.Nullable;

/**
 * SDK相关异常
 *
 * @author wangliang181230
 */
public interface ISdkException extends Serializable {

	/**
	 * 获取异常信息
	 *
	 * @return 异常信息
	 */
	@Nullable
	String getMessage();

	/**
	 * 获取局部异常信息
	 *
	 * @return 局部异常信息
	 */
	@Nullable
	String getLocalizedMessage();

	/**
	 * 获取异常原因
	 *
	 * @return 异常原因
	 */
	@Nullable
	Throwable getCause();
}
