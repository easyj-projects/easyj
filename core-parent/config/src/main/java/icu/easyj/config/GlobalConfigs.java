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

import cn.hutool.core.util.ObjectUtil;
import icu.easyj.core.env.EnvironmentType;
import icu.easyj.core.env.RunMode;

/**
 * 全局配置
 *
 * @author wangliang181230
 * @see GlobalConfigSetter
 */
public class GlobalConfigs {

	//region Fields

	/**
	 * 项目所属区域代码
	 */
	private String area;

	/**
	 * 项目所属区域名称，如：浙江省宁波市鄞州区
	 */
	private String areaName;

	/**
	 * 项目代码
	 */
	private String project;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 应用代码
	 */
	private String application;

	/**
	 * 应用名称
	 */
	private String applicationName;

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
	private EnvironmentType envType = EnvironmentType.PROD;

	/**
	 * 运行模式
	 * 默认：发行模式
	 * 用途举例：为DEBUG模式时，打印更多日志。比修改日志配置更加高效，运维成本更低。
	 */
	private RunMode runMode = RunMode.RELEASE;

	/**
	 * 是否单元测试中
	 */
	private boolean inUnitTest = false;

	/**
	 * 其他全局配置Map
	 */
	private final Map<Object, Object> configs = new HashMap<>(4);

	//endregion


	//region 全局配置持有者（设计模式-创建型模式-单例模式-枚举实现单例）

	private GlobalConfigs() {
	}

	private enum SingletonHolder {
		// 单例
		INSTANCE;

		private final GlobalConfigs globalConfigs = new GlobalConfigs();

		public GlobalConfigs getGlobalConfigs() {
			return globalConfigs;
		}
	}

	private static GlobalConfigs getInstance() {
		return SingletonHolder.INSTANCE.getGlobalConfigs();
	}

	//endregion


	//region Getter start

	/**
	 * 获取项目所属区域代码
	 *
	 * @return 项目所属区域代码
	 */
	public static String getArea() {
		return getInstance().area;
	}

	/**
	 * 获取项目所属区域名称
	 *
	 * @return 项目所属区域名称
	 */
	public static String getAreaName() {
		return getInstance().areaName;
	}

	/**
	 * 获取项目偌
	 *
	 * @return 项目代码
	 */
	public static String getProject() {
		return getInstance().project;
	}

	/**
	 * 获取项目名称
	 *
	 * @return 项目名称
	 */
	public static String getProjectName() {
		return getInstance().projectName;
	}

	/**
	 * 获取应用代码
	 *
	 * @return 应用代码
	 */
	public static String getApplication() {
		return getInstance().application;
	}

	/**
	 * 获取应用名称
	 *
	 * @return 应用名称
	 */
	public static String getApplicationName() {
		return getInstance().applicationName;
	}


	//region 环境相关 start

	/**
	 * 获取环境代码
	 *
	 * @return 环境代码
	 */
	public static String getEnv() {
		return getInstance().env;
	}

	/**
	 * 获取环境名称
	 *
	 * @return 环境名称
	 */
	public static String getEnvName() {
		return getInstance().envName;
	}

	/**
	 * 获取环境类型枚举
	 *
	 * @return 环境类型枚举
	 */
	public static EnvironmentType getEnvType() {
		return getInstance().envType;
	}

	/**
	 * 判断是否为生产环境
	 *
	 * @return 是否为生产环境
	 */
	public static boolean isProdEnv() {
		return EnvironmentType.PROD == getEnvType();
	}

	/**
	 * 判断是否不为生产环境
	 *
	 * @return 是否不为生产环境
	 */
	public static boolean isNotProdEnv() {
		return !isProdEnv();
	}

	/**
	 * 判断是否为沙箱环境
	 *
	 * @return 是否为沙箱环境
	 */
	public static boolean isSandboxEnv() {
		return EnvironmentType.SANDBOX == getEnvType();
	}

	/**
	 * 判断是否不为沙箱环境
	 *
	 * @return 是否不为沙箱环境
	 */
	public static boolean isNotSandboxEnv() {
		return !isSandboxEnv();
	}

	/**
	 * 判断是否为测试环境
	 *
	 * @return 是否为测试环境
	 */
	public static boolean isTestEnv() {
		return EnvironmentType.TEST == getEnvType();
	}

	/**
	 * 判断是否不为测试环境
	 *
	 * @return 是否不为测试环境
	 */
	public static boolean isNotTestEnv() {
		return !isTestEnv();
	}

	/**
	 * 判断是否为开发环境
	 *
	 * @return 是否为开发环境
	 */
	public static boolean isDevEnv() {
		return EnvironmentType.DEV == getEnvType();
	}

