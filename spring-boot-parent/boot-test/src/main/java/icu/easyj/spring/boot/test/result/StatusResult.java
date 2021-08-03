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

import icu.easyj.spring.boot.test.MockResponse;
import icu.easyj.spring.boot.test.util.AssertionUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

/**
 * 响应状态结果
 *
 * @author wangliang181230
 */
public class StatusResult extends BaseResult {

	private final HttpStatus status;

	public StatusResult(MockResponse mockResponse, int status) {
		super(mockResponse);
		this.status = HttpStatus.valueOf(status);
	}


	//region HttpStatus.value 响应状态值

	public MockResponse is(int expectedStatusValue) {
		Assertions.assertEquals(expectedStatusValue, this.status.value());
		return super.end();
	}

	public MockResponse is(int... expectedStatusValueArr) {
		for (int expectedStatusValue : expectedStatusValueArr) {
			if (expectedStatusValue == this.status.value()) {
				return super.end();
			}
		}
		throw AssertionUtils.error("响应状态不正确。", expectedStatusValueArr, this.status.value());
	}

	public MockResponse is(HttpStatus expectedStatus) {
		Assertions.assertEquals(expectedStatus, this.status);
		return super.end();
	}

	public MockResponse is(HttpStatus... expectedStatusArr) {
		for (HttpStatus expectedStatus : expectedStatusArr) {
			if (expectedStatus == this.status) {
				return super.end();
			}
		}
		throw AssertionUtils.error("响应状态不正确。", expectedStatusArr, this.status.value());
	}

	public MockResponse isOk() {
		return this.is(HttpStatus.OK);
	}

	//endregion


	//region HttpStatus.series 响应状态系列

	/**
	 * 判断响应状态系列
	 *
	 * @param expectedStatusSeries 预期系列
	 * @return mockResponse
	 */
	public MockResponse isSeries(HttpStatus.Series expectedStatusSeries) {
		Assertions.assertEquals(expectedStatusSeries, this.status.series());
		return super.end();
	}

	/**
	 * 判断响应状态系列
	 *
	 * @param expectedStatusSeriesArr 预期系列数组
	 * @return mockResponse
	 */
	public MockResponse isSeries(HttpStatus.Series... expectedStatusSeriesArr) {
		for (HttpStatus.Series expectedStatusSeries : expectedStatusSeriesArr) {
			if (expectedStatusSeries == this.status.series()) {
				return super.end();
			}
		}
		throw AssertionUtils.error("响应状态系列不正确。", expectedStatusSeriesArr, this.status.series());
	}

	/**
	 * 判断响应状态为 1xx 系列
	 *
	 * @return mockResponse
	 */
	public MockResponse isInformationalSeries() {
		return isSeries(HttpStatus.Series.INFORMATIONAL);
	}

	/**
	 * 判断响应状态为 2xx 系列
	 *
	 * @return mockResponse
	 */
	public MockResponse isSuccessfulSeries() {
		return isSeries(HttpStatus.Series.SUCCESSFUL);
	}

	/**
	 * 判断响应状态为 3xx 系列
	 *
	 * @return mockResponse
	 */
	public MockResponse isRedirectionSeries() {
		return isSeries(HttpStatus.Series.REDIRECTION);
	}

	/**
	 * 判断响应状态为 4xx 系列
	 *
	 * @return mockResponse
	 */
	public MockResponse isClientErrorSeries() {
		return isSeries(HttpStatus.Series.CLIENT_ERROR);
	}

	/**
	 * 判断响应状态为 5xx 系列
	 *
	 * @return mockResponse
	 */
	public MockResponse isServerErrorSeries() {
		return isSeries(HttpStatus.Series.SERVER_ERROR);
	}

	//endregion
}
