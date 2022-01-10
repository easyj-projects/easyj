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

import icu.easyj.config.AppConfigs;
import icu.easyj.core.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

/**
 * EasyJ定义的项目及应用配置
 *
 * @author wangliang181230
 * @see AppConfigs
 */
public class AppProperties implements InitializingBean {

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
	@Value("${easyj.app.application:${spring.application.name:}}")
	private String application;

	/**
	 * 应用名称
	 */
	private String applicationName;

	@Override
	public void afterPropertiesSet() {
		if (StringUtils.isNotBlank(area)) {
			AppConfigs.setArea(area.trim());
		}
		if (StringUtils.isNotBlank(areaName)) {
			AppConfigs.setAreaName(areaName.trim());
		}
		if (StringUtils.isNotBlank(project)) {
			AppConfigs.setProject(project.trim());
		}
		if (StringUtils.isNotBlank(projectName)) {
			AppConfigs.setProjectName(projectName.trim());
		}
		if (StringUtils.isNotBlank(application)) {
			AppConfigs.setApplication(application.trim());
		}
		if (StringUtils.isNotBlank(applicationName)) {
			AppConfigs.setApplicationName(applicationName.trim());
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

	//endregion
}
