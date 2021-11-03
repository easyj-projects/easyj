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
package icu.easyj.middleware.dwz.server.core.store.impls.db;

import java.util.Date;
import javax.sql.DataSource;

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.shortcode.ShortCodeUtils;
import icu.easyj.data.store.DbStoreException;
import icu.easyj.db.util.DbClockUtils;
import icu.easyj.middleware.dwz.server.core.domain.entity.DwzLogEntity;
import icu.easyj.middleware.dwz.server.core.store.IDwzLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 基于数据库实现的 {@link IDwzLogStore}
 *
 * @author wangliang181230
 */
public class DataBaseDwzLogStoreImpl implements IDwzLogStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseDwzLogStoreImpl.class);


	//region SQL相关常量 start

	private static final String DWZ_LOG_TABLE_NAME = "easyj_dwz_log";
	private static final String ALL_FIELDS = "id, short_url_code, long_url, term_of_validity, status, create_time, update_time, version";
	private static final String ALL_PLACEHOLDER = StringUtils.join('?', ',', ALL_FIELDS.split(",").length);

	/**
	 * 创建记录的SQL
	 */
	private static final String INSERT_DWZ_LOG_SQL = "INSERT INTO " + DWZ_LOG_TABLE_NAME + " (" + ALL_FIELDS + ") VALUES (" + ALL_PLACEHOLDER + ")";

	/**
	 * 根据long_url获取记录的SQL
	 */
	private static final String GET_DWZ_LOG_SQL = "" +
			"SELECT " + ALL_FIELDS +
			"  FROM " + DWZ_LOG_TABLE_NAME + " AS t" +
			" WHERE t.long_url = ?" +
			"   FOR UPDATE";

	/**
	 * 更新记录的SQL
	 */
	private static final String UPDATE_DWZ_LOG_SQL = "" +
			"UPDATE " + DWZ_LOG_TABLE_NAME + " AS t" +
			"   SET t.term_of_validity = ?," +
			"       t.status = 1," +
			"       t.update_time = ?," +
			"       t.version = t.version + 1" +
			" WHERE t.id = ?";

	/**
	 * 根据short_url_code获取long_url的SQL
	 */
	private static final String GET_LONG_URL_SQL = "" +
			"SELECT long_url" +
			"  FROM " + DWZ_LOG_TABLE_NAME + " AS t" +
			" WHERE t.short_url_code = ?" +
			"   AND t.status = 1";

	/**
	 * 获取最大ID值的SQL
	 */
	private static final String GET_MAX_ID_SQL = "" +
			"SELECT MAX(id) FROM " + DWZ_LOG_TABLE_NAME;


	/**
	 * 删除所有超时记录的SQL
	 * 注：MySql数据库 DELETE 语句不支持设置表别名
	 */
	private static final String DELETE_OVERTIME_SQL = "" +
			"DELETE" +
			"  FROM " + DWZ_LOG_TABLE_NAME +
			" WHERE term_of_validity < ?" +
			"   AND status = 1";

	/**
	 * 更新所有超时记录的SQL
	 */
	private static final String UPDATE_OVERTIME_SQL = "" +
			"UPDATE " + DWZ_LOG_TABLE_NAME + " AS t" +
			"   SET t.status = 2," +
			"       t.update_time = ?," +
			"       t.version = t.verion + 1" +
			" WHERE t.term_of_validity < ?" +
			"   AND t.status = 1";

	//endregion SQL相关常量 end


	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;

	private final ISequenceService sequenceService;


	public DataBaseDwzLogStoreImpl(JdbcTemplate jdbcTemplate, ISequenceService sequenceService) {
		Assert.notNull(jdbcTemplate, "'jdbcTemplate' must not be null");
		Assert.notNull(jdbcTemplate.getDataSource(), "'jdbcTemplate.getDataSource()' must not be null");
		Assert.notNull(sequenceService, "'sequenceService' must not be null");

		this.jdbcTemplate = jdbcTemplate;
		this.dataSource = jdbcTemplate.getDataSource();
		this.sequenceService = sequenceService;

		LOGGER.info("当前用于生成短链接记录ID的序列服务的为：{}，对应的序列名：{}。", sequenceService.getClass().getSimpleName(), SEQ_NAME__DWZ_LOG_ID);
	}


	@NonNull
	@Override
	public DwzLogEntity save(@NonNull String longUrl, @Nullable Date termOfValidity) {
		// 通过序列服务，获取下一序列值，作为ID
		long id = this.sequenceService.nextVal(SEQ_NAME__DWZ_LOG_ID);
		// ID 转换为 短字符串，即：短链接码（注：可通过 `ShortCodeUtils.toId(code)` 方法转换回ID）
		String shortUrlCode = ShortCodeUtils.toCode(id);

		// 使用TickClock快速获取数据库时间
		Date now = DbClockUtils.now(this.dataSource);

		// 数据创建成功，创建entity并返回
		DwzLogEntity dwzLog = new DwzLogEntity();
		dwzLog.setId(id);
		dwzLog.setShortUrlCode(shortUrlCode);
		dwzLog.setLongUrl(longUrl);
		dwzLog.setTermOfValidity(termOfValidity);
		dwzLog.setStatus(1);
		dwzLog.setCreateTime(now);
		dwzLog.setUpdateTime(now);
		dwzLog.setVersion(1);

		int rowCount;
		try {
			// 准备SQL参数
			Object[] args = new Object[]{
					dwzLog.getId(),
					dwzLog.getShortUrlCode(),
					dwzLog.getLongUrl(),
					dwzLog.getTermOfValidity(),
					dwzLog.getStatus(),
					dwzLog.getCreateTime(),
					dwzLog.getUpdateTime(),
					dwzLog.getVersion()
			};

			rowCount = jdbcTemplate.update(INSERT_DWZ_LOG_SQL, args);
		} catch (Exception e) {
			throw new DbStoreException("数据库异常，创建短链接记录失败", e);
		}
		if (rowCount != 1) {
			LOGGER.error("数据库未知异常，短链接记录未创建成功！data = {}", StringUtils.toString(dwzLog));
			throw new DbStoreException("数据库未知异常，短链接记录未创建成功！");
		}

		return dwzLog;
	}

	@Nullable
	@Override
	public DwzLogEntity getByLongUrlForUpdate(@NonNull String longUrl) {
		Assert.notNull(longUrl, "'longUrl' must not be null");
		try {
			return jdbcTemplate.queryForObject(GET_DWZ_LOG_SQL, new BeanPropertyRowMapper<>(DwzLogEntity.class), longUrl);
		} catch (Exception e) {
			if (e instanceof EmptyResultDataAccessException && ((EmptyResultDataAccessException)e).getActualSize() == 0) {
				return null;
			}
			throw new DbStoreException("根据长链接获取短链接记录失败", e);
		}
	}

	@Override
	public void update(@NonNull DwzLogEntity dwzLog) {
		Date now = DbClockUtils.now(this.dataSource);

		dwzLog.setUpdateTime(now);
		dwzLog.setVersion(dwzLog.getVersion() + 1);

		int result;

		try {
			result = jdbcTemplate.update(UPDATE_DWZ_LOG_SQL, dwzLog.getTermOfValidity(), dwzLog.getUpdateTime(), dwzLog.getId());
		} catch (Exception e) {
			throw new DbStoreException("更新短链接记录失败", e);
		}

		if (result != 1) {
			throw new DbStoreException("更新已有的短链接记录失败");
		}
	}

	@Nullable
	@Override
	public String getLongUrlByShortUrlCode(@NonNull String shortUrlCode) {
		try {
			return jdbcTemplate.queryForObject(GET_LONG_URL_SQL, String.class, shortUrlCode);
		} catch (Exception e) {
			if (e instanceof EmptyResultDataAccessException && ((EmptyResultDataAccessException)e).getActualSize() == 0) {
				return null;
			}
			throw new DbStoreException("根据短链接码获取长链接失败", e);
		}
	}

	@Nullable
	@Override
	public Long getMaxId() {
		try {
			return jdbcTemplate.queryForObject(GET_MAX_ID_SQL, Long.class);
		} catch (Exception e) {
			if (e instanceof EmptyResultDataAccessException && ((EmptyResultDataAccessException)e).getActualSize() == 0) {
				return null;
			}
			throw new DbStoreException("获取短链接记录的最大ID值失败", e);
		}
	}

	@Override
	public int deleteOvertime() {
		return jdbcTemplate.update(DELETE_OVERTIME_SQL, DbClockUtils.now(this.dataSource));
	}

	@Override
	public int updateOvertime() {
		Date now = DbClockUtils.now(this.dataSource);
		return jdbcTemplate.update(UPDATE_OVERTIME_SQL, now, now);
	}
}
