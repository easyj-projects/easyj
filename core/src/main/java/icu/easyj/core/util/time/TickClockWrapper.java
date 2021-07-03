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
package icu.easyj.core.util.time;

import java.util.Date;

/**
 * 记号时钟包装
 *
 * @author wangliang181230
 */
public class TickClockWrapper implements ITickClock {

	protected final ITickClock tickClock;

	/**
	 * 构造函数
	 *
	 * @param tickClock 记号时钟
	 */
	public TickClockWrapper(ITickClock tickClock) {
		this.tickClock = tickClock;
	}


	//region Override

	@Override
	public Date now() {
		return tickClock.now();
	}

	@Override
	public long currentTimeMillis() {
		return tickClock.currentTimeMillis();
	}

	@Override
	public long currentTimeMicros() {
		return tickClock.currentTimeMicros();
	}

	@Override
	public long currentTimeNanos() {
		return tickClock.currentTimeNanos();
	}

	@Override
	public long getBaseEpochMicros() {
		return tickClock.getBaseEpochMicros();
	}

	@Override
	public long getBaseTickNanos() {
		return tickClock.getBaseTickNanos();
	}

	@Override
	public long getPassedNanos() {
		return tickClock.getPassedNanos();
	}

	//endregion
}
