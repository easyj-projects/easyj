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

import icu.easyj.core.util.clock.AbstractRemotingClockHolder;
import icu.easyj.core.util.clock.IClock;
import icu.easyj.core.util.clock.IRemotingClockHolder;
import icu.easyj.core.util.clock.ITickClock;
import icu.easyj.core.util.clock.TickClock;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库时钟持有者（单例模式）
 *
 * @author wangliang181230
 * @see IClock
 * @see ITickClock
 * @see TickClock
 * @see AbstractRemotingClockHolder
 * @see IRemotingClockHolder
 */
class DbClockHolder extends AbstractRemotingClockHolder<DataSource> {

	private DbClockHolder() {
	}

	/**
	 * 创建数据库时钟
	 *
	 * @param dataSource 数据源
	 * @return dbClock 数据库时钟
	 */
	@Override
	@NonNull
	public IClock createClock(DataSource dataSource) {
		Assert.notNull(dataSource, "dataSource must be not null");
		// TODO: @wangliang181230 待开发，方言需支持不同数据库不同`获取数据库时间的SQL`的功能。
		return null;
	}


	//region Singleton 单例模式-嵌套类

	/**
	 * 单例持有者嵌套类
	 */
	private static class SingletonHolder {
		private static final DbClockHolder INSTANCE = new DbClockHolder();
	}

	/**
	 * @return 单例
	 */
	public static DbClockHolder getInstance() {
		return SingletonHolder.INSTANCE;
	}

	//endregion
}
