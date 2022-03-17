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
package icu.easyj.middleware.dwz.server.core.task;

import icu.easyj.middleware.dwz.server.core.config.DwzServerTaskConfig;
import icu.easyj.middleware.dwz.server.core.domain.enums.OvertimeHandleStrategy;
import icu.easyj.middleware.dwz.server.core.store.IDwzLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * DWZ服务端定时任务
 *
 * @author wangliang181230
 */
public class EasyjDwzServerTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(EasyjDwzServerTask.class);


	private final IDwzLogStore dwzLogStore;

	private final DwzServerTaskConfig dwzServerTaskConfig;


	public EasyjDwzServerTask(IDwzLogStore dwzLogStore, DwzServerTaskConfig dwzServerTaskConfig) {
		this.dwzLogStore = dwzLogStore;
		this.dwzServerTaskConfig = dwzServerTaskConfig;
	}

	/**
	 * 删除超时的短链接记录
	 * <p>
	 * 每10分钟执行一次，项目启动后延迟5秒钟执行第一次。
	 */
	@Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 5 * 1000)
	public void handleOvertimeDwzLog() {
		if (dwzServerTaskConfig.getOvertimeHandleStrategy() == OvertimeHandleStrategy.DELETE) {
			int count = this.dwzLogStore.deleteOvertime();
			// 打印此次删除的数据量
			LOGGER.info("此次删除的超时短链接记录数据有 {} 条", count);
		} else {
			int count = this.dwzLogStore.updateOvertime();
			// 打印此次更新的数据量
			LOGGER.info("此次更新的超时短链接记录数据有 {} 条", count);
		}
	}
}
