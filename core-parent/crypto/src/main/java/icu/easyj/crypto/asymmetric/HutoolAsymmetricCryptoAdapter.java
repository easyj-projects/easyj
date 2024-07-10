/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.crypto.asymmetric;

import cn.hutool.crypto.asymmetric.AbstractAsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Hutool非对称加密算法适配器
 *
 * @author wangliang181230
 */
public class HutoolAsymmetricCryptoAdapter implements IAsymmetricCrypto {
	private static final long serialVersionUID = 1L;

	/**
	 * 加密算法，仅用于查看
	 */
	private final String algorithm;

	/**
	 * 非对称加密算法实例
	 */
	private final AbstractAsymmetricCrypto<?> asymmetricCrypto;

	/**
	 * 构造函数
	 *
	 * @param algorithm        算法
	 * @param asymmetricCrypto Hutool非对称加密
	 * @param <T>              算法类型
	 */
	public <T extends AbstractAsymmetricCrypto<T>> HutoolAsymmetricCryptoAdapter(String algorithm, T asymmetricCrypto) {
		Assert.notNull(asymmetricCrypto, "'asymmetricCrypto' must not be null");
		this.algorithm = algorithm;
		this.asymmetricCrypto = asymmetricCrypto;
	}


	/**
	 * 获取非对称加密算法实例
	 *
	 * @return 非对称加密算法实例
	 */
	public AbstractAsymmetricCrypto<?> getCrypto() {
		return asymmetricCrypto;
	}


	//region Override

	@Override
	public String getAlgorithm() {
		return algorithm;
	}

	@Override
	@Nullable
	public byte[] encrypt(@Nullable byte[] data) {
		if (data == null) {
			return null;
		}
		return asymmetricCrypto.encrypt(data, KeyType.PublicKey);
	}

	@Override
	@Nullable
	public byte[] decrypt(@Nullable byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return asymmetricCrypto.decrypt(bytes, KeyType.PrivateKey);
	}

	//endregion
}
