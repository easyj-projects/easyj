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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.crypto.asymmetric.IAsymmetricCrypto;
import icu.easyj.crypto.symmetric.ISymmetricCrypto;

/**
 * 加密算法工厂类
 *
 * @author wangliang181230
 * @see ISymmetricCrypto 对称加密算法接口
 * @see cn.hutool.crypto.symmetric.SymmetricCrypto Hutool的对称加密算法实现
 * @see cn.hutool.crypto.asymmetric.AsymmetricCrypto Hutool的非对称加密算法实现
 */
public abstract class CryptoFactory {

	//region 加密算法生成器单例持有者（设计模式-创建型模式-单例模式-枚举实现单例）

	private enum CryptoGeneratorSingletonHolder {
		// 单例
		INSTANCE;

		private final ICryptoGenerator instance = EnhancedServiceLoader.load(ICryptoGenerator.class);

		public ICryptoGenerator getInstance() {
			return INSTANCE.instance;
		}
	}

	/**
	 * 获取加密算法生成器
	 *
	 * @return 加密算法生成器
	 */
	public static ICryptoGenerator getGenerator() {
		return CryptoGeneratorSingletonHolder.INSTANCE.getInstance();
	}

	//endregion


	//region 生成对称加密算法

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithm              算法
	 * @param secretKey              密钥
	 * @param algorithmParameterSpec 算法参数
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(String algorithm, SecretKey secretKey,
													  AlgorithmParameterSpec algorithmParameterSpec) {
		return getGenerator().getSymmetricCrypto(algorithm, secretKey, algorithmParameterSpec);
	}

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param iv        偏移向量
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(String algorithm, byte[] key, byte[] iv) {
		return getGenerator().getSymmetricCrypto(algorithm, key, iv);
	}

	/**
	 * 生成对称加密算法，使用UTF-8编码
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param iv        偏移向量
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(String algorithm, String key, String iv) {
		return getGenerator().getSymmetricCrypto(algorithm, key, iv);
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
	public static ISymmetricCrypto getSymmetricCrypto(String algorithm, String key, String iv, Charset charset) {
		return getGenerator().getSymmetricCrypto(algorithm, key, iv, charset);
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
	public IAsymmetricCrypto getAsymmetricCrypto(String algorithm,
												 PublicKey publicKey, PrivateKey privateKey) {
		return getGenerator().getAsymmetricCrypto(algorithm, publicKey, privateKey);
	}

	/**
	 * 生成非对称加密算法
	 *
	 * @param algorithm  算法
	 * @param publicKey  公钥
	 * @param privateKey 私钥
	 * @return asymmetricCrypto 非对称加密算法
	 */
	public static IAsymmetricCrypto getAsymmetricCrypto(String algorithm, byte[] publicKey, byte[] privateKey) {
		return getGenerator().getAsymmetricCrypto(algorithm, publicKey, privateKey);
	}

	/**
	 * 生成非对称加密算法
	 *
	 * @param algorithm     算法
	 * @param publicKeyStr  公钥Hex或Base64表示
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @return asymmetricCrypto 非对称加密算法
	 */
	public static IAsymmetricCrypto getAsymmetricCrypto(String algorithm, String publicKeyStr, String privateKeyStr) {
		return getGenerator().getAsymmetricCrypto(algorithm, publicKeyStr, privateKeyStr);
	}

	//endregion
}
