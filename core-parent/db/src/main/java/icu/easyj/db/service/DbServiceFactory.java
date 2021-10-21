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
package icu.easyj.db.service;

import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.loader.EnhancedServiceNotFoundException;
import icu.easyj.core.util.MapUtils;
import icu.easyj.db.util.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库服务工厂类
 *
 * @author wangliang181230
 * @see IDbService
 */
public abstract class DbServiceFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(DbServiceFactory.class);

	/**
	 * DbService实现类缓存
	 */
	private static final ConcurrentHashMap<DataSource, IDbService> DB_SERVICE_MAP = new ConcurrentHashMap<>();


	/**
	 * 根据数据源获取
	 *
	 * @param dataSource 数据源
	 * @return 数据库服务
	 */
	public static IDbService getDbService(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");

		return MapUtils.computeIfAbsent(DB_SERVICE_MAP, dataSource, ds -> {
			String dbType = DbUtils.getDbType(dataSource);
			Class<?>[] argTypes = new Class[]{DataSource.class};
			Object[] args = new Object[]{dataSource};
			try {
				return EnhancedServiceLoader.load(IDbService.class, dbType, argTypes, args);
			} catch (EnhancedServiceNotFoundException e) {
				LOGGER.error("未找到数据库 '{}' 的服务，请使用 `{}` 的方式自行实现，参照MySQL服务实现类：MySqlDbServiceImpl",
						dbType, EnhancedServiceLoader.class.getSimpleName());
				throw new EnhancedServiceNotFoundException("未找到数据库 '" + dbType + "' 的服务", e);
			}
		});
	}
}
