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
package icu.easyj.web.param.crypto.impls;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import icu.easyj.web.param.crypto.IParamCryptoHandlerProperties;

/**
 * 默认的参数加密处理器配置
 *
 * @author wangliang181230
 */
public class DefaultParamCryptoHandlerPropertiesImpl implements IParamCryptoHandlerProperties {

	/**
	 * 加密解密方式
	 */
	private String algorithm;

	/**
	 * 密钥
	 */
	private String key;

	/**
	 * 偏移向量
	 */
	private String iv;

	/**
	 * 编码
	 */
	private Charset charset = StandardCharsets.UTF_8;

	/**
	 * 入参是否需要加密解密
	 */
	private volatile boolean needEncryptInputParam = true;

	/**
	 * 出参是否需要加密
	 */
	private volatile boolean needEncryptOutputParam = true;


	//region Getter、Setter、Override

	@Override
	public String getAlgorithm() {
		return algorithm;
	}

	@Override
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String getIv() {
		return iv;
	}

	@Override
	public void setIv(String iv) {
		this.iv = iv;
	}

	@Override
	public Charset getCharset() {
		return charset;
	}

	@Override
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	@Override
	public boolean isNeedEncryptInputParam() {
		return needEncryptInputParam;
	}

	@Override
	public void setNeedEncryptInputParam(boolean needEncryptInputParam) {
		this.needEncryptInputParam = needEncryptInputParam;
	}

	@Override
	public boolean isNeedEncryptOutputParam() {
		return needEncryptOutputParam;
	}

	@Override
	public void setNeedEncryptOutputParam(boolean needEncryptOutputParam) {
		this.needEncryptOutputParam = needEncryptOutputParam;
	}

	//endregion
}