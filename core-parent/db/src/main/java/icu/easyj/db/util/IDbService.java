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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;

import icu.easyj.core.util.IOUtils;
import icu.easyj.db.exception.DbDataNotFoundException;
import icu.easyj.db.exception.DbException;
import org.springframework.lang.NonNull;

/**
 * 数据库相关接口
 *
 * @author wangliang181230
 */
public interface IDbService {

	/**
	 * 获取数据库当前时间的SQL（不同数据库SQL语句不同）
	 *
	 * @param dataSource 数据源
	 * @return 获取时间SQL
	 */
	@NonNull
	String getTimeSql(DataSource dataSource);

	/**
	 * 获取数据库的当前时间
	 *
	 * @param dataSource 数据源
	 * @return 数据库当前时间
	 */
	@NonNull
	default Date now(DataSource dataSource) {
		return new Date(currentTimeMillis(dataSource));
	}

	/**
	 * 获取数据库的当前毫秒数
	 *
	 * @param dataSource 数据源
	 * @return 数据库当前毫秒数
	 */
	default long currentTimeMillis(DataSource dataSource) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(true);

			String sql = getTimeSql(dataSource);

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getTimestamp(0).getTime();
			} else {
				throw new DbDataNotFoundException("没有返回时间数据");
			}
		} catch (SQLException e) {
			throw new DbException("获取数据库时间失败", e);
		} finally {
			IOUtils.close(rs, ps, conn);
		}
	}
}
