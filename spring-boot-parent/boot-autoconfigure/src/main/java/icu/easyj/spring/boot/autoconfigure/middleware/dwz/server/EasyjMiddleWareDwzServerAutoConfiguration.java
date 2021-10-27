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
package icu.easyj.spring.boot.autoconfigure.middleware.dwz.server;

import javax.sql.DataSource;

import icu.easyj.config.ServerConfigs;
import icu.easyj.middleware.dwz.server.core.config.DwzServerTaskConfig;
import icu.easyj.middleware.dwz.server.core.service.IDwzServerService;
import icu.easyj.middleware.dwz.server.core.service.impls.DefaultDwzServerServiceImpl;
import icu.easyj.middleware.dwz.server.core.store.IDwzLogStore;
import icu.easyj.middleware.dwz.server.core.store.IDwzShortCodeStore;
import icu.easyj.middleware.dwz.server.core.store.impls.db.DataBaseDwzLogStoreImpl;
import icu.easyj.middleware.dwz.server.core.store.impls.db.DataBaseDwzShortCodeStoreImpl;
import icu.easyj.middleware.dwz.server.core.task.EasyjDwzServerTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * EasyJ中间件：DWZ（短链接服务）服务端自动装配类
 *
 * @author wangliang181230
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan("icu.easyj.middleware.dwz.server.core.controller")
public class EasyjMiddleWareDwzServerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public IDwzLogStore dataBaseDwzLogStore(DataSource dataSource, JdbcTemplate jdbcTemplate) {
		return new DataBaseDwzLogStoreImpl(dataSource, jdbcTemplate, ServerConfigs.getSnowflake());
	}

	@Bean
	@ConditionalOnMissingBean
	public IDwzShortCodeStore dataBaseDwzShortCodeStore(DataSource dataSource) {
		return new DataBaseDwzShortCodeStoreImpl(dataSource, ServerConfigs.getSnowflake());
	}

	@Bean
	@ConditionalOnMissingBean
	public IDwzServerService defaultDwzServerService(IDwzLogStore logStore, IDwzShortCodeStore shortCodeStore) {
		return new DefaultDwzServerServiceImpl(logStore, shortCodeStore);
	}


	/**
	 * 定时任务相关配置
	 */
	@Configuration(proxyBeanMethods = false)
	@EnableScheduling
	@ConditionalOnProperty(value = "easyj.middleware.dwz.server.task.enabled", matchIfMissing = true)
	public static class EasyjDwzServerTaskConfiguration {

		@Bean
		@ConfigurationProperties("easyj.middleware.dwz.server.task")
		public DwzServerTaskConfig dwzServerTaskProperties() {
			return new DwzServerTaskConfig();
		}

		@Bean
		@ConditionalOnMissingBean
		public EasyjDwzServerTask easyjDwzServerTask(IDwzLogStore dwzLogStore, DwzServerTaskConfig dwzServerTaskConfig) {
			return new EasyjDwzServerTask(dwzLogStore, dwzServerTaskConfig);
		}
	}
}
