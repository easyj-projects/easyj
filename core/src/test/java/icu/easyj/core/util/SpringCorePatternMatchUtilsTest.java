package icu.easyj.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.PatternMatchUtils;

/**
 * 测试 spring-core中的 {@link PatternMatchUtils}
 *
 * @author wangliang181230
 */
class SpringCorePatternMatchUtilsTest {

	@Test
	void testSimpleMatch() {
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
