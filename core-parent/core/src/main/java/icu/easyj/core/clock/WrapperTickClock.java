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
package icu.easyj.core.clock;

import cn.hutool.core.lang.Assert;
import org.springframework.lang.NonNull;

/**
 * 包装记号时钟
 *
 * @author wangliang181230
 */
public class WrapperTickClock implements IWrapperTickClock {

	/**
	 * 记号时钟
	 */
	private ITickClock tickClock;


	/**
	 * 有参构造函数
	 *
	 * @param tickClock 记号时钟
	 */
	public WrapperTickClock(@NonNull ITickClock tickClock) {
		this.setTickClock(tickClock);
	}

	/**
	 * 无参构造函数
	 * <p>
	 * 注意，如果使用此构造函数，必须在子类中调用一次 {@link #setTickClock(ITickClock)} 方法，避免tickClock为空。
	 */
	protected WrapperTickClock() {
	}


	/**
	 * 结合无参构造函数一起使用
	 *
	 * @param tickClock 记号时钟
	 */
	protected final void setTickClock(@NonNull ITickClock tickClock) {
		Assert.notNull(tickClock, "'tickClock' must not be null");
		this.tickClock = tickClock;
	}


	//region Override

	@Override
	public final long currentTimeNanos() {
		return tickClock.currentTimeNanos();
	}

	@Override
	public final long getBaseEpochMicros() {
		return tickClock.getBaseEpochMicros();
	}

	@Override
	public final long getBaseTickNanos() {
		return tickClock.getBaseTickNanos();
	}

	@Override
	public final ITickClock getTickClock() {
		return tickClock;
	}

	//endregion
}
