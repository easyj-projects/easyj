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
package icu.easyj.spring.boot.autoconfigure.jdbc;

import javax.sql.DataSource;

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.db.sequence.impls.DbSequenceServiceImpl;
import icu.easyj.db.util.PrimaryDataSourceHolder;
import icu.easyj.spring.boot.autoconfigure.redis.EasyjRedisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * JDBC相关自动装配
 *
 * @author wangliang181230
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
public class EasyjJdbcAutoConfiguration {

	public EasyjJdbcAutoConfiguration(@Autowired(required = false) DataSource primaryDataSource) {
		// 设置主要数据源
		if (primaryDataSource != null) {
			PrimaryDataSourceHolder.set(primaryDataSource);
		}
	}


	/**
	 * 基于 {@link icu.easyj.db.util.PrimaryDbUtils} 实现的序列服务
	 *
	 * @param primaryDataSource 主要数据源
	 * @return 序列服务的实现
	 * @see EasyjRedisAutoConfiguration#springRedisTemplateSequenceServiceImpl() 基于redis实现的
	 */
	@Bean
	@Qualifier("dbSequenceService")
	public ISequenceService primaryDataBaseSequenceService(@Autowired(required = false) DataSource primaryDataSource) {
		return new DbSequenceServiceImpl(primaryDataSource);
	}
}
