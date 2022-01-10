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
package icu.easyj.core.sequence;

import icu.easyj.core.exception.NotSupportedException;
import icu.easyj.core.loader.EnhancedServiceLoader;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 序列工具
 * <p>
 * 如果想要使用自己的实现，可通过set方法设置，也可通过 EnhancedServiceLoader 在/META-INF/services目录下增加实现，并设置order值小于999
 *
 * @author wangliang181230
 */
public abstract class SequenceUtils {

	//region 序列服务

	public static ISequenceService sequenceService;

	public static ISequenceService getSequenceService() {
		if (sequenceService == null) {
			sequenceService = EnhancedServiceLoader.load(ISequenceService.class);
		}

		return sequenceService;
	}

	public static void setSequenceService(ISequenceService sequenceService) {
		SequenceUtils.sequenceService = sequenceService;
	}

	//endregion


	/**
	 * 获取当前序列值
	 *
	 * @param seqName 序列名
	 * @return 当前序列值
	 * @throws NotSupportedException 部分实现无法设置序列值，将抛出该异常
	 */
	public static long currVal(@NonNull String seqName) {
		Assert.notNull(seqName, "'seqName' must be not null");
		return getSequenceService().currVal(seqName);
	}

	/**
	 * 获取下一序列值
	 *
	 * @param seqName 序列名
	 * @return 下一序列值
	 */
	public static long nextVal(@NonNull String seqName) {
		Assert.notNull(seqName, "'seqName' must be not null");
		return getSequenceService().nextVal(seqName);
	}

	/**
	 * 设置序列值
	 *
	 * @param seqName 序列名
	 * @param newVal  新的序列值
	 * @return previousVal 前序列值
	 * @throws NotSupportedException 部分实现无法设置序列值，将抛出该异常
	 */
	public static long setVal(@NonNull String seqName, long newVal) {
		Assert.notNull(seqName, "'seqName' must be not null");
		return getSequenceService().setVal(seqName, newVal);
	}
}
