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

import javax.sql.DataSource;

import icu.easyj.core.clock.IClock;
import icu.easyj.core.clock.ITickClock;
import icu.easyj.core.clock.TickClock;
import icu.easyj.core.clock.holder.AbstractRemotingClockHolder;
import icu.easyj.core.clock.holder.IRemotingClockHolder;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库时钟持有者
 *
 * @author wangliang181230
 * @see IClock
 * @see ITickClock
 * @see TickClock
 * @see AbstractRemotingClockHolder
 * @see IRemotingClockHolder
 */
final class DbClockHolder extends AbstractRemotingClockHolder<DataSource> {

	//region 数据库时钟持有者（设计模式-创建型模式-单例模式-枚举实现单例）

	private DbClockHolder() {
	}

	private enum SingletonHolder {
		// 单例
		INSTANCE;

		private final DbClockHolder dbClockHolder = new DbClockHolder();

		public DbClockHolder getDbClockHolder() {
			return dbClockHolder;
		}
	}

	/**
	 * @return 数据库时钟持有者
	 */
	public static DbClockHolder getInstance() {
		return SingletonHolder.INSTANCE.getDbClockHolder();
	}

	//endregion


	/**
	 * 创建数据库时钟
	 *
	 * @param dataSource 数据源
	 * @return dbClock 数据库时钟
	 */
	@Override
	@NonNull
	public ITickClock createClock(DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must be not null");
		// TODO: @wangliang181230 待开发，方言需支持不同数据库不同`获取数据库时间的SQL`的功能。
		return null;
	}
}
