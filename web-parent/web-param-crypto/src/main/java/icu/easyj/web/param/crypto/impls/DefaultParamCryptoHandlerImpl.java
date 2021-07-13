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
package icu.easyj.web.param.crypto.impls;

import java.nio.charset.StandardCharsets;

import icu.easyj.core.exception.ConfigurationException;
import icu.easyj.crypto.CryptoFactory;
import icu.easyj.crypto.symmetric.ISymmetricCrypto;
import icu.easyj.web.param.crypto.IParamCryptoHandler;
import icu.easyj.web.param.crypto.IParamCryptoHandlerProperties;
import org.springframework.util.StringUtils;

/**
 * 默认的参数加密解密处理器实现类
 *
 * @author wangliang181230
 */
public class DefaultParamCryptoHandlerImpl implements IParamCryptoHandler {

	/**
	 * 处理器配置
	 */
	private final IParamCryptoHandlerProperties properties;

	/**
	 * 对称加密算法实例
	 */
	private final ISymmetricCrypto symmetricCrypto;

	/**
	 * 构造函数
	 *
	 * @param properties 处理器配置
	 */
	public DefaultParamCryptoHandlerImpl(IParamCryptoHandlerProperties properties) {
		// 校验配置
		this.checkProperties(properties);
		// 设置配置
		this.properties = properties;

		// 如果编码为空，则默认为 UTF-8
		if (properties.getCharset() == null) {
			properties.setCharset(StandardCharsets.UTF_8);
		}

		// 使用工厂类，根据配置动态创建对称加密算法实例
		symmetricCrypto = CryptoFactory.getSymmetricCrypto(properties.getAlgorithm(), properties.getKey(),
				properties.getIv(), properties.getCharset());
	}

	/**
	 * 校验配置
	 *
	 * @param properties 配置
	 */
	private void checkProperties(IParamCryptoHandlerProperties properties) {
		if (!StringUtils.hasText(properties.getAlgorithm())) {
			throw new ConfigurationException("出入参加密解密算法未配置，无法创建对称加密算法实例");
		}
		if (!StringUtils.hasText(properties.getKey())) {
			throw new ConfigurationException("出入参加密解密密钥未配置，无法创建对称加密算法实例");
		}
	}


	//region Override

	@Override
	public String encrypt(String param) throws Exception {
		return symmetricCrypto.encryptBase64(param, properties.getCharset());
	}

	@Override
	public String decrypt(String encryptedParam) throws Exception {
		return symmetricCrypto.decryptBase64(encryptedParam, properties.getCharset());
	}

	//endregion
}
