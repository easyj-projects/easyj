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

import javax.sql.DataSource;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Snowflake;
import icu.easyj.core.util.shortcode.ShortCodeUtils;
import icu.easyj.middleware.dwz.server.core.store.IDwzShortCodeStore;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 基于数据库实现的 {@link IDwzShortCodeStore}
 *
 * @author wangliang181230
 */
public class DataBaseDwzShortCodeStoreImpl implements IDwzShortCodeStore {

	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;
	private final Snowflake snowflake;


	public DataBaseDwzShortCodeStoreImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, Snowflake snowflake) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		Assert.notNull(jdbcTemplate, "'jdbcTemplate' must not be null");
		Assert.notNull(snowflake, "'snowflake' must not be null");

		this.dataSource = dataSource;
		this.jdbcTemplate = jdbcTemplate;
		this.snowflake = snowflake;
	}

	public DataBaseDwzShortCodeStoreImpl(DataSource dataSource, Snowflake snowflake) {
		this(dataSource, new JdbcTemplate(dataSource), snowflake);
	}


	@Override
	public String createShortUrlCode() {
		// todo: 临时用雪花生成ID，再通过ShortCodeUtils转为短字符串。但这种方式生成的短字符串比较长，后面有空再修改为数据库序列生成ID
		return ShortCodeUtils.toCode(snowflake.nextId());
	}
}
