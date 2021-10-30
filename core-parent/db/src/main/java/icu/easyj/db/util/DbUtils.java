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
import java.util.Date;
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
 * <p>
 * 获取数据库的一些信息：类型、版本、时间、序列值...等等
 *
 * @author wangliang181230
 * @see DbServiceFactory
 */
public abstract class DbUtils {

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


	//region 数据库信息

	/**
	 * 从数据源中获取数据库类型<br>
	 * 值域：mysql、oracle、ms_sql_server、...（TODO: 其他数据库待补充）
	 *
	 * @param dataSource 数据源
	 * @return 数据库类型（全部转为小写字母）
	 */
	@NonNull
	public static String getDbType(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");

		return MapUtils.computeIfAbsent(DB_TYPE_MAP, dataSource, ds -> {
			try (Connection conn = dataSource.getConnection()) {
				DatabaseMetaData metaData = conn.getMetaData();
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


	//region 数据库时间

	/**
	 * 获取数据库当前时间戳
	 * <p>
	 * 注意：与DbClockUtils的实现不同，DbClockUtils是基于记号时钟来快速计算出数据库的当前时间的。
	 *
	 * @param dataSource 数据源
	 * @return 数据库当前时间戳
	 */
	@NonNull
	public static long currentTimeMillis(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		IDbService dbService = DbServiceFactory.getDbService(dataSource);
		return dbService.currentTimeMillis();
	}

	/**
	 * 获取数据库当前时间
	 * <p>
	 * 注意：与DbClockUtils的实现不同，DbClockUtils是基于记号时钟来快速计算出数据库的当前时间的。
	 *
	 * @param dataSource 数据源
	 * @return 数据库当前时间
	 */
	@NonNull
	public static Date now(@NonNull DataSource dataSource) {
		return new Date(currentTimeMillis(dataSource));
	}

	//endregion


	//region 序列值：当前序列值、下一序列值、设置序列值

	/**
	 * 获取当前序列值
	 * <p>
	 * MySQL支持度较高
	 * FIXME: Oracle存在连接池中的连接第一次调用时，会抛异常，此时会自动调用seqNextVal方法代替，但会导致序列+1。  其他数据库暂不支持。
	 *
	 * @param dataSource 数据源
	 * @param seqName    序列名
	 * @return 当前序列值
	 */
	public static long seqCurrVal(DataSource dataSource, String seqName) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		IDbService dbService = DbServiceFactory.getDbService(dataSource);
		return dbService.seqCurrVal(seqName);
	}

	/**
	 * 获取下一序列值
	 *
	 * @param dataSource 数据源
	 * @param seqName    序列名
	 * @return 下一序列值
	 */
	public static long seqNextVal(DataSource dataSource, String seqName) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		IDbService dbService = DbServiceFactory.getDbService(dataSource);
		return dbService.seqNextVal(seqName);
	}

	/**
	 * 设置序列值，并返回原序列值
	 * <p>
	 * FIXME: 除了MySQL数据库（自建表+函数实现）以外，其他数据库暂不支持！
	 *
	 * @param dataSource 数据源
	 * @param seqName    序列名
	 * @param val        指定序列值
	 * @return previousVal 原序列值
	 */
	public static long seqSetVal(DataSource dataSource, String seqName, long val) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		IDbService dbService = DbServiceFactory.getDbService(dataSource);
		return dbService.seqSetVal(seqName, val);
	}

	//endregion
}
