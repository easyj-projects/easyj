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
package icu.easyj.redis.sequence.impls;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.core.util.MapUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 基于 {@link RedisTemplate} 实现的序列服务
 *
 * @author wangliang181230
 */
public class SpringRedisSequenceServiceImpl implements ISequenceService {

	/**
	 * Redis连接工厂
	 */
	private final RedisConnectionFactory connectionFactory;

	/**
	 * 序列默认值
	 */
	@Nullable
	private final Long initialValue;

	/**
	 * 序列Map
	 */
	private final Map<String, RedisAtomicLong> redisAtomicLongMap;


	public SpringRedisSequenceServiceImpl(RedisConnectionFactory connectionFactory, @Nullable Long initialValue) {
		Assert.notNull(connectionFactory, "'connectionFactory' must be not null");

		this.connectionFactory = connectionFactory;
		this.initialValue = initialValue;

		this.redisAtomicLongMap = new ConcurrentHashMap<>();
	}

	public SpringRedisSequenceServiceImpl(RedisConnectionFactory connectionFactory) {
		this(connectionFactory, null);
	}


	@Override
	public long nextVal(@NonNull String seqName) {
		return this.getRedisAtomicLong(seqName).incrementAndGet();
	}

	@Override
	public long currVal(@NonNull String seqName) {
		return this.getRedisAtomicLong(seqName).get();
	}

	@Override
	public long setVal(@NonNull String seqName, long newVal) {
		return this.getRedisAtomicLong(seqName).getAndSet(newVal);
	}

	private RedisAtomicLong getRedisAtomicLong(String seqName) {
		return MapUtils.computeIfAbsent(this.redisAtomicLongMap, seqName, k -> {
			if (this.initialValue != null) {
				return new RedisAtomicLong(seqName, this.connectionFactory, this.initialValue);
			} else {
				return new RedisAtomicLong(seqName, this.connectionFactory);
			}
		});
	}
}
