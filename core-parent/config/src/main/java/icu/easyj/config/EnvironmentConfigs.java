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
package icu.easyj.config;

import icu.easyj.core.env.EnvironmentType;
import icu.easyj.core.env.RunMode;

/**
 * 全局配置
 *
 * @author wangliang181230
 */
public class EnvironmentConfigs {

	//region Fields

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

	//endregion


	//region 全局配置持有者（设计模式-创建型模式-单例模式-枚举实现单例）

	private EnvironmentConfigs() {
	}

	private enum SingletonHolder {
		// 单例
		INSTANCE;

		private final EnvironmentConfigs configs = new EnvironmentConfigs();

		public EnvironmentConfigs getConfigs() {
			return configs;
		}
	}

	private static EnvironmentConfigs getInstance() {
		return SingletonHolder.INSTANCE.getConfigs();
	}

	//endregion


	//region Getter start

	////region 环境相关 start

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

	////endregion 环境相关 end


	////region 运行模式 start

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

	////endregion 运行模式 end

	//endregion Getter end


	//region Setter start

	/**
	 * 设置环境代码
	 *
	 * @param env 环境代码
	 */
	public static void setEnv(String env) {
		getInstance().env = env;
	}

	/**
	 * 设置环境名称
	 *
	 * @param envName 环境名称
	 */
	public static void setEnvName(String envName) {
		getInstance().envName = envName;
	}

	/**
	 * 设置环境类型
	 *
	 * @param envType 环境类型
	 */
	public static void setEnvType(EnvironmentType envType) {
		getInstance().envType = envType;
	}

	/**
	 * 设置运行模式
	 *
	 * @param runMode 运行模式
	 */
	public static void setRunMode(RunMode runMode) {
		getInstance().runMode = runMode;
	}

	/**
	 * 设置当前是否单元测试中
	 *
	 * @param inUnitTest 是否单元测试中
	 */
	public static void setInUnitTest(boolean inUnitTest) {
		getInstance().inUnitTest = inUnitTest;
	}

	//endregion Setter end
}
