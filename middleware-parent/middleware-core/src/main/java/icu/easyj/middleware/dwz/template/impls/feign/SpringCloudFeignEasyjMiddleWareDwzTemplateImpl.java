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
package icu.easyj.middleware.dwz.template.impls.feign;

import java.util.Date;

import icu.easyj.middleware.dwz.domain.EasyjDwzRequest;
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import icu.easyj.sdk.dwz.DwzSdkException;
import icu.easyj.sdk.dwz.IDwzTemplate;

/**
 * 基于 SpringCloudFeign 实现DWZ服务调用
 *
 * @author wangliang181230
 */
public class SpringCloudFeignEasyjMiddleWareDwzTemplateImpl implements IDwzTemplate {

	private final EasyjDwzRestControllerFeignClient feignClient;

	private final IFeignExceptionHandler exceptionHandler;


	public SpringCloudFeignEasyjMiddleWareDwzTemplateImpl(EasyjDwzRestControllerFeignClient feignClient,
														  IFeignExceptionHandler exceptionHandler) {
		this.feignClient = feignClient;
		this.exceptionHandler = exceptionHandler;
	}

	public SpringCloudFeignEasyjMiddleWareDwzTemplateImpl(EasyjDwzRestControllerFeignClient feignClient) {
		this(feignClient, null);
	}


	@Override
	public DwzResponse createShortUrl(DwzRequest request) {
		try {
			Date termOfValidity = request.getConfig("term-of-validity");
			EasyjDwzRequest req = new EasyjDwzRequest(request.getLongUrl(), termOfValidity);
			return feignClient.createShortUrl(req);
		} catch (RuntimeException ex) {
			try {
				if (this.exceptionHandler != null) {
					this.exceptionHandler.handle(ex);
				}
			} catch (DwzSdkException e) {
				throw e;
			} catch (Exception e) {
				throw new DwzSdkException("调用EasyJ短链接服务失败", e);
			}

			throw new DwzSdkException("调用EasyJ短链接服务失败", ex);
		}
	}
}
