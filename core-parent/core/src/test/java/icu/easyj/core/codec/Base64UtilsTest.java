/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.core.codec;

import java.nio.charset.StandardCharsets;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.JavaInfo;
import cn.hutool.system.SystemUtil;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.StringUtils;
import icu.easyj.test.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static icu.easyj.core.loader.ServiceProviders.HUTOOL;

/**
 * {@link Base64Utils} 测试类
 *
 * @author wangliang181230
 */
public class Base64UtilsTest {

	/**
	 * 测试Base64规范化方法
	 *
	 * @see Base64Utils#normalize(String)
	 */
	@Test
	public void testNormalize() {
		String s = "123\r\n%2B%2B";
		Assertions.assertEquals("123++", Base64Utils.normalize(s));

		Assertions.assertEquals("123+", Base64Utils.normalize("123\r\n "));
		Assertions.assertEquals("123+", Base64Utils.normalize("123 "));
	}

	@Test
	public void testIsBase64Bytes() {
		// 尝试校验20000次生成的Base64串
		int n = 20000;
		while (n-- > 0) {
			String base64Str = RandomUtil.randomString(RandomUtil.randomInt(20, 30));
			byte[] bytes = Base64.encode(StrUtil.utf8Bytes(base64Str), false);
			// 测试isBase64方法
			if (!Base64Utils.isBase64Bytes(bytes)) {
				throw new RuntimeException("Base64校验失败：" + base64Str);
			}
		}
	}

	@Test
	public void testIsBase64Chars() {
		// 尝试校验20000次生成的Base64串
		int n = 20000;
		while (n-- > 0) {
			String base64Str = RandomUtil.randomString(RandomUtil.randomInt(20, 30));
			char[] chars = Base64.encode(base64Str).toCharArray();
			// 测试isBase64方法
			if (!Base64Utils.isBase64Chars(chars)) {
				throw new RuntimeException("Base64校验失败：" + base64Str);
			}
		}
	}

	//region test isBase64

	/**
	 * 测试isBase64方法
	 *
	 * @see Base64Utils#isBase64(CharSequence) EasyJ的判断方法
	 * @see Base64#isBase64(CharSequence) Hutool的判断方法
	 */
	@Test
	public void testIsBase64() {
		// 尝试校验20000次生成的Base64串
		int n = 20000;
		while (n-- > 0) {
			CharSequence base64Str = Base64.encode(RandomUtil.randomString(RandomUtil.randomInt(20, 30)));
			// java版本为9及以上时，校验String.coder值，肯定为0
			if (SystemUtil.getJavaInfo().getVersionFloat() >= 9) {
				// 判断String.coder是否为0，如果为1，则抛出异常
				if (StringUtils.getCoder(base64Str) == 1) {
					throw new RuntimeException("Base64字符串的coder不能为1：" + base64Str);
				}
			}
			// 测试isBase64方法
			if (!Base64Utils.isBase64(base64Str)) {
				throw new RuntimeException("Base64校验失败：" + base64Str);
			}
		}

		// case: true
		Assertions.assertTrue(Base64Utils.isBase64("aaaxxx"));
		Assertions.assertTrue(Base64Utils.isBase64("aaaxxxx"));
		Assertions.assertTrue(Base64Utils.isBase64("aaaxxxxx"));
		Assertions.assertTrue(Base64Utils.isBase64("aaa/xxx="));
		Assertions.assertTrue(Base64Utils.isBase64("aaa+/x=="));

		// case: false
		Assertions.assertFalse(Base64Utils.isBase64("aaaxx")); // 没有补位符时，长度除4的余数不能为1，这个字符串的长度 5 % 4 = 1，所以为false
		Assertions.assertFalse(Base64Utils.isBase64("aaaxxx=f"));
		Assertions.assertFalse(Base64Utils.isBase64("aaaxx=x="));
		Assertions.assertFalse(Base64Utils.isBase64("aaaxxx="));
		Assertions.assertFalse(Base64Utils.isBase64("aa-xxx=="));
		Assertions.assertFalse(Base64Utils.isBase64("aa_xxx=="));

		// case: Hutool的Base64，支持UrlSafe的字符：'-'、'_'
		Assertions.assertFalse(Base64.isBase64("aaaxxx=f")); // hutool已支持对末尾=号的校验
		Assertions.assertTrue(Base64.isBase64("aaaxxx=")); // hutool不校验长度，所以为true
		Assertions.assertTrue(Base64.isBase64("aa-xxx=")); // hutool支持URL安全字符替换
		Assertions.assertTrue(Base64.isBase64("aa_xxx==")); // hutool支持URL安全字符替换
	}

