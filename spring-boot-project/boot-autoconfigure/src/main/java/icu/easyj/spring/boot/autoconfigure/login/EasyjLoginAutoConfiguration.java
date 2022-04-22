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
package icu.easyj.spring.boot.autoconfigure.login;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.jwt.IJwt;
import icu.easyj.login.DefaultLoginValidatorExceptionHandler;
import icu.easyj.login.ILoginProperties;
import icu.easyj.login.ILoginTokenBuilder;
import icu.easyj.login.ILoginValidatorExceptionHandler;
import icu.easyj.login.JwtLoginTokenBuilder;
import icu.easyj.login.JwtLoginTokenBuilderProperties;
import icu.easyj.login.LoginFilter;
import icu.easyj.login.LoginProperties;
import icu.easyj.login.LoginValidatorAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

/**
 * 登录相关自动装配类
 *
 * @author wangliang181230
 */
@ConditionalOnClass(LoginProperties.class)
@ConditionalOnProperty(value = "easyj.login.enabled")
public class EasyjLoginAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(ILoginProperties.class)
	@ConfigurationProperties("easyj.login")
	public LoginProperties loginProperties() {
		return new LoginProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConfigurationProperties("easyj.login.jwt-token-builder")
	public JwtLoginTokenBuilderProperties jwtLoginTokenBuilderProperties() {
		return new JwtLoginTokenBuilderProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public ILoginTokenBuilder loginTokenBuilder(ILoginProperties properties, JwtLoginTokenBuilderProperties loginTokenBuilderProperties) {
		Assert.notNull(loginTokenBuilderProperties.getAlgorithmId(), "Login: 'algorithmId' 配置不能为空");
		Assert.notNull(loginTokenBuilderProperties.getSecretKey(), "Login: 'secretKey' 配置不能为空");
		Assert.notNull(loginTokenBuilderProperties.getSecretKeyAlgorithm(), "Login: 'secretKeyAlgorithm' 配置不能为空");
		Object[] jwtArgs = new Object[]{
				loginTokenBuilderProperties.getAlgorithmId(),
				loginTokenBuilderProperties.getSecretKey(),
				loginTokenBuilderProperties.getSecretKeyAlgorithm(),
		};
		IJwt jwt = EnhancedServiceLoader.load(IJwt.class, loginTokenBuilderProperties.getJwtType(), jwtArgs);
		return new JwtLoginTokenBuilder(jwt, properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public LoginFilter loginFilter(ILoginTokenBuilder loginTokenBuilder, ILoginProperties properties) {
		return new LoginFilter(loginTokenBuilder, properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public ILoginValidatorExceptionHandler defaultLoginValidatorExceptionHandler() {
		return new DefaultLoginValidatorExceptionHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public LoginValidatorAspect loginValidatorAspect(ILoginValidatorExceptionHandler loginValidatorExceptionHandler) {
		return new LoginValidatorAspect(loginValidatorExceptionHandler);
	}
}
