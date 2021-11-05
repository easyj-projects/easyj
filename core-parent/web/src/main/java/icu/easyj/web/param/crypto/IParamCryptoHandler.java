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
package icu.easyj.web.param.crypto;

import icu.easyj.web.param.crypto.exception.ParamDecryptException;
import icu.easyj.web.param.crypto.exception.ParamEncryptException;
import org.springframework.lang.NonNull;

/**
 * 参数加密解密工具接口
 *
 * @author wangliang181230
 */
public interface IParamCryptoHandler {

	/**
	 * 校验加密串的格式，是否为当前加密算法加密后的字符串格式。
	 * <p>
	 * 举例说明：默认实现类中，加密后为base64串，则校验一下base64格式。
	 *
	 * @param encryptedParam 待解密参数
	 * @return 是否需要解密
	 */
	default boolean isEncryptedQueryString(String encryptedParam) {
		return true;
	}

	/**
	 * 加密
	 *
	 * @param param 待加密的参数
	 * @return 加密后的参数
	 * @throws ParamEncryptException 加密异常
	 */
	String encrypt(String param) throws ParamEncryptException;

	/**
	 * 解密
	 *
	 * @param encryptedParam 已加密的内容
	 * @return content 未加密的内容
	 * @throws ParamDecryptException 解密异常
	 */
	String decrypt(String encryptedParam) throws ParamDecryptException;
}
