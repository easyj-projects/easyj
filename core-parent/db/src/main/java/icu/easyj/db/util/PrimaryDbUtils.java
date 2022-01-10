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
package icu.easyj.db.util;

import java.util.Date;

import icu.easyj.core.exception.NotSupportedException;
import icu.easyj.db.service.DbServiceFactory;
import org.springframework.lang.NonNull;

/**
 * 主要数据库工具类
 * <p>
 * 获取数据库的一些信息：类型、版本、时间、序列值...等等
 *
 * @author wangliang181230
 * @see DbUtils
 * @see DbServiceFactory
 */
public abstract class PrimaryDbUtils {

	//region 数据库信息

	/**
	 * 获取主要数据源对应数据库的类型
	 *
	 * @return 主要数据源对应的数据库类型
	 */
	@NonNull
	public static String getDbType() {
		return DbUtils.getDbType(PrimaryDataSourceHolder.get());
	}

	/**
	 * 获取主要数据源对应数据库的版本号
	 *
	 * @return 主要数据源对应的数据库版本号
	 */
	@NonNull
	public static String getDbVersion() {
		return DbUtils.getDbVersion(PrimaryDataSourceHolder.get());
	}

	//endregion


	//region 数据库时间

	/**
	 * 获取数据库当前时间戳
	 * <p>
	 * 注意：与DbClockUtils的实现不同，DbClockUtils是基于记号时钟来快速计算出数据库的当前时间的。
	 *
	 * @return 数据库当前时间戳
	 */
	@NonNull
	public static long currentTimeMillis() {
		return DbUtils.currentTimeMillis(PrimaryDataSourceHolder.get());
	}

	/**
	 * 获取数据库当前时间
	 * <p>
	 * 注意：与DbClockUtils的实现不同，DbClockUtils是基于记号时钟来快速计算出数据库的当前时间的。
	 *
	 * @return 数据库当前时间
	 */
	@NonNull
	public static Date now() {
		return DbUtils.now(PrimaryDataSourceHolder.get());
	}

	//endregion


	//region 序列值：当前序列值、下一序列值、设置序列值

	/**
	 * 获取当前序列值
	 * <p>
	 * MySQL支持度较高
	 * FIXME: Oracle存在连接池中的连接第一次调用时，会抛异常，此时会自动调用seqNextVal方法代替，但会导致序列+1。  其他数据库暂不支持。
	 *
	 * @param seqName 序列名
	 * @return 当前序列值
	 * @throws NotSupportedException 部分实现无法设置序列值，将抛出该异常
	 */
	public static long seqCurrVal(String seqName) {
		return DbUtils.seqCurrVal(PrimaryDataSourceHolder.get(), seqName);
	}

	/**
	 * 获取下一序列值
	 *
	 * @param seqName 序列名
	 * @return 下一序列值
	 */
	public static long seqNextVal(String seqName) {
		return DbUtils.seqNextVal(PrimaryDataSourceHolder.get(), seqName);
	}

	/**
	 * 设置序列值，并返回原序列值
	 * <p>
	 * FIXME: 除了MySQL数据库（自建表+函数实现）以外，其他数据库暂不支持！
	 *
	 * @param seqName 序列名
	 * @param newVal  新的序列值
	 * @return previousVal 前序列值
	 * @throws NotSupportedException 部分实现无法设置序列值，将抛出该异常
	 */
	public static long seqSetVal(String seqName, long newVal) {
		return DbUtils.seqSetVal(PrimaryDataSourceHolder.get(), seqName, newVal);
	}

	//endregion
}
