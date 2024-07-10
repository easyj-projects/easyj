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
package icu.easyj.crypto.symmetric;

import java.io.InputStream;
import java.io.OutputStream;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Hutool对称加密算法适配器
 *
 * @author wangliang181230
 */
public class HutoolSymmetricCryptoAdapter implements ISymmetricCrypto {
	private static final long serialVersionUID = 1L;

	/**
	 * 加密算法，仅用于查看
	 */
	private final String algorithm;

	/**
	 * 对称加密算法实例
	 */
	private final SymmetricCrypto symmetricCrypto;

	/**
	 * 构造函数
	 *
	 * @param algorithm       算法
	 * @param symmetricCrypto Hutool对称加密
	 */
	public HutoolSymmetricCryptoAdapter(String algorithm, SymmetricCrypto symmetricCrypto) {
		Assert.notNull(symmetricCrypto, "'symmetricCrypto' must not be null");
		this.algorithm = algorithm;
		this.symmetricCrypto = symmetricCrypto;
	}


	/**
	 * 获取对称加密算法实例
	 *
	 * @return 对称加密算法实例
	 */
	public SymmetricCrypto getCrypto() {
		return symmetricCrypto;
	}


	//region Override

	@Override
	public String getAlgorithm() {
		return algorithm;
	}

	@Override
	public byte[] encrypt(@Nullable byte[] data) {
		if (data == null) {
			return null;
		}
		return symmetricCrypto.encrypt(data);
	}

	@Override
	public void encrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
		symmetricCrypto.encrypt(data, out, isClose);
	}

	@Override
	@Nullable
	public byte[] decrypt(@Nullable byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return symmetricCrypto.decrypt(bytes);
	}

	@Override
	public void decrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
		symmetricCrypto.decrypt(data, out, isClose);
	}

	//endregion
}
