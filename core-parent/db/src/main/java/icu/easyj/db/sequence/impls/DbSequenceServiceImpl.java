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
package icu.easyj.db.sequence.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.db.util.PrimaryDbUtils;
import org.springframework.lang.NonNull;

/**
 * 基于 {@link PrimaryDbUtils} 实现的序列服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = "db", order = 1000)
public class DbSequenceServiceImpl implements ISequenceService {

	@Override
	public long nextVal(@NonNull String seqName) {
		return PrimaryDbUtils.seqNextVal(seqName);
	}

	@Override
	public long currVal(@NonNull String seqName) {
		return PrimaryDbUtils.seqCurrVal(seqName);
	}
}
