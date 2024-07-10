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
package icu.easyj.spring.boot.autoconfigure.jar;

import icu.easyj.core.util.jar.JarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;

/**
 * 依赖相关的自动装配类
 *
 * @author wangliang181230
 */
@Lazy(false)
@ConditionalOnProperty("easyj.print-dependencies")
@Configuration(proxyBeanMethods = false)
public class EasyjDependenciesAutoConfiguration implements Ordered {

	public EasyjDependenciesAutoConfiguration() {
		// 打印当前项目所有依赖
		Logger logger = LoggerFactory.getLogger(this.getClass());
		if (logger.isInfoEnabled()) {
			logger.info(JarUtils.convertToDescriptionStr(JarUtils.getJarList(this.getClass().getClassLoader())));
		}
	}


	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
