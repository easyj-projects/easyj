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
package icu.easyj.core.logging;

import icu.easyj.core.modelfortest.TestClass;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

/**
 * {@link EnhancedLogger} 测试类
 *
 * @author wangliang181230
 */
class EnhancedLoggerTest {

	@Test
	void test() {
		Logger logger = EnhancedLoggerFactory.getLogger(this.getClass());

		Object logParam1 = new TestClass("aaa");
		Object logParam2 = new TestClass("bbb");

		logger.info("arg1: {}, arg2: {}, arg3: {}", 1, LogUtils.wrap(logParam1), LogUtils.wrap(logParam2));
	}
}
