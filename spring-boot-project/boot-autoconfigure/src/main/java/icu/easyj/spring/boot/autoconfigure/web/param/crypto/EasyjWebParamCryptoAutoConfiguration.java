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
package icu.easyj.spring.boot.autoconfigure.web.param.crypto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.serializer.EasyjFastjsonBugfixUtils;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import icu.easyj.config.EnvironmentConfigs;
import icu.easyj.spring.boot.StarterConstants;
import icu.easyj.spring.boot.util.FilterRegistrationUtils;
import icu.easyj.web.constant.FilterOrderConstants;
import icu.easyj.web.param.crypto.FastJsonParamCryptoHttpMessageConverter;
import icu.easyj.web.param.crypto.IParamCryptoFilterProperties;
import icu.easyj.web.param.crypto.IParamCryptoHandler;
import icu.easyj.web.param.crypto.IParamCryptoHandlerProperties;
import icu.easyj.web.param.crypto.ParamCryptoFilter;
import icu.easyj.web.param.crypto.impls.DefaultParamCryptoFilterPropertiesImpl;
import icu.easyj.web.param.crypto.impls.DefaultParamCryptoHandlerImpl;
import icu.easyj.web.param.crypto.impls.DefaultParamCryptoHandlerPropertiesImpl;
import icu.easyj.web.spring.WebMvcConfigurerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static icu.easyj.core.loader.ServiceProviders.FASTJSON;

