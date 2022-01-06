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
package icu.easyj.web.util;

import java.util.Map;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.web.util.httpclient.IHttpClientService;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

/**
 * http(s)客户端工具类
 * <p>
 * 提供请求http(s)服务。
 *
 * @author wangliang181230
 */
public abstract class HttpClientUtils {

	/**
	 * {@link IHttpClientService} 默认的实现
	 */
	private static final IHttpClientService HTTP_CLIENT_SERVICE = EnhancedServiceLoader.load(IHttpClientService.class);

	/**
	 * 获取 {@link IHttpClientService} 默认的实现
	 *
	 * @return 默认的实现
	 */
	public static IHttpClientService getService() {
		return HTTP_CLIENT_SERVICE;
	}


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
	public <T> T get(String url, @Nullable Map<String, String> queryStringMap, @Nullable MultiValueMap<String, String> headers, Class<T> responseClass) {
		return HTTP_CLIENT_SERVICE.get(url, queryStringMap, headers, responseClass);
	}

	/**
	 * 发送GET请求（无附加头信息）
	 *
	 * @param url            服务地址
	 * @param queryStringMap 更多的 QueryString 参数（键和值未转义）
	 * @param responseClass  响应类型
	 * @param <T>            响应类
	 * @return 响应
	 */
	public static <T> T get(String url, @Nullable Map<String, String> queryStringMap, Class<T> responseClass) {
		return HTTP_CLIENT_SERVICE.get(url, queryStringMap, null, responseClass);
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
	public static <T> T get(String url, @Nullable MultiValueMap<String, String> headers, Class<T> responseClass) {
		return HTTP_CLIENT_SERVICE.get(url, null, headers, responseClass);
	}

	/**
	 * 发送GET请求（无更多QueryString参数、无附加头信息）
	 *
	 * @param url           服务地址
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	public static <T> T get(String url, Class<T> responseClass) {
		return HTTP_CLIENT_SERVICE.get(url, null, null, responseClass);
	}

	/**
	 * 发送GET请求（无更多QueryString参数、无附加头信息、返回String）
	 *
	 * @param url 服务地址
	 * @return 响应
	 */
	public static String get(String url) {
		return HTTP_CLIENT_SERVICE.get(url, null, null, String.class);
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
	public static <T> T post(String url, @Nullable Object requestBody, @Nullable MultiValueMap<String, String> headers, Class<T> responseClass) {
		return HTTP_CLIENT_SERVICE.post(url, requestBody, headers, responseClass);
	}

	/**
	 * 发送POST请求（无请求体）
	 *
	 * @param url           服务地址
	 * @param requestBody   请求体
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	public static <T> T post(String url, @Nullable Object requestBody, Class<T> responseClass) {
		return HTTP_CLIENT_SERVICE.post(url, requestBody, null, responseClass);
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
	public static <T> T post(String url, @Nullable MultiValueMap<String, String> headers, Class<T> responseClass) {
		return HTTP_CLIENT_SERVICE.post(url, null, headers, responseClass);
	}

	/**
	 * 发送POST请求（无请求体、无附加头信息）
	 *
	 * @param url           服务地址
	 * @param responseClass 响应类型
	 * @param <T>           响应类
	 * @return 响应
	 */
	public static <T> T post(String url, Class<T> responseClass) {
		return HTTP_CLIENT_SERVICE.post(url, null, null, responseClass);
	}

	/**
	 * 发送POST请求（无请求体、无附加头信息、返回String）
	 *
	 * @param url 服务地址
	 * @return 响应
	 */
	public static String post(String url) {
		return HTTP_CLIENT_SERVICE.post(url, null, null, String.class);
	}

	//endregion
}
