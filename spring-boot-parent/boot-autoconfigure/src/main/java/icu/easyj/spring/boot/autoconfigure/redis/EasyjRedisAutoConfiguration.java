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
package icu.easyj.spring.boot.autoconfigure.redis;

import javax.sql.DataSource;

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.spring.boot.autoconfigure.jdbc.EasyjJdbcAutoConfiguration;
import icu.easyj.spring.boot.autoconfigure.redis.sequence.SpringRedisSequenceServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis相关功能的自动装配类
 *
 * @author wangliang181230
 */
@ConditionalOnClass(RedisConnectionFactory.class)
public class EasyjRedisAutoConfiguration {

	/**
	 * 基于 {@link RedisTemplate} 实现的序列服务
	 *
	 * @return 序列服务的实现
	 * @see EasyjJdbcAutoConfiguration#primaryDataBaseSequenceService(DataSource) 基于数据库的实现
	 */
	@Lazy(false)
	@Bean
	@Primary
	@Qualifier("redisTemplateSequenceService")
	public ISequenceService springRedisTemplateSequenceServiceImpl() {
		return new SpringRedisSequenceServiceImpl();
	}
}
