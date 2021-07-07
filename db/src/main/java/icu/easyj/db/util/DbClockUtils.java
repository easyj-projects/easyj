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

import icu.easyj.core.util.clock.IClock;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 数据库时钟工具类
 *
 * @author wangliang181230
 * @see icu.easyj.core.util.clock.IClock
 * @see icu.easyj.core.util.clock.ITickClock
 * @see icu.easyj.core.util.clock.TickClock
 */
public abstract class DbClockUtils {

	//region 私有方法

	/**
	 * 获取数据库时钟持有者
	 *
	 * @return dbClockHolder 数据库时钟持有者
	 */
	private static DbClockHolder getHolder() {
		return DbClockHolder.getInstance();
	}

	/**
	 * 创建数据库时钟
	 *
	 * @param dataSource 数据源
	 * @return 时钟
	 */
	@NonNull
	private static IClock createClock(DataSource dataSource) {
		return getHolder().createClock(dataSource);
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
	public static IClock getClock(DataSource dataSource) {
		Assert.notNull(dataSource, "dataSource must be not null");

		if (dataSource == primaryDataSource) {
			return primaryClock;
		} else {
			return getHolder().getClock(dataSource);
		}
	}

	/**
	 * 刷新数据库时钟并返回新时钟
	 *
	 * @param dataSource 数据源
	 * @return newClock 时钟
	 */
	@NonNull
	public static IClock refreshClock(DataSource dataSource) {
		Assert.notNull(dataSource, "dataSource must be not null");

		if (dataSource == primaryDataSource) {
			return setPrimaryDataSource(dataSource);
		} else {
			return getHolder().getClock(dataSource);
		}
	}

	/**
	 * 数据源的当前时间
	 *
	 * @param dataSource 数据源
	 * @return now 当前时间
	 */
	@NonNull
	public static Date now(DataSource dataSource) {
		return getClock(dataSource).now();
	}

	/**
	 * 主要数据源当前毫秒数
	 *
	 * @param dataSource 数据源
	 * @return timeMillis 毫秒数
	 */
	public static long currentTimeMillis(DataSource dataSource) {
		return getClock(dataSource).currentTimeMillis();
	}

	/**
	 * 主要数据源当前微秒数
	 *
	 * @param dataSource 数据源
	 * @return timeMicros 微秒数
	 */
	public static long currentTimeMicros(DataSource dataSource) {
		return getClock(dataSource).currentTimeMicros();
	}

	/**
	 * 主要数据源当前纳秒数<br>
	 * 注意：值格式与`System.nanoTime()`并不相同
	 *
	 * @param dataSource 数据源
	 * @return timeNanos 纳秒数
	 */
	public static long currentTimeNanos(DataSource dataSource) {
		return getClock(dataSource).currentTimeNanos();
	}

	//endregion


	//region 主要数据库时钟相关方法

	/**
	 * 主要的数据源
	 */
	private static DataSource primaryDataSource;

	/**
	 * 主要数据库时钟
	 */
	private static IClock primaryClock;

	/**
	 * 设置主要数据源及其时钟（线程安全的）
	 *
	 * @param primaryDataSource 主要数据源
	 * @param primaryClock      主要数据库时钟
	 */
	private static synchronized void setPrimaryDataSourceClock(DataSource primaryDataSource, IClock primaryClock) {
		DbClockUtils.primaryDataSource = primaryDataSource;
		DbClockUtils.primaryClock = primaryClock;
	}

	/**
	 * 设置主要数据源及其时钟
	 *
	 * @param primaryDataSource 主要数据源
	 * @return primaryClock 主要数据库时钟
	 */
	public static IClock setPrimaryDataSource(DataSource primaryDataSource) {
		IClock primaryClock = createClock(primaryDataSource);
		setPrimaryDataSourceClock(primaryDataSource, primaryClock);
		return primaryClock;
	}

	/**
	 * 获取主要数据源
	 *
	 * @return primaryDataSource 主要数据源
	 */
	@Nullable
	public static DataSource getPrimaryDataSource() {
		return primaryDataSource;
	}

	/**
	 * 获取主要数据库时钟
	 *
	 * @return primaryClock 主要数据库时钟
	 */
	@Nullable
	public static IClock getPrimaryDataSourceClock() {
		return primaryClock;
	}

	/**
	 * 刷新数据库时钟并返回新时钟
	 *
	 * @return newClock 时钟
	 * @throws RuntimeException 当primaryDataSource从未设置过时，会抛出该异常
	 */
	@NonNull
	public static synchronized IClock refreshPrimaryClock() {
		if (primaryDataSource == null) {
			throw new RuntimeException("primaryDataSource未设置，请先设置！");
		}
		return setPrimaryDataSource(primaryDataSource);
	}

	/**
	 * 主要数据源的当前时间
	 *
	 * @return now 当前时间
	 * @throws NullPointerException 如果`primaryClock`为空，将抛出该异常
	 */
	public static Date now() {
		return primaryClock.now();
	}

	/**
	 * 主要数据源的当前毫秒数
	 *
	 * @return timeMillis 毫秒数
	 * @throws NullPointerException 如果`primaryClock`为空，将抛出该异常
	 */
	public static long currentTimeMillis() {
		return primaryClock.currentTimeMillis();
	}

	/**
	 * 主要数据源的当前微秒数
	 *
	 * @return timeMicros 微秒数
	 * @throws NullPointerException 如果`primaryClock`为空，将抛出该异常
	 */
	public static long currentTimeMicros() {
		return primaryClock.currentTimeMicros();
	}

	/**
	 * 主要数据源的当前纳秒数<br>
	 * 注意：值格式与`System.nanoTime()`并不相同
	 *
	 * @return timeNanos 纳秒数
	 * @throws NullPointerException 如果`primaryClock`为空，将抛出该异常
	 */
	public static long currentTimeNanos() {
		return primaryClock.currentTimeNanos();
	}
}
