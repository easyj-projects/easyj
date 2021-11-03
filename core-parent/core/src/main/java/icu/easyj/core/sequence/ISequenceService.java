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

import icu.easyj.core.exception.NotSupportedException;
import org.springframework.lang.NonNull;

/**
 * 序列服务
 *
 * @author wangliang181230
 */
public interface ISequenceService {

	/**
	 * 获取当前序列值
	 *
	 * @param seqName 序列名
	 * @return 当前序列值
	 * @throws NotSupportedException 部分实现无法设置序列值，将抛出该异常
	 */
	long currVal(@NonNull String seqName);

	/**
	 * 获取下一序列值
	 *
	 * @param seqName 序列名
	 * @return 下一序列值
	 */
	long nextVal(@NonNull String seqName);

	/**
	 * 设置序列值
	 *
	 * @param seqName 序列名
	 * @param newVal  新的序列值
	 * @return previousVal 前序列值：null=没有序列值 | -1=未知
	 * @throws NotSupportedException 部分实现无法设置序列值，将抛出该异常
	 */
	long setVal(@NonNull String seqName, long newVal);
}
