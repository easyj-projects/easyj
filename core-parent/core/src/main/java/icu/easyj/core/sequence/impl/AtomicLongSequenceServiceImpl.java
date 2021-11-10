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
package icu.easyj.core.sequence.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import icu.easyj.core.sequence.ISequenceService;
import icu.easyj.core.util.MapUtils;
import org.springframework.lang.NonNull;

/**
 * 基于 {@link AtomicLong} 实现的内存序列服务
 * <p>
 * 主要用途：
 * 1）单机单应用运行时才能使用此序列服务。
 * 2）运行单元测试时，用于模拟Mock序列服务。
 * <p>
 * 特点：性能高。
 * <p>
 * 使用说明：应用启动时，需要指定其初始值。
 *
 * @author wangliang181230
 */
public class AtomicLongSequenceServiceImpl implements ISequenceService {

	private final Map<String, AtomicLong> atomicLongMap = new ConcurrentHashMap<>();

	@Override
	public long currVal(@NonNull String seqName) {
		return this.getAtomicLong(seqName).get();
	}

	@Override
	public long nextVal(@NonNull String seqName) {
		return this.getAtomicLong(seqName).incrementAndGet();
	}

	@Override
	public long setVal(@NonNull String seqName, long newVal) {
		return this.getAtomicLong(seqName).getAndSet(newVal);
	}


	public AtomicLong getAtomicLong(String seqName) {
		return MapUtils.computeIfAbsent(atomicLongMap, seqName, k -> new AtomicLong(0L));
	}
}
