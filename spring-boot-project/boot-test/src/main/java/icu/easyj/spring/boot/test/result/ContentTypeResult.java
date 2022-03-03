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
package icu.easyj.spring.boot.test.result;

import icu.easyj.spring.boot.test.MockResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

/**
 * 响应内容类型结果
 *
 * @author wangliang181230
 */
public class ContentTypeResult extends BaseResult {

	private final String contentType;

	public ContentTypeResult(MockResponse mockResponse, String contentType) {
		super(mockResponse);
		this.contentType = contentType.toLowerCase();
	}

	public String get() {
		return contentType;
	}


	//region ContentType

	public MockResponse is(String expectedContentType) {
		Assertions.assertTrue(contentType.startsWith(expectedContentType));
		return super.end();
	}

	//endregion


	//region MediaType

	public MockResponse is(String expectedMediaType, String expectedMediaSubtype) {
		return is(expectedMediaType + "/" + expectedMediaSubtype);
	}

	public MockResponse is(MediaType expectedMediaType) {
		return is(expectedMediaType.getType(), expectedMediaType.getSubtype());
	}

	//endregion
}
