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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;

import icu.easyj.core.util.IOUtils;
import icu.easyj.db.dialect.IDbDialect;
import icu.easyj.db.exception.DbDataNotFoundException;
import icu.easyj.db.exception.DbException;
import org.springframework.lang.NonNull;

/**
 * 数据库服务接口
 *
 * @author wangliang181230
 */
public interface IDbService extends IDbDialect {

	/**
	 * 获取对应的数据源
	 *
	 * @return 对应的数据源
	 */
	DataSource getDataSource();

	/**
	 * 获取数据库的当前时间
	 *
	 * @return 数据库当前时间
	 */
	@NonNull
	default Date now() {
		return new Date(currentTimeMillis());
	}

	/**
	 * 获取数据库的当前毫秒数
	 *
	 * @return 数据库当前毫秒数
	 */
	default long currentTimeMillis() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getDataSource().getConnection();

			// 执行查询
			String sql = getTimeSql();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			// 获取结果
			if (rs.next()) {
				return rs.getTimestamp(1).getTime();
			} else {
				throw new DbDataNotFoundException("没有返回时间数据");
			}
		} catch (SQLException e) {
			throw new DbException("获取数据库时间失败", e);
		} finally {
			IOUtils.close(rs, ps, conn);
		}
	}

	/**
	 * 获取数据库版本号
	 *
	 * @return 数据库版本号
	 */
	@NonNull
	default String getVersion() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getDataSource().getConnection();

			// 执行查询
			String sql = getVersionSql();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			// 获取结果
			if (rs.next()) {
				return rs.getString(1);
			} else {
				throw new DbDataNotFoundException("没有返回数据库版本号信息");
			}
		} catch (SQLException e) {
			throw new DbException("获取数据库版本号失败", e);
		} finally {
			IOUtils.close(rs, ps, conn);
		}
	}
}
