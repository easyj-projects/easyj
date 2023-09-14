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
import org.springframework.lang.NonNull;

import static icu.easyj.db.constant.DbDriverConstants.DM_DRIVER;
import static icu.easyj.db.constant.DbTypeConstants.DM;

/**
 * dm数据库方言
 *
 * @author wangliang181230
 * @since 0.7.7
 */
@LoadLevel(name = DM, order = 10)
@DependsOnClass(name = {DM_DRIVER}, strategy = ValidateStrategy.ANY_ONE)
class DmDbDialect implements IDbDialect {

	@Override
	public String getVersionSql() {
		return "SELECT banner FROM v$version LIMIT 1";
	}

	@Override
	public String getTimeSql() {
		return "SELECT CURRENT_TIMESTAMP(3)";
	}


	//region 序列相关SQL

	@Override
	public String getSeqCurrValSql(String seqName) {
		// TODO: 待开发
		throw new NotSupportedException("暂不支持DM设置序列值");
	}

	@Override
	public String getSeqNextValSql(String seqName) {
		// TODO: 待开发
		throw new NotSupportedException("暂不支持DM设置序列值");
	}

	@Override
	public String getSeqSetValSql(String seqName, long newVal) {
		// TODO: 待开发
		throw new NotSupportedException("暂不支持DM设置序列值");
	}

	//endregion


	//-----------------------------------------------------------------------------------------


	@NonNull
	@Override
	public String getDbType() {
		return DM;
	}
}
