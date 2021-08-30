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
package icu.easyj.config;

import java.util.Map;

import icu.easyj.core.env.EnvironmentType;
import icu.easyj.core.env.RunMode;

/**
 * 全局配置设置器
 *
 * @author wangliang181230
 */
public abstract class GlobalConfigSetter {

	/**
	 * 设置项目所属区域代码
	 *
	 * @param area 项目所属区域代码
	 */
	public static void setArea(String area) {
		GlobalConfigs.setArea(area);
	}

	/**
	 * 设置项目所属区域名称
	 *
	 * @param areaName 项目所属区域名称
	 */
	public static void setAreaName(String areaName) {
		GlobalConfigs.setAreaName(areaName);
	}

	/**
	 * 设置项目代码
	 *
	 * @param project 项目代码
	 */
	public static void setProject(String project) {
		GlobalConfigs.setProject(project);
	}

	/**
	 * 设置项目名称
	 *
	 * @param projectName 项目名称
	 */
	public static void setProjectName(String projectName) {
		GlobalConfigs.setProjectName(projectName);
	}

	/**
	 * 设置应用代码
	 *
	 * @param application 应用代码
	 */
	public static void setApplication(String application) {
		GlobalConfigs.setApplication(application);
	}

	/**
	 * 设置应用名称
	 *
	 * @param applicationName 应用名称
	 */
	public static void setApplicationName(String applicationName) {
		GlobalConfigs.setApplicationName(applicationName);
	}

	/**
	 * 设置环境代码
	 *
	 * @param env 环境代码
	 */
	public static void setEnv(String env) {
		GlobalConfigs.setEnv(env);
	}

	/**
	 * 设置环境名称
	 *
	 * @param envName 环境名称
	 */
	public static void setEnvName(String envName) {
		GlobalConfigs.setEnvName(envName);
	}

	/**
	 * 设置环境类型
	 *
	 * @param envType 环境类型
	 */
	public static void setEnvType(EnvironmentType envType) {
		GlobalConfigs.setEnvType(envType);
	}

	/**
	 * 设置运行模式
	 *
	 * @param runMode 运行模式
	 */
	public static void setRunMode(RunMode runMode) {
		GlobalConfigs.setRunMode(runMode);
	}

	/**
	 * 设置当前是否单元测试中
	 *
	 * @param inUnitTest 是否单元测试中
	 */
	public static void setInUnitTest(boolean inUnitTest) {
		GlobalConfigs.setInUnitTest(inUnitTest);
	}

	/**
	 * 添加配置
	 *
	 * @param key   配置键
	 * @param value 配置值
	 */
	public static synchronized void addConfig(Object key, Object value) {
		GlobalConfigs.addConfig(key, value);
	}

	/**
	 * 添加多个配置
	 *
	 * @param configs 配置集合
	 * @param <K>     配置键类型
	 * @param <V>     配置值类型
	 */
	public static synchronized <K extends Object, V extends Object> void addConfigs(Map<K, V> configs) {
		GlobalConfigs.addConfigs(configs);
	}
}
