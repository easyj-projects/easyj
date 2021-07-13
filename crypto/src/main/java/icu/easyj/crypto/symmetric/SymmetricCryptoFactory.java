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
package icu.easyj.crypto.symmetric;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cn.hutool.core.text.CharPool;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.util.Assert;

/**
 * 对称加密算法工厂类
 *
 * @author wangliang181230
 * @see ISymmetricCrypto 对称加密算法
 */
public abstract class SymmetricCryptoFactory {

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithmStr 算法字符串，格式：{Algorithm}/{Mode}/{Padding}
	 * @param key          密钥
	 * @param iv           加盐
	 * @param charset      编码
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(String algorithmStr, String key, String iv, Charset charset) {
		Assert.notNull(algorithmStr, "algorithmStr must be not null");
		Assert.notNull(key, "key must be not null");
		Assert.notNull(charset, "charset must be not null");

		byte[] keyBytes = key.getBytes(charset);
		byte[] ivBytes = (iv == null ? null : iv.getBytes(charset));

		return getSymmetricCrypto(algorithmStr, keyBytes, ivBytes);
	}

	/**
	 * 生成对称加密算法，使用UTF-8编码
	 *
	 * @param algorithmStr 算法字符串，格式：{Algorithm}/{Mode}/{Padding}
	 * @param key          密钥
	 * @param iv           加盐
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(String algorithmStr, String key, String iv) {
		return getSymmetricCrypto(algorithmStr, key, iv, StandardCharsets.UTF_8);
	}

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithmStr 算法字符串，格式：{Algorithm}/{Mode}/{Padding}
	 * @param key          密钥
	 * @param iv           加盐
	 * @return symmetricCrypto 对称加密算法
	 */
	public static ISymmetricCrypto getSymmetricCrypto(String algorithmStr, byte[] key, byte[] iv) {
		Assert.notNull(algorithmStr, "algorithmStr must be not null");
		Assert.notNull(key, "key must be not null");

		String algorithm = algorithmStr.substring(0, algorithmStr.indexOf(CharPool.SLASH));
		SecretKey secretKey = SecureUtil.generateKey(algorithm, key);
		AlgorithmParameterSpec parameterSpec = (iv == null ? null : new IvParameterSpec(iv));

		SymmetricCrypto symmetricCrypto = new SymmetricCrypto(algorithmStr, secretKey, parameterSpec);
		return new HutoolSymmetricCryptoAdapter(symmetricCrypto);
	}
}
