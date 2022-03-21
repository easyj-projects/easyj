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
package icu.easyj.core.trace.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.core.trace.TraceService;
import org.slf4j.MDC;

/**
 * 基于 {@link MDC} 的追踪服务。用于记录日志。
 *
 * @author wangliang181230
 */
@LoadLevel(name = "MDC", order = 1)
@DependsOnClass({MDC.class})
public class MDCTraceServiceImpl implements TraceService {

	@Override
	public void put(String key, String value) {
		if (key == null) {
			return;
		}
		MDC.put(key, value);
	}

	@Override
	public void remove(String key) {
		if (key == null) {
			return;
		}
		MDC.remove(key);
	}

	@Override
	public void clear() {
		MDC.clear();
	}
}
