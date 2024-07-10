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

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.core.loader.condition.ValidateStrategy;
import icu.easyj.db.exception.DbException;

import static icu.easyj.db.constant.DbDriverConstants.ORACLE_DRIVER;
import static icu.easyj.db.constant.DbDriverConstants.ORACLE_DRIVER_OLD;
import static icu.easyj.db.constant.DbTypeConstants.ORACLE;

/**
 * Oracle数据库服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = ORACLE, order = 20)
@DependsOnClass(name = {ORACLE_DRIVER, ORACLE_DRIVER_OLD}, strategy = ValidateStrategy.ANY_ONE)
class OracleDbServiceImpl extends CommonDbServiceImpl {

	public OracleDbServiceImpl(DataSource dataSource) {
		super(dataSource);
	}


	@Override
	public long seqCurrVal(String seqName) {
		try {
			return super.seqCurrVal(seqName);
		} catch (DbException e) {
			if (e.getCause() != null && e.getCause().getMessage() != null && e.getCause().getMessage().contains("ORA-08002")) {
				// Oracle在没有获取过序列值的情况下，直接调用`SEQ_NAME.currval`获取当前序列值会抛出ORA-08002的异常，所以此次先调用nextval方法获取一次。
				return this.seqNextVal(seqName);
			}
			throw e;
		}
	}
}
