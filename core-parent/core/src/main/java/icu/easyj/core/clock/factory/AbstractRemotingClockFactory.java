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
package icu.easyj.core.clock.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import icu.easyj.core.clock.ClockManager;
import icu.easyj.core.clock.IAutoRefreshTickClock;
import icu.easyj.core.util.MapUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 抽象远端时钟工厂
 *
 * @param <K> 远端键类型
 * @author wangliang181230
 * @see IAutoRefreshTickClock
 * @see IRemotingClockFactory
 */
public abstract class AbstractRemotingClockFactory<K> implements IRemotingClockFactory<K> {

	/**
	 * 远端时钟Map
	 */
	private final ConcurrentMap<K, IAutoRefreshTickClock> remotingClockMap;

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
	protected AbstractRemotingClockFactory(ConcurrentMap<K, IAutoRefreshTickClock> remotingClockMap) {
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
	public IAutoRefreshTickClock getClock(@NonNull K remotingKey) {
		Assert.notNull(remotingKey, "'remotingKey' must not be null");
		return MapUtils.computeIfAbsent(remotingClockMap, remotingKey, this::createClock);
	}

	/**
	 * 销毁远端时钟
	 *
	 * @param remotingKey 远端键值
	 */
	@Override
	public void destroyClock(@NonNull K remotingKey) {
		Assert.notNull(remotingKey, "'remotingKey' must not be null");

		// 销毁原时钟
		ClockManager.destroy(remotingClockMap.remove(remotingKey));
	}

	//endregion
}
