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
package icu.easyj.core.trace.impls;

import icu.easyj.core.trace.TraceService;
import icu.easyj.core.util.MapUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

/**
 * {@link MDCTraceServiceImpl} 测试类
 *
 * @author wangliang181230
 */
public class MDCTraceServiceImplTest {

	@Test
	@Disabled("mdcAdapter 为 NOPMDCAdapter，导致无法测试")
	public void test() {
		TraceService ts = new MDCTraceServiceImpl();

		// case: put key-value
		ts.put("aaa", "1");
		Assertions.assertEquals("1", MDC.get("aaa"));

		// case: put map
		ts.put(MapUtils.quickMap("bbb", "2", "ccc", "3"));
		Assertions.assertEquals("2", MDC.get("bbb"));
		Assertions.assertEquals("3", MDC.get("ccc"));

		// case: remove
		ts.remove("aaa");
		Assertions.assertNull(MDC.get("aaa"));
		Assertions.assertEquals("2", MDC.get("bbb"));
		Assertions.assertEquals("3", MDC.get("ccc"));
	}
}
