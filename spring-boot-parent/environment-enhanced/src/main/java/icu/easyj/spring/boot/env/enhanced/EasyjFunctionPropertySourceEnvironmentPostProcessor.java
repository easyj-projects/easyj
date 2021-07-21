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
package icu.easyj.spring.boot.env.enhanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * 加载EasyJ的函数式配置源
 *
 * @author wangliang181230
 */
public class EasyjFunctionPropertySourceEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	/**
	 * 添加函数式配置源
	 *
	 * @param environment 环境
	 * @param application 应用
	 */
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		MutablePropertySources propertySources = environment.getPropertySources();

		// 添加函数式配置源：${easyj.xxx}
		// 目前包含：
		// 1、配置加密：${easyj.crypto.decrypt('xxxxxxxxxxxx')}
		// 2、获取本地IP：${easyj.localIp.xxx}
		// 3、生成随机内容：${easyj.random.xxx}，支持：端口、数字、UUID
		propertySources.addLast(new EasyjFunctionPropertySource());
	}

	@Override
	public int getOrder() {
		// 比SpringBoot的配置文件加载器早
		return ConfigDataEnvironmentPostProcessor.ORDER - 1;
	}
}
