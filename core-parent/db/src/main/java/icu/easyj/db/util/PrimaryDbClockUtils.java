/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.db.util;

import java.util.Date;

import icu.easyj.core.clock.IAutoRefreshTickClock;
import org.springframework.lang.NonNull;

/**
 * 主要数据库时钟工具类
 *
 * @author wangliang181230
 * @see DbClockUtils
 * @see IAutoRefreshTickClock
 */
public abstract class PrimaryDbClockUtils {

	/**
	 * 获取主要数据库时钟
	 *
	 * @return primaryClock 主要数据库时钟
	 */
	@NonNull
	public static IAutoRefreshTickClock getClock() {
		return DbClockUtils.getClock(PrimaryDataSourceHolder.get());
	}

	/**
	 * 刷新主要数据库时钟并返回新时钟
	 *
	 * @return newClock 时钟
	 */
	@NonNull
	public static IAutoRefreshTickClock refreshClock() {
		return DbClockUtils.refreshClock(PrimaryDataSourceHolder.get());
	}

	/**
	 * 主要数据源的当前时间
	 *
	 * @return now 当前时间
	 */
	@NonNull
	public static Date now() {
		return DbClockUtils.now(PrimaryDataSourceHolder.get());
	}

	/**
	 * 主要数据源的当前毫秒数
	 *
	 * @return timeMillis 毫秒数
	 */
	public static long currentTimeMillis() {
		return DbClockUtils.currentTimeMillis(PrimaryDataSourceHolder.get());
	}

	/**
	 * 主要数据源的当前微秒数
	 *
	 * @return timeMicros 微秒数
	 */
	public static long currentTimeMicros() {
		return DbClockUtils.currentTimeMicros(PrimaryDataSourceHolder.get());
	}

	/**
	 * 主要数据源的当前纳秒数<br>
	 * 注意：值格式与 {@link System#nanoTime()} 并不相同
	 *
	 * @return timeNanos 纳秒数
	 */
	public static long currentTimeNanos() {
		return DbClockUtils.currentTimeNanos(PrimaryDataSourceHolder.get());
	}
}
