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
import icu.easyj.core.sequence.impls.AtomicLongSequenceServiceImpl;
import icu.easyj.db.sequence.impls.DataBaseSequenceServiceImpl;
import icu.easyj.redis.sequence.impls.SpringRedisSequenceServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 序列服务自动装配类
 *
 * @author wangliang181230
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "easyj.sequence.type")
public class EasyjSequenceAutoConfiguration {

	/**
	 * 基于 Redis 实现的序列服务
	 */
	@Lazy
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = "easyj.sequence.type", havingValue = "redis")
	static class RedisSequenceServiceConfiguration {

		@Bean
		public ISequenceService redisSequenceServiceImpl(RedisConnectionFactory connectionFactory) {
			return new SpringRedisSequenceServiceImpl(connectionFactory);
		}
	}


	/**
	 * 基于 数据库 实现的序列服务
	 */
	@Lazy
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = "easyj.sequence.type", havingValue = "db")
	static class DataBaseSequenceServiceConfiguration {

		@Bean
		public ISequenceService dataBaseSequenceService(DataSource primaryDataSource) {
			return new DataBaseSequenceServiceImpl(primaryDataSource);
		}
	}


	/**
	 * 基于 {@link java.util.concurrent.atomic.AtomicLong} 实现的序列服务
	 */
	@Lazy
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = "easyj.sequence.type", havingValue = "atomic-long")
	static class AtomicLongSequenceServiceConfiguration {

		@Bean
		public ISequenceService atomicLongSequenceService() {
			return new AtomicLongSequenceServiceImpl();
		}
	}
}
