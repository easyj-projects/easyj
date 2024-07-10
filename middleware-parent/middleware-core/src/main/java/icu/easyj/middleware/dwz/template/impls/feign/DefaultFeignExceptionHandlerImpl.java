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
package icu.easyj.middleware.dwz.template.impls.feign;

import java.util.Map;

import feign.FeignException;
import icu.easyj.core.json.JSONParseException;
import icu.easyj.core.json.JSONUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.sdk.dwz.DwzSdkException;
import icu.easyj.sdk.dwz.DwzSdkServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的Feign异常处理器
 *
 * @author wangliang181230
 */
public class DefaultFeignExceptionHandlerImpl implements IFeignExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFeignExceptionHandlerImpl.class);


	private static final String[] ERROR_MESSAGE_KEYS = new String[]{"message", "errorMsg", "errorMessage"};


	@Override
	public void handle(RuntimeException e) throws DwzSdkException {
		if (e instanceof FeignException) {
			FeignException fe = (FeignException)e;

			int status = fe.status();
			String content = fe.contentUTF8();

			String errorMsg = null;
			if (StringUtils.isNotEmpty(content)) {
				try {
					Map<String, Object> contentMap = JSONUtils.toMap(content);
					for (String errorMessageKey : ERROR_MESSAGE_KEYS) {
						if (contentMap.containsKey(errorMessageKey)) {
							errorMsg = (String)contentMap.get(errorMessageKey);
							if (errorMsg != null) {
								break;
							}
						}
					}
				} catch (JSONParseException jpe) {
					LOGGER.info("响应内容JSON解析失败：\r\ncontent：{}\r\nexception：{}", content, jpe.getMessage());
				}
				if (errorMsg == null) {
					errorMsg = "未知异常";
				}
			} else {
				errorMsg = "无错误响应信息";
			}

			throw new DwzSdkServerException("调用EasyJ短链接服务失败：[" + status + "]" + errorMsg, e);
		}
	}
}
