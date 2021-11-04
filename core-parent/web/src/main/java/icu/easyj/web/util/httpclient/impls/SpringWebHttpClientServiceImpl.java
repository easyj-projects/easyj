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
package icu.easyj.web.util.httpclient.impls;

import java.util.Map;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.core.util.MapUtils;
import icu.easyj.core.util.UrlUtils;
import icu.easyj.web.util.httpclient.IHttpClientService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * 基于SpringWeb的 {@link org.springframework.web.client.RestTemplate} 实现
 *
 * @author wangliang181230
 */
@LoadLevel(name = "SpringWeb")
@DependsOnClass(RestTemplate.class)
class SpringWebHttpClientServiceImpl implements IHttpClientService {

	private final RestTemplate restTemplate;


	public SpringWebHttpClientServiceImpl() {
		RestTemplate restTemplate = new RestTemplate();
		((DefaultUriBuilderFactory)restTemplate.getUriTemplateHandler()).setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

		this.restTemplate = restTemplate;
	}


	//region GET

	@Override
	public <T> T get(String url, @Nullable Map<String, String> queryStringMap, @Nullable MultiValueMap<String, String> headers, Class<T> responseClass) {
		// 拼接URL参数
		url = UrlUtils.joinQueryString(url, queryStringMap);

		// 发送GET请求
		if (MapUtils.isEmpty(headers)) {
			return restTemplate.getForObject(url, responseClass);
		} else {
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseClass);
			return response.getBody();
		}
	}

	//endregion


	//region POST

	@Override
	public <T> T post(String url, @Nullable Object requestBody, @Nullable MultiValueMap<String, String> headers, Class<T> responseClass) {
		if (MapUtils.isEmpty(headers)) {
			return restTemplate.postForObject(url, requestBody, responseClass);
		} else {
			HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<T> response = restTemplate.postForEntity(url, entity, responseClass);
			return response.getBody();
		}
	}

	//endregion
}
