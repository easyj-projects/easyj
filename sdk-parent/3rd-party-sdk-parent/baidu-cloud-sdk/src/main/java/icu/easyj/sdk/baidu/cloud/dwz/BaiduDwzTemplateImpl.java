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
package icu.easyj.sdk.baidu.cloud.dwz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import icu.easyj.core.constant.DateConstants;
import icu.easyj.core.constant.ErrorCodeConstants;
import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import icu.easyj.sdk.dwz.DwzSdkClientException;
import icu.easyj.sdk.dwz.DwzSdkException;
import icu.easyj.sdk.dwz.DwzSdkServerException;
import icu.easyj.sdk.dwz.IDwzTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * 基于百度云DWZ实现的 {@link IDwzTemplate} 接口
 *
 * @author wangliang181230
 */
public class BaiduDwzTemplateImpl implements IDwzTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaiduDwzTemplateImpl.class);

	/**
	 * DWZ配置信息
	 */
	private final BaiduDwzConfig config;

	/**
	 * REST请求接口
	 */
	private final RestTemplate restTemplate;


	/**
	 * 构造函数
	 *
	 * @param config DWZ配置信息
	 */
	public BaiduDwzTemplateImpl(BaiduDwzConfig config) {
		Assert.notNull(config, "'config' must not be null");
		this.config = config;

		this.restTemplate = new RestTemplate();
		((DefaultUriBuilderFactory)this.restTemplate.getUriTemplateHandler()).setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
	}


	@Override
	public DwzResponse createShortUrl(DwzRequest request) throws DwzSdkException {
		Assert.notNull(request, "'request' must not be null");
		Assert.notNull(request.getLongUrl(), "'longUrl' must not be null");

		// 调用开始时间
		long startTime = System.nanoTime();

		String body = null;
		String respStr = null;
		Throwable t = null;
		try {
			// 准备Body
			List<BaiduDwzRequest> reqList = new ArrayList<>();
			BaiduDwzRequest req1 = this.buildRequest(request.getLongUrl(), (String)request.getConfig("termOfValidity"));
			reqList.add(req1);
			body = JSONUtil.toJsonStr(reqList);
			// 准备Headers
			HttpHeaders headers = new HttpHeaders();
			headers.add("Dwz-Token", config.getToken());
			//headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			headers.add(HttpHeaders.CONTENT_LANGUAGE, config.getResponseLanguage());

			// 创建HTTP请求体
			HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

			// 发送请求，接收响应
			try {
				ResponseEntity<String> respEntity = restTemplate.postForEntity(config.getServiceUrl(), httpEntity, String.class);
				respStr = respEntity.getBody();
			} catch (RestClientResponseException e) {
				respStr = e.getResponseBodyAsString();
				if (!respStr.startsWith("{")) {
					respStr = "[" + e.getRawStatusCode() + "]" + respStr;
					// 不是JSON数据，直接抛出异常
					throw new DwzSdkServerException("请求百度云短链接服务失败：[" + e.getRawStatusCode() + "]" + e.getMessage(), ErrorCodeConstants.SERVER_ERROR, e);
				}
			} catch (RuntimeException e) {
				throw new DwzSdkServerException("请求百度云短链接服务失败", ErrorCodeConstants.SERVER_ERROR, e);
			}

			// 判断：响应内容是否为空
			if (StringUtils.isEmpty(respStr)) {
				throw new DwzSdkServerException("请求百度云短链接服务无响应", "EMPTY_RESPONSE");
			}

			// 解析响应JSON
			BaiduDwzResponse resp = JSONUtil.toBean(respStr, BaiduDwzResponse.class);
			if (!resp.isSuccess()) {
				BaiduDwzErrorType errorType = resp.getErrorType();
				String errorMsg = resp.getErrorMessage();
				String errorCode = errorType != null ? errorType.name() : (resp.getCode() == null ? null : resp.getCode().toString());

				throw new DwzSdkServerException("请求百度云短链接服务失败：[" + resp.getCode() + "]" + errorMsg, errorCode);
			} else if (CollectionUtils.isEmpty(resp.getShortUrls())) {
				throw new DwzSdkServerException("请求百度云短链接服务的响应中无数据", "NO_DATA");
			}

			// 转换响应类型，并返回
			return this.convertToStandard(resp, req1.getTermOfValidity());
		} catch (DwzSdkException e) {
			t = e;
			throw e;
		} catch (RuntimeException e) {
			t = e;
			throw new DwzSdkClientException("百度云短链接服务未知异常", ErrorCodeConstants.UNKNOWN, e);
		} finally {
			if (t == null) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("百度云短链接服务调用成功：\r\n -  url: {}\r\n - body: {}\r\n - resp: {}\r\n - cost: {} ms",
							config.getServiceUrl(), body, respStr, (float)(System.nanoTime() - startTime) / 1000000);
				}
			} else {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("百度云短链接服务调用失败：{}\r\n -  url: {}\r\n - body: {}\r\n - resp: {}\r\n - cost: {} ms",
							t.getMessage(),
							config.getServiceUrl(), body, respStr, (float)(System.nanoTime() - startTime) / 1000000);
				}
			}
		}
	}

	private BaiduDwzRequest buildRequest(String longUrl, String termOfValidity) {
		BaiduDwzRequest req = new BaiduDwzRequest(longUrl, termOfValidity);
		if (StringUtils.isBlank(req.getTermOfValidity())) {
			req.setTermOfValidity(config.getTermOfValidity());
		}
		return req;
	}

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
