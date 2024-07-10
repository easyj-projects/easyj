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
package icu.easyj.spring.boot.middleware.dwz.server;

import javax.annotation.Resource;

import icu.easyj.middleware.dwz.server.core.controller.DwzRedirectController;
import icu.easyj.middleware.dwz.server.core.controller.DwzRestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * {@link DwzServerApplication} 测试类
 *
 * @author wangliang181230
 */
@SpringBootTest
@ActiveProfiles("unittest")
public class DwzServerApplicationTest {

	@Resource
	DwzRestController dwzRestController;
	@Resource
	DwzRedirectController dwzRedirectController;

	@Test
	public void testStartup() {
		Assertions.assertNotNull(dwzRestController);
		Assertions.assertNotNull(dwzRedirectController);

		System.out.println(dwzRestController.getClass());
		System.out.println(dwzRedirectController.getClass());
	}

	@Test
	public void testLogging() {
		LoggerFactory.getLogger(this.getClass()).error("测试日志功能: {}", "aaa", new Exception("故意输出堆栈日志"));
	}
}
