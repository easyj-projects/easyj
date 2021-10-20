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

import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.MapUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库服务工厂类
 *
 * @author wangliang181230
 * @see IDbService
 */
public abstract class DbServiceFactory {

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
			return EnhancedServiceLoader.load(IDbService.class, dbType.toLowerCase(),
					new Class[]{DataSource.class}, new Object[]{dataSource});
		});
	}
}
