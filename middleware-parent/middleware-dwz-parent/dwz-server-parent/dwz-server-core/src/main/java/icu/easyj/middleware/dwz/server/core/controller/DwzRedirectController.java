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
package icu.easyj.middleware.dwz.server.core.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import icu.easyj.core.util.StringUtils;
import icu.easyj.middleware.dwz.server.core.service.IDwzServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 短链接 重定向到 长链接的接口
 *
 * @author wangliang181230
 */
@Controller
public class DwzRedirectController {

	@Autowired
	private IDwzServerService dwzServerService;

	@GetMapping("/{shortUrlCode}")
	public void redirect(@PathVariable String shortUrlCode, HttpServletResponse response) throws IOException {
		if (StringUtils.isBlank(shortUrlCode)) {
			throw new IllegalArgumentException("短链接码不能为空");
		}

		String longUrl = dwzServerService.getLongUrlByShortUrlCode(shortUrlCode.trim());
		if (StringUtils.isBlank(longUrl)) {
			throw new IllegalArgumentException("未找到对应的长链接");
		}

		response.sendRedirect(longUrl.trim());
	}

	@GetMapping("/test/long-url-page")
	@ResponseBody
	public String test() {
		return "这是一个长链接的页面";
	}
}
