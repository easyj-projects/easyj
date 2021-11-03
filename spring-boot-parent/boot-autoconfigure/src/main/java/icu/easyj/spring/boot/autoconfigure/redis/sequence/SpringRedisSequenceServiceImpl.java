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
package icu.easyj.spring.boot.autoconfigure.redis.sequence;

import icu.easyj.core.sequence.ISequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.lang.NonNull;

/**
 * 基于 {@link RedisTemplate} 实现的序列服务
 *
 * @author wangliang181230
 */
public class SpringRedisSequenceServiceImpl implements ISequenceService {

	private static RedisConnectionFactory connectionFactory;


	@Override
	public long nextVal(@NonNull String seqName) {
		return this.getAtomicLong(seqName).incrementAndGet();
	}

	@Override
	public long currVal(@NonNull String seqName) {
		return this.getAtomicLong(seqName).get();
	}

	@Override
	public void setVal(@NonNull String seqName, long newVal) {
		this.getAtomicLong(seqName).set(newVal);
	}


	private RedisAtomicLong getAtomicLong(String seqName) {
		return new RedisAtomicLong(seqName, connectionFactory);
	}


	/**
	 * 依赖注入RedisTemplate
	 *
	 * @param connectionFactory Redis连接工厂
	 */
	@Autowired
	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		SpringRedisSequenceServiceImpl.connectionFactory = connectionFactory;
	}
}
