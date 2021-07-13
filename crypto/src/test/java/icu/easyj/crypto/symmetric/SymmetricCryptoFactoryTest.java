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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link SymmetricCryptoFactory} 测试类
 *
 * @author wangliang181230
 */
class SymmetricCryptoFactoryTest {

	@Test
	void testGetSymmetricCrypto() {
		String algorithmStr = "AES/CBC/PKCS7Padding";
		String key = "12345678901234567890123456789012";
		String iv = "1234567890123456";
		Charset charset = StandardCharsets.UTF_8;

		ISymmetricCrypto symmetricCrypto = SymmetricCryptoFactory.getSymmetricCrypto(algorithmStr, key, iv, charset);

		String data = "111222333";
		String base64 = symmetricCrypto.encryptBase64(data, charset);
		String data2 = symmetricCrypto.decryptBase64(base64, charset);
		Assertions.assertEquals(data, data2);
	}
}
