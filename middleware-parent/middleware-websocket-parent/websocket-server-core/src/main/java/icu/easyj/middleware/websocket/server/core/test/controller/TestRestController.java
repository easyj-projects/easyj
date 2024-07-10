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
package icu.easyj.middleware.websocket.server.core.test.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试发送消息的接口
 *
 * @author wangliang181230
 */
@Lazy
@RestController
public class TestRestController {

	@GetMapping("/send")
	public String send(@RequestParam String channel, // 路由
					   @RequestParam String msg, // 要发送的消息内容
					   @RequestParam(required = false) String id) { // 业务数据id
		return "推送成功-String";
	}

	@GetMapping("/sendInt")
	public String sendInt(@RequestParam String channel, // 路由
						  @RequestParam Integer msg, // 要发送的消息内容
						  @RequestParam(required = false) String id) { // 业务数据id
		return "推送成功-int";
	}

	@GetMapping("/sendBoolean")
	public String sendBoolean(@RequestParam String channel, // 路由
							  @RequestParam Boolean msg, // 要发送的消息内容
							  @RequestParam(required = false) String id) { // 业务数据id
		return "推送成功-boolean";
	}
}
