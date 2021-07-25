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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import icu.easyj.config.GlobalConfigs;
import icu.easyj.spring.boot.autoconfigure.util.FilterRegistrationUtils;
import icu.easyj.web.constant.FilterOrderConstants;
import icu.easyj.web.param.crypto.FastJsonParamCryptoHttpMessageConverter;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static icu.easyj.spring.boot.autoconfigure.StarterConstants.WEB_PARAM_CRYPTO_FILTER_PREFIX;
import static icu.easyj.spring.boot.autoconfigure.StarterConstants.WEB_PARAM_CRYPTO_HANDLER_PREFIX;

/**
 * Web参数加密解密自动装配类
 *
 * @author wangliang181230
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ParamCryptoFilter.class, WebMvcConfigurer.class})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = WEB_PARAM_CRYPTO_FILTER_PREFIX, name = "enabled", matchIfMissing = true)
public class EasyjParamCryptoAutoConfiguration {

	static final String PREFERRED_MAPPER_PROPERTY = "spring.mvc.converters.preferred-json-mapper";

	/**
	 * @return 参数加密解密过滤器配置
	 */
	@Bean
	@ConditionalOnMissingBean(IParamCryptoFilterProperties.class)
	@ConfigurationProperties(WEB_PARAM_CRYPTO_FILTER_PREFIX)
	public DefaultParamCryptoFilterPropertiesImpl defaultParamCryptoFilterProperties() {
		return new DefaultParamCryptoFilterPropertiesImpl();
	}

	/**
	 * @return 参数加密解密处理器配置
	 */
	@Bean
	@ConditionalOnMissingBean(IParamCryptoHandlerProperties.class)
	@ConfigurationProperties(WEB_PARAM_CRYPTO_HANDLER_PREFIX)
	public DefaultParamCryptoHandlerPropertiesImpl defaultParamCryptoHandlerProperties() {
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
	// 该配置在springboot中，默认为jackson，所以不加`matchIfMissing = true`
	@ConditionalOnProperty(value = PREFERRED_MAPPER_PROPERTY, havingValue = "fastjson")
	static class FastjsonParamCryptoHttpMessageConverterAutoConfiguration implements WebMvcConfigurer {

		private final ParamCryptoFilter paramCryptoFilter;

		public FastjsonParamCryptoHttpMessageConverterAutoConfiguration(ParamCryptoFilter paramCryptoFilter) {
			this.paramCryptoFilter = paramCryptoFilter;
		}

		@Override
		public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
			// 创建：JSON入参解密/出参加密 消息转换器
			FastJsonParamCryptoHttpMessageConverter httpMessageConverter =
					new FastJsonParamCryptoHttpMessageConverter(this.paramCryptoFilter);
			httpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);

			// 创建：fastjson配置
			FastJsonConfig fastJsonConfig = new FastJsonConfig();
			fastJsonConfig.setSerializerFeatures(this.getFeatures());
			fastJsonConfig.setCharset(StandardCharsets.UTF_8);
			// Long类型数据转String，防止JS中丢失精度
			SerializeConfig serializeConfig = SerializeConfig.globalInstance;
			serializeConfig.put(Long.class, ToStringSerializer.instance);
			serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
			//fastjson自带的ListSerializer和CollectionCodec没有对Long数据进行转字符串处理
//			serializeConfig.put(ArrayList.class, new FrwListSerializer());
//			serializeConfig.put(HashSet.class, new FrwCollectionCodec());
//			serializeConfig.put(LinkedList.class, new FrwCollectionCodec());
			fastJsonConfig.setSerializeConfig(serializeConfig);
			// 添加fastjson配置信息到转换器中
			httpMessageConverter.setFastJsonConfig(fastJsonConfig);

			// 创建：需处理的媒体类型列表
			List<MediaType> mediaTypes = new ArrayList<>();
			mediaTypes.add(MediaType.TEXT_PLAIN);
			mediaTypes.add(MediaType.APPLICATION_JSON);
			mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
			mediaTypes.add(MediaType.valueOf("application/*+json"));
			// 添加媒体类型到转换器中
			httpMessageConverter.setSupportedMediaTypes(mediaTypes);

			// 添加到转换器列表中
			converters.add(0, httpMessageConverter);
		}

		private SerializerFeature[] getFeatures() {
			if (GlobalConfigs.isDebugMode()) {
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
