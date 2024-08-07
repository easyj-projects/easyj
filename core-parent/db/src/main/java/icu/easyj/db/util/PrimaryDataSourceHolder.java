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
package icu.easyj.db.util;

import javax.sql.DataSource;

/**
 * 主要数据源持有者
 *
 * @author wangliang181230
 */
public abstract class PrimaryDataSourceHolder {

	private static DataSource primaryDataSource;


	/**
	 * 获取主要数据源
	 *
	 * @return 主要数据源
	 */
	public static DataSource get() {
		return primaryDataSource;
	}

	/**
	 * 设置主要数据源
	 *
	 * @param primaryDataSource 主要数据源
	 */
	public static void set(DataSource primaryDataSource) {
		PrimaryDataSourceHolder.primaryDataSource = primaryDataSource;
	}
}
