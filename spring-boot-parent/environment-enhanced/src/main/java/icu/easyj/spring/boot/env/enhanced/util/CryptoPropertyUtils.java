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
package icu.easyj.spring.boot.env.enhanced.util;

import icu.easyj.core.code.analysis.CodeAnalysisResult;
import icu.easyj.core.exception.ConfigurationException;
import icu.easyj.crypto.GlobalCrypto;
import icu.easyj.crypto.asymmetric.IAsymmetricCrypto;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密解密函数式配置工具类
 *
 * @author wangliang181230
 */
public abstract class CryptoPropertyUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoPropertyUtils.class);

	/**
	 * 根据配置命令解析结果获取配置值
	 *
	 * @param name   配置项
	 * @param result 配置命令解析结果
	 * @return property 配置值
	 */
	public static String getProperty(String name, CodeAnalysisResult result) {
		String property = null;
		try {
			// 执行方法对应的逻辑
			Object[] parameters = result.getParameters();
			switch (result.getMethodName()) {
				case "decrypt":
					try {
						if (ArrayUtils.isEmpty(parameters) || ObjectUtils.isEmpty(parameters[0])) {
							// 没有值，直接返回空字符串
							return "";
						}

						// 获取全局非对称加密算法
						IAsymmetricCrypto crypto = GlobalCrypto.getAsymmetricCrypto();
						if (crypto == null) {
							throw new ConfigurationException(GlobalCrypto.ASYMMETRIC_ERROR_MESSAGE);
						}

						// 配置信息解密
						String encryptedProperty = parameters[0].toString();
						property = crypto.decryptStr(encryptedProperty);

						// 返回解密后的配置信息
						return property;
					} catch (RuntimeException e) {
						throw new ConfigurationException("配置信息解密失败：" + name, e);
					}
				default:
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("不支持的函数`${" + name + "}`");
					}
					return null;
			}
		} finally {
			if (property != null && LOGGER.isDebugEnabled()) {
				LOGGER.debug("函数式配置`${" + name + "}`的值: {}", property);
			}
		}
	}
}
