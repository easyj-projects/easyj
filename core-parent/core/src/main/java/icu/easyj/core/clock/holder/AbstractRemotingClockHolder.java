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
package icu.easyj.core.clock.holder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import icu.easyj.core.clock.ITickClock;
import icu.easyj.core.clock.TickClock;
import icu.easyj.core.util.MapUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 抽象远端时钟持有者
 *
 * @author wangliang181230
 * @see ITickClock
 * @see TickClock
 * @see IRemotingClockHolder
 */
public abstract class AbstractRemotingClockHolder<T> implements IRemotingClockHolder<T> {

	/**
	 * 远端时钟Map
	 */
	private final Map<T, ITickClock> remotingClockMap;

	/**
	 * 无参构造函数
	 */
	protected AbstractRemotingClockHolder() {
		this(new ConcurrentHashMap<>(2));
	}

	/**
	 * 有参构造函数
	 *
	 * @param remotingClockMap 保存远端时钟的Map
	 */
	protected AbstractRemotingClockHolder(Map<T, ITickClock> remotingClockMap) {
		this.remotingClockMap = remotingClockMap;
	}


	//region Override IRemotingClockHolder

	/**
	 * 获取远端时钟
	 *
	 * @param remotingKey 远端健值
	 * @return 时钟
	 */
	@Override
	@NonNull
	public ITickClock getClock(@NonNull T remotingKey) {
		Assert.notNull(remotingKey, "'remotingKey' must be not null");
		return MapUtils.computeIfAbsent(remotingClockMap, remotingKey, ds -> createClock(remotingKey));
	}

	/**
	 * 刷新远端时钟并返回新时钟
	 *
	 * @param remotingKey 远端键值
	 * @return newClock 时钟
	 */
	@Override
	@NonNull
	public ITickClock refreshClock(@NonNull T remotingKey) {
		Assert.notNull(remotingKey, "'remotingKey' must be not null");

		ITickClock newClock = createClock(remotingKey);
		remotingClockMap.put(remotingKey, newClock);
		return newClock;
	}

	//endregion
}
