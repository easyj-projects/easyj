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
package icu.easyj.spring.boot.autoconfigure.global.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static icu.easyj.spring.boot.autoconfigure.StarterConstant.GLOBAL_PREFIX;

/**
 * 全局配置自动装配
 *
 * @author wangliang181230
 */
public class GlobalConfigsAutoConfiguration {

	/**
	 * 创建全局配置bean
	 *
	 * @param activeProfiles 激活的profiles
	 * @return 全局配置
	 */
	@Bean
	@ConfigurationProperties(GLOBAL_PREFIX)
	public GlobalProperties globalProperties(@Value("${spring.profiles.active:}") String[] activeProfiles) {
		GlobalProperties properties = new GlobalProperties();
		if (activeProfiles != null && activeProfiles.length > 0) {
			properties.setEnv(activeProfiles[0]);
		}
		return properties;
	}
}
