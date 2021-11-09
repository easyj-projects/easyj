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

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.middleware.dwz.server.core.config.DwzServerTaskConfig;
import icu.easyj.middleware.dwz.server.core.controller.DwzRedirectController;
import icu.easyj.middleware.dwz.server.core.controller.DwzRestController;
import icu.easyj.middleware.dwz.server.core.listener.DwzServerStartupApplicationListener;
import icu.easyj.middleware.dwz.server.core.service.IDwzCorrectErrorDataService;
import icu.easyj.middleware.dwz.server.core.service.IDwzServerService;
import icu.easyj.middleware.dwz.server.core.service.impls.DefaultDwzCorrectErrorDataServiceImpl;
import icu.easyj.middleware.dwz.server.core.service.impls.DefaultDwzServerServiceImpl;
import icu.easyj.middleware.dwz.server.core.store.IDwzLogStore;
import icu.easyj.middleware.dwz.server.core.store.impls.db.DataBaseDwzLogStoreImpl;
import icu.easyj.middleware.dwz.server.core.store.impls.mock.MockDwzLogStoreImpl;
import icu.easyj.middleware.dwz.server.core.task.EasyjDwzServerTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * EasyJ中间件：DWZ（短链接服务）服务端自动装配类
 *
 * @author wangliang181230
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DwzRestController.class)
@ConditionalOnProperty(value = "easyj.middleware.dwz.server.enabled", matchIfMissing = true)
@ConditionalOnWebApplication
@Import({DwzRestController.class, DwzRedirectController.class})
public class EasyjMiddleWareDwzServerAutoConfiguration {

	/**
	 * 创建：短链接记录存取接口Bean（基于数据库）（默认）
	 *
	 * @param primaryJdbcTemplate 主要数据源对应的jdbcTemplate
	 * @param sequenceService     序列服务
	 * @return 短链接记录存取接口Bean
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "easyj.middleware.dwz.server.log-store.type", havingValue = "db", matchIfMissing = true)
	public IDwzLogStore dataBaseDwzLogStore(JdbcTemplate primaryJdbcTemplate, ISequenceService sequenceService) {
		return new DataBaseDwzLogStoreImpl(primaryJdbcTemplate, sequenceService);
	}

	/**
	 * 创建：短链接记录存取接口Bean（模拟）
	 *
	 * @param sequenceService 序列服务
	 * @return 短链接记录存取接口Bean
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "easyj.middleware.dwz.server.log-store.type", havingValue = "mock")
	public IDwzLogStore mockDwzLogStore(ISequenceService sequenceService) {
		return new MockDwzLogStoreImpl(sequenceService);
	}

	/**
	 * 创建：短链接服务接口Bean
	 *
	 * @param logStore 短链接记录存取接口Bean
	 * @return 短链接服务接口Bean
	 */
	@Bean
	@ConditionalOnMissingBean
	public IDwzServerService defaultDwzServerService(IDwzLogStore logStore) {
		return new DefaultDwzServerServiceImpl(logStore);
	}


	/**
	 * 创建：纠正错误数据的服务接口Bean
	 *
	 * @param dwzLogStore     短链接记录存取接口Bean
	 * @param sequenceService 序列服务bean
	 * @return 纠正错误数据的服务接口Bean
	 */
	@Bean
	@ConditionalOnMissingBean
	public IDwzCorrectErrorDataService defaultDwzCorrectErrorDataService(IDwzLogStore dwzLogStore, ISequenceService sequenceService) {
		return new DefaultDwzCorrectErrorDataServiceImpl(dwzLogStore, sequenceService);
	}

	/**
	 * 创建：项目启动完成的监听器Bean
	 *
	 * @param dwzCorrectErrorDataService 纠正错误数据的服务接口Bean
	 * @return 项目启动完成的监听器Bean
	 */
	@Bean
	public DwzServerStartupApplicationListener dwzServerInitApplicationListener(IDwzCorrectErrorDataService dwzCorrectErrorDataService) {
		return new DwzServerStartupApplicationListener(dwzCorrectErrorDataService);
	}


	/**
	 * 定时任务相关配置类
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = "easyj.middleware.dwz.server.task.enabled", matchIfMissing = true)
	@EnableScheduling
	public static class EasyjDwzServerTaskConfiguration {

		/**
		 * 创建：定时任务配置Bean
		 *
		 * @return 定时任务配置Bean
		 */
		@Bean
		@ConfigurationProperties("easyj.middleware.dwz.server.task")
		public DwzServerTaskConfig dwzServerTaskProperties() {
			return new DwzServerTaskConfig();
		}

		/**
		 * 创建：定时任务Bean
		 *
		 * @param dwzLogStore         短链接记录存取接口Bean
		 * @param dwzServerTaskConfig 定时任务配置Bean
		 * @return 定时任务Bean
		 */
		@Bean
		@Lazy(false)
		@ConditionalOnMissingBean
		public EasyjDwzServerTask easyjDwzServerTask(IDwzLogStore dwzLogStore, DwzServerTaskConfig dwzServerTaskConfig) {
			return new EasyjDwzServerTask(dwzLogStore, dwzServerTaskConfig);
		}
	}
}
