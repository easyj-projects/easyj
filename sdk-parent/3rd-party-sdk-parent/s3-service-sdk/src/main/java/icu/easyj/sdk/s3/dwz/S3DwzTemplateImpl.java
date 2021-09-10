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
import icu.easyj.core.exception.SdkException;
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import icu.easyj.sdk.dwz.DwzSdkException;
import icu.easyj.sdk.dwz.IDwzTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * 短链接服务接口 实现
 *
 * @author wangliang181230
 * @see IDwzTemplate
 * @see S3DwzConfig
 */
public class S3DwzTemplateImpl implements IDwzTemplate {

	/**
	 * 配置信息
	 */
	private final S3DwzConfig config;

	/**
	 * REST请求接口
	 */
	private final RestTemplate restTemplate;


	public S3DwzTemplateImpl(S3DwzConfig config) {
		Assert.notNull(config, "'config' must be not null");
		this.config = config;

		this.restTemplate = new RestTemplate();
	}


	//region Override IDwzTemplate

	@Override
	public DwzResponse createShortUrl(DwzRequest request) throws DwzSdkException {
		Assert.notNull(request, "'request' must be not null");
		Assert.notNull(request.getLongUrl(), "'longUrl' must be not null");

		try {
			// 组装参数
			String url = config.getServiceUrl() + "?client_id=" + config.getClientId() +
					"&client_secret=" + config.getClientSecret() +
					"&url=" + request.getLongUrl();
//			        "&url=" + URLEncoder.encode(request.getLongUrl(), StandardCharsets.UTF_8.name());

			// 发送请求，接收响应
			String respStr = restTemplate.getForObject(url, String.class);
			// 判空
			if (respStr == null) {
				throw new DwzSdkException("请求S-3短链接服务无响应", "NO_RESPONSE");
			}

			// 解析响应JSON
			S3DwzResponse resp = JSONUtil.toBean(respStr, S3DwzResponse.class);
			if (!resp.isSuccess()) {
				S3DwzErrorType errorType = resp.getErrorType();

				String errorMsg = resp.getErrorMessage(errorType);
				String errorCode = errorType != null ? errorType.name() : resp.getCode();

				throw new DwzSdkException("请求S-3短链接服务失败：" + errorMsg, errorCode);
			} else if (resp.getData() == null) {
				throw new DwzSdkException("请求S-3短链接服务的响应中无数据", "NO_DATA");
			}

			// 转换响应类型，并返回
			return this.convert(resp);
		} catch (DwzSdkException e) {
			throw e;
		} catch (RuntimeException e) {
			throw new DwzSdkException("S-3短链接服务未知异常", e);
		}
	}

	//endregion


	private DwzResponse convert(S3DwzResponse resp) {
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
