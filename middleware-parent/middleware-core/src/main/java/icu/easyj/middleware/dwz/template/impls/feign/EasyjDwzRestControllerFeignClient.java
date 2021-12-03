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
package icu.easyj.middleware.dwz.template.impls.feign;

import icu.easyj.middleware.dwz.domain.EasyjDwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DWZ接口的 FeignClient
 *
 * @author wangliang181230
 */
@FeignClient("${easyj.sdk.dwz.easyj-middleware.server-application-name:easyj-dwz}")
@Qualifier("easyjDwzRestControllerFeignClient")
@RequestMapping("/api/v1")
public interface EasyjDwzRestControllerFeignClient {

	@PostMapping("/create-short-url")
	DwzResponse createShortUrl(@RequestBody EasyjDwzRequest param);
}
