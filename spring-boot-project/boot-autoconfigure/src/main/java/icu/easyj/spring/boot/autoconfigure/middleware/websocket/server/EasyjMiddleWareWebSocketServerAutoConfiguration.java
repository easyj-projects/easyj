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
package icu.easyj.spring.boot.autoconfigure.middleware.websocket.server;

import icu.easyj.middleware.websocket.server.core.service.WebSocketService;
import icu.easyj.middleware.websocket.server.core.test.controller.TestController;
import icu.easyj.middleware.websocket.server.core.test.controller.TestRestController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * EasyJ中间件：WebSocket服务端自动装配类
 *
 * @author wangliang181230
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WebSocketService.class)
@ConditionalOnProperty(value = "easyj.middleware.websocket.server.enabled", matchIfMissing = true)
public class EasyjMiddleWareWebSocketServerAutoConfiguration {

	@Bean
	public WebSocketService defaultWebSocketService() {
		return new WebSocketService();
	}


	/**
	 * 调试所需的测试功能
	 */
	@Lazy
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(name = "easyj.env.run-mode", havingValue = "debug")
	@Import({TestRestController.class, TestController.class})
	static class WebSocketServerTestConfiguration {
	}
}
