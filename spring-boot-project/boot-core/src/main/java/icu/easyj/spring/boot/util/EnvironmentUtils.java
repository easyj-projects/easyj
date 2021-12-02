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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.text.StrPool;
import icu.easyj.core.util.MapUtils;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.ResourceUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.spring.boot.exception.NotSupportedConfigFileTypeException;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.DefaultPropertiesPropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
		if (!MapUtils.isEmpty(source)) {
			Map<String, Object> resultingSource = new HashMap<>(source.size());
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
	 * @param configFileResource 配置文件资源
	 * @return properties 配置集合
	 */
	@Nullable
	public static Properties buildProperties(@NonNull Resource configFileResource) {
		String configFileName = configFileResource.getFilename();
		if (configFileName == null) {
			return null;
		}

		// 不同文件类型，采用不同的加载方式
		Properties properties;
		if (configFileName.endsWith(".yml") || configFileName.endsWith(".yaml")) {
			// 创建配置文件工厂对象
			YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
			yaml.setResources(configFileResource);
			// 创建配置源对象
			properties = yaml.getObject();
		} else if (configFileName.endsWith(".properties")) {
			// 创建配置文件工厂对象
			PropertiesFactoryBean pro = new PropertiesFactoryBean();
			pro.setSingleton(false);
			pro.setLocation(configFileResource);
			// 创建配置源对象
			try {
				properties = pro.getObject();
			} catch (IOException e) {
				throw new IORuntimeException("配置文件加载失败：" + configFileName, e);
			}
		} else {
			throw new NotSupportedConfigFileTypeException("暂不支持该类型的配置文件解析，文件名：" + configFileName);
		}
		return properties;
	}

	/**
	 * 加载配置文件，并创建配置集合
	 *
	 * @param configFilePath 配置文件路径
	 * @return properties 配置集合
	 */
	@Nullable
	public static Properties buildProperties(@NonNull String configFilePath) {
		// 创建资源对象
		Resource configFileResource = new ClassPathResource(configFilePath);
		if (!configFileResource.exists()) {
			// 配置文件不存在
			return null;
		}

		return buildProperties(configFileResource);
	}

	/**
	 * 根据配置文件路径，创建配置源名称
	 *
	 * @param configFilePath 配置文件路径
	 * @return propertySourceName 配置源名称
	 */
	@NonNull
	public static String buildPropertySourceNameByPath(@NonNull String configFilePath) {
		return "[EasyJ] Config resource '" + configFilePath + "'";
	}

	/**
	 * 根据配置文件路径，创建配置源名称
	 *
	 * @param configFileResource 配置文件资源
	 * @return propertySourceName 配置源名称
	 */
	@NonNull
	public static String buildPropertySourceNameByPath(@NonNull Resource configFileResource) {
		String configFilePath = ResourceUtils.getResourceUri(configFileResource);
		return buildPropertySourceNameByPath(configFilePath);
	}

	/**
	 * 创建配置文件为配置源
	 *
	 * @param configFileResource 配置文件资源
	 * @param immutable          配置源是否不会更改
	 * @return propertySource 配置源
	 */
	@Nullable
	public static MapPropertySource buildPropertySource(@NonNull Resource configFileResource, boolean immutable) {
		// 加载配置文件
		Properties properties = buildProperties(configFileResource);
		if (properties == null || properties.isEmpty()) {
			// 配置文件不存在或为空
			return null;
		}

		// 根据配置文件路径，生成配置源名称
		String propertySourceName = buildPropertySourceNameByPath(configFileResource);

		// 创建配置源
		Map source = immutable ? Collections.unmodifiableMap(properties) : properties;
		return newMapPropertySource(propertySourceName, source, immutable);
	}

	/**
	 * 创建配置源
	 *
	 * @param propertySourceName 配置源名称
	 * @param source             配置源
	 * @param immutable          配置源是否不会更改
	 * @return 配置源
	 */
	@NonNull
	public static MapPropertySource newMapPropertySource(@NonNull String propertySourceName,
														 @NonNull Map<String, Object> source,
														 boolean immutable) {
		// spring-boot从2.0.0版本开始，才有OriginTrackedMapPropertySource
		try {
			Class clazz = ReflectionUtils.getClassByName("org.springframework.boot.env.OriginTrackedMapPropertySource");
			try {
				// 低于2.2.0版本的springboot中，OriginTrackedMapPropertySource类是没有immutable属性的，特殊处理一下
				Constructor constructor = clazz.getConstructor(String.class, Map.class, boolean.class);
				return (MapPropertySource)constructor.newInstance(propertySourceName, source, immutable);
			} catch (NoSuchMethodException e) {
				Constructor constructor = clazz.getConstructor(String.class, Map.class);
				return (MapPropertySource)constructor.newInstance(propertySourceName, source);
			}
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ignore) {
			return new MapPropertySource(propertySourceName, source);
		}
	}

	/**
	 * 创建配置文件为配置源
	 *
	 * @param configFilePath 配置文件路径
	 * @param immutable      配置源是否不会更改
	 * @return propertySource 配置源
	 */
	@Nullable
	public static MapPropertySource buildPropertySource(@NonNull String configFilePath, boolean immutable) {
		// 创建资源对象
		Resource configFileResource = new ClassPathResource(configFilePath);
		if (!configFileResource.exists()) {
			// 配置文件不存在
			return null;
		}

		return buildPropertySource(configFileResource, immutable);
	}

	/**
	 * 加载配置文件到环境中的最后位置
	 *
	 * @param configFileResource 配置文件资源
	 * @param immutable          配置源是否不会更改
	 * @param environment        环境实例
	 * @return propertySource 配置源
	 */
	@Nullable
	public static PropertySource<?> addPropertySourceToLast(@NonNull Resource configFileResource, boolean immutable, @NonNull ConfigurableEnvironment environment) {
		// 加载配置文件为配置源
		PropertySource<?> propertySource = buildPropertySource(configFileResource, immutable);
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
	 * 加载配置文件到环境中的最后位置
	 *
	 * @param configFilePath 配置文件路径
	 * @param immutable      配置源是否不会更改
	 * @param environment    环境实例
	 * @return propertySource 配置源
	 */
	@Nullable
	public static PropertySource<?> addPropertySourceToLast(@NonNull String configFilePath, boolean immutable, @NonNull ConfigurableEnvironment environment) {
		// 创建资源对象
		Resource configFileResource = new ClassPathResource(configFilePath);
		if (!configFileResource.exists()) {
			// 配置文件不存在
			return null;
		}

		return addPropertySourceToLast(configFileResource, immutable, environment);
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
		if (StringUtils.isBlank(env)) {
			env = environment.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
			if (StringUtils.isBlank(env)) {
				env = environment.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME + "[0]");
			}
		}

		return env;
	}

	/**
	 * 获取配置列表
	 *
	 * @param environment  环境
	 * @param propertyName 配置名
	 * @return propertyList 配置值列表
	 */
	@NonNull
	public static List<String> getPropertyList(@NonNull ConfigurableEnvironment environment, String propertyName) {
		List<String> propertyList = new ArrayList<>();
		String property = environment.getProperty(propertyName);
		if (property != null) {
			propertyList.add(property);
		} else {
			int i = 0;
			while ((property = environment.getProperty(propertyName + "[" + i + "]")) != null) {
				propertyList.add(property);
				i++;
			}
		}
		return propertyList;
	}

	/**
	 * 获取配置列表
	 *
	 * @param propertySource 配置源
	 * @param propertyName   配置名
	 * @return propertyList 配置值列表
	 */
	@NonNull
	public static List<String> getPropertyList(@NonNull PropertySource<?> propertySource, String propertyName) {
		List<String> propertyList = new ArrayList<>();
		String property = getPropertyStr(propertySource, propertyName);
		if (property != null) {
			if (property.contains(StrPool.COMMA)) {
				String[] propertyArr = property.split(StrPool.COMMA);
				for (String pro : propertyArr) {
					if (StringUtils.isNotBlank(pro)) {
						propertyList.add(pro.trim());
					}
				}
			} else {
				if (StringUtils.isNotBlank(property)) {
					propertyList.add(property.trim());
				}
			}
		} else {
			int i = 0;
			while ((property = getPropertyStr(propertySource, propertyName + "[" + i + "]")) != null) {
				if (StringUtils.isNotBlank(property)) {
					propertyList.add(property.trim());
				}
				i++;
			}
		}
		return propertyList;
	}

	@Nullable
	private static String getPropertyStr(@NonNull PropertySource<?> propertySource, String propertyName) {
		Object propertyObj = propertySource.getProperty(propertyName);
		if (propertyObj == null) {
			return null;
		} else {
			return String.valueOf(propertyObj);
		}
	}

	//endregion
}
