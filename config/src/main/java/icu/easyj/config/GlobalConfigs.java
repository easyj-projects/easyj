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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import icu.easyj.env.EnvironmentType;
import icu.easyj.env.RunMode;

/**
 * 全局配置
 *
 * @author wangliang181230
 */
public abstract class GlobalConfigs {

	/**
	 * 项目代码
	 */
	private static String project;

	/**
	 * 项目名称
	 */
	private static String projectName;

	/**
	 * 应用代码
	 */
	private static String application;

	/**
	 * 应用名称
	 */
	private static String applicationName;

	/**
	 * 环境代码
	 */
	private static String env;

	/**
	 * 环境名称
	 */
	private static String envName;

	/**
	 * 环境类型
	 * 默认：生产环境
	 */
	private static EnvironmentType envType = EnvironmentType.PROD;

	/**
	 * 运行模式
	 * 默认：发行模式
	 * 用途举例：为DEBUG模式时，打印更多日志。比修改日志配置更加高效，运维成本更低。
	 */
	private static RunMode runMode = RunMode.RELEASE;

	/**
	 * 其他全局配置Map
	 */
	private static final Map<Object, Object> CONFIGS = new HashMap<>(4);


	//region Getter start

	/**
	 * @return 项目代码
	 */
	public static String getProject() {
		return project;
	}

	/**
	 * @return 项目名称
	 */
	public static String getProjectName() {
		return projectName;
	}

	/**
	 * @return 应用代码
	 */
	public static String getApplication() {
		return application;
	}

	/**
	 * @return 应用名称
	 */
	public static String getApplicationName() {
		return applicationName;
	}


	//region 环境相关 start

	/**
	 * @return 环境代码
	 */
	public static String getEnv() {
		return env;
	}

	/**
	 * @return 环境名称
	 */
	public static String getEnvName() {
		return envName;
	}

	/**
	 * @return 环境类型枚举
	 */
	public static EnvironmentType getEnvType() {
		return envType;
	}

	/**
	 * @return 是否为生产环境
	 */
	public static boolean isProdEnv() {
		return EnvironmentType.PROD == envType;
	}

	/**
	 * @return 是否为沙箱环境
	 */
	public static boolean isSandboxEnv() {
		return EnvironmentType.SANDBOX == envType;
	}

	/**
	 * @return 是否为测试环境
	 */
	public static boolean isTestEnv() {
		return EnvironmentType.TEST == envType;
	}

	/**
	 * @return 是否为开发环境
	 */
	public static boolean isDevEnv() {
		return EnvironmentType.DEV == envType;
	}

	//endregion 环境相关 end


	//region 运行模式 start

	/**
	 * @return 运行模式
	 */
	public static RunMode getRunMode() {
		return runMode;
	}

	/**
	 * @return 是否为发行模式
	 */
	public static boolean isReleaseMode() {
		return RunMode.RELEASE == runMode;
	}

	/**
	 * @return 是否为调试模式
	 */
	public static boolean isDebugMode() {
		return RunMode.DEBUG == runMode;
	}

	//endregion 运行模式 end


	//region 其他全局配置 start

	/**
	 * @return 全局配置Map
	 */
	public static Map<Object, Object> getConfigs() {
		return Collections.unmodifiableMap(CONFIGS);
	}

	/**
	 * 获取配置值
	 *
	 * @param key 配置键
	 * @param <T> 配置值类型
	 * @return 配置值
	 */
	public static <T> T getConfig(Object key) {
		return (T)CONFIGS.get(key);
	}

	//endregion 其他全局配置 end

	//endregion Getter end


	//region Setter start

	/**
	 * 设置项目代码
	 *
	 * @param project 项目代码
	 */
	static void setProject(String project) {
		GlobalConfigs.project = project;
	}

	/**
	 * 设置项目名称
	 *
	 * @param projectName 项目名称
	 */
	static void setProjectName(String projectName) {
		GlobalConfigs.projectName = projectName;
	}

	/**
	 * 设置应用代码
	 *
	 * @param application 应用代码
	 */
	static void setApplication(String application) {
		GlobalConfigs.application = application;
	}

	/**
	 * 设置应用名称
	 *
	 * @param applicationName 应用名称
	 */
	static void setApplicationName(String applicationName) {
		GlobalConfigs.applicationName = applicationName;
	}

	/**
	 * 设置环境代码
	 *
	 * @param env 环境代码
	 */
	static void setEnv(String env) {
		GlobalConfigs.env = env;
	}

	/**
	 * 设置环境名称
	 *
	 * @param envName 环境名称
	 */
	static void setEnvName(String envName) {
		GlobalConfigs.envName = envName;
	}

	/**
	 * 设置环境类型
	 *
	 * @param envType 环境类型
	 */
	static void setEnvType(EnvironmentType envType) {
		GlobalConfigs.envType = envType;
	}

	/**
	 * 设置运行模式
	 *
	 * @param runMode 运行模式
	 */
	static void setRunMode(RunMode runMode) {
		GlobalConfigs.runMode = runMode;
	}

	/**
	 * 添加配置
	 *
	 * @param key   配置键
	 * @param value 配置值
	 */
	static synchronized void addConfig(Object key, Object value) {
		CONFIGS.put(key, value);
	}

	/**
	 * 添加多个配置
	 *
	 * @param configs 配置集合
	 * @param <K>     配置键类型
	 * @param <V>     配置值类型
	 */
	static synchronized <K extends Object, V extends Object> void addConfigs(Map<K, V> configs) {
		GlobalConfigs.CONFIGS.putAll(configs);
	}

	//endregion Setter end
}