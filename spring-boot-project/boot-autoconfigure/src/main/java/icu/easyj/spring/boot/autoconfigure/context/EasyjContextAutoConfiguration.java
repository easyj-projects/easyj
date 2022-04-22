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
package icu.easyj.spring.boot.autoconfigure.context;

import icu.easyj.config.EnvironmentConfigs;
import icu.easyj.web.context.ContextCleanerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * 上下文相关自动装配
 *
 * @author wangliang181230
 * @see EnvironmentConfigs
 */
public class EasyjContextAutoConfiguration {

	/**
	 * 创建内容清理者过滤器bean
	 *
	 * @return 内容清理者过滤器bean
	 */
	@Bean
	@Lazy(false)
	public ContextCleanerFilter contextCleanerFilter() {
		return new ContextCleanerFilter();
	}
}
