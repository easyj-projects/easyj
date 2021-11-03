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
			LOGGER.info("==>");
			LOGGER.info("开始纠正序列值，当前序列服务：{}", this.sequenceService.getClass().getSimpleName());

			// 获取序列的当前值
			long currVal = this.seqCurrValOrNextVal();

			// 获取当前logStore中的maxId
			Long maxId = dwzLogStore.getMaxId();
			if (maxId == null) {
				maxId = 0L;
			}

			// 如果序列值比maxId要小，则初始化序列值为maxId
			if (currVal < maxId) {
				this.initSeqVal(currVal, maxId);
				return 1;
			} else {
				LOGGER.info("不需要纠正序列值，当前序列值：{}，当前最大ID值：{}。", currVal, maxId);
				return 0;
			}
		} catch (NotSupportedException e) {
			LOGGER.warn("当前序列服务或短链接记录存取服务不支持检查序列值与最大ID", e);
			return -1;
		} catch (Exception e) {
			LOGGER.error("检查序列的值与短链接记录最大ID时出现异常", e);
			return -1;
		} finally {
			LOGGER.info("<==");
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
			// 如果不支持获取当前值，则获取下一个值
			return sequenceService.nextVal(SEQ_NAME__DWZ_LOG_ID);
		}
	}

	/**
	 * 初始化序列值
	 *
	 * @param currVal 当前序列值
	 * @param maxId   当前最大ID
	 */
	private void initSeqVal(long currVal, final long maxId) {
		try {
			sequenceService.setVal(SEQ_NAME__DWZ_LOG_ID, maxId);
			LOGGER.warn("当前序列值 [{}] 小于短链接记录最大ID值 [{}]，现已初始化序列值为最大ID值。避免生成已存在的ID，导致创建短链接记录失败。", currVal, maxId);
		} catch (NotSupportedException e) {
			LOGGER.warn("当前序列值 [{}] 小于短链接记录最大ID值 [{}]，但当前序列服务不支持 `setVal`，现正在进行序列值累加操作，直到序列值达到最大ID值为止", currVal, maxId);
			while (currVal < maxId) {
				currVal = sequenceService.nextVal(SEQ_NAME__DWZ_LOG_ID);
			}
			LOGGER.warn("当前序列值已达到 [{}]，服务可继续运行了！", currVal);
		}
	}
}
