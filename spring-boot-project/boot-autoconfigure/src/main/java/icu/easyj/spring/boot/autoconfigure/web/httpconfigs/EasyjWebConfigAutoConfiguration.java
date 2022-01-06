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
package icu.easyj.spring.boot.autoconfigure.web.httpconfigs;

import icu.easyj.web.util.HttpConfigs;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.WebApplicationContext;

/**
 * Web相关配置自动装配类
 *
 * @author wangliang181230
 * @see HttpConfigs // HTTP相关配置持有者
 */
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({WebApplicationContext.class, HttpConfigs.class})
@ConditionalOnWebApplication
public class EasyjWebConfigAutoConfiguration {

	public EasyjWebConfigAutoConfiguration(WebApplicationContext context) {
		HttpConfigs.loadFromWebApplicationContext(context);
	}
}
