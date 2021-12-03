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
package icu.easyj.crypto.impls;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;

import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.crypto.ICryptoGenerator;
import icu.easyj.crypto.asymmetric.HutoolAsymmetricCryptoAdapter;
import icu.easyj.crypto.asymmetric.IAsymmetricCrypto;
import icu.easyj.crypto.symmetric.HutoolSymmetricCryptoAdapter;
import icu.easyj.crypto.symmetric.ISymmetricCrypto;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import static icu.easyj.core.loader.ServiceProviders.HUTOOL;

/**
 * 基于Hutool的加密算法生成器
 *
 * @author wangliang181230
 */
@LoadLevel(name = HUTOOL, order = 100)
public class HutoolCryptoGeneratorImpl implements ICryptoGenerator {

	/**
	 * 生成对称加密算法
	 *
	 * @param algorithm              算法
	 * @param secretKey              密钥
	 * @param algorithmParameterSpec 算法参数
	 * @return symmetricCrypto 对称加密算法
	 */
	@Override
	public ISymmetricCrypto getSymmetricCrypto(@NonNull String algorithm, @NonNull SecretKey secretKey,
											   @Nullable AlgorithmParameterSpec algorithmParameterSpec) {
		// 实例化Hutool的对称加密实例
		SymmetricCrypto symmetricCrypto = new SymmetricCrypto(algorithm, secretKey, algorithmParameterSpec);
		// 创建适配器
		return new HutoolSymmetricCryptoAdapter(algorithm, symmetricCrypto);
	}


	/**
	 * 生成非对称加密算法
	 *
	 * @param algorithm  算法
	 * @param publicKey  公钥
	 * @param privateKey 私钥
	 * @return asymmetricCrypto 非对称加密算法
	 */
	@Override
	public IAsymmetricCrypto getAsymmetricCrypto(@NonNull String algorithm,
												 @NonNull PublicKey publicKey, @NonNull PrivateKey privateKey) {
		Assert.notNull(algorithm, "'algorithm' must not be null");
		Assert.notNull(publicKey, "'publicKey' must not be null");
		Assert.notNull(privateKey, "'privateKey' must not be null");

		// 实例化Hutool的对称加密实例
		AsymmetricCrypto asymmetricCrypto = new AsymmetricCrypto(algorithm, privateKey, publicKey);
		// 创建适配器
		return new HutoolAsymmetricCryptoAdapter(algorithm, asymmetricCrypto);
	}
}
