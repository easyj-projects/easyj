/*
 * Copyright 2021-2022 the original author or authors.
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
package com.alibaba.fastjson.serializer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link EasyjFastjsonBugfixUtils} 测试类
 *
 * @author wangliang181230
 */
public class EasyjFastjsonBugfixUtilsTest {

	@Test
	public void testIsLoopholeVersion() {
		// case: false，即：不存在漏洞的版本
		Assertions.assertFalse(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.1.31.sec10"));
		Assertions.assertFalse(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68.sec10"));
		Assertions.assertFalse(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68.sec11"));
		Assertions.assertFalse(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68.sec999"));
		Assertions.assertFalse(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.69"));
		Assertions.assertFalse(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.69-SNAPSHOT"));
		Assertions.assertFalse(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.78"));
		Assertions.assertFalse(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.78-SNAPSHOT"));

		// case: true，即：存在漏洞的版本
		Assertions.assertTrue(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68"));
		Assertions.assertTrue(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68-SNAPSHOT"));
		Assertions.assertTrue(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68.sec1"));
		Assertions.assertTrue(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68.sec01"));
		Assertions.assertTrue(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68_sec01"));
		Assertions.assertTrue(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68.sec9"));
		Assertions.assertTrue(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68.sec09"));
		Assertions.assertTrue(EasyjFastjsonBugfixUtils.isLoopholeVersion("1.2.68_sec09"));
	}
}
