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

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.middleware.dwz.server.core.store.IDwzShortCodeStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 基于数据库实现的 {@link IDwzShortCodeStore}
 *
 * @author wangliang181230
 */
public class DataBaseDwzShortCodeStoreImpl implements IDwzShortCodeStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseDwzShortCodeStoreImpl.class);


	private final ISequenceService sequenceService;


	public DataBaseDwzShortCodeStoreImpl(@NonNull ISequenceService sequenceService) {
		Assert.notNull(sequenceService, "'sequenceService' must not be null");
		this.sequenceService = sequenceService;

		LOGGER.info("当前用于生成短链接记录ID的序列服务的为：" + sequenceService.getClass().getName());
	}


	@Override
	public long nextShortUrlCodeId() {
		// 目前基于数据库序列来获取下一个ID
		return sequenceService.nextVal("SEQ_SHORT_URL_CODE");
	}
}
