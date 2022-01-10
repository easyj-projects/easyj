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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.security.auth.DestroyFailedException;

import cn.hutool.core.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 可自动刷新的高精准记号时钟
 *
 * @author wangliang181230
 */
public class AutoRefreshHighAccuracyTickClock extends HighAccuracyRefreshableTickClock implements IAutoRefreshTickClock {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoRefreshHighAccuracyTickClock.class);


	private final String name;

	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

	private ScheduledFuture<?> scheduledFuture;


	public AutoRefreshHighAccuracyTickClock(String name, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, @NonNull Supplier<ITickClock> tickClockSupplier, int tryCount) {
		super(tickClockSupplier, tryCount);

		Assert.notNull(name, "'name' must be not null");
		this.name = name;

		this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;

		// 开始自动刷新
		this.startAutoRefresh();
	}

	public AutoRefreshHighAccuracyTickClock(String name, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, @NonNull Supplier<ITickClock> tickClockSupplier) {
		this(name, scheduledThreadPoolExecutor, tickClockSupplier, DEFAULT_TRY_COUNT);
	}

	public AutoRefreshHighAccuracyTickClock(String name, @NonNull Supplier<ITickClock> tickClockSupplier, int tryCount) {
		this(name, new ScheduledThreadPoolExecutor(1, new NamedThreadFactory(name + "-AutoRefreshTask-", true)),
				tickClockSupplier, tryCount);
	}

	public AutoRefreshHighAccuracyTickClock(String name, @NonNull Supplier<ITickClock> tickClockSupplier) {
		this(name, tickClockSupplier, DEFAULT_TRY_COUNT);
	}


	//region Override IAutoRefreshTickClock

	@Override
	public synchronized void startAutoRefresh() {
		if (isAutoRefreshing()) {
			return;
		}

		// 定时任务参数
		long initialDelay = 10; // 初始化延迟时间（即：定时任务开始后的多少时间才第一次执行任务）
		long period = 10; // 每次任务执行间隔时间
		TimeUnit timeUnit = TimeUnit.MINUTES; // 时间单位：分钟

		// 开始定时任务
		scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
			super.refreshTickClock();
			LOGGER.debug("记号时钟 '{}' 已完成时间校准!", this.name);
		}, initialDelay, period, timeUnit);

		LOGGER.info("已开始记号时钟 '{}' 的时间校准任务，执行间隔：{} 分钟。", this.name, period);
	}

	@Override
	public synchronized void stopAutoRefresh() {
		if (isAutoRefreshing()) {
			try {
				scheduledFuture.cancel(true);
				LOGGER.info("已停止记号时钟 '{}' 的时间校准任务！", this.name);
			} catch (Exception e) {
				LOGGER.error("停止记号时钟 '{}' 的时间校准任务失败：{}", this.name, e);
			}

			scheduledFuture = null;
		}
	}

	@Override
	public boolean isAutoRefreshing() {
		return scheduledFuture != null;
	}

	//endregion


	//region Override Destroyable

	@Override
	public void destroy() throws DestroyFailedException {
		if (isDestroyed()) {
			return;
		}

		this.stopAutoRefresh();
		try {
			this.scheduledThreadPoolExecutor.shutdown();
		} catch (Exception e) {
			LOGGER.error("自动刷新记号时钟的任务 shutdown 失败", e);
			//throw new DestroyFailedException("自动刷新记号时钟的任务 shutdown 失败：" + e.getMessage());
		}
		this.scheduledThreadPoolExecutor = null;
	}

	@Override
	public boolean isDestroyed() {
		return scheduledThreadPoolExecutor == null;
	}

	//endregion
}
