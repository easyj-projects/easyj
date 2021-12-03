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
package icu.easyj.web.util.httpclient;

import java.util.Map;

import org.springframework.util.MultiValueMap;

/**
 * http客户端服务
 *
 * @author wangliang181230
 */
public interface IHttpClientService {

	//region GET请求

	/**
	 * 发送GET请求
	 *
	 * @param url            服务地址
	 * @param queryStringMap 更多的 QueryString 参数（键和值未转义）
	 * @param headers        附加头信息
	 * @param responseClass  响应类型
	 * @param <T>            响应类
	 * @return 响应
	 */
	<T> T get(String url, Map<String, String> queryStringMap, MultiValueMap<String, String> headers, Class<T> responseClass);

	/**
	 * 发送GET请求（无附加头信息）
	 *
	 * @param url            服务地址
	 * @param queryStringMap 更多的 QueryString 参数（键和值未转义）
	 * @param responseClass  响应类型
	 * @param <T>            响应类
	 * @return 响应
	 */
	default <T> T get(String url, Map<String, String> queryStringMap, Class<T> responseClass) {
		return get(url, queryStringMap, null, responseClass);
	}

	/**
	 * 发送GET请求（无更多QueryString参数）
	 *
	 * @param url           服务地址
	 * @param headers       附加头信息
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	default <T> T get(String url, MultiValueMap<String, String> headers, Class<T> responseClass) {
		return get(url, null, headers, responseClass);
	}

	/**
	 * 发送GET请求（无更多QueryString参数、无附加头信息）
	 *
	 * @param url           服务地址
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	default <T> T get(String url, Class<T> responseClass) {
		return get(url, null, null, responseClass);
	}

	/**
	 * 发送GET请求（无更多QueryString参数、无附加头信息、返回String）
	 *
	 * @param url 服务地址
	 * @return 响应
	 */
	default String get(String url) {
		return get(url, null, null, String.class);
	}

	//endregion


	//region POST请求

	/**
	 * 发送POST请求
	 *
	 * @param url           服务地址
	 * @param requestBody   请求体
	 * @param headers       附加头信息
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	<T> T post(String url, Object requestBody, MultiValueMap<String, String> headers, Class<T> responseClass);

	/**
	 * 发送POST请求（无请求体）
	 *
	 * @param url           服务地址
	 * @param requestBody   请求体
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	default <T> T post(String url, Object requestBody, Class<T> responseClass) {
		return post(url, requestBody, null, responseClass);
	}

	/**
	 * 发送POST请求（无请求体）
	 *
	 * @param url           服务地址
	 * @param headers       附加头信息
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	default <T> T post(String url, MultiValueMap<String, String> headers, Class<T> responseClass) {
		return post(url, null, headers, responseClass);
	}

	/**
	 * 发送POST请求（无请求体、无附加头信息）
	 *
	 * @param url           服务地址
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	default <T> T post(String url, Class<T> responseClass) {
		return post(url, null, null, responseClass);
	}

	/**
	 * 发送POST请求（无请求体、无附加头信息、返回String）
	 *
	 * @param url 服务地址
	 * @return 响应
	 */
	default String post(String url) {
		return post(url, null, null, String.class);
	}

	//endregion
}