	/**
	 * 判断是否不为开发环境
	 *
	 * @return 是否不为开发环境
	 */
	public static boolean isNotDevEnv() {
		return !isDevEnv();
	}

	//endregion 环境相关 end


	//region 运行模式 start

	/**
	 * 获取运营模式
	 *
	 * @return 运行模式
	 */
	public static RunMode getRunMode() {
		return getInstance().runMode;
	}

	/**
	 * 判断是否为发行模式
	 *
	 * @return 是否为发行模式
	 */
	public static boolean isReleaseMode() {
		return RunMode.RELEASE == getRunMode();
	}

	/**
	 * 判断是否为调试模式
	 *
	 * @return 是否为调试模式
	 */
	public static boolean isDebugMode() {
		return RunMode.DEBUG == getRunMode();
	}

	/**
	 * 判断是否单元测试中
	 *
	 * @return 是否单元测试中
	 */
	public static boolean isInUnitTest() {
		return getInstance().inUnitTest;
	}

	//endregion 运行模式 end


	//region 其他全局配置 start

	/**
	 * 获取全局配置Map
	 *
	 * @return 全局配置Map
	 */
	public static Map<Object, Object> getConfigs() {
		return Collections.unmodifiableMap(getInstance().configs);
	}

	/**
	 * 获取配置值
	 *
	 * @param key 配置键
	 * @param <T> 配置值类型
	 * @return 配置值
	 */
	public static <T> T getConfig(Object key) {
		return (T)getInstance().configs.get(key);
	}

	/**
	 * 获取配置值
	 *
	 * @param key          配置键
	 * @param defaultValue 默认值
	 * @param <T>          配置值类型
	 * @return 配置值
	 */
	public static <T> T getConfig(Object key, T defaultValue) {
		T value = getConfig(key);
		if (ObjectUtil.isEmpty(value)) {
			return defaultValue;
		}
		return value;
	}

	//endregion 其他全局配置 end

	//endregion Getter end


	//region Setter start

	/**
	 * 设置项目所属区域代码
	 *
	 * @param area 项目所属区域代码
	 */
	static void setArea(String area) {
		getInstance().area = area;
	}

	/**
	 * 设置项目所属区域名称
	 *
	 * @param areaName 项目所属区域名称
	 */
	static void setAreaName(String areaName) {
		getInstance().areaName = areaName;
	}

	/**
	 * 设置项目代码
	 *
	 * @param project 项目代码
	 */
	static void setProject(String project) {
		getInstance().project = project;
	}

	/**
	 * 设置项目名称
	 *
	 * @param projectName 项目名称
	 */
	static void setProjectName(String projectName) {
		getInstance().projectName = projectName;
	}

	/**
	 * 设置应用代码
	 *
	 * @param application 应用代码
	 */
	static void setApplication(String application) {
		getInstance().application = application;
	}

	/**
	 * 设置应用名称
	 *
	 * @param applicationName 应用名称
	 */
	static void setApplicationName(String applicationName) {
		getInstance().applicationName = applicationName;
	}

	/**
	 * 设置环境代码
	 *
	 * @param env 环境代码
	 */
	static void setEnv(String env) {
		getInstance().env = env;
	}

	/**
	 * 设置环境名称
	 *
	 * @param envName 环境名称
	 */
	static void setEnvName(String envName) {
		getInstance().envName = envName;
	}

	/**
	 * 设置环境类型
	 *
	 * @param envType 环境类型
	 */
	static void setEnvType(EnvironmentType envType) {
		getInstance().envType = envType;
	}

	/**
	 * 设置运行模式
	 *
	 * @param runMode 运行模式
	 */
	static void setRunMode(RunMode runMode) {
		getInstance().runMode = runMode;
	}

	/**
	 * 设置当前是否单元测试中
	 *
	 * @param inUnitTest 是否单元测试中
	 */
	static void setInUnitTest(boolean inUnitTest) {
		getInstance().inUnitTest = inUnitTest;
	}

	/**
	 * 添加配置
	 *
	 * @param key   配置键
	 * @param value 配置值
	 */
	static synchronized void addConfig(Object key, Object value) {
		if (value != null) {
			getInstance().configs.put(key, value);
		}
	}

	/**
	 * 添加多个配置
	 *
	 * @param configs 配置集合
	 * @param <K>     配置键类型
	 * @param <V>     配置值类型
	 */
	static synchronized <K, V> void addConfigs(Map<K, V> configs) {
		if (configs != null && !configs.isEmpty()) {
			getInstance().configs.putAll(configs);
		}
	}

	//endregion Setter end
}
