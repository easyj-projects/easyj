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
package icu.easyj.middleware.dwz.server.core.service.impls;

import icu.easyj.core.exception.NotSupportedException;
import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.middleware.dwz.server.core.service.IDwzCorrectErrorDataService;
import icu.easyj.middleware.dwz.server.core.store.IDwzLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static icu.easyj.middleware.dwz.server.core.store.IDwzLogStore.SEQ_NAME__DWZ_LOG_ID;

/**
 * {@link IDwzCorrectErrorDataService} 的默认实现
 *
 * @author wangliang181230
 */
public class DefaultDwzCorrectErrorDataServiceImpl implements IDwzCorrectErrorDataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDwzCorrectErrorDataServiceImpl.class);


	private final IDwzLogStore dwzLogStore;

	private final ISequenceService sequenceService;


	public DefaultDwzCorrectErrorDataServiceImpl(IDwzLogStore dwzLogStore, ISequenceService sequenceService) {
		this.dwzLogStore = dwzLogStore;
		this.sequenceService = sequenceService;
	}


	@Override
	public int correctSequence() {
		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("");
				LOGGER.info("==>");
				LOGGER.info("开始纠正序列值，当前序列服务：{}，对应的序列名：{}。", this.sequenceService.getClass().getSimpleName(), SEQ_NAME__DWZ_LOG_ID);
			}

			// 获取序列的当前值
			long currVal = this.seqCurrValOrNextVal();

			// 获取当前logStore中的maxId
			Long maxId;
			try {
				maxId = dwzLogStore.getMaxId();
			} catch (NotSupportedException e) {
				LOGGER.warn("当前短链接记录存取服务不支持获取最大ID，无法确认当前序列值是否正常。", e);
				return -1;
			}
			if (maxId == null) {
				maxId = 0L;
			}

			// 如果序列值比maxId要小，则设置序列值到maxId的值
			if (currVal < maxId) {
				return this.initSeqVal(currVal, maxId);
			} else {
				LOGGER.info("目前无需纠正序列值！当前序列值：{}，当前最大ID值：{}，该序列服务不会生成出已存在的ID。", currVal, maxId == 0L ? "无数据" : maxId);
				return 0;
			}
		} finally {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("<==");
				LOGGER.info("");
			}
		}
	}


	/**
	 * 获取当前序列值或下一序列值
	 *
	 * @return 序列值
	 */
	private long seqCurrValOrNextVal() {
		try {
			return sequenceService.currVal(SEQ_NAME__DWZ_LOG_ID);
		} catch (NotSupportedException e) {
			// 如果不支持获取当前值，则获取下一个值。这个方法是序列服务必须支持的方法。
			return sequenceService.nextVal(SEQ_NAME__DWZ_LOG_ID);
		}
	}

	/**
	 * 初始化序列值
	 *
	 * @param currVal 当前序列值
	 * @param maxId   当前最大ID
	 */
	private int initSeqVal(long currVal, final long maxId) {
		try {
			sequenceService.setVal(SEQ_NAME__DWZ_LOG_ID, maxId);
			LOGGER.warn("序列[{}]的当前值 [{}] 小于短链接记录最大ID值 [{}]，现已初始化序列值为 [{}]。避免生成已存在的ID，导致创建短链接记录失败。",
					SEQ_NAME__DWZ_LOG_ID, currVal, maxId, maxId);
			return 1;
		} catch (NotSupportedException e) {
			// 如果相差值在1000以内，则进行循环递增纠正序列值。否则会太消耗时间，导致项目处于假死状态
			if (maxId - currVal < 1000) {
				return this.correctSequenceValue(currVal, maxId);
			} else {
				LOGGER.error("序列[{}]的当前值太小 [{}]，无法快速纠正到短链接记录最大ID值 [{}]，请尽快手动纠正!", SEQ_NAME__DWZ_LOG_ID, currVal, maxId);
				return -1;
			}
		}
	}

	/**
	 * 循环递增纠正序列值
	 *
	 * @param currVal 当前序列值
	 * @param maxId   最大ID值
	 */
	private int correctSequenceValue(final long currVal, final long maxId) {
		LOGGER.warn("序列[{}]的当前值 [{}] 小于短链接记录最大ID值 [{}]，但当前序列服务不支持 `setVal`，现正在进行序列值累加操作，直到序列值达到最大ID值为止...",
				SEQ_NAME__DWZ_LOG_ID, currVal, maxId);

		// 判断是否为递减序列
		long currVal0 = sequenceService.nextVal(SEQ_NAME__DWZ_LOG_ID);
		if (currVal0 < currVal) {
			LOGGER.warn("序列[{}]为递减序列，暂不支持纠正递减序列的值！", SEQ_NAME__DWZ_LOG_ID);
			return -1;
		}

		// 循环递增，慢慢纠正序列值
		while (currVal0 < maxId) {
			currVal0 = sequenceService.nextVal(SEQ_NAME__DWZ_LOG_ID);
		}

		LOGGER.warn("序列[{}]的当前值已达到 [{}]，服务已经可以继续服务可继续运行了！", SEQ_NAME__DWZ_LOG_ID, currVal);
		return 1;
	}
}
