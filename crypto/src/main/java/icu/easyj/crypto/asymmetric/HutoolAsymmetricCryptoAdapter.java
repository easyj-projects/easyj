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
package icu.easyj.crypto.asymmetric;

import cn.hutool.crypto.asymmetric.AbstractAsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import org.springframework.util.Assert;

/**
 * Hutool非对称加密算法适配器
 *
 * @author wangliang181230
 */
public class HutoolAsymmetricCryptoAdapter implements IAsymmetricCrypto {

	private final AbstractAsymmetricCrypto asymmetricCrypto;

	/**
	 * 构造函数
	 *
	 * @param asymmetricCrypto Hutool非对称加密
	 */
	public HutoolAsymmetricCryptoAdapter(AbstractAsymmetricCrypto asymmetricCrypto) {
		Assert.notNull(asymmetricCrypto, "asymmetricCrypto must be not null");
		this.asymmetricCrypto = asymmetricCrypto;
	}


	//region Override

	@Override
	public byte[] encrypt(byte[] data, KeyType keyType) {
		return asymmetricCrypto.encrypt(data, keyType);
	}


	@Override
	public byte[] decrypt(byte[] bytes, KeyType keyType) {
		return asymmetricCrypto.decrypt(bytes, keyType);
	}

	//endregion
}
