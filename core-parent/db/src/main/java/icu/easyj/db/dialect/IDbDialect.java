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

import icu.easyj.core.dialect.IDialect;
import icu.easyj.db.constant.DbType;
import org.springframework.lang.NonNull;

/**
 * 数据库方言
 *
 * @author wangliang181230
 */
public interface IDbDialect extends IDialect {

	/**
	 * 获取数据库版本号的SQL
	 *
	 * @return 获取版本号SQL
	 */
	@NonNull
	String getVersionSql();

	/**
	 * 获取数据库当前时间的SQL（不同数据库SQL语句不同）
	 *
	 * @return 获取时间SQL
	 */
	@NonNull
	String getTimeSql();


	//-----------------------------------------------------------------------------------------


	/**
	 * 获取数据库类型
	 *
	 * @return 数据库类型（要求只包含 数字 和 小写英文字母）
	 */
	@NonNull
	String getDbType();

	/**
	 * 判断数据库类型
	 *
	 * @param dbType 数据库类型
	 * @return 是否匹配
	 */
	default boolean isDbType(String dbType) {
		return getDbType().equalsIgnoreCase(dbType);
	}

	/**
	 * 判断数据库类型
	 *
	 * @param dbType 数据库类型枚举
	 * @return 是否匹配
	 */
	default boolean isDbType(DbType dbType) {
		if (dbType == null) {
			return false;
		}

		return isDbType(dbType.name());
	}
}
