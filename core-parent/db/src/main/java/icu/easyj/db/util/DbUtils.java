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

import icu.easyj.core.util.MapUtils;
import icu.easyj.db.constant.DbTypeConstants;
import icu.easyj.db.exception.DbException;
import icu.easyj.db.service.DbServiceFactory;
import icu.easyj.db.service.IDbService;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库工具类
 *
 * @author wangliang181230
 * @see DbServiceFactory
 */
public abstract class DbUtils {

	//region 获取数据库的一些信息：类型、版本、...等等

	/**
	 * 数据库类型缓存
	 */
	private static final ConcurrentHashMap<DataSource, String> DB_TYPE_MAP = new ConcurrentHashMap<>();


	@NonNull
	private static String convertDbType(String dbType) {
		// MS SQL Server返回的太长了，按自定义的常量返回
		if ("microsoft sql server".equalsIgnoreCase(dbType)) {
			return DbTypeConstants.MS_SQL_SERVER;
		}

		return dbType;
	}

	/**
	 * 从数据源中获取数据库类型<br>
	 * 值域：mysql、oracle、...（TODO: 其他数据库待补充）
	 *
	 * @param dataSource 数据源
	 * @return 数据库类型（全部转为小写字母）
	 */
	@NonNull
	public static String getDbType(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");

		return MapUtils.computeIfAbsent(DB_TYPE_MAP, dataSource, ds -> {
			try (Connection con = dataSource.getConnection()) {
				DatabaseMetaData metaData = con.getMetaData();
				return convertDbType(metaData.getDatabaseProductName().toLowerCase());
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
	@NonNull
	public static String getDbVersion(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");

		IDbService dbService = DbServiceFactory.getDbService(dataSource);
		return dbService.getVersion();
	}

	//endregion


	//region 获取主要数据源对应数据库的信息

	/**
	 * 获取主要数据源对应数据库的类型
	 *
	 * @return 主要数据源对应的数据库类型
	 */
	@NonNull
	public static String getDbType() {
		return getDbType(PrimaryDataSourceHolder.get());
	}

	/**
	 * 获取主要数据源对应数据库的版本号
	 *
	 * @return 主要数据源对应的数据库版本号
	 */
	@NonNull
	public static String getDbVersion() {
		return getDbVersion(PrimaryDataSourceHolder.get());
	}

	//endregion
}
