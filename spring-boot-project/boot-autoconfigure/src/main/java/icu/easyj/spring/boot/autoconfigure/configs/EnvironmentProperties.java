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

import icu.easyj.config.EnvironmentConfigs;
import icu.easyj.core.env.EnvironmentType;
import icu.easyj.core.env.RunMode;
import icu.easyj.core.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * EasyJ定义的环境配置
 *
 * @author wangliang181230
 * @see EnvironmentConfigs
 */
public class EnvironmentProperties implements InitializingBean {

	/**
	 * 环境代码
	 */
	private String env;

	/**
	 * 环境名称
	 */
	private String envName;

	/**
	 * 环境类型
	 * 默认：生产环境
	 */
	private EnvironmentType envType;

	/**
	 * 运行模式
	 * 默认：发行模式
	 * 用途举例：为DEBUG模式时，打印更多日志。比修改日志配置更加高效，运维成本更低。
	 */
	private RunMode runMode;

	/**
	 * 是否单元测试中
	 */
	private Boolean inUnitTest;

	@Override
	public void afterPropertiesSet() {
		if (StringUtils.isNotBlank(env)) {
			EnvironmentConfigs.setEnv(env.trim());
		}
		if (StringUtils.isNotBlank(envName)) {
			EnvironmentConfigs.setEnvName(envName.trim());
		}
		if (envType != null) {
			EnvironmentConfigs.setEnvType(envType);
		} else {
			String env = EnvironmentConfigs.getEnv();
			// 如果环境类型为空，则根据环境名前缀名与类型名是否匹配，来判断环境类型
			if (StringUtils.isNotBlank(env)) {
				// 转换为大写，与枚举一样
				env = env.toUpperCase();
				for (EnvironmentType type : EnvironmentType.values()) {
					if (env.startsWith(type.name())) {
						EnvironmentConfigs.setEnvType(type);
						break;
					}
				}
			}
		}
		if (runMode != null) {
			EnvironmentConfigs.setRunMode(runMode);
		}
		if (inUnitTest != null) {
			EnvironmentConfigs.setInUnitTest(inUnitTest);
		}
	}

	//region Getter、Setter

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public EnvironmentType getEnvType() {
		return envType;
	}

	public void setEnvType(EnvironmentType envType) {
		this.envType = envType;
	}

	public RunMode getRunMode() {
		return runMode;
	}

	public void setRunMode(RunMode runMode) {
		this.runMode = runMode;
	}

	public Boolean getInUnitTest() {
		return inUnitTest;
	}

	public void setInUnitTest(Boolean inUnitTest) {
		this.inUnitTest = inUnitTest;
	}

	//endregion
}
