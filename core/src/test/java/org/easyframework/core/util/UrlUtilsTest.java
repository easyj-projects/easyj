package org.easyframework.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * URLUtils测试类
 *
 * @author wangliang181230
 */
public class UrlUtilsTest {

	@Test
	@SuppressWarnings("all")
	void testNormalizePath() {
		//case: null
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			UrlUtils.normalizePath(null);
		});

		//case: empty
		Assertions.assertEquals("/", UrlUtils.normalizePath(""));
		//case: 都是斜杆
		Assertions.assertEquals("/", UrlUtils.normalizePath("///\\\\"));
		//case: 前面没有”/“
		Assertions.assertEquals("/abc", UrlUtils.normalizePath("abc"));
		//case: 后面有”/“
		Assertions.assertEquals("/abc", UrlUtils.normalizePath("abc/"));
		//case: 后面有多个”/“
		Assertions.assertEquals("/abc", UrlUtils.normalizePath("abc////"));
		//case: 有”\“
		Assertions.assertEquals("/abc/bb", UrlUtils.normalizePath("abc\\bb/\\\\\\"));
		//case: http完整路径
		Assertions.assertEquals("/abc/bb", UrlUtils.normalizePath("http://a.com/abc\\bb/\\\\\\"));
		//case: https完整路径
		Assertions.assertEquals("/abc/bb", UrlUtils.normalizePath("https:\\\\a.com/abc\\bb/\\\\\\"));
	}
}
