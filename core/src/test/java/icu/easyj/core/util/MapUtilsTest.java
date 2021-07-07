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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link MapUtils} 测试类
 *
 * @author wangliang181230
 */
class MapUtilsTest {

	@Test
	@SuppressWarnings("all")
	void testComputeIfAbsent() {
		// case: null
		Assertions.assertThrows(NullPointerException.class, () -> {
			MapUtils.computeIfAbsent(null, null, k -> null);
		});

		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();

		// case: absent
		Assertions.assertEquals("1", MapUtils.computeIfAbsent(map, "aaa", k -> {
			sb.append("1");
			return sb.toString();
		}));
		Assertions.assertEquals("1", sb.toString());

		// case: not absent
		Assertions.assertEquals("1", MapUtils.computeIfAbsent(map, "aaa", k -> {
			sb.append("2");
			return sb.toString();
		}));
		Assertions.assertEquals("1", sb.toString());
	}
}
