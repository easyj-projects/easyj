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
package icu.easyj.middleware.dwz.template.impls.http;

import java.util.Date;

import icu.easyj.middleware.dwz.domain.EasyjDwzRequest;
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import icu.easyj.sdk.dwz.IDwzTemplate;
import icu.easyj.web.util.HttpClientUtils;
import org.springframework.lang.NonNull;

/**
 * 基于 {@link HttpClientUtils} 实现DWZ服务调用
 *
 * @author wangliang181230
 */
public class HttpEasyjMiddleWareDwzTemplateImpl implements IDwzTemplate {

	private final HttpEasyjMiddleWareDwzTemplateConfig config;


	public HttpEasyjMiddleWareDwzTemplateImpl(HttpEasyjMiddleWareDwzTemplateConfig config) {
		this.config = config;
	}


	@Override
	public DwzResponse createShortUrl(@NonNull DwzRequest request) {
		Date termOfValidity = request.getConfig("term-of-validity");
		EasyjDwzRequest req = new EasyjDwzRequest(request.getLongUrl(), termOfValidity);
		return HttpClientUtils.post(this.config.getServiceUrl(), req, DwzResponse.class);
	}
}
