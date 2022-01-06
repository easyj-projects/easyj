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

import icu.easyj.middleware.dwz.server.core.domain.entity.DwzLogEntity;

/**
 * 超时短链接记录的处理策略
 *
 * @author wangliang181230
 */
public enum OvertimeHandleStrategy {

	/**
	 * 删除策略
	 */
	DELETE,

	/**
	 * 更新策略：更新状态为已过期
	 *
	 * @see DwzLogEntity#getStatus()
	 */
	UPDATE;
}
