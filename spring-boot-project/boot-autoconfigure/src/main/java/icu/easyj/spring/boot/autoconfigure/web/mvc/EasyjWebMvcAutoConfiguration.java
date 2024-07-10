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
package icu.easyj.spring.boot.autoconfigure.web.mvc;

import icu.easyj.core.convert.converter.CharSequenceToDateConverter;
import icu.easyj.web.spring.WebMvcConfigurerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMVC相关的自动装配类
 *
 * @author wangliang181230
 */
@ConditionalOnClass({WebMvcConfigurer.class, CharSequenceToDateConverter.class})
@ConditionalOnWebApplication
public class EasyjWebMvcAutoConfiguration implements WebMvcConfigurerAdapter {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		// 添加 '非JSON传参时的时间字符串转Date' 的转换器
		registry.addConverter(new CharSequenceToDateConverter());
	}
}
