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
package icu.easyj.middleware.dwz.server.core.listener;

import icu.easyj.middleware.dwz.server.core.service.IDwzCorrectErrorDataService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 项目启动完成的监听器
 *
 * @author wangliang181230
 */
public class DwzServerStartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	private final IDwzCorrectErrorDataService dwzCorrectErrorDataService;


	public DwzServerStartupApplicationListener(IDwzCorrectErrorDataService dwzCorrectErrorDataService) {
		this.dwzCorrectErrorDataService = dwzCorrectErrorDataService;
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 执行一次纠正序列值的服务
		dwzCorrectErrorDataService.correctSequence();
	}
}
