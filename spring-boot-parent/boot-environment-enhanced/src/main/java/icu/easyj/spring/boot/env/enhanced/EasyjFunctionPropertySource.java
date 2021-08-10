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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import icu.easyj.core.code.analysis.CodeAnalysisResult;
import icu.easyj.core.code.analysis.CodeAnalysisUtils;
import icu.easyj.core.exception.ConfigurationException;
import icu.easyj.spring.boot.autoconfigure.StarterConstants;
import icu.easyj.spring.boot.env.enhanced.util.CryptoPropertyUtils;
import icu.easyj.spring.boot.env.enhanced.util.LocalIpPropertyUtils;
import icu.easyj.spring.boot.env.enhanced.util.RandomPropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * 函数式配置源
 *
 * @author wangliang181230
 */
public class EasyjFunctionPropertySource extends PropertySource<Object> {

	/**
	 * 配置源名
	 */
	public static final String PROPERTY_SOURCE_NAME = "easyjPropertySource";

	/**
	 * 配置前缀
	 */
	public static final String PREFIX = StarterConstants.PREFIX + ".";

	/**
	 * 不使用缓存的配置值
	 */
	private static final String NO_CACHE_VALUE = "no-cache";

	//region 函数相关常量

	/**
	 * 加密函数名
	 */
	private static final String CRYPTO_FUN_NAME = "crypto";

	/**
	 * 本地IP函数名
	 */
	private static final String LOCAL_IP_FUN_NAME = "localIp";

	/**
	 * 随机函数名
	 */
	private static final String RANDOM_FUN_NAME = "random";

	//endregion

	/**
	 * 缓存
	 */
	private static final Map<String, Object> CACHE = new HashMap<>();


	/**
	 * 构造函数
	 */
	public EasyjFunctionPropertySource() {
		super(PROPERTY_SOURCE_NAME);
	}


	/**
	 * 获取配置值
	 *
	 * @param name 配置键
	 * @return 配置值
	 */
	@Override
	public Object getProperty(String name) {
		// 判断该配置是否为当前配置源的配置
		if (!this.isMatch(name)) {
			return null;
		}

		// 获取缓存
		if (CACHE.containsKey(name)) {
			return CACHE.get(name);
		}

		// 解析代码，目前限制最多只读取4个参数
		CodeAnalysisResult result = CodeAnalysisUtils.analysisCode(name.substring(PREFIX.length()));
		if (result == null) {
			throw new ConfigurationException("配置信息格式有误：" + name);
		}

		// 判断是否需要缓存此次生成的配置值
		boolean needCache = this.isNeedCache(result);

		// 获取配置值
		Object value = this.computeProperty(result);

		// 设置缓存
		if (needCache) {
			CACHE.put(name, value);
		}

		// 返回配置值
		return value;
	}

	/**
	 * 判断该配置是否为当前配置源的配置
	 *
	 * @param name 配置键
	 * @return 是否为当前配置源的配置
	 */
	private boolean isMatch(String name) {
		if (!StringUtils.hasLength(name)) {
			return false;
		}

		if (!name.startsWith(PREFIX)) {
			return false;
		}

		name = name.substring(PREFIX.length());

		return StrUtil.startWithAny(name,
				CRYPTO_FUN_NAME, LOCAL_IP_FUN_NAME, RANDOM_FUN_NAME);
	}

	/**
	 * 通过函数工具类，计算配置值
	 *
	 * @param result 配置代码解析结果
	 * @return 配置值
	 */
	private Object computeProperty(CodeAnalysisResult result) {
		switch (result.getVariableName()) {
			case CRYPTO_FUN_NAME:
				// 加密解密
				return CryptoPropertyUtils.getProperty(name, result);
			case LOCAL_IP_FUN_NAME:
				// 本地IP
				return LocalIpPropertyUtils.getProperty(name, result);
			case RANDOM_FUN_NAME:
				// 随机值
				return RandomPropertyUtils.getProperty(name, result);
			default:
				return null;
		}
	}

	/**
	 * 判断是否需要做缓存
	 *
	 * @param result 配置代码解析结果
	 * @return 是否需要做缓存
	 */
	private boolean isNeedCache(CodeAnalysisResult result) {
		Object[] parameters = result.getParameters();

		// 参数列表不为空 且 最后一个参数值为“no-cache”
		if (ArrayUtils.isNotEmpty(parameters)
				&& NO_CACHE_VALUE.equalsIgnoreCase(String.valueOf(parameters[parameters.length - 1]))) {
			// 移除最后一个参数：no-cache
			parameters = Arrays.copyOf(parameters, parameters.length - 1);
			result.setParameters(parameters);

			// 不需要缓存
			return false;
		} else {
			// 需要缓存
			return true;
		}
	}
}