/**
 * Web参数加密解密自动装配类
 *
 * @author wangliang181230
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ParamCryptoFilter.class, WebMvcConfigurer.class})
@ConditionalOnWebApplication
@ConditionalOnProperty("easyj.web.param-crypto.filter.enabled") // 默认不启用
public class EasyjWebParamCryptoAutoConfiguration {

	static final String PREFERRED_MAPPER_PROPERTY = "spring.mvc.converters.preferred-json-mapper";

	/**
	 * 参数加密解密过滤器配置Bean
	 *
	 * @return 参数加密解密过滤器配置
	 */
	@Bean
	@ConditionalOnMissingBean(IParamCryptoFilterProperties.class)
	@ConfigurationProperties(StarterConstants.WEB_PARAM_CRYPTO_FILTER_PREFIX)
	public DefaultParamCryptoFilterPropertiesImpl defaultParamCryptoFilterProperties() {
		return new DefaultParamCryptoFilterPropertiesImpl();
	}

	/**
	 * 参数加密解密处理器配置 Bean
	 *
	 * @return 参数加密解密处理器配置
	 */
	@Bean
	@ConditionalOnMissingBean(IParamCryptoHandlerProperties.class)
	@ConfigurationProperties(StarterConstants.WEB_PARAM_CRYPTO_HANDLER_PREFIX)
	public DefaultParamCryptoHandlerPropertiesImpl defaultParamCryptoHandlerProperties() {
		return new DefaultParamCryptoHandlerPropertiesImpl();
	}

	/**
	 * 参数加密解密处理器 Bean
	 *
	 * @param properties 参数加密处理器配置
	 * @return 参数加密解密处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public IParamCryptoHandler defaultParamCryptoHandler(IParamCryptoHandlerProperties properties) {
		return new DefaultParamCryptoHandlerImpl(properties);
	}

	/**
	 * 参数加密解密过滤器
	 *
	 * @param filterProperties  过滤器配置
	 * @param handlerProperties 处理器配置
	 * @param handler           处理器
	 * @return paramCryptoFilter 参数加密解密过滤器
	 */
	@Bean
	@ConditionalOnMissingBean
	public ParamCryptoFilter paramEncryptFilter(IParamCryptoFilterProperties filterProperties,
												IParamCryptoHandlerProperties handlerProperties,
												IParamCryptoHandler handler) {
		return new ParamCryptoFilter(filterProperties, handlerProperties, handler);
	}

	/**
	 * 注册参数加密解密过滤器
	 *
	 * @param paramCryptoFilter 参数加密解密过滤器
	 * @return filterRegistrationBean 参数加密解密过滤器注册
	 */
	@Bean
	public FilterRegistrationBean paramEncryptFilterRegistration(ParamCryptoFilter paramCryptoFilter) {
		return FilterRegistrationUtils.register(paramCryptoFilter, paramCryptoFilter.getFilterProperties(), FilterOrderConstants.PARAM_ENCRYPT);
	}


	//region 参数加密解密消息转换器

	/**
	 * 基于Fastjson的参数加密解密消息转换器自动装配类
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({com.alibaba.fastjson.JSON.class})
	@ConditionalOnProperty(value = PREFERRED_MAPPER_PROPERTY, havingValue = FASTJSON) // 该配置在springboot中，默认为jackson，所以不加`matchIfMissing = true`
	static class FastjsonParamCryptoHttpMessageConverterAutoConfiguration implements WebMvcConfigurerAdapter {

		private final ParamCryptoFilter paramCryptoFilter;

		public FastjsonParamCryptoHttpMessageConverterAutoConfiguration(ParamCryptoFilter paramCryptoFilter) {
			this.paramCryptoFilter = paramCryptoFilter;
		}

		@Override
		public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
			// 创建：JSON入参解密/出参加密 消息转换器
			FastJsonParamCryptoHttpMessageConverter httpMessageConverter = new FastJsonParamCryptoHttpMessageConverter(this.paramCryptoFilter);
			httpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);

			//region 创建：fastjson配置
			FastJsonConfig fastJsonConfig = new FastJsonConfig();
			fastJsonConfig.setSerializerFeatures(this.getFeatures());
			fastJsonConfig.setCharset(StandardCharsets.UTF_8);
			// 创建：序列化配置，并设置 Long/long 型数据转String，防止前端JS丢失精度
			SerializeConfig serializeConfig = EasyjFastjsonBugfixUtils.newSerializeConfig(); // 使用BUG修复工具创建
			serializeConfig.put(Long.class, ToStringSerializer.instance);
			fastJsonConfig.setSerializeConfig(serializeConfig);
			//endregion

			// 设置fastjson配置信息到转换器中
			httpMessageConverter.setFastJsonConfig(fastJsonConfig);

			// 创建：需处理的媒体类型列表
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.TEXT_PLAIN);
			mediaTypes.add(MediaType.APPLICATION_JSON);
			mediaTypes.add(MediaType.valueOf("application/*+json"));
			// 添加媒体类型到转换器中
			httpMessageConverter.setSupportedMediaTypes(mediaTypes);

			// 添加到转换器列表中的最前面，优先使用该消息处理器
			converters.add(0, httpMessageConverter);
		}

		private SerializerFeature[] getFeatures() {
			if (EnvironmentConfigs.isDebugMode()) {
				return new SerializerFeature[]{
						SerializerFeature.DisableCircularReferenceDetect, // 不使用$ref
						SerializerFeature.WriteDateUseDateFormat, // 使用时间格式化
						SerializerFeature.UseISO8601DateFormat, // 时间格式化默认采用格式：yyyy-MM-dd HH:mm:ss

						// debug模式下，额外添加以下JSON打包规则
						//SerializerFeature.WriteNullStringAsEmpty, // 为null的对象输出空字符串
						SerializerFeature.SortField, // 根据字段名排序输出
						SerializerFeature.PrettyFormat // 格式化输出
				};
			} else {
				return new SerializerFeature[]{
						SerializerFeature.DisableCircularReferenceDetect, // 不使用$ref
						SerializerFeature.WriteDateUseDateFormat, // 使用时间格式化
						SerializerFeature.UseISO8601DateFormat // 时间格式化默认采用格式：yyyy-MM-dd HH:mm:ss
				};
			}
		}
	}

	//endregion
}
