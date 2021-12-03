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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cn.hutool.core.text.CharPool;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import icu.easyj.core.util.StringUtils;
import icu.easyj.crypto.asymmetric.IAsymmetricCrypto;
import icu.easyj.crypto.symmetric.ISymmetricCrypto;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 加密算法实例生成器接口
 *
 * @author wangliang181230
 */
public interface ICryptoGenerator {

	//region 生成对称加密算法

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithm              算法
	 * @param secretKey              密钥
	 * @param algorithmParameterSpec 算法参数
	 * @return symmetricCrypto 对称加密算法
	 */
	ISymmetricCrypto getSymmetricCrypto(@NonNull String algorithm, @NonNull SecretKey secretKey,
										@Nullable AlgorithmParameterSpec algorithmParameterSpec);

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param iv        偏移向量
	 * @return symmetricCrypto 对称加密算法
	 */
	default ISymmetricCrypto getSymmetricCrypto(@NonNull String algorithm, @NonNull byte[] key, byte[] iv) {
		Assert.notNull(algorithm, "'algorithmStr' must not be null");
		Assert.notNull(key, "'key' must not be null");

		// 截取算法类型
		String algorithmType = algorithm.substring(0, algorithm.indexOf(CharPool.SLASH));

		// 生成密钥
		SecretKey secretKey = SecureUtil.generateKey(algorithmType, key);
		// 生成算法参数
		AlgorithmParameterSpec parameterSpec = (ArrayUtils.isNotEmpty(iv) ? new IvParameterSpec(iv) : null);

		// 生成加密算法实例
		return getSymmetricCrypto(algorithm, secretKey, parameterSpec);
	}

	/**
	 * 生成对称加密算法，使用UTF-8编码
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param iv        偏移向量
	 * @return symmetricCrypto 对称加密算法
	 */
	default ISymmetricCrypto getSymmetricCrypto(@NonNull String algorithm, @NonNull String key, @Nullable String iv) {
		return getSymmetricCrypto(algorithm, key, iv, StandardCharsets.UTF_8);
	}

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param iv        偏移向量
	 * @param charset   编码
	 * @return symmetricCrypto 对称加密算法
	 */
	default ISymmetricCrypto getSymmetricCrypto(@NonNull String algorithm, @NonNull String key, String iv, Charset charset) {
		Assert.notNull(algorithm, "'algorithmStr' must not be null");
		Assert.notNull(key, "'key' must not be null");

		if (charset == null) {
			charset = StandardCharsets.UTF_8;
		}

		byte[] keyBytes = key.getBytes(charset);
		byte[] ivBytes = (StringUtils.isNotEmpty(iv) ? iv.getBytes(charset) : null);

		return getSymmetricCrypto(algorithm, keyBytes, ivBytes);
	}

	//endregion


	//region 生成非对称加密算法

	/**
	 * 生成非对称加密算法
	 *
	 * @param algorithm  算法
	 * @param publicKey  公钥
	 * @param privateKey 私钥
	 * @return asymmetricCrypto 非对称加密算法
	 */
	IAsymmetricCrypto getAsymmetricCrypto(@NonNull String algorithm,
										  @NonNull PublicKey publicKey, @NonNull PrivateKey privateKey);

	/**
	 * 生成非对称加密算法
	 *
	 * @param algorithm  算法
	 * @param publicKey  公钥
	 * @param privateKey 私钥
	 * @return asymmetricCrypto 非对称加密算法
	 */
	default IAsymmetricCrypto getAsymmetricCrypto(String algorithm, byte[] publicKey, byte[] privateKey) {
		return getAsymmetricCrypto(algorithm,
				KeyUtil.generatePublicKey(algorithm, publicKey),
				KeyUtil.generatePrivateKey(algorithm, privateKey)
		);
	}

	/**
	 * 生成非对称加密算法
	 *
	 * @param algorithm     算法
	 * @param publicKeyStr  公钥Hex或Base64表示
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @return asymmetricCrypto 非对称加密算法
	 */
	default IAsymmetricCrypto getAsymmetricCrypto(String algorithm, String publicKeyStr, String privateKeyStr) {
		return getAsymmetricCrypto(algorithm, SecureUtil.decode(publicKeyStr), SecureUtil.decode(privateKeyStr));
	}

	//endregion
}
