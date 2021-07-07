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

import java.io.InputStream;
import java.io.OutputStream;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.util.Assert;

/**
 * Hutool对称加密算法适配器
 *
 * @author wangliang181230
 */
public class HutoolSymmetricCryptoAdapter implements ISymmetricCrypto {

	private final SymmetricCrypto symmetricCrypto;

	/**
	 * 构造函数
	 *
	 * @param symmetricCrypto Hutool对称加密
	 */
	public HutoolSymmetricCryptoAdapter(SymmetricCrypto symmetricCrypto) {
		Assert.notNull(symmetricCrypto, "symmetricCrypto must be not null");
		this.symmetricCrypto = symmetricCrypto;
	}


	@Override
	public byte[] encrypt(byte[] data) {
		return symmetricCrypto.encrypt(data);
	}

	@Override
	public void encrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
		symmetricCrypto.encrypt(data, out, isClose);
	}


	@Override
	public byte[] decrypt(byte[] bytes) {
		return symmetricCrypto.decrypt(bytes);
	}

	@Override
	public void decrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
		symmetricCrypto.decrypt(data, out, isClose);
	}

	//endregion
}