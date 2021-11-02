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
package icu.easyj.core.sequence;

import icu.easyj.core.loader.EnhancedServiceLoader;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 序列工具
 *
 * @author wangliang181230
 */
public class SequenceUtils {

	public static final ISequenceService SEQUENCE_SERVICE = EnhancedServiceLoader.load(ISequenceService.class);

	/**
	 * 获取下一序列值
	 *
	 * @param seqName 序列名
	 * @return 下一序列值
	 */
	public static long nextVal(@NonNull String seqName) {
		Assert.notNull(seqName, "'seqName' must be not null");
		return SEQUENCE_SERVICE.nextVal(seqName);
	}

	/**
	 * 获取当前序列值
	 *
	 * @param seqName 序列名
	 * @return 当前序列值
	 */
	public static long currVal(@NonNull String seqName) {
		Assert.notNull(seqName, "'seqName' must be not null");
		return SEQUENCE_SERVICE.currVal(seqName);
	}
}
