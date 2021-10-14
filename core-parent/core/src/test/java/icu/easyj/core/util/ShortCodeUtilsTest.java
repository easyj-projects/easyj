package icu.easyj.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link ShortCodeUtils} 测试类
 *
 * @author wangliang181230
 */
class ShortCodeUtilsTest {

	@Test
	void testToCodeAndToId() {
		innerTestToCodeAndToId(Long.MAX_VALUE);

		long id = 9999;
		do {
			innerTestToCodeAndToId(id);
			id--;
		}
		while (id >= 0);
	}


	private void innerTestToCodeAndToId(long id) {
		String code = ShortCodeUtils.toCode(id);
		Assertions.assertEquals(id, ShortCodeUtils.toId(code));
	}
}
