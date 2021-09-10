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
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import icu.easyj.sdk.dwz.DwzSdkException;
import icu.easyj.sdk.dwz.IDwzTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * 基于百度云DWZ实现的 {@link IDwzTemplate} 接口
 *
 * @author wangliang181230
 */
public class BaiduDwzTemplateImpl implements IDwzTemplate {

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
		Assert.notNull(config, "'config' must be not null");
		this.config = config;

		this.restTemplate = new RestTemplate();
	}


	@Override
	public DwzResponse createShortUrl(DwzRequest request) throws DwzSdkException {
		Assert.notNull(request, "'request' must be not null");
		Assert.notNull(request.getLongUrl(), "'longUrl' must be not null");

		try {
			// 准备Body
			List<BaiduDwzRequest> reqList = new ArrayList<>();
			BaiduDwzRequest req1 = this.buildRequest(request.getLongUrl(), request.getConfig("termOfValidity"));
			reqList.add(req1);
			String body = JSONUtil.toJsonStr(reqList);
			// 准备Header
			HttpHeaders headers = new HttpHeaders();
			headers.add("Dwz-Token", config.getToken());
			//headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			headers.add(HttpHeaders.CONTENT_LANGUAGE, config.getResponseLanguage());
			// 创建参数
			HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

			// 发送请求，接收响应
			String respStr;
			try {
				ResponseEntity<String> respEntity = restTemplate.postForEntity(config.getServiceUrl(), httpEntity, String.class);
				respStr = respEntity.getBody();
			} catch (HttpClientErrorException.Forbidden e) {
				respStr = e.getResponseBodyAsString();
				if (!respStr.startsWith("{")) {
					// 不是JSON数据，直接抛出异常
					throw e;
				}
			}
			// 判空
			if (respStr == null) {
				throw new DwzSdkException("请求S-3短链接服务无响应", "NO_RESPONSE");
			}

			// 解析响应JSON
			BaiduDwzResponse resp = JSONUtil.toBean(respStr, BaiduDwzResponse.class);
			if (!resp.isSuccess()) {
				BaiduDwzErrorType errorType = resp.getErrorType();
				String errorMsg = resp.getErrorMessage();
				String errorCode = errorType != null ? errorType.name() : resp.getCode().toString();

				throw new DwzSdkException("请求百度云短链接服务失败：" + errorMsg, errorCode);
			} else if (CollectionUtils.isEmpty(resp.getShortUrls())) {
				throw new DwzSdkException("请求百度云短链接服务的响应中无数据", "NO_DATA");
			}

			// 转换响应类型，并返回
			return this.convert(resp, req1.getTermOfValidity());
		} catch (DwzSdkException e) {
			throw e;
		} catch (RuntimeException e) {
			throw new DwzSdkException("百度云短链接服务未知异常", e);
		}
	}

	private BaiduDwzRequest buildRequest(String longUrl, String termOfValidity) {
		BaiduDwzRequest req = new BaiduDwzRequest(longUrl, termOfValidity);
		if (!StringUtils.hasText(req.getTermOfValidity())) {
			req.setTermOfValidity(config.getDefaultTermOfValidity());
		}
		return req;
	}

	private DwzResponse convert(BaiduDwzResponse resp, String termOfValidity) {
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
					response.setExpireIn(365 * 24 * 60 * 60 * 1000L); // 一年有效
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
