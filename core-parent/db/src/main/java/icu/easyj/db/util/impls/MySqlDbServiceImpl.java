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

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import org.springframework.lang.NonNull;

/**
 * MySql数据库服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = "mysql", order = 10)
@DependsOnClass(name = "com.mysql.cj.jdbc.Driver")
class MySqlDbServiceImpl extends AbstractDbServiceImpl {

	public MySqlDbServiceImpl(DataSource dataSource) {
		super(dataSource);
	}


	@NonNull
	@Override
	public String getTimeSql() {
		return "SELECT CURRENT_TIMESTAMP() FROM DUAL";
	}

	@NonNull
	@Override
	public String getVersionSql() {
		return "SELECT VERSION()";
	}
}
