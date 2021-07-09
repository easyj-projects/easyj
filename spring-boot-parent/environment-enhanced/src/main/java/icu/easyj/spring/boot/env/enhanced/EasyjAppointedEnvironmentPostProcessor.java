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
package icu.easyj.spring.boot.env.enhanced;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.IORuntimeException;
import icu.easyj.core.util.ResourceUtils;
import icu.easyj.spring.boot.util.EnvironmentUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 加载 `EasyJ约定的目录下的配置文件` 的环境处理器
 * <p>
 * 思路：约定大于配置<br>
 * 作用：框架中，可以使用此功能，快速的配置项目的一些通用配置、功能性约定配置、不同环境的不同通用和约定配置，以此来简化业务项目的配置。<br>
 * 使用场景：用来配置几乎不会改变的一些配置，如公司内部开发环境下的数据库、redis、各种MQ、注册中心、配置中心等等的一些配置。<br>
 *
 * @author wangliang181230
 */
public class EasyjAppointedEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	/**
	 * HIGHEST + 20
	 */
	public static final int ORDER = ConfigDataEnvironmentPostProcessor.ORDER + 10;

	/**
	 * 指定优先加载的配置文件地址
	 * 按优先级正序排序
	 */
	public static final String[] CONFIG_FILE_PATHS = new String[]{
			// 项目配置文件，主要配置项目代码、项目名称或其他全局配置，相同配置会覆盖区域配置文件
			"config/project.yml",
			"config/project.yaml",
			"config/project.properties",
			// 区域配置文件，主要配置区域代码、区域名称或其他全局配置
			"config/area.yml",
			"config/area.yaml",
			"config/area.properties",
			// 全局配置文件，优先级仅高于`defaultProperties`配置源的配置文件
			"config/global.yml",
			"config/global.yaml",
			"config/global.properties",
	};

	/**
	 * 需加载配置文件的目录，这些目录下的所有*.yml、*.yaml、*.properties文件，都将被加载
	 * 按优先级倒序排列
	 */
	public static final String[] CONFIG_DIRECTORY_PATHS = new String[]{
			// 全局默认配置
			"config/default",
			// 区域默认配置
			"config/${area}/default",
			// 项目默认配置
			"config/${project}/default",
			// 区域项目默认配置
			"config/${area}/${project}/default",
			// 环境配置
			"config/${env}",
			// 区域环境配置
			"config/${area}/${env}",
			// 项目环境配置
			"config/${project}/${env}",
			// 区域项目环境配置
			"config/${area}/${project}/${env}",
	};

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		MutablePropertySources propertySources = environment.getPropertySources();

		// 优先加载的配置文件
		String previousPropertySourceName = null;
		String currentPropertySourceName;
		for (String filePath : CONFIG_FILE_PATHS) {
			currentPropertySourceName = EnvironmentUtils.buildPropertySourceNameByPath(filePath);
			if (propertySources.contains(currentPropertySourceName)) {
				continue;
			}
			PropertySource<?> propertySource = EnvironmentUtils.addPropertySourceToLast(filePath, true, environment);
			if (propertySource != null) {
				previousPropertySourceName = currentPropertySourceName;
			}
		}

		// 项目所属区域代码：在area配置文件加载后，再获取配置值
		String area = environment.getProperty(EnvironmentUtils.AREA_KEY);
		// 项目代码：在project配置文件加载后，再获取配置值
		String project = environment.getProperty(EnvironmentUtils.PROJECT_KEY);
		// 环境代码
		String env = EnvironmentUtils.getEnv(environment);
		if ("default".equalsIgnoreCase(env)) {
			env = null;
		}

		for (String dirPath : CONFIG_DIRECTORY_PATHS) {
			// 替换表达式
			if (dirPath.contains("${area}")) {
				if (StringUtils.hasLength(area)) {
					dirPath = dirPath.replace("${area}", area);
				} else {
					continue;
				}
			}
			if (dirPath.contains("${project}")) {
				if (StringUtils.hasLength(project)) {
					dirPath = dirPath.replace("${project}", project);
				} else {
					continue;
				}
			}
			if (dirPath.contains("${env}")) {
				if (StringUtils.hasLength(env)) {
					dirPath = dirPath.replace("${env}", env);
				} else {
					continue;
				}
			}

			// 加载目录下的所有配置文件
			List<String> configFilePathList = loadConfigFilePathList(dirPath);
			if (CollectionUtils.isEmpty(configFilePathList)) {
				continue;
			}

			// 读取配置文件，并添加配置源
			for (String path : configFilePathList) {
				// 判断相同文件是否已经添加过
				currentPropertySourceName = EnvironmentUtils.buildPropertySourceNameByPath(path);
				if (environment.getPropertySources().contains(currentPropertySourceName)) {
					continue;
				}

				// 加载配置文件为配置源
				PropertySource<?> propertySource = EnvironmentUtils.buildPropertySource(path, true);
				if (propertySource == null) {
					// 配置文件不存在或为空
					continue;
				}

				// 添加配置源
				if (previousPropertySourceName == null) {
					EnvironmentUtils.addLastButBeforeDefault(propertySource, environment);
				} else {
					propertySources.addBefore(previousPropertySourceName, propertySource);
				}

				previousPropertySourceName = currentPropertySourceName;
			}
		}
	}

	/**
	 * 读取目录下所有的配置文件路径
	 *
	 * @param dirPath 目录路径
	 * @return configFilePaths 配置文件路径数组
	 */
	@Nullable
	private List<String> loadConfigFilePathList(@NonNull String dirPath) {
		Resource[] resources = ResourceUtils.getResources(dirPath);
		if (resources == null || resources.length == 0) {
			return null;
		}

		List<String> filePathList = new ArrayList<>();
		for (Resource resource : resources) {
			try {
				if (!resource.exists()) {
					continue;
				}
				for (String fileName : resource.getFile().list()) {
					if (fileName.endsWith(".yml") || fileName.endsWith(".yaml") || fileName.endsWith(".properties")) {
						filePathList.add(dirPath + "/" + fileName);
					}
				}
			} catch (IOException e) {
				throw new IORuntimeException("加载配置文件失败", e);
			}
		}
		return filePathList;
	}

	@Override
	public int getOrder() {
		return ORDER;
	}
}
