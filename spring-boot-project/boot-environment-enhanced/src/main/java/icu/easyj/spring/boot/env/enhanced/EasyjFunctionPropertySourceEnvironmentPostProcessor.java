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
package icu.easyj.spring.boot.env.enhanced;

import org.springframework.boot.SpringApplication;
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
		// 1.1、类是否存在：${easyj.class.isExist('类名1', '类名2', ..., '类名n')}
		// 1.2、获取存在的类：${easyj.class.getExistingOne('类名1', '类名2', ..., '类名n')}

		// 2.1、配置加密：${easyj.crypto.decrypt('xxxxxxxxxxxx')}

		// 3.1、获取本地IP：${easyj.net.getIp()}
		// 3.2、匹配本地IP：${easyj.net.matchIp('10.10.10.*', '192.168.10.*')}

		// 4.1、生成随机内容：${easyj.random.xxx}，支持：端口、数字、UUID

		// 5.1、当前时间：${easyj.time.now('yyyy-MM-dd HH:mm:ss.SSS')}
		propertySources.addLast(new EasyjFunctionPropertySource());
	}

	@Override
	public int getOrder() {
		// 优先级最高，以保证尽快能够使用
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
