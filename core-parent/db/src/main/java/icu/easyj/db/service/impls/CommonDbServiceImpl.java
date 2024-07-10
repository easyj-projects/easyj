/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.db.service.impls;

import javax.sql.DataSource;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.loader.EnhancedServiceNotFoundException;
import icu.easyj.db.dialect.DbDialectAdapter;
import icu.easyj.db.dialect.IDbDialect;
import icu.easyj.db.service.IDbService;
import icu.easyj.db.util.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 通用数据库服务
 *
 * @author wangliang181230
 */
public class CommonDbServiceImpl extends DbDialectAdapter implements IDbService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonDbServiceImpl.class);

	protected final DataSource dataSource;


	public CommonDbServiceImpl(DataSource dataSource) {
		super(() -> {
			String dbType = DbUtils.getDbType(dataSource);
			try {
				return EnhancedServiceLoader.load(IDbDialect.class, dbType);
			} catch (EnhancedServiceNotFoundException e) {
				LOGGER.error("未找到数据库 '{}' 的方言服务，请使用 `{}` 的方式自行实现，请参照MySQL方言实现类：MySqlDbDialect",
						dbType, EnhancedServiceLoader.class.getSimpleName());
				throw new EnhancedServiceNotFoundException("未找到数据库 '" + dbType + "' 的方言服务", e);
			}
		});

		Assert.notNull(dataSource, "'dataSource' must not be null");
		this.dataSource = dataSource;
	}


	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}
}
