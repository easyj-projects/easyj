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
package icu.easyj.core.clock.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import icu.easyj.core.clock.ITickClock;
import icu.easyj.core.util.MapUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 抽象远端时钟工厂
 *
 * @param <K> 远端键类型
 * @author wangliang181230
 * @see ITickClock
 * @see IRemotingClockFactory
 */
public abstract class AbstractRemotingClockFactory<K> implements IRemotingClockFactory<K> {

	/**
	 * 远端时钟Map
	 */
	private final ConcurrentMap<K, ITickClock> remotingClockMap;

	/**
	 * 无参构造函数
	 */
	protected AbstractRemotingClockFactory() {
		this(new ConcurrentHashMap<>(2));
	}

	/**
	 * 有参构造函数
	 *
	 * @param remotingClockMap 保存远端时钟的Map
	 */
	protected AbstractRemotingClockFactory(ConcurrentMap<K, ITickClock> remotingClockMap) {
		this.remotingClockMap = remotingClockMap;
	}


	//region Override IRemotingClockFactory

	/**
	 * 获取远端时钟
	 *
	 * @param remotingKey 远端健值
	 * @return 时钟
	 */
	@Override
	@NonNull
	public ITickClock getClock(@NonNull K remotingKey) {
		Assert.notNull(remotingKey, "'remotingKey' must not be null");
		return MapUtils.computeIfAbsent(remotingClockMap, remotingKey, this::createClock);
	}

	/**
	 * 刷新远端时钟并返回新时钟
	 *
	 * @param remotingKey 远端键值
	 * @return newClock 时钟
	 */
	@Override
	@NonNull
	public ITickClock refreshClock(@NonNull K remotingKey) {
		Assert.notNull(remotingKey, "'remotingKey' must not be null");

		ITickClock newClock = createClock(remotingKey);
		remotingClockMap.put(remotingKey, newClock);
		return newClock;
	}

	//endregion
}
