package icu.easyj.core.util;

import icu.easyj.core.constant.DateConstants;
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
		int range = 100;

		long now = System.currentTimeMillis();
		long nextMonth = now + 26 * 31 * DateConstants.ONE_DAY_MILL;

		innerTestToCodeAndToId(0, range); // 0~range
		innerTestToCodeAndToId(now - range, now); // 11位：时间戳
		innerTestToCodeAndToId(nextMonth - range, nextMonth); // 11位：时间戳
		innerTestToCodeAndToId(Long.MAX_VALUE - range, Long.MAX_VALUE); // max - range, max
	}

	private void innerTestToCodeAndToId(long minId, long maxId) {
		if (minId < 0) {
			minId = 0;
		}

		long id = maxId;
		do {
			innerTestToCodeAndToId(id);
			id--;
		}
		while (id >= minId);
	}

	private void innerTestToCodeAndToId(long id) {
		try {
			String code = ShortCodeUtils.toCode(id);
			System.out.println(id + " -> " + code + " (" + code.length() + ")");
			Assertions.assertEquals(id, ShortCodeUtils.toId(code));
		} catch (ArrayIndexOutOfBoundsException e) {
			ShortCodeUtils.toCode(id);
			throw e;
		}
	}
}
