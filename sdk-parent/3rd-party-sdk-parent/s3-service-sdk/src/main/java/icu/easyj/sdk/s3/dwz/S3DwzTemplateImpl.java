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
package icu.easyj.sdk.s3.dwz;

import java.util.Date;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import icu.easyj.core.constant.ErrorCodeConstants;
import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.UrlUtils;
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import icu.easyj.sdk.dwz.DwzSdkClientException;
import icu.easyj.sdk.dwz.DwzSdkException;
import icu.easyj.sdk.dwz.DwzSdkServerException;
import icu.easyj.sdk.dwz.IDwzTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * 短链接服务接口 实现
 *
 * @author wangliang181230
 * @see IDwzTemplate
 * @see S3DwzConfig
 */
public class S3DwzTemplateImpl implements IDwzTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(S3DwzTemplateImpl.class);

	/**
	 * 配置信息
	 */
	private final S3DwzConfig config;

	/**
	 * REST请求接口
	 */
	private final RestTemplate restTemplate;


	public S3DwzTemplateImpl(S3DwzConfig config) {
		Assert.notNull(config, "'config' must not be null");
		this.config = config;

		this.restTemplate = new RestTemplate();
		((DefaultUriBuilderFactory)this.restTemplate.getUriTemplateHandler()).setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
	}


	//region Override IDwzTemplate

	@Override
	public DwzResponse createShortUrl(DwzRequest request) throws DwzSdkException {
		Assert.notNull(request, "'request' must not be null");
		Assert.notNull(request.getLongUrl(), "'longUrl' must not be null");

		// 调用开始时间
		long startTime = System.nanoTime();

		String url = null;
		String respStr = null;
		Throwable t = null;
		try {
			// 组装URL
			url = config.getServiceUrl()
					+ "?client_id=" + config.getClientId()
					+ "&client_secret=" + config.getClientSecret()
					+ "&url=" + UrlUtils.encode(request.getLongUrl());

			// 发送请求，接收响应
			try {
				respStr = restTemplate.getForObject(url, String.class);
			} catch (RestClientResponseException e) {
				respStr = "[" + e.getRawStatusCode() + "]" + e.getResponseBodyAsString();
				throw new DwzSdkServerException("请求S-3短链接服务异常：" + respStr, ErrorCodeConstants.SERVER_ERROR, e);
			} catch (RuntimeException e) {
				throw new DwzSdkServerException("请求S-3短链接服务异常", ErrorCodeConstants.SERVER_ERROR, e);
			}

			// 判断：响应内容是否为空
			if (StringUtils.isEmpty(respStr)) {
				throw new DwzSdkServerException("请求S-3短链接服务无响应内容", "EMPTY_RESPONSE");
			}

			// 解析响应JSON
			S3DwzResponse resp = JSONUtil.toBean(respStr, S3DwzResponse.class);
			if (!resp.isSuccess()) {
				S3DwzErrorType errorType = resp.getErrorType();

				String errorMsg = resp.getErrorMessage(errorType);
				String errorCode = errorType != null ? errorType.name() : resp.getCode();

				throw new DwzSdkServerException("请求S-3短链接服务失败：[" + resp.getCode() + "]" + errorMsg, errorCode);
			} else if (resp.getData() == null) {
				throw new DwzSdkServerException("请求S-3短链接服务的响应中无数据", "NO_DATA");
			}

			// 转换响应类型，并返回
			return this.convertToStandard(resp);
		} catch (DwzSdkException e) {
			t = e;
			throw e;
		} catch (RuntimeException e) {
			t = e;
			throw new DwzSdkClientException("S-3短链接服务未知异常", ErrorCodeConstants.UNKNOWN, e); // 该异常预计为客户端异常
		} finally {
			if (t == null) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("S-3短链接服务调用成功：\r\n -  url: {}\r\n - resp: {}\r\n - cost: {} ms",
							url, respStr, (float)(System.nanoTime() - startTime) / 1000000);
				}
			} else {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("S-3短链接服务调用失败：{}\r\n -  url: {}\r\n - resp: {}\r\n - cost: {} ms",
							t.getMessage(),
							url, respStr, (float)(System.nanoTime() - startTime) / 1000000);
				}
			}
		}
	}

	//endregion

	/**
	 * 转换为 {@link IDwzTemplate} 接口标准响应
	 *
	 * @param resp 第三方SDK响应
	 * @return 标准响应
	 */
	private DwzResponse convertToStandard(S3DwzResponse resp) {
		DwzResponse response = new DwzResponse();

		S3DwzResponseData data = resp.getData();

		// 设置短链接
		response.setShortUrl(data.getUrlShort());
		// 设置短链接创建时间
		if (data.getCreateTime() != null) {
			response.setCreateTime(new Date(data.getCreateTime() * 1000));
		} else {
			response.setCreateTime(new Date());
		}
		// 设置短链接有效时长，单位：毫秒
		response.setExpireIn(data.getExpireIn() != null ? data.getExpireIn() : 0);

		return response;
	}
}
