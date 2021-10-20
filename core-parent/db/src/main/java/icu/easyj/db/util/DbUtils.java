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
import javax.sql.DataSource;

import icu.easyj.db.exception.DbException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库工具类
 *
 * @author wangliang181230
 */
public abstract class DbUtils {

	/**
	 * 从数据源中获取数据库类型
	 *
	 * @param dataSource 数据源
	 * @return 数据库类型
	 */
	@NonNull
	public static String getDbType(@NonNull DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");

		try (Connection con = dataSource.getConnection()) {
			DatabaseMetaData metaData = con.getMetaData();
			return metaData.getDatabaseProductName();
		} catch (SQLException t) {
			throw new DbException("获取数据库类型");
		}
	}
}
