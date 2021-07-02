/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CollectionUtils} 测试类
 *
 * @author wangliang181230
 */
class CollectionUtilsTest {

	@Test
	@SuppressWarnings("all")
	void testGetLast() {
		// case: null
		Assertions.assertNull(CollectionUtils.getLast(null));

		// case: empty
		Assertions.assertNull(CollectionUtils.getLast(Collections.EMPTY_LIST));

		// case: not empty
		List<String> list = new ArrayList<>();
		list.add("Foo");
		list.add("Bar");
		Assertions.assertEquals("Bar", CollectionUtils.getLast(list));
	}
}
