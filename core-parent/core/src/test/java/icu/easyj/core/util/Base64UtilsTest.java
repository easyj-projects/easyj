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

import java.util.function.Supplier;

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

	/**
	 * @see Base64Utils#normalize(String)
	 */
	@Test
	void testNormalize() {
		String s = "123\r\n%2B%2B";
		Assertions.assertEquals("123++", Base64Utils.normalize(s));

		Assertions.assertEquals("123+", Base64Utils.normalize("123\r\n "));
		Assertions.assertEquals("123+", Base64Utils.normalize("123 "));
	}

	/**
	 * @see Base64Utils#isBase64(CharSequence) Easyj的判断方法
	 * @see Base64#isBase64(CharSequence) Hutool的判断方法
	 */
	@Test
	void testIsBase64() {
		// 尝试校验1000次生成的Base64串
		int n = 1000;
		while (n-- > 0) {
			String str = Base64.encode(RandomUtil.randomString(100));
			if (!Base64Utils.isBase64(str)) {
				throw new RuntimeException("Base64校验失败");
			}
		}

		// case: true
		Assertions.assertFalse(Base64Utils.isBase64("aaaxx")); // 没有补位符时，长度除4的余数不能为1，这个字符串的长度 5 % 4 = 1，所以为false
		Assertions.assertTrue(Base64Utils.isBase64("aaaxxx"));
		Assertions.assertTrue(Base64Utils.isBase64("aaaxxxx"));
		Assertions.assertTrue(Base64Utils.isBase64("aaaxxxxx"));
		Assertions.assertTrue(Base64Utils.isBase64("aaa/xxx="));
		Assertions.assertTrue(Base64Utils.isBase64("aaa+/x=="));

		// case: false
		Assertions.assertFalse(Base64Utils.isBase64("aaaxxx=f"));
		Assertions.assertFalse(Base64Utils.isBase64("aaaxxx="));
		Assertions.assertFalse(Base64Utils.isBase64("aa-xxx=="));
		Assertions.assertFalse(Base64Utils.isBase64("aa_xxx=="));

		// case: Hutool的Base64，支持URL安全的字符
		Assertions.assertFalse(Base64.isBase64("aaaxxx=f")); // hutool已支持对末尾=号的校验
		Assertions.assertTrue(Base64.isBase64("aaaxxx=")); // hutool不校验长度，所以为true
		Assertions.assertTrue(Base64.isBase64("aa-xxx=")); // hutool支持URL安全字符替换
		Assertions.assertTrue(Base64.isBase64("aa_xxx==")); // hutool支持URL安全字符替换
	}

	/**
	 * 性能测试：与Hutool比较性能高低
	 *
	 * @see Base64Utils#isBase64(CharSequence) Easyj的判断方法
	 * @see Base64#isBase64(CharSequence) Hutool的判断方法
	 */
	@Test
	void testIsBase64Performance() {
		testIsBase64Performance("YXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa+oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k111=");
		testIsBase64Performance("YXNkZmFzZGZhc2Rmc2Rmc2Rr啊Yq 我Y5OD1k11312");
	}

	private void testIsBase64Performance(String str) {
		System.out.println(Base64Utils.isBase64(str));

		// 运行次数参数
		int sets = 3;
		int times = 500 * 10000;
		// easyj函数
		Supplier<String> easyjSupplier = () -> {
			Base64Utils.isBase64(str);
			return "easyj";
		};
		// hutool函数
		Supplier<String> hutoolSupplier = () -> {
			Base64.isBase64(str);
			return "hutool";
		};

		// 运行测试，并获取每个函数的耗时
		System.out.println(this.getClass().getSimpleName() + ".testIsBase64():");
		long[] costs = PerformanceTestUtils.execute(sets, times, easyjSupplier, hutoolSupplier);

		// case: 性能比Hutool高
		long costEasyj = costs[0];
		long costHutool = costs[1];
		if (costEasyj > costHutool) {
			throw new RuntimeException("\r\n[WARNING] Easyj的isBase64方法比Hutool的性能要低了，请注意替换实现。");
		}
	}
}
