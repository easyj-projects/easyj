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
package icu.easyj.spring.boot.autoconfigure.sdk.dwz;

import icu.easyj.sdk.dwz.IDwzTemplate;
import icu.easyj.sdk.s3.dwz.S3DwzConfig;
import icu.easyj.sdk.s3.dwz.S3DwzTemplateImpl;
import icu.easyj.web.util.httpclient.IHttpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 基于S-3实现的短链接服务（免费的）
 *
 * @author wangliang181230
 * @see S3DwzConfig
 * @see S3DwzTemplateImpl
 * @see <a href="http://dwz.doc.s-3.cn">S-3短链接服务API文档</a>
 * @see <a href="https://s-3.cn">S-3短链接服务在线生成</a>
 */
@ConditionalOnClass({IDwzTemplate.class, S3DwzTemplateImpl.class})
@ConditionalOnProperty(value = "easyj.sdk.dwz.type", havingValue = "s3", matchIfMissing = true)
public class EasyjS3DwzTemplateAutoConfiguration {

	/**
	 * S-3短链接服务配置 Bean
	 *
	 * @return S-3短链接服务配置
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConfigurationProperties("easyj.sdk.dwz.s3")
	public S3DwzConfig s3DwzConfig() {
		return new S3DwzConfig();
	}

	/**
	 * S-3短链接服务 Bean
	 *
	 * @param config            S-3短链接服务配置
	 * @param httpClientService http客户端服务，用于发起http请求
	 * @return S-3短链接服务
	 */
	@Bean
	@ConditionalOnMissingBean
	public IDwzTemplate s3DwzTemplate(S3DwzConfig config, @Autowired(required = false) IHttpClientService httpClientService) {
		if (httpClientService != null) {
			return new S3DwzTemplateImpl(config, httpClientService);
		} else {
			return new S3DwzTemplateImpl(config);
		}
	}
}
