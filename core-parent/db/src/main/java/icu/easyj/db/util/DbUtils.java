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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.MapUtils;
import icu.easyj.db.exception.DbException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库工具类
 *
 * @author wangliang181230
 */
public abstract class DbUtils {

	//region 根据数据库类型获取IDbService

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
			String dbType = getDbType(dataSource);
			return EnhancedServiceLoader.load(IDbService.class, dbType.toLowerCase(),
					new Class[]{DataSource.class}, new Object[]{dataSource});
		});
	}

	//endregion


	//region 获取数据库的一些信息：类型、版本、...等等

	/**
	 * 数据库类型缓存
	 */
	private static final ConcurrentHashMap<DataSource, String> DB_TYPE_MAP = new ConcurrentHashMap<>();


	/**
	 * 从数据源中获取数据库类型<br>
	 * 值域：MySQL、Oracle、...（注意大小写）（TODO: 其他数据库待补充）
	 *
	 * @param dataSource 数据源
	 * @return 数据库类型
	 */
	@NonNull
	public static String getDbType(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");

		return MapUtils.computeIfAbsent(DB_TYPE_MAP, dataSource, ds -> {
			try (Connection con = dataSource.getConnection()) {
				DatabaseMetaData metaData = con.getMetaData();
				return metaData.getDatabaseProductName();
			} catch (SQLException e) {
				throw new DbException("获取数据库类型失败", e);
			}
		});
	}

	/**
	 * 获取数据库版本号
	 *
	 * @param dataSource 数据源
	 * @return 数据库版本号
	 */
	public static String getDbVersion(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");

		IDbService dbService = getDbService(dataSource);
		return dbService.getVersion();
	}

	//endregion
}