	/**
	 * 性能测试：与Hutool比较性能高低
	 *
	 * @see Base64Utils#isBase64(CharSequence) EasyJ的判断方法
	 * @see Base64#isBase64(CharSequence) Hutool的判断方法
	 */
	@Test
	public void testIsBase64Performance() {
		IBase64Service base64Service = EnhancedServiceLoader.load(IBase64Service.class);
		System.out.println("\r\n" + IBase64Service.class.getSimpleName() + "实现类：" + base64Service.getClass().getName());

		// 校验类型
		JavaInfo javaInfo = SystemUtil.getJavaInfo();
		if (javaInfo.getVersionFloat() < 1.9) {
			Assertions.assertEquals("icu.easyj.core.codec.impls.Jdk8Base64ServiceImpl", base64Service.getClass().getName());
		} else if (javaInfo.getVersionFloat() < 16) {
			Assertions.assertEquals("icu.easyj.core.codec.impls.Jdk9To15Base64ServiceImpl", base64Service.getClass().getName());
		} else {
			Assertions.assertEquals("icu.easyj.core.codec.impls.Jdk16ToLatestBase64ServiceImpl", base64Service.getClass().getName());
		}

		// case: isBase64(str) == true && 长
		CharSequence s1 = "YXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa+oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k111aYXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa+oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k111aYXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa+oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k111aYXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa+oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k111aYXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa+oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k111aYXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa+oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k111=";
		// case: isBase64(str) == true && 短
		CharSequence s2 = "YXNkZmFzZGZhc2Rmc2Rmc2RrZmpsa+oxbDJqM2xrMTJqM2l1OWRzYWY5OD1k111=";
		// case: isBase64(str) == false && 不含双字节字符 && 位数符合
		CharSequence s3 = "YXNkZmFzZGZhc2Rmc2Rmc2Rr1Yq1115OD1k11=1s";
		// case: isBase64(str) == false && 含双字节字符   && 位数符合（此case，java9比java8速度要快非常多）
		CharSequence s4 = "YXNkZmFzZGZhc2Rmc2Rmc2Rr啊Yq 我Y5OD1k11123";
		// case: isBase64(str) == false && 不含双字节字符 && 位数不符合
		CharSequence s5 = "YXNkZmFzZGZhc2Rmc2Rmc2Rr1Yq1115OD1k11";
		// case: isBase64(str) == false && 含双字节字符   && 位数不符合
		CharSequence s6 = "YXNkZmFzZGZhc2Rmc2Rmc2Rr啊Yq 我Y5OD1kx1";

		Assertions.assertTrue(Base64Utils.isBase64(s1));
		Assertions.assertTrue(Base64Utils.isBase64(s2));
		Assertions.assertFalse(Base64Utils.isBase64(s3));
		Assertions.assertFalse(Base64Utils.isBase64(s4));
		Assertions.assertFalse(Base64Utils.isBase64(s5));
		Assertions.assertFalse(Base64Utils.isBase64(s6));

		System.out.println("\r\nJava version: " + javaInfo.getVersionFloat());

		testIsBase64PerformanceOne(1, "case: isBase64(str) == true && length=" + s1.length(), s1);
		testIsBase64PerformanceOne(2, "case: isBase64(str) == true && length=" + s2.length(), s2);
		testIsBase64PerformanceOne(3, "case: isBase64(str) == false && 不含双字节字符 && 位数符合 && length=" + s3.length(), s3);
		testIsBase64PerformanceOne(4, "case: isBase64(str) == false && 含双字节字符 && 位数符合 && length=" + s4.length(), s4);
		testIsBase64PerformanceOne(5, "case: isBase64(str) == false && 不含双字节字符 && 位数不符合 && length=" + s5.length(), s5);
		testIsBase64PerformanceOne(6, "case: isBase64(str) == false && 含双字节字符 && 位数不符合 && length=" + s6.length(), s6);
	}

	private void testIsBase64PerformanceOne(int number, String title, CharSequence cs) {
		System.out.println();
		System.out.println(number + "、" + this.getClass().getSimpleName() + ".testIsBase64(): " + Base64Utils.isBase64(cs) + ": " + cs);
		System.out.println(title);

		// 运行次数
		int threadCount = 5;
		int times = 10 * 10000;
		// 运行测试，并获取每个函数的耗时
		long[] costs = TestUtils.performanceTest(threadCount, times,
				// easyj函数
				() -> {
					Base64Utils.isBase64(cs);
					return "easyj";
				},
				// easyj函数
				() -> {
					isBase64ForJdk16ByBytes(cs);
					return "easyj2";
				},
				// hutool函数
				() -> {
					Base64.isBase64(cs);
					return HUTOOL;
				});

		// case: 性能比Hutool高
		long costEasyj1 = costs[0];
		long costEasyj2 = costs[1];
		long costHutool = costs[2];
		if (costEasyj1 > costHutool) {
			System.out.println("\r\n[WARNING] EasyJ1的isBase64方法比Hutool的性能要低了，请注意替换实现。");
		}
		if (costEasyj1 > costEasyj2) {
			System.out.println("\r\n[WARNING] EasyJ1的isBase64方法比EasyJ2的性能要低了，请注意替换实现。");
		}
	}

	private boolean isBase64ForJdk16ByBytes(CharSequence cs) {
		String str = cs.toString();

		// 获取字符串UTF-8编码的字节数组
		byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

		// 如果长度不一致，说明存在双精度字符，肯定不为Base64
		if (bytes.length != str.length()) {
			return false;
		}

		// 判断字符数组是否为Base64
		return Base64Utils.isBase64Bytes(bytes);
	}

	//endregion
}
