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
package icu.easyj.spring.boot.autoconfigure.web.param.crypto;

import icu.easyj.spring.boot.autoconfigure.util.FilterRegistrationUtils;
import icu.easyj.web.constant.FilterOrderConstants;
import icu.easyj.web.param.crypto.IParamCryptoFilterProperties;
import icu.easyj.web.param.crypto.IParamCryptoHandler;
import icu.easyj.web.param.crypto.IParamCryptoHandlerProperties;
import icu.easyj.web.param.crypto.ParamCryptoFilter;
import icu.easyj.web.param.crypto.impls.DefaultParamCryptoFilterPropertiesImpl;
import icu.easyj.web.param.crypto.impls.DefaultParamCryptoHandlerImpl;
import icu.easyj.web.param.crypto.impls.DefaultParamCryptoHandlerPropertiesImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Web参数加密解密自动装配类
 *
 * @author wangliang181230
 */
@ConditionalOnClass({ParamCryptoFilter.class})
@ConditionalOnWebApplication
@ConditionalOnProperty("easyj.web.param.crypto.filter.enable")
public class EasyjParamCryptoAutoConfiguration {

	/**
	 * @return 参数加密解密过滤器配置
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConfigurationProperties("easyj.web.param.crypto.filter")
	public IParamCryptoFilterProperties defaultParamCryptoFilterProperties() {
		return new DefaultParamCryptoFilterPropertiesImpl();
	}

	/**
	 * @return 参数加密解密处理器配置
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConfigurationProperties("easyj.web.param.crypto.handler")
	public IParamCryptoHandlerProperties defaultParamCryptoHandlerProperties() {
		return new DefaultParamCryptoHandlerPropertiesImpl();
	}

	/**
	 * @param properties 参数加密处理器配置
	 * @return 参数加密解密处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public IParamCryptoHandler defaultParamCryptoHandler(IParamCryptoHandlerProperties properties) {
		return new DefaultParamCryptoHandlerImpl(properties);
	}

	/**
	 * 注册参数加密解密过滤器
	 *
	 * @param filterProperties  过滤器配置
	 * @param handlerProperties 处理器配置
	 * @param handler           处理器
	 * @return filterRegistrationBean 过滤器注册
	 */
	@Bean
	public FilterRegistrationBean paramEncryptFilterRegistration(IParamCryptoFilterProperties filterProperties,
																 IParamCryptoHandlerProperties handlerProperties,
																 IParamCryptoHandler handler) {
		ParamCryptoFilter filter = new ParamCryptoFilter(filterProperties, handlerProperties, handler);
		return FilterRegistrationUtils.register(filter, filterProperties, FilterOrderConstants.PARAM_ENCRYPT);
	}
}
