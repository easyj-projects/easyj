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

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * {@link UrlUtils} 测试类
 *
 * @author wangliang181230
 */
public class UrlUtilsTest {

	static final String BASE_URL = "http://github.com/easyj-projects/easyj";

	@Test
	public void testNormalizePath() {
		//case: null
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			UrlUtils.normalizePath(null);
		});

		//case: empty
		Assertions.assertEquals("/", UrlUtils.normalizePath(""));
		//case: 都是斜杆
		Assertions.assertEquals("/", UrlUtils.normalizePath("///  \\\\"));
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

	@Test
	public void testJoinQueryString() {
		String url = BASE_URL;
		Assertions.assertEquals(url, UrlUtils.joinQueryString(url, null));
		Assertions.assertEquals(BASE_URL + "?a=%E5%95%8A", UrlUtils.joinQueryString(url, MapUtils.quickMap("a", "啊")));
		Assertions.assertEquals(BASE_URL + "?a=%E5%95%8A&b=%E5%93%A6", UrlUtils.joinQueryString(url, MapUtils.quickMap("a", "啊", "b", "哦")));

		url = BASE_URL + "?x=1";
		Assertions.assertEquals(url, UrlUtils.joinQueryString(url, null));
		Assertions.assertEquals(BASE_URL + "?x=1&a=%E5%95%8A", UrlUtils.joinQueryString(url, MapUtils.quickMap("a", "啊")));
		Assertions.assertEquals(BASE_URL + "?x=1&a=%E5%95%8A&b=%E5%93%A6", UrlUtils.joinQueryString(url, MapUtils.quickMap("a", "啊", "b", "哦")));

		url = BASE_URL + "?x=1#xyz";
		Assertions.assertEquals(url, UrlUtils.joinQueryString(url, null));
		Assertions.assertEquals(BASE_URL + "?x=1&a=%E5%95%8A#xyz", UrlUtils.joinQueryString(url, MapUtils.quickMap("a", "啊")));
		Assertions.assertEquals(BASE_URL + "?x=1&a=%E5%95%8A&b=%E5%93%A6#xyz", UrlUtils.joinQueryString(url, MapUtils.quickMap("a", "啊", "b", "哦")));
	}
}
