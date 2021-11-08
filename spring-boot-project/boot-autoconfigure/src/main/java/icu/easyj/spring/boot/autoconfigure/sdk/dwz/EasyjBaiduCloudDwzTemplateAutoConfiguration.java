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

import icu.easyj.sdk.baidu.cloud.dwz.BaiduDwzConfig;
import icu.easyj.sdk.baidu.cloud.dwz.BaiduDwzTemplateImpl;
import icu.easyj.sdk.dwz.IDwzTemplate;
import icu.easyj.web.util.httpclient.IHttpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 基于百度云DWZ实现的短链接服务
 *
 * @author wangliang181230
 * @see BaiduDwzConfig
 * @see BaiduDwzTemplateImpl
 * @see <a href="https://console.bce.baidu.com/dwz">百度云短链接服务控制台</a>
 * @see <a href="https://dwz.cn/console/apidoc/v3">百度云短链接服务API文档</a>
 */
@ConditionalOnClass({IDwzTemplate.class, BaiduDwzTemplateImpl.class})
@ConditionalOnProperty(value = "easyj.sdk.dwz.type", havingValue = "baidu")
public class EasyjBaiduCloudDwzTemplateAutoConfiguration {

	/**
	 * 百度云短链接服务配置 Bean
	 *
	 * @return 百度云短链接服务配置
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConfigurationProperties("easyj.sdk.dwz.baidu")
	public BaiduDwzConfig baiduCloudDwzConfig() {
		return new BaiduDwzConfig();
	}

	/**
	 * 百度云短链接服务服务 Bean
	 *
	 * @param config            百度云短链接服务配置
	 * @param httpClientService http客户端服务，用于发起http请求
	 * @return 百度云短链接服务服务
	 */
	@Bean
	@ConditionalOnMissingBean
	public IDwzTemplate baiduCloudDwzTemplate(BaiduDwzConfig config, @Autowired(required = false) IHttpClientService httpClientService) {
		if (httpClientService != null) {
			return new BaiduDwzTemplateImpl(config, httpClientService);
		} else {
			return new BaiduDwzTemplateImpl(config);
		}
	}
}
