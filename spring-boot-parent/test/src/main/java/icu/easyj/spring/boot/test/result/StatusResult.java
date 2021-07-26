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

import icu.easyj.spring.boot.test.EasyjMockResponse;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import org.springframework.http.HttpStatus;

/**
 * 响应状态结果
 *
 * @author wangliang181230
 */
public class StatusResult extends BaseResult {

	private final HttpStatus status;

	public StatusResult(EasyjMockResponse mockResponse, int status) {
		super(mockResponse);
		this.status = HttpStatus.valueOf(status);
	}


	//region HttpStatus.value 响应状态值

	public EasyjMockResponse is(int expectedStatusValue) {
		Assertions.assertEquals(expectedStatusValue, this.status.value());
		return super.end();
	}

	public EasyjMockResponse is(int... expectedStatusValueArr) {
		for (int expectedStatusValue : expectedStatusValueArr) {
			if (expectedStatusValue == this.status.value()) {
				return super.end();
			}
		}
		throw new AssertionFailedError("响应状态不在预期。", expectedStatusValueArr, this.status.value());
	}

	public EasyjMockResponse is(HttpStatus expectedStatus) {
		Assertions.assertEquals(expectedStatus, this.status);
		return super.end();
	}

	public EasyjMockResponse is(HttpStatus... expectedStatusArr) {
		for (HttpStatus expectedStatus : expectedStatusArr) {
			if (expectedStatus == this.status) {
				return super.end();
			}
		}
		throw new AssertionFailedError("响应状态不在预期。", expectedStatusArr, this.status.value());
	}

	public EasyjMockResponse isOk() {
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
	public EasyjMockResponse isSeries(HttpStatus.Series expectedStatusSeries) {
		Assertions.assertEquals(expectedStatusSeries, this.status.series());
		return super.end();
	}

	/**
	 * 判断响应状态系列
	 *
	 * @param expectedStatusSeriesArr 预期系列数组
	 * @return mockResponse
	 */
	public EasyjMockResponse isSeries(HttpStatus.Series... expectedStatusSeriesArr) {
		for (HttpStatus.Series expectedStatusSeries : expectedStatusSeriesArr) {
			if (expectedStatusSeries == this.status.series()) {
				return super.end();
			}
		}
		throw new AssertionFailedError("响应状态系列不在预期。", expectedStatusSeriesArr, this.status.series());
	}

	/**
	 * 判断响应状态为 1xx 系列
	 *
	 * @return mockResponse
	 */
	public EasyjMockResponse isInformationalSeries() {
		return isSeries(HttpStatus.Series.INFORMATIONAL);
	}

	/**
	 * 判断响应状态为 2xx 系列
	 *
	 * @return mockResponse
	 */
	public EasyjMockResponse isSuccessfulSeries() {
		return isSeries(HttpStatus.Series.SUCCESSFUL);
	}

	/**
	 * 判断响应状态为 3xx 系列
	 *
	 * @return mockResponse
	 */
	public EasyjMockResponse isRedirectionSeries() {
		return isSeries(HttpStatus.Series.REDIRECTION);
	}

	/**
	 * 判断响应状态为 4xx 系列
	 *
	 * @return mockResponse
	 */
	public EasyjMockResponse isClientErrorSeries() {
		return isSeries(HttpStatus.Series.CLIENT_ERROR);
	}

	/**
	 * 判断响应状态为 5xx 系列
	 *
	 * @return mockResponse
	 */
	public EasyjMockResponse isServerErrorSeries() {
		return isSeries(HttpStatus.Series.SERVER_ERROR);
	}

	//endregion
}
