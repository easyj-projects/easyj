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
package icu.easyj.spring.boot.autoconfigure.sequence;

import javax.sql.DataSource;

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.core.sequence.impl.AtomicLongSequenceServiceImpl;
import icu.easyj.db.sequence.impls.DbSequenceServiceImpl;
import icu.easyj.redis.sequence.impls.SpringRedisSequenceServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 序列服务自动装配类
 *
 * @author wangliang181230
 */
@ConditionalOnClass(ISequenceService.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class EasyjSequenceAutoConfiguration {

	/**
	 * 基于 Redis 实现的序列服务
	 *
	 * @return 序列服务的实现
	 */
	@Lazy(false)
	@Primary
	@Bean("redisSequenceService")
	@ConditionalOnMissingBean(name = "redisSequenceService")
	@ConditionalOnClass(RedisConnectionFactory.class)
	@ConditionalOnProperty(value = "easyj.sequence.type", havingValue = "redis", matchIfMissing = true)
	public ISequenceService redisSequenceServiceImpl() {
		return new SpringRedisSequenceServiceImpl();
	}


	//--------------------------------------------------------------------------------------------------------------


	/**
	 * 基于 数据库 实现的序列服务
	 *
	 * @param primaryDataSource 主要数据源
	 * @return 序列服务的实现
	 */
	@Bean("dbSequenceService")
	@ConditionalOnMissingBean(name = "dbSequenceService")
	@ConditionalOnBean(DataSource.class)
	@ConditionalOnProperty(value = "easyj.sequence.type", havingValue = "db", matchIfMissing = true)
	public ISequenceService primaryDataBaseSequenceService(DataSource primaryDataSource) {
		return new DbSequenceServiceImpl(primaryDataSource);
	}


	//--------------------------------------------------------------------------------------------------------------


	/**
	 * 基于 {@link java.util.concurrent.atomic.AtomicLong} 实现的序列服务
	 *
	 * @return 序列服务的实现
	 */
	@Bean("atomicLongSequenceService")
	@ConditionalOnMissingBean(name = "atomicLongSequenceService")
	@ConditionalOnBean(DataSource.class)
	@ConditionalOnProperty(value = "easyj.sequence.type", havingValue = "atomic-long", matchIfMissing = true)
	public ISequenceService atomicLongSequenceService() {
		return new AtomicLongSequenceServiceImpl();
	}
}
