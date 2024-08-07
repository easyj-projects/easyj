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

import javax.sql.DataSource;

import icu.easyj.core.clock.factory.AbstractRemotingClockFactory;
import icu.easyj.db.service.DbServiceFactory;
import icu.easyj.db.service.IDbService;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库时钟工厂
 *
 * @author wangliang181230
 * @see AbstractRemotingClockFactory
 */
final class DbClockFactory extends AbstractRemotingClockFactory<DataSource> {

	//region 数据库时钟工厂单例持有者（设计模式-创建型模式-单例模式：枚举实现单例）

	private DbClockFactory() {
	}

	private enum SingletonHolder {
		// 单例
		INSTANCE;

		private final DbClockFactory dbClockFactory = new DbClockFactory();

		public DbClockFactory getDbClockFactory() {
			return dbClockFactory;
		}
	}

	/**
	 * 获取数据库时钟工厂
	 *
	 * @return 数据库时钟工厂
	 */
	public static DbClockFactory getInstance() {
		return SingletonHolder.INSTANCE.getDbClockFactory();
	}

	//endregion


	/**
	 * 获取数据时间，单位：毫秒
	 *
	 * @param dataSource 数据源
	 * @return 数据库时间
	 */
	@Override
	@NonNull
	public long getRemotingTime(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");

		// 根据数据库类型，获取对应的实现
		IDbService dbService = DbServiceFactory.getDbService(dataSource);

		// 获取数据库时间（毫秒数）
		return dbService.currentTimeMillis();
	}
}
