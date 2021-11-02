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

import java.util.function.Supplier;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 可刷新的记号时钟
 *
 * @author wangliang181230
 */
public class RefreshableTickClock extends WrapperTickClock implements IRefreshableTickClock {

	/**
	 * 记号时钟提供者
	 */
	private final Supplier<ITickClock> tickClockSupplier;


	public RefreshableTickClock(@NonNull Supplier<ITickClock> tickClockSupplier) {
		Assert.notNull(tickClockSupplier, "'tickClockSupplier' must not be null");
		this.tickClockSupplier = tickClockSupplier;
		this.refreshTickClock();
	}


	/**
	 * 创建记号时钟
	 *
	 * @return 记号时钟
	 */
	protected ITickClock createClock() {
		return this.tickClockSupplier.get();
	}


	//region Override

	/**
	 * 刷新记号时钟
	 */
	@Override
	public void refreshTickClock() {
		super.setTickClock(this.createClock());
	}

	@Override
	public Supplier<ITickClock> getTickClockSupplier() {
		return tickClockSupplier;
	}

	//endregion
}
