package icu.easyj.core.util.shortcode;

import icu.easyj.core.constant.DateConstants;
import icu.easyj.core.util.shortcode.impls.MinIdShortCodeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link MinIdShortCodeServiceImpl} 测试类
 *
 * @author wangliang181230
 */
class MinIdShortCodeServiceImplTest {

	long minId = 1000000000000L;

	IShortCodeService shortCodeService = new MinIdShortCodeServiceImpl(minId);


	@Test
	void testToCodeAndToId() {
		int range = 20;

		long now = System.currentTimeMillis();
		long nextMonth = now + 26 * 31 * DateConstants.ONE_DAY_MILL;

		for (long i = 0L; i <= 99; i++) {
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				shortCodeService.toCode(1L);
			});
		}
		innerTestToCodeAndToId(minId, minId + 100);
		innerTestToCodeAndToId(now - range, now); // 11位：时间戳
		innerTestToCodeAndToId(nextMonth - range, nextMonth); // 11位：时间戳
		innerTestToCodeAndToId(Long.MAX_VALUE - range, Long.MAX_VALUE); // max - range, max

		Assertions.assertEquals(minId, shortCodeService.toId("A"));
	}

	private void innerTestToCodeAndToId(long minId, long maxId) {
		if (minId < 0) {
			minId = 0;
		}

		for (long i = minId; i >= 0 && i <= maxId; ++i) {
			innerTestToCodeAndToId(i);
		}
	}

	private void innerTestToCodeAndToId(long id) {
		String code = shortCodeService.toCode(id);
		System.out.println(id + " -> " + code + " (" + code.length() + ")");
		Assertions.assertEquals(id, shortCodeService.toId(code));
	}
}
