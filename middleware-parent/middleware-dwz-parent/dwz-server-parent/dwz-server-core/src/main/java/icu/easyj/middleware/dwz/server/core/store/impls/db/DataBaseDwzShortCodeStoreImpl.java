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
package icu.easyj.middleware.dwz.server.core.store.impls.db;

import javax.sql.DataSource;

import cn.hutool.core.lang.Assert;
import icu.easyj.db.util.DbUtils;
import icu.easyj.middleware.dwz.server.core.store.IDwzShortCodeStore;

/**
 * 基于数据库实现的 {@link IDwzShortCodeStore}
 *
 * @author wangliang181230
 */
public class DataBaseDwzShortCodeStoreImpl implements IDwzShortCodeStore {

	private final DataSource dataSource;


	public DataBaseDwzShortCodeStoreImpl(DataSource dataSource) {
		Assert.notNull(dataSource, "'dataSource' must not be null");
		this.dataSource = dataSource;
	}


	@Override
	public long nextShortUrlCodeId() {
		// 目前基于数据库序列来获取下一个ID
		return DbUtils.seqNextVal(this.dataSource, "SEQ_SHORT_URL_CODE");
	}
}
