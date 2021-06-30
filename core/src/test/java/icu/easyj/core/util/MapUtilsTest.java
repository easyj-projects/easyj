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
