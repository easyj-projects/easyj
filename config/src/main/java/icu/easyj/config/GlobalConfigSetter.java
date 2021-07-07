/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.config;

import java.util.Map;

import icu.easyj.env.EnvironmentType;
import icu.easyj.env.RunMode;

/**
 * 全局配置设置器
 *
 * @author wangliang181230
 */
public abstract class GlobalConfigSetter {

	/**
	 * 设置项目名
	 *
	 * @param project 项目名
	 */
	public static void setProject(String project) {
		GlobalConfig.setProject(project);
	}

	/**
	 * 设置应用名
	 *
	 * @param application 应用名
	 */
	public static void setApplication(String application) {
		GlobalConfig.setApplication(application);
	}

	/**
	 * 设置环境名
	 *
	 * @param env 环境名
	 */
	public static void setEnv(String env) {
		GlobalConfig.setEnv(env);
	}

	/**
	 * 设置环境类型
	 *
	 * @param envType 环境类型
	 */
	public static void setEnvType(EnvironmentType envType) {
		GlobalConfig.setEnvType(envType);
	}

	/**
	 * 设置运行模式
	 *
	 * @param runMode 运行模式
	 */
	public static void setRunMode(RunMode runMode) {
		GlobalConfig.setRunMode(runMode);
	}

	/**
	 * 添加配置
	 *
	 * @param key   配置键
	 * @param value 配置值
	 */
	public static synchronized void addConfig(Object key, Object value) {
		GlobalConfig.addConfig(key, value);
	}

	/**
	 * 添加多个配置
	 *
	 * @param configs 配置集合
	 * @param <K>     配置键类型
	 * @param <V>     配置值类型
	 */
	public static synchronized <K extends Object, V extends Object> void addConfigs(Map<K, V> configs) {
		GlobalConfig.addConfigs(configs);
	}
}
