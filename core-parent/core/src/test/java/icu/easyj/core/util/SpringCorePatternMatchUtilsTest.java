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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.PatternMatchUtils;

/**
 * 测试 spring-core中的 {@link PatternMatchUtils}
 *
 * @author wangliang181230
 */
public class SpringCorePatternMatchUtilsTest {

	@Test
	public void testSimpleMatch() {
		// case: one pattern
		String pattern = "1122.*3";
		Assertions.assertTrue(PatternMatchUtils.simpleMatch(pattern, "1122.a\\lkj?!@#$^*()/*3"));
		Assertions.assertFalse(PatternMatchUtils.simpleMatch(pattern, "112232"));

		// case: some patterns
		String[] patterns = new String[]{"aabbcc", "1122*3"};
		Assertions.assertTrue(PatternMatchUtils.simpleMatch(patterns, "1122xx3"));
		Assertions.assertFalse(PatternMatchUtils.simpleMatch(patterns, "aabbc"));
	}
}
