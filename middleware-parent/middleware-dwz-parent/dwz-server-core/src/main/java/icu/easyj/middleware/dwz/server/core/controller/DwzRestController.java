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
package icu.easyj.middleware.dwz.server.core.controller;

import icu.easyj.config.ServerConfigs;
import icu.easyj.core.util.StringUtils;
import icu.easyj.middleware.dwz.domain.EasyjDwzRequest;
import icu.easyj.middleware.dwz.domain.EasyjDwzResponse;
import icu.easyj.middleware.dwz.server.core.domain.entity.DwzLogEntity;
import icu.easyj.middleware.dwz.server.core.service.IDwzServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangliang181230
 */
@RestController
@RequestMapping("/api/v1")
public class DwzRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DwzRestController.class);


	private final IDwzServerService dwzServerService;


	public DwzRestController(IDwzServerService dwzServerService) {
		this.dwzServerService = dwzServerService;
	}


	/**
	 * 生成短链接
	 *
	 * @param param 长链接、过期时间、...等等的参数
	 * @return response 短链接响应
	 */
	@PostMapping("/create-short-url")
	public EasyjDwzResponse createShortUrl(@RequestBody EasyjDwzRequest param) {
		Assert.notNull(param.getLongUrl(), "长链接不能为空");

		// 生成短链接码
		DwzLogEntity dwzLog = dwzServerService.createShortUrlCode(param.getLongUrl(), param.getTermOfValidity());

		// 拼接公网域名后，就是短链接了
		String shortUrl = ServerConfigs.getDomain() + "/" + dwzLog.getShortUrlCode();
		return new EasyjDwzResponse(shortUrl, dwzLog.getCreateTime(), dwzLog.getTermOfValidity());
	}

	/**
	 * 根据短链接，获取对应长链接
	 *
	 * @param shortUrl 短链接
	 * @return longUrl 长链接
	 */
	@Nullable
	@GetMapping("/get-long-url")
	public String getLongUrl(@RequestParam String shortUrl) {
		String shortUrlCode;
		int idx;
		if (shortUrl.startsWith(ServerConfigs.getDomain() + "/")) {
			shortUrlCode = shortUrl.substring(ServerConfigs.getDomain().length() + 1);
		} else if ((idx = shortUrl.lastIndexOf('/')) < shortUrl.length() - 1) {
			// 可能是修改过域名配置，这里只打印一个warn日志
			LOGGER.warn("短域名参数可能不是当前服务生成的，shortUrl = {}", shortUrl);
			shortUrlCode = shortUrl.substring(idx);
		} else {
			LOGGER.warn("非法的短链接，shortUrl = {}", shortUrl);
			throw new IllegalArgumentException("非法的短链接");
		}

		if (StringUtils.isEmpty(shortUrl)) {
			LOGGER.warn("短链接中的短链接码不能为空，shortUrl = {}", shortUrl);
			throw new IllegalArgumentException("短链接中的短链接码不能为空");
		}

		return dwzServerService.getLongUrlByShortUrlCode(shortUrlCode);
	}
}
