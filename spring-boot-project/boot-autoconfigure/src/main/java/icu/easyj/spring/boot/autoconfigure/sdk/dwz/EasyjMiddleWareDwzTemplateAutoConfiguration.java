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
package icu.easyj.spring.boot.autoconfigure.sdk.dwz;

import icu.easyj.middleware.dwz.template.impls.feign.EasyjDwzRestControllerFeignClient;
import icu.easyj.middleware.dwz.template.impls.feign.SpringCloudFeignEasyjMiddleWareDwzTemplateImpl;
import icu.easyj.middleware.dwz.template.impls.http.HttpEasyjMiddleWareDwzTemplateConfig;
import icu.easyj.middleware.dwz.template.impls.http.HttpEasyjMiddleWareDwzTemplateImpl;
import icu.easyj.sdk.dwz.IDwzTemplate;
import icu.easyj.web.util.httpclient.IHttpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 基于EasyJ自己的DWZ中间件实现的短链接服务
 *
 * @author wangliang181230
 * @since 0.2.1
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "easyj.sdk.dwz.type", havingValue = "easyj-middleware")
public class EasyjMiddleWareDwzTemplateAutoConfiguration {

	@Lazy
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(HttpEasyjMiddleWareDwzTemplateImpl.class)
	@ConditionalOnProperty(value = "easyj.sdk.dwz.easyj-middleware.send-type", havingValue = "http", matchIfMissing = true)
	static class HttpEasyjMiddleWareDwzTemplateConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConfigurationProperties("easyj.sdk.dwz.easyj-middleware")
		public HttpEasyjMiddleWareDwzTemplateConfig httpEasyjMiddleWareDwzTemplateConfig() {
			return new HttpEasyjMiddleWareDwzTemplateConfig();
		}

		@Bean
		@ConditionalOnMissingBean
		public IDwzTemplate httpEasyjMiddleWareDwzTemplate(HttpEasyjMiddleWareDwzTemplateConfig config, @Autowired(required = false) IHttpClientService httpClientService) {
			if (httpClientService != null) {
				return new HttpEasyjMiddleWareDwzTemplateImpl(config, httpClientService);
			} else {
				return new HttpEasyjMiddleWareDwzTemplateImpl(config);
			}
		}
	}


	@Lazy
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(SpringCloudFeignEasyjMiddleWareDwzTemplateImpl.class)
	@EnableFeignClients(clients = EasyjDwzRestControllerFeignClient.class)
	@ConditionalOnProperty(value = "easyj.sdk.dwz.easyj-middleware.send-type", havingValue = "feign")
	static class SpringCloudFeignEasyjMiddleWareDwzTemplateConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public IDwzTemplate springCloudFeignEasyjMiddleWareDwzTemplate(EasyjDwzRestControllerFeignClient feignClient) {
			return new SpringCloudFeignEasyjMiddleWareDwzTemplateImpl(feignClient);
		}
	}
}
