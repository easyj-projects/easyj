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
package icu.easyj.crypto;

/**
 * 全局加密算法
 *
 * @author wangliang181230
 */
public abstract class GlobalCrypto {

	public static final String ERROR_MESSAGE = "全局加密算法为空，请先配置`easyj.global.crypto.*`中的必须配置项。";

	private static ICrypto crypto;

	public static ICrypto getCrypto() {
		return crypto;
	}

	public static void setCrypto(ICrypto crypto) {
		GlobalCrypto.crypto = crypto;
	}
}