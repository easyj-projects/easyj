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
package icu.easyj.sdk.dwz;

import java.util.Map;

/**
 * 短链接服务接口
 *
 * @author wangliang181230
 */
public interface IDwzTemplate {

	/**
	 * 生成短链接
	 *
	 * @param request 请求
	 * @return response 响应
	 * @throws DwzSdkException SDK异常
	 */
	DwzResponse createShortUrl(DwzRequest request) throws DwzSdkException;

	/**
	 * 生成短链接
	 *
	 * @param longUrl 长链接
	 * @return response 响应
	 * @throws DwzSdkException SDK异常
	 */
	default DwzResponse createShortUrl(String longUrl) throws DwzSdkException {
		return createShortUrl(new DwzRequest(longUrl));
	}

	/**
	 * 生成短链接
	 *
	 * @param longUrl 长链接
	 * @param config  可配置参数（主要为了考虑多种实现的不同入参需求）
	 * @return response 响应
	 * @throws DwzSdkException SDK异常
	 */
	default DwzResponse createShortUrl(String longUrl, Map<String, String> config) throws DwzSdkException {
		return createShortUrl(new DwzRequest(longUrl, config));
	}
}
