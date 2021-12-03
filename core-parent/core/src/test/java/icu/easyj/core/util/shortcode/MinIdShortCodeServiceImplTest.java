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
package icu.easyj.core.util.shortcode;

import icu.easyj.core.constant.DateConstants;
import icu.easyj.core.util.shortcode.impls.MinIdShortCodeServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * {@link MinIdShortCodeServiceImpl} 测试类
 *
 * @author wangliang181230
 */
public class MinIdShortCodeServiceImplTest {

	long minId = 1000000000000L;

	IShortCodeService shortCodeService = new MinIdShortCodeServiceImpl(minId);


	@Test
	public void testToCodeAndToId() {
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
