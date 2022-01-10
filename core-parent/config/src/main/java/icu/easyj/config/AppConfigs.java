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

/**
 * 项目及应用配置
 *
 * @author wangliang181230
 */
public class AppConfigs {

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

	//endregion


	//region 项目及应用配置持有者（设计模式-创建型模式-单例模式-枚举实现单例）

	private AppConfigs() {
	}

	private enum SingletonHolder {
		// 单例
		INSTANCE;

		private final AppConfigs configs = new AppConfigs();

		public AppConfigs getConfigs() {
			return configs;
		}
	}

	private static AppConfigs getInstance() {
		return SingletonHolder.INSTANCE.getConfigs();
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

	//endregion Getter end


	//region Setter start

	/**
	 * 设置项目所属区域代码
	 *
	 * @param area 项目所属区域代码
	 */
	public static void setArea(String area) {
		getInstance().area = area;
	}

	/**
	 * 设置项目所属区域名称
	 *
	 * @param areaName 项目所属区域名称
	 */
	public static void setAreaName(String areaName) {
		getInstance().areaName = areaName;
	}

	/**
	 * 设置项目代码
	 *
	 * @param project 项目代码
	 */
	public static void setProject(String project) {
		getInstance().project = project;
	}

	/**
	 * 设置项目名称
	 *
	 * @param projectName 项目名称
	 */
	public static void setProjectName(String projectName) {
		getInstance().projectName = projectName;
	}

	/**
	 * 设置应用代码
	 *
	 * @param application 应用代码
	 */
	public static void setApplication(String application) {
		getInstance().application = application;
	}

	/**
	 * 设置应用名称
	 *
	 * @param applicationName 应用名称
	 */
	public static void setApplicationName(String applicationName) {
		getInstance().applicationName = applicationName;
	}

	//endregion Setter end
}
