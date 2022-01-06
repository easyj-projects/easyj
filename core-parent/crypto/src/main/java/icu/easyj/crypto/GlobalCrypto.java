/*
 * Copyright 2021-2022 the original author or authors.
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

import icu.easyj.crypto.asymmetric.IAsymmetricCrypto;
import icu.easyj.crypto.symmetric.ISymmetricCrypto;

/**
 * 全局加密算法
 *
 * @author wangliang181230
 */
public abstract class GlobalCrypto {

	//region 非对称加密算法

	public static final String ASYMMETRIC_ERROR_MESSAGE = "全局非对称加密算法为空，请先配置`easyj.global.asymmetric-crypto.*`中必须的配置项。";

	private static IAsymmetricCrypto asymmetricCrypto;

	public static IAsymmetricCrypto getAsymmetricCrypto() {
		return asymmetricCrypto;
	}

	public static void setAsymmetricCrypto(IAsymmetricCrypto asymmetricCrypto) {
		GlobalCrypto.asymmetricCrypto = asymmetricCrypto;
	}

	//endregion


	//region 对称加密算法

	public static final String SYMMETRIC_ERROR_MESSAGE = "全局对称加密算法为空，请先配置`easyj.global.symmetric-crypto.*`中必须的配置项。";

	private static ISymmetricCrypto symmetricCrypto;

	public static ISymmetricCrypto getSymmetricCrypto() {
		return symmetricCrypto;
	}

	public static void setSymmetricCrypto(ISymmetricCrypto symmetricCrypto) {
		GlobalCrypto.symmetricCrypto = symmetricCrypto;
	}

	//endregion
}
