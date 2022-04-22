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
package icu.easyj.spring.boot.test;

import icu.easyj.config.EnvironmentConfigs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;

/**
 * 单元测试环境下，添加配置 "easyj.env.in-unit-test=true" ，<br>
 * 同时设置 {@link EnvironmentConfigs#setInUnitTest(boolean)} 为 {@code true}
 *
 * @author wangliang181230
 */
public class EasyjUnitTestEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE; // 优先级最高
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// 设置静态变量
		EnvironmentConfigs.setInUnitTest(true);

		// 添加配置源
		MapPropertySource mapPropertySource = new MapPropertySource("unitTestPropertySource",
				Collections.singletonMap("easyj.env.in-unit-test", true));
		environment.getPropertySources().addFirst(mapPropertySource);
	}
}
