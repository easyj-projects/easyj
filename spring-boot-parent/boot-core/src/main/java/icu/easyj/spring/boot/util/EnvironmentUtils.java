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
package icu.easyj.spring.boot.util;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.hutool.core.io.IORuntimeException;
import icu.easyj.spring.boot.exception.NotSupportedConfigFileException;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.DefaultPropertiesPropertySource;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 环境工具类
 *
 * @author wangliang181230
 */
public abstract class EnvironmentUtils {

	public static final String PREFIX = "easyj.global";

	public static final String AREA_KEY = PREFIX + ".area";
	public static final String PROJECT_KEY = PREFIX + ".project";
	public static final String ENV_KEY = PREFIX + ".env";


	//region 默认配置源 start

	public static final String DEFAULT_PROPERTY_SOURCE_NAME = DefaultPropertiesPropertySource.NAME;

	/**
	 * 添加或合并默认配置
	 *
	 * @param source     需添加的默认配置
	 * @param allSources 所有配置源
	 */
	public static void addOrMergeDefaultProperties(@Nullable Map<String, Object> source, @NonNull MutablePropertySources allSources) {
		if (!CollectionUtils.isEmpty(source)) {
			Map<String, Object> resultingSource = new HashMap<>();
			MapPropertySource defaultPropertySource = new MapPropertySource(DEFAULT_PROPERTY_SOURCE_NAME, resultingSource);
			if (allSources.contains(DEFAULT_PROPERTY_SOURCE_NAME)) {
				mergeIfPossible(source, allSources, resultingSource);
				allSources.replace(DEFAULT_PROPERTY_SOURCE_NAME, defaultPropertySource);
			} else {
				resultingSource.putAll(source);
				allSources.addLast(defaultPropertySource);
			}
		}
	}

	/**
	 * 添加或合并默认配置
	 *
	 * @param source      需添加的默认配置
	 * @param environment 环境实例
	 */
	public static void addOrMergeDefaultProperties(@Nullable Map<String, Object> source, ConfigurableEnvironment environment) {
		addOrMergeDefaultProperties(source, environment.getPropertySources());
	}

	/**
	 * 合并掉，如果可能的话（私有方法）
	 *
	 * @param source          需添加的默认配置
	 * @param allSources      所有配置源
	 * @param resultingSource 待返回的配置源
	 */
	private static void mergeIfPossible(@NonNull Map<String, Object> source,
										@NonNull MutablePropertySources allSources,
										@NonNull Map<String, Object> resultingSource) {
		PropertySource<?> existingSource = allSources.get(DEFAULT_PROPERTY_SOURCE_NAME);
		if (existingSource != null) {
			Object underlyingSource = existingSource.getSource();
			if (underlyingSource instanceof Map) {
				resultingSource.putAll((Map<String, Object>)underlyingSource);
			}
			resultingSource.putAll(source);
		}
	}

	//endregion 默认配置源 end


	//region 加载配置文件 start

	/**
	 * 加载配置文件，并创建配置集合
	 *
	 * @param configFilePath 配置文件相对地址
	 * @return properties 配置集合
	 */
	@Nullable
	public static Properties buildProperties(@NonNull String configFilePath) {
		// 创建资源对象
		ClassPathResource configFileResource = new ClassPathResource(configFilePath);
		if (!configFileResource.exists()) {
			// 配置文件不存在
			return null;
		}

		// 不同文件类型，采用不同的加载方式
		Properties properties;
		if (configFilePath.endsWith(".yml") || configFilePath.endsWith(".yaml")) {
			// 创建配置文件工厂对象
			YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
			yaml.setResources(configFileResource);
			// 创建配置源对象
			properties = yaml.getObject();
		} else if (configFilePath.endsWith(".properties")) {
			// 创建配置文件工厂对象
			PropertiesFactoryBean pro = new PropertiesFactoryBean();
			pro.setSingleton(false);
			pro.setLocation(configFileResource);
			// 创建配置源对象
			try {
				properties = pro.getObject();
			} catch (IOException e) {
				throw new IORuntimeException("配置文件加载失败：" + configFilePath, e);
			}
		} else {
			throw new NotSupportedConfigFileException("暂不支持该配置文件的解析：" + configFilePath);
		}
		return properties;
	}

	/**
	 * 根据配置文件相对地址，创建配置源名称
	 *
	 * @param configFilePath 配置文件相对地址
	 * @return propertySourceName 配置源名称
	 */
	@NonNull
	public static String buildPropertySourceNameByPath(@NonNull String configFilePath) {
		return "[EasyJ] Config resource '" + configFilePath + "'";
	}

	/**
	 * 创建配置文件为配置源
	 *
	 * @param configFilePath 配置文件相对地址
	 * @param immutable      配置源是否不会更改
	 * @return propertySource 配置源
	 */
	@Nullable
	public static PropertySource<?> buildPropertySource(@NonNull String configFilePath, boolean immutable) {
		// 加载配置文件
		Properties properties = buildProperties(configFilePath);
		if (properties == null || properties.isEmpty()) {
			// 配置文件不存在或为空
			return null;
		}

		// 根据配置文件相对地址，生成配置源名称
		String propertySourceName = buildPropertySourceNameByPath(configFilePath);

		// 创建配置源
		Map<?, ?> source = immutable ? Collections.unmodifiableMap(properties) : properties;
		return new OriginTrackedMapPropertySource(propertySourceName, source, immutable);
	}

	/**
	 * 加载配置文件到环境中的最后位置
	 *
	 * @param configFilePath 配置文件相对地址
	 * @param immutable      配置源是否不会更改
	 * @param environment    环境实例
	 * @return propertySource 配置源
	 */
	@Nullable
	public static PropertySource<?> addPropertySourceToLast(@NonNull String configFilePath, boolean immutable, @NonNull ConfigurableEnvironment environment) {
		// 加载配置文件为配置源
		PropertySource<?> propertySource = buildPropertySource(configFilePath, immutable);
		if (propertySource != null) {
			// 添加配置源到末尾，优先级最低
			addLastButBeforeDefault(propertySource, environment);
			return propertySource;
		} else {
			// 配置文件不存在或为空
			return null;
		}
	}

	/**
	 * 将配置源添加到最后位置，但比`defaultProperties`配置源要前面
	 *
	 * @param propertySource 配置源
	 * @param environment    环境实例
	 */
	public static void addLastButBeforeDefault(@NonNull PropertySource<?> propertySource, @NonNull ConfigurableEnvironment environment) {
		MutablePropertySources propertySources = environment.getPropertySources();
		if (propertySources.contains(DEFAULT_PROPERTY_SOURCE_NAME)) {
			propertySources.addBefore(DEFAULT_PROPERTY_SOURCE_NAME, propertySource);
		} else {
			propertySources.addLast(propertySource);
		}
	}

	//endregion


	//region 获取配置值 start

	/**
	 * 获取环境代码
	 *
	 * @param environment 环境
	 * @return env 环境代码
	 */
	@Nullable
	public static String getEnv(@NonNull ConfigurableEnvironment environment) {
		String env = environment.getProperty(ENV_KEY);
		if (!StringUtils.hasText(env)) {
			env = environment.getProperty("spring.profiles.active");
			if (!StringUtils.hasText(env)) {
				env = environment.getProperty("spring.profiles.active[0]");
			}
		}

		return env;
	}

	//endregion
}
