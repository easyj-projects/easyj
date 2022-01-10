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
package icu.easyj.spring.boot.autoconfigure.configs;

import cn.hutool.core.lang.Snowflake;
import icu.easyj.config.EnvironmentConfigs;
import icu.easyj.config.ServerConfigs;
import icu.easyj.spring.boot.StarterConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * 全局配置自动装配
 *
 * @author wangliang181230
 * @see EnvironmentConfigs
 */
public class EasyjConfigsAutoConfiguration {

	/**
	 * 创建项目及应用配置bean
	 *
	 * @return 全局配置
	 */
	@Bean
	@Lazy(false)
	@ConfigurationProperties(StarterConstants.APP_PREFIX)
	public AppProperties projectProperties() {
		return new AppProperties();
	}

	/**
	 * 创建环境配置bean
	 *
	 * @param activeProfiles 激活的profiles
	 * @return 全局配置
	 */
	@Bean
	@Lazy(false)
	@ConfigurationProperties(StarterConstants.ENV_PREFIX)
	public EnvironmentProperties environmentProperties(@Value("${spring.profiles.active:}") String[] activeProfiles) {
		EnvironmentProperties properties = new EnvironmentProperties();
		if (activeProfiles != null && activeProfiles.length > 0) {
			properties.setEnv(activeProfiles[0]);
		}
		return properties;
	}

	/**
	 * 创建服务端配置bean
	 *
	 * @return 服务端配置
	 */
	@Bean
	@Lazy(false)
	@ConfigurationProperties(StarterConstants.SERVER_PREFIX)
	public ServerProperties serverProperties() {
		return new ServerProperties();
	}

	/**
	 * 雪花算法Bean
	 *
	 * @param serverProperties 要先初始化好该bean，所以这里空引用一下
	 * @return 雪花算法
	 */
	@Bean
	@Lazy(true)
	@SuppressWarnings("all")
	public Snowflake snowflake(ServerProperties serverProperties) {
		return ServerConfigs.getSnowflake();
	}
}
