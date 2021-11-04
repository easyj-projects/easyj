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
package icu.easyj.spring.boot.test.result;

import java.nio.charset.Charset;

import icu.easyj.spring.boot.test.MockResponse;
import org.junit.jupiter.api.Assertions;

/**
 * 响应内容编码结果
 *
 * @author wangliang181230
 */
public class CharacterEncodingResult extends BaseResult {

	private final String characterEncoding;

	public CharacterEncodingResult(MockResponse mockResponse, String characterEncoding) {
		super(mockResponse);
		this.characterEncoding = this.toUpperCase(characterEncoding);
	}


	//region CharacterEncoding

	public MockResponse is(String expectedCharacterEncoding) {
		Assertions.assertEquals(this.toUpperCase(expectedCharacterEncoding), characterEncoding);
		return super.end();
	}

	public MockResponse is(Charset expectedCharset) {
		return is(expectedCharset.name());
	}

	//endregion


	private String toUpperCase(String str) {
		return str == null ? null : str.toUpperCase();
	}
}
