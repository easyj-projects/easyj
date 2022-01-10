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
package icu.easyj.web.spring;

import java.util.List;

import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 为了兼容低版本的spring-webmvc，添加该适配器类
 *
 * @author wangliang181230
 */
public interface WebMvcConfigurerAdapter extends WebMvcConfigurer {

	@Override
	default void configurePathMatch(PathMatchConfigurer configurer) {
	}

	@Override
	default void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	}

	@Override
	default void configureAsyncSupport(AsyncSupportConfigurer configurer) {
	}

	@Override
	default void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	}

	@Override
	default void addFormatters(FormatterRegistry registry) {
	}

	@Override
	default void addInterceptors(InterceptorRegistry registry) {
	}

	@Override
	default void addResourceHandlers(ResourceHandlerRegistry registry) {
	}

	@Override
	default void addCorsMappings(CorsRegistry registry) {
	}

	@Override
	default void addViewControllers(ViewControllerRegistry registry) {
	}

	@Override
	default void configureViewResolvers(ViewResolverRegistry registry) {
	}

	@Override
	default void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	}

	@Override
	default void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
	}

	@Override
	default void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	}

	@Override
	default void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
	}

	@Override
	default void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
	}

	@Override
	default void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
	}

	@Override
	default Validator getValidator() {
		return null;
	}

	@Override
	default MessageCodesResolver getMessageCodesResolver() {
		return null;
	}
}
