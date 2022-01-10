/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.middleware.dwz.server.core.domain.enums;

/**
 * 短链接记录状态枚举
 *
 * @author wangliang181230
 */
public enum DwzLogStatus {

	/**
	 * 无效的
	 */
	INVALID(0),

	/**
	 * 有效的
	 */
	EFFECTIVE(1),

	/**
	 * 已过期的
	 */
	Expired(2),

	;


	private final int status;


	DwzLogStatus(int status) {
		this.status = status;
	}


	public int getStatus() {
		return status;
	}
}
