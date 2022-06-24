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
package icu.easyj.core.clock;

import java.util.function.Supplier;

import org.springframework.lang.NonNull;

/**
 * 高精准可刷新记号时钟
 *
 * @author wangliang181230
 */
public class HighAccuracyRefreshableTickClock extends RefreshableTickClock {

	/**
	 * 最小尝试次数
	 */
	protected static final int MIN_TRY_COUNT = 5;

	/**
	 * 默认尝试次数
	 */
	protected static final int DEFAULT_TRY_COUNT = 10;


	/**
	 * 尝试次数
	 * <p>
	 * 为了获取更加精准，时间误差更小的记号时钟，通过多次尝试，获取延迟最小的一次时间作为记号时钟的时间
	 */
	private final int tryCount;


	public HighAccuracyRefreshableTickClock(@NonNull Supplier<ITickClock> tickClockSupplier, int tryCount) {
		super(tickClockSupplier);

		// 不能小于 最小尝试次数（MIN_TRY_COUNT）
		this.tryCount = Math.max(tryCount, MIN_TRY_COUNT);

		// 初始化
		this.init();
	}

	public HighAccuracyRefreshableTickClock(@NonNull Supplier<ITickClock> tickClockSupplier) {
		this(tickClockSupplier, DEFAULT_TRY_COUNT);
	}


	/**
	 * 刷新记号时钟
	 */
	@Override
	public void refreshTickClock() {
		// 尝试次数
		int tryCount = this.tryCount;

		// 方式一：多次创建远端记号时钟，取耗时最小的一次
//		long currentNanoTime;
//		ITickClock newTickClock, currentTickClock = null;
//		long currentCost, minCost = Long.MAX_VALUE;
//		do {
//			currentNanoTime = System.nanoTime();//
//			newTickClock = this.createClock();
//			currentCost = newTickClock.getBaseTickNanos() - currentNanoTime;
//
//			if (currentTickClock == null || minCost > currentCost) {
//				currentTickClock = newTickClock;
//				minCost = currentCost;
//			}
//		} while (--tryCount > 0);


		// 方式二：多次创建远端记号时钟，取时间最大的时钟
		ITickClock newTickClock, currentTickClock = null;
		do {
			newTickClock = this.createClock();
			if (currentTickClock == null || newTickClock.compareTo(currentTickClock) > 0) {
				currentTickClock = newTickClock;
			}
		} while (--tryCount > 0);


		super.setTickClock(currentTickClock);
	}


	//region Getter、Setter

	public int getTryCount() {
		return tryCount;
	}

	//endregion
}
