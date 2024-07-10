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
package icu.easyj.db.sequence.impls;

import javax.sql.DataSource;

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.db.util.DbUtils;
import icu.easyj.db.util.PrimaryDbUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 基于 {@link PrimaryDbUtils} 实现的序列服务
 *
 * @author wangliang181230
 */
public class DataBaseSequenceServiceImpl implements ISequenceService {

	@Nullable
	private final DataSource dataSource;


	public DataBaseSequenceServiceImpl() {
		this(null);
	}

	public DataBaseSequenceServiceImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	@Override
	public long nextVal(@NonNull String seqName) {
		if (this.dataSource != null) {
			return DbUtils.seqNextVal(this.dataSource, seqName);
		} else {
			return PrimaryDbUtils.seqNextVal(seqName);
		}
	}

	@Override
	public long currVal(@NonNull String seqName) {
		if (this.dataSource != null) {
			return DbUtils.seqCurrVal(this.dataSource, seqName);
		} else {
			return PrimaryDbUtils.seqCurrVal(seqName);
		}
	}

	@Override
	public long setVal(@NonNull String seqName, long newVal) {
		if (this.dataSource != null) {
			return DbUtils.seqSetVal(this.dataSource, seqName, newVal);
		} else {
			return PrimaryDbUtils.seqSetVal(seqName, newVal);
		}
	}
}
