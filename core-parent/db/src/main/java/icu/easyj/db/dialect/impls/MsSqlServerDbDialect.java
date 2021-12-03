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

import icu.easyj.core.exception.NotSupportedException;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.db.dialect.IDbDialect;
import icu.easyj.db.util.SqlUtils;

import static icu.easyj.db.constant.DbDriverConstants.MS_SQL_SERVER_DRIVER;
import static icu.easyj.db.constant.DbTypeConstants.MS_SQL_SERVER;

/**
 * MS SQL Server 数据库方言
 *
 * @author wangliang181230
 */
@LoadLevel(name = MS_SQL_SERVER, order = 30)
@DependsOnClass(name = MS_SQL_SERVER_DRIVER)
class MsSqlServerDbDialect implements IDbDialect {

	@Override
	public String getVersionSql() {
		return "SELECT SERVERPROPERTY('PRODUCTVERSION')";
	}

	@Override
	public String getTimeSql() {
		return "SELECT GETDATE()";
	}


	//region 序列相关SQL

	@Override
	public String getSeqCurrValSql(String seqName) {
		// TODO: 待开发
		throw new NotSupportedException("暂不支持MS SQL Server获取当前序列值");
	}

	@Override
	public String getSeqNextValSql(String seqName) {
		return "SELECT NEXT VALUE FOR " + SqlUtils.removeDangerousCharsForSeqName(seqName);
	}

	@Override
	public String getSeqSetValSql(String seqName, long newVal) {
		// TODO: 待开发
		throw new NotSupportedException("暂不支持MS SQL Server设置序列值");
	}

	//endregion


	//-----------------------------------------------------------------------------------------


	@Override
	public String getDbType() {
		return MS_SQL_SERVER;
	}
}
