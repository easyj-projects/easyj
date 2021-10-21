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
package icu.easyj.db.dialect.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.db.dialect.IDbDialect;
import org.springframework.lang.NonNull;

import static icu.easyj.db.constant.DbDriverConstants.MYSQL_DRIVER;
import static icu.easyj.db.constant.DbTypeConstants.MYSQL;

/**
 * MySQL数据库方言
 *
 * @author wangliang181230
 */
@LoadLevel(name = MYSQL, order = 10)
@DependsOnClass(name = MYSQL_DRIVER)
class MySqlDbDialect implements IDbDialect {

	@NonNull
	@Override
	public String getVersionSql() {
		return "SELECT VERSION()";
	}

	@NonNull
	@Override
	public String getTimeSql() {
		return "SELECT CURRENT_TIMESTAMP(3)";
	}


	//-----------------------------------------------------------------------------------------


	@NonNull
	@Override
	public String getDbType() {
		return MYSQL;
	}
}
