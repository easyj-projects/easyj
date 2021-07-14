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
package icu.easyj.crypto;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cn.hutool.core.text.CharPool;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import icu.easyj.crypto.symmetric.HutoolSymmetricCryptoAdapter;
import icu.easyj.crypto.symmetric.ISymmetricCrypto;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 加密算法工厂类
 *
 * @author wangliang181230
 * @see ISymmetricCrypto 对称加密算法接口
 * @see SymmetricCrypto Hutool的对称加密算法实现
 */
public abstract class CryptoFactory {

	//region 对称加密算法

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithm 算法字符串，格式：{Algorithm}/{Mode}/{Padding}
	 * @param key       密钥
	 * @param iv        偏移向量
	 * @param charset   编码
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(@NonNull String algorithm, @NonNull String key, String iv, Charset charset) {
		Assert.notNull(algorithm, "algorithmStr must be not null");
		Assert.notNull(key, "key must be not null");

		if (charset == null) {
			charset = StandardCharsets.UTF_8;
		}

		byte[] keyBytes = key.getBytes(charset);
		byte[] ivBytes = (StringUtils.hasLength(iv) ? iv.getBytes(charset) : null);

		return getSymmetricCrypto(algorithm, keyBytes, ivBytes);
	}

	/**
	 * 生成对称加密算法，使用UTF-8编码
	 *
	 * @param algorithm 算法字符串，格式：{Algorithm}/{Mode}/{Padding}
	 * @param key       密钥
	 * @param iv        偏移向量
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(@NonNull String algorithm, @NonNull String key, String iv) {
		return getSymmetricCrypto(algorithm, key, iv, StandardCharsets.UTF_8);
	}

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithm 算法字符串，格式：{Algorithm}/{Mode}/{Padding}
	 * @param key       密钥
	 * @param iv        偏移向量
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(@NonNull String algorithm, @NonNull byte[] key, byte[] iv) {
		Assert.notNull(algorithm, "algorithmStr must be not null");
		Assert.notNull(key, "key must be not null");

		// 截取算法类型
		String algorithmType = algorithm.substring(0, algorithm.indexOf(CharPool.SLASH));

		// 生成密钥
		SecretKey secretKey = SecureUtil.generateKey(algorithmType, key);
		// 生成算法参数
		AlgorithmParameterSpec parameterSpec = (ArrayUtils.isNotEmpty(iv) ? new IvParameterSpec(iv) : null);

		// 实例化Hutool的对称加密实例
		SymmetricCrypto symmetricCrypto = new SymmetricCrypto(algorithm, secretKey, parameterSpec);

		// 创建适配器
		return new HutoolSymmetricCryptoAdapter(symmetricCrypto);
	}

	//endregion


	//region 非对称加密算法

	// TODO: 待开发

	//endregion
}
