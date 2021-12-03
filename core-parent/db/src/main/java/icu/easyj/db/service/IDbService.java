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

import icu.easyj.core.exception.NotSupportedException;
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
			conn = this.getDataSource().getConnection();
			conn.setAutoCommit(true);

			// 执行查询
			String sql = this.getVersionSql();
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


	//region 数据库时间

	/**
	 * 获取数据库的当前时间戳
	 *
	 * @return 数据库当前时间戳
	 */
	default long currentTimeMillis() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getDataSource().getConnection();
			conn.setAutoCommit(true);

			// 执行查询
			String sql = this.getTimeSql();
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
	 * 获取数据库的当前时间
	 *
	 * @return 数据库当前时间
	 */
	@NonNull
	default Date now() {
		return new Date(currentTimeMillis());
	}

	//endregion


	//region 序列值：当前序列值、下一序列值、设置序列值

	/**
	 * 获取当前序列值
	 *
	 * @param seqName 序列名
	 * @return 当前序列值
	 * @throws NotSupportedException 部分实现无法设置序列值，将抛出该异常
	 */
	default long seqCurrVal(String seqName) {
		String sql = this.getSeqCurrValSql(seqName);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getDataSource().getConnection();
			conn.setAutoCommit(true);

			// 执行查询
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			// 获取结果
			if (rs.next()) {
				return rs.getLong(1);
			} else {
				throw new DbDataNotFoundException("没有返回当前序列值：" + seqName);
			}
		} catch (SQLException e) {
			throw new DbException("获取当前序列值失败：" + seqName, e);
		} finally {
			IOUtils.close(rs, ps, conn);
		}
	}

	/**
	 * 获取下一序列值
	 *
	 * @param seqName 序列名
	 * @return 下一序列值
	 */
	default long seqNextVal(String seqName) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getDataSource().getConnection();
			conn.setAutoCommit(true);

			// 执行查询
			String sql = this.getSeqNextValSql(seqName);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			// 获取结果
			if (rs.next()) {
				return rs.getLong(1);
			} else {
				throw new DbDataNotFoundException("没有返回下一序列值：" + seqName);
			}
		} catch (SQLException e) {
			throw new DbException("获取下一序列值失败：" + seqName, e);
		} finally {
			IOUtils.close(rs, ps, conn);
		}
	}

	/**
	 * 设置序列值，并返回原序列值
	 *
	 * @param seqName 序列名
	 * @param newVal  新的序列值
	 * @return previousVal 前序列值
	 * @throws NotSupportedException 部分实现无法设置序列值，将抛出该异常
	 */
	default long seqSetVal(String seqName, long newVal) {
		String sql = this.getSeqSetValSql(seqName, newVal);

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getDataSource().getConnection();
			conn.setAutoCommit(true);

			// 执行查询
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			// 获取结果
			if (rs.next()) {
				return rs.getLong(1);
			} else {
				throw new DbDataNotFoundException("没有返回原序列值：" + seqName);
			}
		} catch (SQLException e) {
			throw new DbException("设置序列值失败：" + seqName, e);
		} finally {
			IOUtils.close(rs, ps, conn);
		}
	}

	//endregion
}
