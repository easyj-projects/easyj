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
package icu.easyj.jwt;

/**
 * JWT信息
 *
 * @author wangliang181230
 */
public class JwtProperties {
	private static final long serialVersionUID = 1L;

	/**
	 * JWT签名算法ID
	 */
	private String algorithmId;

	/**
	 * 密钥字符串
	 */
	private String secretKey;

	/**
	 * 与给定密钥关联的密钥算法
	 */
	private String secretKeyAlgorithm;


	//region Getter、Setter

	public String getAlgorithmId() {
		return algorithmId;
	}

	public void setAlgorithmId(String algorithmId) {
		this.algorithmId = algorithmId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getSecretKeyAlgorithm() {
		return secretKeyAlgorithm;
	}

	public void setSecretKeyAlgorithm(String secretKeyAlgorithm) {
		this.secretKeyAlgorithm = secretKeyAlgorithm;
	}

	//endregion
}
