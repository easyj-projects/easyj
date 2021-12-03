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
package icu.easyj.spring.boot.middleware.all.server;

import javax.annotation.Resource;

import icu.easyj.middleware.dwz.server.core.controller.DwzRedirectController;
import icu.easyj.middleware.dwz.server.core.controller.DwzRestController;
import icu.easyj.middleware.websocket.server.core.service.WebSocketService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@link AllMiddleWareServerApplication} 测试类
 *
 * @author wangliang181230
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("unittest")
public class AllMiddleWareServerApplicationTest {

	// dwz
	@Resource
	DwzRestController dwzRestController;
	@Resource
	DwzRedirectController dwzRedirectController;

	// websocket
	@Resource
	WebSocketService webSocketService;

	@Test
	public void testStartup() {
		Assertions.assertNotNull(dwzRestController);
		Assertions.assertNotNull(dwzRedirectController);

		Assertions.assertNotNull(webSocketService);
	}
}
