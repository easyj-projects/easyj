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
package icu.easyj.sdk.baidu.cloud.dwz;

import java.util.Date;

import icu.easyj.core.constant.DateConstants;
import icu.easyj.core.constant.ErrorCodeConstants;
import icu.easyj.core.json.EasyjSupportedJSON;
import icu.easyj.core.json.IJSONService;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.ObjectUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.TimeMeter;
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import icu.easyj.sdk.dwz.DwzSdkClientException;
import icu.easyj.sdk.dwz.DwzSdkException;
import icu.easyj.sdk.dwz.DwzSdkServerException;
import icu.easyj.sdk.dwz.IDwzTemplate;
import icu.easyj.web.util.HttpClientUtils;
import icu.easyj.web.util.httpclient.IHttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientResponseException;

/**
 * 基于百度云DWZ实现的 {@link IDwzTemplate} 接口
 *
 * @author wangliang181230
 */
public class BaiduDwzTemplateImpl implements IDwzTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaiduDwzTemplateImpl.class);

	/**
	 * JSON服务实现
	 */
	private static final IJSONService JSON_SERVICE = EnhancedServiceLoader.loadBySupportNames(IJSONService.class, EasyjSupportedJSON.SUPPORTED);


	/**
	 * DWZ配置信息
	 */
	@NonNull
	private final BaiduDwzConfig config;

	/**
	 * http客户端服务
	 */
	@NonNull
	private final IHttpClientService httpClientService;


	public BaiduDwzTemplateImpl(BaiduDwzConfig config, IHttpClientService httpClientService) {
		Assert.notNull(config, "'config' must not be null");
		Assert.notNull(httpClientService, "'httpClientService' must be not null");
		this.config = config;
		this.httpClientService = httpClientService;
	}

	public BaiduDwzTemplateImpl(BaiduDwzConfig config) {
		this(config, HttpClientUtils.getService());
	}


	@Override
	public DwzResponse createShortUrl(DwzRequest request) throws DwzSdkException {
		Assert.notNull(request, "'request' must not be null");
		Assert.notNull(request.getLongUrl(), "'request.longUrl' must not be null");

		// 将入参配置与通用配置合并，生成当前请求所使用的配置
		BaiduDwzConfig config = ObjectUtils.mergeData(this.config, request.getConfigs());

		// 调用开始时间
		TimeMeter tm = TimeMeter.create();

		String body = null;
		String respStr = null;
		RuntimeException ex = null;
		try {
			// Body
			body = String.format("[{\"LongUrl\":\"%s\",\"TermOfValidity\":\"%s\"}]", request.getLongUrl(), config.getTermOfValidity());
			// Headers
			HttpHeaders headers = new HttpHeaders();
			headers.add("Dwz-Token", config.getToken());
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
			headers.add(HttpHeaders.CONTENT_LANGUAGE, config.getResponseLanguage());

			// 发送请求，接收响应
			try {
				respStr = httpClientService.post(config.getServiceUrl(), body, headers, String.class);
			} catch (RestClientResponseException e) {
				respStr = e.getResponseBodyAsString();
				if (!respStr.startsWith("{")) {
					respStr = "[" + e.getRawStatusCode() + "]" + respStr;
					// 不是JSON数据，直接抛出异常
					throw new DwzSdkServerException("请求百度云短链接服务失败：[" + e.getRawStatusCode() + "]" + e.getMessage(), ErrorCodeConstants.SERVER_ERROR, e);
				}
			}

			// 判断：响应内容是否为空
			if (StringUtils.isEmpty(respStr)) {
				throw new DwzSdkServerException("请求百度云短链接服务无响应", "EMPTY_RESPONSE");
			}

			// 解析响应JSON
			BaiduDwzResponse resp = JSON_SERVICE.toBean(respStr, BaiduDwzResponse.class);
			if (!resp.isSuccess()) {
				BaiduDwzErrorType errorType = resp.getErrorType();
				String errorCodeAndMessage = resp.getErrorCodeAndMessage();
				String errorCode = errorType != null ? errorType.name() : (resp.getCode() == null ? null : resp.getCode().toString());

				throw new DwzSdkServerException("请求百度云短链接服务失败：" + errorCodeAndMessage, errorCode);
			} else if (CollectionUtils.isEmpty(resp.getShortUrls())) {
				throw new DwzSdkServerException("请求百度云短链接服务的响应中无数据", "NO_DATA");
			}

			// 转换响应类型，并返回
			return this.convertToStandard(resp, config.getTermOfValidity());
		} catch (DwzSdkException e) {
			ex = e;
			throw e;
		} catch (RuntimeException e) {
			ex = e;
			throw new DwzSdkClientException("请求百度云短链接服务未知异常", ErrorCodeConstants.UNKNOWN, e);
		} finally {
			if (ex == null) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("百度云短链接服务调用成功：\r\n==>\r\n -  url: {}\r\n - body: {}\r\n - resp: {}\r\n - cost: {} ms\r\n==>\r\n",
							config.getServiceUrl(), body, respStr, tm.spendMilliSeconds());
				}
			} else {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("{}{}\r\n==>\r\n -  url: {}\r\n - body: {}\r\n - resp: {}\r\n - cost: {} ms\r\n<==\r\n",
							ex instanceof DwzSdkException ? "" : "百度云短链接服务调用失败：", ex,
							config.getServiceUrl(), body, respStr, tm.spendMilliSeconds());
				}
			}
		}
	}

	/**
	 * 转换为标准响应
	 *
	 * @param resp           Baidu接口响应
	 * @param termOfValidity 长链接有效时间：1-year、long-term
	 * @return 标准响应
	 */
	private DwzResponse convertToStandard(BaiduDwzResponse resp, String termOfValidity) {
		DwzResponse response = new DwzResponse();

		BaiduDwzResponseData data = resp.getShortUrls().get(0);

		// 设置短链接
		response.setShortUrl(data.getShortUrl());
		// 设置短链接创建时间
		response.setCreateTime(new Date());
		// 设置短链接有效时长，单位：毫秒
		if (termOfValidity != null) {
			switch (termOfValidity) {
				case "1-year":
					response.setExpireIn(365 * DateConstants.ONE_DAY_MILL); // 一年有效（固定365天，不考虑闰年）
					break;
				case "long-term":
				default:
					response.setExpireIn(0L);
					break;
			}
		} else {
			response.setExpireIn(0L);
		}

		return response;
	}
}
