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

import java.security.Key;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

/**
 * JWT工具类
 *
 * @author wangliang181230
 */
public abstract class SecretKeyUtils {

	/**
	 * 生成密钥
	 *
	 * @param secretKey          密钥串
	 * @param secretKeyAlgorithm 与密钥关联的算法名称
	 * @return 返回Key
	 */
	public static Key generate(String secretKey, String secretKeyAlgorithm) {
		byte[] encodedKey = Base64.getDecoder().decode(secretKey);
		return new SecretKeySpec(encodedKey, 0, encodedKey.length, secretKeyAlgorithm);
	}
}
