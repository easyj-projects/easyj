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
package icu.easyj.core.util.jar;

import java.util.jar.Attributes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * {@link JarInfo} 测试类
 *
 * @author wangliang181230
 */
public class JarInfoTest {

	static final JarInfo JAR_INFO = new JarInfo("C://a:a.jar", "groupA", "nameA", new Attributes(), "1.0");

	@Test
	public void testCompareToVersion() {
		Assertions.assertEquals(1, JAR_INFO.compareToVersion("0.999"));
		Assertions.assertEquals(0, JAR_INFO.compareToVersion("1.000"));
		Assertions.assertEquals(-1, JAR_INFO.compareToVersion("1.999"));
	}

	@Test
	public void testBetweenVersion() {
		// case: true
		Assertions.assertTrue(JAR_INFO.betweenVersion("0.999", "1.000"));
		Assertions.assertTrue(JAR_INFO.betweenVersion("0.999", "1.999"));
		Assertions.assertTrue(JAR_INFO.betweenVersion("1.000", "1.999"));

		// case: false
		Assertions.assertFalse(JAR_INFO.betweenVersion("1.999", "2.000"));
		Assertions.assertFalse(JAR_INFO.betweenVersion("0.001", "0.999"));
	}

	@Test
	public void testNotBetweenVersion() {
		// case: false
		Assertions.assertFalse(JAR_INFO.notBetweenVersion("0.999", "1.000"));
		Assertions.assertFalse(JAR_INFO.notBetweenVersion("0.999", "1.999"));
		Assertions.assertFalse(JAR_INFO.notBetweenVersion("1.000", "1.999"));

		// case: true
		Assertions.assertTrue(JAR_INFO.notBetweenVersion("1.999", "2.000"));
		Assertions.assertTrue(JAR_INFO.notBetweenVersion("0.001", "0.999"));
	}
}
