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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.ResourceUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.crypto.CryptoFactory;
import icu.easyj.crypto.GlobalCrypto;
import icu.easyj.crypto.asymmetric.IAsymmetricCrypto;
import icu.easyj.crypto.symmetric.ISymmetricCrypto;
import icu.easyj.spring.boot.util.EnvironmentUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import static icu.easyj.spring.boot.autoconfigure.StarterConstants.GLOBAL_ASYMMETRIC_CRYPTO_PREFIX;
import static icu.easyj.spring.boot.autoconfigure.StarterConstants.GLOBAL_SYMMETRIC_CRYPTO_PREFIX;

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
	 * HIGHEST + 20（为了兼容低版本的springboot，不使用springboot的常量）
	 */
	public static final int ORDER = (Ordered.HIGHEST_PRECEDENCE + 10) + 10; // ConfigDataEnvironmentPostProcessor.ORDER + 10;

	/**
	 * 指定优先加载的配置文件地址
	 * 按优先级倒序排序
	 */
	public static final String[] CONFIG_FILE_PATHS = new String[]{
			// 全局配置文件，优先级仅高于`defaultProperties`配置源的配置文件
			"config/global.properties",
			"config/global.yaml",
			"config/global.yml",
			// 区域配置文件，主要配置区域代码、区域名称或其他区域所需的配置，相同配置会覆盖全局配置文件
			"config/area.properties",
			"config/area.yaml",
			"config/area.yml",
			// 项目配置文件，主要配置项目代码、项目名称或其他项目所需的配置，相同配置会覆盖区域配置文件
			"config/project.properties",
			"config/project.yaml",
			"config/project.yml"
	};

	/**
	 * 需加载配置文件的目录，这些目录下的所有*.yml、*.yaml、*.properties文件，都将被加载
	 * 按优先级倒序排列
	 */
	public static final String[] CONFIG_DIRECTORY_PATHS = new String[]{
			// 全局默认配置
			"config/default/",
			// 区域默认配置
			"config/${area}/default/",
			// 项目默认配置
			"config/${project}/default/",
			// 区域项目默认配置
			"config/${area}/${project}/default/",
			// 环境配置
			"config/${env}/",
			// 区域环境配置
			"config/${area}/${env}/",
			// 项目环境配置
			"config/${project}/${env}/",
			// 区域项目环境配置
			"config/${area}/${project}/${env}/"
	};

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		MutablePropertySources propertySources = environment.getPropertySources();

		String previousPropertySourceName = null;
		String currentPropertySourceName;


		//region 优先加载的配置文件

		// 倒序加载
		String filePath;
		Resource[] resources;
		for (int i = CONFIG_FILE_PATHS.length - 1; i >= 0; --i) {
			filePath = CONFIG_FILE_PATHS[i];

			// 加载所有jar包中的所有同名配置文件
			resources = ResourceUtils.getResources("classpath*:/" + filePath);

			for (Resource resource : resources) {
				currentPropertySourceName = EnvironmentUtils.buildPropertySourceNameByPath(resource);
				if (propertySources.contains(currentPropertySourceName)) {
					continue;
				}
				PropertySource<?> propertySource = EnvironmentUtils.addPropertySourceToLast(resource, true, environment);
				if (propertySource != null && previousPropertySourceName == null) {
					previousPropertySourceName = currentPropertySourceName;
				}
			}
		}

		//endregion


		//region 从应用配置及上面优先加载的配置中，获取以下配置信息

		// 项目所属区域代码
		String area = environment.getProperty(EnvironmentUtils.AREA_KEY);
		// 项目代码
		String project = environment.getProperty(EnvironmentUtils.PROJECT_KEY);
		// 环境代码
		String env = EnvironmentUtils.getEnv(environment);
		if ("default".equalsIgnoreCase(env)) {
			// 如果环境为'default'，则设置为null
			env = null;
		}

		//endregion


		//region 加载约定目录下的配置文件

		for (String dirPath : CONFIG_DIRECTORY_PATHS) {
			//region 替换目录中的表达式

			if (dirPath.contains("${area}")) {
				if (StringUtils.isNotEmpty(area)) {
					dirPath = dirPath.replace("${area}", area);
				} else {
					continue;
				}
			}
			if (dirPath.contains("${project}")) {
				if (StringUtils.isNotEmpty(project)) {
					dirPath = dirPath.replace("${project}", project);
				} else {
					continue;
				}
			}
			if (dirPath.contains("${env}")) {
				if (StringUtils.isNotEmpty(env)) {
					dirPath = dirPath.replace("${env}", env);
				} else {
					continue;
				}
			}

			//endregion


			// 加载目录下的所有配置文件
			Resource[] configFileResources = loadConfigFileResources(dirPath);
			if (ArrayUtils.isEmpty(configFileResources)) {
				continue;
			}

			// 先加载所有配置源过滤器
			List<IPropertySourceFilter> filters = EnhancedServiceLoader.loadAll(IPropertySourceFilter.class);

			// 读取配置文件，并添加配置源
			// 根据文件名倒序加载。文件名越靠前，配置优先级越高
			Resource resource;
			for (int i = configFileResources.length - 1; i >= 0; --i) {
				resource = configFileResources[i];

				// 判断相同文件是否已经添加过
				currentPropertySourceName = EnvironmentUtils.buildPropertySourceNameByPath(resource);
				if (environment.getPropertySources().contains(currentPropertySourceName)) {
					continue;
				}

				// 加载配置文件为配置源
				MapPropertySource propertySource = EnvironmentUtils.buildPropertySource(resource, true);
				if (propertySource == null) {
					// 配置文件不存在或为空
					continue;
				}

				// 执行配置源过滤器，用于过滤部分无需加载的配置源
				if (this.doFilters(propertySource, filters)) {
					// 过滤掉当前配置源
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

		//endregion


		// 加载全局加密算法配置，并生成加密算法实例到`GlobalCrypto`类中
		// 在这里就加载的原因：环境加载过程中，有个函数式配置需要用到`GlobalCrypto`：${easyj.crypto.decrypt('xxxxxxxxbase64')}
		// @see CryptoPropertyUtils
		this.loadGlobalCrypto(environment);
	}

	/**
	 * 执行过滤器
	 *
	 * @param propertySource 配置源
	 * @param filters        过滤器列表
	 * @return 是否需要过滤，true=过滤掉|false=不过滤
	 */
	private boolean doFilters(MapPropertySource propertySource, List<IPropertySourceFilter> filters) {
		if (!CollectionUtils.isEmpty(filters)) {
			for (IPropertySourceFilter filter : filters) {
				if (filter.doFilter(propertySource)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 加载全局加密算法配置，并生成加密算法实例
	 *
	 * @param environment 环境
	 */
	private void loadGlobalCrypto(Environment environment) {
		//region 加载全局的非对称加密算法

		// 读取配置：非对称加密算法
		String algorithm = environment.getProperty(GLOBAL_ASYMMETRIC_CRYPTO_PREFIX + ".algorithm");
		if (StringUtils.isNotEmpty(algorithm)) {
			// 读取配置：私钥
			String privateKey = environment.getProperty(GLOBAL_ASYMMETRIC_CRYPTO_PREFIX + ".private-key");
			Assert.notNull(privateKey, "非对称加密私钥 '" + GLOBAL_ASYMMETRIC_CRYPTO_PREFIX + ".private-key' 未配置，无法生成全局非对称加密算法.");

			// 读取配置：公钥
			String publicKey = environment.getProperty(GLOBAL_ASYMMETRIC_CRYPTO_PREFIX + ".public-key");
			Assert.notNull(publicKey, "非对称加密公钥 '" + GLOBAL_ASYMMETRIC_CRYPTO_PREFIX + ".public-key' 未配置，无法生成全局非对称加密算法.");

			// 生成非对称加密算法实例
			IAsymmetricCrypto asymmetricCrypto = CryptoFactory.getAsymmetricCrypto(algorithm, publicKey, privateKey);
			// 设置全局非对称加密算法
			GlobalCrypto.setAsymmetricCrypto(asymmetricCrypto);
		}

		//endregion


		//region 加载全局的对称加密算法

		// 读取配置：非对称加密算法
		algorithm = environment.getProperty(GLOBAL_SYMMETRIC_CRYPTO_PREFIX + ".algorithm");
		if (StringUtils.isNotEmpty(algorithm)) {
			// 读取配置：密钥
			String key = environment.getProperty(GLOBAL_SYMMETRIC_CRYPTO_PREFIX + ".key");
			Assert.notNull(key, "对称加密密钥 '" + GLOBAL_SYMMETRIC_CRYPTO_PREFIX + ".key' 未配置，无法生成全局对称加密算法.");

			// 读取配置：偏移向量
			String iv = environment.getProperty(GLOBAL_SYMMETRIC_CRYPTO_PREFIX + ".iv");
			// 读取配置：密钥编码
			Charset charset = environment.getProperty(GLOBAL_SYMMETRIC_CRYPTO_PREFIX + ".charset", Charset.class, StandardCharsets.UTF_8);

			// 生成对称加密算法实例
			ISymmetricCrypto symmetricCrypto = CryptoFactory.getSymmetricCrypto(algorithm, key, iv, charset);
			// 设置全局对称加密算法
			GlobalCrypto.setSymmetricCrypto(symmetricCrypto);
		}

		//endregion
	}

	/**
	 * 读取目录下所有的配置文件路径
	 *
	 * @param dirPath 目录路径
	 * @return configFilePaths 配置文件路径数组
	 */
	private Resource[] loadConfigFileResources(String dirPath) {
		String[] configFileLocationPatternArr = new String[]{
				"classpath*:/" + dirPath + "*.yml",
				"classpath*:/" + dirPath + "*.yaml",
				"classpath*:/" + dirPath + "*.properties"
		};
		return ResourceUtils.getResources(configFileLocationPatternArr);
	}

	@Override
	public int getOrder() {
		return ORDER;
	}
}
