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
package icu.easyj.db.util;

import java.util.Date;
import javax.sql.DataSource;

import icu.easyj.core.clock.ITickClock;
import icu.easyj.core.clock.TickClock;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库时钟工具类
 *
 * @author wangliang181230
 * @see ITickClock
 * @see TickClock
 */
public abstract class DbClockUtils {

	//region 私有方法（Private Methods）

	/**
	 * 获取数据库时钟工厂
	 *
	 * @return dbClockFactory 数据库时钟工厂
	 */
	private static DbClockFactory getFactory() {
		return DbClockFactory.getInstance();
	}

	//endregion


	//region 所有数据库时钟相关方法

	/**
	 * 获取数据库时钟
	 *
	 * @param dataSource 数据源
	 * @return 时钟
	 */
	@NonNull
	public static ITickClock getClock(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		return getFactory().getClock(dataSource);
	}

	/**
	 * 刷新数据库时钟并返回新时钟
	 *
	 * @param dataSource 数据源
	 * @return newClock 时钟
	 */
	@NonNull
	public static ITickClock refreshClock(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		return getFactory().getClock(dataSource);
	}

	/**
	 * 数据源的当前时间
	 *
	 * @param dataSource 数据源
	 * @return now 当前时间
	 */
	@NonNull
	public static Date now(@NonNull DataSource dataSource) {
		return getClock(dataSource).now();
	}

	/**
	 * 主要数据源当前毫秒数
	 *
	 * @param dataSource 数据源
	 * @return timeMillis 毫秒数
	 */
	public static long currentTimeMillis(@NonNull DataSource dataSource) {
		return getClock(dataSource).currentTimeMillis();
	}

	/**
	 * 主要数据源当前微秒数
	 *
	 * @param dataSource 数据源
	 * @return timeMicros 微秒数
	 */
	public static long currentTimeMicros(@NonNull DataSource dataSource) {
		return getClock(dataSource).currentTimeMicros();
	}

	/**
	 * 主要数据源当前纳秒数<br>
	 * 注意：值格式与 {@link System#nanoTime()} 并不相同
	 *
	 * @param dataSource 数据源
	 * @return timeNanos 纳秒数
	 */
	public static long currentTimeNanos(@NonNull DataSource dataSource) {
		return getClock(dataSource).currentTimeNanos();
	}

	//endregion


	//region 主要数据库时钟相关方法（使用前提是设置过主要数据源：`PrimaryDataSourceHolder.set(primaryDataSource)` ）

	/**
	 * 获取主要数据库时钟
	 *
	 * @return primaryClock 主要数据库时钟
	 */
	@NonNull
	public static ITickClock getClock() {
		return getClock(PrimaryDataSourceHolder.get());
	}

	/**
	 * 刷新主要数据库时钟并返回新时钟
	 *
	 * @return newClock 时钟
	 */
	@NonNull
	public static ITickClock refreshClock() {
		return refreshClock(PrimaryDataSourceHolder.get());
	}

	/**
	 * 主要数据源的当前时间
	 *
	 * @return now 当前时间
	 */
	@NonNull
	public static Date now() {
		return now(PrimaryDataSourceHolder.get());
	}

	/**
	 * 主要数据源的当前毫秒数
	 *
	 * @return timeMillis 毫秒数
	 */
	public static long currentTimeMillis() {
		return currentTimeMillis(PrimaryDataSourceHolder.get());
	}

	/**
	 * 主要数据源的当前微秒数
	 *
	 * @return timeMicros 微秒数
	 */
	public static long currentTimeMicros() {
		return currentTimeMicros(PrimaryDataSourceHolder.get());
	}

	/**
	 * 主要数据源的当前纳秒数<br>
	 * 注意：值格式与 {@link System#nanoTime()} 并不相同
	 *
	 * @return timeNanos 纳秒数
	 */
	public static long currentTimeNanos() {
		return currentTimeNanos(PrimaryDataSourceHolder.get());
	}

	//endregion
}
