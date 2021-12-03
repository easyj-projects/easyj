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
package icu.easyj.middleware.dwz.server.core.store.impls.mock;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.core.util.shortcode.ShortCodeUtils;
import icu.easyj.middleware.dwz.server.core.domain.entity.DwzLogEntity;
import icu.easyj.middleware.dwz.server.core.domain.enums.DwzLogStatus;
import icu.easyj.middleware.dwz.server.core.store.IDwzLogStore;
import org.springframework.dao.DuplicateKeyException;

/**
 * 模拟 {@link IDwzLogStore}
 *
 * @author wangliang181230
 */
public class MockDwzLogStoreImpl implements IDwzLogStore {

	private final Map<Long, DwzLogEntity> dwzLogMap = new ConcurrentHashMap<>();

	private final ISequenceService sequenceService;


	public MockDwzLogStoreImpl(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}


	@Override
	public synchronized DwzLogEntity save(String longUrl, Date termOfValidity) {
		// 通过序列服务，获取下一序列值，作为ID
		long id = this.sequenceService.nextVal(SEQ_NAME__DWZ_LOG_ID);
		if (dwzLogMap.containsKey(id)) {
			throw new DuplicateKeyException("ID已经存在：" + id);
		}

		// ID 转换为 短字符串，即：短链接码（注：可通过 `ShortCodeUtils.toId(code)` 方法转换回ID）
		String shortUrlCode = ShortCodeUtils.toCode(id);

		// 获取当前时间
		Date now = new Date();

		// 数据创建成功，创建entity并返回
		DwzLogEntity dwzLog = new DwzLogEntity();
		dwzLog.setId(id);
		dwzLog.setShortUrlCode(shortUrlCode);
		dwzLog.setLongUrl(longUrl);
		dwzLog.setTermOfValidity(termOfValidity);
		dwzLog.setStatus(DwzLogStatus.EFFECTIVE);
		dwzLog.setCreateTime(now);
		dwzLog.setUpdateTime(now);
		dwzLog.setVersion(1);

		dwzLogMap.put(id, dwzLog);

		return dwzLog;
	}

	@Override
	public synchronized DwzLogEntity getByLongUrlForUpdate(String longUrl) {
		for (DwzLogEntity entity : dwzLogMap.values()) {
			if (longUrl.equals(entity.getLongUrl())) {
				return entity;
			}
		}
		return null;
	}

	@Override
	public synchronized void update(DwzLogEntity dwzLog) {
		// do nothing：数据取出来直接对象修改了，无需更新
	}

	@Override
	public synchronized String getLongUrlByShortUrlCode(String shortUrlCode) {
		for (DwzLogEntity entity : dwzLogMap.values()) {
			if (entity.isStatus(DwzLogStatus.EFFECTIVE) && shortUrlCode.equals(entity.getShortUrlCode())) {
				return entity.getLongUrl();
			}
		}
		return null;
	}

	@Override
	public synchronized Long getMaxId() {
		Long maxId = null;
		for (Long id : dwzLogMap.keySet()) {
			if (maxId == null || maxId < id) {
				maxId = id;
			}
		}
		return maxId;
	}

	@Override
	public synchronized int deleteOvertime() {
		int count = 0;
		Date now = new Date();
		for (DwzLogEntity entity : dwzLogMap.values()) {
			if (entity.getTermOfValidity() != null && entity.getTermOfValidity().compareTo(now) <= 0) {
				dwzLogMap.remove(entity.getId());
				count++;
			}
		}
		return count;
	}

	@Override
	public synchronized int updateOvertime() {
		int count = 0;
		Date now = new Date();
		for (DwzLogEntity entity : dwzLogMap.values()) {
			if (entity.getTermOfValidity() != null
					&& entity.getTermOfValidity().compareTo(now) <= 0
					&& entity.isNotStatus(DwzLogStatus.Expired)) {
				entity.setStatus(DwzLogStatus.Expired);
				count++;
			}
		}
		return count;
	}
}
