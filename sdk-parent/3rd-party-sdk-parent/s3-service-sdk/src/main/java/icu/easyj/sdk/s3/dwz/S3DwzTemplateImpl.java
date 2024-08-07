/*
 * Copyright 2021-2024 the original author or authors.
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

import icu.easyj.core.constant.ErrorCodeConstants;
import icu.easyj.core.json.EasyjSupportedJSON;
import icu.easyj.core.json.IJSONService;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.ObjectUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.TimeMeter;
import icu.easyj.core.util.UrlUtils;
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
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientResponseException;

/**
 * 短链接服务接口 实现
 *
 * @author wangliang181230
 * @see IDwzTemplate
 * @see S3DwzConfig
 * @see <a href="http://dwz.doc.s-3.cn">S-3短链接服务API文档</a>
 * @see <a href="https://s-3.cn">S-3短链接服务在线生成</a>
 */
public class S3DwzTemplateImpl implements IDwzTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(S3DwzTemplateImpl.class);

	/**
	 * JSON服务实现
	 */
	private static final IJSONService JSON_SERVICE = EnhancedServiceLoader.loadBySupportNames(IJSONService.class, EasyjSupportedJSON.SUPPORTED);


	/**
	 * 配置信息
	 */
	@NonNull
	private final S3DwzConfig config;

	/**
	 * http客户端服务
	 */
	@NonNull
	private final IHttpClientService httpClientService;


	public S3DwzTemplateImpl(S3DwzConfig config, IHttpClientService httpClientService) {
		Assert.notNull(config, "'config' must not be null");
		Assert.notNull(httpClientService, "'httpClientService' must be not null");
		this.config = config;
		this.httpClientService = httpClientService;
	}

	public S3DwzTemplateImpl(S3DwzConfig config) {
		this(config, HttpClientUtils.getService());
	}


	//region Override IDwzTemplate

	@Override
	public DwzResponse createShortUrl(DwzRequest request) throws DwzSdkException {
		Assert.notNull(request, "'request' must not be null");
		Assert.notNull(request.getLongUrl(), "'longUrl' must not be null");

		// 调用开始时间
		TimeMeter tm = TimeMeter.create();

		// 将入参配置与通用配置合并，生成当前请求所使用的配置
		S3DwzConfig config = ObjectUtils.mergeData(this.config, request.getConfigs());

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
				respStr = httpClientService.get(url);
			} catch (RestClientResponseException e) {
				respStr = "[" + e.getRawStatusCode() + "]" + e.getResponseBodyAsString();
				throw new DwzSdkServerException("请求S-3短链接服务异常：" + respStr, ErrorCodeConstants.SERVER_ERROR, e);
			}

			// 判断：响应内容是否为空
			if (StringUtils.isEmpty(respStr)) {
				throw new DwzSdkServerException("请求S-3短链接服务无响应内容", "EMPTY_RESPONSE");
			}

			// 解析响应JSON
			S3DwzResponse resp = JSON_SERVICE.toBean(respStr, S3DwzResponse.class);
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
					LOGGER.info("S-3短链接服务调用成功：\r\n==>\r\n -  url: {}\r\n - resp: {}\r\n - cost: {} ms\r\n<==\r\n",
							url, respStr, tm.spendMilliSeconds());
				}
			} else {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("S-3短链接服务调用失败：{}\r\n==>\r\n -  url: {}\r\n - resp: {}\r\n - cost: {} ms\r\n<==\r\n",
							t.getMessage(),
							url, respStr, tm.spendMilliSeconds());
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
		response.setExpireIn(data.getExpireIn() != null ? data.getExpireIn() : 0L);

		return response;
	}
}
