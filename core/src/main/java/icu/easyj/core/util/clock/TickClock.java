/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.core.util.clock;

/**
 * 记号时钟
 *
 * @author wangliang181230
 */
public class TickClock implements ITickClock {

	//region 基准时间

	/**
	 * 基准时间毫秒数
	 */
	private final long baseEpochMillis;
	/**
	 * 基准时间毫秒数
	 */
	private final long baseEpochMicros;

	/**
	 * 基准时间毫秒数
	 * 注意：值格式与`System.nanoTime()`并不相同
	 */
	private final long baseEpochNanos;

	//endregion

	/**
	 * 基准记号纳秒数
	 */
	private final long baseTickNanos;

	/**
	 * 构造函数
	 *
	 * @param baseEpochMicros 基准时间微秒数
	 * @param baseTickNanos   基准记号纳秒数
	 */
	public TickClock(long baseEpochMicros, long baseTickNanos) {
		this.baseEpochMillis = baseEpochMicros / 1000;
		this.baseEpochMicros = baseEpochMicros;
		this.baseEpochNanos = baseEpochMicros * 1000;

		this.baseTickNanos = baseTickNanos;
	}

	/**
	 * 构造函数
	 *
	 * @param baseEpochMicros 基准时间微秒数
	 */
	public TickClock(long baseEpochMicros) {
		this(baseEpochMicros, System.nanoTime());
	}


	//region Override

	@Override
	public long currentTimeMillis() {
		return (getPassedNanos() / 1000000) + baseEpochMillis;
	}

	@Override
	public long currentTimeMicros() {
		return (getPassedNanos() / 1000) + baseEpochMicros;
	}

	@Override
	public long currentTimeNanos() {
		return getPassedNanos() + baseEpochNanos;
	}

	@Override
	public long getBaseEpochMicros() {
		return this.baseEpochMicros;
	}

	@Override
	public long getBaseTickNanos() {
		return this.baseTickNanos;
	}

	//endregion
}
