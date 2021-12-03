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

import icu.easyj.core.clock.IAutoRefreshTickClock;
import org.springframework.util.Assert;

/**
 * 数据库时钟工具类
 *
 * @author wangliang181230
 * @see IAutoRefreshTickClock
 */
public abstract class DbClockUtils {

	//region 私有方法

	/**
	 * 获取数据库时钟工厂
	 *
	 * @return dbClockFactory 数据库时钟工厂
	 */
	private static DbClockFactory getFactory() {
		return DbClockFactory.getInstance();
	}

	//endregion


	/**
	 * 获取数据库时钟
	 *
	 * @param dataSource 数据源
	 * @return 时钟
	 */
	public static IAutoRefreshTickClock getClock(DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		return getFactory().getClock(dataSource);
	}

	/**
	 * 刷新数据库时钟并返回新时钟
	 *
	 * @param dataSource 数据源
	 * @return newClock 时钟
	 */
	public static IAutoRefreshTickClock refreshClock(DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		return getFactory().getClock(dataSource);
	}

	/**
	 * 数据源的当前时间
	 *
	 * @param dataSource 数据源
	 * @return now 当前时间
	 */
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
	 * 注意：值格式与 {@link System#nanoTime()} 并不相同
	 *
	 * @param dataSource 数据源
	 * @return timeNanos 纳秒数
	 */
	public static long currentTimeNanos(DataSource dataSource) {
		return getClock(dataSource).currentTimeNanos();
	}
}
