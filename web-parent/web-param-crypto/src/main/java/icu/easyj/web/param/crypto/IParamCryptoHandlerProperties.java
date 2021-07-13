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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 参数加密解密配置接口
 *
 * @author wangliang181230
 */
public interface IParamCryptoHandlerProperties {

	/**
	 * 对称加密算法<br>
	 * 格式如：{对称加密算法}/{模式}/{补码方式}<br>
	 * 举例：AES/CBC/PKCS7Padding
	 *
	 * @return algorithm 对称加密算法
	 */
	String getAlgorithm();

	/**
	 * 设置对称加密算法
	 *
	 * @param algorithm 对称加密算法
	 */
	void setAlgorithm(String algorithm);

	/**
	 * @return key 密钥
	 */
	String getKey();

	/**
	 * 设置密钥
	 *
	 * @param key 密钥
	 */
	void setKey(String key);

	/**
	 * @return iv 加盐
	 */
	String getIv();

	/**
	 * 设置加盐串
	 *
	 * @param iv 加盐串
	 */
	void setIv(String iv);

	/**
	 * @return 编码
	 */
	default Charset getCharset() {
		return StandardCharsets.UTF_8;
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 */
	void setCharset(Charset charset);

	/**
	 * @return 入参是否需要解密
	 */
	boolean isNeedEncryptInputParam();

	/**
	 * 设置入参是否需要解密
	 *
	 * @param needEncryptInputParam 入参是否需要解密
	 */
	void setNeedEncryptInputParam(boolean needEncryptInputParam);

	/**
	 * @return 出参是否需要加密
	 */
	boolean isNeedEncryptOutputParam();

	/**
	 * 设置出参是否需要加密
	 *
	 * @param needEncryptOutputParam 出参是否需要加密
	 */
	void setNeedEncryptOutputParam(boolean needEncryptOutputParam);
}
