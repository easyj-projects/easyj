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
import icu.easyj.db.util.SqlUtils;
import org.springframework.lang.NonNull;

import static icu.easyj.db.constant.DbDriverConstants.MYSQL_DRIVER;
import static icu.easyj.db.constant.DbTypeConstants.MYSQL;

/**
 * MySQL数据库方言
 *
 * @author wangliang181230
 * @see <a href="https://gitee.com/easyj-projects/easyj/blob/develop/src/script/db/mysql/mysql__sequence-table_and_function.sql">参照需要创建的表和函数的SQL文件</a>
 * @see <a href="https://github.com/easyj-projects/easyj/blob/develop/src/script/db/mysql/mysql__sequence-table_and_function.sql">参照需要创建的表和函数的SQL文件</a>
 */
@LoadLevel(name = MYSQL, order = 10)
@DependsOnClass(name = MYSQL_DRIVER)
class MySqlDbDialect implements IDbDialect {

	@Override
	public String getVersionSql() {
		return "SELECT VERSION()";
	}

	@Override
	public String getTimeSql() {
		return "SELECT CURRENT_TIMESTAMP(3)";
	}

	/**
	 * 获取当前序列号的SQL
	 *
	 * @param seqName 序列名
	 * @return 当前序列号
	 */
	@Override
	public String getSeqCurrValSql(String seqName) {
		return "SELECT func_currval('" + SqlUtils.removeDangerousCharsForSeqName(seqName) + "')";
	}

	/**
	 * 获取下一个序列号的SQL
	 *
	 * @param seqName 序列名
	 * @return 下一个序列号
	 */
	@Override
	public String getSeqNextValSql(String seqName) {
		return "SELECT func_nextval('" + SqlUtils.removeDangerousCharsForSeqName(seqName) + "')";
	}

	/**
	 * 设置序列值的SQL
	 *
	 * @param seqName 序列名
	 * @param val     指定序列值
	 * @return previousVal 原序列值（为null表示原来的序列不存在）
	 */
	@Override
	public String getSeqSetValSql(String seqName, long val) {
		return "SELECT func_setval('" + SqlUtils.removeDangerousCharsForSeqName(seqName) + "', " + val + ")";
	}


	//-----------------------------------------------------------------------------------------


	@NonNull
	@Override
	public String getDbType() {
		return MYSQL;
	}
}
