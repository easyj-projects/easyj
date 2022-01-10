/*
 * Copyright 2021-2022 the original author or authors.
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
import icu.easyj.core.loader.condition.ValidateStrategy;
import icu.easyj.db.dialect.IDbDialect;
import icu.easyj.db.util.SqlUtils;
import org.springframework.lang.NonNull;

import static icu.easyj.db.constant.DbDriverConstants.ORACLE_DRIVER;
import static icu.easyj.db.constant.DbDriverConstants.ORACLE_DRIVER_OLD;
import static icu.easyj.db.constant.DbTypeConstants.ORACLE;

/**
 * Oracle数据库方言
 *
 * @author wangliang181230
 */
@LoadLevel(name = ORACLE, order = 20)
@DependsOnClass(name = {ORACLE_DRIVER, ORACLE_DRIVER_OLD}, strategy = ValidateStrategy.ANY_ONE)
class OracleDbDialect implements IDbDialect {

	@Override
	public String getVersionSql() {
		return "SELECT version FROM product_component_version WHERE product LIKE 'Oracle%'";
	}

	@Override
	public String getTimeSql() {
		return "SELECT SYSTIMESTAMP FROM DUAL";
	}


	//region 序列相关SQL

	@Override
	public String getSeqCurrValSql(String seqName) {
		return "SELECT " + SqlUtils.removeDangerousCharsForSeqName(seqName).toUpperCase() + ".CURRVAL FROM DUAL";
	}

	@Override
	public String getSeqNextValSql(String seqName) {
		return "SELECT " + SqlUtils.removeDangerousCharsForSeqName(seqName).toUpperCase() + ".NEXTVAL FROM DUAL";
	}

	@Override
	public String getSeqSetValSql(String seqName, long newVal) {
		// TODO: 待开发，参考：https://www.cnblogs.com/mq0036/p/13151770.html
		throw new NotSupportedException("暂不支持Oracle设置序列值");
	}

	//endregion


	//-----------------------------------------------------------------------------------------


	@NonNull
	@Override
	public String getDbType() {
		return ORACLE;
	}
}
