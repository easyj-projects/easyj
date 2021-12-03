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

import java.util.Date;

import icu.easyj.core.clock.AutoRefreshHighAccuracyTickClock;
import icu.easyj.core.clock.IAutoRefreshTickClock;
import icu.easyj.core.clock.TickClock;
import org.springframework.util.Assert;

/**
 * 远端时钟工厂接口
 *
 * @param <K> 远端键类型
 * @author wangliang181230
 * @see IAutoRefreshTickClock
 */
public interface IRemotingClockFactory<K> {

	/**
	 * 获取远端时间，单位：毫秒
	 *
	 * @param remotingKey 远端键值
	 * @return 远端时间
	 */
	long getRemotingTime(K remotingKey);

	/**
	 * 创建远端时钟
	 * <p>
	 * 为了使记号时钟的时间误差更小，使用此方法来获取远端时钟
	 *
	 * @param remotingKey 远端键值
	 * @return 时钟
	 */
	default IAutoRefreshTickClock createClock(K remotingKey) {
		Assert.notNull(remotingKey, "'remotingKey' must not be null");
		return new AutoRefreshHighAccuracyTickClock(remotingKey.getClass().getSimpleName(),
				() -> new TickClock(this.getRemotingTime(remotingKey) * 1000));
	}

	/**
	 * 获取远端时钟
	 *
	 * @param remotingKey 远端健值
	 * @return 时钟
	 */
	IAutoRefreshTickClock getClock(K remotingKey);

	/**
	 * 销毁远端时钟
	 *
	 * @param remotingKey 远端键值
	 */
	void destroyClock(K remotingKey);

	/**
	 * 远端的当前时间
	 *
	 * @param remotingKey 远端键值
	 * @return now 当前时间
	 */
	default Date now(K remotingKey) {
		return getClock(remotingKey).now();
	}

	/**
	 * 远端的当前毫秒数
	 *
	 * @param remotingKey 远端键值
	 * @return timeMillis 毫秒数
	 */
	default long currentTimeMillis(K remotingKey) {
		return getClock(remotingKey).currentTimeMillis();
	}

	/**
	 * 远端的当前微秒数
	 *
	 * @param remotingKey 远端键值
	 * @return timeMicros 微秒数
	 */
	default long currentTimeMicros(K remotingKey) {
		return getClock(remotingKey).currentTimeMicros();
	}

	/**
	 * 远端的当前纳秒数<br>
	 * 注意：值格式与 {@link System#nanoTime()} 并不相同
	 *
	 * @param remotingKey 远端键值
	 * @return timeNanos 纳秒数
	 */
	default long currentTimeNanos(K remotingKey) {
		return getClock(remotingKey).currentTimeNanos();
	}
}
