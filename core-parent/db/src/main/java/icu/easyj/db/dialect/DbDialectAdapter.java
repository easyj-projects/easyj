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
package icu.easyj.db.dialect;

import java.util.function.Supplier;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 数据库方言适配器
 *
 * @author wangliang181230
 */
public class DbDialectAdapter implements IDbDialect {

	@NonNull
	protected final IDbDialect dbDialect;


	public DbDialectAdapter(Supplier<IDbDialect> dbDialectSupplier) {
		this(dbDialectSupplier.get());
	}

	public DbDialectAdapter(IDbDialect dbDialect) {
		Assert.notNull(dbDialect, "'dbDialect' must not be null");
		this.dbDialect = dbDialect;
	}


	@Override
	public String getVersionSql() {
		return this.dbDialect.getVersionSql();
	}

	@Override
	public String getTimeSql() {
		return this.dbDialect.getTimeSql();
	}

	@Override
	public String getSeqCurrValSql(String seqName) {
		return this.dbDialect.getSeqCurrValSql(seqName);
	}

	@Override
	public String getSeqNextValSql(String seqName) {
		return this.dbDialect.getSeqNextValSql(seqName);
	}

	@Override
	public String getSeqSetValSql(String seqName, long newVal) {
		return this.dbDialect.getSeqSetValSql(seqName, newVal);
	}


	//-----------------------------------------------------------------------------------------


	@NonNull
	@Override
	public String getDbType() {
		return this.dbDialect.getDbType();
	}
}
