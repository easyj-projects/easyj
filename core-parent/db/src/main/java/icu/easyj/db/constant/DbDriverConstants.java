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
package icu.easyj.db.constant;

/**
 * 数据库驱动常量
 *
 * @author wangliang181230
 */
public interface DbDriverConstants {

	String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
	String MYSQL_DRIVER_OLD = "com.mysql.jdbc.Driver";

	String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	String ORACLE_DRIVER_OLD = "oracle.jdbc.driver.OracleDriver";

	String MS_SQL_SERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
}
