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
package icu.easyj.middleware.websocket.server.core.test.controller;

import javax.servlet.http.HttpServletRequest;

import icu.easyj.config.EnvironmentConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 测试页面
 *
 * @author wangliang181230
 */
@Lazy
@Controller
public class TestController {

	@Value("${server.port}")
	private Integer serverPort;


	@GetMapping("/test")
	public String test(HttpServletRequest request) {
		request.setAttribute("profileActive", EnvironmentConfigs.getEnv());
		request.setAttribute("serverPort", serverPort);
		return "test";
	}
}
