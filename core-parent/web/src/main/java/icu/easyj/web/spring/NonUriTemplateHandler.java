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
package icu.easyj.web.spring;

import java.net.URI;
import java.util.Map;

import org.springframework.web.util.UriTemplateHandler;

/**
 * 空的URI模板处理器
 *
 * @author wangliang181230
 */
public class NonUriTemplateHandler implements UriTemplateHandler {

	/**
	 * 单例
	 */
	private static final UriTemplateHandler INSTANCE = new NonUriTemplateHandler();


	private NonUriTemplateHandler() {
	}


	@Override
	public URI expand(String uriTemplate, Map<String, ?> uriVariables) {
		return URI.create(uriTemplate);
	}

	@Override
	public URI expand(String uriTemplate, Object... uriVariables) {
		return URI.create(uriTemplate);
	}

	/**
	 * 获取实例
	 *
	 * @return 空的URI模板处理器
	 */
	public static UriTemplateHandler getInstance() {
		return INSTANCE;
	}
}
