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
package icu.easyj.db.util.impls;

import javax.sql.DataSource;

import cn.hutool.core.lang.Assert;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.db.dialect.DbDialectAdapter;
import icu.easyj.db.dialect.IDbDialect;
import icu.easyj.db.util.DbUtils;
import icu.easyj.db.util.IDbService;

/**
 * 通用数据库服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = "common")
public class CommonDbServiceImpl extends DbDialectAdapter implements IDbService {

	protected final DataSource dataSource;


	public CommonDbServiceImpl(DataSource dataSource) {
		super(EnhancedServiceLoader.load(IDbDialect.class, DbUtils.getDbType(dataSource)));

		Assert.notNull(dataSource, "'dataSource' must not be null");
		this.dataSource = dataSource;
	}


	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}
}
