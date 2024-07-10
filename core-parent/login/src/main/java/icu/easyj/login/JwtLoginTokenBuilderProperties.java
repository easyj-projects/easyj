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
package icu.easyj.login;

/**
 * 基于JWT构建登录token的构建器配置类
 *
 * @author wangliang181230
 */
public class JwtLoginTokenBuilderProperties {

	/**
	 * JWT类型，可选：hutool、jjwt
	 */
	private String jwtType = "hutool";

	/**
	 * JWT签名算法ID
	 */
	private String algorithmId = "HS256";

	/**
	 * 密钥
	 */
	private String secretKey;

	/**
	 * 密钥算法
	 */
	private String secretKeyAlgorithm = "AES";


	//region Getter、Setter

	public String getJwtType() {
		return jwtType;
	}

	public void setJwtType(String jwtType) {
		this.jwtType = jwtType;
	}

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
