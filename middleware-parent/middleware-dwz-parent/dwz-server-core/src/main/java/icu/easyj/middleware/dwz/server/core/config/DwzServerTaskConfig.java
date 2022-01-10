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
package icu.easyj.middleware.dwz.server.core.config;

import icu.easyj.middleware.dwz.server.core.domain.enums.OvertimeHandleStrategy;

/**
 * 短链接服务端定时任务配置
 *
 * @author wangliang181230
 */
public class DwzServerTaskConfig {

	/**
	 * 是否启用定时任务（多实例部署时，建议只在一个实例上启用定时任务；等以后集成任务调度后，再多实例全部开启）
	 */
	private boolean enabled = true;

	/**
	 * 超时失效短链接记录的处理策略
	 */
	private OvertimeHandleStrategy overtimeHandleStrategy = OvertimeHandleStrategy.DELETE;


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public OvertimeHandleStrategy getOvertimeHandleStrategy() {
		return overtimeHandleStrategy;
	}

	public void setOvertimeHandleStrategy(OvertimeHandleStrategy overtimeHandleStrategy) {
		this.overtimeHandleStrategy = overtimeHandleStrategy;
	}
}
