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
package icu.easyj.spring.boot.autoconfigure.global.configs;

import java.util.Map;

import icu.easyj.config.GlobalConfigSetter;
import icu.easyj.env.EnvironmentType;
import icu.easyj.env.RunMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * EasyJ定义的全局配置
 *
 * @author wangliang181230
 * @see icu.easyj.config.GlobalConfigs
 */
public class GlobalProperties implements InitializingBean {

	/**
	 * 项目所属区域代码
	 */
	private String area;

	/**
	 * 项目所属区域名称
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
	@Value("${easyj.global.application:${spring.application.name:}}")
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
	private EnvironmentType envType;

	/**
	 * 运行模式
	 * 默认：发行模式
	 * 用途举例：为DEBUG模式时，打印更多日志。比修改日志配置更加高效，运维成本更低。
	 */
	private RunMode runMode;

	/**
	 * 其他全局配置Map
	 */
	private Map<Object, Object> configs;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (StringUtils.hasText(area)) {
			GlobalConfigSetter.setArea(area.trim());
		}
		if (StringUtils.hasText(areaName)) {
			GlobalConfigSetter.setAreaName(areaName.trim());
		}
		if (StringUtils.hasText(project)) {
			GlobalConfigSetter.setProject(project.trim());
		}
		if (StringUtils.hasText(projectName)) {
			GlobalConfigSetter.setProjectName(projectName.trim());
		}
		if (StringUtils.hasText(application)) {
			GlobalConfigSetter.setApplication(application.trim());
		}
		if (StringUtils.hasText(applicationName)) {
			GlobalConfigSetter.setApplicationName(applicationName.trim());
		}
		if (StringUtils.hasText(env)) {
			GlobalConfigSetter.setEnv(env.trim());
		}
		if (StringUtils.hasText(envName)) {
			GlobalConfigSetter.setEnvName(envName.trim());
		}
		if (envType != null) {
			GlobalConfigSetter.setEnvType(envType);
		}
		if (runMode != null) {
			GlobalConfigSetter.setRunMode(runMode);
		}
		if (!CollectionUtils.isEmpty(configs)) {
			GlobalConfigSetter.addConfigs(configs);
		}
	}

	//region Getter、Setter

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

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

	public Map<Object, Object> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<Object, Object> configs) {
		this.configs = configs;
	}

	//endregion
}