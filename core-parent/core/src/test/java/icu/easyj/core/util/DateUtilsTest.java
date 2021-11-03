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
package icu.easyj.core.util;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link DateUtils} 测试类
 *
 * @author wangliang181230
 */
class DateUtilsTest {

	@Test
	void testToString() {
		Date time;

		// month
		time = DateUtils.parseAll("2021/01");
		Assertions.assertEquals("2021-01-01", DateUtils.toString(time));

		// date
		time = DateUtils.parseAll("2021/01/01");
		Assertions.assertEquals("2021-01-01", DateUtils.toString(time));
		time = DateUtils.parseAll("2021/01/01 00:00");
		Assertions.assertEquals("2021-01-01", DateUtils.toString(time));

		// minutes
		time = DateUtils.parseAll("2021/01/01 01:02");
		Assertions.assertEquals("2021-01-01 01:02", DateUtils.toString(time));
		time = DateUtils.parseAll("2021/01/01 01:02:00");
		Assertions.assertEquals("2021-01-01 01:02", DateUtils.toString(time));

		// seconds
		time = DateUtils.parseAll("2021/01/01 01:02:03");
		Assertions.assertEquals("2021-01-01 01:02:03", DateUtils.toString(time));
		time = DateUtils.parseAll("2021/01/01 01:02:03.000");
		Assertions.assertEquals("2021-01-01 01:02:03", DateUtils.toString(time));

		// milliseconds
		time = DateUtils.parseAll("2021/01/01 01:02:00.123");
		Assertions.assertEquals("2021-01-01 01:02:00.123", DateUtils.toString(time));
	}
}
