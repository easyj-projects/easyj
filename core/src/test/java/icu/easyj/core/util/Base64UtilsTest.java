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

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Base64Utils} 测试类
 *
 * @author wangliang181230
 */
class Base64UtilsTest {

	@Test
	void testNormalize() {
		String s = "123\r\n%252B";
		Assertions.assertEquals("123+", Base64Utils.normalize(s));
	}

	@Test
	void testIsBase64() {
		// 尝试校验1000次生成的Base64串
		int n = 1000;
		while (n-- > 0) {
			String str = Base64.encode(RandomUtil.randomString(100));
			if (str.contains("-") || str.contains("_")) {
				throw new RuntimeException("base64校验失败");
			}
		}

		// case: true
		Assertions.assertTrue(Base64Utils.isBase64("aaaxxxff"));
		Assertions.assertTrue(Base64Utils.isBase64("aaa/xxf="));
		Assertions.assertTrue(Base64Utils.isBase64("aaa+/x=="));

		// case: false
		Assertions.assertFalse(Base64Utils.isBase64("aaaxxx=f"));
		Assertions.assertFalse(Base64Utils.isBase64("aaaxxx="));
		Assertions.assertFalse(Base64Utils.isBase64("aa-xxx="));

		// TODO: case: Hutool的校验不严格（添加该测试，确认Hutool是否修复了）
		Assertions.assertTrue(Base64.isBase64("aaaxxx=f"));
		Assertions.assertTrue(Base64.isBase64("aaaxxx="));
		Assertions.assertTrue(Base64.isBase64("aa-xxx=")); // 有问题，Base64不存在字符‘-’
		Assertions.assertTrue(Base64.isBase64("aa_xxx=")); // 有问题，Base64不存在字符‘_’


		//region case: 性能比Hutool高

		String base64 = "YXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa2oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k";

		int count = 10 * 10000;
		long t0, t1;
		long minCostHutool = Long.MAX_VALUE, minCostEasyj = Long.MAX_VALUE, cost;

		t0 = System.nanoTime();
		for (int i = count; i > 0; --i) {
			Base64Utils.isBase64(base64);
		}
		t1 = System.nanoTime();
		cost = t1 - t0;
		if (cost < minCostEasyj) {
			minCostEasyj = cost;
		}

		t0 = System.nanoTime();
		for (int i = count; i > 0; --i) {
			Base64.isBase64(base64);
		}
		t1 = System.nanoTime();
		cost = t1 - t0;
		if (cost < minCostHutool) {
			minCostHutool = cost;
		}

		// case: 性能比Hutool高
		System.out.println(this.getClass().getSimpleName() + ".testIsBase64():");
		System.out.println("cost  easyj: " + minCostEasyj);
		System.out.println("cost hutool: " + minCostHutool);
		Assertions.assertTrue(minCostEasyj < minCostHutool);

		//endregion
	}
}
