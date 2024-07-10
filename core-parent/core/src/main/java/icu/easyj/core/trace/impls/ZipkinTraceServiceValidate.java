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
package icu.easyj.core.trace.impls;

import brave.Tracer;
import cn.hutool.extra.spring.SpringUtil;
import icu.easyj.core.loader.IServiceLoaderValidator;
import icu.easyj.core.loader.InvalidServiceException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * Tracer的Bean是否存在的校验器
 *
 * @author wangliang181230
 */
public class ZipkinTraceServiceValidate implements IServiceLoaderValidator {

	@Override
	public void validate(Class<?> serviceClass, ClassLoader classLoader) throws InvalidServiceException {
		try {
			SpringUtil.getBean(Tracer.class);
		} catch (NoSuchBeanDefinitionException e) {
			throw new InvalidServiceException("'" + Tracer.class.getSimpleName() + "'的Bean不存在，无法使用 'ZipkinTraceServiceImpl'", e);
		}
	}
}
