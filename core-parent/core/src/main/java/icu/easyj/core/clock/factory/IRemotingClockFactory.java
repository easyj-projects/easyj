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

import icu.easyj.core.clock.ITickClock;
import org.springframework.lang.NonNull;

/**
 * 远端时钟工厂接口
 *
 * @param <K> 远端键类型
 * @author wangliang181230
 * @see ITickClock
 */
public interface IRemotingClockFactory<K> {

	/**
	 * 创建远端时钟
	 *
	 * @param remotingKey 远端键值
	 * @return 时钟
	 */
	@NonNull
	ITickClock createClock(@NonNull K remotingKey);

	/**
	 * 获取远端时钟
	 *
	 * @param remotingKey 远端健值
	 * @return 时钟
	 */
	@NonNull
	ITickClock getClock(@NonNull K remotingKey);

	/**
	 * 刷新远端时钟并返回新时钟（新时钟实例可以与原来时钟的实例相同，也可以不同）
	 *
	 * @param remotingKey 远端键值
	 * @return newClock 时钟
	 */
	@NonNull
	ITickClock refreshClock(@NonNull K remotingKey);

	/**
	 * 远端的当前时间
	 *
	 * @param remotingKey 远端键值
	 * @return now 当前时间
	 */
	@NonNull
	default Date now(@NonNull K remotingKey) {
		return getClock(remotingKey).now();
	}

	/**
	 * 远端的当前毫秒数
	 *
	 * @param remotingKey 远端键值
	 * @return timeMillis 毫秒数
	 */
	default long currentTimeMillis(@NonNull K remotingKey) {
		return getClock(remotingKey).currentTimeMillis();
	}

	/**
	 * 远端的当前微秒数
	 *
	 * @param remotingKey 远端键值
	 * @return timeMicros 微秒数
	 */
	default long currentTimeMicros(@NonNull K remotingKey) {
		return getClock(remotingKey).currentTimeMicros();
	}

	/**
	 * 远端的当前纳秒数<br>
	 * 注意：值格式与 {@link System#nanoTime()} 并不相同
	 *
	 * @param remotingKey 远端键值
	 * @return timeNanos 纳秒数
	 */
	default long currentTimeNanos(@NonNull K remotingKey) {
		return getClock(remotingKey).currentTimeNanos();
	}
}
