package org.easyframework.core.util;

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
